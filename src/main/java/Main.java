import bot.MyBot;
import db.DB;
import entity.Category;
import entity.Product;
import entity.Restaurant;
import enums.CategoryStatus;
import enums.ProductStatus;

public class Main {
    public static void main(String[] args) {

        //to_be_deleted_later
        generateMockData();

        MyBot myBot = new MyBot();
        myBot.start();

        System.out.println("RUNNING!");
    }

    private static void generateMockData() {
        DB.RESTAURANTS.add(new Restaurant("RESTAURANT1", "123", "src/main/java/bot/photos/beshqozon.png"));
        DB.RESTAURANTS.add(new Restaurant("RESTAURANT2", "345", "src/main/java/bot/photos/mcdonalds.png"));
        DB.RESTAURANTS.add( new Restaurant("RESTAURANT3", "1313", "src/main/java/bot/photos/burgerking.png"));
        DB.RESTAURANTS.add( new Restaurant("RESTAURANT4", "4314", "src/main/java/bot/photos/kamolon.png"));
        DB.RESTAURANTS.add( new Restaurant("RESTAURANT5", "4314", "src/main/java/bot/photos/kamolon.png"));
        DB.RESTAURANTS.add( new Restaurant("RESTAURANT6", "4314", "src/main/java/bot/photos/kamolon.png"));
        DB.RESTAURANTS.add( new Restaurant("RESTAURANT7", "4314", "src/main/java/bot/photos/kamolon.png"));
        DB.RESTAURANTS.add( new Restaurant("RESTAURANT8", "4314", "src/main/java/bot/photos/kamolon.png"));
        DB.RESTAURANTS.add( new Restaurant("RESTAURANT9", "4314", "src/main/java/bot/photos/kamolon.png"));
        DB.RESTAURANTS.add( new Restaurant("RESTAURANT10", "4314", "src/main/java/bot/photos/kamolon.png"));


        DB.CATEGORIES.add(new Category("Shashliklar", 10, CategoryStatus.ACTIVE));
        DB.CATEGORIES.add(new Category("Palovlar", 10, CategoryStatus.ACTIVE));
        DB.CATEGORIES.add(new Category("Ichimliklar", 10, CategoryStatus.ACTIVE));
        DB.CATEGORIES.add(new Category("Suyuq Ovqatlar", 10, CategoryStatus.ACTIVE));
        DB.CATEGORIES.add(new Category("Suyuq Ovq313131atlar", 10, CategoryStatus.ACTIVE));

        DB.PRODUCTS.add(new Product("Shashlik", 10_000, 5_000, DB.CATEGORIES.get(0).getId(), ProductStatus.AVAILABLE, "src/main/java/bot/photos/kebabs.png","Judayam shirin shashlik"));
    }
}
