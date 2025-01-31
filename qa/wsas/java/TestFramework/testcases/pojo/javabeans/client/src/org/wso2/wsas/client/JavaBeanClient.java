package org.wso2.wsas.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.wso2.wsas.service.JavaBeanServiceStub;
/* This is a sample client to verify the java bean 
 * handling capability of WSO2 WSAS. 
 * Written by Charitha Kankanamge 
 */

public class JavaBeanClient {
	
	public static void main(String[] args)throws AxisFault {

JavaBeanServiceStub stub = new JavaBeanServiceStub();
JavaBeanServiceStub.SetName name = new JavaBeanServiceStub.SetName();
JavaBeanServiceStub.SetAge age = new JavaBeanServiceStub.SetAge();
JavaBeanServiceStub.SetDept dept = new JavaBeanServiceStub.SetDept();
JavaBeanServiceStub.SetEmpid empid = new JavaBeanServiceStub.SetEmpid();

//Setting values. 
//ToDo -- Get the values from user

name.setName("Charitha Kankanamge");
age.setAge(30);
dept.setDept("Quality Assurance");
empid.setEmpid("44");

try {
	stub.setAge(age);
	stub.setDept(dept);
	stub.setName(name);
	stub.setEmpid(empid);
} catch (RemoteException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}


try {
	JavaBeanServiceStub.GetAgeResponse ageresponse = stub.getAge();
	JavaBeanServiceStub.GetDeptResponse deptresponse = stub.getDept();
	JavaBeanServiceStub.GetNameResponse nameresponse = stub.getName();
	JavaBeanServiceStub.GetEmpidResponse empidresponse = stub.getEmpid();
	System.out.println("Employee name is " + nameresponse.get_return());
	System.out.println("Employee age is " + ageresponse.get_return());
	System.out.println("Employee department is " + deptresponse.get_return());
	System.out.println("Employee id is " + empidresponse.get_return());
} catch (RemoteException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}


				
		
	}

}
