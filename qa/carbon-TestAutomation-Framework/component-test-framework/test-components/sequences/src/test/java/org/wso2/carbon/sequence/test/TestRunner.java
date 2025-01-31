package org.wso2.carbon.sequence.test;

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

import java.util.Enumeration;
import java.util.Properties;

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.BACKENDSERVER_RUNNING;

public class TestRunner extends TestSuite {

    public static Test suite() throws Exception {
        FrameworkSettings.getProperty();
        TestSuite testSuite = new TestSuite();

        String testName = "";
        Properties sysProps = System.getProperties();

        for (Enumeration e = sysProps.propertyNames(); e.hasMoreElements();) {

            String key = (String) e.nextElement();

            if (key.equals("test.suite")) {
                testName = System.getProperty("test.suite");
            }
        }
        System.out.println("Axis2 server Started : " + BACKENDSERVER_RUNNING);
        System.out.println("Running all ESB tests.");

        if (BACKENDSERVER_RUNNING) {
            testSuite.addTestSuite(SequenceAdminServiceTest.class);
        }

        return testSuite;
    }
}