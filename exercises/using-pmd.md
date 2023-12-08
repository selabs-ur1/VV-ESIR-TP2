# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

Nous avons cloné et exécuté PMD sur le projet [Apache Commons Collections](https://github.com/apache/commons-collections) via la commande ci-dessous.

```
pmd -d src/main/java -R rulesets/java/quickstart.xml -f html > report.html; firefox report.html
```

### Issue that should be solve

Dans le fichier `src/main/java/org/apache/commons/collections4/iterators/BoundedIterator.java: 103`, nous avons le problème suivant : `Avoid unnecessary if..then..else statements when returning booleans`.

Cela correspond au bloc de code ci-dessous :

```java
  private boolean checkBounds() {
    if (pos - offset + 1 > max) {
      return false;
    }
    return true;
  }
```

Nous avons donc enlevé les blocs conditionnels inutiles. 

Notre résolution :

```java
private boolean checkBounds() {
    return pos - offset + 1 <= max;
  }
```

### Issue that is not worth solving

Dans le fichier `src/main/java/org/apache/commons/collections4/map/AbstractHashedMap.java: 316`, nous avons le problème suivant : `The instance method name '_putAll' doesn't match '[a-z][a-zA-Z0-9]*'`.

PMD réagit car le nom de la méthode commence par un underscore. Toutefois, ce n'est pas forcément pertinent de corriger ce problème si c'est le résultat d'une convention interne à l'équipe.
