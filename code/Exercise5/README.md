# Code for the TCC javaparser

This project's purpose is to iterate througth another project repository and compute the Tight Class Cohesion of each class.

It creates a csv file containing 3 colums : "class","package","TCC". I have used this file to create histograms using python. 
```python
df = pd.read_csv("TCCReport.csv")
df=df.sort_values("TCC")
sb.barplot(data=df, x="class",y="TCC",color="Black")
```

The two histograms that I have created correspond to the TCC values of the *common-collection* repository and the *common-lang* repository.

Along with the creation of the two histograms, I have created a folder *GraphFolder/* in the project repository.This folder contains all the .dot files corresponding to each class of the project analyzed. Each file describes the dependency graph of the class that was analyzed.

With graphViz installed the graph can be seen in a svg file with the command:

```Batchfile
dot -Tsvg input.dot > output.svg
```

One example of this can be seen with the test.svg file contained in the GraphFolder along with the .dot files.
