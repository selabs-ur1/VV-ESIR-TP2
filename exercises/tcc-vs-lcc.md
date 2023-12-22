# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

#TCC vs LCC

	Afin que le Loose Class Cohesion et le Tight Class Cohesion soit égaux, il faut, qu'à l'intérieur de la classe concernée, le nombre de méthodes connectées directement soit égal au nombre de méthodes connectées directement et indirectement. Exemple du graphe d’une classe Foo : 



##Calculs du TCC et du LCC : 
Il y a 3 paires de méthodes. 
Chaque paire de méthode a un attribut en commun (connectée directement).
Toutes les méthodes sont reliées.

TCC = 3/3 = 1.
LCC = 3/3 = 1.



```
Foo.java : 
public class Foo {


   private int x;
   private int y;
   private int z;


   public Foo(int x, int y, int z) {
       this.x = x;
       this.y = y;
       this.z = z;
   }


   // On crée 3 méthodes qui sont toutes connectées directement entre elles par l'intermédiaire des attributs de la classe


   public boolean compare_x_and_y() {
       return x > y;
   }


   public boolean compare_y_and_z() {
       return y > z;
   }


   public boolean compare_x_and_z() {
       return x > z;
   }


   public static void main(String[] args) {
       Foo foo = new Foo(0, 1, 2);
       foo.compare_x_and_y();
       foo.compare_y_and_z();
       foo.compare_x_and_z();
   }
}
```
Le LCC est forcément supérieur ou égal au TCC car au numérateur il compte à la fois les connexions directes (comme le TCC) et les connexions indirectes. Le dénominateur quant à lui reste inchangé et est le nombre total de paires de nœuds.

