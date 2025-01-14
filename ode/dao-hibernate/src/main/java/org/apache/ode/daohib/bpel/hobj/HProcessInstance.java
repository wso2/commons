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
package org.apache.ode.daohib.bpel.hobj;


import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Hibernate table representing a BPEL process instance.
 * 
 * @hibernate.class table="BPEL_INSTANCE" dynamic-update="true" lazy="true"
 * @hibernate.query name="COUNT_INSTANCES_BY_STATUS_AND_PROCESS_ID" query="select count(i.id) as cnt, max(i.activityFailureDateTime) as lastFailureDt from HProcessInstance as i where i.process.processId = :processId and i.state in(:states) and i.activityFailureCount > 0"
 * @hibernate.query name="COUNT_FAILED_INSTANCES_BY_STATUS_AND_PROCESS_ID" query="select count(i.id) as cnt, max(i.activityFailureDateTime) as lastFailureDt from HProcessInstance as i where i.process.processId = :processId and i.state in(:states) and i.activityFailureCount > 0"
 * @hibernate.query name="SELECT_ACTIVE_INSTANCES" query="from HProcessInstance as i where i.process.id = :processId and i.state = :state"
 * @hibernate.query name="DELETE_INSTANCES_BY_PROCESS" query="delete from HProcessInstance as p where p.process = :process"
 */
public class HProcessInstance extends HObject {
	public static final String SELECT_ACTIVE_INSTANCES="SELECT_ACTIVE_INSTANCES";
	public static final String DELETE_INSTANCES_BY_PROCESS="DELETE_INSTANCES_BY_PROCESS";
	public static final String COUNT_FAILED_INSTANCES_BY_STATUS_AND_PROCESS_ID="COUNT_FAILED_INSTANCES_BY_STATUS_AND_PROCESS_ID";
	public static final String COUNT_INSTANCeS_BY_STATUS_AND_PROCESS_ID="COUNT_INSTANCES_BY_STATUS_AND_PROCESS_ID";
	
    /** Foreign key to owner {@link HProcess}. */
    private HProcess _process;

    /** Foreign key to the instantiating {@link HCorrelator}. */
    private HCorrelator _instantiatingCorrelator;

    /** Scope instances belonging to this process instnace. */
    private Collection<HScope> _scopes = new HashSet<HScope>();

    private Collection<HCorrelationSet> _csets = new HashSet<HCorrelationSet>();

    /** Events belonging to this instance. */
    private Collection<HBpelEvent> _events = new HashSet<HBpelEvent>();

    private Set<HCorrelatorSelector> _correlatorSelectors = new HashSet<HCorrelatorSelector>();

    private Set<HMessageExchange> _messageExchanges = new HashSet<HMessageExchange>();

    private HFaultData _fault;

    private HLargeData _jacobState;

    private short _previousState;

    private short _state;

    private Date _lastActiveTime;

    private Date _activityFailureDateTime;

    private int _activityFailureCount;

    private Collection<HActivityRecovery> _activityRecoveries = new HashSet<HActivityRecovery>();

    private long _seq;

    private int _execStateCounter;

    /**
     *
     */
    public HProcessInstance() {
        super();
    }

    /**
     * @hibernate.many-to-one column="INSTANTIATING_CORRELATOR" foreign-key="none"
     */
    public HCorrelator getInstantiatingCorrelator() {
        return _instantiatingCorrelator;
    }

    public void setInstantiatingCorrelator(HCorrelator instantiatingCorrelator) {
        _instantiatingCorrelator = instantiatingCorrelator;
    }

    /**
     * @hibernate.many-to-one column="FAULT" column="FAULT" foreign-key="none"
     */
    public HFaultData getFault() {
        return _fault;
    }

    public void setFault(HFaultData fault) {
        _fault = fault;
    }

    /**
     * @hibernate.many-to-one column="JACOB_STATE" foreign-key="none"
     */
    public HLargeData getJacobState() {
        return _jacobState;
    }

    public void setJacobState(HLargeData jacobState) {
        _jacobState = jacobState;
    }

    /**
     * @hibernate.bag lazy="true" inverse="true"
     * @hibernate.collection-key column="IID" foreign-key="none"
     * @hibernate.collection-one-to-many class="org.apache.ode.daohib.bpel.hobj.HBpelEvent"
     */
    public Collection<HBpelEvent> getEvents() {
        return _events;
    }

    public void setEvents(Collection<HBpelEvent> events) {
        _events = events;
    }

    /**
     * @hibernate.set lazy="true" inverse="true"
     * @hibernate.collection-key column="PIID" foreign-key="none"
     * @hibernate.collection-one-to-many class="org.apache.ode.daohib.bpel.hobj.HCorrelatorSelector"
     */
    public Set<HCorrelatorSelector> getCorrelatorSelectors() {
        return _correlatorSelectors;
    }

    /**
     * @param selectors
     *            the _correlatorSelectors to set
     */
    public void setCorrelatorSelectors(Set<HCorrelatorSelector> selectors) {
        _correlatorSelectors = selectors;
    }

    /**
     * @hibernate.set lazy="true" inverse="true"
     * @hibernate.collection-key column="PIID" foreign-key="none"
     * @hibernate.collection-one-to-many class="org.apache.ode.daohib.bpel.hobj.HMessageExchange"
     */
    public Set<HMessageExchange> getMessageExchanges() {
        return _messageExchanges;
    }

    public void setMessageExchanges(Set<HMessageExchange> exchanges) {
        _messageExchanges = exchanges;
    }

    /**
     * @hibernate.property column="PREVIOUS_STATE"
     */
    public short getPreviousState() {
        return _previousState;
    }

    public void setPreviousState(short previousState) {
        _previousState = previousState;
    }

    /**
     *
     * @hibernate.many-to-one column="PROCESS_ID" lazy="false" outer-join="true" foreign-key="none"
     */
    public HProcess getProcess() {
        return _process;
    }

    public void setProcess(HProcess process) {
        _process = process;
    }

    /**
     * @hibernate.bag lazy="true" inverse="true"
     * @hibernate.collection-key column="PIID" foreign-key="none"
     * @hibernate.collection-one-to-many class="org.apache.ode.daohib.bpel.hobj.HScope"
     */
    public Collection<HScope> getScopes() {
        return _scopes;
    }

    public void setScopes(Collection<HScope> scopes) {
        _scopes = scopes;
    }

    /**
     * @hibernate.bag lazy="true" inverse="true"
     * @hibernate.collection-key column="PIID" foreign-key="none"
     * @hibernate.collection-one-to-many class="org.apache.ode.daohib.bpel.hobj.HCorrelationSet"
     */
    public Collection<HCorrelationSet> getCorrelationSets() {
        return _csets;
    }

    public void setCorrelationSets(Collection<HCorrelationSet> csets) {
        _csets = csets;
    }

    /**
     * @hibernate.property column="STATE"
     */
    public short getState() {
        return _state;
    }

    public void setState(short state) {
        _state = state;
    }

    /**
     * @hibernate.property column="LAST_ACTIVE_DT"
     */
    public Date getLastActiveTime() {
        return _lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime) {
        _lastActiveTime = lastActiveTime;
    }

    public void setSequence(long seq) {
        _seq = seq;
    }

    /**
     * @hibernate.property column="SEQUENCE"
     */
    public long getSequence() {
        return _seq;
    }

    /**
     * @hibernate.bag lazy="true" inverse="true" cascade="delete"
     * @hibernate.collection-key column="PIID" foreign-key="none"
     * @hibernate.collection-one-to-many class="org.apache.ode.daohib.bpel.hobj.HActivityRecovery"
     */
    public Collection<HActivityRecovery> getActivityRecoveries() {
        return _activityRecoveries;
    }

    public void setActivityRecoveries(Collection<HActivityRecovery> activityRecoveries) {
        _activityRecoveries = activityRecoveries;
    }

    /**
     * @hibernate.property column="FAILURE_COUNT"
     */
    public int getActivityFailureCount() {
        return _activityFailureCount;
    }

    public void setActivityFailureCount(int count) {
        _activityFailureCount = count;
    }

    /**
     * @hibernate.property column="FAILURE_DT"
     */
    public Date getActivityFailureDateTime() {
        return _activityFailureDateTime;
    }

    public void setActivityFailureDateTime(Date dateTime) {
        _activityFailureDateTime = dateTime;
    }

    /**
     * @hibernate.property column="EXEC_STATE_COUNT"
     * @return
     */
    public int getExecutionStateCounter() {
        return _execStateCounter;
    }

    public void setExecutionStateCounter(int stateCounter) {
        _execStateCounter = stateCounter;
        
    }

}
