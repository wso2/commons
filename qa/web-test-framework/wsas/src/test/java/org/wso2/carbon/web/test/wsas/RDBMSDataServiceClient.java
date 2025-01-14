/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.carbon.web.test.wsas;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;

import javax.xml.namespace.QName;
import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

public class RDBMSDataServiceClient {
    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        FileInputStream freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        freader.close();
        return properties;
    }

    public String RDBMSClient(String epr, String operationName, String SoapAction, String NameSpace) throws Exception {

        ServiceClient sc = new ServiceClient();
        Options opts = new Options();
        opts.setTo(new EndpointReference(epr));
        opts.setAction(SoapAction);
        sc.setOptions(opts);
        OMElement result = sc.sendReceive(CreatePayload(operationName, NameSpace));
        System.out.println(result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice","name")));
        return result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice",  "name"));


    }

    public OMElement CreatePayload(String operationName, String Namespace) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(Namespace, "b");
        OMElement OP1 = fac.createOMElement(operationName, omns);

        return OP1;
    }

     public OMElement CreateRequestReplyPayload(String NameSpace, String operation, String param, String input) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(NameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        OMElement value = fac.createOMElement(param, omns);
        value.addChild(fac.createOMText(value, input));
        OP1.addChild(value);
        return OP1;
    }

    public String twoWayAnonClient(String epr, String operation,String SoapAction, String NameSpace,  String param, String input) throws Exception {

           ServiceClient sc = new ServiceClient();
           Options opts = new Options();
           opts.setTo(new EndpointReference(epr));
           opts.setAction(SoapAction);
           sc.setOptions(opts);
           OMElement result = sc.sendReceive(CreateRequestReplyPayload(NameSpace, operation, param, input));
           System.out.println(result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice","name")));
           return result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice",  "name"));

       }

}
