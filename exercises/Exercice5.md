# Cyclomatic Complexity with JavaParser

With the help of JavaParser implement a program that computes the Cyclomatic Complexity (CC) of all methods in a given Java project. The program should take as input the path to the source code of the project. It should produce a report in the format of your choice (TXT, CSV, Markdown, HTML, etc.) containing a table showing for each method: the package and name of the declaring class, the name of the method, the types of the parameters and the value of CC.
Your application should also produce a histogram showing the distribution of CC values in the project. Compare the histogram of two or more projects.

Include in this repository the code of your application. Remove all unnecessary files like compiled binaries. Do include the reports and plots you obtained from different projects. See the [instructions](../sujet.md) for suggestions on the projects to use.

You may use [javaparser-starter](../code/javaparser-starter) as a starting point.

## Answer

You can find the code (with comments) to answer to this exercise in the code repertory. To calculate the cyclomatic complexity easily, we used the [Mc Cabe method](https://www.theserverside.com/feature/How-to-calculate-McCabe-cyclomatic-complexity-in-Java).

We tested our code by using the three examples in the [textbook by Oscar Luis Vera-Pérez](https://oscarlvp.github.io/vandv-classes/) (Listing 12, 13 and 15). The three examples return the right result. You can also find the code in the code/example/EX5 repository.

We obtained the reports (present in code/exercise5/reports) from the examples and the 3 projects (commons-collection, commons-cli and malo-project) by executing the commands :
```mvn install```
```java -jar <path to javaparser-starter-1.0-jar-with-dependencies.jar> <path to the java project>```

You can also view the associated histograms (made thanks to Python and its matplotlib.pyplot module) in code/exercise5/histograms for the 3 projects. Let's compare them by observing the histograms and the output of this program :

``` text
=== MaloProject ===
Number of classes with a given CC value {1: 206, 2: 30, 5: 13, 8: 2, 4: 7, 7: 2, 3: 21, 14: 1, 10: 3, 6: 2, 9: 1, 13: 1}
Number of classes with a CC value equal to 1: 206 (= 71.3 %)
Number of classes with a CC value equal to 2: 30 (= 10.4 %)
Number of classes with a CC value equal to 3: 21 (= 7.3 %)

=== CommonsCollections ===
Number of classes with a given CC value {1: 2251, 2: 518, 6: 27, 3: 187, 5: 37, 4: 92, 7: 11, 12: 7, 11: 4, 17: 2, 8: 10, 9: 8, 10: 9, 14: 1, 13: 4, 19: 2, 15: 2, 25: 1, 29: 1, 16: 1, 27: 1}
Number of classes with a CC value equal to 1: 2251 (= 70.9 %)
Number of classes with a CC value equal to 2: 518 (= 16.3 %)
Number of classes with a CC value equal to 3: 187 (= 5.9 %)

=== CommonsCli ===
Number of classes with a given CC value {1: 591, 3: 27, 2: 45, 5: 8, 4: 15, 7: 5, 6: 6, 12: 5, 13: 1, 8: 1, 9: 1, 19: 1, 14: 1, 10: 2, 15: 1, 11: 3}
Number of classes with a CC value equal to 1: 591 (= 82.9 %)
Number of classes with a CC value equal to 2: 45 (= 6.3 %)
Number of classes with a CC value equal to 3: 27 (= 3.8 %)
```

- In MaloProject, the majority of the methods (over 71%) have a CC of 1, indicating that most methods are simple and straightforward. However, the presence of methods with higher CC values (up to 14) there are certain areas that may need refactoring for better complexity management.

- In CommonsCollections project, it is similar to MaloProject. In fact, CommonsCollections has a around 70% of its methods with a CC of 1, indicating good design practices. However, with a greater total number of methods, the sheer volume of methods with CC values of 2 and 3 shows a more complex structure overall. This suggests that the project may contain more diverse functionalities, which might require more thorough testing.

- CommonsCli has the highest percentage of methods with a CC of 1 with 82.9%. So it suggests a very well-structured and maintainable codebase. It also has a lower occurrence of methods with higher CC values so complexity is well-managed. It is probable that this project is the easiest to understand and maintain.

    - In conclusion, all the projects have a significant majority of methods with CC = 1, which is a positive indicator of code maintainability. However, the presence of higher CC values points to specific areas where complexity could be improved.