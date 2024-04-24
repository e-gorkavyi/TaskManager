pipeline {
    agent any
    stages {
        stage('Build') { 
            withMaven(maven:mvn) {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
    }
}
