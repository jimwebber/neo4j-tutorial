#!/bin/bash

if [ `uname` != "Darwin" ];
	  then echo "This sorry excuse for a script can only be run on Mac OSX, sorry.";
	  exit 1;
fi

if [ -z $1 ] || [ -z $2 ]	;
	then echo "Usage: make-usb-sticks <volume> <mount point > <tutorial source>";
	echo "E.g. make-usb-sticks /dev/disk1 ~/neo4j-tutorial";
	exit 1;
fi

diskutil eraseVolume fat32 NEO4J $1s1
diskutil mountDisk $1

cp -r $2 "`diskutil info $1 | grep "Mount Point" | awk -F: {'print $2'}`"

diskutil unmountDisk $1

