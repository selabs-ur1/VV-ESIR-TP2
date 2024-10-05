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

We obtained the reports (present in code/exercise6/reports) from the examples and 4 projects (commons-collections, commons-cli, commons-lang and malo-project) by executing the same commands as before.

You can also view the associated histograms (made thanks to the same way as before) in code/exercise6/histograms for the four projects. Let's compare them by observing the histograms and the output of this program :

``` text
=== MaloProject ===
Number of classes with a TCC value of NaN: 14 (= 22.6%)

Number of classes with TCC between 0.0 and 0.05: 41 (= 85.4%)
Number of classes with TCC between 0.05 and 0.1: 3 (= 6.2%)
Number of classes with TCC between 0.1 and 0.2: 3 (= 6.2%)
Number of classes with TCC between 0.2 and 0.5: 1 (= 2.1%)
Number of classes with TCC between 0.5 and 1.0: 0 (= 0.0%)

Number of classes with a TCC value equal to 0: 37
Number of classes with a TCC value equal to 1: 0


=== CommonsCollections ===
Number of classes with a TCC value of NaN: 40 (= 11.6%)

Number of classes with TCC between 0.0 and 0.05: 306 (= 100.0%)
Number of classes with TCC between 0.05 and 0.1: 0 (= 0.0%)
Number of classes with TCC between 0.1 and 0.2: 0 (= 0.0%)
Number of classes with TCC between 0.2 and 0.5: 0 (= 0.0%)
Number of classes with TCC between 0.5 and 1.0: 0 (= 0.0%)

Number of classes with a TCC value equal to 0: 303
Number of classes with a TCC value equal to 1: 0

=== CommonsCli ===
Number of classes with a TCC value of NaN: 6 (= 22.2%)

Number of classes with TCC between 0.0 and 0.05: 21 (= 100.0%)
Number of classes with TCC between 0.05 and 0.1: 0 (= 0.0%)
Number of classes with TCC between 0.1 and 0.2: 0 (= 0.0%)
Number of classes with TCC between 0.2 and 0.5: 0 (= 0.0%)
Number of classes with TCC between 0.5 and 1.0: 0 (= 0.0%)

Number of classes with a TCC value equal to 0: 20
Number of classes with a TCC value equal to 1: 0


=== CommonsLang ===
Number of classes with a TCC value of NaN: 63 (= 24.6%)

Number of classes with TCC between 0.0 and 0.05: 180 (= 93.3%)
Number of classes with TCC between 0.05 and 0.1: 4 (= 2.1%)
Number of classes with TCC between 0.1 and 0.2: 4 (= 2.1%)
Number of classes with TCC between 0.2 and 0.5: 5 (= 2.6%)
Number of classes with TCC between 0.5 and 1.0: 0 (= 0.0%)

Number of classes with a TCC value equal to 0: 170
Number of classes with a TCC value equal to 1: 0
```

blabla