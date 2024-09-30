# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

TCC and LCC metrics produce the same value for a given Java class if all methods of the class are direcly connected to each others. 
An example of this :
``` Java
class Point {

    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        this.x = this.x + (this.y / 2)
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double dot(Point p) {
        return x*p.x + y*p.y;
    }

    public Point sub(Point p) {
        return new Point(x - p.x, y - p.y);
    }

}
```
LCC can't be lower than TCC because is less restrictive, indeed "TCC is defined as the ratio of directly connected pairs of node in the graph to the number or all pairs of nodes. On its side, LCC is the number of pairs of connected (directly or indirectly) nodes to all pairs of node" ([course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph)), TCC handles directly connected pairs of nodes, while LCC handles both directly and indirectly connected pairs of nodes. If a pair of nodes is directly connected, they are also indirectly connected; however, if a pair of nodes is indirectly connected, they are not necessarily directly connected. Therefore, TCC is a subset of LCC in terms of restrictiveness.