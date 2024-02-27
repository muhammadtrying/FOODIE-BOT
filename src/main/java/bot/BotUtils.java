package bot;

import com.pengrad.telegrambot.model.request.*;
import db.DB;
import entity.Category;
import entity.Restaurant;
import entity.TelegramUser;
import enums.Language;
import interfaces.NameFetcher;

import java.util.List;

public class BotUtils {
    public static InlineKeyboardMarkup generateLanguageButton() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("🇬🇧EN").callbackData(Language.EN.toString()),
                new InlineKeyboardButton("🇺🇿UZ").callbackData(Language.UZ.toString()),
                new InlineKeyboardButton("🇷🇺RU").callbackData(Language.RU.toString())
        );
    }

    public static ReplyKeyboardMarkup generateRestaurantsBtns(TelegramUser telegramUser) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(generateBtnMatrix(DB.RESTAURANTS, telegramUser));
        replyKeyboardMarkup.resizeKeyboard(true);
        replyKeyboardMarkup.addRow(new KeyboardButton(telegramUser.getText("RETURN")));
        return replyKeyboardMarkup;
    }

    public static InlineKeyboardMarkup generateCancelOrMenuButton(TelegramUser telegramUser) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton(telegramUser.getText("RETURN")).callbackData(BotConstants.RETURN),
                new InlineKeyboardButton(telegramUser.getText("MENU") + "🍟🍽️").callbackData(BotConstants.MENU)
        );
    }

    public static ReplyKeyboardMarkup generateMenuButtons(Restaurant chosenRestaurant, TelegramUser telegramUser) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(generateBtnMatrix(DB.fetchCategoriesByRestaurantId(chosenRestaurant), telegramUser));
        replyKeyboardMarkup.resizeKeyboard(true);
        replyKeyboardMarkup.addRow(
                new KeyboardButton(telegramUser.getText("BACK_TO_RESTAURANTS")),
                new KeyboardButton(telegramUser.getText("VIEW_MY_BASKET"))
        );
        return replyKeyboardMarkup;
    }

    private static String[][] generateBtnMatrix(List<? extends NameFetcher> nameFetchers, TelegramUser telegramUser) {
        int size = nameFetchers.size();
        int rowCount = size % 2 == 0 ? size / 2 : (size + 1) / 2;
        String[][] mrx = new String[rowCount][2];
        int i = 0;

        for (String[] row : mrx) {
            row[0] = nameFetchers.get(i).getTitle(telegramUser);
            if (i + 1 != nameFetchers.size()) {
                row[1] = nameFetchers.get(i + 1).getTitle(telegramUser);
            }
            i += 2;
        }

        String[] lastRow = mrx[mrx.length - 1];
        if (lastRow[1] == null) {
            mrx[mrx.length - 1] = new String[]{lastRow[0]};
        }
        return mrx;
    }

    public static Keyboard generateProductsBtns(Category chosenCategory, TelegramUser telegramUser) {
        if (chosenCategory == null) {
            return null;
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(generateBtnMatrix(DB.fetchProductsByCategoryId(chosenCategory), telegramUser));
        replyKeyboardMarkup.resizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static InlineKeyboardMarkup generateCounterButton(TelegramUser telegramUser) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("-").callbackData(BotConstants.MINUS),
                new InlineKeyboardButton(String.valueOf(telegramUser.getCounterForProducts())).callbackData("counterWhichDoesntWorkLol"),
                new InlineKeyboardButton("+").callbackData(BotConstants.PLUS)
        );
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(telegramUser.getText("RETURN")).callbackData(BotConstants.RETURN));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("✅ " + telegramUser.getText("ADD_TO_BASKET") + "🛒").callbackData(BotConstants.ADD_TO_BASKET));
        return inlineKeyboardMarkup;
    }

    public static Keyboard generateMainMenuIntroButtons(TelegramUser telegramUser) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(telegramUser.getText("VIEW_RESTAURANTS"))
        );
        replyKeyboardMarkup.addRow(telegramUser.getText("MY_ORDERS"));
        replyKeyboardMarkup.addRow(telegramUser.getText("WRITE_COMMENTS"));
        replyKeyboardMarkup.resizeKeyboard(true);
        return replyKeyboardMarkup;
    }
}
