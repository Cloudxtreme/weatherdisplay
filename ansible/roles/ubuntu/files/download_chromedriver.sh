#!/bin/sh

TMP=/tmp/chromedriver.zip

wget -qO ${TMP} $1
cd /usr/local/bin
unzip ${TMP}
chmod 755 /usr/local/bin/chromedriver
rm ${TMP}

