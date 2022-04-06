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

### Rule defined

The complete code of the rule definition and the test results can be found in the project source codes.

```
    <!-- RULES -->
    <!-- RULE OF THE EXERCICE 3 ADDED BY US -->
    <rule name=""
      language="java"
      message=""
      class="net.sourceforge.pmd.lang.rule.XPathRule"
    >
        <description>

        </description>
        <priority>3</priority>
        <properties>
            <property name="New property" type="String" value="TODO" description="TODO"/>
            <property name="version" value="2.0"/>
            <property name="xpath">
                <value>
                    <![CDATA[
                        //IfStatement//IfStatement//IfStatement
                    ]]>
                </value>
            </property>
        </properties>
    </rule>
 ```

### Test results

We tested on the commons-cli-master project and found an if nested in the following class:
**\src\main\java\org\apache\commons\cli\DefaultParser.java**, line **436**.


We also found false positives like on the following line:
**\src\main\java\org\apache\commons\cli\CommandLine.java**, line **210**.

There is an else if of level 2 which has been signaled by our rule.

 
