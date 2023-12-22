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

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-ISTIC-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer

On a utilisé une nouvelle règle permettant de détecter les if imbriqués, on a détecter si il y a 3 ou plus if imbriqués.

En l'utilisant sur le projet Apache Commons Maths, on constate qu’il y a plusieurs if imbriqués à 3 if ou plus, cependant ces if sont utilisés car dans le contexte il est pertinent d’en utiliser plus de deux imbriqués au vu de la complexité de la logique.

```xml
<?xml version="1.0"?>
<ruleset name="Custom Rules"
         xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.net/ruleset_2_0_0.xsd">
    <description>
        Custom ruleset for detecting complex code patterns
    </description>

    <rule name="AvoidDeeplyNestedIfStmts"
          language="java"
          message="Avoid using three or more nested 'if' statements"
          class="net.sourceforge.pmd.lang.rule.XPathRule">
        <description>
            This rule detects deeply nested 'if' statements.
        </description>
        <priority>3</priority>
        <properties>
            <property name="xpath">
                <value>
                    <![CDATA[
                    //IfStatement[count(ancestor::IfStatement) >= 3]
                    ]]>
                </value>
            </property>
        </properties>
    </rule>
</ruleset>
```