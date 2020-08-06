package mha.ui.SimpleGUI;

import java.awt.Component;
import java.awt.Window;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

import javax.swing.*;
import mha.engine.core.Equipement;

import mha.engine.MHAFileFilter;


public class AjoutEquipement extends JPanel implements ActionListener{

	String[] nomChamp={"Nom","id","type","Att","Esq","Deg","DLA","reg","pv","vue","Armure Physique","Armure Magique","Effet de zone","Bidouille","Poids (en minutes)","MM","RM","Equipé"};
	JTextField idField,nomField,fileNom;
	JComboBox typeComboBox;
	JCheckBox zoneCheckBox,bidouilleCheckBox,equipeCheckBox;
	JButton addButton, loadButton, cancelButton, saveButton;
	JDialog dialog;
	JDialog jw;
	JFileChooser fc;
	JLabel jl;
	Component[] champsListe;
	static String[] typesListe = {"Armure","Bouclier","Casque","Arme (une main)","Talisman","Bottes","Bidouille","Anneau","Bric à Brac","Arme (deux mains)","Composant","Parchemin","Potion","Tarot","Champignon","Minerai"};
	String[] AjoutEquip;
	File file;

	public AjoutEquipement(JDialog g,Equipement e,boolean b) {
		this(g,true);
		//addequip id type Att Esq Deg DLA reg pv vue armure_physique armure_magique effet_de_zone bidouille poids(en minute) mm rm est_équipé nom
		nomField.setText(e.getName());
		idField.setText(""+e.getId());
		typeComboBox.setSelectedIndex(e.getType());
		((JSpinner)champsListe[3]).setValue(e.getBMAttaque());
		((JSpinner)champsListe[4]).setValue(e.getBMEsquive());
		((JSpinner)champsListe[5]).setValue(e.getBMDegat());
		((JSpinner)champsListe[6]).setValue(e.getBMDLA());
		((JSpinner)champsListe[7]).setValue(e.getBMRegeneration());
		((JSpinner)champsListe[8]).setValue(e.getPV());
		((JSpinner)champsListe[9]).setValue(e.getBMVue());
		((JSpinner)champsListe[10]).setValue(e.getBMArmurePhysique());
		((JSpinner)champsListe[11]).setValue(e.getBMArmureMagique());
		zoneCheckBox.setSelected(e.isZone());
		bidouilleCheckBox.setSelected(e.isBidouille());
		((JSpinner)champsListe[15]).setValue(e.getBMMM());
		((JSpinner)champsListe[14]).setValue(e.getPoids());
		((JSpinner)champsListe[16]).setValue(e.getBMRM());
		equipeCheckBox.setSelected(b);
		((JSpinner)champsListe[18]).setValue(e.getBMMAttaque());
		((JSpinner)champsListe[19]).setValue(e.getBMMEsquive());
		((JSpinner)champsListe[20]).setValue(e.getBMMDegat());
		((JSpinner)champsListe[22]).setValue(e.getBMMRegeneration());
		((JSpinner)champsListe[24]).setValue(e.getBMMVue());
		dialog.setVisible(true);
	}

	public AjoutEquipement(JDialog g,boolean isModify) {
		dialog = new JDialog(g, "Créer un équipement",true);
		dialog.setVisible(false);
		dialog.setContentPane(this);
//		dialog.setSize(new Dimension(360, 480));
		AjoutEquip=new String[nomChamp.length+5];
		dialog.setResizable(false);
		jw = g;
		
		if(MHAGUI.lastDirectory.length()==0)
			fc = new JFileChooser(new File ("."));
		else
			fc = new JFileChooser(MHAGUI.lastDirectory);
		MHAFileFilter filter = new MHAFileFilter("mha","Fiche de perso pour Mountyhall Arena");
		fc.setFileFilter(filter);
		filter = new MHAFileFilter("equ","Fichier équipement pour mountyhall arena");
		fc.setFileFilter(filter);
		
		GridBagLayout Gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new java.awt.Insets(1, 1, 1, 1);
		c.anchor=GridBagConstraints.WEST;
		setLayout(Gridbag);
		
		//on initialise
		idField = new JTextField(5);
		nomField = new JTextField(50); nomField.setColumns(18);
		fileNom = new JTextField(50);
		zoneCheckBox = new JCheckBox("");
		bidouilleCheckBox = new JCheckBox("");
		equipeCheckBox = new JCheckBox("");
		typeComboBox = new JComboBox(typesListe);
		typeComboBox.addActionListener(this);
		
		champsListe = new Component[25];
		champsListe[0] = nomField;
		champsListe[1]=	idField;
		champsListe[2]=	typeComboBox;
		champsListe[3]=	new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[4]=	new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[5]=	new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[6]=	new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[7]=	new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[8]=	new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[9]=	new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[10]=	new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[11] = new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[12] = zoneCheckBox;
		champsListe[11].setEnabled(false);
		champsListe[13] = bidouilleCheckBox;
		champsListe[12].setEnabled(false);
		champsListe[14] = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
		champsListe[15] = new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[16] = new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[17] = equipeCheckBox;
		//Pour les BMMs
		champsListe[18] = new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[19] = new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[20] = new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[22] = new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		champsListe[24] = new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		
		
		//mise en page
		
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=1;
		
		for(int i=0;i<nomChamp.length;i++){
			c.gridx=0;
			jl = new JLabel(nomChamp[i], null, SwingConstants.LEFT);
			Gridbag.setConstraints(jl, c);
			add(jl);
			if(i==3 || i==5 || i==4 || i==7 || i==9)
			{
				JPanel jp=new JPanel();
				jp.setLayout(new BoxLayout(jp,BoxLayout.LINE_AXIS));
				JFormattedTextField ftf = getTextField((JSpinner)champsListe[i]);
				ftf.setColumns(3);
				ftf = getTextField((JSpinner)champsListe[i+15]);
				ftf.setColumns(3);
				jp.add(champsListe[i]);
				jp.add(Box.createHorizontalStrut(5));
				jp.add(new JLabel("/"));
				jp.add(Box.createHorizontalStrut(5));
				jp.add(champsListe[i+15]);
				jp.add(Box.createHorizontalGlue());
				c.gridx++;
				Gridbag.setConstraints(jp, c);
				add(jp);
				c.gridy++;
			}
			else
			{
				if(i!=1 && i!=2 && i!=12 && i!=13 && i!=17 && i!=0){
					JFormattedTextField ftf = getTextField((JSpinner)champsListe[i]);
					ftf.setColumns(3);
				}

	//			c.gridx++;
				c.gridx++;
				Gridbag.setConstraints(champsListe[i], c);
				add(champsListe[i]);
				c.gridy++;
			}
		}
		
		c.gridy=20;
		c.gridx=0;
		c.gridwidth=2;
		JPanel jp=new JPanel();
		jp.setLayout(new BoxLayout(jp,BoxLayout.LINE_AXIS));
		jp.add(Box.createHorizontalGlue());
		if(isModify)
			addButton = new JButton("Modifier");
		else
			addButton = new JButton("Ajouter");
		addButton.addActionListener(this);
		jp.add(addButton);
		jp.add(Box.createHorizontalStrut(5));
		cancelButton = new JButton("Annuler");
		cancelButton.addActionListener(this);
		jp.add(cancelButton);
		jp.add(Box.createHorizontalStrut(5));
		saveButton = new JButton("Sauver");
		saveButton.addActionListener(this);
		jp.add(saveButton);
		jp.add(Box.createHorizontalStrut(5));
		loadButton = new JButton("Charger");
		loadButton.addActionListener(this);
		jp.add(loadButton);
		jp.add(Box.createHorizontalGlue());
		add(jp,c);
		dialog.pack();
		dialog.setSize(dialog.getWidth()+10,dialog.getHeight()+10);
		dialog.setLocationRelativeTo(jw);
		if(!isModify)
			dialog.setVisible(true);

	}
	
	public synchronized String [] getEquip()
	{
		try
		{
//			wait();
			return AjoutEquip;
		}
		catch(Exception e)
		{e.printStackTrace();return AjoutEquip;}
	}

	public void actionPerformed(ActionEvent e) {
	
		//addButon, fileButton, loadButton, typeComboBox
		boolean[] etatFields = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,};
		
		if(e.getSource() == addButton){
			try {
				Integer.parseInt(idField.getText());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(dialog,
						"L'Id doit être un nombre", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(nomField.getText() == "" || nomField.getText() == null) {
				JOptionPane.showMessageDialog(dialog,
						"Veuillez entrer le nom", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		
			
			//"id","type","Att","Esq","Deg","DLA","reg","pv","vue","Armure Physique","Armure Magique","Effet de zone","Bidouille","Poids (en minutes)","MM","RM","Equipé","Nom"
			String[] EquipString=new String[23];
			EquipString[1]=idField.getText();
			EquipString[2]="";
			String s=(String)(typeComboBox.getSelectedItem());
			for(int i=0;i<typesListe.length;i++)
				if(typesListe[i].equals(s))
				{
					EquipString[2]=""+i;
					break;
				}
			EquipString[3]=((JSpinner)(champsListe[3])).getValue().toString();
			EquipString[4]=((JSpinner)(champsListe[18])).getValue().toString();
			EquipString[5]=((JSpinner)(champsListe[4])).getValue().toString();
			EquipString[6]=((JSpinner)(champsListe[19])).getValue().toString();
			EquipString[7]=((JSpinner)(champsListe[5])).getValue().toString();
			EquipString[8]=((JSpinner)(champsListe[20])).getValue().toString();
			EquipString[9]=((JSpinner)(champsListe[6])).getValue().toString();
			EquipString[10]=((JSpinner)(champsListe[7])).getValue().toString();
			EquipString[11]=((JSpinner)(champsListe[22])).getValue().toString();
			EquipString[12]=((JSpinner)(champsListe[8])).getValue().toString();
			EquipString[13]=((JSpinner)(champsListe[9])).getValue().toString();
			EquipString[14]=((JSpinner)(champsListe[24])).getValue().toString();
			EquipString[15]=((JSpinner)(champsListe[10])).getValue().toString();
			EquipString[16]=((JSpinner)(champsListe[11])).getValue().toString();
			if(((JCheckBox)(champsListe[12])).isSelected()) {EquipString[17]="1";} else {EquipString[17]="0";}
			if(((JCheckBox)(champsListe[13])).isSelected()) {EquipString[18]="1";} else {EquipString[18]="0";}
			EquipString[19]=((JSpinner)(champsListe[14])).getValue().toString();
			EquipString[20]=((JSpinner)(champsListe[15])).getValue().toString();
			EquipString[21]=((JSpinner)(champsListe[16])).getValue().toString();
			if(((JCheckBox)(champsListe[17])).isSelected()) {EquipString[22]="1";} else {EquipString[22]="0";}
			EquipString[0]=nomField.getText();
		
			for(int i=0;i<EquipString.length;i++){
				AjoutEquip[i]=EquipString[i];
			}
			dialog.setVisible(false);
			dialog.dispose();
//			notify();
			return;
			
		}
		
		if(e.getSource() == cancelButton){
			dialog.setVisible(false);
			dialog.dispose();
		}
		
		if(e.getSource() == saveButton){
			
			//addequip id type Att Esq Deg DLA reg pv vue armure_physique armure_magique effet_de_zone bidouille poids(en minute) mm rm est_équipé nom

			
			String addline="addequip ";
			addline += idField.getText() + " ";
			addline += typeComboBox.getSelectedIndex() + " ";
			addline += ((JSpinner)(champsListe[3])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[18])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[4])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[19])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[5])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[20])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[6])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[7])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[22])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[8])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[9])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[24])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[10])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[11])).getValue().toString() + " ";
			if(((JCheckBox)(champsListe[12])).isSelected()) {addline += "1 ";} else {addline += "0 ";} 
			if(((JCheckBox)(champsListe[13])).isSelected()) {addline += "1 ";} else {addline += "0 ";}
			addline += ((JSpinner)(champsListe[14])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[15])).getValue().toString() + " ";
			addline += ((JSpinner)(champsListe[16])).getValue().toString() + " ";
			if(((JCheckBox)(champsListe[17])).isSelected()) {addline += "1 ";} else {addline += "0 ";} 
			addline += nomField.getText();
			int returnVal = fc.showSaveDialog(this);
			file = fc.getSelectedFile();
			try { MHAGUI.lastDirectory = file.getCanonicalPath(); } catch(Exception ex){}
			try	{
				FileWriter writer = new FileWriter(file);
				writer.append(addline);
				writer.close();
			}
			catch (Exception e3){
				JOptionPane.showMessageDialog(dialog,
						"Fichier introuvable ou incorrect", "Erreur",
						JOptionPane.ERROR_MESSAGE);
					return;
			}
			JOptionPane.showMessageDialog(dialog,
					"Sauvegarde réussie", "Erreur",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(e.getSource() == loadButton){
			Vector <String> lignes = new Vector <String> ();
			String valeur;
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				try { MHAGUI.lastDirectory = file.getCanonicalPath(); } catch(Exception ex){}
				try {
					FileReader fr =  new FileReader(file);
					BufferedReader bufferin = new BufferedReader(fr);
					String input = bufferin.readLine();
					if(input!=null)
						input=input.replaceFirst("//.*","");
					while(input != null) {
						String [] ls = input.split(" ");
						if(ls.length>18 && ls[0].toLowerCase().equals("addequip"))
						{
							lignes.add(input);
						}
						input = bufferin.readLine();
						if(input!=null)
							input=input.replaceFirst("//.*","");
					}
					if(lignes.size()==0)
					{
						JOptionPane.showMessageDialog(dialog,
							"Aucun équipement à importer", "Erreur",
							JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(lignes.size()==1)
					{
						//addequip id type Att Esq Deg DLA reg pv vue armure_physique armure_magique effet_de_zone bidouille poids(en minute) mm rm est_équipé nom
						String [] ligne=lignes.elementAt(0).split(" ");
						boolean bmm=false,fullbmm=false;
						try{Integer.parseInt(ligne[18]);Integer.parseInt(ligne[19]);bmm=true;}catch(NumberFormatException ex){}
						try{Integer.parseInt(ligne[21]);Integer.parseInt(ligne[22]);fullbmm=true;}catch(Exception ex){}
						if(fullbmm)
						{
							for(int i=24;i<ligne.length;i++)
								ligne[20]+=" "+ligne[i];
							((JSpinner)champsListe[18]).setValue(Integer.parseInt(ligne[4]));
							ligne[4]=ligne[5];
							((JSpinner)champsListe[19]).setValue(Integer.parseInt(ligne[6]));
							ligne[5]=ligne[7];
							((JSpinner)champsListe[20]).setValue(Integer.parseInt(ligne[8]));
							ligne[6]=ligne[9];
							ligne[7]=ligne[10];
							((JSpinner)champsListe[22]).setValue(Integer.parseInt(ligne[11]));
							ligne[8]=ligne[12];
							ligne[9]=ligne[13];
							((JSpinner)champsListe[24]).setValue(Integer.parseInt(ligne[14]));
							for(int i=10;i<=18;i++)
								ligne[i]=ligne[i+5];
						}
						else if(bmm)
						{
							for(int i=21;i<ligne.length;i++)
								ligne[20]+=" "+ligne[i];
							((JSpinner)champsListe[18]).setValue(Integer.parseInt(ligne[4]));
							ligne[4]=ligne[5];
							ligne[5]=ligne[6];
							((JSpinner)champsListe[20]).setValue(Integer.parseInt(ligne[7]));
							for(int i=6;i<=18;i++)
								ligne[i]=ligne[i+2];
							((JSpinner)(champsListe[19])).setValue(0);
							((JSpinner)(champsListe[22])).setValue(0);
							((JSpinner)(champsListe[24])).setValue(0);
						}
						else
						{
							for(int i=19;i<ligne.length;i++)
								ligne[18]+=" "+ligne[i];
						}
							
						nomField.setText(ligne[18]);
						idField.setText(ligne[1]);
						typeComboBox.setSelectedIndex(Integer.parseInt(ligne[2]));
						((JSpinner)champsListe[3]).setValue(Integer.parseInt(ligne[3]));
						((JSpinner)champsListe[4]).setValue(Integer.parseInt(ligne[4]));
						((JSpinner)champsListe[5]).setValue(Integer.parseInt(ligne[5]));
						((JSpinner)champsListe[6]).setValue(Integer.parseInt(ligne[6]));
						((JSpinner)champsListe[7]).setValue(Integer.parseInt(ligne[7]));
						((JSpinner)champsListe[8]).setValue(Integer.parseInt(ligne[8]));
						((JSpinner)champsListe[9]).setValue(Integer.parseInt(ligne[9]));
						((JSpinner)champsListe[10]).setValue(Integer.parseInt(ligne[10]));
						((JSpinner)champsListe[11]).setValue(Integer.parseInt(ligne[11]));
//						System.out.println(ligne[12]);
						if(Integer.parseInt(ligne[12]) == 1){zoneCheckBox.setSelected(true);} else{zoneCheckBox.setSelected(false);}
//						System.out.println(ligne[13]);
						if(Integer.parseInt(ligne[13]) == 1){bidouilleCheckBox.setSelected(true);} else{bidouilleCheckBox.setSelected(false);}
						((JSpinner)champsListe[14]).setValue(Integer.parseInt(ligne[14]));
						((JSpinner)champsListe[15]).setValue(Integer.parseInt(ligne[15]));
						((JSpinner)champsListe[16]).setValue(Integer.parseInt(ligne[16]));
//						System.out.println(ligne[17]);
						if(Integer.parseInt(ligne[17]) == 1){equipeCheckBox.setSelected(true);} else{equipeCheckBox.setSelected(false);}
						((JSpinner)(champsListe[18])).setValue(0);
						((JSpinner)(champsListe[19])).setValue(0);
						((JSpinner)(champsListe[20])).setValue(0);
						((JSpinner)(champsListe[22])).setValue(0);
						((JSpinner)(champsListe[24])).setValue(0);
					}
					else
					{
						String[] possibilities = new String[lignes.size()];
						for(int i=0;i<lignes.size();i++)
						{
							boolean bmm=false;
							String [] ls = lignes.elementAt(i).split(" ");
							int indice=18;
							try{Integer.parseInt(ls[18]);Integer.parseInt(ls[19]);indice=20;}catch(NumberFormatException ex){}
							try{Integer.parseInt(ls[21]);Integer.parseInt(ls[22]);indice=23;}catch(Exception ex){}
							possibilities[i]=ls[indice];
							for(int j=indice+1;j<ls.length;j++)
								possibilities[i]+=" "+ls[j];
							possibilities[i]+=" ("+ls[1]+")";
						}
						String s = (String)JOptionPane.showInputDialog(
									dialog,
									"Choisissez l'équipement à importer :",
									"Importation d'équipement",
									JOptionPane.PLAIN_MESSAGE,
									null,
									possibilities,
									possibilities[0]);
						String [] ligne=new String[19];
						for(int i=0;i<possibilities.length;i++)
						{
							if(possibilities[i].equals(s))
							{
								ligne=lignes.elementAt(i).split(" ");
								break;
							}
						}
						boolean bmm=false,fullbmm=false;
						try{Integer.parseInt(ligne[18]);Integer.parseInt(ligne[19]);bmm=true;}catch(NumberFormatException ex){}
						try{Integer.parseInt(ligne[21]);Integer.parseInt(ligne[22]);fullbmm=true;}catch(Exception ex){}
						if(fullbmm)
						{
							for(int i=24;i<ligne.length;i++)
								ligne[20]+=" "+ligne[i];
							((JSpinner)champsListe[18]).setValue(Integer.parseInt(ligne[4]));
							ligne[4]=ligne[5];
							((JSpinner)champsListe[19]).setValue(Integer.parseInt(ligne[6]));
							ligne[5]=ligne[7];
							((JSpinner)champsListe[20]).setValue(Integer.parseInt(ligne[8]));
							ligne[6]=ligne[9];
							ligne[7]=ligne[10];
							((JSpinner)champsListe[22]).setValue(Integer.parseInt(ligne[11]));
							ligne[8]=ligne[12];
							ligne[9]=ligne[13];
							((JSpinner)champsListe[24]).setValue(Integer.parseInt(ligne[14]));
							for(int i=10;i<=18;i++)
								ligne[i]=ligne[i+5];
						}
						if(bmm)
						{
							for(int i=21;i<ligne.length;i++)
								ligne[20]+=" "+ligne[i];
							((JSpinner)champsListe[18]).setValue(Integer.parseInt(ligne[4]));
							ligne[4]=ligne[5];
							ligne[5]=ligne[6];
							((JSpinner)champsListe[20]).setValue(Integer.parseInt(ligne[7]));
							for(int i=6;i<=18;i++)
								ligne[i]=ligne[i+2];
							((JSpinner)(champsListe[19])).setValue(0);
							((JSpinner)(champsListe[22])).setValue(0);
							((JSpinner)(champsListe[24])).setValue(0);
						}
						else
						{
							for(int i=19;i<ligne.length;i++)
								ligne[18]+=" "+ligne[i];
							((JSpinner)(champsListe[18])).setValue(0);
							((JSpinner)(champsListe[19])).setValue(0);
							((JSpinner)(champsListe[20])).setValue(0);
							((JSpinner)(champsListe[22])).setValue(0);
							((JSpinner)(champsListe[24])).setValue(0);
						}
						nomField.setText(ligne[18]);
						idField.setText(ligne[1]);
						typeComboBox.setSelectedIndex(Integer.parseInt(ligne[2]));
						((JSpinner)champsListe[3]).setValue(Integer.parseInt(ligne[3]));
						((JSpinner)champsListe[4]).setValue(Integer.parseInt(ligne[4]));
						((JSpinner)champsListe[5]).setValue(Integer.parseInt(ligne[5]));
						((JSpinner)champsListe[6]).setValue(Integer.parseInt(ligne[6]));
						((JSpinner)champsListe[7]).setValue(Integer.parseInt(ligne[7]));
						((JSpinner)champsListe[8]).setValue(Integer.parseInt(ligne[8]));
						((JSpinner)champsListe[9]).setValue(Integer.parseInt(ligne[9]));
						((JSpinner)champsListe[10]).setValue(Integer.parseInt(ligne[10]));
						((JSpinner)champsListe[11]).setValue(Integer.parseInt(ligne[11]));
						if(Integer.parseInt(ligne[12]) == 1){zoneCheckBox.setSelected(true);} else{zoneCheckBox.setSelected(false);}
						if(Integer.parseInt(ligne[13]) == 1){bidouilleCheckBox.setSelected(true);} else{bidouilleCheckBox.setSelected(false);}
						((JSpinner)champsListe[14]).setValue(Integer.parseInt(ligne[14]));
						((JSpinner)champsListe[15]).setValue(Integer.parseInt(ligne[15]));
						((JSpinner)champsListe[16]).setValue(Integer.parseInt(ligne[16]));
						if(Integer.parseInt(ligne[17]) == 1){equipeCheckBox.setSelected(true);} else{equipeCheckBox.setSelected(false);}
					}
				}
				catch (FileNotFoundException e2) {
//					e2.printStackTrace();
					JOptionPane.showMessageDialog(dialog,
							"Fichier introuvable ou incorrect", "Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				catch (Exception e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(dialog,
							"Fichier corrompu", "Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}
		
		if(e.getSource() == typeComboBox){
			
			for (int i=0;i<etatFields.length;i++){
				etatFields[i]=true;
			}
			
			if(typeComboBox.getSelectedItem().equals("Bidouille") || typeComboBox.getSelectedItem().equals("Anneau") || typeComboBox.getSelectedItem().equals("Composants") || typeComboBox.getSelectedItem().equals("Bric à Brac") || typeComboBox.getSelectedItem().equals("Tarot") || typeComboBox.getSelectedItem().equals("Champignon") || typeComboBox.getSelectedItem().equals("Minerai")){
				etatFields[3]=false;
				etatFields[4]=false;
				etatFields[5]=false;
				etatFields[6]=false;
				etatFields[7]=false;
				etatFields[8]=false;
				etatFields[9]=false;
//				etatFields[10]=false;
//				etatFields[11]=false;
				etatFields[12]=false;
				etatFields[15]=false;
				etatFields[16]=false;
				etatFields[17]=false;
				
			}
			else if(typeComboBox.getSelectedItem().equals("Arme (une main)") || typeComboBox.getSelectedItem().equals("Arme (deux mains)")){
				etatFields[8]=false;
//				etatFields[10]=false;
				etatFields[12]=false;
			}
			else {
				etatFields[8]=false;
//				etatFields[11]=false;
				etatFields[12]=false;				
			}
			if(typeComboBox.getSelectedItem().equals("Potion") || typeComboBox.getSelectedItem().equals("Parchemin"))
			{
				etatFields[8]=true;
				etatFields[12]=true;
			}
			else if(typeComboBox.getSelectedItem().equals("Bidouille"))
			{
				etatFields[8]=false;
				((JCheckBox) champsListe[13]).setSelected(true);
				etatFields[13]=false;
			}
			for(int i=0;i<nomChamp.length;i++){
				champsListe[i].setEnabled(etatFields[i]);
			}
			
		}
	
	}
	
	
	
	public JFormattedTextField getTextField(JSpinner spinner) {
		JComponent editor = spinner.getEditor();
		if (editor instanceof JSpinner.DefaultEditor) {
			return ((JSpinner.DefaultEditor) editor).getTextField();
		} else {
			System.err.println("Unexpected editor type: "
					+ spinner.getEditor().getClass()
					+ " isn't a descendant of DefaultEditor");
			return null;
		}
	}
}
