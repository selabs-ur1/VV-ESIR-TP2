# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false negative). Explain why you would not solve this issue.

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
lthuillier@e206m03:~$ run.sh pmd -d Documents/VV-ESIR-TP2-main/code/javaparser-starter/ -f text -R rulesets/java/quickstart.xml
mars 25, 2022 11:35:36 AM net.sourceforge.pmd.PMD encourageToUseIncrementalAnalysis
AVERTISSEMENT: This analysis could be faster, please consider using Incremental Analysis: https://pmd.github.io/pmd-6.43.0/pmd_userdocs_incremental_analysis.html
/private/student/r/er/lthuillier/Documents/VV-ESIR-TP2-main/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:3:	UnnecessaryImport:	Unused import 'com.github.javaparser.Problem'
/private/student/r/er/lthuillier/Documents/VV-ESIR-TP2-main/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:4:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.CompilationUnit'
/private/student/r/er/lthuillier/Documents/VV-ESIR-TP2-main/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:5:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.body.ClassOrInterfaceDeclaration'
/private/student/r/er/lthuillier/Documents/VV-ESIR-TP2-main/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:6:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.body.MethodDeclaration'
/private/student/r/er/lthuillier/Documents/VV-ESIR-TP2-main/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:7:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.visitor.VoidVisitor'
/private/student/r/er/lthuillier/Documents/VV-ESIR-TP2-main/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:8:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.visitor.VoidVisitorAdapter'
/private/student/r/er/lthuillier/Documents/VV-ESIR-TP2-main/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:13:	UnnecessaryImport:	Unused import 'java.nio.file.Path'
/private/student/r/er/lthuillier/Documents/VV-ESIR-TP2-main/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:14:	UnnecessaryImport:	Unused import 'java.nio.file.Paths'
/private/student/r/er/lthuillier/Documents/VV-ESIR-TP2-main/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:16:	UseUtilityClass:	All methods are static.  Consider using a utility class instead. Alternatively, you could add a private constructor or make the class abstract to silence this warning.
/private/student/r/er/lthuillier/Documents/VV-ESIR-TP2-main/code/javaparser-starter/src/main/java/fr/istic/vv/PublicElementsPrinter.java:4:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.body.*'
/private/student/r/er/lthuillier/Documents/VV-ESIR-TP2-main/code/javaparser-starter/src/main/java/fr/istic/vv/PublicElementsPrinter.java:20:	ControlStatementBraces:	This statement should have braces
/private/student/r/er/lthuillier/Documents/VV-ESIR-TP2-main/code/javaparser-starter/src/main/java/fr/istic/vv/PublicElementsPrinter.java:28:	ControlStatementBraces:	This statement should have braces
/private/student/r/er/lthuillier/Documents/VV-ESIR-TP2-main/code/javaparser-starter/src/main/java/fr/istic/vv/PublicElementsPrinter.java:44:	ControlStatementBraces:	This statement should have braces
```

True positive : the warning "UseUtilityClass might be important for us because this affect the structure of our program. Indeed, here all the methods are statics and since static methods belong to the class and not a particular instance, mocking them becomes difficult and dangerous.


False positive : Unused imports are not a big deal because this don't affect the code and don't have a bad effect on the utilisation of the program
