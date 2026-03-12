pipeline {

    agent any

    parameters {
        string(name: 'USERS', defaultValue: '50', description: 'Number of concurrent users')
        string(name: 'RAMP', defaultValue: '150', description: 'Ramp-up time (seconds)')
        string(name: 'DURATION', defaultValue: '300', description: 'Test duration (seconds)')
        string(name: 'BASE_URL', defaultValue: 'http://localhost', description: 'Target base URL')
    }

    environment {
        JAVA_HOME = "/usr/lib/jvm/java-17-openjdk-amd64"
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        REPORT_BASE_DIR = "target/gatling"
    }

    stages {

        stage('Clean Workspace') {
            steps {
                deleteDir()
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/vkhalaim/gatling-homework-scala'
            }
        }

        stage('Environment Check') {
            steps {
                sh '''
                java -version
                ./mvnw -version
                '''
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
        }

        stage('Archive Results') {
            steps {
                archiveArtifacts artifacts: "${REPORT_BASE_DIR}/**", fingerprint: true
            }
        }

        stage('Publish HTML Report') {
            steps {
                publishHTML([
                    reportName: 'Gatling Performance Report',
                    reportDir: "${REPORT_BASE_DIR}",
                    reportFiles: 'index.html',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: true
                ])
            }
        }
    }

    post {
        always {
            echo "Pipeline finished"
        }
    }
}