package org.wso2.carbon.mediaotr.rule.test;

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
import org.wso2.carbon.mediation.configadmin.test.commands.ConfigServiceAdminStubCommand;
import org.wso2.carbon.mediation.configadmin.ui.ConfigServiceAdminStub;

import java.io.File;

public class RuleMediatorDroolsTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(RuleMediatorDroolsTest.class);

    @Override
    public void init() {
        log.info("Initializing Rule mediator Tests");
        log.debug("Rule mediator Tests Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running Rule mediator SuccessCase ");
        StockQuoteClient stockQuoteClient = new StockQuoteClient();
        OMElement result = null;

        try {
            ConfigServiceAdminStub configServiceAdminStub = new
                    ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);

            String xmlPath = frameworkPath + File.separator + "components" + File.separator + "mediators-rule"
                             + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "rule_drools.xml";
           
            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(omElement);

            if (FrameworkSettings.STRATOS.equalsIgnoreCase("false")) {
                result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT, null, "IBM");
            }
            if (FrameworkSettings.STRATOS.equalsIgnoreCase("true")) {
                result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + "/services/" + FrameworkSettings.TENANT_NAME + "/", null, "IBM");
            }

            log.info(result);
            System.out.println(result);
            //<ns:getQuoteResponse xmlns:ns="http://services.samples"><ns:return xmlns:ax25="http://services.samples/xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="ax25:GetQuoteResponse"><ax25:change>-2.9856545572035147</ax25:change><ax25:earnings>-8.939702046822939</ax25:earnings><ax25:high>88.93236214661339</ax25:high><ax25:last>86.02399603435495</ax25:last><ax25:lastTradeTimestamp>Wed Oct 27 17:51:37 IST 2010</ax25:lastTradeTimestamp><ax25:low>89.81397149274402</ax25:low><ax25:marketCap>5.7155207241712295E7</ax25:marketCap><ax25:name>IBM Company</ax25:name><ax25:open>-85.10913985065287</ax25:open><ax25:peRatio>25.022447053714327</ax25:peRatio><ax25:percentageChange>-3.0601982321946433</ax25:percentageChange><ax25:prevClose>97.56408999237709</ax25:prevClose><ax25:symbol>IBM</ax25:symbol><ax25:volume>7645</ax25:volume></ns:return></ns:getQuoteResponse>

            assert result != null;
            if (!result.toString().contains("IBM")) {
                Assert.fail("Rule  Mediator not invoked");
                log.error("Rule Mediator not invoked");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Rule Mediator doesn't work : " + e.getMessage());

        }


    }


    @Override
    public void runFailureCase
            () {
    }

    @Override
    public void cleanup() {
        ConfigServiceAdminStub configServiceAdminStub = new
                ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);
        new ConfigServiceAdminStubCommand(configServiceAdminStub).loadDefaultConfig();
    }
}

