package com;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestCalculatorService {
	
	int num1 = 20;
	int num2 = 5;
	
	@Test
	public void testSum() {
		CalculatorService calc = new CalculatorService();
		System.out.println("testSum");
		Assertions.assertEquals(25, calc.calculateSum(num1, num2));
	}
	
	@Test
	public void testDiff() {
		CalculatorService calc = new CalculatorService();
		System.out.println("testDiff");
		Assertions.assertEquals(15, calc.calculateDiff(num1, num2));
	}

	
	@Test
	public void testMul() {
		CalculatorService calc = new CalculatorService();
		System.out.println("testMul");
		Assertions.assertEquals(100, calc.calculateMul(num1, num2));
	}
	
	@Test
	public void testMod() {
		CalculatorService calc = new CalculatorService();
		System.out.println("testMod");
		Assertions.assertEquals(0, calc.calculateMod(num1, num2));
	}
	
	@Test
	public void testInc() {
		CalculatorService calc = new CalculatorService();
		System.out.println("testInc");
		Assertions.assertEquals(21, calc.calculateInc(num1));
	}
	
	@Test
	public void testDec() {
		CalculatorService calc = new CalculatorService();
		System.out.println("testDec");
		Assertions.assertEquals(4, calc.calculateDec(num2));
	}
	
	@Test
	public void testIsGreater() {
		CalculatorService calc = new CalculatorService();
		System.out.println("testIsGreater");
		Assertions.assertEquals(true, calc.calculateGreater(num1, num2));
	}
	
	@Test
	public void testIsLower() {
		CalculatorService calc = new CalculatorService();
		System.out.println("testIsLower");
		Assertions.assertEquals(false, calc.calculateLower(num1, num2));
	}
	
	@Test
	public void testIsEqual() {
		CalculatorService calc = new CalculatorService();
		System.out.println("testIsEqual");
		Assertions.assertEquals(false, calc.calculateEqual(num1, num2));
	}
}
