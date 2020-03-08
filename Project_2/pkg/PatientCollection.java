package pkg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PatientCollection implements PatientCollectionADT {
	private ArrayList<Patient> myPatients;
	private ArrayList<String> ids;
	private String fileName;
	
	public PatientCollection() {
		myPatients = new ArrayList<Patient>();
		ids = new ArrayList<String>();
		fileName = "./project1/data.csv";
		readFile();
	}
	public PatientCollection(String fn) {
		myPatients = new ArrayList<Patient>();
		ids = new ArrayList<String>();
		fileName = fn;
		readFile();
	}	
	private void readFile () {
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				String values ="";
				for (int i = 3; i < tokens.length-1; i++) {
					values = values + tokens[i] +",";
				}
			
				values += tokens[tokens.length-1];
				
				//if the patients doesn't have a note...
				if(tokens.length == 4779) {
					
					String nTokens[] = new String[tokens.length + 1];
					
					//copies old elements to new array
					for (int i = 0; i < tokens.length; i++){
					      nTokens[i] = tokens[i];
					   }
					   tokens = nTokens;
					   tokens[tokens.length-1] = null; //sets last element to null
				}
				
				String prediction = Predictor.predict(Double.parseDouble(tokens[3697]), Double.parseDouble(tokens[3258]));
				String notes = tokens[tokens.length-1];//adds notes
				
				Patient toAdd = new Patient(tokens[0], prediction,tokens[2],values, notes);
				myPatients.add(toAdd);
				ids.add(tokens[2]);
			}
		} catch (Exception e) {
			System.err.println("there was a problem with the file reader, try different read type.");
			try {
				br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(fileName.substring(1))));
				String line = null;
				while ((line = br.readLine()) != null) {
					String[] tokens = line.split(",");
					String values ="";
					for (int i = 3; i < tokens.length-2; i++) {
						values = values + tokens[i] +",";
					}
					values += tokens[tokens.length-2];	
					
					//if the patients doesn't have a note...
					if(tokens.length == 4779) {
						
						String nTokens[] = new String[tokens.length + 1];
						
						//copies old elements to new array
						for (int i = 0; i < tokens.length; i++){
						      nTokens[i] = tokens[i];
						   }
						   tokens = nTokens;
						   tokens[tokens.length-1] = null; //sets last element to null
					}
					
					String notes = tokens[tokens.length-1];//add notes
					Patient toAdd = new Patient(tokens[0],tokens[1],tokens[2],values, notes);
					myPatients.add(toAdd);
					ids.add(tokens[2]);
				}
			} catch (Exception e2) {
				System.err.println("there was a problem with the file reader, try again.  either no such file or format error");
			} finally {
				if (br != null)
					try {
						br.close();
					} catch (IOException e2) {
						System.err.println("could not close BufferedReader");
					}
			}			
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					System.err.println("could not close BufferedReader");
				}
		}
	} // end of readFile method	
	
	
	public void setResultForPatient (String id, String result) {
		Patient pat = getPatient(id);
		if (pat!=null) {
			pat.setResult(result);
		}
	}
	public String addPatientsFromFile (String fileName) {
		// format
		//id,protein1,protein2, ... , protein4776
		String errors = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String line;
			int lineNum = 1;
			while ((line = br.readLine()) != null) {
				System.out.println(line.substring(0,30));
				String[] tokens = line.split(",");
				int index = line.indexOf(',');
				String values =line.substring(index+1);
				String prediction = Predictor.predict(Double.parseDouble(tokens[3697]), Double.parseDouble(tokens[3258]));
				
				System.out.println(tokens.length);
				
				//if the patients doesn't have a note...
				if(tokens.length == 4777) {
					
					String nTokens[] = new String[tokens.length + 1];
					
					//copies old elements to new array
					for (int i = 0; i < tokens.length; i++){
					      nTokens[i] = tokens[i];
					   }
					   tokens = nTokens;
					   tokens[tokens.length-1] = null; //sets last element to null
				}
				
				System.out.println(tokens.length);
				
				String notes = tokens[tokens.length-1];
				Patient toAdd = new Patient("unknown",prediction,tokens[0],values, notes);
				if (ids.contains(tokens[0])) {
					errors += "line # "+lineNum+" "+tokens[0]+" is not a unique identifier\n";
				}
				else if (tokens.length!=4778) {
					errors += "line # "+lineNum+" not the correct number of values\n";
				}
				else {
					myPatients.add(toAdd);
					ids.add(tokens[0]);
					;				}
				lineNum++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		return errors;
	}
	public void doWrite(String fn) {
		try {
			FileWriter fw = new FileWriter(fn);
			BufferedWriter myOutfile = new BufferedWriter(fw);
			for (Patient pat : myPatients) {
				myOutfile.write(pat.getResult()+","+pat.getPrediction()+","+pat.getId() );
				for (int i = 0; i < pat.getValuesCount() - 1; i++) {
					myOutfile.write(","+pat.getValue(i));
				}
				
				myOutfile.write(" " + pat.getNotes());
				
				myOutfile.write("\n");
			}
			myOutfile.flush();
			myOutfile.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Didn't save to " + fn);
		}
	}	
	
	public Patient getPatient (String id) {
		int index = myPatients.indexOf(new Patient(id));
		if (index<0)
			return null;
		else
			return myPatients.get(index);
	}

	public Patient removePatient (String id) {
		Patient toRemove = new Patient(id);
		int index  = myPatients.indexOf(toRemove);
		if (index >= 0) {
			Patient toReturn = myPatients.remove(index);
			myPatients.remove(toRemove);
			ids.remove(id);
			return toReturn;
		}
		else {
			return null;
		}
	}
	public ArrayList<String> getIds () {
		return ids;
	}
	public String toString () {
		StringBuilder sb = new StringBuilder();
		for (Patient patient : myPatients) {
			sb.append(patient+"\n");
		}
		return sb.toString();
	}
}
