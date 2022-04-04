# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

TCC and LCC
The Tight and Loose Class Cohesions relate to the cohesion of a class as a metric with the visible methods of a class. 

The TCC is measured by calculating the fraction of directly connected pairs of methods:

TCC = number of directly connected methods / maximum number of possible connections

The LCC is measured by calculating the fraction of directly or transitively connected pairs of methods:

LCC = (number of directly connected methods + number of indirectly connected methods) / maximum number of possible connections

Both of those metrics exist in the [0,1] range and follow TCC<=LCC.

The case where TCC = LCC is can happen when there are not indirectly connected methods, so for example if all the methods are connected directly.
An example:
```
public class Point{
    int x;
    int y;
    
    private int get_x(){
        return this.x;
    }
    private int get_y(){
        return this.y;
    }
    
    public int[] get_coords(){
        int[] arr = {get_x(), get_y()};
        return arr;
    }
    
    public int[] get_line(int new_x, int new_y){
        int[] arr = get_coords();
        arr[0] += new_x;
        arr[1] += new_y;
        return arr;
    }
}

