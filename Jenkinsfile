
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
environment {
registry = "mdprakash/pennstack-backendservice"
registryCredential = 'dockerhub'
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
	 
	 sh 'cd pennstack-backend-server/'
	 script{
	 dir("${env.WORKSPACE}/pennstack-backend-server"){
     sh "pwd"
}
	  docker.build registry + ":$BUILD_NUMBER"
	}
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

	stage('Verify') {
		steps {
	   sh 'mvn verify'
		}
	}


	stage('Deploy Image') {
		steps{
		script {
			docker.withRegistry( '', registryCredential ) {
			dockerImage.push()
				}
				}
				}
	}
	stage('Remove Unused docker image') {
		steps{
		sh "docker rmi $registry:$BUILD_NUMBER"
			}
			}
			
}

}