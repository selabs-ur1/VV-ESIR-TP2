# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

On aura égalité entre la LCC et la TCC si toutes les méthodes sont connectées directement, ou au contraire aucune connexion. En effet, la TCC compte le nombre de liens directs entre les méthodes, tandis que la LCC compte les liens directs ainsi que les liens indirects, pour avoir une égalité ils ne faut donc aucun lien indirect. En ce sens, la LCC est forcément supérieure ou égale à la TCC.

Exemple de classe pour une égalité :

public class CohesiveClass {
    private int variable1;
    private int variable2;
    
    public void method1() {
        variable1 = 5;
    }
    
    public void method2() {
        variable2 = 10;
    }
  }

  Dans cet exemple, il n'y a aucun lien entre les deux méthodes, donc la TCC et la LCC valent 0. D'ailleurs on peut remarquer qu'avec seulement deux méthodes, on aura forcément égalité, vu qu'aucun lien indirect n'est possible. 
