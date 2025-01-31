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
package org.wso2.carbon.interop.addressing;

import junit.framework.TestCase;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.wso2.carbon.interop.common.InteropConstants;
import org.wso2.carbon.interop.common.InteropSettings;


public class WSAddressingCRTest extends TestCase {

    protected ConfigurationContext configurationContext;

    protected void setUp() throws Exception {
        configurationContext =
                ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                        InteropSettings.getProperty(InteropConstants.AXIS2_REPO_PATH),
                        InteropSettings.getProperty(InteropConstants.AXIS2_XML_PATH));
    }

    public void tearDown() throws Exception {
        configurationContext.terminate();
    }
}
