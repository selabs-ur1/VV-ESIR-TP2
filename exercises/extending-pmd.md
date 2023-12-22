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
On télécharge pmd designer et on teste notre règle sur du code source Java.
Voici la règle XML : 
```xml
<rule name="ThreeNestedIf"
      language="java"
      message="MoreThanTwoNestedIf"
      class="net.sourceforge.pmd.lang.rule.XPathRule">
   <description>

   </description>
   <priority>3</priority>
   <properties>
      <property name="version" value="3.1"/>
      <property name="xpath">
         <value>
<![CDATA[
 //IfStatement[descendant::IfStatement[descendant::IfStatement]]
]]>
         </value>
      </property>
   </properties>
</rule>

```
La règle Xpath est la suivante : ```//IfStatement[descendant::IfStatement[descendant::IfStatement]]```
Elle permet de vérifier à partir de n'importe quel If, s'il n'y a pas deux If imbriqués parmi ces descendants. 

On applique cette règle lors de l'analyse statique pmd sur le projet apache collections par cette commande (on a crée notre ruleset.xml contenant notre règle auparavant):

```bash
pmd.bat check -d path/du/projet -R path/du/ruleset.xml > rapport2.txt
```
On redirige la sortie standard vers un fichier txt, on le lit et on obtient notamment cette ligne : 

 ->  MapUtils.java:226:	3IF:	MoreThanTwoNestedIf

Cela correspond à cet endroit dans le code :
```java
 public static <K> Boolean getBoolean(final Map<? super K, ?> map, final K key) {
        if (map != null) {
            final Object answer = map.get(key);
            if (answer != null) {
                if (answer instanceof Boolean) {
                    return (Boolean) answer;
                }
                if (answer instanceof String) {
                    return Boolean.valueOf((String) answer);
                }
                if (answer instanceof Number) {
                    final Number n = (Number) answer;
                    return n.intValue() != 0 ? Boolean.TRUE : Boolean.FALSE;
                }
            }
        }
        return null;
    }
```
La ligne 226 correspond au premier if, donc la règle a bien reconnue le noeud problématique contenant deux if imbriqués
