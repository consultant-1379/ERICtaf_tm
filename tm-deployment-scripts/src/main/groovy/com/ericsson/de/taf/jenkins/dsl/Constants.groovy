package com.ericsson.de.taf.jenkins.dsl

class Constants {

    static final String PROJECT_NAME = 'TMS'

    static final String JOBS_PREFIX = "${PROJECT_NAME}"
    static final String GIT_PROJECT = 'OSS/com.ericsson.cifwk/ERICtaf_tm'

    static final String JOBS_MODULE = 'tm-deployment-scripts'
    static final String JOBS_DIRECTORY = 'jenkins'
    static final String JOBS_PATH = "${JOBS_MODULE}/${JOBS_DIRECTORY}"

    static final String SLAVE_TAF_MAIN = 'taf_main_slave'
    static final String SLAVE_TAF_6 = 'taf_main_slave_6'
    static final String RHEL6_4_OGE = "RHEL6.4_OGE"
    static final String JDK_1_8 = 'JDK 1.8.0_25'
    static final String SYSTEM = '(System)'

    static final String GERRIT_SERVER = 'gerrit.ericsson.se'
    static final String GERRIT_CENTRAL = '${GERRIT_CENTRAL}' // resolves to 'ssh://gerrit.ericsson.se:29418'
    static final String GERRIT_MIRROR = '${GERRIT_MIRROR}' // resolves to 'ssh://gerritmirror.lmera.ericsson.se:29418'
    static final String GERRIT_BRANCH = '${GERRIT_BRANCH}'

    static final String GERRIT_REFSPEC = '${GERRIT_REFSPEC}'
    static final String GIT_URL = "${GERRIT_CENTRAL}/${GIT_PROJECT}"
    static final String GIT_REMOTE = 'origin'
    static final String GIT_BRANCH = 'master'
    static final String GIT_COMMIT = '${GIT_COMMIT}'

    static final String ARTIFACT_BUILD_NUMBER = '$ARTIFACT_BUILD_NUMBER'


}
