package com.ericsson.de.taf.jenkins.dsl.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*

class ChangeLogBuilder extends FreeStyleJobBuilder {
    final String mavenGoal
    ChangeLogBuilder(String name,
                    String description,
                     String mavenGoal ) {
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
                        url "${GERRIT_CENTRAL}/${GIT_PROJECT}"
                    }
                    branch GIT_BRANCH
                    extensions {
                        cleanBeforeCheckout()
                    }
                }
            }
            jdk(SYSTEM)
            label(SLAVE_TAF_6)
            steps{
                maven {
                    mavenInstallation('Maven 3.1.0')
                    goals(mavenGoal)
                }
                shell('cd /proj/eiffel004_config_fem119/slaves/workspace-slave6/workspace/TMS-CC-Changelog/target\n' +
                        'targetDir=/proj/pduosstaf/taf/taflanding/tmsrelease\n' +
                        'rm -f ${targetDir}/changelog.html\n' +
                        'cp changelog.html ${targetDir}/changelog.html')
            }
        }
    }
}