/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <stdio.h>
#include <axiom.h>
#include <axis2_util.h>
#include <axiom_soap.h>
#include <axis2_client.h>

axiom_node_t *
build_om_payload_for_echo_svc(const axutil_env_t *env);


int main(int argc, char** argv)
{
    const axutil_env_t *env = NULL;
    const axis2_char_t *address = NULL;
    axis2_endpoint_ref_t* endpoint_ref = NULL;
    axis2_options_t *options = NULL;
    const axis2_char_t *client_home = NULL;
    axis2_svc_client_t* svc_client = NULL;
    axiom_node_t *payload = NULL;
    axiom_node_t *ret_node = NULL;
    axiom_node_t *payload2 = NULL;
    axiom_node_t *ret_node2 = NULL;
    const axis2_char_t *un = NULL;
    const axis2_char_t *pw = NULL;
    /*axutil_allocator_t *allocator = NULL;*/

    /* Set up the environment */
    env = axutil_env_create_all("echo.log", AXIS2_LOG_LEVEL_TRACE);

    /* Set end point reference of echo service */
    address = "http://localhost:9090/axis2/services/echo";
    if (argc > 1)
    {
        if (axutil_strcmp(argv[1], "-h") == 0)
        {
            printf("Usage : %s [endpoint_url] (-auth [username] [password])\n",
                   argv[0]);
            printf("use -auth option for HTTP Authentication\n");
            printf("use -h for help\n");
            return 0;
        }
        else if (axutil_strcmp(argv[1], "-auth") == 0)
        {
            if (argc > 3)
            {
                un = argv[2];
                pw = argv[3];
            }
        }
        else
        {
            address = argv[1];
        }

        if(argc > 4)
        {
            if (axutil_strcmp(argv[2], "-auth") == 0)
            {
                un = argv[3];
                pw = argv[4];
            }
        }
    }
    printf("Using endpoint : %s\n", address);

    /* Create EPR with given address */
    endpoint_ref = axis2_endpoint_ref_create(env, address);

    /* Setup options */
    options = axis2_options_create(env);
    axis2_options_set_to(options, env, endpoint_ref);
    axis2_options_set_action(options, env,
            "http://ws.apache.org/axis2/c/samples/echoString");

    /* Set up deploy folder. It is from the deploy folder, the configuration is picked up
     * using the axis2.xml file.
     * In this sample client_home points to the Axis2/C default deploy folder. The client_home can 
     * be different from this folder on your system. For example, you may have a different folder 
     * (say, my_client_folder) with its own axis2.xml file. my_client_folder/modules will have the 
     * modules that the client uses
     */
    client_home = AXIS2_GETENV("WSFC_HOME");
    if (!client_home || !strcmp (client_home, ""))
        client_home = "../..";

    /* Create service client */
    svc_client = axis2_svc_client_create(env, client_home);
    if (!svc_client)
    {
        printf("Error creating service client, Please check WSFC_HOME again\n");
        AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Stub invoke FAILED: Error code:"
                " %d :: %s", env->error->error_number,
                AXIS2_ERROR_GET_MESSAGE(env->error));
		  return -1;
    }
    
    /* Set http-auth information */
    if (un && pw)
    {
		axutil_property_t *prop_pw = NULL;
        axutil_property_t *prop_un = axutil_property_create(env);
        axutil_property_set_value(prop_un, env, axutil_strdup(env, un));
        axis2_options_set_property(options, env, "HTTP_AUTH_USERNAME", prop_un);

        prop_pw = axutil_property_create(env);
        axutil_property_set_value(prop_pw, env, axutil_strdup(env, pw));
        axis2_options_set_property(options, env, "HTTP_AUTH_PASSWD", prop_pw);
    }

    /* Set service client options */
    axis2_svc_client_set_options(svc_client, env, options);

    /* Engage addressing module */
    axis2_svc_client_engage_module(svc_client, env, AXIS2_MODULE_ADDRESSING);



    /* Build the SOAP request message payload using OM API.*/
    payload = build_om_payload_for_echo_svc(env);

    /* Send request */
    ret_node = axis2_svc_client_send_receive(svc_client, env, payload);

    if (ret_node)
    {
        axis2_char_t *om_str = NULL;
        om_str = axiom_node_to_string(ret_node, env);
        if (om_str)
            printf("\nReceived OM : %s\n", om_str);
        printf("\necho client invoke SUCCESSFUL!\n");

        AXIS2_FREE(env->allocator, om_str);
        ret_node = NULL;
    }
    else
    {
        AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Stub invoke FAILED: Error code:"
                " %d :: %s", env->error->error_number,
                AXIS2_ERROR_GET_MESSAGE(env->error));
        printf("echo client invoke FAILED!\n");
    }

    payload2 = build_om_payload_for_echo_svc(env);
    ret_node2 = axis2_svc_client_send_receive(svc_client, env, payload2);
    if (ret_node2)
    {
        axis2_char_t *om_str = NULL;
        om_str = axiom_node_to_string(ret_node2, env);
        if (om_str)
            printf("\nReceived OM : %s\n", om_str);
        printf("\necho client invoke SUCCESSFUL!\n");

        AXIS2_FREE(env->allocator, om_str);
        ret_node2 = NULL;
    }
    else
    {
        AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Stub invoke FAILED: Error code:"
                " %d :: %s", env->error->error_number,
                AXIS2_ERROR_GET_MESSAGE(env->error));
        printf("echo client invoke FAILED!\n");
    }
    
    if (svc_client)
    {
        axis2_svc_client_free(svc_client, env);
        svc_client = NULL;
    }

    if (env)
    {
        axutil_env_free((axutil_env_t *) env);
        env = NULL;
    }

    return 0;
}

/* build SOAP request message content using OM */
axiom_node_t *
build_om_payload_for_echo_svc(const axutil_env_t *env)
{
    axiom_node_t *echo_om_node = NULL;
    axiom_element_t* echo_om_ele = NULL;
    axiom_node_t* text_om_node = NULL;
    axiom_element_t * text_om_ele = NULL;
    axiom_namespace_t *ns1 = NULL;
    axis2_char_t *om_str = NULL;

    ns1 = axiom_namespace_create(env, "http://ws.apache.org/axis2/services/echo", "ns1");
    echo_om_ele = axiom_element_create(env, NULL, "echoString", ns1, &echo_om_node);
    text_om_ele = axiom_element_create(env, echo_om_node, "text", NULL, &text_om_node);
    axiom_element_set_text(text_om_ele, env, "Hello World!", text_om_node);
    om_str = axiom_node_to_string(echo_om_node, env);

    if (om_str)
    {
        printf("\nSending OM : %s\n", om_str);
        AXIS2_FREE(env->allocator, om_str);
        om_str =  NULL;
    }
    return echo_om_node;
}
