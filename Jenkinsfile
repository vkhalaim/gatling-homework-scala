pipeline {

    agent any

    tools {
        maven 'Maven'   // must match Jenkins tool config name
    }

    parameters {
        string(name: 'USERS', defaultValue: '50', description: 'Number of concurrent users')
        string(name: 'RAMP', defaultValue: '150', description: 'Ramp-up time (seconds)')
        string(name: 'DURATION', defaultValue: '300', description: 'Test duration (seconds)')
        string(name: 'BASE_URL', defaultValue: 'http://localhost', description: 'Target base URL')
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/vkhalaim/gatling-homework-scala'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw -B clean package -DskipTests'
            }
        }

        stage('Run Gatling') {
            steps {
                sh """
                ./mvnw gatling:test \
                -Dusers=${params.USERS} \
                -Dramp=${params.RAMP} \
                -Dduration=${params.DURATION} \
                -DbaseUrl=${params.BASE_URL}
                """
            }
            post {
                always {
                    gatlingArchive()
                }
            }
        }
    }
}