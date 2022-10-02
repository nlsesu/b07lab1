import java.io.File;

public class Driver {
	public static void main(String[] args) {
		Polynomial p = new Polynomial(); 
		System.out.println(p.evaluate(3));
		double [] c1 = {6,0,0,5};
		int [] e1 = {0, 1, 2, 3};
		Polynomial p1 = new Polynomial(c1, e1);
		double [] c2 = {0,-2,0,0,-9};
		int [] e2 = {0, 1, 2, 3, 4};
		Polynomial p2 = new Polynomial(c2, e2); 
		Polynomial s = p1.add(p2);
		Polynomial p3 = new Polynomial(new File("test.txt"));
		System.out.println("s(0.1) = " + s.evaluate(0.1)); 
		if(s.hasRoot(1))
			System.out.println("1 is a root of s"); 
		else
			System.out.println("1 is not a root of s");
		System.out.println(p1.multiply(p3).evaluate(1));
		p1.saveToFile("testpolyn.txt");
		p1.multiply(p3).saveToFile("testpolynomial.txt");
	}
}