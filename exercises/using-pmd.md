# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer
When we ran PMD on the source code, we found an issue about a clone method which should be a public method but it was a protected method. (org.apache.commons.collections4.map). So we proposed to change the "protected" to "public".


PMD warned us on a wrong utilisation of equality comparaison. In the method **isEqualList**, the first if compare the two list with "==" but here we want to compare the adresses of the objects. So it is a false positive because the utilisation of "==" is well used here.