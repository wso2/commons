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


package org.wso2.carbon.mediator.clone.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;

public class TestRunner extends TestSuite {

    public static Test suite() throws Exception {
        FrameworkSettings.getProperty();
        String frameworkPath = FrameworkSettings.getFrameworkPath();
        System.setProperty("java.util.logging.config.file", frameworkPath + "/lib/log4j.properties");
        TestSuite testSuite = new TestSuite();

        /* String testName = "";
        Properties sysProps = System.getProperties();

        for (Enumeration e = sysProps.propertyNames(); e.hasMoreElements();) {

            String key = (String) e.nextElement();

            if (key.equals("test.suite")) {
                testName = System.getProperty("test.suite");
            }
        }  */
        testSuite.addTestSuite(CloneMediatorTest.class);
        // testSuite.addTestSuite(XpathTest.class);


        return testSuite;
    }
}
