/*
 * Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.monitor.listener;

import org.wso2.monitor.mbeans.ServerMonitor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Context listener loads ServerMonitor MBean during deployment.
 */
public final class MonitorContextListener implements ServletContextListener {

    /**
     * Instantiates MBean implementation.
     * @param event context event.
     */
    public void contextInitialized(ServletContextEvent event) {
        ServerMonitor mBean = new ServerMonitor();
    }

    public void contextDestroyed(ServletContextEvent event) {

    }
}
