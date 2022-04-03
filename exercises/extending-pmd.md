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

Here is the XML description of our rule :

<rule  name="3IfDetection"
  message="3 or more nested If detected"
  class="net.sourceforge.pmd.lang.rule.XPathRule">
  <description>
  Detect if there is 3 or more nested if.
  </description>
  <properties>
    <property name="xpath">
    <value>
<![CDATA[
.//IfStatement[.//IfStatement[.//IfStatement]]
]]>
    </value>
    </property>
  </properties>
  <priority>3</priority>
  <example>
<![CDATA[
public class foo {
    public void testIf(){

        if(true){

            if(true){
                if(true){
        }}}

        if(true){
                if(true){
                    if(true){
                        if(true){
                            if(true){
        }}}}}

    }
}
]]>
  </example>
</rule>

We runned our rule on the 4 different projects and it returned null each time which mean there's no 3 or more nested if in these projects.