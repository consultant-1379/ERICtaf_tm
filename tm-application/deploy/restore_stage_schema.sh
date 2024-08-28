#!/bin/sh

export PATH=/opt/SELIW/mysql/5.6.13/bin:$PATH

RESTORE_DIR="/proj/PDU_OSS_CI_TAF_LOG/nam_tms/TMS_Backup/"
LS_COMMAND="ls ${RESTORE_DIR}"
RESTORE_FILE=$($LS_COMMAND | sort -n | head -1)

HOST=taftestmanagementstage-mysql-1441.seli.gic.ericsson.se
USER=taftest
PASSWORD=SQ6lD8z5riU32qL
PORT=6446
SCHEMA=taftestmanagementstage-mysql-544


echo "Restoring db: $SCHEMA from: ${RESTORE_FILE}"
mysql -h ${HOST} -u ${USER} -p${PASSWORD} --port ${PORT} ${SCHEMA} < "$RESTORE_DIR$RESTORE_FILE"
