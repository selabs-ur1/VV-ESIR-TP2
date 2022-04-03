# Code of your exercise

Our first version of the <value> was ```/IfStatement/Statement/Block/BlockStatement/Statement/IfStatement/Statement/Block/BlockStatement/Statement/IfStatement``` because we didn't know how to create a rule which would find all the ifs wherever they were in the "nested chain".
So it couldn't work with not direct children like when one if is inside a "for" loop or any other statement. So we modified it to ```/IfStatement//IfStatement//IfStatement``` and now it's possible to catch them all.

```xml
<?xml version="1.0"?>

<ruleset name="Custom Rules"
    xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd">

    <description>
        nested if
    </description>


    <rule name="nested if"
      language="java"
      message=""
      class="net.sourceforge.pmd.lang.rule.XPathRule">
   <description>

   </description>
   <priority>3</priority>
   <properties>
      <property name="version" value="2.0"/>
      <property name="xpath">
         <value>
<![CDATA[
/IfStatement//IfStatement//IfStatement
]]>
         </value>
      </property>
   </properties>
</rule>

</ruleset>
```

