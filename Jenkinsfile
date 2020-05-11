
node {
  try {
      
    stage('git checkout'){
        git credentialsId: 'git-creds', url: 'https://github.com/mechdeveloper/TCSDevOpsBootCamp.git'
    }
    stage('clean up'){
       def mavenHome = tool name: 'maven3', type: 'maven'
       def mavenCMD = "${mavenHome}/bin/mvn"
       sh "${mavenCMD} clean"
    }
    stage('build, test and package'){
       def mavenHome = tool name: 'maven3', type: 'maven'
       def mavenCMD = "${mavenHome}/bin/mvn"
       sh "${mavenCMD} package"
    }
    stage('SonarQube analysis'){
        withSonarQubeEnv(){
           def mavenHome = tool name: 'maven3', type: 'maven'
           def mavenCMD = "${mavenHome}/bin/mvn"
           sh "${mavenCMD} sonar:sonar"
        }
    }
	stage('build docker image'){
	    withCredentials([usernamePassword(credentialsId: 'docker-creds', passwordVariable: 'dockerHubPwd', usernameVariable: 'dokcerHubUser')]) {
            sh "docker build -t ${dokcerHubUser}/devops-e2-casestudy:${env.BUILD_ID} ."
        }
    }
    stage('push docker image to dockerhub') {
        withCredentials([usernamePassword(credentialsId: 'docker-creds', passwordVariable: 'dockerHubPwd', usernameVariable: 'dokcerHubUser')]) {
            sh "docker login -u ${dokcerHubUser} -p ${dockerHubPwd}"
            sh "docker push ${dokcerHubUser}/devops-e2-casestudy:${env.BUILD_ID}"
        }
        
    }

    // Run Ansible playbook here
    stage ('Ansible create EC2 Instance'){
        withCredentials([string(credentialsId: 'AWS_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY_ID'), string(credentialsId: 'AWS_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_ACCESS_KEY')]) {
            def ansibleCMD = "ansible-playbook task.yml"
            sh "docker run --rm -v \$(pwd):/ansible/playbooks --env AWS_ACCESS_KEY_ID=\${AWS_ACCESS_KEY_ID} --env AWS_SECRET_ACCESS_KEY=\${AWS_SECRET_ACCESS_KEY}  ashishbagheldocker/my-ansible:ubuntu-18.04 -c '${ansibleCMD}'"
        }
    }

    // Get IP Address of running instance
    def output = ""
    withCredentials([string(credentialsId: 'AWS_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY_ID'), string(credentialsId: 'AWS_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_ACCESS_KEY')]) {
    def awsclicmd = "aws ec2 describe-instances --region us-east-2 --query 'Reservations[].Instances[].PublicIpAddress'"
    def command = "docker run --rm --env AWS_ACCESS_KEY_ID=\${AWS_ACCESS_KEY_ID} --env AWS_SECRET_ACCESS_KEY=\${AWS_SECRET_ACCESS_KEY} ashishbagheldocker/my-ansible:ubuntu-18.04 -c '${awsclicmd}'"
    output = sh script : "${command}", returnStdout:true
    }
    print output
    def myIp = output.split('"')
    def ipAddress = myIp[1]
    print ipAddress

    // Requires SSH Agent Jenkins plugin
    stage('Installing Docker on EC2'){
        def dockerCMD = "sudo yum install docker -y"
        sshagent(['devops-ec2-key']) {
            sh "ssh -o StrictHostKeyChecking=no ec2-user@${ipAddress} ${dockerCMD}"
        }
    }
    stage('starting docker engine service on EC2 Instance'){
        def dockerCMD = "sudo service docker start"
        sshagent(['devops-ec2-key']) {
            sh "ssh -o StrictHostKeyChecking=no ec2-user@${ipAddress} ${dockerCMD}"
        }
    }
    
    // Check if swarm mode active 
    def isSwarm = ""
    sshagent(['devops-ec2-key']) {
        def swarmCheck = "sudo docker info | grep Swarm | sed 's/Swarm: //g'"
        isSwarm = sh script : "ssh -o StrictHostKeyChecking=no ec2-user@${ipAddress} ${swarmCheck}", returnStdout:true
    }
    print isSwarm
    print isSwarm.trim();

    if (isSwarm.trim() == 'inactive') {
        echo "ec2 swarm inactive"
        stage('Initialize docker swarm on EC2 Instance'){
            def dockerCMD = "sudo docker swarm init"
            sshagent(['devops-ec2-key']) {
               sh "ssh -o StrictHostKeyChecking=no ec2-user@${ipAddress} ${dockerCMD}"
            }
        }
    }else{
        echo "ec2 swarm active"
    }
    
    stage('Copy docker-compose.yml to EC2 Instance'){
        sshagent(['devops-ec2-key']) {
            sh "scp -o StrictHostKeyChecking=no docker-compose.yml ec2-user@${ipAddress}:"
        }
    }
    
    stage('Run the docker image in EC2 Instance'){
        
        withCredentials([usernamePassword(credentialsId: 'docker-creds', passwordVariable: 'dockerHubPwd', usernameVariable: 'dokcerHubUser')]) {
            // def dockerCMD = "sudo docker run -d --name=myapp -p 80:8885 ashishbagheldocker/devops-e2-casestudy:${env.BUILD_ID}"
            def IMAGE_NAME = "${dokcerHubUser}/devops-e2-casestudy:${env.BUILD_ID}"
            def dockerCMD = "sudo env IMAGE_NAME=${IMAGE_NAME} docker stack deploy -c docker-compose.yml mystack"
            sshagent(['devops-ec2-key']) {
                sh "ssh -o StrictHostKeyChecking=no ec2-user@${ipAddress} ${dockerCMD}"
            }
        }
    }
    
  } catch (e) {
    /* ... Catches exceptions and triggers the email notification for failed builds ... */
     currentBuild.result = "FAILED"
     notifyFailed()
     throw e
  }

}

/* ... email notification for failed builds ... */
def notifyFailed() {
  emailext ( 
      body: '$DEFAULT_CONTENT', 
      recipientProviders: [developers(), requestor()], 
      subject: '$DEFAULT_SUBJECT'
  )
}