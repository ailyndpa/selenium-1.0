package Pruebas;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AppTest 
{
	WebDriver driver;
	static ExtentTest test;
	static ExtentReports extent;
	static ExtentSparkReporter spark;
	
	@BeforeTest
	public void startReport() {
		extent = new ExtentReports();
		spark = new ExtentSparkReporter("ExtentReport.html");
		spark.config().setDocumentTitle("Automation Report");
		spark.config().setReportName("Test Report");
		spark.config().setTimeStampFormat("dd.MM.yyyy HH:mm:ss");
		spark.config().setTheme(Theme.DARK);
		extent.attachReporter(spark);
	}
	@BeforeMethod
	public void setup(Method method){
		test = extent.createTest(method.getName());
		WebDriverManager.chromedriver().setup();
		ChromeOptions option1 = new ChromeOptions();
		option1.addArguments("--headless");
		driver = new ChromeDriver(option1);
		driver.get("https://rahulshettyacademy.com/dropdownsPractise/");
	    driver.manage().window().maximize();
	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
	}
	
    @Test
    public void selectHaiti()
    {
    	driver.findElement(By.id("autosuggest")).sendKeys("Ha");
    	List<WebElement> list = driver.findElements(By.cssSelector("li[class='ui-menu-item'] a"));
																			
		for (WebElement elem : list) {
			if (elem.getText().equalsIgnoreCase("Haiti")) {
				elem.click();
			}
		}
    }
    
    @Test
    public void clickCuba()
    {
    	Assert.assertTrue(driver.findElement(By.name("Cosa")).isDisplayed());
    	    	
    }
    
    @Test
	public void verifRadioButton(){
		WebElement oneWay = driver.findElement(By.id("ctl00_mainContent_rbtnl_Trip_0"));
		WebElement	round_Trip = driver.findElement(By.id("ctl00_mainContent_rbtnl_Trip_1"));
		round_Trip.click();
		if (oneWay.isSelected()) {
			WebElement Date = driver.findElement(By.xpath("//div/span[@class='date-close-disabled']"));
			Assert.assertTrue(Date.isEnabled(),"Los campos asociados a la fecha deben estar deshabilitados cuando se selecciona la opción One Way");
		} else if (round_Trip.isSelected()) {
				WebElement Date = driver.findElement(By.xpath("//div/span[@class='date-close']"));
				Assert.assertTrue(Date.isEnabled(),"Los campos asociados a la fecha deben estar habilitados cuando se selecciona la opción Round Trip");
		}
	}
    
    @AfterMethod
    public void After(ITestResult result) throws IOException {
    	if (result.getStatus() == ITestResult.FAILURE){
    		File evidencia=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        	FileUtils.copyFile(evidencia, new File("target/surefire-reports/Evidencia"));
    		test.log(Status.FAIL, result.getThrowable());
        	test.addScreenCaptureFromPath("target/surefire-reports/Evidencia");
        	
    	} else if(result.getStatus() == ITestResult.SUCCESS) {
    		test.log(Status.PASS, result.getTestName());
    	} else {
    		test.log(Status.SKIP, result.getTestName());
    	}
   }
    
    @AfterTest
    public void tearDrown() {
    	extent.flush();
    }
}
