# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

TCC et LCC produisent les mêmes valeurs quand il n'y a que des connexions directes entre les méthodes d'une classe, et aucune connexion indirecte.

Un exemple d'une telle classe peut être :

````Java
public class example{
    public void setA(int a) {
        this.a = a;
    }

    public int getA() {
        return a;
    }

    private int a;

}
````
Ici, TCC vaut 1 et LCC vaut 1 puisque toutes les méthodes sont directement connectées et qu'il n'y a pas de connexion indirecte.

LCC ne peut jamais être plus petit que TCC puisque TCC ne considère que les connexions directes 
tandis que LCC considère les connexions directes ainsi que les connexions indirectes.
LCC vaut donc au moins la valeur de TCC, quand toutes les connexions dans la classe sont directes.
