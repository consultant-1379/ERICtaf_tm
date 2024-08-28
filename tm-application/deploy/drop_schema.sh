#!/bin/sh
case "$ENV" in
test )
  HOST=atclvm791.athtem.eei.ericsson.se
  USER=tmuser
  PASSWORD="$PASSWORD_TEST"
  PORT=3306
  SCHEMA=tafdb
  ;;
itest )
  HOST=taftmitests-mysql-838-mysql-1389.seli.gic.ericsson.se
  USER=taftmitest
  PASSWORD=D5tfHa19Ce0N6dF
  PORT=6446
  SCHEMA=taftmitest
  ;;
stage )
  HOST=taftestmanagementstage-mysql-1441.seli.gic.ericsson.se
  USER=taftest
  PASSWORD=SQ6lD8z5riU32qL
  PORT=6446
  SCHEMA=taftestmanagementstage-mysql-544
  ;;
esac

echo "Dropping and recreating schema: $SCHEMA"
mysql -h ${HOST} -p${PASSWORD} --port=${PORT} -u ${USER} ${SCHEMA} <<EOF
DROP DATABASE IF EXISTS ${SCHEMA};
CREATE DATABASE IF NOT EXISTS ${SCHEMA} DEFAULT CHARACTER SET utf8;
EOF
