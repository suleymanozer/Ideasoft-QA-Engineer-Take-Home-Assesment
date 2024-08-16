import dev.failsafe.internal.util.Assert;
import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class UrunEkleme {

    WebDriver driver;

    @BeforeEach
    void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://testcase.myideasoft.com/");
    }

    @Test
    void urunEklemeTesti() throws InterruptedException, IOException {

        //"Ürün" arama ve "ürünü" sepete ekleme
        driver.findElement(By.name("q")).click();
        Thread.sleep(500);
        driver.findElement(By.name("q")).sendKeys("ürün");
        Thread.sleep(500);
        driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
        Thread.sleep(500);
        driver.findElement(By.className("lazyload")).click();
        driver.findElement(By.name("qty-input")).click();
        Thread.sleep(500);
        Select adet = new Select(driver.findElement(By.id("qty-input")));
        adet.selectByValue("5");
        Thread.sleep(500);
        driver.findElement(By.className("add-to-cart-button")).click();
        Thread.sleep(2000);


//        String dogrulama = driver.findElement(By.className("shopping-information-cart-inside")).getText();
//        if (Objects.equals(dogrulama, "SEPETİNİZE EKLENMİŞTİR")){
//            System.out.println("\"Sepetinize Eklenmiştir\" yazısı ekrana gelmiştir");
//        }else {
//            System.out.println("Sepetinize Eklenmiştir yazısı ekrana gelmemiştir");
//        }

        //"SEPETİNİZE EKLENMİŞTİR" yazısının görünmesinin kontrolü
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement mesajElementi = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping-information-cart-inside")));

            if (mesajElementi.isDisplayed()) {
                System.out.println("\"Sepetinize Eklenmiştir\" yazısı ekrana gelmiştir");
            }
        } catch (TimeoutException e) {
            System.out.println("Sepetiniz Eklenmiştir yazısı ekrana gelmemiştir");
        }

        //"SEPETİNİZE EKLENMİŞTİR" yazısının görünmesinin ekran görüntüsü alarak kontrolü
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File image = screenshot.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(image, new File("screenshots/ekranGoruntusu.jpeg"));
        Thread.sleep(500);

        ///sepet sayfasına gidilir
        driver.get("https://testcase.myideasoft.com/sepet");
        Thread.sleep(1000);

        //Sepet içeriğinde ilgili üründen 5 adet olup olmadığının kontrolü
        String value = driver.findElement(By.className("form-control")).getAttribute("value");
        if ("5".equals(value)){
            System.out.println("Ürün adeti doğru eklenmiştir");
        }else {
            System.out.println("Ürün adeti yanlış eklenmiştir");
        }

        Thread.sleep(1000);

    }

    @AfterEach
    void tearDown(){
        driver.close();
    }

}
