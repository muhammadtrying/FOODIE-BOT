package entity;

import enums.ProductStatus;
import interfaces.NameFetcher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class Product implements NameFetcher {
    private final UUID id = UUID.randomUUID();
    private String nameInUzbek;
    private String nameInEnglish;
    private String nameInRussian;
    private Integer retailPrice;
    private Integer originalPrice;
    private UUID categoryId;
    private ProductStatus productStatus;
    private String photoUrl;
    private String description;

    @Override
    public String getTitle(TelegramUser telegramUser) {
        switch (telegramUser.getLanguage()) {
            case EN -> {
                return nameInEnglish;
            }
            case RU -> {
                return nameInRussian;
            }
        }
        return nameInUzbek;
    }
}
