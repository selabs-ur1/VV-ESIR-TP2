# Class cohesion with JavaParser

With the help of JavaParser implement a program that computes the Tight Class Cohesion (TCC) for each class in a given Java project. The program should take as input the path to the source code of the project. It should produce a report in the format of your choice (TXT, CSV, Markdown, HTML, etc.) containing a table showing for each class: the package, name and TCC value. 
Your application should also produce a histogram showing the distribution of CC values in the project. Compare the histogram of two or more projects.
Finally, your application should also produce the dependency graph of each class (cf. example [here](https://people.irisa.fr/Benoit.Combemale/pub/course/vv/vv-textbook-v0.1.pdf#cohesion-graph)). The graph should be written using the [GraphViz DOT format](https://www.graphviz.org/)

Ignore inherited members to compute TCC of a class.

Include in this repository the code of your application. Remove all unnecessary files like compiled binaries. Do include the reports and plots you obtained from different projects. See the [instructions](../sujet.md) for suggestions on the projects to use.

You may use [javaparser-starter](../code/javaparser-starter) as a starting point.

## Answer

You can find the code (with comments) to answer to this exercise in the code repertory.

We tested our code by using the two examples in the [textbook by Oscar Luis Vera-Pérez](https://oscarlvp.github.io/vandv-classes/) (Listing 16 and 18). The three examples return the right result. You can also find the code in the code/example/EX5 repository.

We obtained the reports (present in code/exercise6/reports) from the examples and ... projects (..., ..., ...).

You can also view the associated histograms (made thanks to Python and its matplotlib.pyplot module) in code/exercise6/histograms for the (...) projects. Let's compare them by observing the histograms and the output of this program :

``` text
```

blabla