package com.openclassrooms.starterjwt.e2e;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterE2ETest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless"); // Run in headless mode for CI/CD
        driver = new FirefoxDriver(options);
        wait = new WebDriverWait(driver, 10);
    }

//    @Test
//    public void testSuccessfulRegistration() {
//        // Navigate to the registration page
//        driver.get("http://localhost:4200/register");
//
//        // Fill in the registration form
//        WebElement firstNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@data-testid='First name']")));
//        firstNameField.sendKeys("John");
//
//        WebElement lastNameField = driver.findElement(By.xpath("//input[@data-testid='Last name']"));
//        lastNameField.sendKeys("Doe");
//
//        WebElement emailField = driver.findElement(By.xpath("//input[@data-testid='Email']"));
//        emailField.sendKeys("john.doe@example.com");
//
//        WebElement passwordField = driver.findElement(By.xpath("//input[@data-testid='Password']"));
//        passwordField.sendKeys("password123");
//
//        // Submit the registration form
//        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));
//        submitButton.click();
//
//        // Wait for navigation to the login page
//        wait.until(ExpectedConditions.urlContains("/login"));
//
//        // Verify that we are on the login page
//        assertTrue(driver.getCurrentUrl().contains("/login"));
//    }

    @Test
    public void testRegistrationFailure() {
        // Navigate to the registration page
        driver.get("http://localhost:4200/register");

        // Fill in the registration form with an email that's already registered
        WebElement firstNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@data-testid='First name']")));
        firstNameField.sendKeys("Jane");

        WebElement lastNameField = driver.findElement(By.xpath("//input[@data-testid='Last name']"));
        lastNameField.sendKeys("Doe");

        WebElement emailField = driver.findElement(By.xpath("//input[@data-testid='Email']"));
        emailField.sendKeys("jane.doe@example.com");

        WebElement passwordField = driver.findElement(By.xpath("//input[@data-testid='Password']"));
        passwordField.sendKeys("password123");

        // Submit the registration form
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));
        submitButton.click();

        // Wait for the error message to be displayed
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class, 'error') and contains(text(), 'An error occurred')]")));

        // Verify that the error message is displayed
        assertTrue(errorMessage.isDisplayed());
    }

    @Test
    public void testFormValidation() {
        // Navigate to the registration page
        driver.get("http://localhost:4200/register");

        // Attempt to submit the form with invalid data
        WebElement firstNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@data-testid='First name']")));
        firstNameField.sendKeys("J");

        WebElement lastNameField = driver.findElement(By.xpath("//input[@data-testid='Last name']"));
        lastNameField.sendKeys("D");

        WebElement emailField = driver.findElement(By.xpath("//input[@data-testid='Email']"));
        emailField.sendKeys("invalid-email");

        // Attempt to submit the form
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));

        // Verify that the button is disabled when the form is invalid
        assertTrue(!submitButton.isEnabled());
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
