/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.mex;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.modules.Module;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;

/**
 * Metadata Exchange Module to support WS-MEX specification
 * 
 */
public class MetadataExchangeModule implements Module {

	private static final Log log = LogFactory.getLog(MetadataExchangeModule.class);

	public void engageNotify(AxisDescription axisDescription) throws AxisFault {
	 log.debug("metadataExchange module has been engaged to Service." );	
	}

	public void init(ConfigurationContext configContext, AxisModule module)
			throws AxisFault {
	
	}

	public void shutdown(ConfigurationContext configurationContext)
			throws AxisFault {
	

	}

	public void applyPolicy(Policy policy, AxisDescription axisDescription) throws AxisFault {
			
	}

	public boolean canSupportAssertion(Assertion assertion) {
		return false;
	}

}
