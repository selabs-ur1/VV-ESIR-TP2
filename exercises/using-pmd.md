# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer
run.sh pmd -d ../VV-ESIR-TP2/code/javaparser-starter/ -R rulesets/java/quickstart.xml
mars 25, 2022 12:04:59 PM net.sourceforge.pmd.PMD encourageToUseIncrementalAnalysis
AVERTISSEMENT: This analysis could be faster, please consider using Incremental Analysis: https://pmd.github.io/pmd-6.43.0/pmd_userdocs_incremental_analysis.html
/private/student/n/un/blebrun/VV/TP2/VV-ESIR-TP2/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:3:	UnnecessaryImport:	Unused import 'com.github.javaparser.Problem'
/private/student/n/un/blebrun/VV/TP2/VV-ESIR-TP2/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:4:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.CompilationUnit'
/private/student/n/un/blebrun/VV/TP2/VV-ESIR-TP2/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:5:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.body.ClassOrInterfaceDeclaration'
/private/student/n/un/blebrun/VV/TP2/VV-ESIR-TP2/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:6:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.body.MethodDeclaration'
/private/student/n/un/blebrun/VV/TP2/VV-ESIR-TP2/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:7:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.visitor.VoidVisitor'
/private/student/n/un/blebrun/VV/TP2/VV-ESIR-TP2/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:8:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.visitor.VoidVisitorAdapter'
/private/student/n/un/blebrun/VV/TP2/VV-ESIR-TP2/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:13:	UnnecessaryImport:	Unused import 'java.nio.file.Path'
/private/student/n/un/blebrun/VV/TP2/VV-ESIR-TP2/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:14:	UnnecessaryImport:	Unused import 'java.nio.file.Paths'
/private/student/n/un/blebrun/VV/TP2/VV-ESIR-TP2/code/javaparser-starter/src/main/java/fr/istic/vv/Main.java:16:	UseUtilityClass:	All methods are static.  Consider using a utility class instead. Alternatively, you could add a private constructor or make the class abstract to silence this warning.
/private/student/n/un/blebrun/VV/TP2/VV-ESIR-TP2/code/javaparser-starter/src/main/java/fr/istic/vv/PublicElementsPrinter.java:4:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.body.*'
/private/student/n/un/blebrun/VV/TP2/VV-ESIR-TP2/code/javaparser-starter/src/main/java/fr/istic/vv/PublicElementsPrinter.java:20:	ControlStatementBraces:	This statement should have braces
/private/student/n/un/blebrun/VV/TP2/VV-ESIR-TP2/code/javaparser-starter/src/main/java/fr/istic/vv/PublicElementsPrinter.java:28:	ControlStatementBraces:	This statement should have braces
/private/student/n/un/blebrun/VV/TP2/VV-ESIR-TP2/code/javaparser-starter/src/main/java/fr/istic/vv/PublicElementsPrinter.java:44:	ControlStatementBraces:	This statement should have braces


I use pmd on the javaparser project.
There is a true positive error in this project, like "UseUtilityClass:	All methods are static.  Consider using a utility class instead. Alternatively, you could add a private constructor or make the class abstract to silence this warning." If we decide to develop more this application, it's possible to have a problem later if we don't solve this  error.

For an false positive error, there is "ControlStatementBraces". that asking to make braces for the command. It's not an error which need a correction. There may have many reasons to don't solve this error, it can be the checkstyle which ask this syntaxe.