package com.ericsson.de.taf.jenkins.dsl.builders

import com.ericsson.de.taf.jenkins.dsl.utils.Git
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*

class ReleaseMinorVersionJobBuilder extends FreeStyleJobBuilder {

    String script;

    ReleaseMinorVersionJobBuilder(String name,
                                  String description,
                                  String script) {
        super(name, description)
        this.script = script;
    }

    @Override
    Job build(DslFactory factory) {
        super.build(factory).with {
            scm {
                Git.simple delegate
            }
            label(RHEL6_4_OGE)
            parameters {
                stringParam('Version', 'calculateMe', 'Variable to represent the new version of TMS. <br/>If you use the default (i.e. calculateMe), the new version will be calculated based on the version in the poms.<br/>Otherwise you can specify a new version e.g. 3.1.5-SNAPSHOT and in such case all the boms will be changed over to this new version. <br/>\n' +
                        'NOTE: Make sure you include the \'-SNAPSHOT\'')
            }
            steps {
                shell("git checkout master|| git checkout -b master\n" +
                        "git reset --hard gm/master")
                shell("chmod +x tm-deployment-scripts/shell/${script}\n" +
                        "tm-deployment-scripts/shell/${script}")
                shell("git add .\n" +
                        "if [ \"\$(git status --porcelain)\" != \"\" ]; then\n" +
                        "    git commit -m \"Sprint version update\"\n" +
                        "fi")
            }
            publishers {
                git {
                    pushOnlyIfSuccess(true)
                    branch('origin', 'master')
                }
            }
        }
    }
}