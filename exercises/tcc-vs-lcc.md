# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Soit M et N deux méthodes .
Dans le contexte de LCC et TCC, M et N sont liés directement si et seulement si il existe au mions une instance de variable commune aux deux . 
Pour que TCC et LCC soient égaux, Il faut que pour tout couple de méthodes M et N du graphes, il existe une liaision directe entre ces deux méthodes.

Exemple :

public class Person{
    private String name;

    public Person(String name){
        this.name = name;
    }

    public getName(){
        return this.name;
    }

    public setName(String name){
        this.name = name;
    }
}


C'est impossible que LCC soit inférieur à TCC .
Supposons que LCC soient supérieur à TCC
Soit a le nombre de liaison directe , b le nombre de liasion indirecte et c le nombre total de couple de méthode du graphe
LCC < TCC <=> (a+b/c)  < a/c <=> a+b < a <=> b < 0 ce qui est absurde car b >= 0 
Donc LCC ne peut pas être inférieur à TCC.