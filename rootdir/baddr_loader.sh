#!/system/bin/sh

readonly BT_MAC_ADDR="/proc/idme/bt_mac_addr"
readonly BT_MAC_FILE="/data/misc/bluedroid/bdaddr"
PATH=/sbin:/system/sbin:/system/bin:/system/xbin

if [ -f ${BT_MAC_ADDR} ]; then
	bt_mac_idme=`cat $BT_MAC_ADDR`
	bt_mac=${bt_mac_idme:0:2}:${bt_mac_idme:2:2}:${bt_mac_idme:4:2}:${bt_mac_idme:6:2}:${bt_mac_idme:8:2}:${bt_mac_idme:10:2}
	echo $bt_mac > ${BT_MAC_FILE}
	setprop persist.service.bdroid.bdaddr $bt_mac
else
	setprop persist.service.bdroid.bdaddr "00:00:00:00:00:00" 
	echo "00:00:00:00:00:00" > ${BT_MAC_FILE}
fi