package org.wso2.mbtest.queue.producer;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class DefaultMessageProducer {
	
	public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
    private static final String CF_NAME_PREFIX = "connectionfactory.";
    private static final String QUEUE_NAME_PREFIX = "queue.";
    private static final String CF_NAME = "qpidConnectionfactory";
    String userName = "admin"; 
    String password = "admin";
    private static String CARBON_CLIENT_ID = "carbon";
    private static String CARBON_VIRTUAL_HOST_NAME = "carbon";
    private static String CARBON_DEFAULT_HOSTNAME = "204.13.85.2";
    private static String CARBON_DEFAULT_PORT = "5682";
    String queueName = "lanka";


 
    public static void main(String[] args) throws NamingException, JMSException {
    	DefaultMessageProducer queueSender = new DefaultMessageProducer();
        queueSender.sendMessages();
    }
    
    public void sendMessages() throws NamingException, JMSException {
    	
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
        properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));
        properties.put(QUEUE_NAME_PREFIX + queueName, queueName);
        
        Properties properties2 = new Properties();
        properties2.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
        properties2.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));
      
        
        System.out.println("getTCPConnectionURL(userName,password) = " + getTCPConnectionURL(userName, password));
        InitialContext ctx = new InitialContext(properties);
        
        
        // Lookup connection factory
        QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup(CF_NAME);
        QueueConnection queueConnection = connFactory.createQueueConnection();
        queueConnection.start();
        QueueSession queueSession =  queueConnection.createQueueSession(false,QueueSession.AUTO_ACKNOWLEDGE );//
   
        Queue queue = (Queue)ctx.lookup(queueName);
      
	    javax.jms.QueueSender queueSender = queueSession.createSender(queue);
	
	
	//sending 100 messages to the above created queue here
	for(int i=1;i<=1000;i=i+1){
        		
		TextMessage textMessage = queueSession.createTextMessage("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://services.samples\" xmlns:xsd=\"http://services.samples/xsd\"><soapenv:Header/><soapenv:Body><ser:getFullQuote><!--Optional:--><ser:request><!--Optional:--><xsd:symbol>WSO2</xsd:symbol></ser:request></ser:getFullQuote></soapenv:Body></soapenv:Envelope>");
         
		queueSender.send(textMessage);
	      
     }

        queueSender.close();
        queueSession.close();
        queueConnection.close();


    }
    public String getTCPConnectionURL(String username, String password) {
        // amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}'
        return new StringBuffer()
                .append("amqp://").append(username).append(":").append(password)
                .append("@").append(CARBON_CLIENT_ID)
                .append("/").append(CARBON_VIRTUAL_HOST_NAME)
                .append("?brokerlist='tcp://").append(CARBON_DEFAULT_HOSTNAME).append(":").append(CARBON_DEFAULT_PORT).append("'")
                .toString();
    }
 

}
