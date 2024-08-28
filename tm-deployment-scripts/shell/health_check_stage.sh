#!/bin/sh
check() {
 echo "verify $1"
    CODE=$(curl -sLI --insecure -w "%{http_code}\\n" "$1" -o /dev/null)
    if test $CODE -ne 200; then
        exit 1
    fi
}

check "https://taftmstage.seli.wh.rnd.internal.ericsson.com/"
check "https://taftmstage.seli.wh.rnd.internal.ericsson.com/tm-server/api/"