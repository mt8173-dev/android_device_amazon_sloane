#!/system/bin/sh
PATH=/sbin:/system/sbin:/system/bin:/system/xbin

FILE="/data/.firstboot"

# SELinux enforce
setenforce 0

if [ -f $FILE  ]; then
  echo
else
  sleep 120
  # Keyboard at first boot
  settings --user 0 put secure show_ime_with_hard_keyboard 1
  # Bluetooth at first boot
  settings --user 0 put global bluetooth_on 1
  # Screen timeout
  settings --user 0 put system screen_off_timeout 900000
  # Bluetooth at first boot
  service call bluetooth_manager 8
  # No status bar
  settings --user 0 put global policy_control immersive.status=*
  echo 1 > $FILE
fi
