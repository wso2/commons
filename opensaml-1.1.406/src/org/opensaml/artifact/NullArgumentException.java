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

/* 
 * File:  NullArgumentException.java
 *
 */

package org.opensaml.artifact;

/**
 * <p>Indicates a null argument to a method or constructor.</p>
 *
 * @author Tom Scavo
 */
public class NullArgumentException extends IllegalArgumentException 
                                implements SAMLArtifactChecking  {

  /**
   * Constructs a <code>NullArgumentException</code> object
   * with a default detail message.
   */
  public NullArgumentException() {
    super( NULL_ARG_ERROR_MSG );
  }

  /**
   * Constructs a <code>NullArgumentException</code> object 
   * with the specified detail message.
   *
   * @param message the detail message
   */
  public NullArgumentException( String message ) {
    super( message );
  }

}

