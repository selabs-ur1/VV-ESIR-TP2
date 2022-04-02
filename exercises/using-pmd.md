# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

JUGIEAU - BARTHELAT


True positive: 
The first warnings given by PMD about the Main.java are about the fact we import libraries but never use them. The libraries may be useful later on, however they also increase dependencies and take spaces. Removing them is a better practice even if we have to put them back later when we will use them.

False positive :
A warning tells us that our class contains only static methods, which is true but that is not an issue.


In the other file, we have similar true positives like the same import warnings. Moreover, we have other true positives such as warnings about braces. While structural blocks in Java do not need braces if they have only one instruction, this is good programming practice to use them anyway.

