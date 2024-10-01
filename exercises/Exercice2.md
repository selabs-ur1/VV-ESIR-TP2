
# Using PMD


Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.


## Answer

We have chosen <a href="https://github.com/apache/commons-math" target="_blank">commons-math</a> source code for this exercise.  

After running 

```
pmd check -f text -R rulesets/java/quickstart.xml -d ./src
```
with src is the source code of commons-math library.

PMD has detected many problems that could be source of bugs, often due to poor coding practice.

For example

[!WARNING]  
./src/userguide/java/org/apache/commons/math4/userguide/genetics/PolygonChromosome.java:106:	UselessParentheses:	Useless parentheses.
