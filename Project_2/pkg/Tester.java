package pkg;

public class Tester {

	public static void main(String[] args) {
		
		PatientCollection pats = new PatientCollection ("./project1/data.csv");
		System.out.println(pats);

		Patient pat1 = pats.getPatient("1");
		if (pat1!=null) {
			System.out.println("patient 1:"+pat1);
		}
		else {
			System.out.println("patient not found");
		}
		
		pat1.setResult("resultSet");
		pat1 = pats.getPatient("1");
		if (pat1!=null) {
			System.out.println("patient 1 after result reset:"+pat1);
		}
		else {
			System.out.println("patient not found");
		}
		
		System.out.println("arraylist of ids "+pats.getIds());
		System.out.println();
		System.out.println(pats);
		
		// test removing a patient
		Patient removed = pats.removePatient("111");
		if (removed == null)
			System.out.println("removed failed");
		System.out.println("arraylist of ids after bad remove"+pats.getIds());
		System.out.println();
		System.out.println(pats);		
		System.out.println();
		removed = pats.removePatient("9");
		if (removed != null)
			System.out.println("removed worked");	
		System.out.println("arraylist of ids after 9 removed"+pats.getIds());
		System.out.println();
		System.out.println(pats);		
		System.out.println();		
		
		
		System.out.println("adding patients from newdata");
		System.out.println(pats.addPatientsFromFile("./project1/newdata.csv"));
		System.out.println();
		System.out.println(pats);		
		
		Patient pat30 = pats.getPatient("30");
		System.out.println(pat30);
		
		pats.doWrite("./project1/testwrite.csv");
	}

}
