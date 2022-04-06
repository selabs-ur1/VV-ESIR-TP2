public class Person {

    private int age;

    private String name;

    public String getName() {
	  System.out.println("OK");
        return name;
    }

    public boolean isAdult() {
        return age > 17;
    }
}
