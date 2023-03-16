package project;

import org.junit.jupiter.api.Test;
import junit.framework.TestCase;

class operationsTest extends TestCase{

	@Test
	public void testSum() {
		operations sumTest = new operations();
		int sum =(int) sumTest.sum(1, 2);
		assertEquals(3,sum);
	}
	
	@Test
	public void testSubstraction() {
		operations substractionTest = new operations();
		int substraction=(int) substractionTest.substract(5,1);
		assertEquals(4,substraction);
	}
	@Test
	public void testMultiply() {
		operations multiplyTest = new operations();
		int multiply=(int) multiplyTest.multiply(2,4);
		assertEquals(8,multiply);
	}
	@Test
	public void testDivition() {
		operations divitionTest = new operations();
		int divition=(int) divitionTest.divide(30,2);
		assertEquals(15,divition);
	}

}
