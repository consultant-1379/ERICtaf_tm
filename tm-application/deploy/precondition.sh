#!/bin/sh

URL="http://atclvm791.athtem.eei.ericsson.se/tm-server/api/version"
SLEEP_TIMEOUT=1
SPENT_TIME=0

while true; do
    CODE=$(curl -s -o /dev/null -w "%{http_code}" --head $URL)
    if [ "$CODE" -eq "200" ];
    then
        echo "Application is up and running."
        break
    else
        echo "Application unavailable with code: $CODE"
    fi

    sleep ${SLEEP_TIMEOUT}
    SPENT_TIME=$(($SPENT_TIME + $SLEEP_TIMEOUT))
    if [ "$SPENT_TIME" -ge "$WAIT_TIMEOUT" ];
    then
        echo "Timeout limit reached. Break."
        break
    fi
done
echo
echo "Time spent waiting: $SPENT_TIME"

