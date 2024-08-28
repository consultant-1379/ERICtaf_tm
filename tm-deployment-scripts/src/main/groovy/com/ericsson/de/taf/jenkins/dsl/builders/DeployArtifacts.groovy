package com.ericsson.de.taf.jenkins.dsl.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*

class DeployArtifacts extends FreeStyleJobBuilder {

    DeployArtifacts(String name,
                    String description) {
        super(name, description)
    }

    @Override
    Job build(DslFactory factory) {
        super.build(factory).with {
            scm {
                git {
                    remote {
                        name 'gm'
                        url "${GERRIT_MIRROR}/${GIT_PROJECT}"
                    }
                    branch GIT_COMMIT
                    extensions{
                        cleanBeforeCheckout()
                    }
                }
            }
            label(SLAVE_TAF_MAIN)
            parameters {
                choiceParam('ENV', ['test', 'stage', 'prod'], 'Environment to deploy to (test, stage, prod).')
                stringParam('GIT_COMMIT', 'master', 'Commit ref to use build scripts from.')
                nonStoredPasswordParam('SUDO_PWD', 'Password for getting sudo to restart Tomcat. Required only for prod env.')
                stringParam('ARTIFACT_BUILD_NUMBER ', '', 'Build number of TMS_Build job to copy artifacts from. Leave blank to build from GIT_COMMIT.')
                stringParam('PASSWORD_TEST', '${TMS_C_PASSWORD_TEST}', 'This is a global password set in jenkins system configuration')
                stringParam('PASSWORD_STAGE', '${TMS_CA_PASSWORD_PROD}', 'This is a global password set in jenkins system configuration')
                stringParam('PASSWORD_PROD', '${TMS_CA_PASSWORD_PROD}', 'This is a global password set in jenkins system configuration')
            }
            wrappers {
                maskPasswords()
                injectPasswords {
                    injectGlobalPasswords()
                }
            }
            steps {
                conditionalSteps {
                    steps {
                        condition {
                            expression('^.+$', ARTIFACT_BUILD_NUMBER)
                        }
                        runner('DontRun')
                        steps {
                            copyArtifacts('TMS-BA-build') {
                                includePatterns('**/*.war', '**/*.tar.gz')
                                buildSelector {
                                    buildNumber(ARTIFACT_BUILD_NUMBER)
                                }
                                fingerprintArtifacts(true)
                            }
                        }
                    }
                }
                maven {
                    mavenInstallation('Maven 3.1.0')
                    goals('clean install -pl tm-application/tm-client,tm-application/tm-server-api,tm-application/tm-server-model,tm-application/tm-server-requirements,tm-application/tm-server-scheduling,tm-application/tm-server-reporting,tm-application/tm-server -DskipTests')
                }
                shell("tm-application/deploy/app.sh")
            }
            publishers {
                downstreamParameterized {
                    trigger('TMS-CB-DeployDocs') {
                        condition('SUCCESS')
                        parameters {
                            currentBuild()
                            gitRevision(false)
                        }
                    }
                }
            }

        }

    }
}
