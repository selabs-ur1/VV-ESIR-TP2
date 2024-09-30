
# Using PMD


Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.


## Answer

Command:
pmd check -f text -R rulesets/java/quickstart.xml -d . -r report

Issue:
1	./commons-collections/src/test/java/org/apache/commons/collections4/TestUtils.java	67	Ensure that resources like this ObjectInputStream object are closed after use

from:
  final ObjectInputStream ois = new ObjectInputStream(is);
  final Object object = ois.readObject();
  ois.close();

to:
  final ObjectInputStream ois = new ObjectInputStream(is);
  try {
    final Object object = ois.readObject();
  } catch(Error e) {
    throw new Exception(e);
  } finally {
    ois.close();
  }

Issue:
2	./commons-collections/src/test/java/org/apache/commons/collections4/MapUtilsTest.java	154	The final local variable name 'EXPECTED_OUT' doesn't match '[a-z][a-zA-Z0-9]*'

Cette erreur mentionne un caractère non compatible ('_') avec l'expression régulière. Une erreur de syntaxe n'impactera pas le fonctionnement du code et si le développeur garde la meme forme pour toute ces variables alors c'est acceptable.
