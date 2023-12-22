package fr.istic.vv;

public class Foo {
    private int a;
    private int b;
    private int c;

    public Foo(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int sum() {
        return a + b;
    }

    public int getC(){
        return c;
    }

    //do a bad cyclomatic method 
    public int badCyclomaticMethod(int a, int b, int c){
        if(a > 0){
            if(b > 0){
                if(c > 0){
                    return a + b + c;
                }
            }
        }
        return 0;
    }

    //do a good cyclomatic method
    public int goodCyclomaticMethod(int a, int b, int c){
        if(a > 0 && b > 0 && c > 0){
            return a + b + c;
        }
        return 0;
    }

    public int testing_ifs(int a, int b, int c){
        if(a > 0){
            a = a + 1;
        }
        if(b > 0){
            b = b + 1;
        }
        if(c > 0){
            c = c + 1;
        }
    }
}
