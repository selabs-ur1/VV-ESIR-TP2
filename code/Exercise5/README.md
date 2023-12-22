# Cyclomatic Complexity
Pour calculer la complexité cyclomatique d'une méthode on compte le nombre de conditionnelles (if, for, foreach, while) et on ajoute 1. Pour cela on crée un programme s'inspirant du JavaParser afin de parcourir l'ast du programme d'entrée. On génère un rapport et un histogramme.

On compare la complexité cyclomatique des deux projets Apache commons-collections et commons-cli. On remarque sur les histogrammes que commons-cli a la plus grande partie de ses méthodes avec une CC égale à 1 tandis que commons-cli a beaucoup de méthode avec une CC entre 1 et 5.
