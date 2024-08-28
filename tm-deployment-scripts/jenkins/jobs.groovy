/*
Full API documentation:
https://jenkinsci.github.io/job-dsl-plugin/

Job DSL playground:
http://job-dsl.herokuapp.com/
*/


import com.ericsson.de.taf.jenkins.dsl.builders.DatabaseBackupJobBuilder
import com.ericsson.de.taf.jenkins.dsl.builders.DatabaseRestoreJobBuilder
import com.ericsson.de.taf.jenkins.dsl.builders.GerritJobBuilder
import com.ericsson.de.taf.jenkins.dsl.builders.GerritITestJobBuilder
import com.ericsson.de.taf.jenkins.dsl.builders.ReleaseMinorVersionJobBuilder
import com.ericsson.de.taf.jenkins.dsl.builders.IntegrationTestJobBuilder
import com.ericsson.de.taf.jenkins.dsl.builders.SonarQubeGerritJobBuilder
import com.ericsson.de.taf.jenkins.dsl.builders.BuildApplicationJobBuilder
import com.ericsson.de.taf.jenkins.dsl.builders.FunctionalTestsJobBuilder
import com.ericsson.de.taf.jenkins.dsl.builders.DropSchemaJobBuilder
import com.ericsson.de.taf.jenkins.dsl.builders.DeployArtifacts
import com.ericsson.de.taf.jenkins.dsl.builders.DeployDocsBuilder
import com.ericsson.de.taf.jenkins.dsl.builders.HealthCheckJobBuilder
import com.ericsson.de.taf.jenkins.dsl.builders.MasterBuildFlowBuilder
import com.ericsson.de.taf.jenkins.dsl.builders.ReleaseJobBuilder
import com.ericsson.de.taf.jenkins.dsl.builders.ChangeLogBuilder



import javaposse.jobdsl.dsl.DslFactory

def mvnUnitTest = 'clean install'
def mvnGerritITest = 'test -Pitest -Dserver.port=9334 -Dpersistence=itest -DmigrationPath=test -Dcdt.skipInstall -Dcdt.skipBuild'
def mvnITest = 'test -Pitest -Dserver.port=9333 -Dpersistence=itest -DmigrationPath=test -Dcdt.skipInstall -Dcdt.skipBuild'
def mvnCleanInstall ='clean install -DskipTests'
def mvnFunctionalTests = 'clean package -Pfun -pl :tm-tests -amd -Dmaven.test.failure.ignore=false -Dtaf.profiles=grid,test -Dsuitethreadpoolsize=10'
def mvnCleanInstallPhase = 'install -Dcdt.phase=install'
def mvnChangeLog = 'com.ericsson.cifwk.taf:tafchangelog-maven-plugin:1.0.14:generate -X -e'
def mvnDeployDocs = 'clean install -pl tm-application/tm-server-api -Dmaven.install.skip=true -DskipTests -Pdocs'
def mvnRelease = '-Dresume=false release:prepare release:perform  -DlocalCheckout=true -DpreparationGoals=\"clean install -DskipTests\" -Dgoals=\"clean deploy -DskipTests -Pdeploy-nexus\" -Pdeploy-nexus -DskipTests'

def unitTests = 'Unit tests'
def iTests = 'Integration tests'
def buildApplication = 'Build TMS application and archive files'
def dropSchema = 'Connect to MySQL and drop specified schema'
def deployArtifacts = 'Deploys TMS artifacts to defined environment.'
def deployDocs = 'Deploys TMS API documentation to defined environment'
def changeLog = 'Creates the changelog html page and uploads to taflanding'
def functionalTests = 'Run Functional Tests\n" + "Selenium Grid restart url https://fem119-eiffel004.lmera.ericsson.se:8443/jenkins/view/All/job/Selenium_Grid_Restart/'
def stageHealthCheck = 'Runs various checks to ensure TMS Stage Environment is up and running.'
def prodHealthCheck = 'Runs various checks to ensure TMS Prod Environments is up and running.'
def databaseBackUp = 'Backup production database'
def releaseMinorVersion = 'This Job updates the minor version for release with Ericsson gask etc i.e. (0.0.43-SNAPSHOT to 0.1.1-SNAPSHOT)'
def databaseRestore = 'Restore stage database'

//Gerrit flow
def aa = new GerritJobBuilder('AA-gerrit-unit-tests', unitTests, mvnUnitTest)
def ab = new GerritITestJobBuilder('AB-gerrit-integration-tests', iTests, mvnGerritITest, mvnCleanInstall)
def ac = new SonarQubeGerritJobBuilder('AC-gerrit-sonar-qube')

//Build flow
def ba = new BuildApplicationJobBuilder('BA-build', buildApplication, mvnCleanInstallPhase)
def bb = new FunctionalTestsJobBuilder('BB-Functional-Tests', functionalTests, mvnFunctionalTests)
def bc = new DropSchemaJobBuilder('BC-drop-schema', dropSchema)
def bd = new IntegrationTestJobBuilder('BD-Integration-Tests', iTests, mvnITest, mvnCleanInstall)
def ca = new DeployArtifacts('CA-DeployArtifacts', deployArtifacts)
def cb = new DeployDocsBuilder('CB-DeployDocs', deployDocs, mvnDeployDocs)
def cc = new ChangeLogBuilder('CC-Changelog', changeLog, mvnChangeLog)

def da = new HealthCheckJobBuilder('DA-Stage-HealthCheck', stageHealthCheck, 'health_check_stage.sh')
def build = new MasterBuildFlowBuilder('B-build-flow', 'TMS-.*-flow',
        """\
        def b = build ('${ba.name}')
        def build_number = b.build.number
        def revision = b.environment.get("GIT_COMMIT")
        parallel(
            { build ('${bc.name}', ENV: "itest") 
             build ('${bd.name}', GIT_COMMIT: revision) },
            { build ('${bc.name}', ENV: "test" )
             build ('${ca.name}', ARTIFACT_BUILD_NUMBER: build_number, GIT_COMMIT: revision, SUDO_PWD: "", ENV: "test") }
        )
        build ('${bb.name}', GIT_COMMIT: revision, MERGE_STABLE: true)
        """.stripIndent())

//Release
def ea = new HealthCheckJobBuilder('EA-Prod-HealthCheck', prodHealthCheck, 'health_check.sh')
def eb = new ReleaseJobBuilder('EB-Release', mvnRelease)
def ec = new ReleaseMinorVersionJobBuilder('EC-Release-Minor-Version', releaseMinorVersion, 'releaseMinorVersion.sh')

//Manually Triggered Jobs
def fa = new DatabaseBackupJobBuilder('FA-Database-Backup-Prod', databaseBackUp)
def fb = new DatabaseRestoreJobBuilder('FB-Database-Restore-Stage', databaseRestore)



[aa, ab, ac, ba, bb, bc, bd, ca, cb, cc, da, ea, eb, ec, fa, fb, build]*.build(this as DslFactory)

