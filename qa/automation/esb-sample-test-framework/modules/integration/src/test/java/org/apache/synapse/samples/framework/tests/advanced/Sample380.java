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
package org.apache.synapse.samples.framework.tests.advanced;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.samples.framework.SampleClientResult;
import org.apache.synapse.samples.framework.SynapseTestCase;
import org.apache.synapse.samples.framework.clients.StockQuoteSampleClient;

public class Sample380 extends SynapseTestCase {

    private static final Log log = LogFactory.getLog(Sample380.class);
    SampleClientResult result;
    StockQuoteSampleClient client;

    public Sample380() {
        super(380);
        client = getStockQuoteClient();
    }


    public void testCustomMediator() {
        String addUrl = "http://localhost:8280/";

        log.info("Running test: Demonstrate the use of Class mediator to extend the mediation functionality");
        result = client.requestStandardQuote(addUrl, null, null, "IBM" ,null);
        assertTrue("Client did not get run successfully ", result.responseReceived());
    }

}
