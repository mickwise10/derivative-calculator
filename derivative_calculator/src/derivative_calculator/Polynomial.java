package derivative_calculator;

import java.util.ArrayList;

public class Polynomial {
	
	private double[] coefficients;
	
	/**
	 * @pre coefficients[0] != 0
	 */
	public Polynomial (double[] coefficients){
		assert coefficients.length > 0;
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

	public Polynomial addition(Polynomial otherPol) {
		int thisDegree = coefficients.length - 1;
		int otherDegree = otherPol.coefficients.length - 1;
		int degreeDifference = Math.abs(otherDegree - thisDegree);
		if (otherDegree < thisDegree) {
			double[] resultCoefficients = new double[thisDegree + 1];
			for (int i = 0; i <= otherDegree; i++ ) {
				resultCoefficients[thisDegree - i] = coefficients[thisDegree - i] 
						+ otherPol.coefficients[otherDegree - i];
			}
			for (int j = 0; j < degreeDifference; j++) {
				resultCoefficients[j] = coefficients[j];
			}
			Polynomial result = new Polynomial(resultCoefficients);
			return result;
		}
		else {
			double[] resultCoefficients = new double[otherDegree + 1];
			for (int i = 0; i <= thisDegree; i++ ) {
				resultCoefficients[otherDegree - i] = coefficients[thisDegree - i] 
						+ otherPol.coefficients[otherDegree - i];
			}
			for (int j = 0; j < degreeDifference; j++) {
				resultCoefficients[j] = otherPol.coefficients[j];
			}
			Polynomial result = new Polynomial(resultCoefficients);
			return result;
		}	

	}
	
	public Polynomial subtraction(Polynomial otherPol) {
		return this.addition(otherPol.scalerMultiplication(-1));
	}
	
	public Polynomial multiplication(Polynomial otherPol) {
		int thisDegree = coefficients.length - 1;
		int otherDegree = otherPol.coefficients.length - 1;
		double[] resultCoefficients = new double[thisDegree + otherDegree + 1];
		for (int i = 0; i <= thisDegree; i++) {
			for (int j = 0; j <= otherDegree; j++) {
				resultCoefficients[i + j] += coefficients[i]*otherPol.coefficients[j];
			}
		}
		Polynomial result = new Polynomial(resultCoefficients);
		return result;
	}
	
	public Polynomial scalerMultiplication(double scaler) {
		double[] resultCoefficients = new double[coefficients.length];
		for (int i = 0; i < coefficients.length; i++) {
			resultCoefficients[i] = coefficients[i]*scaler;
		}
		Polynomial result = new Polynomial(resultCoefficients);
		return result;
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
	
	/**
	 * @pre coefficients.length != 0 && otherPolynomial.coefficients.length != 0 
	 */
	public Polynomial[] polynomialDivision(Polynomial otherPol) { /* Returns an array with multiplier 
																	 as first element and remainder as 
																	 second */
		int thisDegree = coefficients.length - 1;
		int otherDegree = otherPol.coefficients.length - 1;
		if (thisDegree < otherDegree) { // Check if division is possible
			return null;
		}
		/* The degree of the multiplier is the difference in degrees from 
		 * the definition of polynomial multiplication */
		int multiplierDegree = thisDegree - otherDegree;
		double[] multiplierCoefficients = new double[multiplierDegree + 1];
		ArrayList <Double> remainderCoefficients = new ArrayList <Double>();
		copyArrayIntoArrayList(remainderCoefficients, coefficients);
		int index = 0;
		while (remainderCoefficients.size() > otherDegree) { // Begin devision process
			// Remove zeros from remainder coefficients
			if (remainderCoefficients.get(remainderCoefficients.size() - 1) == 0) {
				index += removeZeros(remainderCoefficients);
				break;
			}
			/* Normalization n constant will force 
			 * n*otherPol.coefficients[0] == remandierCoefficients[0] */
			double normalizationConstant = 
					(remainderCoefficients.get(thisDegree - index) / 
							otherPol.coefficients[0]);
			multiplierCoefficients[index] = normalizationConstant;
			// Remove biggest power from remainder because we forced it'll become 0
			remainderCoefficients.remove(thisDegree - index); 
			index++;
			for ( int j = 1; j <= otherDegree; j++) { // Calculate the new remainder
				// Subtract elements in the same positions multiplied by normalization constant
				double currentElement = remainderCoefficients.get(remainderCoefficients.size() - j);
				remainderCoefficients.set(remainderCoefficients.size() - j, 
						currentElement - normalizationConstant*otherPol.coefficients[j]);
			}
		}
		removeZeros(remainderCoefficients); // Remove excess zeros
		// Initialize a polynomial array to return the remainder and multiplier
		Polynomial multiplier = new Polynomial(multiplierCoefficients);
		double[] remainderArray = createArrayFromList(remainderCoefficients);
		Polynomial remainder = new Polynomial(remainderArray);
		Polynomial[] multiplierAndRemainder = {multiplier, remainder};
		return multiplierAndRemainder;
	}
	
	private static void copyArrayIntoArrayList(ArrayList <Double> list, double[] arr) {
		for (int i = 0; i < arr.length; i++) {
			list.add(arr[arr.length - (i + 1)]);
		}
	}
	
	private static double[] createArrayFromList(ArrayList <Double> list) {
		double[] result = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}
	
	// Function that removes zeros and returns the amounts removed
	private static int removeZeros(ArrayList <Double> list) {
		int k  = list.size() - 1;
		while(list.get(k) == 0 && k > 0) { 
			list.remove(k);
			k--;
		}
		return list.size() - k;
	}
	
	/**
	 * 
	 * @param otherPol
	 * @pre otherPol.coefficients.length < coefficients.length
	 */
	public Polynomial[] findGcd(Polynomial otherPol) { /* Returns an array of with the gcd as the first 
	 													  element and Bezout coefficients as the 
	 													  second and third */
		ArrayList <Polynomial> multipliers = new ArrayList<Polynomial>();
		Polynomial currentRemainder = otherPol;
		Polynomial lastRemainder = this;
		Polynomial gcd;
		while (currentRemainder.coefficients.length > 1) { // Find gcd using Euclids algorithm
			Polynomial[] currentArray = lastRemainder.polynomialDivision(currentRemainder);
			multipliers.add(currentArray[0].scalerMultiplication(-1));
			lastRemainder = currentRemainder;
			currentRemainder = currentArray[1];
		}
		if (currentRemainder.coefficients[0] != 0) { // If gcd is constant normalize it
			currentRemainder = 
					currentRemainder.scalerMultiplication(1/currentRemainder.coefficients[0]);
		}
		if (currentRemainder.coefficients[0] == 0) {
			gcd = lastRemainder;
		}
		else {
			gcd = currentRemainder;
		}
		double[] onePol = {1};
		Polynomial firstBezoutCoeff = new Polynomial(onePol);
		Polynomial secondBezoutCoeff = multipliers.get(multipliers.size() - 1);
		for (int i = 1; i < multipliers.size(); i++) {
			Polynomial temp = firstBezoutCoeff;
			firstBezoutCoeff = secondBezoutCoeff;
			secondBezoutCoeff = temp.addition(secondBezoutCoeff.
					multiplication(multipliers.get(multipliers.size() - (i + 1))));	
		}
		Polynomial[] results = {gcd, firstBezoutCoeff, secondBezoutCoeff};
		return results;
		
	}
	
	public boolean isEqual(Polynomial otherPol) {
		if (otherPol.coefficients.length != coefficients.length) {
			return false;
		}
		for (int i = 0; i < coefficients.length; i++) {
			if (coefficients[i] != otherPol.coefficients[i]) {
				return false;
			}
		}
		return true;
	}
}


