package example.EX5;

public class SumOfNonPrimes {
    int sumOfNonPrimes(int limit) {
        int sum = 0;
        OUTER: for (int i = 0; i < limit; ++i) {
            if (i <= 2) {
                continue;
            }
            for (int j = 2; j < i; ++j) {
                if (i % j == 0) {
                    continue OUTER;
                 }
            }
            sum += i;
        }
        return sum;
    }
}
