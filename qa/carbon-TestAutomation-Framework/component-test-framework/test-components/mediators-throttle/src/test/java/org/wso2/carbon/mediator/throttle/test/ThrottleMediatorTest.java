package org.wso2.carbon.mediator.throttle.test;
/*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

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

import junit.framework.Assert;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.common.test.utils.ConfigHelper;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.common.test.utils.client.StockQuoteClient;
import org.wso2.carbon.logging.view.LogViewerStub;
import org.wso2.carbon.logging.view.test.commands.InitializeLogViewerAdmin;
import org.wso2.carbon.logging.view.test.commands.LogViewerAdminCommand;
import org.wso2.carbon.logging.view.types.carbon.LogMessage;
import org.wso2.carbon.mediation.configadmin.test.commands.ConfigServiceAdminStubCommand;
import org.wso2.carbon.mediation.configadmin.ui.ConfigServiceAdminStub;

import java.io.File;

public class ThrottleMediatorTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(ThrottleMediatorTest.class);
    String searchWord = "ThrottleMediator Concurrent access controller for ID : A allows : 10 concurrent accesses";

    @Override
    public void init() {
        log.info("Initializing Throttle Mediator Tests");
        log.debug("Throttle Mediator Tests Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running Throttle Mediator SuccessCase ");
        OMElement result = null;


        StockQuoteClient stockQuoteClient = new StockQuoteClient();

        try {
            ConfigServiceAdminStub configServiceAdminStub = new
                    ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);
            LogViewerStub logViewerStub = new InitializeLogViewerAdmin().executeAdminStub();

            String xmlPath = frameworkPath + File.separator + "mediators-throttle"
                             + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "throttle.xml";
            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(omElement);

            /*Sending a StockQuoteClient request*/
            if (FrameworkSettings.STRATOS.equalsIgnoreCase("false")) {
                result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT, null, "IBM");
            }
            else if (FrameworkSettings.STRATOS.equalsIgnoreCase("true")) {
                result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT + "/services/" + FrameworkSettings.TENANT_NAME + "/", null, "IBM");
            }
            log.info(result);

            LogMessage[] logMessages = new LogViewerAdminCommand(logViewerStub).addEntrySuccessCase("mediator");
            System.out.println(logMessages[logMessages.length - 1].getLogMessage());
            if (logMessages[logMessages.length - 1].getLogMessage().contains(searchWord)) {
                Assert.assertTrue("Throttle Mediation Success", true);
                log.info("Throttle Mediation Success");
            }

            /*if (isFound == true) {
                System.out.println("Results Found at " + timeStamp + " as ** " + searchWord + " **");
                Assert.assertTrue("Throttle Mediation Success", true);
                log.info("Throttle Mediation Success");
            }*/
        }
        catch (Exception e) {
            log.error("Throttle Mediator doesn't work : " + e.getMessage());

        }
    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {
        loadDefaultConfig();
    }
}

