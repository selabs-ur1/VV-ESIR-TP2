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

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](./designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer

We have created the following example to illustrate how the rule works.

```java
class Example {

    public testIf() {
        if (x == 0) {
            if (y == 0) {
                if (a==0) {
                    // ...
                }
            }
            else if (a==0) {
                // ...
            }
            else {
                // ...
            }
        }
    }
}
```

This code shows three levels of if condition nesting, which can make the code harder to read and to maintain. This type of structure is exactly what our PMD rule should detect once written.

Here is the rule definition we have made in the form of a ruleset file. We have used this [website](https://www.w3schools.com/xml/xpath_syntax.asp) as a guide:

```xml
<?xml version="1.0"?>

<ruleset name="Custom Rules"
         xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd">

    <description>
        Custom rules to detect overly complex code with excessive nested if-statements.
    </description>

    <rule name="ExcessiveNestedIfs"
          language="java"
          message="Too many nested if-statements, consider refactoring."
          class="net.sourceforge.pmd.lang.rule.xpath.XPathRule">
        
        <description>
            This rule detects if-statements nested three or more levels deep, which can make the code harder to understand and maintain.
        </description>
        
        <priority>3</priority>
        
        <properties>
            <property name="xpath">
                <value>
                    <![CDATA[
                    //IfStatement/Block//IfStatement/Block//IfStatement
                    ]]>
                </value>
            </property>
        </properties>
    </rule>

</ruleset>
```

Here are the main XPath elements used in our rule :
- ```//```: Selects nodes at any level of the hierarchy starting from the current node.
- ``IfStatement``: Selects all if statements in the syntax tree.
- Block: Selects a block of code (a set of statements enclosed in {}).

So our ``//IfStatement/Block//IfStatement/Block//IfStatement`` rule can be broken down as follows:

- ``//IfStatement``: Selects all if statements in the source code. This can include if statements at different nesting levels.

- ``/Block``: Selects the block of code that follows the if statement. A block may contain several other statements, including loops, assignments and even other if statements.

- ``//IfStatement``: Selects a second level of if statements nested in the first block, i.e. if an "if" is a descendant/child of another "if".

and we repeat it for the third one.

We have therefore written an XPath rule that identifies cases where three or more if statements are nested in Java code, and alerts the developer to improve the readability of the code. 