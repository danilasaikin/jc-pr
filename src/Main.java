public class Main {
    interface Filter {
        Object apply(Object o);
    }

    public static Object[] filter(Object[] array, Filter filter) {
        Object[] result = new Object[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = filter.apply(array[i]);
        }
        return result;
    }

    public static void main(String[] args) {
        Integer[] numbers = {1, 2, 3, 4, 5};

        Filter doubleFilter = new Filter() {
            @Override
            public Object apply(Object o) {
                Integer number = (Integer) o;
                return number * 2;
            }
        };

        Object[] doubledNumbers = filter(numbers, doubleFilter);
        for (Object num : doubledNumbers) {
            System.out.println(num);
        }
    }
}
