/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.automation.selenium.test.greg;

import com.thoughtworks.selenium.Selenium;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebDriverException;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.BrowserManager;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.seleniumUtils.GRegBackEndURLEvaluator;
import org.wso2.carbon.system.test.core.utils.seleniumUtils.GregUserLogin;
import org.wso2.carbon.system.test.core.utils.seleniumUtils.GregUserLogout;
import org.wso2.carbon.system.test.core.utils.seleniumUtils.SeleniumScreenCapture;

public class GregApplyTagtoResourceSeleniumTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(GregApplyTagtoResourceSeleniumTest.class);
    private static Selenium selenium;
    private static WebDriver driver;
    String username;
    String password;

    @Override
    public void init() {
        testClassName = GregApplyTagtoResourceSeleniumTest.class.getName();
        String userId = new GregUserIDEvaluator().getTenantID();
        String baseUrl = new GRegBackEndURLEvaluator().getBackEndURL();
        log.info("baseURL is " + baseUrl);
        driver = BrowserManager.getWebDriver();
        selenium = new WebDriverBackedSelenium(driver, baseUrl);
        driver.get(baseUrl);
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(userId));
        username = tenantDetails.getTenantName();
        password = tenantDetails.getTenantPassword();
    }

    @Override
    public void runSuccessCase() {
        try {
            new GregUserLogin().userLogin(driver, username, password);
            selenium.waitForPageToLoad("30000");
            Assert.assertTrue(selenium.isTextPresent("WSO2 Governance Registry Home"));
            //Click on Browse link
            driver.findElement(By.linkText("Browse")).click();
            Thread.sleep(5000L);
            //Go to Detail view Tab
            driver.findElement(By.id("stdView")).click();
            Thread.sleep(3000L);
            Assert.assertTrue("Browse Detail View Page fail :", selenium.isTextPresent("Browse"));
            Assert.assertTrue("Browse Detail View Page fail :", selenium.isTextPresent("Metadata"));
            Assert.assertTrue("Browse Detail View Page fail :", selenium.isTextPresent("Entries"));
            //Click on Add Collection link
            driver.findElement(By.linkText("Add Collection")).click();
            Thread.sleep(3000L);
            driver.findElement(By.id("collectionName")).sendKeys("selenium_root/collection/resource/a1");
            driver.findElement(By.id("colDesc")).sendKeys("Selenium Test");
            //Click on Add button
            driver.findElement(By.xpath("//div[7]/form/table/tbody/tr[5]/td/input")).click();
            Thread.sleep(3000L);
            Assert.assertTrue("Add new Collection pop -up failed :", selenium.isTextPresent("WSO2 Carbon"));
            Assert.assertTrue("Add new Collection pop -up failed :", selenium.isTextPresent("Successfully added new collection."));
            //click on OK button
            selenium.click("//button");
            Thread.sleep(2000L);
            Assert.assertEquals("Collection Creation Fail :", "/selenium_root/collection/resource/a1", selenium.getValue("//input"));
            //click on Add Resource button
            driver.findElement(By.linkText("Add Resource")).click();
            Thread.sleep(2000L);
            Assert.assertTrue("Add new resource page failed :", selenium.isTextPresent("Add Resource"));
            //select create text content
            selenium.select("id=addMethodSelector", "label=Create Text content");
            selenium.click("css=option[value=\"text\"]");
            Thread.sleep(3000L);
            // Enter name
            driver.findElement(By.id("trFileName")).sendKeys("test");
            driver.findElement(By.id("trMediaType")).sendKeys("txt");
            driver.findElement(By.id("trDescription")).sendKeys("selenium test resource");
            driver.findElement(By.id("trPlainContent")).sendKeys("selenium test123");
            // Click on Add button
            driver.findElement(By.xpath("//tr[4]/td/form/table/tbody/tr[6]/td/input")).click();
            Thread.sleep(3000L);
            Assert.assertTrue("Add Resource pop-up fail :", selenium.isTextPresent("WSO2 Carbon"));
            Assert.assertTrue("Add Resource pop-up fail :", selenium.isTextPresent("Successfully added Text content."));
            //Click on OK button
            driver.findElement(By.xpath("//button")).click();
            Thread.sleep(2000L);
            driver.findElement(By.id("uLocationBar")).clear();
            driver.findElement(By.id("uLocationBar")).sendKeys("/selenium_root/collection/resource/a1/test");
            driver.findElement(By.xpath("//input[2]")).click();
            Thread.sleep(3000L);
            Assert.assertTrue("Browse Added Resource Fail:", selenium.isTextPresent("Metadata"));
            Assert.assertTrue("Browse Added Resource Fail:", selenium.isTextPresent("Properties"));
            //click on Apply Tag Frame
            driver.findElement(By.id("tagsIconMinimized")).click();
            Thread.sleep(2000L);
            //click on Add New Tag
            driver.findElement(By.linkText("Add New Tag")).click();
            Thread.sleep(2000L);
            driver.findElement(By.id("tfTag")).sendKeys("apply resource tag");
            driver.findElement(By.xpath("//div[2]/input[3]")).click();
            Thread.sleep(6000L);
            selenium.mouseOver("//div[12]/div[3]/a");
            Thread.sleep(2000L);
            //Delete Tag
            driver.findElement(By.xpath("//div[12]/div[3]/a[2]/img")).click();
            Thread.sleep(3000L);
            Assert.assertTrue("Delete Tag Pop-up fail :", selenium.isTextPresent("WSO2 Carbon"));
            Assert.assertTrue("Delete Tag Pop-up fail :", selenium.isTextPresent("exact:Are you sure you want to delete this tag?"));
            //click on "yes" button
            selenium.click("//button");
            selenium.waitForPageToLoad("30000");
            //Sign out
            driver.findElement(By.linkText("Browse")).click();
            Thread.sleep(5000L);
            //Go to Detail view Tab
            driver.findElement(By.id("stdView")).click();
            Thread.sleep(3000L);
        } catch (AssertionFailedError e) {
            log.info("GregApplyTagtoResourceSeleniumTest - Assertion Failure ::" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregApplyTagtoResourceSeleniumTest");
            driver.quit();
            Assert.fail("GregApplyTagtoResourceSeleniumTest - Assertion Failure :" + e.getMessage());
        } catch (WebDriverException e) {
            log.info("GregApplyTagtoResourceSeleniumTest - WebDriver Exception :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregApplyTagtoResourceSeleniumTest");
            driver.quit();
            Assert.fail("GregApplyTagtoResourceSeleniumTest - WebDriver Exception :" + e.getMessage());
        } catch (Exception e) {
            log.info("GregApplyTagtoResourceSeleniumTest- Fail :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregApplyTagtoResourceSeleniumTest");
            driver.quit();
            Assert.fail("GregApplyTagtoResourceSeleniumTest - Exception :" + e.getMessage());
        }

    }

    @Override
    public void cleanup() {
        deleteResourceFromBrowser(2);
        selenium.waitForPageToLoad("30000");
        driver.quit();
        log.info("GregApplyTagtoResourceSeleniumTest - Passed");
    }

    private void deleteResourceFromBrowser(int ResourceRowId) {
        driver.findElement(By.id("actionLink" + ResourceRowId)).click();
        selenium.waitForPageToLoad("30000");
        ResourceRowId = ((ResourceRowId - 1) * 7) + 2;
        driver.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[3]/table/tbody/tr[2]/td/div/div/" +
                                    "table/tbody/tr/td/div[2]/div[3]/div[3]/div[9]/table/tbody/tr[" + ResourceRowId + "]" +
                                    "/td/div/a[3]")).click();
        selenium.waitForPageToLoad("30000");
        Assert.assertTrue("Resource Delete pop-up  failed :", selenium.isTextPresent("WSO2 Carbon"));
        Assert.assertTrue("Resource Delete pop-up  failed :", selenium.isTextPresent("exact:Are you sure you want to delete"));
        selenium.click("//button");
        selenium.waitForPageToLoad("30000");
    }
}
