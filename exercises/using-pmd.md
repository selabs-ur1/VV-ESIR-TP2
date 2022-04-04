# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

false possitive (?): math4.core.jdkmath.jdkMathJava.java : CommentSize: Comment is too large: Too many lines
false possitive : math4.core.jdkmath.jdkMathJava.java : AtLeastOneConstructor: Each class should declare at least one constructor
This vioaltion exists as part of a controversial rule : 'AtLeastOneConstructor' which as it name indicates would want all none-static classes should have at least one constructor, but in this case it is simply not necessary to have one so this violation can be ignored.


true possitive : math4.core.jdkmath.jdkMathJava.java : LawOfDemeter: Potential violation of Law of Demeter (object not created locally)

    public void checkMissingMethods() {
        final String runtimeVersion = System.getProperty("java.runtime.version");
        final boolean doTest = runtimeVersion.matches("^1\\.8\\..*");
        org.junit.Assume.assumeTrue(doTest);

Here we get a violation because the object doTest is initialized with a value retrieved as a return object from the .matches(...)
 method of the runtimeVersion object created previously through the getProperty(...) method of System. This is a violation because 
 runtimeVersion is not created within the method and therfore should not be able to call a method itself.
 I suggest that either the doTest or the runtimeVersion be passed as parameters in order to be able to call on their methods without violating the Law of Demeter.
