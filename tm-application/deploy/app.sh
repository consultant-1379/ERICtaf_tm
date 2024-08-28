#!/bin/sh
source `dirname $0`/env.sh
CLIENT_ARTIFACT=tm-client
SERVER_ARTIFACT=tm-server

echo "Uploading artifacts"
${SSHPASS} -p "$PASSWORD" scp tm-application/tm-server/target/${SERVER_ARTIFACT}-*.war ${USER}@${HOST}:/tmp/tms/${SERVER_ARTIFACT}.war
${SSHPASS} -p "$PASSWORD" scp tm-application/tm-client/target/${CLIENT_ARTIFACT}-*.tar.gz ${USER}@${HOST}:/tmp/tms/${CLIENT_ARTIFACT}.tar.gz

echo "Running deployment commands"
${SSHPASS} -p "$PASSWORD" ssh ${USER}@${HOST}<<EOF
${TOMCAT_CMD} stop

echo "Moving client static"
rm -rf ${STATIC_DIR}
mkdir -p ${STATIC_DIR}
tar -xzf /tmp/tms/${CLIENT_ARTIFACT}.tar.gz -C ${STATIC_DIR}
rm -f /tmp/tms/${CLIENT_ARTIFACT}.tar.gz

echo "Moving web app"
rm -rf ${WEBAPP_DIR}/${SERVER_ARTIFACT}
mv -f /tmp/tms/${SERVER_ARTIFACT}.war ${WEBAPP_DIR}/

${TOMCAT_CMD} start
EOF
