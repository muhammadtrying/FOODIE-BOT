import bot.MyBot;
import db.DB;
import entity.Category;
import entity.Product;
import entity.Restaurant;
import enums.CategoryStatus;
import enums.ProductStatus;

public class Main {

    public static void main(String... args) {



//        to_be_deleted_later
        generateMockData();

        MyBot myBot = new MyBot();
        myBot.start();

        System.out.println("RUNNING!");

    }

    private static void generateMockData() {
        DB.RESTAURANTS.add(new Restaurant("RESTAURANT1", "RESTAURANT1 in ru", "RESTAURANT1 in eng", "123", "src/main/java/bot/photos/beshqozon.png"));
        DB.RESTAURANTS.add(new Restaurant("RESTAURANT2", "RESTAURANT2 in ru", "RESTAURANT2 in eng", "345", "src/main/java/bot/photos/mcdonalds.png"));
        DB.RESTAURANTS.add(new Restaurant("RESTAURANT3", "RESTAURANT3 in ru", "RESTAURANT3 in eng", "1313", "src/main/java/bot/photos/burgerking.png"));
        DB.RESTAURANTS.add(new Restaurant("RESTAURANT4", "RESTAURANT4 in ru", "RESTAURANT4 in eng", "4314", "src/main/java/bot/photos/kamolon.png"));
        DB.RESTAURANTS.add(new Restaurant("RESTAURANT5", "RESTAURANT5 in ru", "RESTAURANT5 in eng", "4314", "src/main/java/bot/photos/kamolon.png"));
        DB.RESTAURANTS.add(new Restaurant("RESTAURANT6", "RESTAURANT6 in ru", "RESTAURANT6 in eng", "4314", "src/main/java/bot/photos/kamolon.png"));
        DB.RESTAURANTS.add(new Restaurant("RESTAURANT7", "RESTAURANT7 in ru", "RESTAURANT7 in eng", "4314", "src/main/java/bot/photos/kamolon.png"));
        DB.RESTAURANTS.add(new Restaurant("RESTAURANT8", "RESTAURANT8 in ru", "RESTAURANT8 in eng", "4314", "src/main/java/bot/photos/kamolon.png"));
        DB.RESTAURANTS.add(new Restaurant("RESTAURANT9", "RESTAURANT9 in ru", "RESTAURANT9 in eng", "4314", "src/main/java/bot/photos/kamolon.png"));
        DB.RESTAURANTS.add(new Restaurant("RESTAURANT10", "RESTAURANT10 in ru", "RESTAURANT10 in eng", "4314", "src/main/java/bot/photos/kamolon.png"));


        DB.CATEGORIES.add(new Category("Shashliklar", "Kebabs", "Shashliklar in russian", 10, CategoryStatus.ACTIVE));
        DB.CATEGORIES.add(new Category("Palovlar", "Palovlar in english", "Palovlar in russian", 10, CategoryStatus.ACTIVE));
        DB.CATEGORIES.add(new Category("Ichimliklar", "Drinks", "Ichimliklar in russian", 10, CategoryStatus.ACTIVE));
        DB.CATEGORIES.add(new Category("Suyuq Ovqatlar", "Suyuq Ovqatlar in english", "Suyuq Ovqatlar in russian", 10, CategoryStatus.ACTIVE));
        DB.CATEGORIES.add(new Category("Suyuq Ovq313131atlar", "Suyuq Ovq313131atlar in english", "Suyuq Ovq313131atlar in russian", 10, CategoryStatus.ACTIVE));

        DB.PRODUCTS.add(new Product("Shashlik", "KEBAB", "Kebab in russian", 10_000, 5_000, DB.CATEGORIES.get(0).getId(), ProductStatus.AVAILABLE, "src/main/java/bot/photos/kebabs.png", "Judayam shirin shashlik"));
        DB.PRODUCTS.add(new Product("Shashlik 2222", "KEBAB222", "Kebab in russian222", 20_000, 10_000, DB.CATEGORIES.get(0).getId(), ProductStatus.AVAILABLE, "src/main/java/bot/photos/kebabs.png", "Judayam shirin shashlik222"));
    }
}
