# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

True positive :
We used PMD on the Apache Commons Collections project. By examining returns from PMD, we can see that in Flat3Map.java, there are some switch which don't have a default case. We can look at the switch from line 306 and 329 where we just need to add a default case which doesn't do anything.

False positive :
We can see in CursorableLinkedList.java at line 529 that there are useless parenthesis. This is a false positive because it doesn't hinder the functionnement of the code and it isn't a error source.

