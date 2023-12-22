# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

We chose to test PMD on the Apache Commons Collections project. We used the default rule base. 

For example we found :
- A true positive error : *When doing a String.toLowerCase()/toUpperCase() call, use a Locale*; issue at line 51 in *commons-collections-master/src/test/java/org/apache/commons/collections4/collection/TransformedCollectionTest.java*. Using this method without specifying an argument implicitly uses Locale::getDefault(). The problem being that the default local depends on the JVM.
It could be patched by adding the Locale as an argument to the method call.
An example could be : originalText.toLowerCase(Locale.US);


- A false negative
One instance of the *Avoid using implementation types like 'ArrayList'; use the interface instead* problem was identified at line 389 in *commons-collections-master/src/main/java/org/apache/commons/collections4/CollectionUtils.java*. In this case, the method requires the use of ArrayList instead of List due to a specific process that can only be performed with ArrayList on this particular List.