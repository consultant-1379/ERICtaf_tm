package com.ericsson.de.taf.jenkins.dsl.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*

class DatabaseBackupJobBuilder extends FreeStyleJobBuilder {

    DatabaseBackupJobBuilder(String name,
                             String description) {
        super(name, description)

    }

    @Override
    Job build(DslFactory factory) {
        super.build(factory).with {
            jdk SYSTEM
            label ''
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
                stringParam('PASSWORD_PROD', '${TMS_FA_PASSWORD_PROD}', 'This is a global password set in jenkins system configuration')
            }
            wrappers {
                maskPasswords()
                injectPasswords {
                    injectGlobalPasswords()
                }
            }
            steps {
                shell("cd /proj/nam_tms/TMS_Backup/\n" +
                        "rm -f *")
                shell("chmod 777 tm-application/deploy/backup_prod_schema.sh\n" +
                        "tm-application/deploy/backup_prod_schema.sh")
            }
        }
    }
}