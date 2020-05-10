
node {
  try {
    /* ... existing build steps ... */
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
       sh "docker build -t ashishbagheldocker/devops-e2-casestudy:${env.BUILD_ID} ."
    }
    stage('push docker image to dockerhub') {
        withCredentials([string(credentialsId: 'docker-password', variable: 'dockerHubPwd')]){
            sh "docker login -u ashishbagheldocker -p ${dockerHubPwd}"
        }
        sh "docker push ashishbagheldocker/devops-e2-casestudy:${env.BUILD_ID}"
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
  def awsclicmd = "aws ec2 describe-instances --region us-east-2 --query 'Reservations[].Instances[].PrivateIpAddress'"
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
        sshagent(['']){
            sh "ssh -i private.pem -o StrictHostKeyChecking=no ec2-user@${ipAddress} ${dockerCMD}"
        }
    }
    stage('starting docker engine service on EC2 Instance'){
        def dockerCMD = "sudo service docker start"
        sshagent(['']){
            sh "ssh -i private.pem -o StrictHostKeyChecking=no ec2-user@${ipAddress} ${dockerCMD}"
        }
    }
    
    stage('Run the docker image in EC2 Instance'){
        def dockerCMD = "sudo docker run --name=myapp -p 80:8885 ashishbagheldocker/devops-e2-casestudy:${env.BUILD_ID}"
        sshagent(['']){
            sh "ssh -i private.pem -o StrictHostKeyChecking=no ec2-user@${ipAddress} ${dockerCMD}"
        }
    }
    
  } catch (e) {
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