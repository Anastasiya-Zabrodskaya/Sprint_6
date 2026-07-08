package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MainPage {
    private final WebDriver driver;


    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    // =========================================================================
    // Спиcoк элементов Главной страницы
    // =========================================================================

    // Кнопка «Заказать» вверху страницы (в хедере)
    private final By topOrderButton = By.xpath(".//div[contains(@class, 'Header_Nav')]/button[text()='Заказать']");

    // Кнопка «Заказать» внизу страницы (в секции «Как это работает»)
    private final By bottomOrderButton = By.xpath(".//div[contains(@class, 'Home_FinishButton')]/button[text()='Заказать']");

    // Кнопка согласия с куки
    private final By cookieButton = By.id("rcc-confirm-button");

    // Стрелочка / заголовок вопроса в разделе «Вопросы о важном»
    // Локатор динамический, возвращает By в зависимости от индекса (0-7)
    public By getQuestionButton(int index) {
        return By.id("accordion__heading-" + index);
    }

    // Текст ответа в раскрытом дропдауне
    // Локатор динамический, возвращает By в зависимости от индекса (0-7)
    public By getAnswerText(int index) {
        return By.id("accordion__panel-" + index);
    }

    // =========================================================================
    // Методы для работы с элементами (Действия)
    // =========================================================================

    // Кликнуть по верхней кнопке «Заказать»
    public void clickTopOrderButton() {
        driver.findElement(topOrderButton).click();
    }

    // Кликнуть по нижней кнопке «Заказать»
    public void clickBottomOrderButton() {
        // Скролл до нижней кнопки
        var element = driver.findElement(bottomOrderButton);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        element.click();
    }

    // Кликнуть по вопросу по его индексу (0-7)
    public void clickQuestion(int index) {
        var element = driver.findElement(getQuestionButton(index));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        element.click();
    }

    // Получить текст ответа по его индексу (0-7) с ожиданием появления
    public String getAnswerTextString(int index) {
        // Ждем, пока текст ответа станет видимым (анимация раскрытия)
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(getAnswerText(index)));
        return driver.findElement(getAnswerText(index)).getText();
    }

    // Принять куки
    public void acceptCookies() {
        try {
            driver.findElement(cookieButton).click();
        } catch (Exception e) {
            // Если плашка с куки не появилась, просто идём дальше
        }
    }
}
