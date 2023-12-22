# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Tight Class Cohesion (TCC) and Loose Class Cohesion (LCC) metrics produce the same value for a given Java class if **the ratio of directly connected pairs of node in the graph to the number of all pairs of node** is the same as **the number of pairs of connected (directly or indirectly) nodes to all pairs of node**. So its if all connected nodes are directly connected.

For example, we created the class car that we joined in the Exercice1 folder.
In our example, we have 4 methods in our class  (without the constructor).
- We have 6 potential pairs of methods.
- We have 2 pairs of directly connected methods.
- We have 2 pairs of indirectly connected methods.

LCC can be lower than TCC when pairs are connected indirectly.
In our example it could have been if the cost method used the number of wheels.


------------------------------
------------------------------
Authors:
- Baptiste AMICE
- Ulysse-Néo LARTIGAUD
