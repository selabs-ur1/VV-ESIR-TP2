# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

TCC and LCC are equals if :
 - all methods are connected
 - or all existing connection between methods are direct
 - or there is no method connected to each other

Here is an exemple of LCC and TCC being equal :

public class question_1 {

    private int data1;
    private int data2;
    private int data3;

    public question_1(int data1, int data2, int data3) {
        this.data1=data1;
        this.data2=data2;
        this.data3=data3;
    }

    public int getData1() {
        return this.data1;
    }

    public int getData2() {
        return this.data2;
    }

    public int getData3() {
        return this.data3;
    }
}

In this exemple, the 3 methods aren't connected so LCC = TCC = 0


LCC is a ratio that uses the number of connections (direct or indirect) and TCC is a ratio that uses the number of direct connections so TCC can only be inferior or equals to LCC.