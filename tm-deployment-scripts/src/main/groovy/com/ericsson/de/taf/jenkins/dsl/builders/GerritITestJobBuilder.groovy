package com.ericsson.de.taf.jenkins.dsl.builders

import com.ericsson.de.taf.jenkins.dsl.utils.Gerrit
import com.ericsson.de.taf.jenkins.dsl.utils.Git
import com.ericsson.de.taf.jenkins.dsl.utils.Maven
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*

class GerritITestJobBuilder extends FreeStyleJobBuilder {

    private static final String DESCRIPTION_SUFFIX = 'as a part of Gerrit verification process'

    final String mavenGoal
    final String mvmCleanInstall

    GerritITestJobBuilder(String name,
                          String description,
                          String mavenGoal,
                          String mvmCleanInstall) {
        super(name, "${description} ${DESCRIPTION_SUFFIX}")

        this.mavenGoal = mavenGoal
        this.mvmCleanInstall = mvmCleanInstall
    }

    @Override
    Job build(DslFactory factory) {
        def job = super.build(factory)
        job.with {
            concurrentBuild()
            blockOn(['TMS-BC-drop-schema', 'TMS-BD-Integration-Tests']) {
                blockLevel('GLOBAL')
            }
            scm {
                Git.gerrit delegate
            }
            triggers {
                Gerrit.patchsetCreated delegate
            }
            wrappers {
                maskPasswordsBuildWrapper {
                    varPasswordPairs {
                        varPasswordPair {
                            var('PASSWORD_ITEST')
                            password('h5dEye9I4JnAk')
                        }
                    }
                }
            }
            steps {
                shell("ENV=itest tm-application/deploy/drop_schema.sh")
                Maven.goal delegate, mvmCleanInstall
                Maven.goal delegate, mavenGoal
            }
        }
        return job
    }
}
