/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.usermanager.custom.acegi;

import org.wso2.usermanager.Authenticator;
import org.wso2.usermanager.Realm;
import org.wso2.usermanager.UserManagerException;

import junit.framework.TestCase;

public class AcegiTest extends TestCase {

    public void testAcegi() throws UserManagerException {
        String contextFile = "//home/muthulee/downloads/um/usermanager/modules/core/src/test/resources/firstapp.xml";
        AcegiRealmConfig config = new AcegiRealmConfig();
        config.setAuthProviderId("authenticationProvider");
        config.setAuthenticationProviderBeanMappingFile(contextFile);
        Realm realm = new AcegiRealm();
        realm.init(config);
        Authenticator authenticator = realm.getAuthenticator();
        TestCase.assertTrue(authenticator.authenticate("dianne", "emu"));

        TestCase.assertTrue(authenticator.authenticate("marissa", "koala"));

    }

}
