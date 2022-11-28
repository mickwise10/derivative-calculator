package derivative_calculator;

public class Main {

	public static void main(String[] args) {
		double[] coeff = {1, 2, 3};
		Polynomial pl = new Polynomial(coeff);
		Polynomial deriv = pl.derivative();
		System.out.println(pl.toString());
		System.out.println(pl.derivative().toString());
		System.out.println(pl.calculateValue(3));
		System.out.println(deriv.calculateValue(3));
		

	}

}
