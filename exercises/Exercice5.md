# Cyclomatic Complexity with JavaParser

With the help of JavaParser implement a program that computes the Cyclomatic Complexity (CC) of all methods in a given Java project. The program should take as input the path to the source code of the project. It should produce a report in the format of your choice (TXT, CSV, Markdown, HTML, etc.) containing a table showing for each method: the package and name of the declaring class, the name of the method, the types of the parameters and the value of CC.
Your application should also produce a histogram showing the distribution of CC values in the project. Compare the histogram of two or more projects.

Include in this repository the code of your application. Remove all unnecessary files like compiled binaries. Do include the reports and plots you obtained from different projects. See the [instructions](../sujet.md) for suggestions on the projects to use.

You may use [javaparser-starter](../code/javaparser-starter) as a starting point.

## Answer

TODO : commentaires dans code + commentaires sur histogramme 

You can find the code (with comments) to answer to this exercise in the code repertory. To calculate the cyclomatic complexity easily, we used the [Mc Cabe method](https://www.theserverside.com/feature/How-to-calculate-McCabe-cyclomatic-complexity-in-Java).

We obtained the reports (present in code/exercise5/reports) by executing the commands :
```mvn install```
```java -jar <path to javaparser-starter-1.0-jar-with-dependencies.jar> <path to the java project>```

You can also view the associated histograms in code/exercise5/histograms for 3 projects : commons-collection, commons-cli and malo-project. DO COMMENTS!!!!
