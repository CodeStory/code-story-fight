#!/bin/bash

function alert_user {
	echo "${1}"
	if [ ! -z `which growlnotify` ]; then
		growlnotify `basename $0` -m "${1}"
	fi
}

function exit_ko {
	alert_user "${1}"
	exit 1
}

function exit_ok {
	alert_user "${1}"; exit 0
}

echo '--- CHECK FOR PROCFILE ---'
foreman check
if [ $? -ne 0 ]; then
	exit_ko "No procfile to be run with foreman"
fi


echo '--- BUILD ---'
mvn -B -e clean install -Dmaven.test.skip=true
if [ $? -ne 0 ]; then
	exit_ko "Unable to build"
fi

echo '--- CONFIGURE ---'
export PORT=8080

echo '--- STOP ---'
killall "foreman: master"

echo '--- START ---'
nohup foreman start < /dev/null > prod.log 2>&1 &
if [ $? -ne 0 ]; then
	exit_ko "Unable to start"
fi

exit_ok "Devoxx fight started on port #{PORT}"
