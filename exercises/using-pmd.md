# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer
Nous avons décidé de travailler sur le projet GitHub Apache Commons Math et nous avons ensuite exécuté PMD sur ce code. Un erreur que nous jugeons importante dans le code est :  
Rule : AvoidFieldNameMatchingMethodName  
Violation : Field size has the same name as a method  
Ligne : 74  

En effet, cette erreur montre que le nom d’une des variables est le même qu’une méthode implémentée dans le code. Cela pourrait porter à confusion lors de l’utilisation de cette variable ou de cette méthode.  

Pour régler cette erreur nous pourrions juste renommer la variable (ou  la méthode) :  
private final int size; --> private final int sizeRange;

A l'inverse, une erreur que nous pensons moins grave :  

Rule : ConstructorCallsOverridableMethod  
Violation : Overridable method 'getRadixDigits' called during object construction  
Ligne : 345  

L’appel d’une fonction lors de la construction d’un objet n’est pas fondamentalement grave et n'a pas besoin d’être corrigé. Si nous devions corriger cette partie du code, nous serions dans l’obligation d’ajouter plusieurs lignes avant l’appel de construction ce qui, au final, complexifierait notre programme.