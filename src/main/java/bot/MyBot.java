package bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import db.DB;
import entity.TelegramUser;
import enums.TelegramState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyBot {

    public static final Integer NUMBER_OF_THREADS = 50;
    public static TelegramBot telegramBot = new TelegramBot("6344817090:AAFL7AqYKYYNJsv-t0rw0Y-SHMHXqclLsc4");
    public static ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public void start() {
        telegramBot.setUpdatesListener((updates) -> {
            executorService.submit(() -> updates.forEach(this::handleUpdate));
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, Throwable::printStackTrace);
    }

    private void handleUpdate(Update update) {
        if (update.message() != null) {
            Message message = update.message();
            Long chatId = message.chat().id();
            TelegramUser telegramUser = DB.getUser(chatId);

            if (message.text() != null) {
                String text = message.text();

                if (text.equals("/start")) {
                    BotService.acceptStartAskForLanguage(telegramUser, message);
                } else if (telegramUser.checkState(TelegramState.DEALING_WITH_MAIN_MENU)) {
                    BotService.acceptMainMenuOrder(telegramUser,message);
                } else if (telegramUser.checkState(TelegramState.ACCEPTING_RESTAURANT_CHOICE)) {
                    BotService.acceptRestaurantShowMenuAndRestaurantInfo(telegramUser, message);
                } else if (telegramUser.checkState(TelegramState.ACCEPTING_CATEGORY_CHOICE_AND_SENDING_DISHES)) {
                    BotService.acceptCategoryChoiceSendDishes(telegramUser, message);
                } else if (telegramUser.checkState(TelegramState.ACCEPTING_FOOD_CHOICE_GIVE_FULL_INFO_ABOUT_IT)) {
                    BotService.acceptingFoodChoiceNowGivingFullInfoAbout(telegramUser, message);
                }
            }


            //working with inline buttons
        } else if (update.callbackQuery() != null) {
            CallbackQuery callbackQuery = update.callbackQuery();
            Long chatId = callbackQuery.from().id();
            TelegramUser telegramUser = DB.getUser(chatId);

            if (callbackQuery.data() != null) {
                String data = callbackQuery.data();
                switch (telegramUser.getTelegramState()) {
                    case ACCEPTING_LANGUAGE ->BotService.acceptLanguageShowRestaurants(telegramUser, data);
                    case ACCEPTING_MENU_OR_CANCEL_BUTTON ->BotService.acceptChoiceOfMenuOrCancelThenActAccordingly(telegramUser, data);
                    case ACCEPTING_COUNTER_BUTTONS -> BotService.acceptCounterInfoAndChangeIt(telegramUser, data);
                }
            }
        }
    }
}
