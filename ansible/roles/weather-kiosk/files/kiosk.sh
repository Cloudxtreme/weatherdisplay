#!/bin/sh
#
# PROVIDE: kiosk
# REQUIRE: DAEMON
#

kiosk_enable=${chromekiosk_enable-"NO"}

. /etc/rc.subr

name=kiosk
rcvar=kiosk_enable
start_cmd=kiosk_start
stop_cmd=kiosk_stop

kiosk_start()
{
        sh -c 'echo "(sleep 5; /usr/local/bin/xinit -- :1) &" | su -l kiosk &' >/var/log/kiosk.log 2>%1 </dev/null
}

kiosk_stop()
{
        pkill -f 'X :1'
}

load_rc_config $name

run_rc_command $1
