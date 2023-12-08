# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Rappelons :

$ TCC = \frac{NbDePairesDirectementConnectées}{NbTotalDePaires} $

$ LCC = \frac{NbDePairesConnectées}{NbTotalDePaires} $

Pour que $ TCC $ et $ LCC $ soient égaux, il ne doit pas y avoir de paires indirectement connectées.

```java
class Example {
    int a;

    int A() {
        a = 1;
        return a;
    }

    int B() {
        a = 2;
        return a;
    }
}
```

Dans cet exemple, on a bien $ TCC = \frac{1}{1} $ et $ LCC = \frac{1}{1} $.

LCC ne peut pas être inférieur à TCC pour une classe quelconque car on a :

$ LCC = \frac{NbDePairesDirectementConnectées}{NbTotalDePaires} + \frac{NbDePairesIndirectementConnectées}{NbTotalDePaires} = TCC + \frac{NbDePairesIndirectementConnectées}{NbTotalDePaires} $

D'où $ LCC \geq TCC $
