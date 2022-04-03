# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

There is an issue where a catch block is empty:

C:\Users\charl\WorkSpaces\MDI\commons-collections\src\test\java\org\apache\commons\collections4\collection\AbstractCollectionTest.java:1325:    EmptyCatchBlock:        Avoid empty catch blocks

Here is the code :

```
/**
* Handle the optional exceptions declared by {@link Collection#containsAll(Collection)}
* @param coll
* @param sub
*/
protected static void assertNotCollectionContainsAll(final Collection<?> coll, final Collection<?> sub) {
    try {
        assertFalse(coll.containsAll(sub));
    } catch (final ClassCastException | NullPointerException e) {
            //apparently not
    }
}
```

This block doesn't do anything, as a comment already states in the catch block, so we can remove it entirely.


For a false positive, there are a lot like this one:

C:\Users\charl\WorkSpaces\MDI\commons-collections\src\test\java\org\apache\commons\collections4\trie\PatriciaTrieTest.java:68:  AvoidDuplicateLiterals: The String literal "Ammun" appears 4 times in this file; the first occurrence is on line 68

It reports the number of strings with a certain contributor name, which means it only describes the number of contributions this person did. It does not need any change as it is not a bug.
