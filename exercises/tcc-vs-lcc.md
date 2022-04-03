# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

TCC and LCC produce the same values if all methods of a class have a direct link with every method they would have an indirect link with.

```Java
public class Exo1 {

	public int method1() {
		int res = 10;
		if(methodBool()) {
			res+=2;
		}
		res+=method2();
		return res;
	}

	public boolean methodBool() {
		return method2()==21;
	}

	private int method2() {
		return 21;
	}
	
	
	public String method3() {
		return method4()+" :)";
	}

	private String method4() {
		return "Bonjour";
	}
}
```
``method1()`` is directly connected to ``methodBool()`` and ``method2()``, so ``methodBool()`` and ``method2()`` have to be directly linked for TCC and LCC to be equal.
Note that ``method3()`` and ``method4()`` are not connected to the other methods, and TCC and LCC are still not equal. They were added to show that in order for them to be the same, it is not needed for every method of a class to be connected to every other one (even though such a class would merit to be partitioned in multiple ones in a real scenario).

LCC can not be lower than TCC, because it counts every link, including direct ones.
