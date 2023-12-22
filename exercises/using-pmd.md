# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

Nous avons choisi le projet Apache Commons Maths.

**True positive**

En utilisant la règle rulesets/java/quickstart.xml, on obtient cette sortie :
`commons-math/commons-math-core/src/main/java/org/apache/commons/math4/core/jdkmath/AccurateMath.java:396:    UselessParentheses:    Useless parentheses.`

On constate qu’à la ligne 396 de AccurateMath.java on a des parenthèses inutiles:
`return (0.5 * t) * t;`

Pour résoudre cette violation, on peut réécrire le retour de cette manière :
`return 0.5 * t * t;`

**False Positive**

`commons-math/commons-math-core/src/main/java/org/apache/commons/math4/core/jdkmath/AccurateMath.java:699:    UnnecessaryFullyQualifiedName:    Unnecessary qualifier 'AccurateMath': 'log' is already in scope`

On constate qu’à la ligne 699 de AccurateMath.java, on a : 
	`return AccurateMath.log(a + Math.sqrt(a * a - 1));`

On pense qu’il ne vaut mieux pas résoudre cette violation car elle a été ajoutée volontairement afin d’améliorer la lisibilité du code afin qu’on comprenne aisément que la méthode log est statique.




