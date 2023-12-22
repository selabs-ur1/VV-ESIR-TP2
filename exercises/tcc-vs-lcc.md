# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer
Pour calculer les scores LCC et TCC, on représente les méthodes par des noeuds qu'on relie entre eux par leurs variables communes. 
Le score LCC compte le nombre de paires de noeud reliés directement ou indirectement sur le nombre de paires de noeuds total tandis que le score TCC compte seulement le nombre de paires de noeud reliés directement sur le nombre de paires de noeuds total. Le nombre de paires reliés directement ou indirectement  ne peut être inférieur au nombre de paires reliés directement, ainsi le score LCC est toujours supérieur ou égal au score TCC. 

Dans l'exemple ci-dessous, nous avons deux méthodes dans la classe Person : isOlder(Person p) et toString(). Les deux méthodes ne partagent aucune instance de variable et ne sont donc pas réliées. Il n'y aucun lien indirect non plus donc les scores TCC et LCC sont égaux et valent 0.

```java
class Person {

    private String lastname;
    private String firtname;
    private int age; 

    public Person(String lastname, String firstname, age) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.age = age;
    }

    public boolean isOlder(Person p) {
        return p.age < this.age ;
    }

    public String toString() {
        return this.lastname + this.firstname;
    }

}
```

