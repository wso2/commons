/*
 *  Copyright 2001-2005 Internet2
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml;

import java.io.FileInputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import junit.framework.TestCase;

/**
 * @author Scott Cantor
 *
 */
public class SAMLSubjectTest extends TestCase
{
    private String xmlpath = "data/org/opensaml/SAMLSubjectTest.xml";
    
    public SAMLSubjectTest(String arg0)
    {
        super(arg0);
		Logger.getRootLogger().setLevel(Level.OFF);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(SAMLSubjectTest.class);
    }

    public void testSAMLSubject() throws Exception
    {
        SAMLSubject obj = new SAMLSubject(new FileInputStream(xmlpath));
        SAMLNameIdentifierTest.testSAMLNameIdentifier(obj.getNameIdentifier());
        
        SAMLSubject obj2 = (SAMLSubject)obj.clone();
        SAMLNameIdentifierTest.testSAMLNameIdentifier(obj2.getNameIdentifier());
        
        assertEquals("subjects do not serialize equal",obj.toString(),obj2.toString());
    }
}
