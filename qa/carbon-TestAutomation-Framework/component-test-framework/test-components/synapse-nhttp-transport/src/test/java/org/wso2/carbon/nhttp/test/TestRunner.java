package org.wso2.carbon.nhttp.test;
/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/

import junit.framework.Test;
import junit.framework.TestSuite;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;

public class TestRunner extends TestSuite {

    public static Test suite() throws Exception {
        FrameworkSettings.getProperty();
        String frameworkPath = FrameworkSettings.getFrameworkPath();
        System.setProperty("java.util.logging.config.file", frameworkPath + "/lib/log4j.properties");
        TestSuite testSuite = new TestSuite();

        testSuite.addTestSuite(HeaderProtocol_10Test.class);
        testSuite.addTestSuite(HeaderProtocol_11Test.class);
        testSuite.addTestSuite(ReturnServerIPTest.class);


        return testSuite;
    }
}
