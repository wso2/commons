/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.admin.service;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.governance.services.stub.AddServicesServiceStub;
import org.wso2.carbon.governance.services.stub.RegistryExceptionException;

import javax.activation.DataHandler;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

public class AdminServiceGovernanceService {
    private static final Log log = LogFactory.getLog(AdminServiceGovernanceService.class);

    private final String serviceName = "AddServicesService";
    private AddServicesServiceStub addServicesServiceStub;
    private String endPoint;

    public AdminServiceGovernanceService(String backEndUrl) throws AxisFault {
        this.endPoint = backEndUrl + serviceName;
        addServicesServiceStub = new AddServicesServiceStub(endPoint);
    }

    public void addService(String sessionCookie, DataHandler dh) throws RegistryExceptionException, IOException, XMLStreamException {
        new AuthenticateStub().authenticateStub(sessionCookie, addServicesServiceStub);
        XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(dh.getInputStream());
        //create the builder
        StAXOMBuilder builder = new StAXOMBuilder(parser);
        OMElement serviceElem = builder.getDocumentElement();
        addServicesServiceStub.addService(serviceElem.toString());
        log.info("Service Added");

    }


}
