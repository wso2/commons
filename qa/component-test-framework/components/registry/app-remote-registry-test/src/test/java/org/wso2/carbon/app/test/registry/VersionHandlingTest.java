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

import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

public class VersionHandlingTest extends TestTemplate {
     public RemoteRegistry registry;

    @Override
    public void init() {
        InitializeAPI initializeAPI = new InitializeAPI();
        registry = initializeAPI.getRegistry(FrameworkSettings.CARBON_HOME,FrameworkSettings.HTTPS_PORT,FrameworkSettings.HTTP_PORT);
    }

    @Override
    public void runSuccessCase() {
        try {
            CreateVersionsTest();
            ResourceContentVersioningTest();
            ResourcePropertyVersioningTest();
            SimpleCollectionVersioningTest();
            ResourceRestoreTest();
            SimpleCollectionRestoreTest();
            AdvancedCollectionRestoreTest();
            PermaLinksForResourcesTest();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }

    public void CreateVersionsTest() throws RegistryException {

        Resource r1 = registry.newResource();
        r1.setContent("some content");
        registry.put("/version/r1", r1);

        //registry.createVersion("/version/r1");

        String[] r1Versions = registry.getVersions("/version/r1");
        assertEquals("/version/r1 should have 1 version.", r1Versions.length, 1);
//        assertTrue("Resource should have atleaset 1 version.", FileSystemImportExport.versionCount(r1Versions));

        Resource r1v2 = registry.get("/version/r1");
        r1v2.setContent("another content");
        registry.put("/version/r1", r1v2);

        //registry.createVersion("/version/r1");

        r1Versions = registry.getVersions("/version/r1");
        assertEquals("/version/r1 should have 2 version.", r1Versions.length, 2);
    }

    public void ResourceContentVersioningTest() throws RegistryException {

        Resource r1 = registry.newResource();
        r1.setContent("content 1");
        registry.put("/v2/r1", r1);

        Resource r12 = registry.get("/v2/r1");
        r12.setContent("content 2");
        registry.put("/v2/r1", r12);

        String[] r1Versions = registry.getVersions("/v2/r1");

        Resource r1vv1 = registry.get(r1Versions[1]);


        assertEquals("r1's first version's content should be 'content 1'",
                new String((byte[]) r1vv1.getContent()), "content 1");

        Resource r1vv2 = registry.get(r1Versions[0]);

        assertEquals("r1's second version's content should be 'content 2'",
                new String((byte[]) r1vv2.getContent()), "content 2");
    }

    public void ResourcePropertyVersioningTest() throws RegistryException {

        Resource r1 = registry.newResource();
        r1.setContent("content 1");
        r1.addProperty("p1", "v1");
        registry.put("/v4/r1", r1);

        Resource r1v2 = registry.get("/v4/r1");
        r1v2.addProperty("p2", "v2");
        registry.put("/v4/r1", r1v2);

        String[] r1Versions = registry.getVersions("/v4/r1");

        Resource r1vv1 = registry.get(r1Versions[1]);

        assertEquals("r1's first version should contain a property p1 with value v1",
                r1vv1.getProperty("p1"), "v1");

        Resource r1vv2 = registry.get(r1Versions[0]);

        assertEquals("r1's second version should contain a property p1 with value v1",
                r1vv2.getProperty("p1"), "v1");

        assertEquals("r1's second version should contain a property p2 with value v2",
                r1vv2.getProperty("p2"), "v2");
    }

    public void SimpleCollectionVersioningTest() throws RegistryException {

        Collection c1 = registry.newCollection();
        registry.put("/v3/c1", c1);

        registry.createVersion("/v3/c1");

        Collection c2 = registry.newCollection();
        registry.put("/v3/c1/c2", c2);

        registry.createVersion("/v3/c1");

        Collection c3 = registry.newCollection();
        registry.put("/v3/c1/c3", c3);

        registry.createVersion("/v3/c1");

        Collection c4 = registry.newCollection();
        registry.put("/v3/c1/c2/c4", c4);

        registry.createVersion("/v3/c1");

        Collection c5 = registry.newCollection();
        registry.put("/v3/c1/c2/c5", c5);

        registry.createVersion("/v3/c1");

        String[] c1Versions = registry.getVersions("/v3/c1");

        registry.get(c1Versions[0]);
        registry.get(c1Versions[1]);
        registry.get(c1Versions[2]);
    }

    public void ResourceRestoreTest() throws RegistryException {

        Resource r1 = registry.newResource();
        r1.setContent("content 1");
        registry.put("/test/v10/r1", r1);

        Resource r1e1 = registry.get("/test/v10/r1");
        r1e1.setContent("content 2");
        registry.put("/test/v10/r1", r1e1);

        Resource r1e2 = registry.get("/test/v10/r1");
        r1e2.setContent("content 3");
        registry.put("/test/v10/r1", r1e2);

        String[] r1Versions = registry.getVersions("/test/v10/r1");
        registry.restoreVersion(r1Versions[1]);

        Resource r1r1 = registry.get("/test/v10/r1");

        assertEquals("Restored resource should have content 'content 1'",
                "content 2", new String((byte[]) r1r1.getContent()));
    }

    public void SimpleCollectionRestoreTest() throws RegistryException {

        Collection c1 = registry.newCollection();
        registry.put("/test/v11/c1", c1);

        registry.createVersion("/test/v11/c1");

        Resource r1 = registry.newResource();
        r1.setContent("r1c1");
        registry.put("/test/v11/c1/r1", r1);

        registry.createVersion("/test/v11/c1");

        Resource r2 = registry.newResource();
        r2.setContent("r1c1");
        registry.put("/test/v11/c1/r2", r2);

        registry.createVersion("/test/v11/c1");

        String[] c1Versions = registry.getVersions("/test/v11/c1");
        assertEquals("/test/v11/c1 should have 3 versions.", c1Versions.length, 3);

        registry.restoreVersion(c1Versions[2]);
        Collection c1r1 = (Collection) registry.get("/test/v11/c1");
        assertEquals("version 1 of c1 should not have any children", 0, c1r1.getChildren().length);

        try {
            registry.get("/test/v11/c1/r1");
            fail("Version 1 of c1 should not have child r1");
        } catch (RegistryException e) {
        }

        try {
            registry.get("/test/v11/c1/r2");
            fail("Version 1 of c1 should not have child r2");
        } catch (RegistryException e) {
        }

        registry.restoreVersion(c1Versions[1]);
        Collection c1r2 = (Collection) registry.get("/test/v11/c1");
        assertEquals("version 2 of c1 should have 1 child", 1, c1r2.getChildren().length);

        try {
            registry.get("/test/v11/c1/r1");
        } catch (RegistryException e) {
            fail("Version 2 of c1 should have child r1");
        }

        try {
            registry.get("/test/v11/c1/r2");
            fail("Version 2 of c1 should not have child r2");
        } catch (RegistryException e) {

        }

        registry.restoreVersion(c1Versions[0]);
        Collection c1r3 = (Collection) registry.get("/test/v11/c1");
        assertEquals("version 3 of c1 should have 2 children", 2, c1r3.getChildren().length);

        try {
            registry.get("/test/v11/c1/r1");
        } catch (RegistryException e) {
            fail("Version 3 of c1 should have child r1");
        }

        try {
            registry.get("/test/v11/c1/r2");
        } catch (RegistryException e) {
            fail("Version 3 of c1 should have child r2");
        }
    }

    public void AdvancedCollectionRestoreTest() throws RegistryException {

        Collection c1 = registry.newCollection();
        registry.put("/test/v12/c1", c1);

        registry.createVersion("/test/v12/c1");

        Resource r1 = registry.newResource();
        r1.setContent("r1c1");
        registry.put("/test/v12/c1/c11/r1", r1);

        registry.createVersion("/test/v12/c1");

        Collection c2 = registry.newCollection();
        registry.put("/test/v12/c1/c11/c2", c2);

        registry.createVersion("/test/v12/c1");

        Resource r1e1 = registry.get("/test/v12/c1/c11/r1");
        r1e1.setContent("r1c2");
        registry.put("/test/v12/c1/c11/r1", r1e1);

        registry.createVersion("/test/v12/c1");

        String[] c1Versions = registry.getVersions("/test/v12/c1");
        assertEquals("c1 should have 4 versions", c1Versions.length, 4);

        registry.restoreVersion(c1Versions[3]);

        try {
            registry.get("/test/v12/c1/c11");
            fail("Version 1 of c1 should not have child c11");
        } catch (RegistryException e) {
        }

        registry.restoreVersion(c1Versions[2]);

        try {
            registry.get("/test/v12/c1/c11");
        } catch (RegistryException e) {
            fail("Version 2 of c1 should have child c11");
        }

        try {
            registry.get("/test/v12/c1/c11/r1");
        } catch (RegistryException e) {
            fail("Version 2 of c1 should have child c11/r1");
        }

        registry.restoreVersion(c1Versions[1]);

        Resource r1e2 = null;
        try {
            r1e2 = registry.get("/test/v12/c1/c11/r1");
        } catch (RegistryException e) {
            fail("Version 2 of c1 should have child c11/r1");
        }

        try {
            registry.get("/test/v12/c1/c11/c2");
        } catch (RegistryException e) {
            fail("Version 2 of c1 should have child c11/c2");
        }

        String r1e2Content = new String((byte[]) r1e2.getContent());
        assertEquals("c11/r1 content should be 'r1c1", r1e2Content, "r1c1");

        registry.restoreVersion(c1Versions[0]);

        Resource r1e3 = registry.get("/test/v12/c1/c11/r1");
        String r1e3Content = new String((byte[]) r1e3.getContent());
        assertEquals("c11/r1 content should be 'r1c2", r1e3Content, "r1c2");
    }

    public void PermaLinksForResourcesTest() throws RegistryException {

        Resource r1 = registry.newResource();
        r1.setContent("r1c1");
        registry.put("/test/v13/r1", r1);

        String[] r1Versions = registry.getVersions("/test/v13/r1");

        Resource r1e1 = registry.get("/test/v13/r1");
        assertEquals("Permalink incorrect", r1e1.getPermanentPath(), r1Versions[0]);

        r1e1.setContent("r1c2");
        registry.put("/test/v13/r1", r1e1);

        r1Versions = registry.getVersions("/test/v13/r1");

        Resource r1e2 = registry.get("/test/v13/r1");
        assertEquals("Permalink incorrect", r1e2.getPermanentPath(), r1Versions[0]);

        registry.restoreVersion(r1Versions[1]);

        Resource r1e3 = registry.get("/test/v13/r1");
        assertEquals("Permalink incorrect", r1e3.getPermanentPath(), r1Versions[1]);
    }

//    public void testPermaLinksForCollections() throws RegistryException {
//
//        Collection c1 = registry.newCollection();
//        registry.put("/test/v14/c1", c1);
//
//        registry.createVersion("/test/v14/c1");
//
//        String[] c1Versions = registry.getVersions("/test/v14/c1");
//        Resource c1e1 = registry.get("/test/v14/c1");
//        assertEquals("Permalink incorrect", c1e1.getPermanentPath(), c1Versions[0]);
//
//        Resource r1 = registry.newResource();
//        r1.setContent("r1c1");
//        registry.put("/test/v14/c1/r1", r1);
//
//        for ( int i = 0; i <= 1000; i++){
//            registry.createVersion("/test/v14/c1/r1");
//            registry.resourceExists("/test/v14/c1/r1");
//        }
//
//        c1Versions = registry.getVersions("/test/v14/c1");
//        Resource c1e2 = registry.get("/test/v14/c1");
//        assertEquals("Permalink incorrect", c1e2.getPermanentPath(), c1Versions[0]);
//        //System.out.println(c1e2.getPermanentPath());
//
//        for (int i = 0; i < c1Versions.length; i++){
//            System.out.println(c1Versions[i]);
//        }
//
//        registry.restoreVersion(c1Versions[1]);
//
//        Resource c1e3 = registry.get("/test/v14/c1");
//        System.out.println("c1e3" + c1e3.getPermanentPath());
//        assertEquals("Permalink incorrect", c1e3.getPermanentPath(), c1Versions[1]);
//    }

//    public void testRootLevelVersioning() throws RegistryException {
//
//        Resource r1 = registry.newResource();
//        r1.setContent("r1c1");
//        registry.put("/vtr1", r1);
//
//        //registry.createVersion("/");
//
//        Collection c2 = registry.newCollection();
//        registry.put("/vtc2", c2);
//
//        //registry.createVersion("/");
//
//        String[] rootVersions = registry.getVersions("/");
//         System.out.println("test" + rootVersions[0]);
//        System.out.println(rootVersions[1]);
//
//        for (int i = 0; i < rootVersions.length; i++){
//            System.out.println(rootVersions[i]);
//        }
//
//        Collection rootv0 = (Collection) registry.get(rootVersions[0]);
//        String[] rootv0Choldren = (String[]) rootv0.getContent();
//
//
//
//        System.out.println(RegistryUtils.containsAsSubstring("/vtr1", rootv0Choldren));
//        System.out.println(RegistryUtils.getParentPath("/vtr1"));
//        System.out.println(RegistryUtils.getResourceName("/ama/test"));
//        System.out.println(RegistryUtils.getPureResourcePath("/ama/test/ooo"));
//        System.out.println(RegistryUtils.prepareGeneralPath("/ama/test/uuu"));
//
//
//        assertTrue("Root should have child vtr1",
//                RegistryUtils.containsAsSubstring("/vtr1", rootv0Choldren));
//        assertTrue("Root should have child vtc2",
//                RegistryUtils.containsAsSubstring("/vtc2", rootv0Choldren));
//
//        Collection rootv1 = (Collection) registry.get(rootVersions[1]);
//        String[] rootv1Choldren = (String[]) rootv1.getContent();
//        assertTrue("Root should have child vtr1",
//                RegistryUtils.containsAsSubstring("/vtr1", rootv1Choldren));
//        assertFalse("Root should not have child vtc2",
//                RegistryUtils.containsAsSubstring("/vtc2", rootv1Choldren));
//    }
}
