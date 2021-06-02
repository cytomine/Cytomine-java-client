node {
    stage 'Retrieve sources'
    checkout([
        $class: 'GitSCM',
        branches: [[name: 'refs/heads/'+env.BRANCH_NAME]],
        extensions: [[$class: 'CloneOption', noTags: false, shallow: false, depth: 0, reference: '']],
        userRemoteConfigs: scm.userRemoteConfigs,
    ])

    stage 'Clean'
    sh 'rm -rf ./ci'
    sh 'mkdir -p ./ci'

    stage 'Compute version name'
    sh 'scripts/ciBuildVersion.sh ${BRANCH_NAME}'

    stage 'Download and cache dependencies'
    sh 'scripts/ciDownloadDependencies.sh'

    lock('cytomine-instance-test') {
        stage 'Run cytomine instance'
        catchError {
            sh 'docker-compose -f scripts/docker-compose.yml down -v'
        }
        sh 'docker-compose -f scripts/docker-compose.yml up -d'

        stage 'Build and test'
        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
            sh 'scripts/ciTest.sh'
        }
        stage 'Publish test'
        step([$class: 'JUnitResultArchiver', testResults: '**/ci/surefire-reports/*.xml'])

        stage 'Clear cytomine instance'
        catchError {
            sh 'docker-compose -f scripts/docker-compose.yml down -v'
        }
    }
    stage 'Publish'
    withCredentials(
        [
            file(credentialsId: 'GPG_PRIVATE_KEY', variable: 'GPG_PRIVATE_KEY_FILE'),
            file(credentialsId: 'GPG_PUBLIC_KEY', variable: 'GPG_PUBLIC_KEY_FILE'),
            string(credentialsId: 'GPG_KEYNAME', variable: 'GPG_KEYNAME'),
            string(credentialsId: 'GPG_PASSPHRASE', variable: 'GPG_PASSPHRASE'),
            usernamePassword(credentialsId: 'OSSRH_USER', usernameVariable: 'OSSRH_USER', passwordVariable: 'OSSRH_PASSWORD')
        ]
        ) {
            sh 'cp $GPG_PRIVATE_KEY_FILE ./ci'
            sh 'cp $GPG_PUBLIC_KEY_FILE ./ci'
            sh 'scripts/ciPublish.sh'
        }
}