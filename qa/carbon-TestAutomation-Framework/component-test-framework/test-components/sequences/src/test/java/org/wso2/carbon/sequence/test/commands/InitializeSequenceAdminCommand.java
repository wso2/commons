package org.wso2.carbon.sequence.test.commands;

/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import junit.framework.TestCase;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.sequences.ui.types.SequenceAdminServiceStub;

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.HOST_NAME;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.HTTPS_PORT;

public class InitializeSequenceAdminCommand extends TestCase {

    private static final Log log = LogFactory.getLog(InitializeSequenceAdminCommand.class);
    // Test for normal success case

    public SequenceAdminServiceStub executeAdminStub(String sessionCookie) {
        log.debug("sessionCookie:" + sessionCookie);
        FrameworkSettings.getProperty();
        String serviceURL = "https://" + HOST_NAME + ":" + HTTPS_PORT + "/services/SequenceAdminService";

        SequenceAdminServiceStub sequenceAdminServiceStub = null;

        try {
            sequenceAdminServiceStub = new SequenceAdminServiceStub(serviceURL);

            ServiceClient client = sequenceAdminServiceStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
        } catch (AxisFault axisFault) {
            fail("Unexpected exception thrown");
            axisFault.printStackTrace();
        }

        log.info("endpointAdminStub created");
        return sequenceAdminServiceStub;

    }
}