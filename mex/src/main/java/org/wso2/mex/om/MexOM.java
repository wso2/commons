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

package org.wso2.mex.om;

import org.apache.axiom.om.OMElement;
import org.wso2.mex.MexConstants;

/**
 * Base class for objects implemented for elements defined in 
 * the WS-MEX spec.
 */

public abstract class MexOM implements IMexOM {

	
	public OMElement toOM() throws MexOMException {
		return null;
	}
	
	public String getNamespaceValue(){
		return MexConstants.Spec_2004_09.NS_URI;
	}

	public boolean isNamespaceSupported (String namespaceName) {
		boolean supported = false;
		supported = MexConstants.Spec_2004_09.NS_URI.equals(namespaceName);
		return supported;
	}
}
