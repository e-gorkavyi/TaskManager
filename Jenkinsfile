pipeline {
    agent any
    stages {
        stage('Build') { 
            steps {
                container ('maven') {
                    sh 'mvn version' 
                }
            }
        }
    }
}
