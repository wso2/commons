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

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.mediation.artifactuploader.stub.SynapseArtifactUploaderAdminStub;

import javax.activation.DataHandler;
import java.rmi.RemoteException;


public class AdminServiceSynapseArtifactUploader {
    private static final Log log = LogFactory.getLog(AdminServiceSynapseArtifactUploader.class);

    private final String serviceName = "SynapseArtifactUploaderAdmin";
    private SynapseArtifactUploaderAdminStub synapseArtifactUploaderAdminStub;
    private String endPoint;

    public AdminServiceSynapseArtifactUploader(String backEndUrl) throws AxisFault {
        this.endPoint = backEndUrl + serviceName;
        synapseArtifactUploaderAdminStub = new SynapseArtifactUploaderAdminStub(endPoint);
    }


    public void uploadFile(String sessionCookie, String fileName, DataHandler dh) throws RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, synapseArtifactUploaderAdminStub);

        synapseArtifactUploaderAdminStub.uploadArtifact(fileName, dh);
        log.info("Artifact uploaded");

    }

    public void deleteFile(String sessionCookie, String fileName) throws RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, synapseArtifactUploaderAdminStub);

        synapseArtifactUploaderAdminStub.removeArtifact(fileName);
        log.info("Artifact Deleted");

    }

}
