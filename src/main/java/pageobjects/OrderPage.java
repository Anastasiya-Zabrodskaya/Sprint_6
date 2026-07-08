package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class OrderPage {
    private final WebDriver driver;

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    // =========================================================================
    // Спиcoк элементов страницы заказа
    // =========================================================================

    // --- ЭТАП 1: «Для кого самокат» ---

    // Поле ввода «Имя»
    private final By firstNameInput = By.xpath(".//input[@placeholder='* Имя']");

    // Поле ввода «Фамилия»
    private final By lastNameInput = By.xpath(".//input[@placeholder='* Фамилия']");

    // Поле ввода «Адрес: куда привезти заказ»
    private final By addressInput = By.xpath(".//input[@placeholder='* Адрес: куда привезти заказ']");

    // Поле выбора «Станция метро»
    private final By metroStationInput = By.xpath(".//input[@placeholder='* Станция метро']");

    // Динамический локатор для выбора станции метро из списка
    private By getMetroStationOption(String stationName) {
        return By.xpath(".//div[text()='" + stationName + "']");
    }

    // Поле ввода «Телефон»
    private final By phoneInput = By.xpath(".//input[@placeholder='* Телефон: на него позвонит курьер']");

    // Кнопка «Далее»
    private final By nextButton = By.xpath(".//button[text()='Далее']");

    // --- ЭТАП 2: «Про аренду» ---

    // Поле «Когда привезти самокат»
    private final By deliveryDateInput = By.xpath(".//input[@placeholder='* Когда привезти самокат']");

    // Поле «Срок аренды»
    private final By rentalPeriodDropdown = By.className("Dropdown-control");

    // Динамический локатор для выбора срока аренды
    private By getRentalPeriodOption(String period) {
        return By.xpath(".//div[@class='Dropdown-menu']/div[text()='" + period + "']");
    }

    // Чекбокс выбора цвета
    private By getColorCheckbox(String color) {
        return By.id(color);
    }

    // Поле ввода «Комментарий»
    private final By commentInput = By.xpath(".//input[@placeholder='Комментарий для курьера']");

    // Кнопка «Заказать» (финальная)
    private final By finalOrderButton = By.xpath(".//div[contains(@class, 'Order_Buttons')]/button[text()='Заказать']");

    // --- ЭТАП 3: Окна подтверждения ---

    // Кнопка «Да» в модальном окне
    private final By confirmOrderButton = By.xpath(".//button[text()='Да']");

    // Заголовок успешного создания заказа
    private final By orderCreatedHeader = By.xpath(".//div[contains(@class, 'Order_ModalHeader') and text()='Заказ оформлен']");


    // =========================================================================
    // Методы для заполнения формы заказа (Действия)
    // =========================================================================

    // Шаг 1: Заполнение личных данных
    public void fillPersonalData(String firstName, String lastName, String address, String metroStation, String phone) {
        driver.findElement(firstNameInput).sendKeys(firstName);
        driver.findElement(lastNameInput).sendKeys(lastName);
        driver.findElement(addressInput).sendKeys(address);

        driver.findElement(metroStationInput).click();
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(getMetroStationOption(metroStation)));
        driver.findElement(getMetroStationOption(metroStation)).click();

        driver.findElement(phoneInput).sendKeys(phone);
        driver.findElement(nextButton).click();
    }

    // Шаг 2: Заполнение деталей аренды
    public void fillRentalData(String date, String period, String color, String comment) {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(deliveryDateInput));

        driver.findElement(deliveryDateInput).sendKeys(date);
        driver.findElement(deliveryDateInput).sendKeys(org.openqa.selenium.Keys.ENTER);

        driver.findElement(rentalPeriodDropdown).click();
        driver.findElement(getRentalPeriodOption(period)).click();

        if (color != null && !color.isEmpty()) {
            driver.findElement(getColorCheckbox(color)).click();
        }

        driver.findElement(commentInput).sendKeys(comment);
        driver.findElement(finalOrderButton).click();
    }

    // Шаг 3: Подтверждение заказа
    public void confirmOrder() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(confirmOrderButton));
        driver.findElement(confirmOrderButton).click();
    }

    // Проверка успешного создания заказа
    public boolean isOrderCreatedPopupDisplayed() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(orderCreatedHeader)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
