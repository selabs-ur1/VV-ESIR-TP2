# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

On a pris le projet common-maths, avec ceci s'ajoutent nombre d'erreurs données par PMD. Un "false positive" pourrait être une erreur de type "Useless parentheses", qui relève vraiment du détail. Ce n'est pas une source de bug mais de forme. Un "true positive" pourrait être un "Useless import", en cas d'import important, cela pourrait poser des problème et alourdir significativement et inutilement le fichier. Une autre possiblitié de "true positive" est "Switch statements should be exhaustive" indiquant que l'intégralité des cas n'est pas considéré. Cela peut être un gros problème si un autre cas que ceux traité apparait (même dans le cas ou on gère completemnt l'entré de la fonction). Pour régler le problème on peut donc ajouter "un case default:", ainsi tout les cas seront traité évitant une erreur de matching.
