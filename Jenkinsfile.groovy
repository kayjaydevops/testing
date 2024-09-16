pipeline {
  agent any

  environment {
    AWS_ACCESS_KEY_ID = credentials('aws-access-key-id')
    AWS_SECRET_ACCESS_KEY = credentials('aws-secret-access-key')
  }

  stages {
    stage('Clone Repository') {
      steps {
        git branch: 'main', url: 'https://github.com/kayjayvisure/testing.git'
      }
    }

    stage('Install Terraform') {
      steps {
        // Install Terraform (only if it's not already installed globally)
        sh 'curl -o terraform.zip https://releases.hashicorp.com/terraform/1.4.6/terraform_1.4.6_linux_amd64.zip'
        sh 'unzip terraform.zip'
        sh 'sudo mv terraform /usr/local/bin/'
      }
    }

    stage('Terraform Init') {
      steps {
        sh 'terraform init'
      }
    }

    stage('Terraform Plan') {
      steps {
        sh 'terraform plan -var-file=terraform.tfvars'
      }
    }

    stage('Terraform Apply') {
      steps {
        // Automate EC2 creation; use -auto-approve to skip interactive approval
        sh 'terraform apply -var-file=terraform.tfvars -auto-approve'
      }
    }
  }

  post {
    always {
      echo 'Cleaning up workspace...'
      cleanWs()
    }
    success {
      echo 'Terraform EC2 instance created successfully.'
    }
    failure {
      echo 'Terraform apply failed.'
    }
  }
}
