# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

Voici ce que j'ai obtenu après passage de PMD sur la bibliothèque Java Collections : [Rapport PMD](https://github.com/TheKingHydra/VV-ESIR-TP2/edit/main/exercises/Rapport%20PMD.txt)

On peut prendre comme cas de faux positif le premier retour de PMD :  ->  ArrayStack.java:56:	UncommentedEmptyConstructor:	Document empty constructor

En effet, ici PMD nous demande de commenter un constructeur vide, or cela n'est pas nécessaire (ou même utile). On ne commente pas du code vide.
