# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer


### Project tested and rules used

We have tested the **commons-cli-master** project with some rules that are:
- ** category/java/errorprone.xml/EmptyCatchBlock**
- ** category/java/bestpractices.xml/AbstractClassWithoutAbstractMethod **

### True problem detected

Here is a problem (true positive) detected on the first rule. The class is on the following path: 
**commons-cli-master\src\test\java\org\apache\commons\cli\HelpFormatterTest.java** at line **356**.

```
    @Test
    public void testPrintHelpWithEmptySyntax() {
        final HelpFormatter formatter = new HelpFormatter();
        try {
            formatter.printHelp(null, new Options());
            fail("null command line syntax should be rejected");
        } catch (final IllegalArgumentException e) {
            // expected
        }

        ...
    }
```

The catch block is empty and contains no code to handle the exception.


### Solving the problem

To solve this problem, we would add a display of the problem with the following instruction: e.printStackTrace();

``````
    @Test
    public void testPrintHelpWithEmptySyntax() {
        final HelpFormatter formatter = new HelpFormatter();
        try {
            formatter.printHelp(null, new Options());
            fail("null command line syntax should be rejected");
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        }

        ...
    }
```
```
