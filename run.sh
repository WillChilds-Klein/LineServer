#!/bin/sh

./scripts/build.sh;

cd bin;
if [ -z $1 ]
	then
		java LineClient;
	else
		java LineServer ../$1;
fi
rm -rf lines;
cd ..;