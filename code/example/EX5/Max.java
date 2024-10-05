package example.EX5;

public class Max {
    public static int max(int a, int b, int c) {
        if (a > b) {
            if(a > c) {
                return a;
            }
            else {
                return c;
            }
        }
        else {
            if (b > c) {
                return b;
            }
            else {
                return c;
            }
        }
    }
}
