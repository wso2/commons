#!/bin/bash
mkdir ./tests/temp
mv ./scenario/wso2esb-3.0.1_pass/message* ./tests/temp/
cp -f ./tests/temp/message100k.xml ./scenario/wso2esb-3.0.1_pass/
mv ./conf/config.xml ./tests/temp
cp -f ./tests/wso2esb-3.0.1_pass/config_100k.xml ./conf/config.xml
./Test.pl
mv ./tests/temp/config.xml ./conf
mv ./tests/temp/* ./scenario/wso2esb-3.0.1_pass/
rm -rf tests/temp
