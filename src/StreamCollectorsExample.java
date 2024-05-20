import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Order {
    private String product;
    private double cost;

    public Order(String product, double cost) {
        this.product = product;
        this.cost = cost;
    }

    public String getProduct() {
        return product;
    }

    public double getCost() {
        return cost;
    }
}

public class StreamCollectorsExample {
    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("Laptop", 1200.0),
                new Order("Smartphone", 800.0),
                new Order("Laptop", 1500.0),
                new Order("Tablet", 500.0),
                new Order("Smartphone", 900.0)
        );

        // Группируем заказы по продуктам
        Map<String, Double> totalCostByProduct = orders.stream()
                .collect(Collectors.groupingBy(Order::getProduct,
                        Collectors.summingDouble(Order::getCost)));

        // Сортируем продукты по убыванию общей стоимости
        List<Map.Entry<String, Double>> sortedProducts = totalCostByProduct.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toList());

        // Выбираем три самых дорогих продукта
        List<Map.Entry<String, Double>> topThreeProducts = sortedProducts.stream()
                .limit(3)
                .collect(Collectors.toList());

        // Выводим результат
        System.out.println("Три самых дорогих продукта:");
        topThreeProducts.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}
