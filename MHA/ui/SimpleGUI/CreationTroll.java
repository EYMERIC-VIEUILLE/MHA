/*********************************************************************************
 *    This file is part of Mountyhall Arena                                       *
 *                                                                                *
 *    Mountyhall Arena is free software; you can redistribute it and/or modify    *
 *    it under the terms of the GNU General Public License as published by        *
 *    the Free Software Foundation; either version 2 of the License, or           *
 *    (at your option) any later version.                                         *
 *                                                                                *
 *    Mountyhall Arena is distributed in the hope that it will be useful,         *
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of              *
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the               *
 *    GNU General Public License for more details.                                *
 *                                                                                *
 *    You should have received a copy of the GNU General Public License           *
 *    along with Mountyzilla; if not, write to the Free Software                  *
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA  *
 *********************************************************************************/

package mha.ui.SimpleGUI;

import java.io.*;
import java.math.BigInteger;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.*;
import mha.engine.*;
import mha.engine.core.*;
import mha.ui.SimpleGUI.MHAGUI.MyTableModel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

//import com.sun.java_cup.internal.internal_error;


public class CreationTroll extends JPanel implements ActionListener,
		ChangeListener {
	final JButton cancelButton, saveButton, executeButton, loadButton, avatarButton, ajoutEquip, supprEquip, modifyEquip;
	final JSpinner[] caracSpinner;
	final JTextField numeroField, nomField, mmField, rmField, saveField; // niveauField, raceField, piRestantsField, mmField, rmField, saveField;
	final JLabel niveauLabel, niveauLabelTitre, piTotalLabel,piTotalLabelTitre;
	final JTextField[] caracFields = { new JTextField(20), new JTextField(20),new JTextField(20), new JTextField(20), new JTextField(20),new JTextField(20), new JTextField(20) };
	final JLabel[] amelioPlusInit, piInvestis, prochaineAmelio;
	final String[] titresCarac = { "Caractéristique", "Nb Amélios", "","PI investis", "Prix prochaine Amélio" };
	final String[] caracs = { "Att", "Esq", "Dég", "Règ", "PV", "Vue", "DLA" };
	final String[] uniteCaracs = { "D6", "D6", "D3", "D3", "", "", "min" };
	final int[] incrementCaracs = { 1, 1, 1, 1, 10, 1, 30 };
	final int[] piInitCoutCaracs = { 16, 16, 16, 30, 16, 16, 18 };
	final int[] piInitCoutCaracsParRace = { 16, 16, 16, 30, 16, 16, 18 };
	final int[] nbInitCaracs = { 3, 3, 3, 1, 30, 3, 720 };
	final int[] nbInitCaracsParRace = { 3, 3, 3, 1, 30, 3, 720 };
	final String[] competences_reservees = { "Botte secrète", "Régénération accrue","Accélération du métabolisme", "Camouflage"};
	final String[] competences = {"Charger", "Poser un piége", "Contre-attaque","Déplacement éclair", "Frénésie", "Lancer de potion", "Pistage" };
	final String[] competences_niveau_sup = {"Attaque précise","Coup de butoir", "Parer",};
	final String[] sorts = { "Hypnotisme", "Rafale Psychique", "Vampirisme","Projectile magique", "Analyse anatomique", "Armure éthérée","Augmentation de l'attaque", "Augmentation de l'esquive","Augmentation des dégats", "Bulle Anti-magie", "Bulle magique","Explosion", "Faiblesse passagére", "Flash aveuglant", "Glue","Griffe du sorcier", "Invisibilité", "Projection", "Sacrifice","Téléportation", "Vision accrue", "Vision lointaine","Voir le caché", "Vue troublée" };	
	final String[] equipementColonnes = {"Id","Nom","Type","Caracs"};
	//final String[][] equipementLignes;
	final int[] piCoutComp_reservees = { 0, 0, 0, 0 };
	final int[] piCoutComp = { 50, 50, 20, 20, 100, 30, 10 };
	final int[] piCoutComp_niveau_sup = { 50, 50, 20};
	final JTable equipementTable;
	
	final JTextField[] pourcentComp_reservees = new JTextField[competences_reservees.length];
	final JTextField[] pourcentComp = new JTextField[competences.length];
	final JTextField[] pourcentComp_niveau_sup = new JTextField[competences_niveau_sup.length];
	final JCheckBox[] compCheckBox_reservees = new JCheckBox[competences_reservees.length];
	final JCheckBox[] compCheckBox = new JCheckBox[competences.length];
	final JCheckBox[] compCheckBox_niveau_sup = new JCheckBox[competences_niveau_sup.length];
	final JSpinner[] compSpinner = new JSpinner[competences_niveau_sup.length];
	
	final JTextField[] pourcentSort = new JTextField[sorts.length];
	final JCheckBox[] sortCheckBox = new JCheckBox[sorts.length];
	final String[] racesArray = { "Skrim", "Durakuir", "Kastar", "Tomawak" };
	final JLabel avatar;
	final int[] caracSpecialisee = { 0, 4, 2, 5 };
	final int[] sortSpecialise = { 0, 1, 2, 3 };
	final int[] compSpecialisee = { 0, 1, 2, 3 };
	final JTextField[] mouchesFields = { new JTextField(3), new JTextField(3),new JTextField(3), new JTextField(3), new JTextField(3),new JTextField(3), new JTextField(3) };
	final String[] mouchesFieldsText = { "Crobate", "Vertie", "Lunettes","Miel", "Xidant", "Rivatant", "Nabolisants" };
	final int[] mouchesInt = { 0, 1, 2, 3, 4, 5, 8 };
	final Vector <Equipement> equipements = new Vector<Equipement>();
	String addTroll, addCaracs;
	String[] addEquip, addComp, addSort;
	final JComboBox raceCombo;
//	final JFileChooser fc;
	int coutPiTotal = 0;
	int Niveau = 1;
	boolean isRAZ = false;
	JPanel jpg;
	GridBagLayout gbl;
	GridBagConstraints gbc;
	JTabbedPane jtp;

	/******************************************************************************************************
	 * MISE EN PAGE
	 ******************************************************************************************************/

	JTextArea log;

	//final JFileChooser fc;
	final JDialog dialog;

	File file;

	JFrame gui;

	public CreationTroll(JFrame g) {
		super();
		dialog = new JDialog(gui, "Créer un troll");
		dialog.setContentPane(this);
//		dialog.setSize(new Dimension(710, 460));
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(g);
		amelioPlusInit = new JLabel[caracs.length];
		piInvestis = new JLabel[caracs.length];
		prochaineAmelio = new JLabel[caracs.length];

		caracSpinner = new JSpinner[7];
		gui = g;
		GridBagLayout Gridbag = new GridBagLayout();

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new java.awt.Insets(1, 1, 1, 1);
		setLayout(Gridbag);

		c.gridx = 0;
		c.gridwidth = 4;
		JLabel jl = new JLabel("Création de Personnage", null,
				SwingConstants.CENTER);
		jl.setFont(jl.getFont().deriveFont(jl.getFont().getSize2D()+10));
		Gridbag.setConstraints(jl, c);
		add(jl);
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		jtp = new JTabbedPane();

		// informations générales du troll	

		c.gridy = 1;
		c.gridx = 0;
		jl = new JLabel("Nom du troll : ", null, SwingConstants.RIGHT);
		Gridbag.setConstraints(jl, c);
		//	jl.setMinimumSize(new Dimension(200,13));
		add(jl);
		c.gridx = 1;
		nomField = new JTextField(30);
		//	nomField.setPreferredSize(new Dimension(100,20));
		Gridbag.setConstraints(nomField, c);
		nomField.setColumns(10);
		add(nomField);
		c.gridx = 2;
		jl = new JLabel("Numéro du troll : ", null, SwingConstants.RIGHT);
		Gridbag.setConstraints(jl, c);
		//	jl.setMinimumSize(new Dimension(200,13));
		add(jl);
		c.gridx = 3;
		numeroField = new JTextField(10);
		//	numeroField.setPreferredSize(new Dimension(80,20));
		Gridbag.setConstraints(numeroField, c);
		numeroField.setColumns(10);
		add(numeroField);

		/*************************************************************************************************
		 * total des PI dépensés, et niveau du troll
		 *************************************************************************************************/

		c.gridy = 2;

		c.gridx = 0;
		piTotalLabelTitre = new JLabel("Total des PI dépensés : ", null,
				SwingConstants.RIGHT);
		Gridbag.setConstraints(piTotalLabelTitre, c);
		//	piTotalLabelTitre.setMinimumSize(new Dimension(200,13));
		add(piTotalLabelTitre);

		c.gridx = 1;
		piTotalLabel = new JLabel("0", null, SwingConstants.RIGHT);
		Gridbag.setConstraints(piTotalLabel, c);
		//	piTotalLabel.setMinimumSize(new Dimension(200,13));
		add(piTotalLabel);

		c.gridx = 2;
		niveauLabelTitre = new JLabel("Niveau du Troll : ", null,
				SwingConstants.RIGHT);
		Gridbag.setConstraints(niveauLabelTitre, c);
		//	niveauLabelTitre.setMinimumSize(new Dimension(200,13));
		add(niveauLabelTitre);

		c.gridx = 3;
		niveauLabel = new JLabel("1", null, SwingConstants.RIGHT);
		Gridbag.setConstraints(niveauLabel, c);
		//	niveauLabel.setMinimumSize(new Dimension(200,13));
		add(niveauLabel);

		// ajout du jtabbedPAne

		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 4;
		Gridbag.setConstraints(jtp, c);
		jtp.setMinimumSize(new Dimension(620,500));
		add(jtp);
		c.gridwidth = 1;

		/***********************************************************************************************
		 * race (combobox) 
		 ***********************************************************************************************/
		JPanel jpg = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new java.awt.Insets(1, 1, 1, 1);
		jpg.setLayout(gbl);
		//add(jpg,c);
		//

		gbc.gridwidth = 1;
		gbc.gridy = 0;
		gbc.gridx = 0;
		jl = new JLabel("Race : ", null, SwingConstants.LEFT);
		gbl.setConstraints(jl, gbc);
		//	jl.setMinimumSize(new Dimension(200,10));
		jpg.add(jl);

		gbc.gridx = 1;
		raceCombo = new JComboBox(racesArray);
		raceCombo.addActionListener(this);
		gbl.setConstraints(raceCombo, gbc);
		jpg.add(raceCombo);
		/*	
		 for (int i=0;i<4;i++) {
		 raceButton[i] = new JRadioButton(racesArray[i]);
		 raceButton[i].setMnemonic(KeyEvent.VK_R);
		 raceButton[i].setActionCommand(racesArray[i]);
		 raceButton[i].addActionListener(this);
		 c.gridx=i+1;
		 Gridbag.setConstraints(raceButton[i], c);
		 add(raceButton[i]);
		 }*/

		/***********************************************************************************************
		 * caracs du troll	
		 ***********************************************************************************************/

		for (int i = 0; i < titresCarac.length; i++) {
			gbc.gridx = i;
			gbc.gridy = 2;
			jl = new JLabel(titresCarac[i], null, SwingConstants.LEFT);
			gbl.setConstraints(jl, gbc);
			jpg.add(jl);
		}
		for (int i = 0; i < caracs.length; i++) {
			gbc.gridy = i + 3;
			
			//carac
			gbc.gridx = 0;
			jl = new JLabel(caracs[i] + " : ", null, SwingConstants.CENTER);
			gbl.setConstraints(jl, gbc);
			jpg.add(jl);

			//nb amélios
			gbc.gridx = 1;
			SpinnerModel caracmodel = new SpinnerNumberModel(0, 0, null, 1);
			caracSpinner[i] = new JSpinner(caracmodel);
			JFormattedTextField ftf = getTextField(caracSpinner[i]);
			ftf.setColumns(3);
			caracSpinner[i].addChangeListener(this);
			gbl.setConstraints(caracSpinner[i], gbc);
			jpg.add(caracSpinner[i]);

			//valeurs total : initial + amélios
			gbc.gridx = 2;
			amelioPlusInit[i] = new JLabel(nbInitCaracs[i] + " "
					+ uniteCaracs[i], null, SwingConstants.CENTER);
			gbl.setConstraints(amelioPlusInit[i], gbc);
			jpg.add(amelioPlusInit[i]);

			// PI investis
			gbc.gridx = 3;
			piInvestis[i] = new JLabel("0", null, SwingConstants.CENTER);
			gbl.setConstraints(piInvestis[i], gbc);
			jpg.add(piInvestis[i]);

			//Pi prochaine amélio
			gbc.gridx = 4;
			prochaineAmelio[i] = new JLabel(Integer
					.toString(piInitCoutCaracs[i]), null, SwingConstants.CENTER);
			gbl.setConstraints(prochaineAmelio[i], gbc);
			jpg.add(prochaineAmelio[i]);
		}

		/*************************************************************************************************
		 * intercalaire
		 *************************************************************************************************/
		gbc.gridy = 10;
		gbc.gridx = 0;
		jl = new JLabel("", null, SwingConstants.CENTER);
		gbl.setConstraints(jl, gbc);
		jl.setMinimumSize(new Dimension(200, 10));
		jpg.add(jl);

		/*************************************************************************************************
		 * Mouches
		 *************************************************************************************************/

		gbc.fill = GridBagConstraints.NONE;
		gbc.gridy = 3;

		for (int i = 0; i < mouchesFields.length; i++) {
			gbc.gridx = 5;
			jl = new JLabel(mouchesFieldsText[i] + " : ", null,
					SwingConstants.LEFT);
			gbl.setConstraints(jl, gbc);
			jpg.add(jl);
			gbc.gridx++;
			gbl.setConstraints(mouchesFields[i], gbc);
			jpg.add(mouchesFields[i]);
			mouchesFields[i].setText("0");
			gbc.gridy++;
		}

		/*************************************************************************************************
		 * intercalaire
		 *************************************************************************************************/
		gbc.gridy = 19;
		gbc.gridx = 0;
		jl = new JLabel("", null, SwingConstants.CENTER);
		gbl.setConstraints(jl, gbc);
		jl.setMinimumSize(new Dimension(200, 10));
		jpg.add(jl);
		gbc.gridy = 20;
		gbc.gridx = 0;
		jl = new JLabel("", null, SwingConstants.CENTER);
		gbl.setConstraints(jl, gbc);
		jl.setMinimumSize(new Dimension(200, 10));
		jpg.add(jl);

		/*************************************************************************************************
		 * RM / MM
		 *************************************************************************************************/
		gbc.gridy = 0;
		gbc.gridx = 2;
		jl = new JLabel("MM : ", null, SwingConstants.CENTER);
		gbl.setConstraints(jl, gbc);
		//    	jl.setMinimumSize(new Dimension(200,10));
		jpg.add(jl);
		gbc.gridx = 3;
		mmField = new JTextField(10);
		//    	mmField.setPreferredSize(new Dimension(80,20));
		gbl.setConstraints(mmField, gbc);
		mmField.setColumns(10);
		jpg.add(mmField);
		gbc.gridx = 4;
		jl = new JLabel("RM : ", null, SwingConstants.CENTER);
		gbl.setConstraints(jl, gbc);
		//    	jl.setMinimumSize(new Dimension(200,10));
		jpg.add(jl);
		gbc.gridx = 5;
		rmField = new JTextField(10);
		gbl.setConstraints(rmField, gbc);
		rmField.setColumns(10);
		//    	rmField.setPreferredSize(new Dimension(80,20));
		jpg.add(rmField);

		jtp.addTab("Caracs", jpg);


		/*************************************************************************************************
		 * Compétences checkbox
		 *************************************************************************************************/

		jpg = new JPanel();
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		gbc.insets = new java.awt.Insets(2, 2, 2, 2);
		gbc.anchor = GridBagConstraints.LINE_START;
		jpg.setLayout(gbl);
		//	add(jpg,c);
		//	

		jtp.addTab("Compétences", jpg);

		//    	Gridbag.setConstraints(jl, c);
		//    	jl.setMinimumSize(new Dimension(200,13));
		//    	add(jl);   	
		gbc.gridwidth = 1;
		
		for(int i=0;i < competences_reservees.length; i++)
		{
			gbc.gridx = 0;
			gbc.gridy = i;
			compCheckBox_reservees[i] = new JCheckBox(competences_reservees[i]);
			compCheckBox_reservees[i].addActionListener(this);
			gbl.setConstraints(compCheckBox_reservees[i], gbc);
			jpg.add(compCheckBox_reservees[i]);
			gbc.gridx = 1;
			pourcentComp_reservees[i] = new JTextField(2);
			pourcentComp_reservees[i].setEnabled(false);
			gbl.setConstraints(pourcentComp_reservees[i], gbc);
			pourcentComp_reservees[i].setColumns(2);
			jpg.add(pourcentComp_reservees[i]);
			compCheckBox_reservees[i].setEnabled(false);
			gbc.gridx = 2;
			JLabel jlab = new JLabel("%");
			gbl.setConstraints(jlab, gbc);
			jpg.add(jlab);
		}
		
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.gridheight = 4;
		
		JSeparator jsep = new JSeparator(SwingConstants.VERTICAL);
		gbl.setConstraints(jsep, gbc);
		jsep.setMinimumSize(new Dimension(3,100));
		jsep.setPreferredSize(new Dimension(3,100));
		jpg.add(jsep);
		
		gbc.gridx = 7;
		
		jsep = new JSeparator(SwingConstants.VERTICAL);
		gbl.setConstraints(jsep, gbc);
		jsep.setMinimumSize(new Dimension(3,100));
		jsep.setPreferredSize(new Dimension(3,100));
		jpg.add(jsep);

		gbc.gridx = 3;
		gbc.gridy = -1;
		gbc.gridheight = 1;

		for (int i = 0; i < competences.length; i++) {
			if (i%2==0) {
				gbc.gridy++;
				gbc.gridx = 4;
			}
			else
				gbc.gridx++;
			compCheckBox[i] = new JCheckBox(competences[i]);
			//		compCheckBox[i].setMinimumSize(new Dimension(160,13));
			compCheckBox[i].addActionListener(this);
			gbl.setConstraints(compCheckBox[i], gbc);
			jpg.add(compCheckBox[i]);
			gbc.gridx++;
			pourcentComp[i] = new JTextField(2);
			//		jl.setMinimumSize(new Dimension(20,10));
			pourcentComp[i].setEnabled(false);
			gbl.setConstraints(pourcentComp[i], gbc);
			pourcentComp[i].setColumns(2);
			jpg.add(pourcentComp[i]);
			gbc.gridx++;
			JLabel jlab = new JLabel("%");
			gbl.setConstraints(jlab, gbc);
			jpg.add(jlab);
			gbc.gridx++;
		}
		
		gbc.gridx = 11;
		gbc.gridy = 0;
		gbc.gridheight = 4;
		
		jsep = new JSeparator(SwingConstants.VERTICAL);
		gbl.setConstraints(jsep, gbc);
		jsep.setMinimumSize(new Dimension(3,100));
		jsep.setPreferredSize(new Dimension(3,100));
		jpg.add(jsep);

		gbc.gridheight = 1;
		
		for (int i = 0; i < competences_niveau_sup.length; i++) {
			gbc.gridx = 12;
			gbc.gridy = i;
			compCheckBox_niveau_sup[i] = new JCheckBox(competences_niveau_sup[i]);
			//		compCheckBox[i].setMinimumSize(new Dimension(160,13));
			compCheckBox_niveau_sup[i].addActionListener(this);
			gbl.setConstraints(compCheckBox_niveau_sup[i], gbc);
			jpg.add(compCheckBox_niveau_sup[i]);
			gbc.gridx++;
			JLabel jlab = new JLabel("(Niveau ");
			gbl.setConstraints(jlab, gbc);
			jpg.add(jlab);
			gbc.gridx++;
			
			SpinnerModel caracmodel = new SpinnerNumberModel(1, 1, 5, 1);
			compSpinner[i] = new JSpinner(caracmodel);
			JFormattedTextField ftf = getTextField(compSpinner[i]);
			ftf.setColumns(1);
			compSpinner[i].addChangeListener(this);
			compSpinner[i].setEnabled(false);
			gbl.setConstraints(compSpinner[i], gbc);
			jpg.add(compSpinner[i]);
			gbc.gridx++;
			jlab = new JLabel(")");
			gbl.setConstraints(jlab, gbc);
			jpg.add(jlab);
			gbc.gridx++;
			
			pourcentComp_niveau_sup[i] = new JTextField(2);
			//		jl.setMinimumSize(new Dimension(20,10));
			pourcentComp_niveau_sup[i].setEnabled(false);
			gbl.setConstraints(pourcentComp_niveau_sup[i], gbc);
			pourcentComp_niveau_sup[i].setColumns(2);
			jpg.add(pourcentComp_niveau_sup[i]);
			gbc.gridx++;
			jlab = new JLabel("%");
			gbl.setConstraints(jlab, gbc);
			jpg.add(jlab);
			gbc.gridx++;
		}
		
		compCheckBox_reservees[0].setSelected(true);
		pourcentComp_reservees[0].setEnabled(true);
		pourcentComp_reservees[0].setText("90");


		/*************************************************************************************************
		 * sorts checkbox
		 *************************************************************************************************/
		gbc.gridy = 35;
		gbc.gridx = 0;
		gbc.gridwidth = 7;

		jpg = new JPanel();
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		gbc.insets = new java.awt.Insets(1, 1, 1, 1);
		gbc.anchor = GridBagConstraints.LINE_START;
		jpg.setLayout(gbl);
		//	add(jpg,c);

		jtp.addTab("Sorts", jpg);

		gbl.setConstraints(jl, gbc);
		gbc.gridwidth = 1;

		for (int i = 0; i < sorts.length; i++) {
			if ((i / 3) * 3 == i) {
				gbc.gridy++;
				gbc.gridx = 0;
			}
			sortCheckBox[i] = new JCheckBox(sorts[i]);
			//		sortCheckBox[i].setMinimumSize(new Dimension(200,13));
			sortCheckBox[i].addActionListener(this);
			gbl.setConstraints(sortCheckBox[i], gbc);
			jpg.add(sortCheckBox[i]);
			gbc.gridx++;
			pourcentSort[i] = new JTextField(2);
			//		jl.setMinimumSize(new Dimension(20,10));
			pourcentSort[i].setEnabled(false);
			gbl.setConstraints(pourcentSort[i], gbc);
			pourcentSort[i].setColumns(2);
			jpg.add(pourcentSort[i]);
			gbc.gridx++;
			if (i < 4) {
				sortCheckBox[i].setEnabled(false);
			}
		}
		sortCheckBox[0].setSelected(true);
		pourcentSort[0].setEnabled(true);
		pourcentSort[0].setText("80");
		gbc.anchor = GridBagConstraints.LINE_START;



		/********************************************************************************************************
		 * Equipement
		 ********************************************************************************************************/
		jpg = new JPanel();
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		gbc.insets = new java.awt.Insets(1, 1, 1, 1);
		gbc.anchor = GridBagConstraints.LINE_START;
		jpg.setLayout(gbl);
		//	add(jpg,c);
		//	

		jtp.addTab("Equipement", jpg);

		gbc.gridy = 0;
		gbc.gridx = 0;
//
//		jl = new JLabel("Equipement :", null, SwingConstants.LEFT);
//		gbl.setConstraints(jl, gbc);
//		jl.setMinimumSize(new Dimension(200, 13));
//		jpg.add(jl);

		gbc.gridwidth = 3;
		gbc.gridy = 0;
		MyTableModel model = new MyTableModel();
		equipementTable = new JTable(model);
		model.addColumn("Id");
		model.addColumn("Nom");
		model.addColumn("Type");
		model.addColumn("Caractérisques");
		model.addColumn("Equipé");
		
		equipementTable.getColumnModel().getColumn(0).setPreferredWidth(70);
		equipementTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		equipementTable.getColumnModel().getColumn(1).setCellRenderer(new MultiLineCellRenderer());
		equipementTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		equipementTable.getColumnModel().getColumn(3).setPreferredWidth(340);
		equipementTable.getColumnModel().getColumn(3).setCellRenderer(new MultiLineCellRenderer());
		equipementTable.getColumnModel().getColumn(4).setPreferredWidth(50);
		equipementTable.setPreferredScrollableViewportSize(new Dimension(650,200));
		equipementTable.setMinimumSize(new Dimension(600,200));

		JScrollPane scrollPane = new JScrollPane(equipementTable);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		gbl.setConstraints(scrollPane, gbc);
		jpg.add(scrollPane);
		
		gbc.gridwidth = 3;
		gbc.anchor=GridBagConstraints.CENTER;
		gbc.gridy = 1;

		JPanel jp=new JPanel();
		jp.setLayout(new BoxLayout(jp,BoxLayout.LINE_AXIS));
		jp.add(Box.createHorizontalGlue());
		ajoutEquip = new JButton("Ajouter");
		ajoutEquip.addActionListener(this);
		jp.add(ajoutEquip);
		jp.add(Box.createHorizontalStrut(5));
		modifyEquip = new JButton("Modifier");
		modifyEquip.addActionListener(this);
		jp.add(modifyEquip);
		jp.add(Box.createHorizontalStrut(5));
		supprEquip = new JButton("Supprimer");
		supprEquip.addActionListener(this);
		jp.add(supprEquip);
		jp.add(Box.createHorizontalGlue());
		jpg.add(jp,gbc);
		


		/*************************************************************************************************
		 * boutons
		 *************************************************************************************************/

		c.gridy = 4;

		c.gridx = 0;
		c.gridwidth = 5;
		c.anchor = GridBagConstraints.CENTER;
		jpg = new JPanel();
		jpg.setLayout(new BoxLayout(jpg, BoxLayout.LINE_AXIS));
		jpg.add(Box.createHorizontalGlue());
		cancelButton = new JButton("Annuler");
		cancelButton.addActionListener(this);
		jpg.add(cancelButton);
		jpg.add(Box.createHorizontalStrut(300));

		loadButton = new JButton("Charger");
		loadButton.addActionListener(this);
		jpg.add(loadButton);
		jpg.add(Box.createHorizontalGlue());
		add(jpg,c);
		
		c.gridy = 5;
		c.gridx = 0;
		c.gridwidth = 5;
		c.anchor = GridBagConstraints.CENTER;
		jpg = new JPanel();
		jpg.setLayout(new BoxLayout(jpg, BoxLayout.LINE_AXIS));
		jpg.add(Box.createHorizontalGlue());
		executeButton = new JButton("Enregistrer");
		executeButton.addActionListener(this);
		jpg.add(executeButton);
		jpg.add(Box.createHorizontalStrut(5));
		jl = new JLabel("Enregistrer dans : ", null, SwingConstants.RIGHT);
		jpg.add(jl);
		jpg.add(Box.createHorizontalStrut(5));
		saveField = new JTextField(20);
		jpg.add(saveField);
		jpg.add(Box.createHorizontalStrut(5));
		saveButton = new JButton("Parcourir");
		saveButton.addActionListener(this);
		jpg.add(saveButton);
		jpg.add(Box.createHorizontalGlue());
		add(jpg,c);
		c.gridwidth = 1;
		
		//Create a file chooser
		//fc = new JFileChooser();
		//MHAFileFilter filter = new MHAFileFilter("mha","Fiche de perso pour Mountyhall Arena");
		//fc.setFileFilter(filter);
		//	filter.addExtension("gif");
		//	filter.setDescription("Perso Mountyhall Arena");
		//fc.setFileFilter(filter);

		/********************************************************************************************************
		 * Avatar
		 ********************************************************************************************************/
		jpg = new JPanel();
		jpg.setLayout(new BoxLayout(jpg, BoxLayout.PAGE_AXIS));
		jtp.addTab("Avatar", jpg);

		//Set up the picture.
		avatar = new JLabel();
		avatar.setFont(avatar.getFont().deriveFont(Font.ITALIC));
		avatar.setAlignmentX(Component.CENTER_ALIGNMENT);;
		//updateLabel(petStrings[petList.getSelectedIndex()]);
		//avatar.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
		avatar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	
		//The preferred size is hard-coded to be the width of the
		//widest image and the height of the tallest image + the border.
		//A real program would compute this.
		avatar.setPreferredSize(new Dimension(120, 120+10));
		avatar.setMinimumSize(new Dimension(120, 120+10));
		avatar.setMaximumSize(new Dimension(120, 120+10));
		jpg.add(Box.createVerticalGlue());
		jpg.add(avatar);
		jpg.add(Box.createVerticalStrut(5));
		avatarButton = new JButton("Changer d'avatar");
		avatarButton.addActionListener(this);
		avatarButton.setAlignmentX(Component.CENTER_ALIGNMENT);;
		jpg.add(avatarButton);
		
		jpg.add(Box.createVerticalGlue());

		
		
		dialog.pack();
		dialog.setSize(dialog.getWidth()+10,dialog.getHeight()+10);
		dialog.setVisible(true);
	}

	/**************************************************************************************************
	 * Fonctions
	 **************************************************************************************************/

	/******************************
	 * remise é Zéro
	 ******************************/

	private void remiseAZero() {
		for (int i = 0; i < caracs.length; i++) {
			amelioPlusInit[i].setText(nbInitCaracs[i] + " " + uniteCaracs[i]);
			prochaineAmelio[i].setText(Integer.toString(piInitCoutCaracs[i]));
			nbInitCaracsParRace[i] = nbInitCaracs[i];
			piInitCoutCaracsParRace[i] = piInitCoutCaracs[i];
			piInvestis[i].setText("0");
			caracSpinner[i].setValue(0);
			mouchesFields[i].setText("0");
		}
		for (int i = 0; i < competences.length; i++) {
			compCheckBox[i].setSelected(false);
			pourcentComp[i].setText("");
		}
		for (int i = 0; i < competences_reservees.length; i++) {
			compCheckBox_reservees[i].setSelected(false);
			pourcentComp_reservees[i].setText("");
			pourcentComp_reservees[i].setEnabled(false);
		}
		for (int i = 0; i < competences_niveau_sup.length; i++) {
			compCheckBox_niveau_sup[i].setSelected(false);
			pourcentComp_niveau_sup[i].setText("");
		}
		for (int i = 0; i < sorts.length; i++) {
			sortCheckBox[i].setSelected(false);
			pourcentSort[i].setText("");
			if (i < 4) {
				pourcentSort[i].setEnabled(false);
			}
		}
		while(((DefaultTableModel) equipementTable.getModel()).getRowCount()!=0)
			((DefaultTableModel) equipementTable.getModel()).removeRow(0);
		while(equipements.size()!=0)
			equipements.remove(0);
		updatePI();
	}

	/*******************************************************************
	 * choix d'une race, sélection des comps et des sorts
	 *******************************************************************/

	public void actionPerformed(ActionEvent e) {

		/*******************************************************************
		 * choix d'une race
		 *******************************************************************/

		for (int i = 0; i < racesArray.length && e.getSource() == raceCombo; i++) {
			if (raceCombo.getSelectedItem() == racesArray[i]) {
				isRAZ = true;
				remiseAZero();
				nbInitCaracsParRace[caracSpecialisee[i]] = 4 * nbInitCaracs[caracSpecialisee[i]] / 3;
				piInitCoutCaracsParRace[caracSpecialisee[i]] = 12;
				amelioPlusInit[caracSpecialisee[i]].setText(4
						* nbInitCaracs[caracSpecialisee[i]] / 3 + " "
						+ uniteCaracs[caracSpecialisee[i]]);
				prochaineAmelio[caracSpecialisee[i]].setText(""
						+ piInitCoutCaracsParRace[caracSpecialisee[i]]);
/*				coutPiTotal = 0;
				piTotalLabel.setText(Integer.toString(coutPiTotal));
				Niveau = (int) (Math.floor((-1 + Math
						.sqrt(9 + 4 * coutPiTotal / 5)) / 2));
				niveauLabel.setText(Integer.toString(Niveau));*/
				compCheckBox_reservees[compSpecialisee[i]].setSelected(true);
				pourcentComp_reservees[compSpecialisee[i]].setEnabled(true);
				if (pourcentComp_reservees[sortSpecialise[i]].getText().length() == 0)
					pourcentComp_reservees[sortSpecialise[i]].setText("90");
				sortCheckBox[sortSpecialise[i]].setSelected(true);
				pourcentSort[sortSpecialise[i]].setEnabled(true);
				if (pourcentSort[sortSpecialise[i]].getText().length() == 0)
					pourcentSort[sortSpecialise[i]].setText("80");
				isRAZ = false;
				updatePI();
			}
		}

		/*******************************************************************
		 * sélection des comps
		 *******************************************************************/

		for (int i = 0; i < competences.length; i++) {
			if (e.getSource() == compCheckBox[i]) {
				if (compCheckBox[i].isSelected()) {
					pourcentComp[i].setEnabled(true);
					if (pourcentComp[i].getText().length() == 0)
						pourcentComp[i].setText("90");
				} else {
					pourcentComp[i].setEnabled(false);
				}
				updatePI();
			}
		}
		
		/*******************************************************************
		 * sélection des comps
		 *******************************************************************/

		for (int i = 0; i < competences_niveau_sup.length; i++) {
			if (e.getSource() == compCheckBox_niveau_sup[i]) {
				if (compCheckBox_niveau_sup[i].isSelected()) {
					pourcentComp_niveau_sup[i].setEnabled(true);
					compSpinner[i].setEnabled(true);
					if (pourcentComp_niveau_sup[i].getText().length() == 0)
						pourcentComp_niveau_sup[i].setText("90");
				} else {
					pourcentComp_niveau_sup[i].setEnabled(false);
					compSpinner[i].setEnabled(false);
				}
				updatePI();
			}
		}
		
		/*******************************************************************
		 * sélection des comps
		 *******************************************************************/

		for (int i = 0; i < compSpinner.length; i++) {
			if (e.getSource() == compSpinner[i]) {
				updatePI();
			}
		}

		/*******************************************************************
		 * sélection des sorts
		 *******************************************************************/

		for (int i = 0; i < sorts.length; i++) {
			if (e.getSource() == sortCheckBox[i]) {
				if (sortCheckBox[i].isSelected()) {
					pourcentSort[i].setEnabled(true);
					if (pourcentSort[i].getText().length() == 0)
						pourcentSort[i].setText("80");
				} else {
					//pourcentSort[i].setText("");
					pourcentSort[i].setEnabled(false);
				}
			}
		}

		/*********************************************************************
		 * boutons
		 ********************************************************************/

		//boutons globaux : annuler, enregistrer, choisir un fichier
		
		if (e.getSource() == cancelButton) {
			dialog.setVisible(false);
			dialog.dispose();
		}

		//charger un avatar
		if(e.getSource() == avatarButton){
			Vector <String> lignes = new Vector <String> ();
			String valeur;
			JFileChooser fc2 ;
			if(MHAGUI.lastDirectory.length()==0)
				fc2 = new JFileChooser(new File ("."));
			else
				fc2 = new JFileChooser(MHAGUI.lastDirectory);
			fc2.setFileFilter(new ImageFilter());
			fc2.setAccessory(new ImagePreview(fc2));
			int returnVal = fc2.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				int count=0;
				byte[]buf = new byte[2000000];
				try {
					file = fc2.getSelectedFile();
					MHAGUI.lastDirectory = file.getCanonicalPath();
					FileInputStream fr =  new FileInputStream(file);
					BufferedInputStream bufferin = new BufferedInputStream(fr);
					count = bufferin.read(buf);
					fr.close();
				} catch (Exception ieo) {
					JOptionPane.showMessageDialog(dialog, "Le fichier "+file.getName()+" ne peut pas étre lu", "Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (count <= 0) {
					JOptionPane.showMessageDialog(dialog, "Le fichier "+file.getName()+" est vide", "Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
				ImageIcon inImage=new ImageIcon(Toolkit.getDefaultToolkit().createImage(buf));
				int maxDim = 120;
				double scale=(double) maxDim / (double) inImage.getImage().getHeight(null);
				if (inImage.getImage().getWidth(null) > inImage.getImage().getHeight(null))
				{
					scale = (double) maxDim / (double) inImage.getImage().getWidth(null);
				}
				// Determine size of new image.
				//One of them
				// should equal maxDim.
				int scaledW = (int) (scale * inImage.getImage().getWidth(null));
				int scaledH = (int) (scale * inImage.getImage().getHeight(null));
				ImageIcon img = new ImageIcon(inImage.getImage().getScaledInstance(scaledW , scaledH, Image.SCALE_SMOOTH));
				avatar.setIcon(img);
				//System.out.println(img.getImageLoadStatus());
				//MediaTracker.COMPLETE
			}
			return;
		}
		
		// charger un troll
		if(e.getSource() == loadButton){
			Vector <String> lignes = new Vector <String> ();
			String valeur;
			JFileChooser fc;
			if(MHAGUI.lastDirectory.length()==0)
				fc = new JFileChooser(new File ("."));
			else
				fc = new JFileChooser(MHAGUI.lastDirectory);
			MHAFileFilter filter = new MHAFileFilter("mha","Fiche de perso pour Mountyhall Arena");
			fc.setFileFilter(filter);
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
						if(ls[0].toLowerCase().equals("addequip") || ls[0].toLowerCase().equals("addtroll") || 
								ls[0].toLowerCase().equals("carac") || ls[0].toLowerCase().equals("addsort") || ls[0].toLowerCase().equals("icontroll") ||
								ls[0].toLowerCase().equals("addcomp") || ls[0].toLowerCase().equals("addmouche"))
						{
							lignes.add(input);
						}
						input = bufferin.readLine();
						if(input!=null)
							input=input.replaceFirst("//.*","");
					}
					if(lignes.size()==0) {
						JOptionPane.showMessageDialog(dialog,
							"Fichier vide", "Erreur",
							JOptionPane.ERROR_MESSAGE);
						return;
					}
					remiseAZero();
					for(int i=0;i<lignes.size();i++){						
						input = lignes.get(i);
						//System.out.println(input);
						String[] ls2 = input.split(" ");
						//System.out.println(ls2[0]);
						if (ls2[0].toLowerCase().equals("addtroll")){
							//addtroll id nom race 
							numeroField.setText(ls2[1]);
							String tmpNomTroll = "";
							for(int j=0;j<ls2.length-3;j++){tmpNomTroll+=ls2[j+2] + " ";}
							nomField.setText(tmpNomTroll.trim());
							raceCombo.setSelectedIndex(Integer.parseInt(ls2[ls2.length-1]));
						}
						else if (ls2[0].toLowerCase().equals("carac")) {
							//carac Niv Att Esq Deg Reg PV Vue Durée_DLA(en minutes) MM_pure RM_pure
							//niveau
							niveauLabel.setText(ls2[1]);

							//caracs simples
							for(int j=0;j<caracs.length-1;j++){
								int valAmelio=Integer.parseInt(ls2[j+2]) - Integer.parseInt(amelioPlusInit[j].getText().substring(0,amelioPlusInit[j].getText().length()-(1 + uniteCaracs[j].length())));
								caracSpinner[j].setValue( valAmelio / incrementCaracs[j]);
							}
														
							//DLA
							int nbDlaAmelio = 0;
							if(Integer.parseInt(ls2[8]) < 555 ) {
								nbDlaAmelio = (int)(10 + (555-Integer.parseInt(ls2[8]))/2.5);
							}
							else {
								nbDlaAmelio = (int)((21 - Math.sqrt((8*Integer.parseInt(ls2[8]) - 4437)/3))/2);
							}
							caracSpinner[6].setValue(nbDlaAmelio);
													
							//MM et RM
							mmField.setText(ls2[9]);
							rmField.setText(ls2[10]);							
						}
						else if (ls2[0].toLowerCase().equals("addcomp")) {
							int nbComp = Integer.parseInt(ls2[1]);
							if(nbComp<=4)
							{
								compCheckBox_reservees[nbComp-1].setSelected(true);
								pourcentComp_reservees[nbComp-1].setText(ls2[2]);
								pourcentComp_reservees[nbComp-1].setEnabled(true);
							}
							else if(nbComp == 5)
							{
								int level = 1;
								if(ls2.length==4)
									level = Integer.parseInt(ls2[3]);
									
								if(level >=Integer.parseInt(compSpinner[0].getValue().toString()))
								{
									compCheckBox_niveau_sup[0].setSelected(true);
									pourcentComp_niveau_sup[0].setText(ls2[2]);
									pourcentComp_niveau_sup[0].setEnabled(true);
									compSpinner[0].setEnabled(true);
									compSpinner[0].setValue(level);
								}
							}
							else if(nbComp == 9 )
							{
								int level = 1;
								if(ls2.length==4)
									level = Integer.parseInt(ls2[3]);
								if(level>=Integer.parseInt(compSpinner[1].getValue().toString()))
								{
									compCheckBox_niveau_sup[1].setSelected(true);
									pourcentComp_niveau_sup[1].setText(ls2[2]);
									pourcentComp_niveau_sup[1].setEnabled(true);
									compSpinner[1].setEnabled(true);
									compSpinner[1].setValue(level);
								}
							}
							else if(nbComp == 13 )
							{
								int level = 1;
								if(ls2.length==4)
									level = Integer.parseInt(ls2[3]);
								if(level>=Integer.parseInt(compSpinner[1].getValue().toString()))
								{
									compCheckBox_niveau_sup[2].setSelected(true);
									pourcentComp_niveau_sup[2].setText(ls2[2]);
									pourcentComp_niveau_sup[2].setEnabled(true);
									compSpinner[2].setEnabled(true);
									compSpinner[2].setValue(level);
								}
							}
							else if(nbComp>5 && nbComp<9)
							{
								compCheckBox[nbComp-6].setSelected(true);
								pourcentComp[nbComp-6].setText(ls2[2]);
								pourcentComp[nbComp-6].setEnabled(true);
							}
							else if(nbComp<13)
							{
								compCheckBox[nbComp-7].setSelected(true);
								pourcentComp[nbComp-7].setText(ls2[2]);
								pourcentComp[nbComp-7].setEnabled(true);
							}
							else if(nbComp<15)
							{
								compCheckBox[nbComp-8].setSelected(true);
								pourcentComp[nbComp-8].setText(ls2[2]);
								pourcentComp[nbComp-8].setEnabled(true);
							}

						}
						else if (ls2[0].toLowerCase().equals("addsort")) {
							sortCheckBox[Integer.parseInt(ls2[1])-1].setSelected(true);
							pourcentSort[Integer.parseInt(ls2[1])-1].setText(ls2[2]);
							pourcentSort[Integer.parseInt(ls2[1])-1].setEnabled(true);
							
						}
						else if (ls2[0].toLowerCase().equals("addequip")) {
							boolean bmm=false,fullbmm=false;
							Equipement equip=null;
							String isEquip="Non";
							try{Integer.parseInt(ls2[18]);Integer.parseInt(ls2[19]);bmm=true;}catch(NumberFormatException ex){}
							try{Integer.parseInt(ls2[21]);Integer.parseInt(ls2[22]);fullbmm=true;}catch(Exception ex){}
							if(fullbmm)
							{
								int idEquip=Integer.parseInt(ls2[1]);
								int typeEquip=Integer.parseInt(ls2[2]);
								int attEquip=Integer.parseInt(ls2[3]);
								int attMEquip=Integer.parseInt(ls2[4]);
								int esqEquip=Integer.parseInt(ls2[5]);
								int esqMEquip=Integer.parseInt(ls2[6]);
								int degEquip=Integer.parseInt(ls2[7]);
								int degMEquip=Integer.parseInt(ls2[8]);
								int dlaEquip=Integer.parseInt(ls2[9]);
								int regEquip=Integer.parseInt(ls2[10]);
								int regMEquip=Integer.parseInt(ls2[11]);
								int pvEquip=Integer.parseInt(ls2[12]);
								int vueEquip=Integer.parseInt(ls2[13]);
								int vueMEquip=Integer.parseInt(ls2[14]);
								int apEquip=Integer.parseInt(ls2[15]);
								int amEquip=Integer.parseInt(ls2[16]);
								int poidsEquip=Integer.parseInt(ls2[19]);
								int mmEquip=Integer.parseInt(ls2[20]);
								int rmEquip=Integer.parseInt(ls2[21]);
								//int equipEquip=Math.abs(Integer.parseInt(liste[19]));
								boolean zoneEquip=false;
								if(ls2[17].equals("1"))
									zoneEquip=true;
								boolean dropEquip=false;
								if(ls2[18].equals("1"))
									dropEquip=true;
								if(ls2[22].equals("1"))
									isEquip="Oui";
								String tmpNomEquip="";
								for(int j=0;j<ls2.length-23;j++){tmpNomEquip += ls2[j+23] + " ";}
								String nomEquip=tmpNomEquip.trim();
								equip=new Equipement(idEquip,nomEquip,typeEquip,attEquip,attMEquip,esqEquip,esqMEquip,degEquip,degMEquip,dlaEquip,regEquip,regMEquip,vueEquip,vueMEquip,pvEquip,apEquip,amEquip,mmEquip,rmEquip,zoneEquip,dropEquip,poidsEquip);
								equipements.add(equip);
							}
							else if(!bmm)
							{
								int idEquip=Integer.parseInt(ls2[1]);
								int typeEquip=Integer.parseInt(ls2[2]);
								int attEquip=Integer.parseInt(ls2[3]);
								int esqEquip=Integer.parseInt(ls2[4]);
								int degEquip=Integer.parseInt(ls2[5]);
								int dlaEquip=Integer.parseInt(ls2[6]);
								int regEquip=Integer.parseInt(ls2[7]);
								int pvEquip=Integer.parseInt(ls2[8]);
								int vueEquip=Integer.parseInt(ls2[9]);
								int apEquip=Integer.parseInt(ls2[10]);
								int amEquip=Integer.parseInt(ls2[11]);
								int poidsEquip=Integer.parseInt(ls2[14]);
								int mmEquip=Integer.parseInt(ls2[15]);
								int rmEquip=Integer.parseInt(ls2[16]);
								//int equipEquip=Math.abs(Integer.parseInt(liste[17]));
								boolean zoneEquip=false;
								if(ls2[12].equals("1"))
									zoneEquip=true;
								boolean dropEquip=false;
								if(ls2[13].equals("1"))
									dropEquip=true;
								if(ls2[17].equals("1"))
									isEquip="Oui";
								String tmpNomEquip="";
								for(int j=0;j<ls2.length-18;j++){tmpNomEquip += ls2[j+18] + " ";}
								String nomEquip=tmpNomEquip.trim();
								equip=new Equipement(idEquip,nomEquip,typeEquip,attEquip,esqEquip,degEquip,dlaEquip,regEquip,vueEquip,pvEquip,apEquip,amEquip,mmEquip,rmEquip,zoneEquip,dropEquip,poidsEquip);
								equipements.add(equip);
							}
							else
							{
								int idEquip=Integer.parseInt(ls2[1]);
								int typeEquip=Integer.parseInt(ls2[2]);
								int attEquip=Integer.parseInt(ls2[3]);
								int attMEquip=Integer.parseInt(ls2[4]);
								int esqEquip=Integer.parseInt(ls2[5]);
								int degEquip=Integer.parseInt(ls2[6]);
								int degMEquip=Integer.parseInt(ls2[7]);
								int dlaEquip=Integer.parseInt(ls2[8]);
								int regEquip=Integer.parseInt(ls2[9]);
								int pvEquip=Integer.parseInt(ls2[10]);
								int vueEquip=Integer.parseInt(ls2[11]);
								int apEquip=Integer.parseInt(ls2[12]);
								int amEquip=Integer.parseInt(ls2[13]);
								int poidsEquip=Integer.parseInt(ls2[16]);
								int mmEquip=Integer.parseInt(ls2[17]);
								int rmEquip=Integer.parseInt(ls2[18]);
								//int equipEquip=Math.abs(Integer.parseInt(liste[19]));
								boolean zoneEquip=false;
								if(ls2[14].equals("1"))
									zoneEquip=true;
								boolean dropEquip=false;
								if(ls2[15].equals("1"))
									dropEquip=true;
								if(ls2[19].equals("1"))
									isEquip="Oui";
								String tmpNomEquip="";
								for(int j=0;j<ls2.length-20;j++){tmpNomEquip += ls2[j+20] + " ";}
								String nomEquip=tmpNomEquip.trim();
								equip=new Equipement(idEquip,nomEquip,typeEquip,attEquip,attMEquip,esqEquip,degEquip,degMEquip,dlaEquip,regEquip,vueEquip,pvEquip,apEquip,amEquip,mmEquip,rmEquip,zoneEquip,dropEquip,poidsEquip);
								equipements.add(equip);
							}
							((DefaultTableModel) equipementTable.getModel()).addRow(new Object[] {equip.getId(),equip.getName(),Equipement.formateType(equip.getType()),equip.getDescr(),isEquip});
	
						}
						else if (ls2[0].toLowerCase().equals("icontroll")) {
							BigInteger bi = new BigInteger(ls2[1], 16);
							byte[] data = bi.toByteArray();
							ImageIcon inImage=new ImageIcon(Toolkit.getDefaultToolkit().createImage(data));
							avatar.setIcon(inImage);
						}
						else if (ls2[0].toLowerCase().equals("addmouche")) {
							int numMouches;
							if(Integer.parseInt(ls2[1]) == 8){
								numMouches=6;
							}
							else {
								numMouches=Integer.parseInt(ls2[1]);
							}
							mouchesFields[numMouches].setText(Integer.toString((Integer.parseInt(mouchesFields[numMouches].getText())+1)));
						}
						
					}
					updatePI();
					jtp.setSelectedIndex(0);
					
				}
				catch (Exception e4){
					System.out.println(e4.getMessage() + " : " + e4.toString());
					e4.printStackTrace();
					JOptionPane.showMessageDialog(dialog,
							"Fichier incorrect ou inexistant", "Erreur",
							JOptionPane.ERROR_MESSAGE);
						return;					
				}

				
			}
		}
		
		//choisir le fichier de sauvegarde
		
		if (e.getSource() == saveButton) {
			JFileChooser fc;
			if(MHAGUI.lastDirectory.length()==0)
				fc = new JFileChooser(new File ("."));
			else
				fc = new JFileChooser(MHAGUI.lastDirectory);
			MHAFileFilter filter = new MHAFileFilter("mha","Fiche de perso pour Mountyhall Arena");
			fc.setFileFilter(filter);
			int returnVal = fc.showDialog(this,"Choisir");
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				try { MHAGUI.lastDirectory = file.getCanonicalPath(); } catch(Exception ex){}
				saveField.setText(file.getAbsolutePath());
				if (!saveField.getText().endsWith(".mha")) {
					saveField.setText(saveField.getText() + ".mha");
				}
			}
		}
		
		// sauvegarder
		
		if (e.getSource() == executeButton) {
			int num;
			if (nomField.getText().length() == 0
					|| numeroField.getText().length() == 0
					|| saveField.getText().length() == 0) {
				JOptionPane.showMessageDialog(dialog,
						"Merci de remplir tous les champs", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				num = Integer.parseInt(numeroField.getText());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(dialog,
						"Le numéro de troll doit étre un nombre", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			creeTroll();
		}
		
		// modifier équipement
		
		if(e.getSource() == modifyEquip)
		try {
			if(equipementTable.getSelectedRowCount()!=1)
			{
				JOptionPane.showMessageDialog(dialog,
						"Veuillez sélectionner un équipement à modifier", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			int i=equipementTable.getSelectedRow();
			String [] ajoutEquip;
			AjoutEquipement ae=new AjoutEquipement(dialog,equipements.elementAt(i),equipementTable.getValueAt(i, 4).toString().equals("Oui"));
			ajoutEquip=ae.getEquip();
			if(ajoutEquip[0] == null || ajoutEquip[0].length() == 0)
				return;
			int idEquip=Integer.parseInt(ajoutEquip[1]);
			int typeEquip=Integer.parseInt(ajoutEquip[2]);
			int attEquip=Integer.parseInt(ajoutEquip[3]);
			int attMEquip=Integer.parseInt(ajoutEquip[4]);
			int esqEquip=Integer.parseInt(ajoutEquip[5]);
			int esqMEquip=Integer.parseInt(ajoutEquip[6]);
			int degEquip=Integer.parseInt(ajoutEquip[7]);
			int degMEquip=Integer.parseInt(ajoutEquip[8]);
			int dlaEquip=Integer.parseInt(ajoutEquip[9]);
			int regEquip=Integer.parseInt(ajoutEquip[10]);
			int regMEquip=Integer.parseInt(ajoutEquip[11]);
			int pvEquip=Integer.parseInt(ajoutEquip[12]);
			int vueEquip=Integer.parseInt(ajoutEquip[13]);
			int vueMEquip=Integer.parseInt(ajoutEquip[14]);
			int apEquip=Integer.parseInt(ajoutEquip[15]);
			int amEquip=Integer.parseInt(ajoutEquip[16]);
			int poidsEquip=Integer.parseInt(ajoutEquip[19]);
			int mmEquip=Integer.parseInt(ajoutEquip[20]);
			int rmEquip=Integer.parseInt(ajoutEquip[21]);
			//int equipEquip=Math.abs(Integer.parseInt(liste[17]));
			boolean zoneEquip=false;
			if(ajoutEquip[17].equals("1"))
				zoneEquip=true;
			boolean dropEquip=false;
			if(ajoutEquip[18].equals("1"))
				dropEquip=true;
			String isEquip="Non";
			if(ajoutEquip[22].equals("1"))
				isEquip="Oui";
			String nomEquip=ajoutEquip[0];
			Equipement equip=new Equipement(idEquip,nomEquip,typeEquip,attEquip,attMEquip,esqEquip,esqMEquip,degEquip,degMEquip,dlaEquip,regEquip,regMEquip,vueEquip,vueMEquip,pvEquip,apEquip,amEquip,mmEquip,rmEquip,zoneEquip,dropEquip,poidsEquip);
			equipements.setElementAt(equip,i);
			((DefaultTableModel) equipementTable.getModel()).removeRow(i);
			((DefaultTableModel) equipementTable.getModel()).insertRow(i,new Object[] {idEquip,nomEquip,Equipement.formateType(typeEquip),equip.getDescr(),isEquip});
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return ;
		}
		
		//ajouter equipement
		
		if(e.getSource() == ajoutEquip){
			try
			{
				String [] ajoutEquip;
				//AjoutEquipement ae=new AjoutEquipement(ajoutEquip,gui);
				AjoutEquipement ae=new AjoutEquipement(dialog,false);
				ajoutEquip=ae.getEquip();
				if(ajoutEquip[0] == null || ajoutEquip[0].length() == 0)
					return;
				int idEquip=Integer.parseInt(ajoutEquip[1]);
				int typeEquip=Integer.parseInt(ajoutEquip[2]);
				int attEquip=Integer.parseInt(ajoutEquip[3]);
				int attMEquip=Integer.parseInt(ajoutEquip[4]);
				int esqEquip=Integer.parseInt(ajoutEquip[5]);
				int esqMEquip=Integer.parseInt(ajoutEquip[6]);
				int degEquip=Integer.parseInt(ajoutEquip[7]);
				int degMEquip=Integer.parseInt(ajoutEquip[8]);
				int dlaEquip=Integer.parseInt(ajoutEquip[9]);
				int regEquip=Integer.parseInt(ajoutEquip[10]);
				int regMEquip=Integer.parseInt(ajoutEquip[11]);
				int pvEquip=Integer.parseInt(ajoutEquip[12]);
				int vueEquip=Integer.parseInt(ajoutEquip[13]);
				int vueMEquip=Integer.parseInt(ajoutEquip[14]);
				int apEquip=Integer.parseInt(ajoutEquip[15]);
				int amEquip=Integer.parseInt(ajoutEquip[16]);
				int poidsEquip=Integer.parseInt(ajoutEquip[19]);
				int mmEquip=Integer.parseInt(ajoutEquip[20]);
				int rmEquip=Integer.parseInt(ajoutEquip[21]);
				//int equipEquip=Math.abs(Integer.parseInt(liste[17]));
				boolean zoneEquip=false;
				if(ajoutEquip[17].equals("1"))
					zoneEquip=true;
				boolean dropEquip=false;
				if(ajoutEquip[18].equals("1"))
					dropEquip=true;
				String isEquip="Non";
				if(ajoutEquip[22].equals("1"))
					isEquip="Oui";
				String nomEquip=ajoutEquip[0];
				Equipement equip=new Equipement(idEquip,nomEquip,typeEquip,attEquip,attMEquip,esqEquip,esqMEquip,degEquip,degMEquip,dlaEquip,regEquip,regMEquip,vueEquip,vueMEquip,pvEquip,apEquip,amEquip,mmEquip,rmEquip,zoneEquip,dropEquip,poidsEquip);
				equipements.add(equip);
				((DefaultTableModel) equipementTable.getModel()).addRow(new Object[] {idEquip,nomEquip,Equipement.formateType(typeEquip),equip.getDescr(),isEquip});
			//	System.out.print(ajoutEquip.toString());
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				return ;
			}
		}
		
		// supprimer un équipement
		
		if(e.getSource() == supprEquip){
			if(equipementTable.getSelectedRow() == -1){
				JOptionPane.showMessageDialog(dialog,
						"Veuillez sélectionner un équipement à supprimer", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			else {
				for(int i=0;i<equipementTable.getRowCount();i++)
				{
					if(equipementTable.isRowSelected(i) )
					{
						equipements.remove(i);
						((DefaultTableModel) equipementTable.getModel()).removeRow(i);
						//equipementTable.removeRowSelectionInterval(i,i+1);
						i--;
					}
				}
			}
		}
		
		
		
	}

	private int getRace() {
		for (int i = 0; i < racesArray.length; i++) {
			if (raceCombo.getSelectedItem() == racesArray[i]) {
				return i;
			}
		}
		return -1;
	}

	private void creeTroll() {
		String addTrollLine, addCaracLine, addCompLine, addSortLine, addMoucheLine, addEquipLine;
		int num;
		String valeurEquip;

		try {

			// Ajout du Troll

			addTrollLine = "addtroll " + numeroField.getText() + " "
					+ nomField.getText() + " ";
			int raceInt = getRace();
			if (raceInt != -1) {
				addTrollLine += Integer.toString(raceInt);
			} else {
				JOptionPane.showMessageDialog(dialog,
						"Vous devez choisir une race", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Ajout des caracs
			addCaracLine = "carac " + niveauLabel.getText();
			for (int i = 0; i < caracs.length; i++) {
				addCaracLine += " "
						+ amelioPlusInit[i].getText().substring(
								0,
								amelioPlusInit[i].getText().length()
										- (1 + uniteCaracs[i].length()));
			}
			try {
				int mm = Integer.parseInt(mmField.getText());
				int rm = Integer.parseInt(rmField.getText());
				addCaracLine += " " + mm + " " + rm;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(dialog,
						"La RM et la MM doivent être des nombres", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			//ajout des comps
			addCompLine = "";
			for (int i = 0; i < competences_reservees.length; i++) {
				if (compCheckBox_reservees[i].isSelected()) {
					try {
						num = Integer.parseInt(pourcentComp_reservees[i].getText());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(dialog,
								"Le niveau de compétence doit être un nombre",
								"Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (Integer.parseInt(pourcentComp_reservees[i].getText()) < 10
							|| Integer.parseInt(pourcentComp_reservees[i].getText()) > 100) {
						JOptionPane
								.showMessageDialog(
										dialog,
										"Le niveau de compétence doit être compris entre 10 et 100",
										"Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}
					addCompLine += "addcomp " + (i + 1) + " "
							+ pourcentComp_reservees[i].getText() + " 1\n";
				}
			}
			for (int i = 0; i < competences.length; i++) {
				if (compCheckBox[i].isSelected()) {
					int nbComp=1;
					try {
						num = Integer.parseInt(pourcentComp[i].getText());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(dialog,
								"Le niveau de compétence doit être un nombre",
								"Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (Integer.parseInt(pourcentComp[i].getText()) < 10
							|| Integer.parseInt(pourcentComp[i].getText()) > 100) {
						JOptionPane
								.showMessageDialog(
										dialog,
										"Le niveau de compétence doit être compris entre 10 et 100",
										"Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(i<=2)
						nbComp = i+6;
					else
						nbComp = i+7;
					addCompLine += "addcomp " + nbComp + " "
							+ pourcentComp[i].getText() + " 1\n";
				}
			}
			for (int i = 0; i < competences_niveau_sup.length; i++) {
				if (compCheckBox_niveau_sup[i].isSelected()) {
					int nbComp=5;
					if(i==1)
						nbComp = 9;
					if(i==2)
						nbComp = 13;
					int level=1;
					try {
						level = Integer.parseInt(compSpinner[i].getValue().toString());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(dialog,
								"Le niveau de compétence doit être un nombre",
								"Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}
					try {
						num = Integer.parseInt(pourcentComp_niveau_sup[i].getText());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(dialog,
								"Le niveau de compétence doit être un nombre",
								"Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (Integer.parseInt(pourcentComp_niveau_sup[i].getText()) < 10
							|| Integer.parseInt(pourcentComp_niveau_sup[i].getText()) > 100) {
						JOptionPane
								.showMessageDialog(
										dialog,
										"Le niveau de compétence doit être compris entre 10 et 100",
										"Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}
					for(int j=1;j<level;j++)
						addCompLine += "addcomp " + nbComp + " 90 "+j+"\n";
					addCompLine += "addcomp " + nbComp + " "
							+ pourcentComp_niveau_sup[i].getText() + " "+level+"\n";
				}
			}

			//ajout des sorts
			addSortLine = "";
			for (int i = 0; i < sorts.length; i++) {
				if (sortCheckBox[i].isSelected()) {
					try {
						num = Integer.parseInt(pourcentSort[i].getText());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(dialog,
								"Le niveau de sort doit être un nombre",
								"Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (Integer.parseInt(pourcentSort[i].getText()) < 10
							|| Integer.parseInt(pourcentSort[i].getText()) > 100) {
						JOptionPane
								.showMessageDialog(
										dialog,
										"Le niveau de sort doit être compris entre 10 et 100",
										"Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}
					addSortLine += "addsort " + (i + 1) + " "
							+ pourcentSort[i].getText() + "\n";
				}
			}

			addMoucheLine = "";
			//ajout des mouches
			for (int i = 0; i < mouchesFields.length; i++) {
				try {
					num = Integer.parseInt(mouchesFields[i].getText());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(dialog,
							"Le nombre de mouches doit être un nombre",
							"Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}

				for (int j = 0; j < Integer
						.parseInt(mouchesFields[i].getText()); j++) {
					addMoucheLine += "addmouche " + mouchesInt[i] + " "
							+ "pas de nom\n";
				}
			}

			//ajout de l'équipement
			String s = "";
			try {
				//addequip id type Att Esq Deg DLA reg pv vue armure_physique armure_magique effet_de_zone bidouille poids(en minute) mm rm est_équipé nom
				for (int i = 0; i < equipementTable.getRowCount(); i++) {
					addEquipLine = "addequip "+equipementTable.getValueAt(i, 0).toString()+" ";
					for(int j=0; j<AjoutEquipement.typesListe.length;j++)
						if(AjoutEquipement.typesListe[j].equals(equipementTable.getValueAt(i, 2).toString()))
						{
							addEquipLine+=j+" ";
						}
					addEquipLine+=equipements.elementAt(i).getBMAttaque()+" "+
							equipements.elementAt(i).getBMMAttaque()+" "+
							equipements.elementAt(i).getBMEsquive()+" "+
							equipements.elementAt(i).getBMDegat()+" "+
							equipements.elementAt(i).getBMMDegat()+" "+
							equipements.elementAt(i).getBMDLA()+" "+
							equipements.elementAt(i).getBMRegeneration()+" "+
							equipements.elementAt(i).getPV()+" "+
							equipements.elementAt(i).getBMVue()+" "+
							equipements.elementAt(i).getBMArmurePhysique()+" "+
							equipements.elementAt(i).getBMArmureMagique()+" ";
					if(equipements.elementAt(i).isZone())
						addEquipLine+="1 ";
					else
						addEquipLine+="0 ";
					if(equipements.elementAt(i).isBidouille())
						addEquipLine+="1 ";
					else
						addEquipLine+="0 ";
					addEquipLine+=equipements.elementAt(i).getPoids()+" "+
							equipements.elementAt(i).getBMMM()+" "+
							equipements.elementAt(i).getBMRM()+" ";
					
					if(equipementTable.getValueAt(i, 4).toString().equals("Oui"))
						addEquipLine+="1 ";
					else
						addEquipLine+="0 ";
					addEquipLine+=equipementTable.getValueAt(i, 1).toString()+"\n";
					s += addEquipLine;
				}
			} catch (Exception ex) {
				JOptionPane
						.showMessageDialog(
								dialog,
								"Les caractéristiques de l'équipement doivent être des nombres",
								"Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(avatar.getIcon()!=null && ((ImageIcon) avatar.getIcon()).getImageLoadStatus()==MediaTracker.COMPLETE)
			{
				ImageIcon img=(ImageIcon) avatar.getIcon();
				int w = img.getImage().getWidth(null);
				int h = img.getImage().getHeight(null);
				BufferedImage bImage = new BufferedImage(w, h,BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2 = bImage.createGraphics();
				g2.drawImage(img.getImage(), 0, 0, null);
				ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
				try {
					ImageIO.write(bImage, "png", baos);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(dialog, "Problème de conversion des images", "Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
				byte[] imageBytes = baos.toByteArray();
				BigInteger bi = new BigInteger(imageBytes);    
				s+="icontroll "+bi.toString(16)+"\n";
			}

			//fin du fichier
			FileWriter writer = new FileWriter(saveField.getText());
			writer.write(addTrollLine + "\n");
			writer.write(addCaracLine + "\n");
			writer.write(addCompLine);
			writer.write(addSortLine);
			writer.write(addMoucheLine);
			writer.write(s);
			writer.write("validtroll");
			writer.close();
			dialog.setVisible(false);
			dialog.dispose();
			JOptionPane.showMessageDialog(dialog,
					"Le personnage a bien été créé", "Infos",
					JOptionPane.PLAIN_MESSAGE);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
	}

	/******************************
	 * changement d'une carac
	 ******************************/

	public void stateChanged(ChangeEvent e) {
		updatePI();
	}

	public void updatePI()
	{
		int nbAmelios = 0;
		if (!isRAZ) {
			coutPiTotal = 0;
			for (int i = 0; i < caracs.length; i++) {
				nbAmelios = Integer.parseInt(caracSpinner[i].getValue()
						.toString());
				if (i != 6) {
					amelioPlusInit[i].setText(nbInitCaracsParRace[i]
							+ nbAmelios * incrementCaracs[i] + " "
							+ uniteCaracs[i]);
				} else {
					if (nbAmelios <= 10) {
						amelioPlusInit[i].setText(720 - 33 * nbAmelios + 3
								* nbAmelios * (nbAmelios + 1) / 2 + " "
								+ uniteCaracs[i]);
					} else {
						amelioPlusInit[i].setText(555 - (nbAmelios - 10) * 5
								/ 2 + " " + uniteCaracs[i]);
					}
				}
				piInvestis[i].setText(Integer
						.toString(piInitCoutCaracsParRace[i] * nbAmelios
								* (nbAmelios + 1) / 2));
				prochaineAmelio[i]
						.setText(Integer.toString(piInitCoutCaracsParRace[i]
								* (nbAmelios + 1)));
				coutPiTotal += Integer.parseInt(piInvestis[i].getText());
			}
			for (int i = 0; i < competences.length; i++) {
				if (compCheckBox[i].isSelected())
					coutPiTotal += piCoutComp[i];
			}
			for (int i = 0; i < competences_niveau_sup.length; i++) {
				if (compCheckBox_niveau_sup[i].isSelected())
					coutPiTotal += piCoutComp[i]*((Integer.parseInt(compSpinner[i].getValue().toString())*(Integer.parseInt(compSpinner[i].getValue().toString())+1))/2);
			}
			piTotalLabel.setText(Integer.toString(coutPiTotal));
			Niveau = (int) (Math
					.floor((-1 + Math.sqrt(9 + 4 * coutPiTotal / 5)) / 2));
			niveauLabel.setText(Integer.toString(Niveau));
		}
	}

	/****************************
	 * utils
	 ***************************/

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

	class MyTableModel extends DefaultTableModel {
		int col=-1;
		public MyTableModel()
		{ super();}
		public MyTableModel(int c)
		{
			super();
			col=c;
		}
		public boolean isCellEditable(int row, int c) {
			return col==c;
        	}
        }
}
