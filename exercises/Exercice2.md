
# Using PMD


Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.


## Answer
we choose the java project Apache Commons CLI from github

__ISSUE FIND BY PMD THAT SHOULD BE SOLVED ( true positive)__

**File**: src/main/java/org/apache/commons/cli/HelpFormatter.java

**PMD Rule Violated**: CloseResource

**Description**:  PMD recommends explicitly closing resources like PrintWriter objects after use to avoid resource leaks.

this is revelant because failing to close resources like output streams can lead to memory leaks, which is critical in long-term environments like servers.

**Proposition of correction**
```java

PrintWriter writer = null;
try {
    writer = new PrintWriter(new FileWriter("output.txt"));
    // Utilisation du writer
} finally {
    if (writer != null) {
        writer.close(); // Fermeture explicite du PrintWriter
    }
}

```

__ISSUE FIND BY PMD THAT IS NOT WORTH SOLVED ( false positive)__

**File**: src/main/java/org/apache/commons/cli/DefaultParser.java:615

**Violated Rule**: SimplifyBooleanReturns

**Description**: PMD recommends simplifying an if statement into a direct return of the condition, e.g., return condition;.

It's a False Positive and it should not be solve because there is no functional issue here; the simplification will only slightly improve the readability of the code.
