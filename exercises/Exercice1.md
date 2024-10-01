# TCC *vs* LCC

1. Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. 

2. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below.

3. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

### 1. Circumstances where TCC and LCC Produce the Same Value
- If all the methods (or nodes) are disconnected, TCC and LCC will both be 0, because no direct or indirect connection exists between the methods.
- If all the methods are directly connected, TCC and LCC will both be 1, because each method uses attributes that are shared with all the other methods.

### 2. Example of a Class where TCC and LCC are Equal

In the example below, all the methods access separate attributes, so there is no direct or indirect connection between the methods. This makes both TCC and LCC equal to 0.

```java
class Example {

    private int x;
    private int y;
    private int z;

    public Example(int x, int x, int x) {
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

### Can LCC Be Lower Than TCC ?

According to the formula given in the course notes and this [website](https://www.aivosto.com/project/help/pm-oo-cohesion.html), we have :

$TCC = \frac{NDC}{NP}$
and
$LCC = \frac{NDC + NIC}{NP}$

with :
- N = number of methods
- NP = maximum number of possible connections = $\frac{N * (N − 1)}{2}$ 
- NDC = number of direct connections (number of edges in the connection graph)
- NIC = number of indirect connections

We can deduce from this that LCC will always have be greater than TCC. In fact, as these numbers are always positive, we necessarily have $NDC + NIC >= NDC$.

So LCC can never be less than TCC for a given class.

