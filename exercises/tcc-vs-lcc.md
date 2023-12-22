# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Le seul cas dans lequel ces métriques produiront la même valeur, c'est si toutes les méthodes partagent au moins une variable entre elles (tous les noeuds du graphes ont des liens directs entre eux). Si dans un graphe, nous avons plusieurs sous-graphes, il faut qu'il n'y ait aucun lien entre eux pour que TCC et LCC renvoient la même valeur.  

```java
public class ExempleCohesionClasse {
    private int attributCommun;

    // Les deux méthodes accèdent à attributCommun

    public void methode1(int parametre) {
        attributCommun = parametre * 3;
    }

    public void methode2(int parametre) {
        attributCommun = parametre + 5;
    }
}
```

Il est impossible que LCC ait une valeur inférieure à TCC. TCC utilise les liens directs et LCC les liens directs et indirects. LCC a donc forcément au moins autant de liens que TCC. 
