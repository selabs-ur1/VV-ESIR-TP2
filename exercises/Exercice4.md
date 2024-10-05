# No getter!

With the help of JavaParser implement a program that obtains the private fields of public classes that have no public getter in a Java project. 

A field has a public getter if, in the same class, there is a public method that simply returns the value of the field and whose name is `get<name-of-the-field>`.

For example, in the following class:

```Java

class Person {
    private int age;
    private String name;
    
    public String getName() { return name; }

    public boolean isAdult() {
        return age > 17;
    }
}
```

`name` has a public getter, while `age` doesn't.

The program should take as input the path to the source code of the project. It should produce a report in the format of your choice (TXT, CSV, Markdown, HTML, etc.) that lists for each detected field: its name, the name of the declaring class and the package of the declaring class.

Include in this repository the code of your application. Remove all unnecessary files like compiled binaries. See the [instructions](../sujet.md) for suggestions on the projects to use.

*Disclaimer* In a real project not all fields need to be accessed with a public getter.

## Answer

You can find the code (with comments) to answer to this exercise in the code folder.

We obtained the reports (present in code/exercise4/reports) by executing the commands :
```mvn install```
```java -jar <path to javaparser-starter-1.0-jar-with-dependencies.jar> <path to the java project>```


The report will have this layout.
<Class> <Attribute> : <Is there a getter for it?> 
```
Optional[me.astri.idleBot.GameBot.entities.equipment.Equipment] currentWeight : false
Optional[me.astri.idleBot.GameBot.entities.equipment.Equipment] booster : true
```

If we count, Malo has :
56 where no getter is associated to an attribute out of 105 attributes.

But as mentioned, not every attribute needs a getter in the real world, so this metric is kind of useless in order to find bugs. However, it is useful to find all the attributes that can be tested with unit tests.