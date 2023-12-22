# Code of your exercise

Voilà le .xml obtenu en exportant :

```
<rule name="ifpuissance3"
      language="java"
      message="there's three nested if"
      class="net.sourceforge.pmd.lang.rule.XPathRule">
  <description>
    promote nested if to avoid them
  </description>
  <priority>1</priority>
  <properties>
    <property name="version" value="3.1"/>
    <property name="xpath">
      <value>
        <![CDATA[
//IfStatement[count(ancestor::IfStatement) + 1 >= 2]

]]>
      </value>
    </property>
  </properties>
</rule>
```
