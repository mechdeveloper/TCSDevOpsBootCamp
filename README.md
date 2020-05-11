# TCSDevOpsBootCamp

## Run Tests

```bash
mvn clean test
```

## Pacakge

```bash
mvn package
```

## Jenkinsfile

Packages the application as a contianer, pushes to dockerhub and runs the image locally at port 8888
Once the Jenkins pipeline completes successfully you can access the application at <http://localhost:8885/>
