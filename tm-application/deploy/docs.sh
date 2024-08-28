#!/bin/sh
source `dirname $0`/env.sh
DOCS_PATH=tm-application/tm-server-api/target
DOCS_ZIP=docs.zip

echo "Uploading docs"
${SSHPASS} -p "$PASSWORD" scp ./${DOCS_PATH}/${DOCS_ZIP} ${USER}@${HOST}:/tmp/${DOCS_ZIP}

echo "Running deployment commands"
${SSHPASS} -p "$PASSWORD" ssh ${USER}@${HOST} <<EOF
echo "Moving REST docs"
rm -rf ${STATIC_DIR}/docs
mkdir -p ${STATIC_DIR}/docs
unzip /tmp/${DOCS_ZIP} -d ${STATIC_DIR}/docs/
rm -f /tmp/${DOCS_ZIP}
EOF
