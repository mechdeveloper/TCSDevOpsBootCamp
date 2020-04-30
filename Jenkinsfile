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
    stage('Pull Image') {
        sh 'docker pull ashishbagheldocker/cicd:2.0'
    }
    stage('Run Container') {
        sh 'docker run -d -p 8888:8888 ashishbagheldocker/cicd:2.0'
    }
    
}