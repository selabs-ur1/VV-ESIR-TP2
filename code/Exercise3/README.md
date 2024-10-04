# Code of your exercise

Put here all the code created for this exercise

In this ruleset xml we defined our rule that detects the presence of three nested ifs

```xml
<?xml version="1.0"?>

<ruleset name="Custom Rules"
    xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd">

    <description>
        My custom rules
    </description>


    <!-- Your rules will come here -->


    <rule name="DontUseThreeNestedIfs"
      language="java"
      message="Avoid using three or more nested if statements."
      class="net.sourceforge.pmd.lang.rule.xpath.XPathRule">
      
     	<description>
  		  Detect three ifs in a row
     	</description>
      <priority>3</priority>
     <properties>
      <property name="xpath">
         <value>
<![CDATA[

//IfStatement[descendant::IfStatement[descendant::IfStatement]]

]]>
         </value>
      </property>
   </properties>
	</rule>

</ruleset>
```
