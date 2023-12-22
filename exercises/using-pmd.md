# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer
On choisit le projet "Apache Collection" pour notre analyse statique PMD.
Dans le retour de l'analyse, nous avons vu la ligne suivante : 
->  functors\NullIsFalsePredicate.java:68:    SimplifyBooleanReturns:    This if statement can be replaced by return !{condition} || {elseBranch};
Le problème pointait sur cette méthode : 
```java
public boolean evaluate(final T object) {
        if (object == null) {
            return false;
        }
        return iPredicate.evaluate(object);
    }
```
Nous pouvons effectivement modifier le code comme suit : 
```java
public boolean evaluate(final T object) {
        return !(object == null) || iPredicate.evaluate(object);
    }
```
C'est un vrai positif de l'analyse statique de PMD. 
On trouve par contre un faux positif avec cette ligne :
AbstractSerializableSetDecorator.java:22:	UnnecessaryImport:	Unused import 'java.util.Collection'

PMD nous dis que l'import de java.util.Collection n'est pas nécessaire, or, en lisant le code, on remarque qu'on utilise bien une Collection Java.
Voici un extrait du code :
```java
import java.util.Collection;

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        setCollection((Collection<E>) in.readObject());
    }
```

