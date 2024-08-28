package com.ericsson.de.taf.jenkins.dsl.builders

import com.ericsson.de.taf.jenkins.dsl.utils.Git
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*

class DropSchemaJobBuilder extends FreeStyleJobBuilder {

    DropSchemaJobBuilder(String name,
                         String description) {
        super(name, description)

    }

    @Override
    Job build(DslFactory factory) {
        super.build(factory).with {
            blockOn('TMS-AB-gerrit-integration-tests') {
                blockLevel('GLOBAL')
            }
            scm {
                Git.simple delegate
            }
            label(RHEL6_4_OGE)
            parameters {
                choiceParam('ENV', ['test', 'itest', 'stage'], 'Environment to use for DB connection to recreate schema from scratch.')
                stringParam('GIT_COMMIT', 'master')
                stringParam('PASSWORD_TEST', '${TMS_BC_PASSWORD_TEST}', 'This is a global password set in jenkins system configuration')
                stringParam('PASSWORD_ITEST', '${TMS_BC_PASSWORD_ITEST}', 'This is a global password set in jenkins system configuration')
                stringParam('PASSWORD_STAGE', '${TMS_BC-FB_PASSWORD_STAGE}', 'This is a global password set in jenkins system configuration')

            }
            wrappers {
                maskPasswords()
                injectPasswords {
                    injectGlobalPasswords()
                }
            }
            steps {
                shell("tm-application/deploy/drop_schema.sh")
            }

        }
    }
}