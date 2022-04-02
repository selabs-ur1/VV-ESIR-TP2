# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

JUGIEAU - BARTHELAT

The TCC is equal to the LCC if all connected pairs are directly connected (see the example below)

The LCC is a ratio that uses the number of connections overall, direct or indirect, whereas the TCC only uses the number of direct connections. As such, the TCC will always be lower than or equal to the LCC.


Example : 

We have 4 methods.
The maximum number of possible connections is 6.

Every method is connected through x aside from getY() which uses y and is completely independant. The only connected methods are connected directly.

TCC = 3/6
LCC = 3/6

```java
public class TCCEqualsLCC
{
    private int x;
    private int y=0;
    
    
    /** The following methods are all connected through x **/
    
    public TCCEqualsLCC(int value)
    {
   	 x=value;
    }
    
    public int getX()
    {
   	 return x;
    }
    
    public int opX()
    {
   	 return x=4*x+1;
    }
    
    /** getY() is the only method not using x and using y, it isn't connected to the others **/
    
    public int getY()
    {
   	 return y;
    }
    
}
```

