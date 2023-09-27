//Have to init the list before modifying it
static def initList() {
    return ['']
}

static def getGerritTriggerFlavorsArray() {
    return ['']
}

static def getMergeOnMasterFlavorsArray() {
    return ['']
}

void deployOnFMS() {
    def apkFiles = findFiles(glob: '**/*app-release*.apk')

    apkFiles.each { file ->
        def response = sh(script: """curl -X POST -H 'Authorization: Bearer $TOKEN' https://my.famoco.com/api/organizations/$ORGANIZATION_ID/applications/ -F apk=@$file""", returnStdout: true)
        echo response
    }
}

void deployOnS3() {
    def apkFiles = findFiles(glob: '**/*.apk')

    projectName = sh(
            returnStdout: true,
            script: "basename `git rev-parse --show-toplevel`"
    )

    apkFiles.each { file ->
        withAWS(credentials: 'aws-customer-delivery', region: 'eu-west-1') {
            s3Upload acl: 'Private', bucket: 'famoco-customer-delivery', file: file.getPath(), path: "deliverables/${projectName.trim()}/${file.getName()}"
        }
    }

    if (RELEASE_VERSION != null && RELEASE_VERSION != "") {
        withAWS(credentials: 'aws-customer-delivery', region: 'eu-west-1') {
            s3Upload acl: 'Private', bucket: 'famoco-customer-delivery', file: "$RELEASE_NOTE_FILE_PATH", path: "deliverables/${projectName.trim()}/releaseNote-$RELEASE_VERSION"
        }
    }
}

pipeline {
    agent {
        docker {
            image 'android-sdk-cd-build:latest'
        }
    }

    environment {
        //The token here has to have 'Application Write' option selected
        TOKEN = credentials('FMS_TOKEN_WRITE_APPLICATION')
        //Famoco Project Team FMS Organization id
        ORGANIZATION_ID = 861
        BUILD_LIST = initList()
        RELEASE_NOTE_FILE_PATH = 'releaseNote.txt'
        PROPERTIES_FILE = credentials('FAMOCOPAYAPP2APP_APP_FOLDER_LOCAL_PROPERTIES')
    }

    stages {
        stage('Init Pipeline') {
            steps {
                sh script: '''
                    #!/bin/bash
                    cp $PROPERTIES_FILE ./local.properties
                    chmod -R 777 .
                '''

                script {
                    switch ("$BUILD_TYPE") {
                        case "GERRIT_TRIGGER":
                            BUILD_LIST = getGerritTriggerFlavorsArray()
                            break
                        case "MERGE_ON_MASTER":
                            BUILD_LIST = getMergeOnMasterFlavorsArray()
                            break
                        case "RELEASE_BUILD":
                            // nothing to do
                            break
                        default:
                            echo "NON_GERRIT"
                            break
                    }
                }
            }
        }

        stage('Clean') {
            steps {
                sh script: """
                    #!/bin/bash
                    chmod +x gradlew
                    ./gradlew clean --no-daemon --info
                """
            }
        }

        stage('Static Analysis') {
            when {
                environment name: 'BUILD_TYPE', value: 'GERRIT_TRIGGER'
            }

            steps {
                script {
                    withAWS(credentials: 'aws-customer-delivery', region: 'eu-west-1') {
                        echo 'Static Analysis'
                        sh(script: """
                                #!/bin/bash
                                ./gradlew lintDebug
                                """)
                    }
                }
            }
        }

        stage('Build Each Flavor') {
            when {
                not {
                    environment name: 'BUILD_TYPE', value: 'RELEASE_BUILD'
                }
            }

            steps {
                script {
                    withAWS(credentials: 'aws-customer-delivery', region: 'eu-west-1') {
                        sh(script: """
                                #!/bin/bash
                                ./gradlew assembleDebug
                                """)
                    }

                    archiveArtifacts '**/*.apk'
                }
            }
        }

        stage('Build Releases') {
            when {
                environment name: 'BUILD_TYPE', value: 'RELEASE_BUILD'
            }

            steps {
                script {
                    succeed = false
                    echo "Build all release flavors"
                    withAWS(credentials: 'aws-customer-delivery', region: 'eu-west-1') {
                        sh(script: '''
                                #!/bin/bash
                                ./gradlew assembleRelease
                                ''')
                    }

                    archiveArtifacts '**/*.apk'

                    releaseNote = sh(
                            returnStdout: true,
                            script: "git log --pretty=\"* %s\" $PREVIOUS_VERSION_SHA1..$COMMIT_SHA1"
                    )
                    echo "release note: \n$releaseNote"

                    projectName = sh(
                            returnStdout: true,
                            script: "basename `git rev-parse --show-toplevel`"
                    )

                    date = new Date().format("yyyy-dd-MM", TimeZone.getTimeZone('UTC'))

                    writeFile file: "$RELEASE_NOTE_FILE_PATH", text: "${projectName.trim()} ($date)\n----------------------\n$releaseNote"
                    archiveArtifacts "$RELEASE_NOTE_FILE_PATH"
                }
            }
        }

        stage('S3 Deploy') {
            when {
                allOf {
                    environment name: 'BUILD_TYPE', value: 'RELEASE_BUILD';
                    environment name: 'S3_PUSH', value: 'true';
                }
            }

            steps {
                script {
                    deployOnS3()
                }
            }
        }

        stage('Post Release APK to the FMS') {
            when {
                allOf {
                    environment name: 'BUILD_TYPE', value: 'RELEASE_BUILD';
                    environment name: 'FMS_PUSH', value: 'true';
                }
            }

            steps {
                script {
                    deployOnFMS()
                }
            }
        }
    }

    post {
        failure {
            script {
                if ("$BUILD_TYPE" == "GERRIT_TRIGGER") {
                    echo 'Record Analysis'
                    recordIssues enabledForFailure: true, aggregatingResults: true, tools: [androidLintParser(pattern: '**/reports/lint-results-debug.xml')]
                }
                if ("$BUILD_TYPE" == "RELEASE_BUILD") {
                    emailext body: '''${SCRIPT, template="groovy-html.template"}''',
                            subject: "${env.JOB_NAME} - Version # $RELEASE_VERSION - Failed",
                            mimeType: 'text/html',to: "$EMAILS"
                    sh(script: "rm $RELEASE_NOTE_FILE_PATH")
                }
            }
        }
        success {
            script {
                if ("$BUILD_TYPE" == "GERRIT_TRIGGER") {
                    echo 'Record Analysis'
                    recordIssues enabledForFailure: true, aggregatingResults: true, tools: [androidLintParser(pattern: '**/reports/lint-results-debug.xml')]
                }
                if ("$BUILD_TYPE" == "RELEASE_BUILD") {
                    emailext body: '''${SCRIPT, template="groovy-html.template"}''',
                            subject: "${env.JOB_NAME} - Version # $RELEASE_VERSION - Successful",
                            mimeType: 'text/html', to: "$EMAILS"
                    sh(script: "rm $RELEASE_NOTE_FILE_PATH")
                }
            }
        }
    }
}
