# TCSDevOpsBootCamp

## DevOps Engineer - Case Study

The key objectives of your analysis and recommendations are:

- Create an Spring Boot Application and configure the inbuilt server port to 8885.

    Status: Complete

- Write Junit5 test cases for Spring Boot Application.

    Status: Complete

- Create a GitHub Repository and push the code to GitHub repository.

    Status: Complete

- Integrate Git plugin in jenkins pipeline to checkout the code.

    Status: Complete

- Implement Continuous Integration and Automation of Build, Test and Package.

    Status: Complete

- Implement a tool to analyse the quality of code (Sonar plugin)

    Status: Complete

- Implement Automation of Docker image creation.

    Status: Complete

- Implement authentication on docker registry.

    Status: Complete

- Implement Automation of pushing the docker image to docker registry.

    Status:Complete

- Implement email alerts for bad builds.

    Status: Complete

- Implement auto configuration of tomcat or aws ec2 instance.(using Ansible)

    Status: Complete

- Implement Automation of deployment on tomcat or aws ec2 instance.

    Status: Complete

- Implement Auto trigger of the jenkins cicd pipeline.

    Status: Complete

## Run Tests

```bash
mvn clean test
```

## Pacakge

```bash
mvn package
```

## SonarQube Analysis

```bash
mvn sonar:sonar
```

## Setup Details

### Java Spring Boot Application
Github repo for Spring Boot Application -
<https://github.com/mechdeveloper/TCSDevOpsBootCamp.git>

- Inbuilt server port 8885 defined in application.properties file

### JUnit5 Testcases

### Jenkins Setup
Running jenkins on docker container.
Github repo with docker-compose.yml file to run jenkins as docker container -
<https://github.com/mechdeveloper/jenkins-docker.git>

## Jenkinsfile

SpringBoot Application Jenkinsfile explained -

- Git checkout (source code checkout from github)
- mvn clean (cleanup)
- mvn package (build,test and package)
- SonarQube analysis
- Build Docker image (withCredentials, using dockerhub credentials to mask
username and password for dockerhub)
- Push image to dockerhub
- Ansible create EC2 Instance (runs taks.yml file to create EC2 instance along with
its security group to allow ssh and http port 80 access over internet)
Note: Pulling my own ansible docker image from dockerhub and running the
ansible commands to spin up resources in AWS cloud -
- EC2 instance Install docker (ssh into EC2 instance using ip and privatekey)
- EC2 instance Start docker (ssh into EC2 instance using ip and privatekey)
- EC2 instance initiate docker swarm mode for docker stack deployment (ssh into
EC2 instance using ip and privatekey)
- EC2 instance copy docker-compose.yml for deployment of application on EC2
Instance
- EC2 instance Deploy application as a stack of service in EC2 Docker swarm (ssh
into EC2 instance using ip and privatekey)
- Entire pipeline is enclosed in try catch block to catch any build step failure and
report/send notification via email.

### Sonarqube server Setup

Running Sonarqube server on docker container -
Github repo with docker-compose.yml file to run sonarqube server as docker container -
<https://github.com/mechdeveloper/sonarqube-docker.git>

### Jenkins configuration and Integration explained

#### Credentials configuration on Jenkins -

- git-creds : Github credentials
- docker-creds : Dockerhub credentials
- sonarqube-token : Token for sonarqube server authentication from jenkins server
- AWS_ACCESS_KEY_ID : AWS access key ID to allow AWS access for EC2
configuration
- AWS_SECRET_ACCESS_KEY: AWS secret access key to allow AWS access
for EC2 configuration
- devops-ec2-key: private key configured in AWS to enable ssh into E2 Instances
- Master Github Token: Github integration for auto trigger of builds

#### Email Notification Jenkins

- Enabled email Notification via gmail SMTP server. Created app password in personal gmail account for integration with Jenkins

- Failed Builds are notified via email.

#### SonarQube Server configuration in Jenkins

- SonarQube Token generated for Jenkins authentication.

    SonarQube server URL in jenkins is IPv4 address of local machine as sonarqube is
    running on a docker container similarly jenkins is also running on its own docker
    container both cannot talk to each other over network as they have their own isolated
    docker network. SonarQube Token is required for Jenkins to access the SonarQube
    server over the ip address of local machine

#### Github Integration of Jenkins for auto trigger of builds

- Created Personal Access token on Github for Jenkins integration

### Jenkins Global Tool Configuration

- SonarQube Scanner installation
- Maven Installation

## Jenkins Build Pipeline to trigger build and deployment of Spring Boot application

- Entire pipeline is written as Groovy Script
- Automatic Build triggers

## Docker Hub Image Registry

Created docker hub account to push docker images created from jenkins pipeline.

## Auto configuration of AWS EC2 Instances via Ansible

Running ansible command on a docker container with ansible installed. This Build step runs task.yml playbook to configure EC2 Instance -
task.yml explained -

- First task sets up a new security group to allow access over port 80 and 22
- Second task launches the AWS EC2 instance with key devops-ec2-key to allow ssh access to server via this key.
- Third task waits untill ssh starts working for the AWS EC2 instance just created via ansible playbook.

## AWS Configuration -

Created non root Administrator user with admin access to AWS for Jenkins Integration via Access Key ID and and Secret Access Key.

Created private Key pair for AWS Region us-east-2 to enable access to EC2 machines deployed in the region
