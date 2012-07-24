/*
 * @(#)Result.java
 *
 * Copyright 2003-2005 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistribution of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 * 
 *   2. Redistribution in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for use in
 * the design, construction, operation or maintenance of any nuclear facility.
 */

package org.wso2.balana.ctx.xacml2;

import org.wso2.balana.*;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.ctx.Status;

import java.io.OutputStream;
import java.io.PrintStream;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wso2.balana.xacml2.Obligation;
import org.wso2.balana.xacml3.Advice;

/**
 * XACML 2 and 1 implementation of <code>AbstractResult</code>
 *
 */
public class Result extends AbstractResult {

    /**
     * the resourceId identifier or null if there is none
     */
    private String resourceId = null;


    public Result(int decision, Status status, Set<ObligationResult> obligationResults,
                  Set<Advice> advices, EvaluationCtx evaluationCtx) throws IllegalArgumentException {
        super(decision, status, obligationResults, advices, evaluationCtx);
    }

    public Result(int decision, Status status, Set<ObligationResult> obligationResults,
                  String resourceId) throws IllegalArgumentException {
        super(decision, status, obligationResults, null, null);
        this.resourceId = resourceId;
    }

    /**
     * Creates a new instance of a <code>Result</code> based on the given
     * DOM root node. A <code>ParsingException</code> is thrown if the DOM
     * root doesn't represent a valid ResultType.
     *
     * @param root the DOM root of a ResultType
     *
     * @return a new <code>Result</code>
     *
     * @throws ParsingException if the node is invalid
     */
    public static AbstractResult getInstance(Node root) throws ParsingException {
        
        int decision = -1;
        Status status = null;
        String resource = null;
        Set<ObligationResult> obligations = null;

        NamedNodeMap attrs = root.getAttributes();
        Node resourceAttr = attrs.getNamedItem("ResourceId");
        if (resourceAttr != null){
            resource = resourceAttr.getNodeValue();
        }
        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String name = node.getNodeName();

            if (name.equals("Decision")) {
                String type = node.getFirstChild().getNodeValue();
                for (int j = 0; j < DECISIONS.length; j++) {
                    if (DECISIONS[j].equals(type)) {
                        decision = j;
                        break;
                    }
                }

                if (decision == -1)
                    throw new ParsingException("Unknown Decision: " + type);
            } else if (name.equals("Status")) {
                if(status == null){
                    status = Status.getInstance(node);
                } else {
                    throw new ParsingException("More than one StatusType defined");      
                }
            } else if (name.equals("Obligations")) {
                if(obligations == null){
                    obligations = parseObligations(node);
                } else {
                    throw new ParsingException("More than one ObligationsType defined");    
                }
            }
        }

        return new Result(decision, status, obligations, resource);
    }
    
    /**
     * Helper method that handles the obligations
     *
     * @param root the DOM root of the ObligationsType XML type
     * @return a <code>Set</code> of <code>ObligationResult</code>
     * @throws ParsingException  if any issues in parsing DOM
     */
    private static Set<ObligationResult> parseObligations(Node root) throws ParsingException {

        Set<ObligationResult> set = new HashSet<ObligationResult>();
        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals("Obligation")){
                set.add(Obligation.getInstance(node));
            }
        }

        if (set.size() == 0){
            throw new ParsingException("ObligationsType must not be empty");
        }

        return set;
    }



    /**
     * Returns the resourceId to which this Result applies, or null if none is specified.
     * 
     * @return a resourceId identifier or null
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * Sets the resourceId identifier if it has not already been set before. The core code does not
     * set the resourceId identifier, so this is useful if you want to write wrapper code that needs
     * this information.
     * 
     * @param resource the resourceId identifier
     * 
     * @return true if the resourceId identifier was set, false if it already had a value
     */
    public boolean setResource(String resource) {
        if (this.resourceId != null){
            return false;
        }
        
        this.resourceId = resource;

        return true;
    }

    @Override
    public void encode(OutputStream output, Indenter indenter) {
        PrintStream out = new PrintStream(output);
        String indent = indenter.makeString();

        indenter.in();
        String indentNext = indenter.makeString();

        // encode the starting tag
        if (resourceId == null)
            out.println(indent + "<Result>");
        else
            out.println(indent + "<Result ResourceId=\"" + resourceId + "\">");

        // encode the decision
        out.println(indentNext + "<Decision>" + DECISIONS[decision] + "</Decision>");

        // encode the status
        if (status != null)
            status.encode(output, indenter);

        // encode the obligations
        if (obligations != null && obligations.size() != 0) {
            out.println(indentNext + "<Obligations>");

            Iterator it = obligations.iterator();
            indenter.in();

            while (it.hasNext()) {
                ObligationResult obligation = (ObligationResult) (it.next());
                obligation.encode(output, indenter);
            }

            indenter.out();
            out.println(indentNext + "</Obligations>");
        }

        indenter.out();

        // finish it off
        out.println(indent + "</Result>");
    }
}