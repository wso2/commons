/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ode.axis2;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.extensions.http.HTTPAddress;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.xml.namespace.QName;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPFault;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ode.axis2.soapbinding.SoapMessageConverter;
import org.apache.ode.il.epr.EndpointFactory;
import org.apache.ode.il.epr.MutableEndpoint;
import org.apache.ode.il.epr.WSAEndpoint;
import org.apache.ode.bpel.iapi.BpelServer;
import org.apache.ode.bpel.iapi.EndpointReference;
import org.apache.ode.bpel.iapi.InvocationStyle;
import org.apache.ode.bpel.iapi.Message;
import org.apache.ode.bpel.iapi.MessageExchange;
import org.apache.ode.bpel.iapi.MyRoleMessageExchange;
import org.apache.ode.bpel.iapi.ProcessConf;
import org.apache.ode.utils.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A running service, encapsulates the Axis service, its receivers and our receivers as well.
 *
 * @author Matthieu Riou <mriou at apache dot org>
 */
public class ODEService {
    private static final Log __log = LogFactory.getLog(ODEService.class);

    public static final String TARGET_SESSION_ENDPOINT = "targetSessionEndpoint";
    public static final String CALLBACK_SESSION_ENDPOINT = "callbackSessionEndpoint";

    private AxisService _axisService;
    private BpelServer _server;
    private ProcessConf _pconf;
    private Definition _wsdlDef;
    private QName _serviceName;
    private String _portName;
    private WSAEndpoint _serviceRef;
    private SoapMessageConverter _converter;

    public ODEService(AxisService axisService, ProcessConf pconf, QName serviceName, String portName, BpelServer server) throws AxisFault {
        _axisService = axisService;
        _server = server;
        _serviceName = serviceName;
        _pconf = pconf;
        _wsdlDef = pconf.getDefinitionForService(serviceName);
        _portName = portName;
        _serviceRef = EndpointFactory.convertToWSA(createServiceRef(genEPRfromWSDL(_wsdlDef, serviceName, portName)));
        _converter = new SoapMessageConverter(_wsdlDef, serviceName, portName);
    }

    public void onAxisMessageExchange(MessageContext msgContext, MessageContext outMsgContext, SOAPFactory soapFactory)
            throws AxisFault {
        MyRoleMessageExchange odeMex = null;
        try {
            // Creating mesage exchange
            String messageId = new GUID().toString();
            odeMex = _server.createMessageExchange(InvocationStyle.UNRELIABLE, _serviceName,
                    msgContext.getAxisOperation().getName().getLocalPart(), "" + messageId);

            __log.debug("ODE routed to operation " + odeMex.getOperation() + " from service " + _serviceName);

            if (odeMex.getOperation() == null) {
                String errmsg = "Call to " + _serviceName + "." + odeMex.getOperationName() + " was not routable.";
                __log.error(errmsg);
                throw new OdeFault(errmsg);
            }

            // Preparing message to send to ODE
            Message odeRequest = odeMex.createMessage(odeMex.getOperation().getInput().getMessage().getQName());
            _converter.parseSoapRequest(odeRequest, msgContext.getEnvelope(), odeMex.getOperation());
            readHeader(msgContext, odeMex);

            if (__log.isDebugEnabled()) {
                __log.debug("Invoking ODE using MEX " + odeMex);
                __log.debug("Message content:  " + DOMUtils.domToString(odeRequest.getMessage()));
            }

            odeMex.setRequest(odeRequest);
            odeMex.setTimeout(resolveTimeout());
            if (__log.isDebugEnabled()) __log.debug("Blocking invoke with mex " + odeMex.getMessageExchangeId() + ".");
            odeMex.invokeBlocking();


            if (odeMex.getOperation().getOutput() != null && outMsgContext != null) {
                SOAPEnvelope envelope = soapFactory.getDefaultEnvelope();
                outMsgContext.setEnvelope(envelope);

                // Hopefully we have a response
                __log.debug("Handling response for MEX " + odeMex);
                onResponse(odeMex, outMsgContext);
            }
        } catch (java.util.concurrent.TimeoutException te) {
            String errmsg = "Call to " + _serviceName + "." + odeMex.getOperationName() + " timed out(" + resolveTimeout() + " ms).";
            __log.error(errmsg, te);
            throw new OdeFault(errmsg);
        } catch (Exception e) {
            String errmsg = "Call to " + _serviceName + "." + odeMex.getOperationName() + " caused an exception.";
            __log.error(errmsg, e);
            throw new OdeFault(errmsg, e);
        } finally {
            // we cannot release ode mex here since it's outside of the transaction
        }
    }

    public boolean respondsTo(QName serviceName, QName portTypeName) {
        boolean result = _serviceName.equals(serviceName);
        result = result
                && _wsdlDef.getService(_serviceName).getPort(_portName).getBinding().getPortType().getQName().equals(
                portTypeName);
        return result;
    }

    /**
     * do not store the value so it can be dynamically updated
     */
    private long resolveTimeout() {
        String timeout = _pconf.getEndpointProperties(_serviceRef).get(Properties.PROP_MEX_TIMEOUT);
        if (timeout != null) {
            try {
                return Long.parseLong(timeout);
            } catch (NumberFormatException e) {
                if (__log.isWarnEnabled())
                    __log.warn("Mal-formatted Property: [" + Properties.PROP_MEX_TIMEOUT + "=" + timeout + "] Default value (" + Properties.DEFAULT_MEX_TIMEOUT + ") will be used");
            }
        }
        return Properties.DEFAULT_MEX_TIMEOUT;
    }

    private void onResponse(MyRoleMessageExchange mex, MessageContext msgContext) throws AxisFault {
        switch (mex.getAckType()) {
            case FAULT:
                if (__log.isDebugEnabled())
                    __log.debug("Fault response message: " + mex.getFault());
                SOAPFault fault = _converter.createSoapFault(mex.getFaultResponse().getMessage(), mex.getFault(), mex.getOperation());
                msgContext.getEnvelope().getBody().addFault(fault);

                if (__log.isDebugEnabled())
                    __log.debug("Returning fault: " + msgContext.getEnvelope().toString());
                break;
            case ONEWAY:
            case RESPONSE:
                _converter.createSoapResponse(msgContext, mex.getResponse(), mex.getOperation());
                if (__log.isDebugEnabled())
                    __log.debug("Response message " + msgContext.getEnvelope());
                writeHeader(msgContext, mex);
                break;
            case FAILURE:
                throw new OdeFault("Message exchange failure");
            default:
                throw new OdeFault("Received ODE message exchange in unexpected state: " + mex.getStatus());
        }
    }

    /**
     * Extracts endpoint information from Axis MessageContext (taken from WSA headers) to stuff them into ODE mesage exchange.
     */
    private void readHeader(MessageContext msgContext, MyRoleMessageExchange odeMex) {
        Object otse = msgContext.getProperty(TARGET_SESSION_ENDPOINT);
        Object ocse = msgContext.getProperty(CALLBACK_SESSION_ENDPOINT);
        if (otse != null) {
            Element serviceEpr = (Element) otse;
            WSAEndpoint endpoint = new WSAEndpoint();
            endpoint.set(serviceEpr);
            // Extract the session ID for the local process.
            odeMex.setProperty(MessageExchange.PROPERTY_SEP_MYROLE_SESSIONID, endpoint.getSessionId());
        }
        if (ocse != null) {
            Element serviceEpr = (Element) ocse;
            WSAEndpoint endpoint = new WSAEndpoint();
            endpoint.set(serviceEpr);

            // Save the session id of the remote process. Also, magically
            // initialize the EPR
            // of the partner to the EPR provided.
            odeMex.setProperty(MessageExchange.PROPERTY_SEP_PARTNERROLE_SESSIONID, endpoint.getSessionId());
            odeMex.setProperty(MessageExchange.PROPERTY_SEP_PARTNERROLE_EPR, DOMUtils.domToString(serviceEpr));
        }
    }

    /**
     * Handle callback endpoints for the case where partner contact process my-role which results in an "updated" my-role EPR due to
     * session id injection.
     */
    private void writeHeader(MessageContext msgContext, MyRoleMessageExchange odeMex) {
        EndpointReference targetEPR = odeMex.getEndpointReference();
        if (targetEPR == null)
            return;

        // The callback endpoint is going to be the same as the target
        // endpoint in this case, except that it is updated with session
        // information (if available).
        if (odeMex.getProperty(MessageExchange.PROPERTY_SEP_MYROLE_SESSIONID) != null) {
            WSAEndpoint sessionAwareEndPoint = new WSAEndpoint(_serviceRef);
            sessionAwareEndPoint.setSessionId(odeMex.getProperty(MessageExchange.PROPERTY_SEP_MYROLE_SESSIONID));
            msgContext.setProperty("callbackSessionEndpoint", sessionAwareEndPoint);
        }

    }

    public AxisService getAxisService() {
        return _axisService;
    }

    /**
     * Return the service-ref element that will be used to represent this endpoint.
     *
     * @return my service endpoint
     */
    public EndpointReference getMyServiceRef() {
        return _serviceRef;
    }

    /**
     * Get the EPR of this service from the WSDL.
     *
     * @param name     service name
     * @param portName port name
     * @return XML representation of the EPR
     */
    public static Element genEPRfromWSDL(Definition wsdlDef, QName name, String portName) {
        Service serviceDef = wsdlDef.getService(name);
        if (serviceDef != null) {
            Port portDef = serviceDef.getPort(portName);
            if (portDef != null) {
                Document doc = DOMUtils.newDocument();
                Element service = doc.createElementNS(Namespaces.WSDL_11, "service");
                service.setAttribute("name", serviceDef.getQName().getLocalPart());
                service.setAttribute("targetNamespace", serviceDef.getQName().getNamespaceURI());
                Element port = doc.createElementNS(Namespaces.WSDL_11, "port");
                service.appendChild(port);
                port.setAttribute("name", portDef.getName());
                port.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:bindns", portDef.getBinding().getQName()
                        .getNamespaceURI());
                port.setAttribute("bindns:binding", portDef.getName());
                for (Object extElmt : portDef.getExtensibilityElements()) {
                    if (extElmt instanceof SOAPAddress) {
                        Element soapAddr = doc.createElementNS(Namespaces.SOAP_NS, "address");
                        port.appendChild(soapAddr);
                        soapAddr.setAttribute("location", ((SOAPAddress) extElmt).getLocationURI());
                    } else if (extElmt instanceof HTTPAddress) {
                        Element httpAddr = doc.createElementNS(Namespaces.HTTP_NS, "address");
                        port.appendChild(httpAddr);
                        httpAddr.setAttribute("location", ((HTTPAddress) extElmt).getLocationURI());
                    } else {
                        port.appendChild(doc.importNode(((UnknownExtensibilityElement) extElmt).getElement(), true));
                    }
                }
                return service;
            }
        }
        return null;
    }

    /**
     * Create-and-copy a service-ref element.
     *
     * @param elmt
     * @return wrapped element
     */
    public static MutableEndpoint createServiceRef(Element elmt) {
        Document doc = DOMUtils.newDocument();
        QName elQName = new QName(elmt.getNamespaceURI(), elmt.getLocalName());
        // If we get a service-ref, just copy it, otherwise make a service-ref
        // wrapper
        if (!EndpointReference.SERVICE_REF_QNAME.equals(elQName)) {
            Element serviceref = doc.createElementNS(EndpointReference.SERVICE_REF_QNAME.getNamespaceURI(),
                    EndpointReference.SERVICE_REF_QNAME.getLocalPart());
            serviceref.appendChild(doc.importNode(elmt, true));
            doc.appendChild(serviceref);
        } else {
            doc.appendChild(doc.importNode(elmt, true));
        }

        return EndpointFactory.createEndpoint(doc.getDocumentElement());
    }
}
