pipeline {
    agent { label 'master' }
    stages {
        stage('build') {
            steps {
                echo "Hello World!"
                bat "echo Hello from the shell"
                bat "hostname"
                bat "uptime"
            }
        }
    }
}