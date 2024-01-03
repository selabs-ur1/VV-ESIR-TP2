# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Le TCC est le ratio de paires noeuds directement connectés (PDC) sur le nombre total de paires des noeuds du graphe (PT) :
TCC = PDC / PT   

Le lCC quant à lui est TCC est le ratio de paires noeuds directement(PDC) ou indirectement (PIC) connectés sur le nombre total de paires des noeuds du graphe (PT) :
LCC = (PIC + PDC) / PT

Ces deux métriques sont égales si :
- Tous les noeuds sont déconnectés : TCC = LCC = 0
- Tous les noeuds sont directement connectés entre eux (PIC=0) : TCC = LCC = 1


// Code 
class StockManager {
    private Map<String, Integer> stock;

    public StockManager() {
        stock = new HashMap<>();
        stock.put("Produit A", 5);
        stock.put("Produit B", 10);
        stock.put("Produit C", 2);
    }

    public boolean vérifierDisponibilitéProduit(String produit) {
        if (stock.containsKey(produit)) {
            int quantity = stock.get(produit);
            if (quantity > 0) {
                System.out.println("Le produit " + produit + " est disponible en stock.");
                return true;
            } else {
                System.out.println("Le produit " + produit + " n'est pas disponible en stock.");
                return false;
            }
        } else {
            System.out.println("Le produit " + produit + " n'est pas répertorié en stock.");
            return false;
        }
    }

    public void réduireStock(String produit) {
        if (stock.containsKey(produit)) {
            int quantity = stock.get(produit);
            if (quantity > 0) {
                stock.put(produit, quantity - 1);
                System.out.println("Le stock du produit " + produit + " a été réduit après une commande.");
            } else {
                System.out.println("Le produit " + produit + " n'est pas disponible en stock.");
            }
        } else {
            System.out.println("Le produit " + produit + " n'est pas répertorié en stock.");
        }
    }
    
    public void approvisionnerStock(String produit, int quantite) {
        System.out.println("Approvisionnement du stock pour le produit : " + produit + ", Quantité : " + quantite);

        if (stock.containsKey(produit)) {
            int quantiteActuelle = stock.get(produit);
            stock.put(produit, quantiteActuelle + quantite);
        } else {
            stock.put(produit, quantite);
        }

        System.out.println("Le stock pour le produit " + produit + " a été approvisionné avec succès.");
    }
}//



Le LCC ne pourrait pas être inférieur au TCC car pour le determiner on fait le ratio du nombre de paires de noeuds directement et indirectement connectés, tandis que pour le LCC on ne prend en compte que le nombre de paires de noeuds directement  connectés.
