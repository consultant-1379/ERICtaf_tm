package com.ericsson.de.taf.jenkins.dsl.builders

import com.ericsson.de.taf.jenkins.dsl.utils.Maven
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*

class IntegrationTestJobBuilder extends FreeStyleJobBuilder {

    final String mavenGoal
    final String mvmCleanInstall

    IntegrationTestJobBuilder(String name,
                              String description,
                              String mavenGoal,
                              String mvmCleanInstall) {
        super(name, description)

        this.mavenGoal = mavenGoal
        this.mvmCleanInstall = mvmCleanInstall
    }

    @Override
    Job build(DslFactory factory) {
        super.build(factory).with {
            blockOn('TMS-AB-gerrit-integration-tests') {
                blockLevel('GLOBAL')
            }
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
            steps {
                shell('echo "##################################"\n' +
                        'ulimit -a')
                Maven.goal delegate, mvmCleanInstall
                Maven.goal delegate, mavenGoal
            }
        }
    }
}