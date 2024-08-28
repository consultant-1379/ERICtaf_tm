package com.ericsson.de.taf.jenkins.dsl.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*

class DatabaseRestoreJobBuilder extends FreeStyleJobBuilder {

    DatabaseRestoreJobBuilder(String name,
                              String description) {
        super(name, description)

    }

    @Override
    Job build(DslFactory factory) {
        super.build(factory).with {
            jdk SYSTEM
            scm {
                git {
                    remote {
                        name 'gm'
                        url "${GERRIT_MIRROR}/${GIT_PROJECT}"
                    }
                    branch GIT_BRANCH
                }
            }
            parameters {
                stringParam('PASSWORD_STAGE', '${TMS_BC-FB_PASSWORD_STAGE}', 'This is a global password set in jenkins system configuration')
            }
            wrappers {
                maskPasswords()
                injectPasswords {
                    injectGlobalPasswords()
                }
            }
            steps {
                shell("chmod 777 tm-application/deploy/restore_stage_schema.sh\n" +
                        "tm-application/deploy/restore_stage_schema.sh")
            }
        }
    }
}