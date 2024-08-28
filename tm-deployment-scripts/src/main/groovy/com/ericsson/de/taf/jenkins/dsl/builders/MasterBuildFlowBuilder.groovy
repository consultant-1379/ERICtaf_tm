package com.ericsson.de.taf.jenkins.dsl.builders

import com.ericsson.de.taf.jenkins.dsl.utils.Gerrit
import com.ericsson.de.taf.jenkins.dsl.utils.Git
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*

class MasterBuildFlowBuilder extends BuildFlowBuilder {

    static final String DESCRIPTION = "Build flow upon branch '${GIT_BRANCH}' update"
    final String buildFlowJobName

    MasterBuildFlowBuilder(String name, String buildFlowJobName, String buildFlowText) {
        super(name, DESCRIPTION, buildFlowText, buildFlowJobName)
    }

    @Override
    Job build(DslFactory factory) {
        super.build(factory).with {
            scm {
                Git.gerrit delegate
            }
            triggers {
                Gerrit.refUpdated delegate
            }
            label(RHEL6_4_OGE)
        }
    }
}
