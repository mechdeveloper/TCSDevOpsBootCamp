package com;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorService {
	
	@GetMapping("/sum")
	public int calculateSum(int a, int b){
		return a+b;
	}
	
	@GetMapping("/diff")
	public int calculateDiff(int a, int b){
		return a-b;
	}
	
	@GetMapping("/mul")
	public int calculateMul(int a, int b){
		return a*b;
	}
	
	@GetMapping("/mod")
	public int calculateMod(int a, int b){
		return a%b;
	}
	
	@GetMapping("/inc")
	public int calculateInc(int a){
		return ++a;
	}
	
	@GetMapping("/dec")
	public int calculateDec(int a){
		return --a;
	}
	
	@GetMapping("/isequal")
	public Boolean calculateEqual(int a, int b){
		return ((a == b) ? true:false);
	}
	
	@GetMapping("/isgreater")
	public Boolean calculateGreater(int a, int b){
		return ((a > b) ? true:false);
	}
	
	@GetMapping("/islower")
	public Boolean calculateLower(int a, int b){
		return ((a < b) ? true:false);
	}
}
