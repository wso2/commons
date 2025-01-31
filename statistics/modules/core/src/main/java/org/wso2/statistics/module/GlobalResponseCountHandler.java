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

import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.Parameter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.statistics.StatisticsConstants;
import org.wso2.statistics.Counter;

/**
 * Handler to count all responses from services
 */
public class GlobalResponseCountHandler extends AbstractHandler {
    private static Log log = LogFactory.getLog(GlobalResponseCountHandler.class);

    public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {
        Parameter parameter = msgContext.getParameter(StatisticsConstants.GLOBAL_RESPONSE_COUNTER);
        if (parameter != null) {
            ((Counter) parameter.getValue()).increment();
        } else {
            log.warn(StatisticsConstants.GLOBAL_RESPONSE_COUNTER + " is null");
        }
        return InvocationResponse.CONTINUE;
    }
}
