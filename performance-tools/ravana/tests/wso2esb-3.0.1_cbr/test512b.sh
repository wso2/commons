#!/bin/bash
mkdir ./tests/temp
mv ./scenario/wso2esb-3.0.1_cbr/message* ./tests/temp/
cp -f ./tests/temp/message512b.xml ./scenario/wso2esb-3.0.1_cbr/
mv ./conf/config.xml ./tests/temp
cp -f ./tests/wso2esb-3.0.1_cbr/config_512b.xml ./conf/config.xml
./Test.pl
mv ./tests/temp/config.xml ./conf
mv ./tests/temp/* ./scenario/wso2esb-3.0.1_cbr/
rm -rf tests/temp
