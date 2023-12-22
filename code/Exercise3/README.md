Exercice3: extending pmd designer
- Après avoir installer Javafx et pmd designer , on crée une rule avec l’expression Xpath suivante :
//IfStatement[count(ancestor::IfStatement) >= 2 ] pour vérifier qu’un nœud if de l’ AST n’a pas plus de 2 ancêtres
- puis en ajouteant les autres proriétés au fichier xml, on génère la regle.






- la rule donne :
<ruleset name="Mon Ruleset Personnalisé"
xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 http://pmd.sourceforge.net/ruleset_2_0_0.xsd">



<description>
Mon ruleset personnalisé pour PMD.
</description>



<!-- Ajoutez vos règles personnalisées ici -->
<rule name="IfImbriquer"
language="java"
message="Une Imbrication de plus trois ifs!! "
class="net.sourceforge.pmd.lang.rule.XPathRule">
<priority>3</priority>
<properties>
<property name="version" value="3.1"/>
<property name="xpath">
<value>
<![CDATA[
//IfStatement[count(ancestor::IfStatement) >= 2 ]
]]>
</value>
</property>
</properties>
</rule>



</ruleset>



- On applique la cette règle sur le code java suivant :



public class IfNestingExample {



public static void main(String[] args) {
int a = 10;
int b = 20;
int c = 30;



if (a > 0) {
   if (b > 0) {
      if (c > 0) {
             System.out.println("Tous les nombres sont positifs.");
     } else {
          System.out.println("c est négatif.");
      }
    } else {
       System.out.println("b est négatif.");
      }
} else {
    System.out.println("a est négatif.");
  }
}
}



- pour le resultat voir l’image associée
