/**
 * Created by IntelliJ IDEA.
 * User: asela
 * Date: Oct 7, 2009
 * Time: 1:36:34 PM
 * To change this template use File | Settings | File Templates.
 */


import org.apache.neethi.Policy;
 import org.apache.neethi.PolicyEngine;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.axiom.om.OMElement;
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


public class SecurityClient implements CallbackHandler {


 public static void main(String srgs[]) {

        SecurityClient securityCl = new SecurityClient();
        OMElement result = null;
          try {
                result = securityCl.runSecurityClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(result.toString());

        }



    public OMElement runSecurityClient( ) throws Exception {

        Properties properties = new Properties();
        FileInputStream freader=new FileInputStream("."+File.separator+"src"+File.separator+"client.properties");
        properties.load(freader);
        String carbon_home = properties.getProperty("carbon.home");
        String client_repo  = properties.getProperty("client_repo");
        String endpoint_https    = properties.getProperty("endpoint_https");
        String endpoint_http   = properties.getProperty("endpoint_http");
        String client_home = properties.getProperty("client_home");
        int security_scenario_no =Integer.parseInt(properties.getProperty("security_scenario_no"));
        String key_path = properties.getProperty("key_path");

        OMElement result = null;

//        String security_policy_path =carbon_home + File.separator + "samples" + File.separator + "security_scenarios"+File.separator+"scenario"+security_scenario_no+"-policy.xml";
        String security_policy_path =client_home + File.separator + "security_scenarios"+File.separator+"scenario"+security_scenario_no+"-policy.xml";
        //System.out.println(security_policy_path);

        String SoapAction = "http://ode/bpel/unit-testAssign1.wsdl/TestAssignPortType/TestIn";
//        String body ="<p:hello xmlns:p=\"http://ode/bpel/unit-test.wsdl\"> <TestPart>Wso2</TestPart> </p:hello>";
        String body ="<p:testAssign xmlns:p=\"http://ode/bpel/unit-test.wsdl\">\n" +
                " <TestPart>test</TestPart>\n" +
                "</p:testAssign>";

        System.setProperty("javax.net.ssl.trustStore", carbon_home + File.separator + "repository" + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

        ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(client_repo, null);
        ServiceClient sc = new ServiceClient(ctx, null);
        sc.engageModule("rampart");
        sc.engageModule("addressing");

        Options opts = new Options();
        if(security_scenario_no==1)
        {
        opts.setTo(new EndpointReference(endpoint_https));
        }
        else
        {
         opts.setTo(new EndpointReference(endpoint_http));
        }
        opts.setAction(SoapAction);

        try {
            opts.setProperty(RampartMessageData.KEY_RAMPART_POLICY, loadPolicy(security_policy_path,key_path));

        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.setOptions(opts);

        result = sc.sendReceive(AXIOMUtil.stringToOM(body));
        System.out.println(result.getFirstElement().getText());
        return result;



    }

    public Policy loadPolicy(String xmlPath , String key_path) throws Exception {


        StAXOMBuilder builder = new StAXOMBuilder(xmlPath);
        Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());

        RampartConfig rc = new RampartConfig();

        rc.setUser("pavithra");
        rc.setUserCertAlias("client");
        rc.setEncryptionUser("service");
        rc.setPwCbClass(SecurityClient.class.getName());

        CryptoConfig sigCryptoConfig = new CryptoConfig();

        sigCryptoConfig.setProvider("org.apache.ws.security.components.crypto.Merlin");


        Properties prop1 = new Properties();
        prop1.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
        prop1.put("org.apache.ws.security.crypto.merlin.file", key_path);
        prop1.put("org.apache.ws.security.crypto.merlin.keystore.password", "apache");
        sigCryptoConfig.setProp(prop1);

        CryptoConfig encrCryptoConfig = new CryptoConfig();
        encrCryptoConfig.setProvider("org.apache.ws.security.components.crypto.Merlin");

        Properties prop2 = new Properties();

        prop2.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
        prop2.put("org.apache.ws.security.crypto.merlin.file", key_path);
        prop2.put("org.apache.ws.security.crypto.merlin.keystore.password", "apache");
        encrCryptoConfig.setProp(prop2);

        rc.setSigCryptoConfig(sigCryptoConfig);
        rc.setEncrCryptoConfig(encrCryptoConfig);

        policy.addAssertion(rc);
        return policy;
    }



    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        WSPasswordCallback pwcb = (WSPasswordCallback) callbacks[0];
        String id = pwcb.getIdentifer();
        int usage = pwcb.getUsage();

        if (usage == WSPasswordCallback.USERNAME_TOKEN) {
            // Logic to get the password to build the username token

                 if ("pavithra".equals(id)) {
                pwcb.setPassword("pavithra");
            }
        } else if (usage == WSPasswordCallback.SIGNATURE || usage == WSPasswordCallback.DECRYPT) {
            // Logic to get the private key password for signture or decryption
            if ("client".equals(id)) {
                pwcb.setPassword("apache");}
            if ("service".equals(id)) {
                pwcb.setPassword("apache");
            }
        }
    }
}