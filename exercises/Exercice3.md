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

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](./designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer

After adding the following expression in XPath expressions on pmd designer
```code
//IfStatement[descendant::IfStatement[descendant::IfStatement]]
```
We tested the rule inside of pmd designer to prove that it works, even for loops between the ifs.

Then we created a file called rulesets in which we defined a ruleset xml code <a href="https://github.com/salahbdg/VV-ESIR-TP2/blob/xxx/code/Exercise3/README.md">HERE</a> 

To check our rule commons-math library, we executed the following command
```code
pmd check -f text -R /private/student/e/ue/sboudguigue/Téléchargements/pmd-bin-7.5.0/rulesets/test_ifs.xml -d ./src

```

> [!WARNING]
./src/userguide/java/org/apache/commons/math4/userguide/genetics/Polygon.java:92:	DontUseThreeNestedIfs:	Avoid using three or more nested if statements.

which refers to the following code block

```java
    public Polygon mutate(float mutationRate, float mutationAmount) {
        Polygon mutated = new Polygon();
        int size = data.length;
        mutated.data = new float[size];
        for (int i = 0; i < size; i++) {
            float val = this.data[i];
            if (GeneticAlgorithm.getRandomGenerator().nextFloat() < mutationRate) {
                val += GeneticAlgorithm.getRandomGenerator().nextFloat() * mutationAmount * 2 - mutationAmount;

                if (val < 0f) {
                    val = 0f;
                } else if (val > 1f) {
                    val = 1f;
                }
            }
            mutated.data[i] = val;
        }
        return mutated;

```
