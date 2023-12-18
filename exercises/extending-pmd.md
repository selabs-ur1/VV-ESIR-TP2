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

Notre règle XPath '//IfStatement//IfStatement//IfStatement' permet de sélectionner les IfStatement possèdant 2 ancêtres.

~~~xml
<rule name="Three_IF"
      language="java"
      message="Avoid using three if imbricated"
      class="net.sourceforge.pmd.lang.rule.XPathRule">
   <description>

   </description>
   <priority>3</priority>
   <properties>
      <property name="version" value="3.1"/>
      <property name="xpath">
         <value>
<![CDATA[
//IfStatement//IfStatement//IfStatement
]]>
         </value>
      </property>
   </properties>
</rule>
~~~

Après avoir lancé la commande pmd check avec notre ruleset contenant notre règle et le fichier a analysé, nous obtenons ceci:  
~~~shell
C:\Users\Axel\OneDrive\Documents\commons-math\commons-math-legacy-core\src\main\java\org\apache\commons\math4\legacy\core\dfp\DfpMath.java:605:        Three_IF:       Avoid using three if imbricated
C:\Users\Axel\OneDrive\Documents\commons-math\commons-math-legacy-core\src\main\java\org\apache\commons\math4\legacy\core\dfp\DfpMath.java:613:        Three_IF:       Avoid using three if imbricated
~~~
La règle est bien appliquée au fichier Java et renvoie le nom de la règle, le message de violation et la ligne où l'erreur a été detectée.
