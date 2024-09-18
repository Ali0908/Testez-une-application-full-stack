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

public class ParticipationE2ETest {

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

    @Test
    public void testUserParticipationInSession() {
        // Navigate to the login page
        driver.get("http://localhost:4200/login");

        // Perform login
        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@formcontrolname='email']")));
        emailField.sendKeys("louis@test.com");

        WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@formcontrolname='password']")));
        passwordField.sendKeys("password");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        loginButton.click();

        // Wait for the user to be redirected to the sessions list page
        wait.until(ExpectedConditions.urlContains("/sessions"));

        // Verify that session details are displayed (e.g., Test session creation)
        WebElement sessionCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//mat-card-title[contains(text(), 'Test session creation')]")));
        assertTrue(sessionCard.isDisplayed());

        // Click on the Detail button of the session
        WebElement detailButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[mat-raised-button][contains(text(), 'Detail')]")));
        detailButton.click();

        // Wait for the session detail page to load
        wait.until(ExpectedConditions.urlContains("/session/1"));

        // Verify that the Participate button is visible and click it
        WebElement participateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Participate')]")));
        participateButton.click();

        // Wait for some indication that participation was successful (e.g., a change in the button text or URL)
        // For example, you might check for the presence of a success message
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Participation successful')]")));

        // Assertions to verify the participation can be added here based on how your application indicates successful participation
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
