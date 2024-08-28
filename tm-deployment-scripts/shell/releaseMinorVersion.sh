#!bin/sh
export currentVersion=`/proj/lciadm100/tools/apache-maven-3.0.5/bin/mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version|grep "^[0-9]*\.[0-9]*\.[0-9]*"`
export cleanedVersion=`echo $currentVersion | sed -e 's/[^0-9][^0-9]*$//'`
export incrementalVersion=`echo $cleanedVersion | cut -d"." -f3`
export minorVersion=`echo $cleanedVersion | cut -d"." -f2`
export majorVersion=`echo $cleanedVersion | cut -d"." -f1`
export nextminorVersion=$(($minorVersion+1))

if [ "${Version}" != "calculateMe" ]
    then
        echo '[INFO] A new TMS version was specified, the poms will be updated'
        find . -name "pom.xml" -exec sed -i "s/${currentVersion}/${Version}/g" {} \; 2>/dev/null
        echo Version=$Version > newTAFVersion.txt
    else
        echo [INFO] The TMS version has been requested to be calculated
        newVersion=`echo $majorVersion.$nextminorVersion.1`
        find . -name "pom.xml" -exec sed -i "s/${currentVersion}/${newVersion}-SNAPSHOT/g" {} \; 2>/dev/null
        echo Version=$newVersion > newTAFVersion.txt
fi