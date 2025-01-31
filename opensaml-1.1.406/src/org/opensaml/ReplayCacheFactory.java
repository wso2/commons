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

import java.lang.reflect.Constructor;

import org.w3c.dom.Element;

/**
 *  Factory for the ReplayCache interface
 *
 * @author     Scott Cantor
 * @created    February 3, 2005
 */
public class ReplayCacheFactory
{
    /**
     *  Obtains a default provider of the ReplayCache interface
     * 
     * @return  The ReplayCache provider
     * @throws   NoSuchProviderException   Raised if an error occurs while obtaining an instance
     *      of the interface
     */
    public static ReplayCache getInstance() throws NoSuchProviderException {
        return getInstance(null, SAMLConfig.instance().getProperty("org.opensaml.provider.replaycache"));
    }

    /**
     *  Obtains a specific provider of the ReplayCache interface
     * 
     * @param provider  Name of the provider class to build
     * @return  The ReplayCache provider
     * @throws   NoSuchProviderException   Raised if an error occurs while obtaining an instance
     *      of the interface
     */
    public static ReplayCache getInstance(String provider) throws NoSuchProviderException {
        return getInstance(null, provider);
    }

    /**
     *  Obtains a default provider of the ReplayCache interface
     * 
     * @param e     A DOM element as input to configuring the provider
     * @return  The ReplayCache provider
     * @throws   NoSuchProviderException   Raised if an error occurs while obtaining an instance
     *      of the interface
     */
    public static ReplayCache getInstance(Element e) throws NoSuchProviderException {
        return getInstance(e, SAMLConfig.instance().getProperty("org.opensaml.provider.replaycache"));
    }

    /**
     *  Obtains a specific provider of the ReplayCache interface
     * 
     * @param e     A DOM element as input to configuring the provider
     * @param provider  Name of the provider class to build
     * @return  The ReplayCache provider
     * @throws   NoSuchProviderException   Raised if an error occurs while obtaining an instance
     *      of the interface
     */
    public static ReplayCache getInstance(Element e, String provider) throws NoSuchProviderException {
        try {
            Class implementation = Class.forName(provider);
            Class[] paramtypes = {Element.class};
            Object[] params = {e};
            Constructor ctor = implementation.getDeclaredConstructor(paramtypes);
            return (ReplayCache)ctor.newInstance(params);
        }
        catch (Exception ex) {
            throw new NoSuchProviderException(
                    "getInstance() unable to build instance of cache provider (" + provider + "): " + ex.getMessage(), ex
                    );
        }
    }
}
