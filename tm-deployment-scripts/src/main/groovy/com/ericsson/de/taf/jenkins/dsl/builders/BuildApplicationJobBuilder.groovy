package com.ericsson.de.taf.jenkins.dsl.builders

import com.ericsson.de.taf.jenkins.dsl.utils.Git
import com.ericsson.de.taf.jenkins.dsl.utils.Maven
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*

class BuildApplicationJobBuilder extends FreeStyleJobBuilder {

    final String mavenGoal

    BuildApplicationJobBuilder(String name,
                               String description,
                               String mavenGoal) {
        super(name, description)

        this.mavenGoal = mavenGoal
    }

    @Override
    Job build(DslFactory factory) {
        def job = super.build(factory)
        job.with {
            blockOn(['TMS-CA-DeployArtifacts', 'TMS-job-seed']) {
                blockLevel('GLOBAL')
            }
            scm {
                Git.simple delegate
            }
            label(RHEL6_4_OGE)
            jdk(JDK_1_8)

            steps {
                Maven.goal delegate, mavenGoal
            }
            publishers {
                archiveArtifacts('tm-application/tm-server/target/tm-server-*.war, tm-application/tm-client/target/tm-client-*.tar.gz')
            }
        }
        return job
    }
}
