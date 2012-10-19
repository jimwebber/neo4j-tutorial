#!/bin/bash

if [ -z $1 ] || [ -z $2 ] || [ -z $3 ];
	then echo "Usage: make-usb-sticks <volume> <mount point > <tutorial source>";
	echo "E.g. make-usb-sticks /dev/disk1 /Volumes/NEO4J ~/neo4j-tutorial";
	exit 1;
fi

diskutil unmount $1
#diskutil eraseDisk "MS-DOS FAT32" NEO4J disk1
diskutil eraseDisk "MS-DOS FAT32" NEO4J $1
#cp -r ~/Desktop/neo4j-tutorial/ /Volumes/NEO4J/
cp -r $3 $2
diskutil unmountDisk $2


