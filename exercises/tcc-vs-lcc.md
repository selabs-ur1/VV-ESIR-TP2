# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such a class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

TCC and LCC produce the same value when the class has all its nodes directly connected, like in this exemple:

```
class ExempleTCCLCC {
    private int a;
    
    public ExempleTCCLCC(int a) {
        a = a;
    }
    
    public int getA() {
        return this.a;
    }
    
    public int addA() {
        return this.a+1;
    }
}
```

LCC cannot be lower than TCC because it is at minimum equal or higher as it counts indirect connections too.
