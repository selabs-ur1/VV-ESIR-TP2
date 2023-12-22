# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

Nous utiliserons le projet Apache Commons Math
Nous utiliserons le fichier src/main/resources/pmd/pmd-ruleset.xml comme ruleset.

Dans le fichier 

commons-math-legacy/src/main/java/org/apache/commons/math4/legacy/optim/nonlinear/scalar/noderiv/BOBYQAOptimizer.java, 

PMD détecte le problème suivant: This switch case may be be reached by fallthrough from the previous case ce qui signifie qu'un cas de switch case sera accessible après la fin d'un autre du fait d'une absence de break .
Cela peut être considéré comme un vrai positif que nous règlerons en ajoutant simplement un break à la ligne 481 au cas précédent.


Dans le fichier 
commons-math-legacy/src/main/java/org/apache/commons/math4/legacy/analysis/integration/gauss/GaussIntegrator.java,

PMD détecte le problème Useless parentheses. Cela peut être considéré comme un faux positif car ce problème n'a pas vraiment d'impact à notre avis sur le code et donc il n'est pas vraiment nécessaire de la corriger .

