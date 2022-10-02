import java.util.*;
import java.io.*;

public class Polynomial {
    public double[] coefficientArr;
    public int[] exponentArr;

    public Polynomial() {
        coefficientArr = new double[0];
        exponentArr = new int[0];
    }

    public Polynomial(double[] coeffArr, int[] expArr) {
		/*this.coefficientArr = new double[coeffArr.length];
		for (int i = 0; i < coeffArr.length; i++) {
			this.coefficientArr[i] = coeffArr[i];
		}
		this.exponentArr = new int[expArr.length];
		for (int i = 0; i < expArr.length; i++) {
			this.exponentArr[i] = expArr[i];
		}*/
        this.coefficientArr = Arrays.copyOf(coeffArr, coeffArr.length);
        this.exponentArr = Arrays.copyOf(expArr, expArr.length);
    }

    public Polynomial(File f) {
        try {
            Scanner sc = new Scanner(f);
            String polyStr = "";
            while (sc.hasNextLine()) {
                polyStr += sc.nextLine();
            }
            sc.close();
            String[] split = polyStr.split("((?=[+-]))"); //positive lookahead on + or -
            this.coefficientArr = new double[split.length];
            this.exponentArr = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                String[] subSplit = split[i].split("x");
                coefficientArr[i] = Double.parseDouble(subSplit[0]);
                if (split[i].contains("x")) {
                    exponentArr[i] = Integer.parseInt(subSplit[1]);
                } else {
                    exponentArr[i] = 0;
                }

            }
        } catch (Exception e) {
            System.err.println(e);
        }


    }

    public Polynomial add(Polynomial p) {
        if (this.coefficientArr == null) {
            return p;
        } else if (p.coefficientArr == null) {
            return this;
        }
        // exponentArr.length == coefficientArr.length should hold (and indices should match)
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
            return new Polynomial(newArr, this.exponentArr);
        } else {
            int i = 0;
            for (double d : p.coefficientArr) {
                newArr[i] = d;
                i++;
            }
            for (int j = 0; j < this.coefficientArr.length; j++) {
                newArr[j] += this.coefficientArr[j];
            }
            return new Polynomial(newArr, p.exponentArr);
        }
    }

    public double evaluate(double d) {
        if (this.coefficientArr == null) {
            return 0;
        }
        double sum = 0;
        for (int i = 0; i < this.coefficientArr.length; i++) {
            sum += this.coefficientArr[i] * Math.pow(d, exponentArr[i]);
        }
        return sum;
    }

    public boolean hasRoot(double d) {
        return evaluate(d) == 0;
    }

    private int bruteForceExpIndx(int[] arr, int key) {
        // returns first instance of key in arr, -1 otherwise
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == key) {
                return i;
            }
        }
        return -1;
    }

    private int maxVal(int[] arr) {
        int largest = 0;
        for (int i : arr) {
            if (i > largest) {
                largest = i;
            }
        }
        return largest;
    }

    private int[] shrinkExpArr(int[] arr, double[] arr2) {
        int nonZero = 0;
        for (double d : arr2) {
            if (d != 0) {
                nonZero++;
            }
        }
        int[] newArr = new int[nonZero];
        for (int i = 0, j = 0; i < arr2.length; i++) {
            if (arr2[i] != 0) {
                newArr[j] = arr[i];
                j++;
            }
        }
        return newArr;
    }

    private double[] shrinkArr(double[] arr) {
        int nonZero = 0;
        for (double d : arr) {
            if (d != 0) {
                nonZero++;
            }
        }
        double[] newArr = new double[nonZero];
        for (int i = 0, j = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                newArr[j] = arr[i];
                j++;
            }
        }
        return newArr;
    }

    public Polynomial multiply(Polynomial p) {
		/*
		1) for each element in p1, multiply coefficients with every element of p2, and add their exponents
		2) if exponent already exists, get index of exponent and add to corresponding coefficientArr indx
		[0, 1, 2, 3]
		[0, 2, 8]
		 */
        if (this.coefficientArr == null || p.coefficientArr == null) {
            return new Polynomial();
        }
        int newLen = maxVal(this.exponentArr) + maxVal(p.exponentArr) + 1;
        double[] newCoeffArr = new double[newLen]; // <- this needs to be longer most likely
        int[] newExpArr = new int[newLen]; // <- this needs to be len of sum of both expArrs?
        // for each coefficient/exponent pair of current polynomial (len/index should match)
        for (int i = 0; i < this.coefficientArr.length; i++) {
            // multiply coefficients and add exponents
            for (int j = 0; j < p.coefficientArr.length; j++) {
                // if exponent exists in new exponent array, add to that index for coefficient array
                int newExp = this.exponentArr[i] + p.exponentArr[j];
                double newCoeff = this.coefficientArr[i] * p.coefficientArr[j];
                int indx = bruteForceExpIndx(newExpArr, newExp);
                if (indx < 0) { // if exponent doesn't exist
                    newExpArr[newExp] = newExp;
                }
                newCoeffArr[newExp] += newCoeff;
            }
        }
        return new Polynomial(shrinkArr(newCoeffArr), shrinkExpArr(newExpArr, newCoeffArr));
    }

    public void saveToFile (String s) {
        try {
            File f = new File(s);
            FileWriter fw = new FileWriter(f);
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < this.coefficientArr.length - 1; i++) {
                if (this.exponentArr[i] == 0 && this.coefficientArr[i] != 0) {
                    pw.print(this.coefficientArr[i]);
                } else if (this.coefficientArr[i] != 0) {
                    pw.print(this.coefficientArr[i] + "x" + this.exponentArr[i]);
                }
                if (this.coefficientArr[i+1] > 0) {
                    pw.print("+");
                }
            }
            int len1 = this.coefficientArr.length - 1;
            pw.print(this.coefficientArr[len1] + "x" + this.exponentArr[len1]);
            pw.close();
            fw.close();
        } catch (Exception e) {
            System.err.println(e);
        }

    }


}
