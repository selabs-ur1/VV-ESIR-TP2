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


---
**Student**  
<table align="left">
<tr>
  <th> First Name </th>
  <th> Last Name </th>
  <th> @Mail adress </th>
</tr>
<tr>
  <td> Léo </td>
  <td> Thuillier </td>
  <td> leo.thuillier@etudiant.univ-rennes1.fr </td>
</tr>
<tr>
  <td> Thibaut </td>
  <td> Le Marre </td>
  <td> thibaut.le-marre@etudiant.univ-rennes1.fr </td>
</tr>
</table>
<br><br><br><br><br><br>

---


```
//ifStatement//ifStatement//ifStatement
```
