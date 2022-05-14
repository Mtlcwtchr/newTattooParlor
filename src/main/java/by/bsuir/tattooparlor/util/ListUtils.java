package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    private ListUtils() {

    }

    public static List<List<Product>> mapToQuarts(List<Product> products) {
        List<List<Product>> quarts = new ArrayList<>();

        List<Product> quart = new ArrayList<>();
        int j = 0;
        for (Product product : products) {
            quart.add(product);

            if(++j % 4 == 0 || j == products.size()) {
                quarts.add(quart);
                quart = new ArrayList<>();
            }
        }

        return quarts;
    }
}
