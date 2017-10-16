import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.net.Authenticator;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.*;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Title("Jxtym Неуспешная авторизация пользователя")
public class TestAuthorizationNew {
    FirefoxDriver wd;
    WriteReadFromFile readData;
    WriteReadFromFile writeData;
    String resultOfTest;

    @BeforeMethod
    public void setUp() throws Exception {
        Authenticator.setDefault(new ProxyAuthenticator("ADMDI\\manuhin", "123"));
        System.setProperty("http.proxyHost", "proxy.mdi.ru");
        System.setProperty("http.proxyPort", "3128");

        wd = new FirefoxDriver();
        wd.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        //читаем из файла адрес сервера
        readData= new WriteReadFromFile("C:\\test\\notes3.txt");
        wd.get(readData.readFromFile().get(0).substring(1));
       // System.out.println(readData.readFromFile().get(0));
       // wd.get("http://vm-107-stu-dev.ursip.ru/auth/");
    }

    @Title("Неуспешная авторизация пользователя")
    @Test
    @Step("Вызов метода авторизации")
    public void TestAuthorizationNew() {
        unSuccessfulTest(21,21);
        successfulTest();
      login("userName", "password", readData.readFromFile().get(1), readData.readFromFile().get(2));
    }

    @Step("Поиск элементов {2} и {3}")
    private void login(String elementUserName, String elementPassword, String nameLogin, String passwordLogin) {
        wd.findElement(By.id(elementUserName)).click();
        wd.findElement(By.id(elementUserName)).clear();
        wd.findElement(By.id(elementUserName)).sendKeys(nameLogin);
        wd.findElement(By.id(elementPassword)).click();
        wd.findElement(By.id(elementPassword)).clear();
        wd.findElement(By.id(elementPassword)).sendKeys(passwordLogin);
        //*[@id="authorization"]/div/form/button
      //  wd.findElement(By.xpath("//div[@class='authorization-page']//button[.='Войти']")).click();
        wd.findElement(By.xpath("//*[@id=\"authorization\"]/div/form/button")).click();

        resultOfTest = " "+"\n"+"Test \n passed \n";
        writeData = new WriteReadFromFile("C:\\test\\notes2.txt", resultOfTest);
        writeData.writeToFile();

        wd.findElement(By.xpath("//*[@id=\"authorization\"]/div/div[2]/div[1]/div/div/span[2]/button1")).isEnabled();
           // wd.findElement(By.xpath("//*[@id=\"authorization\"]/div/div[2]/div[1]/div/div/span[2]/button")).click();}
    }

    @Title("Новый тестовый метод")
    @Test
    @Step("Вызов нового тестового метода")
    public void tesst(){
        unSuccessfulTest(2,21);
        successfulTest();
    }
    @Step("Первая проверка равенства элементов {0} и {1}")
   // @Title("Сравнение логина и пароля с данными из бд")
    public void unSuccessfulTest(int a, int b) {
        assertTrue(a == b);
    }
   // @Title("Вызов метода SuccessfulTest")
    @Step("Вторая проверка равенства элементов")
    public void successfulTest() {
        assertEquals("admin","admin");
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
