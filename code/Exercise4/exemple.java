
public class Person {
    private int age;
    private String name;
    public String lastName;
    
    public String getName() { return name; }

    public boolean isAdult() {
        return age > 17;
    }
}