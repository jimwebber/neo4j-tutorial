#!/bin/bash

DISK=$1
SOURCE=$2

if [ `uname` != "Darwin" ];
	  then echo "This sorry excuse for a script can only be run on Mac OSX, sorry.";
	  exit 1;
fi

if [ -z $DISK ] || [ -z $SOURCE ]	;
	then echo "Usage: make-usb-sticks <volume> <mount point > <tutorial source>";
	echo "E.g. make-usb-sticks /dev/disk1 ~/neo4j-tutorial";
	exit 1;
fi

diskutil eraseDisk fat32 NEO4J MBR $1

echo Disk $DISK formatted, copying content from $SOURCE

if [ $? -ne 0 ];
	then echo "Something went wrong creating or mounting the fat32 USB stick";
	exit 1;
fi


MOUNT_POINT=`diskutil info ${DISK}s1 | grep "Mount Point" | awk -F: {'print $2'} | sed 's/^ *//g'`
cp -r $SOURCE "${MOUNT_POINT}"

diskutil eject $1

echo "Finished, please physically remove stick"
echo $'\cg'

