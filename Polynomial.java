public class Polynomial {
	public double[] coefficientArr;
	
	public Polynomial() {
		coefficientArr = new double[0];
	}
	public Polynomial(double[] inCoeffArr) {
		coefficientArr = new double[inCoeffArr.length];
		for (int i = 0; i < inCoeffArr.length; i++) {
			coefficientArr[i] = inCoeffArr[i];
		}
	}
	
	public Polynomial add(Polynomial p) {
		int maxLen = Math.max(this.coefficientArr.length, p.coefficientArr.length);
		double[] newArr = new double[maxLen];
		if (this.coefficientArr.length == maxLen) {
			int i = 0;
			for (double d : this.coefficientArr) {
				newArr[i] = d;
				i++;
			}
			for (int j = 0; j < p.coefficientArr.length; j++) {
				newArr[j] += p.coefficientArr[j];
			}
		} else {
			int i = 0;
			for (double d : p.coefficientArr) {
				newArr[i] = d;
				i++;
			}
			for (int j = 0; j < this.coefficientArr.length; j++) {
				newArr[j] += this.coefficientArr[j];
			}
		}
		return new Polynomial(newArr);
	}

	public double evaluate(double d) {
		double sum = 0;
		for (int i = 0; i < this.coefficientArr.length; i++) {
			sum += this.coefficientArr[i] * Math.pow(d, i);	
		}
		return sum;
	}

	public boolean hasRoot(double d) {
		if (evaluate(d) == 0) {
			return true;
		} else {
			return false;
		}
	}
}