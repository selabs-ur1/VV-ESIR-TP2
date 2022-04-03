# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
    ...
    if (...) {
        ...
        if (...) {
            ....
        }
    }

}
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer

To see if our rules would work, we used this class:

``
class Foo{
    void foo(){
        for(int i=0; i<5; i++){
            if(true){
                if(true){
                    while(true){
                        if(true){
                            if(true){
                            }
                        }
                    }
                }
            }
        }
    }
}
``
in XPath, // means the descendant, so if we make a rule //IfStatement//IfStatement//IfStatement, it will try to find an If statement descendant of the root, then an other If statement but this time descendant of the first If, and then a third one with the same method.
