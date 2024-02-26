package bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import db.DB;
import entity.Category;
import entity.Product;
import entity.Restaurant;
import entity.TelegramUser;
import enums.Language;
import enums.TelegramState;
import lombok.SneakyThrows;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static bot.MyBot.telegramBot;

public class BotService {


    private static String getFullName(TelegramUser telegramUser) {
        String fullName = "";
        if (telegramUser.getFirstName() != null) fullName += telegramUser.getFirstName();
        if (telegramUser.getLastName() != null) fullName += " " + telegramUser.getLastName();
        return fullName;
    }

    public static void acceptStartAskForLanguage(TelegramUser telegramUser, Message message) {
        String messageTxt = """
                Welcome %s !
                                
                Assalamu aleykum. O'zingizga qulay bo'lgan tilni tanlang!
                                
                Hi. Choose a language you are comfortable with!
                                
                Привет. Выбирайте язык, который вам удобен!
                """.formatted(getFullName(telegramUser));

        telegramUser.setFirstName(message.from().firstName());
        telegramUser.setLastName(message.from().lastName());
        telegramUser.setUserName(message.from().username());

        SendMessage sendMessage = new SendMessage(
                telegramUser.getChatId(),
                messageTxt
        );

        sendMessage.replyMarkup(BotUtils.generateLanguageButton());
        SendResponse execute = MyBot.telegramBot.execute(sendMessage);


        if (telegramUser.getDeleting_messages() == null) {
            telegramUser.setDeleting_messages(new ArrayList<>());
        }
        telegramUser.getDeleting_messages().add(execute.message().messageId());
        telegramUser.setTelegramState(TelegramState.ACCEPTING_LANGUAGE);
    }

    public static void acceptLanguageShowRestaurants(TelegramUser telegramUser, String data) {
        DB.clearMessages(telegramUser);
        telegramUser.setLanguage(Language.valueOf(data));
        showMainMenu(telegramUser);
        telegramUser.setTelegramState(TelegramState.DEALING_WITH_MAIN_MENU);
    }

    private static void showMainMenu(TelegramUser telegramUser) {
        SendMessage sendMessage = new SendMessage(telegramUser.getChatId(),
                generateIntroTextForLosers(telegramUser)
        );

        sendMessage.replyMarkup(BotUtils.generateMainMenuIntroButtons(telegramUser));
        SendResponse execute = telegramBot.execute(sendMessage);
        telegramUser.getDeleting_messages().add(execute.message().messageId());
    }

    private static String generateIntroTextForLosers(TelegramUser telegramUser) {
        String fullName = getFullName(telegramUser);
        return telegramUser.getText("GREETING") + " " + fullName;
    }

    public static void showRestaurants(TelegramUser telegramUser) {
        DB.clearMessages(telegramUser);

        SendMessage sendMessage = new SendMessage(
                telegramUser.getChatId(),
                telegramUser.getText("CHOOSE_RESTAURANT")
        );

        sendMessage.replyMarkup(BotUtils.generateRestaurantsBtns(telegramUser));
        SendResponse execute = telegramBot.execute(sendMessage);

        telegramUser.getDeleting_messages().add(execute.message().messageId());
        telegramUser.setTelegramState(TelegramState.ACCEPTING_RESTAURANT_CHOICE);
    }

    @SneakyThrows
    public static void acceptRestaurantShowMenuAndRestaurantInfo(TelegramUser telegramUser, Message message) {
        DB.clearMessages(telegramUser);
        telegramUser.getDeleting_messages().add(message.messageId());

        if (message.text().equals(telegramUser.getText("RETURN"))) {
            showMainMenu(telegramUser);
        } else if (message.text().equals(telegramUser.getText("VIEW_RESTAURANTS"))) {
            showRestaurants(telegramUser);
        } else if (DB.fetchChosenRestaurantByName(message.text(), telegramUser) == null) {
            SendMessage sendMessage = new SendMessage(telegramUser.getChatId(), telegramUser.getText("WRONG_RESTAURANT"));
            SendResponse execute = telegramBot.execute(sendMessage);
            telegramUser.getDeleting_messages().add(execute.message().messageId());
            showRestaurants(telegramUser);
        } else {
            Restaurant chosenRestaurant = DB.fetchChosenRestaurantByName(message.text(), telegramUser);

            assert chosenRestaurant != null;
            telegramUser.setChosenRestaurantID(chosenRestaurant.getId());

            SendPhoto sendPhoto = new SendPhoto(telegramUser.getChatId(), new File(chosenRestaurant.getPhotoURL()));
            sendPhoto.caption(telegramUser.getText("RES_INFO_FULL").formatted(chosenRestaurant.getNameInUzbek(), chosenRestaurant.getPhoneNumber()));
            sendPhoto.replyMarkup(BotUtils.generateCancelOrMenuButton(telegramUser));
            SendResponse execute = telegramBot.execute(sendPhoto);
            telegramUser.getDeleting_messages().add(execute.message().messageId());
            telegramUser.setTelegramState(TelegramState.ACCEPTING_MENU_OR_CANCEL_BUTTON);
        }
    }


    public static void acceptChoiceOfMenuOrCancelThenActAccordingly(TelegramUser telegramUser, String data) {
        if (data.equals(BotConstants.RETURN)) {
            DB.clearMessages(telegramUser);
            showRestaurants(telegramUser);
        } else {
            showMenu(telegramUser);
            telegramUser.setTelegramState(TelegramState.ACCEPTING_CATEGORY_CHOICE_AND_SENDING_DISHES);
        }
    }

    private static void showMenu(TelegramUser telegramUser) {
        DB.clearMessages(telegramUser);

        Optional<Restaurant> restaurantOptional = DB.fetchChosenRestaurantById(telegramUser);

        if (restaurantOptional.isPresent()) {
            Restaurant chosenRestaurant = restaurantOptional.get();
            SendPhoto sendPhoto = new SendPhoto(telegramUser.getChatId(), new File("src/main/java/bot/photos/menu.jpg"));

            sendPhoto.replyMarkup(BotUtils.generateMenuButtons(chosenRestaurant, telegramUser));

            SendResponse execute = telegramBot.execute(sendPhoto);
            telegramUser.getDeleting_messages().add(execute.message().messageId());
        }
    }

    @SneakyThrows
    public static void acceptCategoryChoiceSendDishes(TelegramUser telegramUser, Message message) {
        telegramUser.getDeleting_messages().add(message.messageId());
        DB.clearMessages(telegramUser);

        if (message.text().equals(telegramUser.getText("BACK_TO_RESTAURANTS"))) {
            showRestaurants(telegramUser);
        } else if (message.text().equals(telegramUser.getText("VIEW_MY_BASKET"))) {
            // logic for viewing basket
        } else {
            Category chosenCategory = DB.fetchCategoryByItsName(message.text(), telegramUser);

            if (BotUtils.generateProductsBtns(chosenCategory, telegramUser) == null) {

                SendMessage sendMessage = new SendMessage(
                        telegramUser.getChatId(),
                        telegramUser.getText("WRONG_CATEGORY") + "......."
                );

                SendResponse execute = telegramBot.execute(sendMessage);
                telegramUser.getDeleting_messages().add(execute.message().messageId());

                showMenu(telegramUser);
            } else {
                telegramUser.setChosenCategoryId(chosenCategory.getId());
                successFullySendingProducts(telegramUser);
            }
        }
    }

    private static void successFullySendingProducts(TelegramUser telegramUser) {
        DB.clearMessages(telegramUser);
        SendPhoto sendPhoto = new SendPhoto(
                telegramUser.getChatId(),
                new File("src/main/java/bot/photos/dishes.jpg")
        );

        sendPhoto.caption(telegramUser.getText("PRODUCTS"));
        Category chosenCategory = DB.fetchCategoryByItsId(telegramUser);

        sendPhoto.replyMarkup(BotUtils.generateProductsBtns(chosenCategory, telegramUser));
        SendResponse execute = telegramBot.execute(sendPhoto);

        telegramUser.getDeleting_messages().add(execute.message().messageId());
        telegramUser.setTelegramState(TelegramState.ACCEPTING_FOOD_CHOICE_GIVE_FULL_INFO_ABOUT_IT);
    }

    public static void acceptingFoodChoiceNowGivingFullInfoAbout(TelegramUser telegramUser, Message message) {
        telegramUser.getDeleting_messages().add(message.messageId());
        DB.clearMessages(telegramUser);

        List<Product> products = DB.fetchChosenProductById(message, telegramUser);

        if (products.isEmpty()) {
            SendMessage sendMessage = new SendMessage(
                    telegramUser.getChatId(),
                    telegramUser.getText("WRONG_PRODUCT")
            );
            SendResponse execute = telegramBot.execute(sendMessage);

            telegramUser.getDeleting_messages().add(execute.message().messageId());
            successFullySendingProducts(telegramUser);
        } else {
            Product chosenProduct = products.get(0);
            SendPhoto sendPhoto = new SendPhoto(
                    telegramUser.getChatId(),
                    new File(chosenProduct.getPhotoUrl())
            );

            sendPhoto.caption(getProductInfo(chosenProduct, telegramUser));
            telegramUser.setCounterForProducts(1);

            sendPhoto.replyMarkup(BotUtils.generateCounterButton(telegramUser));
            SendResponse execute = telegramBot.execute(sendPhoto);
            telegramUser.getMessageIds_of_products_to_delete_them_later().add(execute.message().messageId());
            telegramUser.setMessageIdForCounter(execute.message().messageId());

            telegramUser.setTelegramState(TelegramState.ACCEPTING_COUNTER_BUTTONS);
        }
    }

    private static String getProductInfo(Product chosenProduct, TelegramUser telegramUser) {
        return """
                🍽️ %s %s
                ℹ️🗯️ %s
                🏷️ %s %s so'm
                     
                       """.formatted(
                telegramUser.getText("PRODUCT"),
                chosenProduct.getNameInUzbek(),
                chosenProduct.getDescription(),
                telegramUser.getText("PRICE"),
                formatPrice(chosenProduct.getRetailPrice())
        );
    }

    public static void acceptCounterInfoAndChangeIt(TelegramUser telegramUser, String data) {
        if (data.equals(BotConstants.PLUS)) {
            telegramUser.setCounterForProducts(telegramUser.getCounterForProducts() + 1);
        } else if (data.equals(BotConstants.MINUS) && (telegramUser.getCounterForProducts() != 1)) {
            telegramUser.setCounterForProducts(telegramUser.getCounterForProducts() - 1);
        } else if (data.equals(BotConstants.RETURN)) {
            successFullySendingProducts(telegramUser);
        } else if (data.equals(BotConstants.ADD_TO_BASKET)) {

            successFullySendingProducts(telegramUser);
        }


        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup(
                telegramUser.getChatId(),
                telegramUser.getMessageIdForCounter()
        );


        editMessageReplyMarkup.replyMarkup(BotUtils.generateCounterButton(telegramUser));
        telegramBot.execute(editMessageReplyMarkup);
    }

    private static String formatPrice(Integer price) {
        return NumberFormat.getNumberInstance().format(price);
    }

    public static void acceptMainMenuOrder(TelegramUser telegramUser, Message message) {

        telegramUser.getDeleting_messages().add(message.messageId());

        if (message.text().equals(telegramUser.getText("VIEW_RESTAURANTS"))) {
            showRestaurants(telegramUser);
        } else if (message.text().equals(telegramUser.getText("MY_ORDERS"))) {

        } else if (message.text().equals(telegramUser.getText("WRITE_COMMENTS"))) {

        }
    }
}





