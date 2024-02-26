package bot;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
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

    public static ReplyKeyboardMarkup generateRestaurantsBtns() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(generateBtnMatrix(DB.RESTAURANTS));
        replyKeyboardMarkup.resizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static InlineKeyboardMarkup generateCancelOrMenuButton(TelegramUser telegramUser) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton(telegramUser.getText("RETURN") + "⬅️").callbackData(BotConstants.RETURN),
                new InlineKeyboardButton(telegramUser.getText("MENU") + "🍟🍽️").callbackData(BotConstants.MENU)
        );
    }

    public static ReplyKeyboardMarkup generateMenuButtons(Restaurant chosenRestaurant) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(generateBtnMatrix(DB.fetchCategoriesByRestaurantId(chosenRestaurant)));
        replyKeyboardMarkup.resizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    private static String[][] generateBtnMatrix(List<? extends NameFetcher> nameFetchers) {
        int size = nameFetchers.size();
        int rowCount = size % 2 == 0 ? size / 2 : (size + 1) / 2;
        String[][] mrx = new String[rowCount][2];
        int i = 0;

        for (String[] row : mrx) {
            row[0] = nameFetchers.get(i).getTitle();
            if (i + 1 != nameFetchers.size()) {
                row[1] = nameFetchers.get(i + 1).getTitle();
            }
            i += 2;
        }

        String[] lastRow = mrx[mrx.length - 1];
        if (lastRow[1] == null) {
            mrx[mrx.length - 1] = new String[]{lastRow[0]};
        }
        return mrx;
    }

    public static Keyboard generateProductsBtns(Category chosenCategory) {
        if (chosenCategory == null) {
            return null;
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(generateBtnMatrix(DB.fetchProductsByCategoryId(chosenCategory)));
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
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("⬅️ " + telegramUser.getText("RETURN")).callbackData(BotConstants.RETURN));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("✅ " + telegramUser.getText("ADD_TO_BASKET") + "🛒").callbackData(BotConstants.ADD_TO_BASKET));
        return inlineKeyboardMarkup;
    }
}
