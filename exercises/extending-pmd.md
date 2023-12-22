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


Voici ma règle XML :

```xml
<rule name="No3If"
      language="java"
      message="There is at least 3 nested Ifs !"
      class="net.sourceforge.pmd.lang.rule.XPathRule">
   <description>

   </description>
   <priority>3</priority>
   <properties>
      <property name="version" value="3.1"/>
      <property name="xpath">
         <value>
            <![CDATA[
            //IfStatement[.//IfStatement[.//IfStatement]]
            ]]>
         </value>
      </property>
   </properties>
</rule>
```

Cette règle vérifie s'il y a plus de 3 if imbriqués.
Cette règle est utile car s'il y a plus de 3 if imbriqués, c'est parce qu'il faut utiliser un switch.

J'ai utilisé cette règle sur la Collection Java avec la commande "check" de pmd.
Le résultat obtenu était un peu troublant au départ, car PMD indique des erreurs là où il y a plusieurs "if{} else if{}".
Mais après y avoir un peu réflechi, c'est bel et bien une forme de if imbriqués. 
Puisque le "if" juste après le "else" est considéré comme un "if" dans un autre "if".

La règle indique également la bonne quantité de "if" qui ne respecte pas la règle d'imbrication.
En effet, elle n'indique pas les deux derniers "if".
