# No Getter

Notre programme relève les attributs privés des classes publiques ne disposant pas de getters. Pour cela on s'inspire du JavaParser et on parcourt l'ast du programme : on liste tous les attributs privés et tous les getters puis on compare. On produit un rapport donnant les attributs privés à afficher pour chaque classe de chaque fichier.
