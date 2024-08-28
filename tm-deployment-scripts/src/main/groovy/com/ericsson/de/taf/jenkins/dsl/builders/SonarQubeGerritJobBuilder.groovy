package com.ericsson.de.taf.jenkins.dsl.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.de.taf.jenkins.dsl.Constants.*
import static com.ericsson.de.taf.jenkins.dsl.utils.Maven.MAVEN_VERSION

class SonarQubeGerritJobBuilder extends GerritJobBuilder {

    private static final String DESCRIPTION_SUFFIX = 'as a part of Gerrit verification process'

    SonarQubeGerritJobBuilder(String name) {
        super(name, "Sonarqube ${DESCRIPTION_SUFFIX}", "clean org.jacoco:jacoco-maven-plugin:prepare-agent install -DskipTests -Dcdt.skip")
    }

    @Override
    Job build(DslFactory factory) {
        super.build(factory).with {
            scm {
                git {
                    extensions {
                        choosingStrategy {
                            gerritTrigger()
                        }
                    }
                    remote {
                        name 'gm'
                        url "${GERRIT_MIRROR}/${GIT_PROJECT}"
                        refspec GERRIT_REFSPEC
                    }
                    branch GERRIT_REFSPEC
                    extensions {
                        cleanBeforeCheckout()
                    }
                }
            }
            parameters {
                stringParam('GERRIT_REFSPEC', 'refs/heads/master')
            }
            publishers {
                sonar {
                    installationName("SonarQube")
                    additionalProperties("-Dsonar.java.binaries=target/classes")
                    mavenInstallation MAVEN_VERSION
                }
            }
        }
    }
}
