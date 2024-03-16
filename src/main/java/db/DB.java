package db;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.DeleteMessages;
import entity.*;
import enums.CategoryStatus;
import enums.ProductStatus;
import enums.TelegramState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static bot.MyBot.telegramBot;

public interface DB {
    ConcurrentHashMap<Long, TelegramUser> TELEGRAM_USERS = new ConcurrentHashMap<>();
    List<Restaurant> RESTAURANTS = new ArrayList<>();

    List<Product> PRODUCTS = new ArrayList<>();

    List<Category> CATEGORIES = new ArrayList<>();
    List<Basket> BASKETS = new ArrayList<>();
    List<BasketProduct> BASKET_PRODUCTS = new ArrayList<>();


    static void clearMessages(TelegramUser telegramUser) {
        int[] array = telegramUser.getDeleting_messages().stream().mapToInt(Integer::intValue).toArray();

        DeleteMessages deleteMessages = new DeleteMessages(telegramUser.getChatId(), array);
        telegramBot.execute(deleteMessages);
        telegramUser.getDeleting_messages().clear();
    }

    static Category fetchCategoryByItsName(String text, TelegramUser telegramUser) {
        Stream<Category> categoryStream = CATEGORIES.stream().filter(category -> (extraFunctionForFetchCategoryByItsName(category, telegramUser).equals(text)));
        List<Category> list = categoryStream.toList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    static String extraFunctionForFetchCategoryByItsName(Category category, TelegramUser telegramUser) {
        switch (telegramUser.getLanguage()) {
            case RU -> {
                return category.getNameInRussian();
            }
            case EN -> {
                return category.getNameInEnglish();
            }
        }
        return category.getNameInUzbek();
    }

    static List<Product> fetchProductsByCategoryId(Category chosenCategory) {
        return PRODUCTS.stream().filter(product -> product.getCategoryId().equals(chosenCategory.getId())).filter(product -> product.getProductStatus().equals(ProductStatus.AVAILABLE)).toList();
    }

    static List<Category> fetchCategoriesByRestaurantId(Restaurant chosenRestaurant) {
        return CATEGORIES.stream().filter(category -> category.getRestaurantId().equals(chosenRestaurant.getId())).filter(category -> category.getCategoryStatus().equals(CategoryStatus.ACTIVE)).toList();
    }

    static Optional<Restaurant> fetchChosenRestaurantById(TelegramUser telegramUser) {
        return Optional.of(RESTAURANTS.stream().filter(restaurant -> restaurant.getId().equals(telegramUser.getChosenRestaurantID())).toList().get(0));
    }

    static List<Product> fetchChosenProductById(Message message, TelegramUser telegramUser) {
        Category chosenCategory = fetchCategoryByItsId(telegramUser);

        return PRODUCTS.stream().filter(product -> extraFunctionForFetchChosenProductById(telegramUser, product).equals(message.text())).filter(product -> product.getProductStatus().equals(ProductStatus.AVAILABLE)).filter(product -> product.getCategoryId().equals(chosenCategory.getId())).toList();
    }

    static String extraFunctionForFetchChosenProductById(TelegramUser telegramUser, Product product) {
        switch (telegramUser.getLanguage()) {
            case EN -> {
                return product.getNameInEnglish();
            }
            case RU -> {
                return product.getNameInRussian();
            }
        }
        return product.getNameInUzbek();
    }

    static Category fetchCategoryByItsId(TelegramUser telegramUser) {
        List<Category> list = CATEGORIES.stream().filter(category -> category.getId().equals(telegramUser.getChosenCategoryId())).toList();
        return list.get(0);
    }

    static TelegramUser getUser(Long chatId) {
        TelegramUser telegramUser = TELEGRAM_USERS.getOrDefault(chatId, null);
        if (telegramUser != null) {
            return telegramUser;
        }
        TelegramUser userNew = TelegramUser.builder().chatId(chatId).telegramState(TelegramState.START).build();
        TELEGRAM_USERS.put(chatId, userNew);
        return userNew;
    }

    static Restaurant fetchChosenRestaurantByName(String text, TelegramUser telegramUser) {
        List<Restaurant> list = DB.RESTAURANTS.stream().filter(restaurant -> (extraFunctionForFetchChosenRestaurantByName(telegramUser, restaurant)).contains(text)).toList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    static String extraFunctionForFetchChosenRestaurantByName(TelegramUser telegramUser, Restaurant restaurant) {
        switch (telegramUser.getLanguage()) {
            case RU -> {
                return restaurant.getNameInRussian();
            }
            case EN -> {
                return restaurant.getNameInEnglish();
            }
        }
        return restaurant.getNameInUzbek();
    }

    static Product fetchChosenProductByName(UUID productId) {
        return PRODUCTS.stream().filter(product -> product.getId().equals(productId)).toList().get(0);
    }

    static List<BasketProduct> fetchBasketProductsByBasketId(Basket basket) {
        return BASKET_PRODUCTS.stream().filter(basketProduct -> basketProduct.getBasketId().equals(basket.getId())).toList();
    }

    static String fetchProductNameById(TelegramUser telegramUser, UUID productId) {
        return PRODUCTS.stream().filter(product -> product.getId().equals(productId)).toList().get(0).getTitle(telegramUser);
    }
}