package entity;

import interfaces.NameFetcher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Restaurant implements NameFetcher {
    private String nameInUzbek;
    private String nameInRussian;
    private String nameInEnglish;
    private String phoneNumber;
    private static Integer idGenerator = 10;
    private final Integer id = idGenerator++;
    private final String photoURL;

    @Override
    public String getTitle(TelegramUser telegramUser) {
        switch (telegramUser.getLanguage()) {
            case RU -> {
                return nameInRussian;
            }
            case EN -> {
                return nameInEnglish;
            }
        }
        return nameInUzbek;
    }
}
