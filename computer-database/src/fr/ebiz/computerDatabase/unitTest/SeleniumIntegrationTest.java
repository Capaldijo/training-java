package fr.ebiz.computerDatabase.unitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class SeleniumIntegrationTest {

    private WebDriver driver;
    private final String baseUrl = "http://localhost:8080/computer-database/dashboard";
    private final String addUrl = "http://localhost:8080/computer-database/add_computer";
    private final String editUrl = "http://localhost:8080/computer-database/edit_computer?id=581";
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        
        /*
         * For Windaube Users, go get your driver here and chose the v0.15.0
         * https://github.com/mozilla/geckodriver/releases
         * 
         * System.setProperty("webdriver.gecko.driver","src/test/resources/seleniumDriver/geckodriver");
         * 
         * driver = new FirefoxDriver();
         *  Test done on Chrome because the firefox driver is not fully compatible with firefox v53
         */
        
        System.setProperty("webdriver.chrome.driver","src/test/resources/seleniumDriver/chromedriver");
        
        driver = new ChromeDriver();
        
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testWelcomePage() throws Exception {
        driver.get(baseUrl);
        
        String actualUrl = driver.getCurrentUrl();
        
        assertEquals("Correct URL is open", baseUrl, actualUrl);
    }
    
    @Test
    public void testAddComputer() throws Exception {
        driver.get(addUrl);
        
        
        String actualUrl = driver.getCurrentUrl();
        
        assertEquals("Correct URL is open", addUrl, actualUrl);
        
        WebElement inputName = driver.findElement(By.id("computerName"));
        WebElement inputIntro = driver.findElement(By.id("introduced"));
        WebElement inputDiscon = driver.findElement(By.id("discontinued"));
        Select inputSelect = new Select(driver.findElement(By.id("companyId")));
        
        inputName.sendKeys("TestSeleniumAdd");
        
        /*
         * Because of dateTimePicker, we must click first so 
         * that it puts default value, clear the input
         * and then add the date we want
         */
        inputIntro.click();
        inputIntro.clear();
        inputIntro.sendKeys("1995-04-05");
        
        /*
         * Still because of dateTimepicker that overlap over the next input because
         * of previous click, throw an exception. To fix this, click on another
         * input in order to get it works.
         */
        inputName.click();
        
        inputDiscon.click();
        inputDiscon.clear();
        inputDiscon.sendKeys("2005-06-05");
        
        // Select the fifth option in the select
        inputSelect.selectByIndex(4);

        driver.findElement(By.id("submitAdd")).sendKeys(Keys.ENTER);
    }
    
    @Test
    public void testEditComputer() throws Exception {
        driver.get(editUrl);
        
        
        String actualUrl = driver.getCurrentUrl();
        
        assertEquals("Correct URL is open", editUrl, actualUrl);
        
        WebElement inputName = driver.findElement(By.id("computerName"));
        WebElement inputIntro = driver.findElement(By.id("introduced"));
        WebElement inputDiscon = driver.findElement(By.id("discontinued"));
        Select inputSelect = new Select(driver.findElement(By.id("companyId")));
        
        inputName.clear();
        inputName.sendKeys("TestSeleniumEdit");
        
        /*
         * Because of dateTimePicker, we must click first so 
         * that it puts default value, clear the input
         * and then add the date we want
         */
        inputIntro.click();
        inputIntro.clear();
        inputIntro.sendKeys("1995-04-05");
        
        /*
         * Still because of dateTimepicker that overlap over the next input because
         * of previous click, throw an exception. To fix this, click on another
         * input in order to get it works.
         */
        inputName.click();
        
        inputDiscon.click();
        inputDiscon.clear();
        inputDiscon.sendKeys("2005-06-05");
        
        // Select the fifth option in the select
        inputSelect.selectByIndex(6);

        driver.findElement(By.id("submitEdit")).sendKeys(Keys.ENTER);
    }

    @After
    public void tearDown() throws Exception {
      driver.quit();
      String verificationErrorString = verificationErrors.toString();
      if (!"".equals(verificationErrorString)) {
        fail(verificationErrorString);
      }
    }

}
