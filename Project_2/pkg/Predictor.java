package pkg;

public class Predictor {
	
	public static String predict (double p1, double p2) {
		if (p1 <= 20.903959) {
			return "predDP";
		}
		else {
			if (p2<= 22.058599) {
				return "predCR";
			}
			else {
				return "predDP";
			}
		}
	}

}
