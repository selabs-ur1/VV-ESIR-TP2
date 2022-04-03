# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer
Given a class with many methods, each method is a node and each pair of methods that have a common instance variable is an edge.
TCC metrics corresponds to the ratio of directly connected pairs of node in the graph to the number or all pairs of nodes.
While LCC metrics is the ratio of the number of all connected pairs directly (like for TCC) or indirectly (connected through one or more nodes) to the number of all pairs of nodes.  

So to get both TCC and LCC metrics to have the same value for a given Java Class, we must have the same number of nodes connected directly than the number of nodes connected indirectly. That means that all nodes must be connected to others directly.  

For example, we could use the following class : 
```java  
public class TCC_LCC{

    public int x;
    public int y;

    public TCC_LCC(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public int a(){
        this.y++;
        return this.x;
    }
    
    public int b(){
        this.x++;
        return this.y;
    }
    
    public void addBoth(){
        this.x++;
        this.y++;
    }
}  
```

As we can see in this code example, both a(), b() and addBoth() methods are using the x and y variables, so there are 3 nodes (a, b and addBoth) and there are 3 edges (one between b and addBoth, one between a and addBoth and another one between a and b). All the edges are directely connected so the values of TCC and LCC are : 
TCC = 3/3 = 1 and LCC = 3/3 = 1. We have the same values for TCC and LCC.  

LCC can't be lower than TCC because all nodes directely connected are part of the nodes indirectely connected. And most of the time LCC is bigger than TCC because there are more indirectely connected pairs of nodes than directely connected pairs of nodes.
