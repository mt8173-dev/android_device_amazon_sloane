#!/system/bin/sh

readonly BT_MAC_ADDR="/proc/idme/bt_mac_addr"
readonly BT_MAC_FILE="/data/misc/bluedroid/bdaddr"
PATH=/sbin:/system/sbin:/system/bin:/system/xbin

if [ -f ${BT_MAC_ADDR} ]; then
	echo "$(cat $BT_MAC_ADDR | sed 's/../:&/g' | cut -c2-)" > ${BT_MAC_FILE}
else
	echo "00:00:00:00:00:00" > ${BT_MAC_FILE}
fi