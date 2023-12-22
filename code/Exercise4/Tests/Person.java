public class Person {
    private int age;
    private String name;
    public String name2;

    public String getName() { return name; }

    public boolean isAdult() {
        return age > 17;
    }
}