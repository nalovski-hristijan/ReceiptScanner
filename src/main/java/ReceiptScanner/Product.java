package ReceiptScanner;

public class Product {
    private String name;
    private boolean domestic;
    private double price;
    private String weight;
    private String description;

    // Constructor
    public Product(String name, boolean domestic, double price, String weight, String description) {
        this.name = name;
        this.domestic = domestic;
        this.price = price;
        this.weight = weight;
        this.description = description.length() > 10 ? description.substring(0, 10) : description;
    }


    public String getName() {
        return name;
    }

    public boolean isDomestic() {
        return domestic;
    }

    public double getPrice() {
        return price;
    }

    public String getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", domestic=" + domestic +
                ", price=" + price +
                ", weight=" + weight +
                ", description='" + description + '\'' +
                '}';
    }
}
