#!/bin/sh

./build.sh;

if [ -z $1 ]
	then
		java LineClient;
		echo "no argument"
	else
		java LineServer $1;
		"argument = $1"
fi
rm -rf lines;