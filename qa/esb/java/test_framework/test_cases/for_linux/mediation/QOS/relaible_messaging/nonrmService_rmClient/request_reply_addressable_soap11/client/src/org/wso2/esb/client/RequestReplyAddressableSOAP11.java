package org.wso2.esb.client;

import java.rmi.RemoteException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

public class RequestReplyAddressableSOAP11 {

	public static void main(String[] args)throws AxisFault {
		

		ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(args[0],args[1]+"/axis2.xml");
		OMElement payload = createPayLoad();
		ServiceClient serviceclient = new ServiceClient(cc, null);   

		Options opts = new Options();
		opts.setTo(new EndpointReference(args[2]+"/RMProxy"));
		opts.setAction("urn:add");
		opts.setUseSeparateListener(true);

		serviceclient.setOptions(opts);
		serviceclient.engageModule("Mercury");
		serviceclient.engageModule("addressing");


		for (int i = 0; i < 4; i++) {
			try {
				OMElement response = serviceclient.sendReceive(payload);
				System.out.println(response);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
			//Setting the last message
			serviceclient.getOptions().setProperty("MercuryLastMessage", Constants.VALUE_TRUE);
			
			try {
				OMElement response = serviceclient.sendReceive(payload);	
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cc.getListenerManager().stop();
	}

	public static OMElement createPayLoad(){
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace("http://service.esb.wso2.org", "ns");
		OMElement method = fac.createOMElement("add", omNs);
		OMElement value1 = fac.createOMElement("x", omNs);
		OMElement value2 = fac.createOMElement("y", omNs);

		value1.addChild(fac.createOMText(value1, "10"));
		value2.addChild(fac.createOMText(value2, "10"));
		method.addChild(value1);
		method.addChild(value2);

		return method;
	}
}

