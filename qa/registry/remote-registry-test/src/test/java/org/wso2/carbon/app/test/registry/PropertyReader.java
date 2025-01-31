/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.app.test.registry;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;

public class PropertyReader {

    public static Properties loadRegistryProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("./conf/remoteregistry.properties"));
        //System.out.println(PropertyReader.loadRegistryProperties().getProperty("wokerClass"));
        return properties;

    }
}
