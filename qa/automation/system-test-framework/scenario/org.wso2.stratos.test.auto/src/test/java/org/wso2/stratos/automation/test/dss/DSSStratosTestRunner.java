/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.stratos.automation.test.dss;

import junit.framework.Test;
import junit.framework.TestSuite;

public class DSSStratosTestRunner extends TestSuite {

    public static Test suite() {
        TestSuite testSuite = new TestSuite();

        testSuite.addTestSuite(CSVDataServiceTestClient.class);
        testSuite.addTestSuite(ExcelDataServiceTestClient.class);
        testSuite.addTestSuite(GSpreadDataServiceTestClient.class);
        testSuite.addTestSuite(MySqlDataServiceTestClient.class);
        testSuite.addTestSuite(CarbonDataSourceTestClient.class);
        testSuite.addTestSuite(DTPServiceTestClient.class);
        testSuite.addTestSuite(EventingServiceTestClient.class);
        testSuite.addTestSuite(NestedQueryTestClient.class);
        testSuite.addTestSuite(TestBatchRequestTestClient.class);
        testSuite.addTestSuite(TestMySqlFileServiceClient.class);
        testSuite.addTestSuite(ResourcesServiceTestClient.class);
        testSuite.addTestSuite(SecureDataServiceTestClient.class);
        testSuite.addTestSuite(InputParametersValidationTest.class);
        testSuite.addTestSuite(MySqlStoredProcedureTest.class);
        testSuite.addTestSuite(FaultyServiceTest.class);
        testSuite.addTestSuite(EditFaultyDataServiceTest.class);
        testSuite.addTestSuite(ServiceFaultyTest.class);

        return testSuite;
    }
}
