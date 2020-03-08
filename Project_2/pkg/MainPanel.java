package pkg;

import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.border.BevelBorder;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.SoftBevelBorder;

import  java.awt.Robot;

public class MainPanel extends JPanel  {

	private PatientCollection myPats;
	private JTable table;
	private JTable PatientTable;
	private String selectedPatient = "1";
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JComboBox PatientComboBox;
	private JRadioButton DPRadioButton;
	private JRadioButton CRRadioButton;
	private JTextPane patNotesBox;
	private JTextField proteinField;
	private JSpinner proteinSpinner;

	public MainPanel(){

		this.createImage(null);

		setBackground(new Color(220, 220, 220)); //sets background color to gray
		setPreferredSize(new Dimension(800, 600)); //sets preferred size to 800 x 600
		myPats = new PatientCollection("./data.csv"); //sets default file to "data.csv in pkg folder"

		//////////////////////////
		//Creates program banner
		//////////////////////////
		JLabel pimsBanner = new JLabel("");
		pimsBanner.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(null,"Thank you for using PIMS! \n\n Designed by Alexander Salas for the University of Arkansas Medical Science \n\n Dr. Mark Doderer, University of Central Arkansas \n\n All rights waived.", "PIMS", JOptionPane.PLAIN_MESSAGE);
			}
		});
		pimsBanner.setBounds(160, 22, 480, 166);
		pimsBanner.setIcon(new ImageIcon(MainPanel.class.getResource("/pkg/pimsBANNER.png")));
		add(pimsBanner);

		///////////////////////
		//Creates Radio buttons
		//////////////////////

		//Creates Result radio buttons label
		JLabel lblResult = new JLabel("Result");
		lblResult.setBounds(138, 297, 46, 14);
		add(lblResult);

		//creates radio button for Disease Progression
		DPRadioButton = new JRadioButton("Disease Progression");
		DPRadioButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) { //if button was pressed do...

				if(!(selectedPatient.equals("(none)"))){ //if an ID was selected do...
					myPats.getPatient(selectedPatient).setResult("DP");
					updateSelectedTable(); //update selectedTable to show change
					updatePatCollectionTable();//update PatCollectionTable to show change
				}
			}
		});
		buttonGroup.add(DPRadioButton);
		DPRadioButton.setBounds(164, 320, 150, 20);
		add(DPRadioButton);

		//creates radio button for Complete Response
		CRRadioButton = new JRadioButton("Complete Response");
		CRRadioButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		CRRadioButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) { //if button was pressed do...

				if(!(selectedPatient.equals("(none)"))){ //if an ID was selected do...
					myPats.getPatient(selectedPatient).setResult("CR");
					updateSelectedTable();
					updatePatCollectionTable();
				}
			}
		});
		buttonGroup.add(CRRadioButton);
		CRRadioButton.setBounds(10, 320, 150, 20);
		add(CRRadioButton);

		//////////////////////////////////////
		//creates combo box with patient ids
		//////////////////////////////////////

		//creates label for patient combo box
		JLabel patientLabel = new JLabel("Patient ID");
		patientLabel.setBounds(10, 225, 65, 20);
		add(patientLabel);

		PatientComboBox = new JComboBox(); 
		patientLabel.setLabelFor(PatientComboBox);
		PatientComboBox.addActionListener(new ActionListener() { //if combobox element is seleced do...

			public void actionPerformed(ActionEvent e) { 

				selectedPatient = (String)PatientComboBox.getSelectedItem(); //stores the id selected
				updateSelectedTable(); //updates selectedTable to show selectedPatients info
				updatePatNotes(); //updates patientNotes to show selected patients notes, allows editing
				updateProteinField();
			}
		});
		PatientComboBox.setBounds(75, 225, 65, 20); 
		ArrayList<String> myIds = myPats.getIds(); 
		setLayout(null); //come back and find out exactly why i need this
		PatientComboBox.setModel(new DefaultComboBoxModel(myIds.toArray()));
		add(PatientComboBox); 

		/////////////////////////
		//Selected Patient table
		/////////////////////////
		PatientTable = new JTable();
		PatientTable.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		PatientTable.setModel(new DefaultTableModel(
				new Object[][] {
					{"ID", "Prediction", "Result", "Protein[3697]", "Proteint[3258]"},
					{myPats.getPatient(selectedPatient).getId(), myPats.getPatient(selectedPatient).getPrediction(), myPats.getPatient(selectedPatient).getResult(), Double.toString(myPats.getPatient(selectedPatient).getValue(3697)), Double.toString(myPats.getPatient(selectedPatient).getValue(3258))}
				},
				new String[] {
						"ID", "Prediction", "Result", "Protein[1]", "Protein[2]"
				}
				));
		PatientTable.getColumnModel().getColumn(0).setPreferredWidth(26);
		PatientTable.getColumnModel().getColumn(1).setPreferredWidth(70);
		PatientTable.getColumnModel().getColumn(2).setPreferredWidth(50);
		PatientTable.getColumnModel().getColumn(3).setPreferredWidth(90);
		PatientTable.getColumnModel().getColumn(4).setPreferredWidth(90);
		PatientTable.setBounds(10, 254, 315, 32);
		add(PatientTable);
		updateSelectedTable();

		//////////////////////////////////
		//Creates button to remove patient
		///////////////////////////////////
		JButton RemovePatientButton = new JButton("Remove Patient");
		RemovePatientButton.setForeground(Color.RED); //sets remove patient text to RED for additional significance
		RemovePatientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				selectedPatient = (String)PatientComboBox.getSelectedItem(); //stores the id selected

				int input = JOptionPane.showConfirmDialog(null, "Are you sure you would like to remove this patient?");

				if(!(selectedPatient.equals("(none)")) && (myIds.contains(selectedPatient) && (input == 0))){ //if an ID was selected do...
					myPats.removePatient(selectedPatient);
					JOptionPane.showMessageDialog(null, "Patient ID: " + selectedPatient + " removed");
					ArrayList<String> myIds = myPats.getIds();
					PatientComboBox.setModel(new DefaultComboBoxModel(myIds.toArray()));
					selectedPatient = (String)PatientComboBox.getSelectedItem(); //stores next id before updating
					updateSelectedTable();
					updatePatCollectionTable();
				}

				else{
					JOptionPane.showMessageDialog(null, "No patients removed");
				}
			}
		});
		RemovePatientButton.setBounds(150, 225, 150, 20);
		add(RemovePatientButton);

		////////////////////////////////
		//Creates Patient notes text box
		////////////////////////////////
		//Label for patient notes
		JLabel patientLabelNotes = new JLabel("Patient Notes: ");
		patientLabelNotes.setBounds(66, 394, 84, 14);
		add(patientLabelNotes);

		patNotesBox = new JTextPane();
		patNotesBox.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		patNotesBox.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					myPats.getPatient(selectedPatient).setNotes(patNotesBox.getText());
					add(patNotesBox);
				}
			}
		});
		patientLabelNotes.setLabelFor(patNotesBox);
		patNotesBox.setBounds(10, 419, 200, 100);
		add(patNotesBox);
		updatePatNotes();

		////////////////////////////////
		//Add Patients from file button
		////////////////////////////////
		JButton addFromFileButton = new JButton("Add Patients From File");
		addFromFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser("./");

				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					//File selectedFile = jfc.getSelectedFile();
					//imagePanel.changeImage(selectedFile.getAbsolutePath());
					myPats.addPatientsFromFile(""+jfc.getSelectedFile());
					PatientComboBox.setModel(new DefaultComboBoxModel(myIds.toArray()));
					updatePatCollectionTable();
				} 
			}
		});
		addFromFileButton.setBounds(150, 200, 160, 20);
		add(addFromFileButton);

		///////////////////////
		//Creates menu Bar
		//////////////////////
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 800, 22);
		add(menuBar);

		//Creates file menu
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		//Creates Import item within the File menu, imports patients from .csv file
		JMenuItem mntmImport = new JMenuItem("Import");
		mntmImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser("./");

				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					myPats.addPatientsFromFile(""+jfc.getSelectedFile());
					PatientComboBox.setModel(new DefaultComboBoxModel(myIds.toArray()));
					updatePatCollectionTable();
				} 
			}
		});

		mnFile.add(mntmImport);

		//create save menu item, writes the data to test.csv or data.csv
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myPats.doWrite("./testwrite.csv"); //change me to data.csv when you turn me in!
			}
		});

		//create print button
		JMenuItem mntmPrint = new JMenuItem("Print");
		mntmPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					printDoc();
				} catch (PrinterException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
				);
		mnFile.add(mntmPrint);
		mnFile.add(mntmSave);

		//Creates Exit menu item, exits the program
		JMenuItem mntmNewMenuItem = new JMenuItem("Exit");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int input = JOptionPane.showConfirmDialog(null, "Do you want to save changes?");

				//if yes, save and close
				if(input == 0) {
					myPats.doWrite("./testwrite.csv"); //change me to data.csv when you turn me in!
					doClose();
				}

				//if no, close
				else if(input == 1) {
					doClose();
				}

				//if cancel, do nothing
				else {}
			}
		});

		//Creates take screenshot menu item
		JMenuItem mntmTakeScreenshot = new JMenuItem("Take Screenshot");
		mntmTakeScreenshot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {


				int input = JOptionPane.showConfirmDialog(null, "Are you sure you would like to take a screenshot?");
				if(input == 0){
					takeScreenShot();
					JOptionPane.showMessageDialog(null, "Screenshot saved in root folder.");
				}

				else {
					JOptionPane.showMessageDialog(null, "Screen Shot not taken");
				}
			}
		});
		mnFile.add(mntmTakeScreenshot);
		mnFile.add(mntmNewMenuItem);

		////////////////////////////////////
		//Creates Patient collection table
		////////////////////////////////////
		JScrollPane scrollPane = new JScrollPane(); //adds scroll bar to table
		scrollPane.setBounds(335, 214, 400, 350); 
		add(scrollPane);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				selectedPatient = table.getValueAt(table.getSelectedRow(), 0).toString(); //gets value at table[selected, 0] (selected patient's ID)
				updateSelectedTable();
				updatePatNotes();
				updateComboBox();
				updateProteinField();
			}
		});
		scrollPane.setViewportView(table);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null, null, null},
				},
				new String[] {
						"ID", "Prediction", "Result", "Protein[3697]", "Protein [3258]"
				}
				) {
			Class[] columnTypes = new Class[] {
					String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
					false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(60);
		table.getColumnModel().getColumn(2).setPreferredWidth(40);
		table.getColumnModel().getColumn(3).setPreferredWidth(90);
		table.getColumnModel().getColumn(4).setPreferredWidth(90);
		updatePatCollectionTable();

		///////////////////////////////////////
		//Creates uams badge and parter label
		///////////////////////////////////////
		JLabel lblUamsbadge = new JLabel("uamsBADGE");
		lblUamsbadge.setIcon(new ImageIcon(MainPanel.class.getResource("/pkg/uamsBadge.png")));
		lblUamsbadge.setBounds(676, 52, 114, 80);
		add(lblUamsbadge);

		JLabel lblAPartnerOf = new JLabel("A partner of");
		lblAPartnerOf.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblAPartnerOf.setBounds(694, 40, 71, 14);
		add(lblAPartnerOf);

		/////////////////////////////////////////
		//Creates protein value finder
		/////////////////////////////////////////

		//creates protein number label
		JLabel lblProtein = new JLabel("Protein #");
		lblProtein.setBounds(10, 362, 60, 14);
		add(lblProtein);

		//creates protein spinner
		proteinSpinner = new JSpinner();
		proteinSpinner.setValue(1);
		proteinSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateProteinField();
			}
		});
		proteinSpinner.setBounds(66, 359, 50, 20);
		add(proteinSpinner);

		//creates protein text field (for protein values based off of spinner number)
		proteinField = new JTextField();
		proteinField.setBounds(173, 359, 100, 20);
		add(proteinField);	

		//label for protein value text field
		JLabel lblValue = new JLabel("Value: ");
		lblValue.setBounds(126, 362, 46, 14);
		add(lblValue);
		updateProteinField();	

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	//											METHODS												  //
	///////////////////////////////////////////////////////////////////////////////////////////////////

	//print a (not so) pretty document
	public void printDoc() throws PrinterException, IOException{

		write();

		File file = new File("pdata.txt");

		Desktop.getDesktop().print(file);

	}

	public void write() {

		String fn = "./pdata.txt";

		try {
			FileWriter fw = new FileWriter(fn);
			BufferedWriter myOutfile = new BufferedWriter(fw);
			for (int i = 0; i < myPats.getIds().size(); i++) {

				myOutfile.write(myPats.getPatient(myPats.getIds().get(i)).getId() + "," + myPats.getPatient(myPats.getIds().get(i)).getResult()+","+myPats.getPatient(myPats.getIds().get(i)).getPrediction()+"," + myPats.getPatient(myPats.getIds().get(i)).getValue(3697)+ ","+ myPats.getPatient(myPats.getIds().get(i)).getValue(3258)  );
				myOutfile.write("\n");
			}
			myOutfile.flush();
			myOutfile.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Didn't save to " + fn);
		}
	}	


	//update proteinField
	public void updateProteinField() {	

		if((Integer)proteinSpinner.getValue() > 0 && (Integer)proteinSpinner.getValue() < 4776) { //if spinner is within range do...
			proteinField.setForeground(Color.BLACK); //make sure color is black
			proteinField.setText(Double.toString((myPats.getPatient(selectedPatient).getValue((Integer) proteinSpinner.getValue()))));
		}

		else {
			proteinField.setForeground(Color.RED);//set color red to indicate a boo-boo
			proteinField.setText("ERROR"); 
		}
	}


	//update selectedTableMethod
	public void updateSelectedTable() {

		PatientTable.setModel(new DefaultTableModel(
				new Object[][] {
					{"ID", "Prediction", "Result", "Protein[3697]", "Proteint[3258]"},
					{myPats.getPatient(selectedPatient).getId(), myPats.getPatient(selectedPatient).getPrediction(), myPats.getPatient(selectedPatient).getResult(), Double.toString(myPats.getPatient(selectedPatient).getValue(3697)), Double.toString(myPats.getPatient(selectedPatient).getValue(3258))}
				},
				new String[] {
						"ID", "Prediction", "Result", "Protein[1]", "Protein[2]"
				}
				));

		PatientTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		PatientTable.getColumnModel().getColumn(1).setPreferredWidth(70);
		PatientTable.getColumnModel().getColumn(2).setPreferredWidth(50);
		PatientTable.getColumnModel().getColumn(3).setPreferredWidth(90);
		PatientTable.getColumnModel().getColumn(4).setPreferredWidth(90);
		add(PatientTable);

		//gets result and sets radio button
		switch(myPats.getPatient(selectedPatient).getResult()) {

		case "CR":
			CRRadioButton.setSelected(true);
			break;
		case "DP":
			DPRadioButton.setSelected(true);
			break;
		}
	}

	//method to take a screenshot
	public void takeScreenShot() {
		BufferedImage image = ScreenImage.createImage(this);		
		String fileName = "jpanel.png";
		try {
			ScreenImage.writeImage(image, fileName);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	//method to update patient notes text box, given the selected patient
	public void updatePatNotes(){
		patNotesBox.setText(myPats.getPatient(selectedPatient).getNotes());	
		add(patNotesBox);
	}

	//method to update patient collection table
	public void updatePatCollectionTable() {

		ArrayList<String> myIds = myPats.getIds();
		Object patCollection[][] = new Object[myIds.size()][myIds.size()];

		for(int i = 0; i < myIds.size(); i++) {

			patCollection[i][0] = myPats.getPatient(myIds.get(i)).getId();
			patCollection[i][1] = myPats.getPatient(myIds.get(i)).getPrediction();
			patCollection[i][2] = myPats.getPatient(myIds.get(i)).getResult();
			patCollection[i][3] = myPats.getPatient(myIds.get(i)).getValue(3697);
			patCollection[i][4] = myPats.getPatient(myIds.get(i)).getValue(3258);
		}

		table.setModel(new DefaultTableModel(
				patCollection,
				new String[] {
						"ID", "Prediction", "Result", "Protein[3697]", "Protein [3258]"
				}
				) {
			Class[] columnTypes = new Class[] {
					String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.setCellSelectionEnabled(true);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(60);
		table.getColumnModel().getColumn(2).setPreferredWidth(40);
		table.getColumnModel().getColumn(3).setPreferredWidth(90);
		table.getColumnModel().getColumn(4).setPreferredWidth(90);
	}

	//update combo box method
	public void updateComboBox() {

		//this was difficult to do
		//this is in case the user selects a patient via patient collection window (the big one)
		//, now the patientComboBox will now reflect the new selectedPatient, that way
		// if the user decides to remove the patient, it will be the one selected via collection table
		// ...i'm a little proud of this one
		for(int i = 0; i < PatientComboBox.getItemCount()-1; i++) {
			if (PatientComboBox.getItemAt(i).equals(selectedPatient)) {
				PatientComboBox.setSelectedIndex(i);
				break;
			}
		}
	}

	//close method
	public void doClose() {
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
		JOptionPane.showMessageDialog(null, "Exiting PIMS...");
		frame.dispose();
	}
}