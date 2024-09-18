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

public class LogoutE2ETest {

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
    public void testLogout() {
        // Navigate to the login page
        driver.get("http://localhost:4200/login");

        // Perform login
        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@formcontrolname='email']")));
        emailField.sendKeys("louis@test.com");

        WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@formcontrolname='password']")));
        passwordField.sendKeys("password");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        loginButton.click();

        // Wait for URL to change after login
        wait.until(ExpectedConditions.urlContains("/sessions"));

        // Find the logout button or span and click it
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//app-root//span[contains(text(), 'Logout')]")));
        logoutButton.click();

        // Check that the app navigates to the root URL
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/"));

        // Verify that the login and register links are visible on the root page
        WebElement loginLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(), 'Login')]")));
        WebElement registerLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(), 'Register')]")));

        assertTrue(loginLink.isDisplayed());
        assertTrue(registerLink.isDisplayed());
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
