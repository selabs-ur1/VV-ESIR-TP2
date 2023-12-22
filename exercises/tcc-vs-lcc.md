# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Les métriques TCC et LCC produisent la même valeur pour une classe Java donnée si les fonctions de cette classe n’ont pas de lien indirect. C'est à dire que soit toutes les fonctions d'une classe utilise uniquement les même variables ou elle n' utilise aucune variable en commun. On peut voir cela en reprenant la formule de ces métriques : 

NP = nombre maximum de connections possibles
= N * (N − 1) / 2 où N est le nombre de méthodes

NDC = Nombre de connections directs
NID = Nombre de connections indirects

TCC = NDC / NP
LCC = (NDC + NIC) / NP

Pour que TCC = LCC,
TCC=LCC
NDC / NP=(NDC + NIC) / NP
NDC=NDC+NIC

NIC=0

On en déduit qu'il faut qu'une classe n'est pas de lien indirect pour que les métriques TCC et LCC produisent la même valeur.

La classe ci-dessous montre l'exemple d'une classe dont les métriques TCC et LCC produisent la même valeur.
```java
// TCC=1/6 LCC=1/6 
public class ExampleClass {
    
    private int data1;
    private int data2;
    private int data3;
    
    public ExampleClass(int data1, int data2, int data3) {
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
    }
    
    public void processData1() {
        // Code processing data1
        System.out.println(data1);
    }
    public void processData12() {
        // Code processing data1
        System.out.println(data1+data2);
    }
    public void processData3() {
        // Code processing data3
        System.out.println(data3);
    }
   
}

```


Afin de voir si LCC peut être inférieur à TCC, nous reprenons leur formule : 

On sait que NP, NID et NDC sont supérieurs ou égales à 0.

Donc, 

NDC<= NDC + NID

NDC / NP <= (NDC + NID)/NP

TCC <= LCC

LCC ne peut donc pas être inférieur TCC.

