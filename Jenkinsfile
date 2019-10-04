pipeline {
    agent any 
    stages {
        stage('SCM checkout') { 
            steps {
                   echo 'Checkout phase' 
                // 
                 checkout scm
            }
        }
        stage('Build ') { 
            steps {
                //                    echo 'Checkout phase' 
                sh 'mvn clean install'
                
            }
        }
        stage('Deploy') { 
            steps {
                //                    echo 'Checkout phase' 
                                sh 'mvn clean deploy'
                
            }
        }
    }
}