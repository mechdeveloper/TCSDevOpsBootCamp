node{
    
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
    stage('build docker image'){
       sh 'docker build -t ashishbagheldocker/cicd:2.0 .'
    }
    stage('Push docker image') {
        withCredentials([string(credentialsId: 'docker-password', variable: 'dockerHubPwd')]){
            sh "docker login -u ashishbagheldocker -p ${dockerHubPwd}"
        }
        sh 'docker push ashishbagheldocker/cicd:2.0'
    }

    // Run Ansible task here 
    // Requires Ansible Jenkins Plugin
    stage ('Create EC2 Instance'){
        ansiblePlaybook 
    }
    
    
    // Get IP Address of EC2 Instance
    // Requires AWS CLI
    //stage ('fecth EC2 IP Address') {
        def command = 'aws ec2 describe '
        def output = sh script : "${command}", returnStdout:true
        def myIp = output.split('"')
        def ipAddress = myIp[1]
        print ipAddress
    //}
    
    // Requires SSH Agent Jenkins plugin
    stage('Installing Docker on EC2'){
        def dockerCMD = "sudo yum install docker -y"
        sshagent(['aws-dev-server']){
            sh "ssh -o StrictHostKeyChecking=no ec2-user@${ipAddress} ${dockerCMD}"
        }
    }
    
    stage('starting docker engine service on EC2 Instance'){
        def dockerCMD = "sudo service docker start"
                sshagent(['aws-dev-server']){
            sh "ssh -o StrictHostKeyChecking=no ec2-user@${ipAddress} ${dockerCMD}"
        }
    }
    
    stage('Run the docker Image'){
        def dockerCMD = "sudo docker run --name=myapp -p 80:8888 ashishbagheldocker/cicd:2.0"
    }
    
}