# Code of your exercise

Dans cette partie, nous allons à nouveau utiliser le Java Parser. Cependant, cette fois, nous allons suivre une approche différente. Nous récupérerons toutes les méthodes, les parcourrons, et incrémenterons un compteur dédié à chaque instruction (if, while, etc.) au sein de chaque méthode. Chaque méthode aura ainsi son propre nombre d'instructions, permettant le calcul de la complexité cyclomatique.

Il peut être quelque peu difficile de déduire la complexité de notre programme à partir de ce seul résultat. Ainsi, nous aurions généré le rendu ainsi que des histogrammes exprimant en pourcentage de la complexité globale du projet (pour chaque méthode) . Cela aurait facilité l'identification des méthodes nécessitant une optimisation.
