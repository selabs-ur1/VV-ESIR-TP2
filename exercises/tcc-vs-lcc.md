# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

TCC and LCC produce the same value when every method are linked to all other method.
And it's not possible for TCC to give greater value than LCC, we can see the demonstration:

TCC and LCC definitions

NP= maximum number of possible connections = N * (N-1)/2 where N is the number of methods

NDC = number of direct connections (nmb of edges in the connection graph)

NID = number of indirect connections

Tight class cohesion TCC = NDC/NP
Loose class cohesion LCC=(NDC+NIC)/NP

We can see TCC can never be over of LCC.