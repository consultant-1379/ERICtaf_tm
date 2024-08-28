package com.ericsson.de.taf.jenkins.dsl.builders

import com.ericsson.de.taf.jenkins.dsl.utils.Maven
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*


@SuppressWarnings("GroovyAssignabilityCheck")
class ReleaseJobBuilder extends FreeStyleJobBuilder {

    static
    final String DESCRIPTION = 'Performs release, advances the incremental version and uploads all artifacts to Nexus i.e. 0.0.1-SNAPSHOT to 0.0.2-SNAPSHOT and posts 0.0.1 version to nexus'
    final String mavenGoal

    ReleaseJobBuilder(String name, String mavenGoal) {
        super(name, DESCRIPTION)
        this.mavenGoal = mavenGoal
    }

    @Override
    Job build(DslFactory factory) {
        def job = super.build(factory)
        job.with {
            blockOn(['TMS-AA-gerrit-unit-tests', 'TMS-AB-gerrit-integration-tests', 'TMS-AC-gerrit-sonar-qube']) {
                blockLevel 'GLOBAL'
            }
            jdk(JDK_1_8)
            scm {
                git {
                    remote {
                        name 'gc'
                        url "${GERRIT_CENTRAL}/${GIT_PROJECT}"
                    }
                    branch '*/' + GIT_BRANCH
                    extensions {
                        localBranch(GIT_BRANCH)
                    }
                    configure {
                        def ext = it / 'extensions'
                        def pkg = 'hudson.plugins.git.extensions.impl'
                        ext / "${pkg}.UserExclusion" << excludedUsers('Jenkins Release')
                        ext / "${pkg}.UserIdentity" << name('Jenkins Release')
                    }
                }
            }
            steps {
                Maven.goal delegate, mavenGoal
            }
            wrappers {
                preBuildCleanup()
            }
            publishers {
                git {
                    pushOnlyIfSuccess()
                    branch 'gc', GIT_BRANCH
                }
            }
        }
        return job
    }
}
