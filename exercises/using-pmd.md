# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

### False Positive

<b>File</b> : /commons-math-master/commons-math-core/src/main/java/org/apache/commons/math4/core/jdkmath/AccurateMath.java:4086 

<b>Code</b>        :
```java
PrintStream out = System.out;
```

<b>PMD error</b>   : ```"CloseResource:	Ensure that resources like this PrintStream object are closed after use"``` 

PMD is complaining because there is no call to "out.close()" in the method. But calling "out.close()" will close System.out wich will cause every upcoming   System.out.println() to fail.  


### True Positive

<b>File</b>        : /commons-math-master/commons-math-core/src/main/java/org/apache/commons/math4/core/jdkmath/AccurateMathCalc.java:100  

<b>Code</b>        : 
```java
private static void buildSinCosTables(double[] SINE_TABLE_A, double[] SINE_TABLE_B,
                                          double[] COSINE_TABLE_A, double[] COSINE_TABLE_B,
                                          int SINE_TABLE_LEN,double[] TANGENT_TABLE_A, double[] TANGENT_TABLE_B)
```
                                          
<b>PMD error</b>   : ```"FormalParameterNamingConventions:	The method parameter name 'SINE_TABLE_A' doesn't match '[a-z][a-zA-Z0-9]*'"```  

<b>Solving</b>     : 
```java
private static void buildSinCosTables(double[] sineTableA, double[] sineTableB,  
                                          double[] cosineTableA, double[] cosineTableB,  
                                          int sineTableA, double[] tangentTableA, double[] tangentTableB)  
```
PMD is telling us that the formal parameter "double[] SINE_TABLE_A" does not match the naming convention. Indeed if we, check for other formal parameter of type double[] in the code base they are all named after the regex [a-z][a-zA-Z0-9]* (exemple : static double slowCos(final double x, final double[] result))  
 

