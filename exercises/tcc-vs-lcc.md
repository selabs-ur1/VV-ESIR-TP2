# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

TCC and LCC metrics produce the same value for a given Java Class if there isn't any pairs of methods connected indirectly. Now we are going to show an example we found in the [textbook](https://people.irisa.fr/Benoit.Combemale/pub/course/vv/vv-textbook-v0.1.pdf) : 

```java
class Group {
    private int weight;
    private String name;
    private Color color;
    public Group(String name, Color color, int weight) {
        this.name = name;
        this.color = color;
        this.weight = weight;
    }
    public int compareTo(Group other) {
        return weight - other.weight;
    }
    public void draw() {
        Screen.rectangle(color, name);
    }
}
```

Here we haven't any connection between the methods, so TCC = 0 and LCC = 0.

LCC can't be lower than TCC because LCC = TCC + nbPairsIndirectlyConnected / nbPossiblePairs