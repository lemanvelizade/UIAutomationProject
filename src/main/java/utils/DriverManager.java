package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverManager {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<String> browser = new ThreadLocal<>();

    public static void setBrowser(String browserName) {
        browser.set(browserName);
    }

    public static WebDriver getDriver() {
        if (browser.get() == null) {
            throw new IllegalStateException("Browser name is null");
        }
        if (driver.get() == null) {
            WebDriver newDriver;
            if (browser.get().equalsIgnoreCase("firefox")) {
                newDriver = new FirefoxDriver();
            } else {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-notifications");
                newDriver = new ChromeDriver(options);
            }
            driver.set(newDriver);
        }
        return driver.get();
    }
}
