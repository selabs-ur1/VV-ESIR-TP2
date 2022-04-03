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

Our XML definition:

```Xml
<ruleset name="NestedIfRuleset"
	 xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd">
    <description>
        Ruleset test
    </description>
    <rule name="NestedIf" ref="category/java/design.xml/AvoidDeeplyNestedIfStmts">
	<properties>
	    <property name="problemDepth" value="3" />
	</properties>
     </rule>
</ruleset>
```

Commons-collections analyse: 

We found several patterns in the code:

```
C:\Users\lucas\OneDrive\Documents\ESIR2\MDI\TP2\commons-collections-master\commons-collections-master\src\main\java\org\apache\commons\collections4\MapUtils.java:230:  NestedIf:       Deeply nested if..then statements are hard to read
C:\Users\lucas\OneDrive\Documents\ESIR2\MDI\TP2\commons-collections-master\commons-collections-master\src\main\java\org\apache\commons\collections4\MapUtils.java:233:  NestedIf:       Deeply nested if..then statements are hard to read
C:\Users\lucas\OneDrive\Documents\ESIR2\MDI\TP2\commons-collections-master\commons-collections-master\src\main\java\org\apache\commons\collections4\MapUtils.java:236:  NestedIf:       Deeply nested if..then statements are hard to read
C:\Users\lucas\OneDrive\Documents\ESIR2\MDI\TP2\commons-collections-master\commons-collections-master\src\main\java\org\apache\commons\collections4\MapUtils.java:934:  NestedIf:       Deeply nested if..then statements are hard to read
C:\Users\lucas\OneDrive\Documents\ESIR2\MDI\TP2\commons-collections-master\commons-collections-master\src\main\java\org\apache\commons\collections4\MapUtils.java:937:  NestedIf:       Deeply nested if..then statements are hard to read
C:\Users\lucas\OneDrive\Documents\ESIR2\MDI\TP2\commons-collections-master\commons-collections-master\src\main\java\org\apache\commons\collections4\map\CompositeMap.java:142:  NestedIf:       Deeply nested if..then statements are hard to read
```

The first three are due to this function: 
```Java
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
We observed that there are three nested if statements three times. 

The last detected pattern is due to this function:
```Java
public synchronized void addComposited(final Map<K, V> map) throws IllegalArgumentException {
    if (map != null) {
        for (int i = composite.length - 1; i >= 0; --i) {
            final Collection<K> intersect = CollectionUtils.intersection(this.composite[i].keySet(), map.keySet());
            if (!intersect.isEmpty()) {
                if (this.mutator == null) {
                    throw new IllegalArgumentException("Key collision adding Map to CompositeMap");
                }
                this.mutator.resolveCollision(this, this.composite[i], map, intersect);
            }
        }
        final Map<K, V>[] temp = new Map[this.composite.length + 1];
        System.arraycopy(this.composite, 0, temp, 0, this.composite.length);
        temp[temp.length - 1] = map;
        this.composite = temp;
    }
}
```
Here we have seen that two of the three if statements are inside a for loop. XML rule works.
