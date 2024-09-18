package com.openclassrooms.starterjwt.e2e;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class LoginE2ETest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.firefoxdriver().setup(); // Set up Firefox WebDriver
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless"); // Run in headless mode for CI/CD
        driver = new FirefoxDriver(options);
        wait = new WebDriverWait(driver, 10);
    }
    @Test
    public void testLogin() {
        // Navigate to the frontend's login page
        driver.get("http://localhost:4200/login");

        // Find the email input field using formControlName and enter email
        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@formcontrolname='email']")));
        emailField.sendKeys("louis@test.com");

        // Find the password input field using formControlName and enter password
        WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@formcontrolname='password']")));
        passwordField.sendKeys("password");

        // Find the login button and click it
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        loginButton.click();

        // Verify successful login by checking the redirected page (or use a specific element within the success page)
        wait.until(ExpectedConditions.urlContains("sessions")); // Wait for URL to contain "dashboard"
    }


    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

