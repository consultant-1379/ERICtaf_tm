package com.ericsson.de.taf.jenkins.dsl.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*

class FunctionalTestsJobBuilder extends FreeStyleJobBuilder {

    final String mavenGoal

    FunctionalTestsJobBuilder(String name,
                              String description,
                              String mavenGoal) {
        super(name, description)

        this.mavenGoal = mavenGoal
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
                    branch GIT_COMMIT
                    extensions {
                        cleanBeforeCheckout()
                    }
                }
            }
            label(RHEL6_4_OGE)
            parameters {
                stringParam('GIT_COMMIT', 'master', 'Commit ref to build and run functional tests from')
                booleanParam('MERGE_STABLE', false, 'If true, merge and push changes at GIT_COMMIT to stable branch on success.')
                stringParam('WAIT_TIMEOUT', '120', 'Determines, how long we should wait, before execute.')
            }
            steps {
                shell('echo merge stable value\n' +
                        'echo "$MERGE_STABLE"')
                shell("# Wait for when application is ready\n" +
                        "tm-application/deploy/precondition.sh")
                conditionalSteps {
                    condition {
                        booleanCondition('${ENV,var="MERGE_STABLE"}')
                    }
                    runner('Fail')
                    steps {
                        shell("git checkout stable || git checkout -b stable\n" +
                                "git reset --hard gm/stable\n" +
                                "git merge $GIT_COMMIT || git merge \$(git rev-parse gm/$GIT_COMMIT)\n" +
                                "git remote set-url gm ssh://gerrit.ericsson.se:29418/OSS/com.ericsson.cifwk/ERICtaf_tm")
                    }
                }
                maven {
                    mavenInstallation('Maven 3.0.5')
                    goals(mavenGoal)

                }
            }
            publishers {
                archiveTestNG('**/test-output/testng-results.xml') {
                    escapeTestDescription(true)
                    escapeExceptionMessages(true)
                    showFailedBuildsInTrendGraph(false)
                    markBuildAsUnstableOnSkippedTests(false)
                    markBuildAsFailureOnFailedConfiguration(false)
                }
            }
            publishers {
                flexiblePublish {
                    conditionalAction {
                        condition {
                            booleanCondition('${ENV,var="MERGE_STABLE"}')
                        }
                    }
                }
            }
            publishers {
                allureReportPublisher {
                    config {
                        jdk('')
                        commandline('')
                        resultsPattern('target/allure-results')
                        properties {
                            propertyConfig {
                                key('allure.issues.tracker.pattern')
                                value('https://taftm.seli.wh.rnd.internal.ericsson.com/#tm/viewTC/%s')
                            }
                            propertyConfig {
                                key('allure.issues.tracker.pattern')
                                value('https://eteamproject.internal.ericsson.com/browse/%s')
                            }
                        }
                        reportBuildPolicy('ALWAYS')
                        includeProperties(true)
                    }
                }
            }
        }
        return job
    }
}
