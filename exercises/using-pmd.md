# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

Pmd trouve ce vrai positive : "This if statement can be replaced by `return !{condition} || {elseBranch};`", ce qui signifie qu'il est possible de simplifier un bout de code en le rendant plus clair. Le code suivant peu donc être modifier :

if (!(obj instanceof MutableInteger)) {
                return false;
            }

Il deviendra ceci :

return !(obj instanceof MutableInteger)




"Document empty constructor" est un faux positive trouvé par pmd qui indique qu'une classe n'a pas de constructeur. Cependant, il est inutile de régler l'erreur puisque par default, lorsqu'il n'y a pas de constructeur, java construit un constructeur par defaut.
