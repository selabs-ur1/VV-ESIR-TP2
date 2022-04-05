# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer
---
**Student**  
<table align="left">
<tr>
  <th> First Name </th>
  <th> Last Name </th>
  <th> @Mail adress </th>
</tr>
<tr>
  <td> Léo </td>
  <td> Thuillier </td>
  <td> leo.thuillier@etudiant.univ-rennes1.fr </td>
</tr>
<tr>
  <td> Thibaut </td>
  <td> Le Marre </td>
  <td> thibaut.le-marre@etudiant.univ-rennes1.fr </td>
</tr>
</table>
<br><br><br><br><br><br>

---

```
public class Op {
	private int Opg;
	private int Opd;
	
	public Op(int g, int d) {
		Opg=g;
		Opd=d;
	}
	
	public int somme() {
		return Opg+Opd;
	}
	
	public int soustraction() {
		return Opg-Opd;
	}
}
```

No it's not possible TCC being the number or direct arc appearing getwen our node in the graph and LCC being the number of max N length path between two node in the graph.
We can say that LCC = TCC + not directed connected node.
