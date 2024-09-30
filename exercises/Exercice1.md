# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Une classe qui produit les mêmes valeurs pour TCC et LCC serait une classe implémentant des méthodes qui utilisent chacunes tous les attributs de la classe.

Exemple:
public class Example {
    private int A;
    private int B;

    public void increment() {
      A++;
      B++;
    }

    public void decrement() {
      A--;
      B--;
    }

    public multiply() {
      A = A*B;
    }

    public division() {
      A = A/B;
    }
}

increment --- decrement  
  |       X     |
multiply --- division          --- : liaison A et B

TCC = 6/6 = 1 
LCC = 6/6 = 1 = TCC


On ne peut pas avoir un LCC plus petit qu'un TCC car les connexions entre les méthodes prisent en compte pour le calcul du TCC sont aussi prisent en compte dans le calcul du LCC.
LCC = TCC + nombreLienParTransitivité / nombreLienTotal


