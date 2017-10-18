import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;

import java.io.File;
import java.net.Authenticator;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Reporter.log;

public class TestAuthorization {
    FirefoxDriver wd;
    WriteReadFromFile readData;
    WriteReadFromFile writeData;
    String resultOfTest;


    @BeforeMethod
    public void setUp() throws Exception {

        //Получить реальный путь к geco
        File gecoFile = new File("src/help-files/geckodriver.exe");
        String pathToGeckoDriver = gecoFile.getAbsolutePath();
        System.out.println(pathToGeckoDriver);
        //Подготовка утилиты для работы браузера FF
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
    @Title("Успешная авторизация пользователя")
    @Test
    @Step("Вызов метода авторизации")
    public void TestAuthorizationNewSuccess() {
      login("userName", "password", "admin", "admin");
    }
    @Step("Проверка наличия элементов и заполнение полей")
    private void login(String elementUserName, String elementPassword, String nameLogin, String passwordLogin) {
       // log("Ввод логина: "+ nameLogin);
        wd.findElement(By.id(elementUserName)).click();
        wd.findElement(By.id(elementUserName)).clear();
        wd.findElement(By.id(elementUserName)).sendKeys(nameLogin);
       // log("Ввод пароля: "+ passwordLogin);
        wd.findElement(By.id(elementPassword)).click();
        wd.findElement(By.id(elementPassword)).clear();
        wd.findElement(By.id(elementPassword)).sendKeys(passwordLogin);
        wd.findElement(By.xpath("//*[@id=\"authorization\"]/div/form/button")).click();
        boolean flag = wd.findElement(By.xpath("//*[@id=\"authorization\"]/div/div[2]/div[1]/div/div/span[2]/button")).isEnabled();
        //log("Проверка на наличие кнопки Выход: " + flag);
        assertEquals(flag, true);
    }

    @AfterMethod
    public void tearDown() {
        wd.quit();
    }
    public static boolean isAlertPresent(FirefoxDriver wd) {
        try {
            wd.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }
}