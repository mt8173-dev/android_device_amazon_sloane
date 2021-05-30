echo $1
rootdirectory="$PWD"
# ---------------------------------

dirs="frameworks/av hardware/libhardware system/bt system/core hardware/interfaces"

for dir in $dirs ; do
	cd $rootdirectory
	cd $dir
	echo "Applying $dir patches..."
	git apply $rootdirectory/device/amazon/sloane/patches/$dir/*.patch
	echo " "
done

# -----------------------------------
echo "Changing to build directory..."
cd $rootdirectory
