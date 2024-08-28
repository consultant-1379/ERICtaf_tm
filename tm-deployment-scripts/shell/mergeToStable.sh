#!bin/sh
git checkout stable || git checkout -b stable
git reset --hard origin/stable
git merge $GIT_COMMIT || git merge $(git rev-parse origin/$GIT_COMMIT)
git remote set-url origin ssh://gerrit.ericsson.se:29418/OSS/com.ericsson.cifwk/ERICtaf_tm