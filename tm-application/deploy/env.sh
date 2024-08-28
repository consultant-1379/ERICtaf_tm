#!/bin/sh
SSHPASS=/proj/PDU_OSS_CI_TAF/tools/sshpass-1.05/sshpass

case "$ENV" in
test )
  HOST=atclvm791.athtem.eei.ericsson.se
  USER=root
  PASSWORD="$PASSWORD_TEST"
  WEBAPP_DIR=/opt/tomcat/webapps
  STATIC_DIR=/var/www/html
  TOMCAT_CMD=/etc/init.d/tomcat
  ;;
stage )
  HOST=seliiuts02214.seli.gic.ericsson.se
  USER=tafuser
  PASSWORD=wREbruHERUqas4aw
  WEBAPP_DIR=/proj/pduosstaf/taftmstage/
  STATIC_DIR=/proj/pduosstaf/taftmstage-static
  TOMCAT_CMD=echo
  ;;
prod )
  HOST=seliiuts02214.seli.gic.ericsson.se
  USER=tafuser
  PASSWORD=wREbruHERUqas4aw
  WEBAPP_DIR=/proj/pduosstaf/taftm/
  STATIC_DIR=/proj/pduosstaf/taftmstatic
  TOMCAT_CMD=echo
  ;;
*)
  echo "Invalid environment: '$ENV'"
  exit 1
esac

echo "
Deployment environment:
HOST=${HOST}
USER=${USER}
WEBAPP_DIR=${WEBAPP_DIR}
STATIC_DIR=${STATIC_DIR}
TOMCAT_CMD=${TOMCAT_CMD}
"
