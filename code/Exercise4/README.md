# exo 4

On observe que dans un fichier Maison.java. Dans ce dernier, l'attribut adresse n'a pas de getter. Cet attribut va nous permetre de tester  si notre fonction est bonne. C'est a dire si elle nous renvoie bien l'ensemble des attributs sans getter.
On observe que dans notre fichier Public ElementsPrinter.java on à ajouté:

```java
 public void attributs_that_dont_get_getter(){
        List<String> no_getter = new ArrayList<String>();
        for (int i =0; i<private_attributs.size();i++){
            String[] temp_attribut = private_attributs.get(i).split(" ");
                    boolean got_getter=false;
            for (int y=0;y<public_method.size();y++){
                String[] temp_methode=public_method.get(y).split(" ");
                if (temp_methode[2].startsWith("get")){
                    if ((temp_methode[2].substring(3,temp_methode[2].length()-2)).toLowerCase().equals(temp_attribut[2].toLowerCase().substring(0,temp_attribut[2].length()-1))){
                       got_getter=true;
                    }
                } else if (temp_methode[2].startsWith("is")) {
                    if ((temp_methode[2].substring(2,temp_methode[2].length()-2)).toLowerCase().equals(temp_attribut[2].toLowerCase().substring(0,temp_attribut[2].length()-1))){
                        got_getter=true;
                    }
                }
            }
            if(!got_getter){
            no_getter.add(private_attributs.get(i));}
        }
        System.out.println(no_getter);
        }
```
```java
        public void visit(FieldDeclaration declaration,Void arg){
            if(!declaration.isPrivate()) return;
            System.out.println("  " + declaration.toString());
            private_attributs.add(declaration.toString());
        }
    }
```
Pour lancer le programme il est important d'indiquer en paramètre de la fonction main, le chemin vers la classe à analyser.

L'idée derrière ces deux fonctions est de remplir deux listes : une liste public_method qui va contenir l'ensemble des méthodes publiques, ainsi qu'une liste private_attributs qui va accueillir l'ensemble des attributs privés (remplis lors de l'appel de Visit plus haut). Une fois que l'on a parcouru l'ensemble du fichier, nous comparons les noms des attributs et des méthodes. Si le nom de notre méthode est de type "is" ou "get" suivi d'un nom d'attribut, alors l'attribut a un getter. Si ce n'est pas le cas, nous l'ajoutons à la liste des attributs sans getter.

Critique : Cette méthode présente de nombreux problèmes et repose sur les bonnes pratiques de programmation. En effet, elle utilise les noms pour détecter les getters. Dans le cas où le getter ne possède pas un nom approprié, ou qu'une fonction ne remplit pas la fonction de getter mais possède un nom de getter, la méthode ne serait pas pertinente.
