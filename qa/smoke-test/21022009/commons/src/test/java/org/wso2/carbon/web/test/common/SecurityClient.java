package org.wso2.carbon.web.test.common;

import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.rampart.policy.model.RampartConfig;
import org.apache.rampart.policy.model.CryptoConfig;
import org.apache.rampart.RampartMessageData;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.callback.CallbackHandler;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;

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


public class SecurityClient implements CallbackHandler {

    public OMElement runSecurityClient(String scenarioid, String serviceName, String Namespace, String SoapAction, String operation, String param) throws Exception {

        Properties properties = new Properties();
        OMElement result = null;
        properties.load(new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties"));
        String carbon_home = properties.getProperty("carbon.home");
        String host_name = properties.getProperty("host.name");
        String http_port = properties.getProperty("http.port");
        String https_port = properties.getProperty("https.port");
        System.setProperty("javax.net.ssl.trustStore", carbon_home + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

        ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(carbon_home + File.separator + "repository", null);
        ServiceClient sc = new ServiceClient(ctx, null);
        sc.engageModule("rampart");
        sc.engageModule("addressing");

        Options opts = new Options();
        if (scenarioid.equals("scenario1")) {
            opts.setTo(new EndpointReference("https://" + host_name + ":" + https_port + "/services/" + serviceName));
        } else {
            opts.setTo(new EndpointReference("http://" + host_name + ":" + http_port + "/services/" + serviceName));
        }
        opts.setAction(SoapAction);
        try {
            opts.setProperty(RampartMessageData.KEY_RAMPART_POLICY, loadPolicy(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "policy" + File.separator + scenarioid + "-policy.xml"));
            //opts.setProperty(RampartMessageData.KEY_RAMPART_POLICY, loadPolicy("D:\\wsas\\wsas-3.0\\wso2wsas-3.0\\samples\\conf\\rampart\\scenario4-policy.xml"));
            //options.setSoapVersionURI(Constants.URI_SOAP12_ENV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sc.setOptions(opts);
            result = sc.sendReceive(CreateRequestReplyPayload(Namespace, operation, param));
            System.out.println(result.getFirstElement().getText());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Policy loadPolicy(String xmlPath) throws Exception {
        StAXOMBuilder builder = new StAXOMBuilder(xmlPath);
        Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());

        RampartConfig rc = new RampartConfig();

        rc.setUser("admin");
        rc.setUserCertAlias("qaclient");
        rc.setEncryptionUser("qaserver");
        rc.setPwCbClass(SecurityClient.class.getName());

        CryptoConfig sigCryptoConfig = new CryptoConfig();

        sigCryptoConfig.setProvider("org.apache.ws.security.components.crypto.Merlin");

        File file = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaclient.jks");

        if (!file.exists()) {
            throw new Exception("Key store cannot be found");
        }

        String filepath = file.getCanonicalPath();

        Properties prop1 = new Properties();
        prop1.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
        prop1.put("org.apache.ws.security.crypto.merlin.file", filepath);
        prop1.put("org.apache.ws.security.crypto.merlin.keystore.password", "qaclient");
        sigCryptoConfig.setProp(prop1);

        CryptoConfig encrCryptoConfig = new CryptoConfig();
        encrCryptoConfig.setProvider("org.apache.ws.security.components.crypto.Merlin");

        Properties prop2 = new Properties();

        prop2.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
        prop2.put("org.apache.ws.security.crypto.merlin.file", filepath);
        prop2.put("org.apache.ws.security.crypto.merlin.keystore.password", "qaclient");
        encrCryptoConfig.setProp(prop2);

        rc.setSigCryptoConfig(sigCryptoConfig);
        rc.setEncrCryptoConfig(encrCryptoConfig);

        policy.addAssertion(rc);
        return policy;
    }

    public OMElement CreateRequestReplyPayload(String NameSpace, String operation, String param) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(NameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        OMElement value = fac.createOMElement(param, omns);
        value.addChild(fac.createOMText(value, "Invoking Security Scenario!!!!"));
        OP1.addChild(value);
        return OP1;
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        WSPasswordCallback pwcb = (WSPasswordCallback) callbacks[0];
        String id = pwcb.getIdentifer();
        int usage = pwcb.getUsage();

        if (usage == WSPasswordCallback.USERNAME_TOKEN) {
            // Logic to get the password to build the username token
            if ("admin".equals(id)) {
                pwcb.setPassword("admin");
            }
        } else if (usage == WSPasswordCallback.SIGNATURE || usage == WSPasswordCallback.DECRYPT) {
            // Logic to get the private key password for signture or decryption
            if ("qaclient".equals(id)) {
                pwcb.setPassword("qaclient");
            }
        }
    }
}
