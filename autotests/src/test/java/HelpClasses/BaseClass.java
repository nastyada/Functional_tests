package HelpClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import ru.yandex.qatools.allure.annotations.Step;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static com.sun.activation.registries.LogSupport.log;
import static org.testng.Assert.assertEquals;

public class BaseClass {
    String resultOfTest;
    public FirefoxDriver wd;
    WriteReadFromFile readData;
    WriteReadFromFile writeData;

    public static boolean isAlertPresent(FirefoxDriver wd) {
        try {
            wd.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    @BeforeMethod
    public void setUp() throws Exception {

        //Получить реальный путь к geco
        File gecoFile = new File("src/help-files/geckodriver.exe");
        String pathToGeckoDriver = gecoFile.getAbsolutePath();
        //Подготовка утилиты для работы браузера FF
        System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("marionette", true);

        //Получить путь к тестовым файлам
        File testFile = new File("src/help-files/auth-info.txt");

        wd = new FirefoxDriver();
        wd.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        //Читаем из файла адрес сервера
        readData= new WriteReadFromFile(testFile.getAbsolutePath());
        wd.get(readData.readFromFile().get(0).substring(1));
    }

    @Step("Проверка наличия элементов и заполнение полей")
    protected void login(String elementUserName, String elementPassword, String nameLogin, String passwordLogin) {
        wd.findElement(By.id(elementUserName)).click();
        wd.findElement(By.id(elementUserName)).clear();
        wd.findElement(By.id(elementUserName)).sendKeys(nameLogin);
        wd.findElement(By.id(elementPassword)).click();
        wd.findElement(By.id(elementPassword)).clear();
        wd.findElement(By.id(elementPassword)).sendKeys(passwordLogin);
        wd.findElement(By.xpath("//*[@id=\"authorization\"]/div/form/button")).click();
        //Переход на страницу с делами
        wd.navigate().to("http://vm-107-stu-dev.ursip.ru/");
        wd.findElement(By.cssSelector("div.departments-tree")).click();
    }

    @AfterMethod
    public void tearDown() {
        wd.quit();
    }
}
