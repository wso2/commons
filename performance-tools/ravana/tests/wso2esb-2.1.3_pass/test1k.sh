#!/bin/bash
mkdir ./tests/temp
mv ./scenario/wso2esb-2.1.3_pass/message* ./tests/temp/
cp -f ./tests/temp/message1k.xml ./scenario/wso2esb-2.1.3_pass/
mv ./conf/config.xml ./tests/temp
cp -f ./tests/wso2esb-2.1.3_pass/config_1k.xml ./conf/config.xml
./Test.pl
mv ./tests/temp/config.xml ./conf
mv ./tests/temp/* ./scenario/wso2esb-2.1.3_pass/
rm -rf tests/temp
