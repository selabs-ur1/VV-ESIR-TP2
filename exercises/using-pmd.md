# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer
Here, we will use Apache Commons Collections.

A true positive that was found with PMD is :
../commons-collections-master/src/test/java/org/apache/commons/collections4/map/Flat3MapTest.java:363:	UnusedLocalVariable:	Avoid unused local variables such as 'mapEntry1'.

The corresponding line is : "final Map.Entry<K, V> mapEntry1 = it.next();".
mapEntry1 is declared but never used, so the line could be shortened to "it.next()".


A false positive found is:
..\commons-collections-master\src\main\java\org\apache\commons\collections4\CollectionUtils.java:629: CompareObjectsWithEquals:       Use equals() to compare object references.

Indeed, the line 629 is : "if (helper.cardinalityA.size() != helper.cardinalityB.size()) {" where .size() returns an integer.
Thus we do want to compare integers here, and not references.
