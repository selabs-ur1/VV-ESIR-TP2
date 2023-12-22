public class nestedIf {

    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        int c = 3;

        if (a == 1) {
            if (b == 2) {
                if (c == 3) {
                    System.out.println("a = 1, b = 2, c = 3");
                }
            }
        }

        if (a == 1) {
            if (b == 2) {
                if (c == 3) {
                    System.out.println("a = 1, b = 2, c = 3");
                }
            }
        }
    }
    
}
