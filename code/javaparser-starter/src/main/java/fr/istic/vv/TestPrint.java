package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

public class TestPrint {
	public int test;
	public int test2  = 3;
	
	private int test3;
	private int test4  = 3;
	TestPrint(){
		test = 2;
		test4 = 2;
	}
	
	public int getTest3() {
		this.getTest2();
		test2 = 1;
		int tes = 2;
		return this.test3;
		//return SourceRoot.Callback.Result.DONT_SAVE;
	}

	public int getTest2() {

		return this.test2;
	}
}
