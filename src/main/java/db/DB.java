package db;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.DeleteMessages;
import entity.Category;
import entity.Product;
import entity.Restaurant;
import entity.TelegramUser;
import enums.CategoryStatus;
import enums.ProductStatus;
import enums.TelegramState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static bot.MyBot.telegramBot;

public interface DB {
    ConcurrentHashMap<Long, TelegramUser> TELEGRAM_USERS = new ConcurrentHashMap<>();
    List<Restaurant> RESTAURANTS = new ArrayList<>();

    List<Product> PRODUCTS = new ArrayList<>();

    List<Category> CATEGORIES = new ArrayList<>();


    static void clearMessages(TelegramUser telegramUser) {
        int[] array = telegramUser.getDeleting_messages().stream().mapToInt(Integer::intValue).toArray();

        DeleteMessages deleteMessages = new DeleteMessages(
                telegramUser.getChatId(), array
        );
        telegramBot.execute(deleteMessages);
        telegramUser.getDeleting_messages().clear();
    }

    static Category fetchCategoryByItsName(Message message) {
        List<Category> list = CATEGORIES.stream().filter(category -> category.getName().equals(message.text())).toList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    static List<Product> fetchProductsByCategoryId(Category chosenCategory) {
        return PRODUCTS.stream()
                .filter(product -> product.getCategoryId().equals(chosenCategory.getId()))
                .filter(product -> product.getProductStatus().equals(ProductStatus.AVAILABLE))
                .toList();
    }

    static List<Category> fetchCategoriesByRestaurantId(Restaurant chosenRestaurant) {
        return CATEGORIES.stream()
                .filter(category -> category.getRestaurantId().equals(chosenRestaurant.getId()))
                .filter(category -> category.getCategoryStatus().equals(CategoryStatus.ACTIVE))
                .toList();
    }

    static Optional<Restaurant> fetchChosenRestaurantById(TelegramUser telegramUser) {
        return Optional.of(RESTAURANTS.stream()
                .filter(restaurant -> restaurant.getId().equals(telegramUser.getChosenRestaurantID()))
                .toList().get(0));
    }

    static List<Product> fetchChosenProductById(Message message, TelegramUser telegramUser) {
        Category chosenCategory = fetchCategoryByItsId(telegramUser);

        return PRODUCTS.stream()
                .filter(product -> product.getName().equals(message.text()))
                .filter(product -> product.getProductStatus().equals(ProductStatus.AVAILABLE))
                .filter(product -> product.getCategoryId().equals(chosenCategory.getId()))
                .toList();
    }

    static Category fetchCategoryByItsId(TelegramUser telegramUser) {
        List<Category> list = CATEGORIES.stream()
                .filter(category -> category.getId().equals(telegramUser.getChosenCategoryId()))
                .toList();
        return list.get(0);
    }

    static TelegramUser getUser(Long chatId) {
        TelegramUser telegramUser = TELEGRAM_USERS.getOrDefault(chatId, null);
        if (telegramUser != null) {
            return telegramUser;
        }
        TelegramUser userNew = TelegramUser.builder()
                .chatId(chatId)
                .telegramState(TelegramState.START)
                .build();
        TELEGRAM_USERS.put(chatId, userNew);
        return userNew;
    }

    static Restaurant fetchChosenRestaurantByName(String text) {
        List<Restaurant> list = DB.RESTAURANTS
                .stream()
                .filter(restaurant -> restaurant.getName().equals(text)).toList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
