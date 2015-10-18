#!/bin/sh

LOG=~/kiosk/weatherchannel.log

if [ $(pgrep -f 'sh /home/kiosk/kiosk/kiosk.sh') -ne $$ ]; then
	exit
fi

while :; do
	mv $LOG $LOG.bak
	groovy ~/kiosk/weatherchannel.groovy > $LOG
	sleep 1
	pkill chrome
done

