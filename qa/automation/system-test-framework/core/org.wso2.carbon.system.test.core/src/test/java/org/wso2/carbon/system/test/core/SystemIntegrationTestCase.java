/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.system.test.core;

import junit.framework.TestCase;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;


public class SystemIntegrationTestCase extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if (FrameworkSettings.DEPLOYMENT_FRAMEWORK_ENABLE) {
            startServers();
        }
//        new UserPopulator().populateUsers();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void startServers() {
        if (!FrameworkSettings.getStratosTestStatus()) {
            ServerGroupManager.startServers();
        }
    }
}
