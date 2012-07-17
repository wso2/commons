/*
*  Copyright (c) 2005-2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.siddhi.core.util;

import org.wso2.siddhi.core.event.Event;

import java.util.Arrays;

public class EventPrinter {

//    public static String convertToString(ComplexEvent complexEvent) {
//        return "";
//    }
    //    public static void print(ComplexEvent complexEvent) {
//        System.out.println(convertToString(complexEvent));
//    }
    public static void print(Event[] events) {
        System.out.println(Arrays.deepToString(events));
    }


    public static void print(long timeStamp, Event[] inEvents, Event[] removeEvents,
                             Event[] faultEvents) {
        StringBuilder sb = new StringBuilder();
        sb.append("Events{ @timeStamp = ").append(timeStamp).
                append(", inEvents = ").append( Arrays.deepToString(inEvents)).
                append(", RemoveEvents = ").append(Arrays.deepToString(removeEvents)).
                append(", FaultEvents = ").append(Arrays.deepToString(faultEvents)).append(" }");
        System.out.println(sb.toString());
    }

}