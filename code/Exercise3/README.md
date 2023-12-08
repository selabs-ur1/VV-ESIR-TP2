# Code of your exercise

règle pour vérifier qu'il n'y ai pas 3 if imbriqués peut importe en ignorant les blocks.

<?xml version="1.0" encoding="UTF-8"?>
<ruleset name="ruleIf"
	xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd">
        <description>regle qui avertit quand au moins 3 if sont imbriques</description>
	<rule name="if statement"
	      language="java"
	      message=""
	      class="net.sourceforge.pmd.lang.rule.XPathRule">
	   <description>

	   </description>
	   <priority>3</priority>
	   <properties>
	      <property name="version" value="3.1"/>
	      <property name="xpath">
		 <value>
	<![CDATA[
	//IfStatement
	//IfStatement
	//IfStatement
	]]>
		 </value>
	      </property>
	   </properties>
	</rule>

</ruleset>
