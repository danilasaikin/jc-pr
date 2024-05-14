import java.util.ArrayList;
import java.util.List;

public class MyStringBuilder {
    private List<String> snapshots;
    private StringBuilder stringBuilder;

    public MyStringBuilder() {
        this.snapshots = new ArrayList<>();
        this.stringBuilder = new StringBuilder();
        saveSnapshot();
    }

    public void append(String str) {
        stringBuilder.append(str);
        saveSnapshot();
    }

    public void undo() {
        if (snapshots.size() > 1) {
            snapshots.remove(snapshots.size() - 1);
            stringBuilder = new StringBuilder(snapshots.get(snapshots.size() - 1));
        }
    }

    public String toString() {
        return stringBuilder.toString();
    }

    private void saveSnapshot() {
        snapshots.add(stringBuilder.toString());
    }

    public static void main(String[] args) {
        MyStringBuilder builder = new MyStringBuilder();
        builder.append("Hello ");
        builder.append("world!");
        System.out.println(builder); // Hello world!
        builder.undo();
        System.out.println(builder); // Hello
    }
}
