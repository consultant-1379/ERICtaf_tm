package com.ericsson.de.taf.jenkins.dsl.builders

import com.ericsson.de.taf.jenkins.dsl.utils.Maven
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*

class DeployDocsBuilder extends FreeStyleJobBuilder {
    final String mavenGoal

    DeployDocsBuilder(
            String name,
            String description,
            String mavenGoal
    ) {
        super(name, description)
        this.mavenGoal = mavenGoal
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
                    extensions {
                        cleanBeforeCheckout()
                    }
                }
            }
            label(RHEL6_4_OGE)
            parameters {
                choiceParam('ENV', ['test', 'stage', 'prod'], 'Environment to deploy to (test, stage, prod).')
                stringParam('GIT_COMMIT', 'master', 'Commit ref to use build scripts from.')
                stringParam('PASSWORD_TEST', '${TMS_C_PASSWORD_TEST}', 'This is a global password set in jenkins system configuration')
            }
            wrappers {
                maskPasswords()
                injectPasswords {
                    injectGlobalPasswords()
                }
            }
            steps {
                Maven.goal delegate, mavenGoal
                shell('tm-application/deploy/docs.sh')

            }
        }
    }
}