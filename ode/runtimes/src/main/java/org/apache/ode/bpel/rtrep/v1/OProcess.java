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
package org.apache.ode.bpel.rtrep.v1;

import org.apache.ode.bpel.rapi.ConstantsModel;
import org.apache.ode.bpel.rapi.ProcessModel;
import org.apache.ode.bpel.rapi.ActivityModel;
import org.apache.ode.bpel.rapi.PartnerLinkModel;
import org.apache.ode.bpel.rapi.PropertyAliasModel;
import org.apache.ode.bpel.rapi.ScopeModel;

import javax.wsdl.Operation;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.util.*;

/**
 * Compiled BPEL process representation.
 */
public class OProcess extends OBase implements ProcessModel {

    public static int instanceCount = 0;
    static final long serialVersionUID = -1L  ;

    public String guid;

    /** BPEL version. */
    public final String version;

    /** Various constants that are needed at runtime. */
    public OConstants constants;

    /** Universally Unique Identifier */
    public String uuid;

    /** Namespace of the process. */
    public String targetNamespace;

    /** Name of the process. */
    public String processName;

    /** ProcessImpl-level scope. */
    public OScope procesScope;

    /** All partner links in the process. */
    public final Set<OPartnerLink> allPartnerLinks = new HashSet<OPartnerLink>();

    public final List<OProperty> properties = new ArrayList<OProperty>();

    /** Date process was compiled. */
    public Date compileDate;

    int _childIdCounter = 0;

    List<OBase> _children = new ArrayList<OBase>();

    public final HashSet<OExpressionLanguage> expressionLanguages = new HashSet<OExpressionLanguage>();

    public final HashMap<QName, OMessageVarType> messageTypes = new HashMap<QName, OMessageVarType>();

    public final HashMap<QName, OElementVarType> elementTypes = new HashMap<QName, OElementVarType>();

    public final HashMap<QName, OXsdTypeVarType> xsdTypes = new HashMap<QName, OXsdTypeVarType>();

    public final HashMap<URI, OXslSheet> xslSheets = new HashMap<URI, OXslSheet>();

    public OProcess(String bpelVersion) {
        super(null);
        this.version = bpelVersion;
        instanceCount++;
    }

    public ActivityModel getChild(final int id) {
        for (int i=_children.size()-1; i>=0; i--) {
            OBase child = _children.get(i);
            if (child.getId() == id) return child;
            }
        return null;
    }

    public List<OBase> getChildren() {
        return _children;
    }

    public OScope getScope(String scopeName) {
        throw new UnsupportedOperationException();
    }


    public Set<? extends PartnerLinkModel> getAllPartnerLinks() {
        return Collections.unmodifiableSet(allPartnerLinks);
    }

    public PartnerLinkModel getPartnerLink(String name) {
        for (OPartnerLink partnerLink : allPartnerLinks) {
            if (partnerLink.getName().equals(name)) return partnerLink;
        }
        return null;
    }

    public String getName() {
        return processName;
    }

    public Collection getExpressionLanguages() {
        throw new UnsupportedOperationException(); // TODO: implement me!
    }

    public List<String> getCorrelators() {
        // MOVED from ProcessSchemaGenerator
        List<String> correlators = new ArrayList<String>();

        HashSet<OPartnerLink> copy = new HashSet(getAllPartnerLinks());
        for (OPartnerLink plink : copy) {
            if (plink.hasMyRole()) {
                for (Object o : plink.myRolePortType.getOperations()) {
                    Operation op = (Operation) o;
                    correlators.add(plink.getId() + "." + op.getName());
                }
            }
        }

        return correlators;
    }

    public static class OProperty extends OBase {

        static final long serialVersionUID = -1L  ;
        public final List<OPropertyAlias> aliases = new ArrayList<OPropertyAlias>();
        public QName name;

        public OProperty(OProcess process) { super(process); }

        public OPropertyAlias getAlias(OVarType messageType) {
            for (OPropertyAlias aliase : aliases)
                if (aliase.varType.equals(messageType))
                    return aliase;
            return null;
        }

        public String toString() {
            return "{OProperty " + name + "}";
        }
    }

    public static class OPropertyAlias extends OBase implements PropertyAliasModel {

        static final long serialVersionUID = -1L  ;

        public OVarType varType;

        /** For BPEL 1.1 */
        public OMessageVarType.Part part;

        public OExpression location;

        public OPropertyAlias(OProcess owner) {super(owner); }

        public String toString() {
            return "{OPropertyAlias " + getDescription() +  "}";
        }

        public String getDescription() {
            StringBuffer buf = new StringBuffer(varType.toString());
            buf.append('[');
            buf.append(part != null ? part.name : "");
            if (location != null) {
                buf.append("][");
                buf.append(location.toString());
            }
            buf.append(']');
            return buf.toString();
        }

    }

    public QName getQName() {
        return new QName(targetNamespace, processName);
    }


    protected void finalize() throws Throwable {
        instanceCount--;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // our "pseudo-constructor"
        in.defaultReadObject();
        instanceCount++;
    }

    public PartnerLinkModel getPartnerLink(int partnerLinkModelId) {
        return (PartnerLinkModel) getChild(partnerLinkModelId);
    }

    public String getGuid() {
        return guid;
    }

    public int getModelVersion() {
        return 1;
    }

    public ConstantsModel getConstantsModel() {
    	return new ConstantsModel() {

			public QName getConflictingReceive() {
				return constants.qnConflictingReceive;
			}

			public QName getCorrelationViolation() {
				return constants.qnCorrelationViolation;
			}

			public QName getDuplicateInstance() {
				// can't define new faults in this version of OConstants, so return null
				return null;
				// return constants.qnDuplicateInstance;
			}

			public QName getForEachCounterError() {
				return constants.qnForEachCounterError;
			}

			public QName getForcedTermination() {
				return constants.qnForcedTermination;
			}

			public QName getInvalidBranchCondition() {
				return constants.qnInvalidBranchCondition;
			}

			public QName getInvalidExpressionValue() {
				return constants.qnInvalidExpressionValue;
			}

			public QName getJoinFailure() {
				return constants.qnJoinFailure;
			}

			public QName getMismatchedAssignmentFailure() {
				return constants.qnMismatchedAssignmentFailure;
			}

			public QName getMissingReply() {
				return constants.qnMissingReply;
			}

			public QName getMissingRequest() {
				return constants.qnMissingRequest;
			}

			public QName getRetiredProcess() {
				// can't define new faults in this version of OConstants, so return null
				return null;
				// return constants.qnRetiredProcess;
			}

			public QName getSelectionFailure() {
				return constants.qnSelectionFailure;
			}

			public QName getSubLanguageExecutionFault() {
				return constants.qnSubLanguageExecutionFault;
			}

			public QName getUninitializedPartnerRole() {
				return constants.qnUninitializedPartnerRole;
			}

			public QName getUninitializedVariable() {
				return constants.qnUninitializedVariable;
			}

			public QName getUnknownFault() {
				// can't define new faults in this version of OConstants, so return null
				return null;
				// return constants.qnUnknownFault;
			}

			public QName getXsltInvalidSource() {
				return constants.qnXsltInvalidSource;
			}
    	};
    }

	public ScopeModel getProcessScope() {
		return procesScope;
	}
}
