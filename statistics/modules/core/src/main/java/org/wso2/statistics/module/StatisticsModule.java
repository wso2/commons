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
package org.wso2.statistics.module;

import org.wso2.statistics.Counter;
import org.wso2.statistics.StatisticsConstants;
import org.wso2.statistics.module.ResponseTimeProcessor;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.modules.Module;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;


/**
 * Module for handling statistics
 */
public class StatisticsModule implements Module {
    public static ResponseTimeProcessor responseTimeProcessor;

    public void init(ConfigurationContext configContext,
                     AxisModule module) throws AxisFault {

        AxisConfiguration axisConfig = configContext.getAxisConfiguration();

        {
            Counter globalRequestCounter = new Counter();
            Parameter globalRequestCounterParam = new Parameter();
            globalRequestCounterParam.setName(StatisticsConstants.GLOBAL_REQUEST_COUNTER);
            globalRequestCounterParam.setValue(globalRequestCounter);
            axisConfig.addParameter(globalRequestCounterParam);
        }

        {
            Counter globalResponseCounter = new Counter();
            Parameter globalResponseCounterParam = new Parameter();
            globalResponseCounterParam.setName(StatisticsConstants.GLOBAL_RESPONSE_COUNTER);
            globalResponseCounterParam.setValue(globalResponseCounter);
            axisConfig.addParameter(globalResponseCounterParam);
        }

        {
            Counter globalFaultCounter = new Counter();
            Parameter globalFaultCounterParam = new Parameter();
            globalFaultCounterParam.setName(StatisticsConstants.GLOBAL_FAULT_COUNTER);
            globalFaultCounterParam.setValue(globalFaultCounter);
            axisConfig.addParameter(globalFaultCounterParam);
        }

        {
            Parameter startTime = new Parameter();
            startTime.setName(StatisticsConstants.SERVER_START_TIME);
            startTime.setValue(new Long(System.currentTimeMillis()));
            axisConfig.addParameter(startTime);
        }

        responseTimeProcessor = new ResponseTimeProcessor();
    }

    public void engageNotify(AxisDescription axisDescription)
            throws AxisFault {
    }

    /**
     * Evalute whether it can support the specified assertion and returns true if the assertion can
     * be supported.
     *
     * @param assertion the assertion that the module must decide whether it can support or not.
     * @return true if the specified assertion can be supported by the module
     */
    public boolean canSupportAssertion(Assertion assertion) {
        //TODO: Method implementation
        return true;
    }

    /**
     * Evaluates specified policy for the specified AxisDescription. It computes the configuration that
     * is appropriate to support the policy and stores it the appropriate description.
     *
     * @param policy the policy that is applicable for the specified AxisDescription
     * @throws org.apache.axis2.AxisFault if anything goes wrong.
     */
    public void applyPolicy(Policy policy, AxisDescription axisDescription) throws AxisFault {
        //TODO: Method implementation

    }

    public void shutdown(ConfigurationContext configurationContext)
            throws AxisFault {
    }
}
