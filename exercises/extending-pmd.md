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

We have made a custom PMD rule using Xpath
```xml
<?xml version="1.0"?>
<ruleset>
	<rule name="ThreeNestedFor"
		  language="java"
		  message="At most 2 nested FOR loop"
		  class="net.sourceforge.pmd.lang.rule.XPathRule">
	   <description>

	   </description>
	   <priority>1</priority>
	   <properties>
		  <property name="version" value="2.0"/>
		  <property name="xpath">
		     <value>
	<![CDATA[
	//ForStatement/*/*/*/*/ForStatement/*/*/*/*/ForStatement
	]]>
		     </value>
		  </property>
	   </properties>
	</rule>
</ruleset>
```

Using it with PMD on Apache commons math gives us :

```
/commons-math-master/commons-math-legacy/src/test/java/org/apache/commons/math4/legacy/analysis/interpolation/TricubicInterpolatorTest.java:200 : ThreeNestedFor:	At most 2 nested FOR loop
```

If we check the <b>TricubicInterpolatorTest.java</b> file at line 200

```java
        for (int i = 0; i < xval.length; i++) {
            for (int j = 0; j < yval.length; j++) {
                for (int k = 0; k < zval.length; k++) {
                    fval[i][j][k] = f.value(xval[i], yval[j], zval[k]);
                }
            }
        }
```

We have successfully detected a 3 nested loop patern in a code base.
