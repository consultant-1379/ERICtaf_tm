package com.ericsson.de.taf.jenkins.dsl.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*

class HealthCheckJobBuilder extends FreeStyleJobBuilder {

    final String script;

    HealthCheckJobBuilder(String name,
                          String description,
                          String script
    ) {
        super(name, description)

        this.script = script;
    }

    @Override
    Job build(DslFactory factory) {
        def job = super.build(factory)
        job.with {
            scm {
                git {
                    remote {
                        name 'gm'
                        url "${GERRIT_MIRROR}/${GIT_PROJECT}"
                    }
                    branch GIT_BRANCH
                    extensions {
                        cleanAfterCheckout()
                        disableRemotePoll()
                    }
                }
            }
            label(RHEL6_4_OGE)
            jdk('(System)')
            steps {
                shell("chmod +x tm-deployment-scripts/shell/${script}")
                shell("tm-deployment-scripts/shell/${script}")
            }
            triggers {
                cron('H/5 7-18 * * 1-5')
            }
            publishers {
                mailer('Hyderabad.EricssonTAF@tcs.com', false, false)
            }
        }
        return job
    }
}
