
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
Here is an example of a warning. It is true that the outer parathesis are not useful in this context, they can be removed, so this warning can be identified as true positive.


> [!WARNING]
> ./src/userguide/java/org/apache/commons/math4/userguide/genetics/PolygonChromosome.java:106:	UselessParentheses:	Useless parentheses.

was identifying the following block of code

```java
return (1.0 - diff / (width * height * 3.0 * 256));
```


Here is another true positive, where PMD detected a missing @Override for a method run() that is overriden.

> [!WARNING]
> ./src/userguide/java/org/apache/commons/math4/userguide/genetics/ImageEvolutionExample.java:160:	MissingOverride:	The method 'run()' is missing an @Override annotation.

was identifying the following block of code

```java
public void startEvolution() {
            noStopRequested = true;
            Runnable r = new Runnable() {
                public void run() {
                    int evolution = 0;
                    while (noStopRequested) {
                        currentPopulation = ga.nextGeneration(currentPopulation);
```

Here we can see an example of a false positive where PMD detected that the class HelloWorldExample need to be final since it has only private constructors which is not the case, since this class has no constructors and
is just an entry point class used to test other classes.

> [!WARNING]
> ./src/userguide/java/org/apache/commons/math4/userguide/genetics/HelloWorldExample.java:168:	ClassWithOnlyPrivateConstructorsShouldBeFinal:	This class has only private constructors and may be final

was identifying the following block of code

```java
public class HelloWorldExample {
    public static final int    POPULATION_SIZE   = 1000;
    public static final double CROSSOVER_RATE    = 0.9;
    public static final double MUTATION_RATE     = 0.03;
    public static final double ELITISM_RATE      = 0.1;
    public static final int    TOURNAMENT_ARITY  = 2;

    public static final String TARGET_STRING = "Hello World!";
    public static final int DIMENSION = TARGET_STRING.length();

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ...
```    

