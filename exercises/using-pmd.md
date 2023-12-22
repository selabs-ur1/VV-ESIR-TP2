# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

On a cloné le dépôt de [Commons Lang](https://github.com/apache/commons-lang) et utilisé le ruleset par défaut de Java.

Un vrai positif peut être *Document empty constructor* dans le fichier `src/main/java/org/apache/commons/lang3/builder/RecursiveToStringStyle.java`. Il y a bien :

```java
    /**
     * Constructor.
     */
    public RecursiveToStringStyle() {
    }
```

qu'on pourrait tout simplement supprimer.

Un faux positif peut être *The static method name 'init_Aarch_64Bit' doesn't match '[a-z][a-zA-Z0-9]*'* dans le fichier `src/main/java/org/apache/commons/lang3/ArchUtils.java`. On pense que cette règle n'est pas importante ici, surtout que ça concerne des méthodes faites pour déterminer l'architecture du processeur en interagissant avec la JVM.
