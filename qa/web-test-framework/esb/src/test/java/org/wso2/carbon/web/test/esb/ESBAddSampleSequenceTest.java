package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.*;
import org.wso2.carbon.web.test.esb.*;
import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

public class ESBAddSampleSequenceTest extends TestCase {
    Selenium selenium;

    public ESBAddSampleSequenceTest(Selenium _browser){
        selenium = _browser;
    }


    public void testNew() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBInMediatorTest esbInMediatorTest = new ESBInMediatorTest(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBOutMediatorTest esbOutMediatorTest = new ESBOutMediatorTest(selenium);

        //Loging in and creating the sequence
        esbCommon.addSequence("SampleSequence");

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child", "Filter", "In");
        esbInMediatorTest.addInMediator("0");

        //Adding the In > Log mediator
        esbCommon.addMediators("Add Child","0","Core","Log");
        esbLogMediatorTest.addLogMediator("0.0","Simple");
        esbCommon.mediatorUpdate();
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding the In > Send mediator
        esbCommon.addMediators("Add Child","0", "Core", "Send");
        //String endpointType, String radioSelection, String resourceName, String endpointURI, String format, String optimize, String errCode, String durSec, String maxDur, String factor,
        //String timoutErr, String retry, String retryDelay, String action, String actDur, String wsAddEnable, String sepLister, String wsSec, String secResource, String wsRm, String rmResource
        esbSendMediatorTest.addAnonSendMediator("0");
        esbSendMediatorTest.addMandInfoSendMediator("http://localhost:9000/services/SimpleStockQuoteService");
        esbSendMediatorTest.addFormatOptimizeInfoSendMediator("Leave As-Is", "Leave As-Is");
        esbSendMediatorTest.addSuspendInfoSendMediator("","","","1.0");
        esbSendMediatorTest.retryTimeoutInfoSendMediator("","","");
        esbSendMediatorTest.timeoutInfoSendMediator("Never timeout","");
        esbSendMediatorTest.eprQosInfoSendMediator(null,null,null,null,null,null);
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();                                    

        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding the Out mediator
        esbCommon.addRootLevelChildren("Add Sibling", "Filter", "Out");
        esbOutMediatorTest.addOutMediator("1");
        esbSequenceTreePopulatorTest.clickMediator("1");

        //Adding the Out > Log mediator
        esbCommon.addMediators("Add Child","1", "Core", "Log");
        esbSequenceTreePopulatorTest.clickMediator("1.0");

        //Adding the Out > Send mediator        
        esbCommon.addMediators("Add Child","1", "Core", "Send");
        esbSendMediatorTest.addNormalSendMediator("0");

        //Saving the sequence
        esbCommon.sequenceSave();
        }
}
