# Cyclomatic Complexity with JavaParser

With the help of JavaParser implement a program that computes the Cyclomatic Complexity (CC) of all methods in a given Java project. The program should take as input the path to the source code of the project. It should produce a report in the format of your choice (TXT, CSV, Markdown, HTML, etc.) containing a table showing for each method: the package and name of the declaring class, the name of the method, the types of the parameters and the value of CC.
Your application should also produce a histogram showing the distribution of CC values in the project. Compare the histogram of two or more projects.


Include in this repository the code of your application. Remove all unnecessary files like compiled binaries. Do include the reports and plots you obtained from different projects. See the [instructions](../sujet.md) for suggestions on the projects to use.

You may use [javaparser-starter](../code/javaparser-starter) as a starting point.

#Answer 

Après avoir mis en place l’évaluation de la complexité cyclomatique, j’ai échangé avec mes camarades au sujet des appels de méthode pour savoir si ceux-ci faisaient partie de l’équation. Nous en avons conclu que la complexité cyclomatique ne permet pas de rendre compte de cette caractéristique. Ainsi, si un programme récursif vient à être testé, sa complexité cyclomatique ne représentera pas forcément bien son schéma d’exécution réel. C’est à prendre en compte à part dans l'évaluation de la complexité d’un programme.
Voici la classe de test que j’ai utilisé : 

```
package fr.istic.vv;


public class Foo {
   private int a;
   private int b;
   private int c;


   public Foo(int a, int b) {
       this.a = a;
       this.b = b;
   }


   public int sum() {
       return a + b;
   }


   public int getC(){
       return c;
   }


   //do a bad cyclomatic method
   public int badCyclomaticMethod(int a, int b, int c){
       if(a > 0){
           if(b > 0){
               if(c > 0){
                   return a + b + c;
               }
           }
       }
       return 0;
   }


   //do a good cyclomatic method
   public int goodCyclomaticMethod(int a, int b, int c){
       if(a > 0 && b > 0 && c > 0){
           return a + b + c;
       }
       return 0;
   }


   public int testing_ifs(int a, int b, int c){
       if(a > 0){
           a = a + 1;
       }
       if(b > 0){
           b = b + 1;
       }
       if(c > 0){
           c = c + 1;
       }
   }
}
```

Voici les valeurs de retour que j’ai obtenu : 
Cyclomatic Complexity Report:
Package: fr.istic.vv
Class: Foo
Method: sum
Parameters: N/A
CC: 1

Package: fr.istic.vv
Class: Foo
Method: getC
Parameters: N/A
CC: 1

Package: fr.istic.vv
Class: Foo
Method: badCyclomaticMethod
Parameters: N/A
CC: 4

Package: fr.istic.vv
Class: Foo
Method: goodCyclomaticMethod
Parameters: N/A
CC: 2

Package: fr.istic.vv
Class: Foo
Method: testing_ifs
Parameters: N/A
CC: 4

