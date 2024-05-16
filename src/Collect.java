import java.util.HashMap;
import java.util.Map;

public class Collect {
    public static <T> Map<T, Integer> countOfElements(T[] array) {
        Map<T, Integer> elements = new HashMap<>();
        for (T element : array) {
            elements.put(element, elements.getOrDefault(element, 0) + 1);
        }
        return elements;
    }

    public static void main(String[] args) {
        Integer[] numbers = {1, 2, 2, 3, 3, 3, 4, 4, 4, 4};

        Map<Integer, Integer> occurrences = countOfElements(numbers);
        for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
