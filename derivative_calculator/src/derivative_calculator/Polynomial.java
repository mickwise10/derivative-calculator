package derivative_calculator;

public class Polynomial {
	
	private double[] coefficients;
	
	/**
	 * @pre coefficients[0] != 0
	 */
	public Polynomial (double[] coefficients){
		this.coefficients = coefficients;
	}
	
	public String toString() {
		StringBuilder representation = new StringBuilder();
		switch(coefficients.length) {
			case(0):
				return null;
			case(1):
				representation.append(coefficients[0]);
				return representation.toString();
			case(2):
				representation.append(coefficients[0] + "x");
				break;
			default:
				representation.append(coefficients[0] + "x^" + (coefficients.length - 1));
		
		}
		for (int i = 1; i < coefficients.length; i++) {
				if (coefficients[i] != 0) {
					if (i == coefficients.length- 2) {
						representation.append(" + " + coefficients[i] + "x" );
					}
					else if (i == coefficients.length - 1) {
						representation.append(" + " + coefficients[i]);
					}
					else {
						representation.append(" + " + coefficients[i] + "x^" 
								+ (coefficients.length - (i + 1)));
					}
				}
			}
		
		return representation.toString();
	}

	public Polynomial derivative() {
		switch(coefficients.length) {
			case(0):
				return null;
			case(1):
				double[] derivativeCoefficients = {0};
				Polynomial derivative = new Polynomial(derivativeCoefficients);
				return derivative;
		}
		double[] derivativeCoefficients = new double[coefficients.length - 1];
		for (int i = 0; i < derivativeCoefficients.length; i++) {
			derivativeCoefficients[i] = coefficients[i] * (coefficients.length - (i + 1));
		}
		Polynomial derivative = new Polynomial(derivativeCoefficients);
		return derivative;
	}
	
	/**
	 * @pre coefficients.length != 0
	 */
	public double calculateValue(double input) {
		double result = 0;
		for (int i = 0; i < coefficients.length; i++) {
			result += coefficients[i] * Math.pow(input, coefficients.length - (i + 1));
		}
		return result;
	}
	
	
	
	
	
	
}
