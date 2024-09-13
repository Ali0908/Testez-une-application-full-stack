//package com.openclassrooms.starterjwt.e2e;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class LoginE2ETest {
//
//    private WebDriver driver;
//    private WebDriverWait wait;
//
//    @BeforeEach
//    public void setUp() {
//        WebDriverManager.chromedriver().setup();
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless"); // Run in headless mode for CI/CD
//        driver = new ChromeDriver(options);
//        wait = new WebDriverWait(driver, 10); // 10 seconds wait time
//    }
//
//    @Test
//    public void testLogin() {
//        // Navigate to login page
//        driver.get("http://localhost:8080/api/auth/login");
//
//        // Find the email input field and enter email
//        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
//        emailField.sendKeys("louis@test.com");
//
//        // Find the password input field and enter password
//        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
//        passwordField.sendKeys("password");
//
//        // Find the login button and click it
//        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("loginButton")));
//        loginButton.click();
//
//        // Verify successful login by checking the redirected page or content
//        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successMessage")));
//        assertEquals("Login successful!", successMessage.getText());
//    }
//
//    @AfterEach
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }
//}
//
