package ru.netology.testmode.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class DataGeneratorTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Успешный вход для зарегистрированного активного пользователя")
    void shouldSuccessfullyLoginIfRegisteredActiveUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();

        // Проверяем, что попали в личный кабинет (пример селектора и текста)
        $("h2").shouldBe(visible).shouldHave(text("Личный кабинет"));
    }

    @Test
    @DisplayName("Ошибка для заблокированного пользователя")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");

        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(visible)
                .shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Ошибка при неверном логине")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongLogin = DataGenerator.getRandomLogin();

        $("[data-test-id='login'] input").setValue(wrongLogin); // неверный логин
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Ошибка при неверном пароле")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongPassword = DataGenerator.getRandomPassword();

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword); // неверный пароль
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Ошибка для незарегистрированного пользователя")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = DataGenerator.Registration.getUser("active"); // в систему его не отправляли

        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }
}

