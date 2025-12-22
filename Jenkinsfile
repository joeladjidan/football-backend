pipeline {
  agent any

  options {
    buildDiscarder(logRotator(daysToKeepStr: '14'))
    timeout(time: 1, unit: 'HOURS')
    timestamps()
  }

  parameters {
    string(name: 'BRANCH', defaultValue: 'develop', description: 'Branche Git à builder')
    string(name: 'MVN_GOALS', defaultValue: 'clean verify', description: 'Goals Maven à exécuter')
    booleanParam(name: 'SKIP_TESTS', defaultValue: false, description: 'Ignorer les tests')
    // Optionnel: nom d'une installation Maven configurée dans Jenkins (laisser vide pour utiliser mvn du PATH)
    string(name: 'MAVEN_TOOL', defaultValue: '', description: 'Nom de l installation Maven configurée dans Jenkins (optionnel)')
    // Optionnel: nom d'une installation JDK configurée dans Jenkins (laisser vide pour utiliser le JDK système)
    string(name: 'JDK_TOOL', defaultValue: '', description: 'Nom de l installation JDK configurée dans Jenkins (optionnel)')
  }

  environment {
    MVN_FLAGS = "-B"
    REPO_URL  = "https://github.com/joeladjidan/football-backend.git"
    // NOTE: créez un credential Jenkins (Username with password) contenant votre nom GitHub
    // comme username et un Personal Access Token (PAT) comme password, puis mettez son ID ci-dessous
    GIT_CREDENTIALS_ID = 'github-token'
  }

  stages {
    stage('Checkout') {
      steps {
        // Utilise les credentials Jenkins pour le checkout HTTPS (évite l'erreur d'authentification)
        checkout([$class: 'GitSCM', branches: [[name: "refs/heads/${params.BRANCH}"]],
                  userRemoteConfigs: [[url: env.REPO_URL, credentialsId: env.GIT_CREDENTIALS_ID]]])
      }
    }

    stage('Build') {
      steps {
        script {
          // Try to resolve Maven tool: use provided name, otherwise probe a list of common tool names.
          def mvnHome = null
          def pathSep = isUnix() ? ':' : ';'
          if (params.MAVEN_TOOL?.trim()) {
            try {
              mvnHome = tool name: params.MAVEN_TOOL, type: 'maven'
              echo "Using Maven tool provided: ${params.MAVEN_TOOL} -> ${mvnHome}"
            } catch (err) {
              echo "Maven tool '${params.MAVEN_TOOL}' not found: ${err}. Will try fallback probes or use mvn from PATH"
            }
          }
          if (!mvnHome) {
            def candidates = ['Maven 3.8.7','Maven 3.6.3','maven','M3']
            for (c in candidates) {
              try {
                mvnHome = tool name: c, type: 'maven'
                echo "Auto-detected Maven tool '${c}' -> ${mvnHome}"
                break
              } catch (ignored) {
                // continue
              }
            }
          }
          if (mvnHome) {
            env.PATH = "${mvnHome}/bin${pathSep}${env.PATH}"
          } else {
            echo "No Jenkins Maven tool detected; using 'mvn' from PATH (ensure correct mvn is available on agent)"
          }

          // Try to resolve JDK tool similarly
          def javaHome = null
          if (params.JDK_TOOL?.trim()) {
            try {
              javaHome = tool name: params.JDK_TOOL, type: 'jdk'
              echo "Using JDK tool provided: ${params.JDK_TOOL} -> ${javaHome}"
            } catch (err) {
              echo "JDK tool '${params.JDK_TOOL}' not found: ${err}. Will try fallback probes or use system java"
            }
          }
          if (!javaHome) {
            def jcandidates = ['JDK 17','jdk-17','jdk17','JDK11','java']
            for (c in jcandidates) {
              try {
                javaHome = tool name: c, type: 'jdk'
                echo "Auto-detected JDK tool '${c}' -> ${javaHome}"
                break
              } catch (ignored) {
                // continue
              }
            }
          }
          if (javaHome) {
            env.JAVA_HOME = javaHome
            env.PATH = "${javaHome}/bin${pathSep}${env.PATH}"
          } else {
            echo "No Jenkins JDK tool detected; using system java from PATH"
          }

          // Build command
          def skipArg = params.SKIP_TESTS ? '-DskipTests=true' : ''
          def mavenCommand = "${env.MVN_FLAGS} ${params.MVN_GOALS} ${skipArg}"
          if (isUnix()) {
            sh "mvn ${mavenCommand}"
          } else {
            bat "mvn ${mavenCommand}"
          }
        }
      }
    }

    stage('Publish Test Results') {
      steps {
        junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
      }
    }

    stage('Archive') {
      steps {
        archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
      }
    }
  }

  post {
    always {
      junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
      cleanWs()
    }
    success {
      echo "Build succeeded for ${params.BRANCH}"
    }
    failure {
      echo "Build failed for ${params.BRANCH}"
    }
  }
}