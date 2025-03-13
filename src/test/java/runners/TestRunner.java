package runners;

import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.DriverManager;
import io.cucumber.testng.AbstractTestNGCucumberTests;


@CucumberOptions(
        tags = "@test",
        plugin = {"pretty"},
        features = "src/test/features/",
        glue = "stepdefs"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Parameters("browser")
    @BeforeClass(alwaysRun = true)
    public void setUpClass(@Optional("chrome") String browser) {
        DriverManager.setBrowser(browser);
    }
}
