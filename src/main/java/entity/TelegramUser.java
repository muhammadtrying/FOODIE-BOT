package entity;

import enums.Language;
import enums.TelegramState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@AllArgsConstructor
@Builder
public class TelegramUser {
    private String firstName;
    private String lastName;
    private Integer age;
    private Long chatId;
    private String userName;
    private String bio;
    private TelegramState telegramState;
    private Language language;
    private Integer chosenRestaurantID;
    private int counterForProducts;
    private Integer messageIdForCounter;
    private UUID chosenCategoryId;
    private List<Integer> deleting_messages;
    private List<Integer> messageIds_of_products_to_delete_them_later;

    public String getText(String txt) {
        return ResourceBundle.getBundle("message", Locale.forLanguageTag(language.toString())).getString(txt);
    }

    public boolean checkState(TelegramState state) {
        return this.telegramState.equals(state);
    }
}
