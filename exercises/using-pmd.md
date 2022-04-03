# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer
In the Apache Collections map, PMD tells us what we think is a false positive in the method isEqualKey in the AbstracReferenceMap.java.

```java
return key1 == key2 || key1.equals(key2);
```

PMD tells us “Use equals() to compare object references.” but we can see that the function already uses equals() to check if the keys have the same value. It uses the “==” operator to check if it’s the same reference

Then in AbstractBitwiseTrie.java, PMD tells us that at line 204 “Avoid unnecessary if..then..else statements when returning booleans” for the following code : 
```java
if (compare(key, other.getKey()) && compare(value, other.getValue())) {
return true;
}
```

We think it is a true positive because it doesn't change the result and for us it doesn’t make the code more readable. We would change the code as the following : 

```java
return (compare(key, other.getKey()) && compare(value, other.getValue());
```
