package scooter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pageobjects.MainPage;
import pageobjects.OrderPage;

import java.util.stream.Stream;

public class OrderScooterTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Создаем объект страницы и вызываем готовый метод
        MainPage mainPage = new MainPage(driver);
        mainPage.acceptCookies();
    }

    // Поставщик данных для параметризации формы заказа
    public static Stream<Arguments> getOrderData() {
        return Stream.of(
                // Набор 1: Тестируем ВЕРХНЮЮ кнопку заказа
                Arguments.of(
                        "top", "Иван", "Петров", "г. Москва, ул. Ленина, д. 10", "Сокольники", "89991112233",
                        "15.07.2026", "сутки", "black", "Пожалуйста, позвоните за час"
                ),
                // Набор 2: Тестируем НИЖНЮЮ кнопку заказа
                Arguments.of(
                        "bottom", "Анна", "Смирнова", "г. Москва, ул. Мира, д. 5, кв. 12", "Черкизовская", "89995554433",
                        "20.07.2026", "двое суток", "grey", ""
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getOrderData")
    public void checkOrderFlowSuccess(
            String buttonType, String firstName, String lastName, String address, String metroStation, String phone,
            String date, String period, String color, String comment
    ) {
        MainPage mainPage = new MainPage(driver);
        OrderPage orderPage = new OrderPage(driver);

        // 1. Выбираем точку входа в зависимости от параметра
        if ("top".equals(buttonType)) {
            mainPage.clickTopOrderButton();
        } else {
            mainPage.clickBottomOrderButton();
        }

        // 2. Заполняем первый шаг формы (Личные данные)
        orderPage.fillPersonalData(firstName, lastName, address, metroStation, phone);

        // 3. Заполняем второй шаг формы (Детали аренды)
        orderPage.fillRentalData(date, period, color, comment);

        // 4. Подтверждаем создание заказа
        orderPage.confirmOrder();

        // 5. Проверяем появление окна об успешном заказе
        boolean isSuccess = orderPage.isOrderCreatedPopupDisplayed();
        Assertions.assertTrue(isSuccess, "Всплывающее окно об успешном создании заказа не появилось!");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
