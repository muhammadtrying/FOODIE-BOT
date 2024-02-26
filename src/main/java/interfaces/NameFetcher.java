package interfaces;

import entity.TelegramUser;

public interface NameFetcher {
    String getTitle(TelegramUser telegramUser);
}
