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

```xml
<?xml version="1.0"?>
<ruleset name="Custom ruleset"
  xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.net/ruleset_2_0_0.xsd">
  <description>Ruleset for detecting complex code with nested if statements</description>
  <rule name="AvoidDeeplyNestedIfStmts"
    message="Avoid deeply nested if statements"
    class="net.sourceforge.pmd.lang.rule.XPathRule"
    language="java">
    <description>
      This rule detects when if statements are nested 3 levels or more.
    </description>
    <priority>3</priority>
    <properties>
      <property name="xpath">
        <value>
          <![CDATA[
            //IfStatement[count(ancestor::IfStatement) >= 2]
          ]]>
        </value>
      </property>
    </properties>
  </rule>
</ruleset>
```


Dans l'extrait de code XML, nous avons définit une nouvelle règle PMD nommée "AvoidDeeplyNestedIfStmts" pour repérer la présence de trois instructions if ou plus imbriquées dans le code Java. L'expression XPath //IfStatement[count(ancestor::IfStatement) >= 2] est spécifiée afin d'identifier ces configurations.
