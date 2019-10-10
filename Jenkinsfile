
    def mvn(def args) {
    def mvnHome = tool 'maven'
    def javaHome = tool 'JDK8'

    withEnv(["JAVA_HOME=${javaHome}", "PATH+MAVEN=${mvnHome}/bin:${env.JAVA_HOME}/bin"]) {
        sh "${mvnHome}/bin/mvn ${args} --batch-mode -V -U -e -Dsurefire.useFile=false"
    }
}
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
            sh 'mvn clean install'
        }
    }

    stage('Unit Test') {
        steps {
           sh 'mvn test'
        }
    }

    stage('Integration Test') {
        steps {
           sh 'mvn verify'
        }
    }
    }
}