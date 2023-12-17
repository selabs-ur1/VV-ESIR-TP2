# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

TCC et LCC produisent le même résultat lorsque tous les nœuds sont déconnectés (toutes les méthodes utilisent des variables différentes) ou bien que tous les nœuds sont connectés entre eux (toutes les méthodes manipulent les mêmes variables).

```java
public class Example {
    public String data1;
    public String data2;

    public Exameple(String data1, String data2) {
        this.data1 = data1;
        this.data2 = data2;
    }

    public void set(String data1, String data2) {
        this.data1 = data1;
        this.data2 = data2;
    }

    public String toString() {
        return this.data1+ this.data2;
    }
}
```

Non LCC ne peut pas être inférieur à TCC car TCC compte les connexions directes tandis que LCC compte aussi les connexions indirectes, donc on a toujours LCC >= TCC.