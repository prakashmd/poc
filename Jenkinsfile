pipeline {
    agent any

    tools {
        maven 'maven'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
        steps {
            mvn 'clean install'
        }
    }

    stage('Unit Test') {
        steps {
            mvn 'test'
        }
    }

    stage('Integration Test') {
        steps {
            mvn 'verify'
        }
    }
    }
}