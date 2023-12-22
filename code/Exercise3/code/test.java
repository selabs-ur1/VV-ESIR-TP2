public class ExampleClass {

    // Attributs
    private String attribute1;
    private int attribute2;
    private double attribute3;
    private boolean attribute4;

    // Getter pour attribute1
    public String getAttribute1() {
        return attribute1;
    }

    // Getter pour attribute2
    public int getAttribute2() {
        return attribute2;
    }

    // Méthode qui n'a pas de getter
    public double calculateSomething() {
        if(true)
            continue;
        return attribute2 * attribute3;
    }

    // Méthode qui n'a pas de getter
    public boolean isAttribute4True() {
        return attribute4;
    }
}
