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

import java.util.Collection;

import javax.xml.namespace.QName;

import org.w3c.dom.Element;

/**
 *  Indicates that an error occurred during the processing of a SAML profile, and
 *  the error is not retryable in the context of the profile
 *
 * @author     Scott Cantor
 * @created    January 17, 2003
 */
public class FatalProfileException extends ProfileException implements Cloneable
{
    /**
     *  Creates a new FatalProfileException
     *
     * @param  e    The root of a DOM tree
     * @exception  SAMLException   Raised if an exception occurs while constructing
     *                              the object.
     */
    protected FatalProfileException(Element e)
        throws SAMLException
    {
        super(e);
    }

    /**
     *  Creates a new FatalProfileException
     *
     * @param  msg    The detail message
     */
    public FatalProfileException(String msg)
    {
        super(msg);
    }

    /**
     *  Creates a new FatalProfileException
     *
     * @param  msg    The detail message
     * @param  e      The exception to be wrapped in a FatalProfileException
     */
    public FatalProfileException(String msg, Exception e)
    {
        super(msg,e);
    }

    /**
     *  Creates a new FatalProfileException
     *
     * @param  codes  A collection of QNames
     * @param  msg    The detail message
     */
    public FatalProfileException(Collection codes, String msg)
    {
        super(codes,msg);
    }

    /**
     *  Creates a new FatalProfileException wrapping an existing exception <p>
     *
     *  The existing exception will be embedded in the new one, and its message
     *  will become the default message for the FatalProfileException.</p>
     *
     * @param  codes  A collection of QNames
     * @param  e      The exception to be wrapped in a FatalProfileException
     */
    public FatalProfileException(Collection codes, Exception e)
    {
        super(codes,e);
    }

    /**
     *  Creates a new FatalProfileException from an existing exception. <p>
     *
     *  The existing exception will be embedded in the new one, but the new
     *  exception will have its own message.</p>
     *
     * @param  codes  A collection of QNames
     * @param  msg    The detail message
     * @param  e      The exception to be wrapped in a FatalProfileException
     */
    public FatalProfileException(Collection codes, String msg, Exception e)
    {
        super(codes,msg,e);
    }

    /**
     *  Creates a new FatalProfileException
     *
     * @param  code   A status code
     * @param  msg    The detail message
     */
    public FatalProfileException(QName code, String msg)
    {
        super(code,msg);
    }

    /**
     *  Creates a new FatalProfileException wrapping an existing exception <p>
     *
     *  The existing exception will be embedded in the new one, and its message
     *  will become the default message for the FatalProfileException.</p>
     *
     * @param  code   A status code
     * @param  e      The exception to be wrapped in a FatalProfileException
     */
    public FatalProfileException(QName code, Exception e)
    {
        super(code,e);
    }

    /**
     *  Creates a new FatalProfileException from an existing exception. <p>
     *
     *  The existing exception will be embedded in the new one, but the new
     *  exception will have its own message.</p>
     *
     * @param  code   A status code
     * @param  msg    The detail message
     * @param  e      The exception to be wrapped in a FatalProfileException
     */
    public FatalProfileException(QName code, String msg, Exception e)
    {
        super(code,msg,e);
    }
}

