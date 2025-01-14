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
package org.wso2.tracer.module;

import org.apache.axis2.modules.Module;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.AxisFault;
import org.apache.axis2.util.Loader;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;
import org.wso2.tracer.TracerConstants;
import org.wso2.tracer.module.DefaultTraceFilter;
import org.wso2.tracer.module.MemoryBasedTracePersister;
import org.wso2.tracer.module.TraceFilter;
import org.wso2.tracer.module.TracePersister;

/**
 * 
 */
public class TracerModule implements Module {
    public void init(ConfigurationContext configContext,
                     AxisModule axisModule) throws AxisFault {

        AxisConfiguration axisConfig;
        {
            axisConfig = configContext.getAxisConfiguration();
            Parameter traceFilterImplParam;
            if ((traceFilterImplParam =
                    axisConfig.getParameter(TracerConstants.TRACE_FILTER_IMPL)) != null) {

                String traceFilterImpl = ((String) traceFilterImplParam.getValue()).trim();
                TraceFilter traceFilter;
                try {
                    traceFilter = (TraceFilter) Loader.loadClass(axisModule.getModuleClassLoader(),
                                                                 traceFilterImpl).newInstance();
                } catch (Exception e) {
                    throw AxisFault.makeFault(e);
                }
                traceFilterImplParam.setValue(traceFilter);
                axisModule.addParameter(traceFilterImplParam);
            } else {
                axisModule.addParameter(new Parameter(TracerConstants.TRACE_FILTER_IMPL,
                                                      new DefaultTraceFilter()));
            }
        }

        {
            Parameter tracePersisterImplParam;
            if ((tracePersisterImplParam =
                    axisConfig.getParameter(TracerConstants.TRACE_PERSISTER_IMPL)) != null) {

                String tracePersisterImpl = ((String) tracePersisterImplParam.getValue()).trim();
                TracePersister tracePersister;
                try {
                    tracePersister =
                            (TracePersister) Loader.loadClass(axisModule.getModuleClassLoader(),
                                                              tracePersisterImpl).newInstance();
                } catch (Exception e) {
                    throw AxisFault.makeFault(e);
                }
                tracePersisterImplParam.setValue(tracePersister);
                axisModule.addParameter(tracePersisterImplParam);
            } else {
                MemoryBasedTracePersister tracePersister = new MemoryBasedTracePersister();
                axisModule.addParameter(new Parameter(TracerConstants.TRACE_PERSISTER_IMPL,
                                                      tracePersister));
                configContext.setProperty(TracerConstants.TRACE_PERSISTER_IMPL,
                                          tracePersister);
            }
        }
    }

    public void engageNotify(AxisDescription axisDescription) throws AxisFault {
    }

    public boolean canSupportAssertion(Assertion assertion) {
        return false;
    }

    public void applyPolicy(Policy policy, AxisDescription axisDescription) throws AxisFault {

    }

    public void shutdown(ConfigurationContext configurationContext) throws AxisFault {
    }
}
