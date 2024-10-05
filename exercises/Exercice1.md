# TCC *vs* LCC

1. Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. 

2. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below.

3. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

According to the formula given in the course notes and this [website](https://www.aivosto.com/project/help/pm-oo-cohesion.html), we have :

$TCC = \frac{NDC}{NP}$
and
$LCC = \frac{NDC + NIC}{NP}$

with :
- N = number of methods
- NP = maximum number of possible connections = $\frac{N * (N − 1)}{2}$ 
- NDC = number of direct connections (number of edges in the connection graph)
- NIC = number of indirect connections

### 1. Circumstances where TCC and LCC Produce the Same Value
- If all the methods (or nodes) are disconnected, TCC and LCC will both be equal to 0, because no direct or indirect connection exists between the methods.
- If all the methods are directly connected, TCC and LCC will both be equal to 1, because each method uses attributes that are shared with all the other methods.

### 2. Example of a Class where TCC and LCC are Equal

In the example below, all the methods access separate attributes, so there is no direct or indirect connection between the methods. This makes both TCC and LCC equal to 0.

```java
class Example {

    private int x;
    private int y;
    private int z;

    public Example(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

}
```

In fact, when we identify the attributes used by each method, we obtain that :
- getX() uses the x attribute.
- getY() uses the y attribute.
- getZ() uses the z attribute.

The methods in this class are getX(), getY(), and getZ(). There are therefore 3 possible pairs of methods:
- getX() and getY()
- getX() and getZ()
- getY() and getZ()

Let us calculate TCC. Here, each method accesses a different attribute (x, y, z), so there are no pairs of methods that share an attribute. So the number of pairs accessing a common attribute is 0. The total number of possible pairs is 3. So, we have : 

$TCC = \frac{NDC}{NP} = \frac{0}{3} = 0$

LCC considers indirect connections, but here each method only accesses its own attribute. There are therefore no direct or indirect connections between the methods. So, we have in this case :

$LCC = \frac{NDC + NIC}{NP} = \frac{0 + 0}{3} = 0$

Therefore, we have in this example : $TCC = LCC = 0$


### Can LCC Be Lower Than TCC ?

According to the formula above, we can deduce that LCC will always be greater than TCC. In fact, as these numbers are always positive, we necessarily have $NDC + NIC >= NDC$.

So LCC can never be lower than TCC for a given class.
