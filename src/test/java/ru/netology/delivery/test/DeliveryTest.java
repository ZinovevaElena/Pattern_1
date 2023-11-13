package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan meeting")
    void shouldSuccessfulPlanMeeting() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 7;
        String secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $(withText("Успешно")).shouldBe(visible, Duration.ofSeconds(50));
        $(".button").click();
        $("[data-test-id=success-notification]").shouldBe(Condition.visible, Duration.ofSeconds(40));
        $("[data-test-id=success-notification]").shouldHave(Condition.text("Успешно!\n" +
                "Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(".button").click();
        $("[data-test-id=replan-notification]").shouldBe(Condition.visible, Duration.ofSeconds(40));
        $("[data-test-id=replan-notification]").shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(Condition.visible, Duration.ofSeconds(40));
        $("[data-test-id=replan-notification] button").click();
        $("[data-test-id=success-notification]").shouldBe(Condition.visible, Duration.ofSeconds(40));
        $("[data-test-id=success-notification]").shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate)).shouldBe(Condition.visible);
    }
}
