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

package org.wso2.uri.template.parser;

import org.wso2.uri.template.URITemplateException;

import java.util.Map;

public class Literal extends Node {

    private int tokenLength = 0;

    public Literal(String token) throws URITemplateException {
        super(token);
        tokenLength = token.length();
        if (tokenLength == 0) {
            throw new URITemplateException("Invalid literal token with zero length");
        }
    }

    @Override
    String expand(Map<String, String> variables) {
        return token;
    }

    @Override
    int match(String uriFragment, Map<String, String> variables) {
        if (uriFragment.length() < tokenLength) {
            return -1;
        }

        for (int i = 0; i < tokenLength; i++) {
            if (token.charAt(i) != uriFragment.charAt(i)) {
                return -1;
            }
        }
        return tokenLength;
    }

    @Override
    String getToken() {
        return token;
    }

    @Override
    char getFirstCharacter() {
        return token.charAt(0);
    }
}
