#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" == 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    mvn deploy -Dcheckstyle.skip=true  -P sign,build-extras --settings cd/mvnsettings.xml
fi
