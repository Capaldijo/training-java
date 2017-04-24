package fr.ebiz.computerDatabase.unitTest;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumIntegrationTest {

    private WebDriver driver;
    private final String baseUrl = "http://localhost:8080/computer-database/dashboard";
    private final String addUrl = "http://localhost:8080/computer-database/add_computer";
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        
        /*
         * For Windaube Users, go get your driver here and chose the v0.15.0
         * https://github.com/mozilla/geckodriver/releases
         */
        System.setProperty("webdriver.gecko.driver","src/test/resources/geckoDriver/geckodriver");
        
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testWelcomePage() throws Exception {
        driver.get(baseUrl);
        
        Thread.sleep(2000);
    }
    
    @Test
    public void testAddComputer() throws Exception {
        driver.get(addUrl);
        
        Thread.sleep(1000);
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
