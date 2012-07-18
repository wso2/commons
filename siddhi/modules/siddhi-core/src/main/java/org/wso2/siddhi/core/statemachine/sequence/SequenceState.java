/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
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
package org.wso2.siddhi.core.statemachine.sequence;

import org.wso2.siddhi.core.stream.packer.sequence.SequenceStreamPacker;
import org.wso2.siddhi.core.stream.recevier.sequence.SequenceSingleStreamReceiver;
import org.wso2.siddhi.query.api.stream.SingleStream;

public class SequenceState {

    private SingleStream singleStream;
    private int stateNumber;
//    private List<SequenceState> nextStates = new ArrayList<SequenceState>();
    private SequenceState nextState =null;

    private boolean first = false;
    private boolean last = false;

    private SequenceSingleStreamReceiver sequenceSingleStreamReceiver;
    private SequenceStreamPacker sequenceStreamPacker;

    public SequenceState(int stateNumber, SingleStream singleStream) {
        this.stateNumber = stateNumber;
        this.singleStream = singleStream;
    }

    public SingleStream getSingleStream() {
        return singleStream;
    }

    public void setSingleStream(SingleStream singleStream) {
        this.singleStream = singleStream;
    }

    public int getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(int stateNumber) {
        this.stateNumber = stateNumber;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }


    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isFirst() {
        return first;
    }


    public SequenceSingleStreamReceiver getSequenceSingleStreamReceiver() {
        return sequenceSingleStreamReceiver;
    }

    public void setSequenceSingleStreamReceiver(
            SequenceSingleStreamReceiver sequenceSingleStreamReceiver) {
        this.sequenceSingleStreamReceiver = sequenceSingleStreamReceiver;
    }

    public SequenceStreamPacker getSequenceStreamPacker() {
        return sequenceStreamPacker;
    }

    public void setSequenceStreamPacker(SequenceStreamPacker sequenceStreamPacker) {
        this.sequenceStreamPacker = sequenceStreamPacker;
    }

    public SequenceState getNextState() {
        return nextState;
    }

    public void setNextState(SequenceState nextState) {
        this.nextState = nextState;
    }
}