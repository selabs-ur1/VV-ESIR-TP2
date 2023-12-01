# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

En se basant sur le code source d'Apache Commons Collections, on lance une analyse avec PMD. On sélectionne 2 problèmes initiées :

- Vrai positif : map/Flat3Map.java, certains switch ne comporte pas de cas par défaut ce qui peut potentiellement résultat à une erreur non detécté si jamais aucun des cas prévue n'est valide (le switch est effectué sur un entier).

- Faux positif : iterators/ObjectGraphIterator.java, PMD détecte un problème car on compare 2 référence avec l'opérateur == au lieu d'utiliser la fonction equals() mais dans le cas ou l'on compare avec null, l'appel à la fonction equals() n'est pas nécessaire. Je le laisserai pour éviter de potentiel erreur si dans la fonction equals(), le fait de passer un objet null n'est pas supporté.