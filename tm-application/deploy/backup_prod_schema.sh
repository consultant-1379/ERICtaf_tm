#!/bin/sh

currentTime=`date +"%m_%d_%Y"`
TEMP_FILE="/proj/PDU_OSS_CI_TAF_LOG/nam_tms/TMS_Backup/TMS_backup_${currentTime}.sql"
export PATH=/opt/SELIW/mysql/5.6.13/bin:$PATH

HOST=tms_prod-mysql-1442.seli.gic.ericsson.se
USER=taf_user
PASSWORD=9iBe10y4EtNzH6K
PORT=6446
SCHEMA=tms_prod

echo "Backing Up Database"
mysqldump --version
mysqldump --no-tablespaces -h ${HOST} -u ${USER} -p${PASSWORD} --port ${PORT} ${SCHEMA} > ${TEMP_FILE}

echo "Saving file to ${TEMP_FILE}"
