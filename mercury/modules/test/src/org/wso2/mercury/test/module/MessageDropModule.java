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
package org.wso2.mercury.test.module;

import org.apache.axis2.modules.Module;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.AxisFault;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;


public class MessageDropModule implements Module {

    public static final String RELIABILITY = "reliability";
    public static final String COUNTINUE_CLIENT_IN_MESSAGES = "countineClientInMessages";

    // initialize the module
    public void init(ConfigurationContext configContext, AxisModule module)
            throws AxisFault {

        Parameter relaiablility = module.getParameter(RELIABILITY);
        System.out.println("Reliability ==> " + relaiablility.getValue());
        configContext.setProperty(RELIABILITY, new Double((String) relaiablility.getValue()));

        Parameter countinueClientInMessages = module.getParameter(COUNTINUE_CLIENT_IN_MESSAGES);
        System.out.println("Countinue ==> " + countinueClientInMessages.getValue());
        configContext.setProperty(COUNTINUE_CLIENT_IN_MESSAGES, countinueClientInMessages.getValue());


    }

    public void engageNotify(AxisDescription axisDescription)
            throws AxisFault {

    }

    public boolean canSupportAssertion(Assertion assertion) {
        return false;
    }

    public void applyPolicy(Policy policy, AxisDescription axisDescription)
            throws AxisFault {

    }

    // shutdown the module
    public void shutdown(ConfigurationContext configurationContext)
            throws AxisFault {

    }
}
