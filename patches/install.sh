#!/bin/sh

echo $1
rootdirectory="$PWD"
# ---------------------------------

dirs="system/core build packages/apps/TvSettings"

# red + nocolor
RED='\033[0;31m'
NC='\033[0m'

for dir in $dirs ; do
	cd $rootdirectory
	cd $dir
    echo -e "\n${RED}Applying ${NC}$dir ${RED}patches...${NC}\n"
	git apply -v $rootdirectory/device/amazon/sloane/patches/$dir/*.patch
done

# -----------------------------------
echo -e "Done !\n"
cd $rootdirectory
