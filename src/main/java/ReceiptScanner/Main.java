package ReceiptScanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final String url = "https://interview-task-api.mca.dev/qr-scanner-codes/alpha-qr-gFpwhsQ8fkY1";

    public static void main(String[] args) {
        try {
            String response = getApiData();
            List<Product> products = listProducts(new JSONArray(response));


            List<Product> domesticProducts = products.stream()
                    .filter(Product::isDomestic)
                    .sorted(Comparator.comparing(Product::getName))
                    .collect(Collectors.toList());

            List<Product> importedProducts = products.stream()
                    .filter(p -> !p.isDomestic())
                    .sorted(Comparator.comparing(Product::getName))
                    .collect(Collectors.toList());


            System.out.println("Domestic Products:");
            domesticProducts.forEach(Main::printProduct);

            System.out.println("\nImported Products:");
            importedProducts.forEach(Main::printProduct);


            printTotals(domesticProducts, importedProducts);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String getApiData() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private static List<Product> listProducts(JSONArray jsonArray) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String weight = jsonObject.has("weight") ? jsonObject.getInt("weight") + "g" : "N/A";
            products.add(new Product(jsonObject.getString("name"), jsonObject.getBoolean("domestic"), jsonObject.getDouble("price"), weight, jsonObject.getString("description")));
        }
        return products;
    }

    private static void printProduct(Product product) {
        System.out.printf("Name: %s, Price: $%.2f, Weight: %s, Description: %s\n",
                product.getName(), product.getPrice(), product.getWeight(), product.getDescription());
    }

    private static void printTotals(List<Product> domesticProducts, List<Product> importedProducts) {
        double domesticTotal = domesticProducts.stream().mapToDouble(Product::getPrice).sum();
        double importedTotal = importedProducts.stream().mapToDouble(Product::getPrice).sum();

        System.out.printf("\nTotal cost of Domestic Products: $%.2f\n", domesticTotal);
        System.out.printf("Total cost of Imported Products: $%.2f\n", importedTotal);

        System.out.printf("\nTotal number of Domestic Products: %d\n", domesticProducts.size());
        System.out.printf("Total number of Imported Products: %d\n", importedProducts.size());
    }
}
