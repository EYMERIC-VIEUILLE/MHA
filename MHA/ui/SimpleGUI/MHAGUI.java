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

import mha.engine.*;
import mha.engine.core.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MHAGUI extends JFrame implements MouseInputListener {

	private final static String version = "0.6.4 build 11022008";
	private final static String product = "Mountyhall Arena";

	private JTextPane Console;
	private StyledDocument doc;
	private JTextField Command;
	private JButton Submit;
	private static MHAGUI gui;
//	private PicturePanel pp;

	private JLabel statusBar;
	private JLabel hourBar;
	private JLabel gameStatus;
	private JScrollPane Con;
	private JTable tableEvent;
	private JTable tableTrolls;
	private JTable tableVue;
	private JTable tableLieux;
	private JTable tableBM;
	private JTable tableMouches;
	private JEditorPane profil;
	private JEditorPane equipement;
	private JTabbedPane tabbedPane;

	private MHA mha;
	private Vector history;
	private int pointer;
	private String temptext;

	private JPanel gp;
	private JLabel Pix;

	private JComboBox actionComboBox;
	private JButton action;
	
	private JMenu menuFile;
	private JTabbedPane messages;
	private Vector<Integer> listeTab=new Vector<Integer>();
	private JTextField texteMessage;
	
	private int current_time=0;
	private int nbPA=0;
	private int trollId=-1;
	private int idTeam=-1;
	private int size=5;
	private boolean dlaActive=false;
	private boolean popNext=false;
	private boolean updateEquip=false;
	private boolean dead=false;
	private int[] sorts=new int[Troll.NB_SORT];
	private Hashtable<Integer,Hashtable<Integer,Integer>> comps=new Hashtable<Integer,Hashtable<Integer,Integer>>();
	private Vector<String []> parchemins;
	private Vector<String []> potions;
	private int x=0;
	private int y=0;
	private int n=0;
	private boolean finish=false;
	
	private boolean useTP;
	private boolean useInvi;
	private int mode;
	private int nbTeam;
	private boolean isServer=false;
	private Vector <Color> couleurs=new Vector<Color>();
	private ColorCellRenderer rendererVue;
	
	private Vector <InfoTroll> trolls=new Vector<InfoTroll>();
	public static String lastDirectory="";
	private JDialog newDLA=null;


	public MHAGUI(MHA r) {
		super();
		SplashScreen sc=new SplashScreen("splash.png",3000);
		mha=r;

// this get all the commands from the game and does what needs to be done
		MHAAdapter SimpleMHAAdapter = new MHAAdapter() {

			public void sendMessage(String output, boolean redrawNeeded, boolean repaintNeeded) {
				try {
					Style style = doc.addStyle("StyleName", null);
					if(output.length()>8 && output.substring(0,8).equals("Action: "))
					{
						JTextArea jta=((JTextArea) ((JViewport) ((JScrollPane) messages.getComponentAt(1)).getComponent(0) ).getView());
						jta.append(System.getProperty("line.separator")+"<"+hour2string(current_time)+">"+System.getProperty("line.separator")+output.substring(8)+System.getProperty("line.separator"));
						jta.setCaretPosition(jta.getLineEndOffset(jta.getLineCount()-1));
						JOptionPane.showMessageDialog(gui, output.substring(8), "Evènement sur votre troll", 	JOptionPane.PLAIN_MESSAGE);
					}
					else if(output.length()>7 && output.substring(0,7).equals("Error: "))
					{
						updateEquip=false;
						popNext=false;
						JOptionPane.showMessageDialog(gui, output.substring(7), "Erreur", 	JOptionPane.ERROR_MESSAGE);
					}
					else if(output.length()>9 && output.substring(0,9).equals("Newturn: "))
					{
						JOptionPane.showMessageDialog(gui, output.substring(9), "Activation de DLA", 	JOptionPane.PLAIN_MESSAGE);
						dlaActive=true;
					}
					else if(output.equals("Vous avez été déconnecté du serveur !"))
					{
						JOptionPane.showMessageDialog(gui, output, "Déconnexion", 	JOptionPane.ERROR_MESSAGE);
						reInitGui();
					}
					else if(strcmp(output,"Deconnexion: "))
					{
						String s=extractString(output,"Deconnexion: ");
						String []ls=s.split(" ");
						int id=Integer.parseInt(ls[0]);
						String name=ls[1];
						for(int i=2;i<ls.length;i++)
							name+=" "+ls[i];
						for(int i=0;i<tableTrolls.getModel().getRowCount();i++)
						{
							if((tableTrolls.getModel().getValueAt(i,1)).toString().equals(""+id))
							{
								trolls.remove(i);
								couleurs.remove(i);
								//if(idTeam!=-1)
								//	messages.remove(i+2);
								//messages.remove(i+1);
								((DefaultTableModel) tableTrolls.getModel()).removeRow(i);
								break;
							}
						}
						int idTab=0;
						for(int i=0;i<listeTab.size();i++)
							if(listeTab.elementAt(i)==id)
							{
								idTab=i+2;
								if(idTeam!=-1)
									idTab++;
								if(trollId==-1)
									idTab--;
								messages.remove(idTab);
								break;
							}
						for(int i=0;i<tableVue.getModel().getRowCount();i++)
						{
							if((tableVue.getModel().getValueAt(i,1)).toString().equals(""+id))
							{
								((DefaultTableModel) tableVue.getModel()).removeRow(i);
								break;
							}
						}
						JOptionPane.showMessageDialog(gui, name+" ("+id+") vient de quitter la partie", "Déconnexion", 	JOptionPane.PLAIN_MESSAGE);
						
					}
					else if(strcmp(output,"Rules: "))
					{
						String s=extractString(output,"Rules: ");
						String []ls=s.split(" ");
						mode=Integer.parseInt(ls[0]);
						if(mode==1)
							for(int k=0;k<menuFile.getItemCount();k++)
								if(menuFile.getItem(k).getText().equals("Charger un bot"))
									menuFile.getItem(k).setEnabled(false);
						useInvi=Boolean.parseBoolean(ls[1]);
						useTP=Boolean.parseBoolean(ls[2]);
						nbTeam=Integer.parseInt(ls[3]);
					}
					else if(output.length()>6 && output.substring(0,6).equals("Time: "))
					{
						current_time=Integer.parseInt(output.substring(6));
						hourBar.setText("Nous sommes actuellement le "+Troll.hour2string(current_time));
						actionComboBox.removeAllItems();
						actionComboBox.addItem("*** Choisissez une action ***");
						actionComboBox.setEnabled(false);
						if(newDLA!=null)
						{
							newDLA.setVisible(false);
							newDLA.dispose();
							newDLA=null;
						}
						action.setEnabled(false);
						
					}
					else if(strcmp(output,"Partie terminée\n"))
					{
						String s=extractString(output,"Partie terminée\n");
						actionComboBox.removeAllItems();
						actionComboBox.addItem("*** Choisissez une action ***");
						actionComboBox.setEnabled(false);
						action.setEnabled(false);
						finish=true;
						JOptionPane.showMessageDialog(gui, s, "Fin de partie", JOptionPane.INFORMATION_MESSAGE);
					}
					else if(strcmp(output,"MHA serveur "))
					{	
						//System.out.println(output);
						String s=extractString(output,"MHA serveur ");
						String ls[] = s.split("\n");
						if(ls[0].trim().equals(MHAServer.version))
						{
							if(ls.length>1)
								s=ls[1];
							else
								s="";
							if(isServer)
							{
								menuFile.remove(1);
							}
							else
							{
								menuFile.remove(0);
								menuFile.remove(0);									
							}
							JMenuItem openFile = new JMenuItem("Charger un troll");
							openFile.setMnemonic('R');
							openFile.addActionListener(
									new java.awt.event.ActionListener() {
										public void actionPerformed(java.awt.event.ActionEvent e) {
					
											final JFileChooser fc;
											if(lastDirectory.length()==0)
												fc = new JFileChooser(new File ("."));
											else
												fc = new JFileChooser(lastDirectory);
											MHAFileFilter filter = new MHAFileFilter("mha","Fiche de perso pour Mountyhall Arena");
											fc.setFileFilter(filter);
					
											int returnVal = fc.showDialog(MHAGUI.this, "Charger");
											if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
												java.io.File file = fc.getSelectedFile();
												// Write your code here what to do with selected file
					
												try {
					
													FileReader filein = new FileReader(file);
													BufferedReader bufferin = new BufferedReader(filein);
													lastDirectory = file.getCanonicalPath();
													String input = bufferin.readLine();
													input=input.replaceFirst("//.*","");
													while(input != null) {
					
														go(input);
														input = bufferin.readLine();
														input=input.replaceFirst("//.*","");
													}
													bufferin.close();
					
												}
												catch(Exception error) {
					
												}
					
											} else {
												// Write your code here what to do if user has canceled Open dialog
											}
										}
									});
							JMenuItem openBot = new JMenuItem("Charger un bot");
							openBot.addActionListener(
									new java.awt.event.ActionListener() {
										public void actionPerformed(java.awt.event.ActionEvent e) {
					
											final JFileChooser fc;
											if(lastDirectory.length()==0)
												fc = new JFileChooser(new File ("."));
											else
												fc = new JFileChooser(lastDirectory);
											MHAFileFilter filter = new MHAFileFilter("mha","Fiche de perso pour Mountyhall Arena");
											fc.setFileFilter(filter);
					
											int returnVal = fc.showDialog(MHAGUI.this, "Charger");
											if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
												java.io.File file = fc.getSelectedFile();
												// Write your code here what to do with selected file
												String output="";
												try {
													MHA m=new MHA();
													m.parser("join "+mha.getServeur());
													FileReader filein = new FileReader(file);
													BufferedReader bufferin = new BufferedReader(filein);
													lastDirectory = file.getCanonicalPath();
													String input = bufferin.readLine();
													while(input != null) {
														input=input.replaceFirst("//.*","");
														input=input.replaceFirst("validtroll","validbot");
														m.parser(input);
														input = bufferin.readLine();
														
													}
													bufferin.close();
													output="Le bot a bien été chargé";
												}
												catch(Exception error) {
													output="Un problème est survenu lors de la création du bot";
												}
												JOptionPane.showMessageDialog(gui, output, "Création d'un bot",JOptionPane.PLAIN_MESSAGE);
											} else {
												// Write your code here what to do if user has canceled Open dialog
											}
										}
									});
							if(isServer)
							{
								menuFile.insert(openFile,1);
								menuFile.insert(openBot,2);
							}
							else
							{
								menuFile.insert(openFile,0);
								menuFile.insert(openBot,1);
							}
							JOptionPane.showMessageDialog(gui, s, "Connexion au serveur",JOptionPane.PLAIN_MESSAGE);
						}
						else
						{
							JOptionPane.showMessageDialog(Console,"Mauvaise version du serveur", "Erreur",JOptionPane.ERROR_MESSAGE);
							mha.parser("logout");
						}
					}
					else if(output.equals("Le serveur a démarré"))
					{
						JMenuItem demarrer = new JMenuItem("Arrêter le serveur");
						demarrer.setMnemonic('K');
						demarrer.addActionListener(
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									mha.parser("killserver");
								}
							});
						menuFile.remove(0);
						menuFile.insert(demarrer,0);
						isServer=true;
						JOptionPane.showMessageDialog(gui, "Le serveur a bien été lancé", "Serveur", 	JOptionPane.PLAIN_MESSAGE);
					}
					else if(output.equals("Le serveur a été tué."))
					{
						JMenuItem demarrer = new JMenuItem("Démarrer le serveur");
						demarrer.setMnemonic('S');
						demarrer.addActionListener(
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									new LaunchServer(gui,mha);
								}
							});
						menuFile.remove(0);
						menuFile.insert(demarrer,0);
						isServer=false;
					}					
					else if(output.length()>6 && output.substring(0,6).equals("begin "))
					{
						final int time=Integer.parseInt(output.substring(6));
						dead=false;
						String s="Activer la DLA";
						mha.parser("getfullprofil");
						mha.parser("getbm");
						if(dlaActive && current_time>=time)
							dlaActive=false;
						if(dlaActive)
							s="Jouer";
							
						final JDialog dialog = new JDialog(gui,"Nouvelle DLA");
						newDLA = dialog;
						JLabel label = new JLabel("<html><p align=center>"
							+ "Nous sommes le "+Troll.hour2string(current_time)+"<br>"
							+ "Votre nouvelle DLA commence le "+Troll.hour2string(time)+"<br>"
							+ "Que souhaitez vous faire ?");
						label.setHorizontalAlignment(JLabel.CENTER);
//						Font font = label.getFont();
//						label.setFont(label.getFont().deriveFont(font.PLAIN,14.0f));
						JButton closeButton = new JButton("Ne pas jouer de suite");
						closeButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								dialog.setVisible(false);
								dialog.dispose();
								newDLA=null;
								String s = (String)JOptionPane.showInputDialog(gui,
									"De combien de minutes voulez vous décaler votre tour de jeu ?",
									"Décaler sa DLA",
									JOptionPane.PLAIN_MESSAGE,
									null,
									null,
									"");
								if ((s != null) && (s.length() > 0)) {
									int i=Integer.parseInt(s);
									mha.parser("decaletour "+i);
								}
								else
								{
									sendMessage("begin "+time,false,false);
								}
							}
						});
						JButton otherButton = new JButton(s);
						if(dlaActive)
						{
							otherButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								dialog.setVisible(false);
								dialog.dispose();
								mha.parser("activeturn");
								mha.parser("getpa");
								mha.parser("getfullprofil");
								mha.parser("getbm");							
							} });
						}
						else
						{
							otherButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								dialog.setVisible(false);
								dialog.dispose();
								mha.parser("begindla");
								mha.parser("getpa");
								mha.parser("getvue");
								mha.parser("getfullprofil");
								mha.parser("getbm");
							} });
						}
						JPanel closePanel = new JPanel();
						closePanel.setLayout(new BoxLayout(closePanel,BoxLayout.LINE_AXIS));
						closePanel.add(Box.createHorizontalGlue());
						closePanel.add(otherButton);
						closePanel.add(Box.createHorizontalStrut(5));
						closePanel.add(closeButton);
						closePanel.add(Box.createHorizontalGlue());
//						closePanel.setBorder(BorderFactory.createEmptyBorder(0,0,5,5));
						JPanel contentPane = new JPanel(new BorderLayout());
						contentPane.add(label, BorderLayout.CENTER);
						contentPane.add(closePanel, BorderLayout.PAGE_END);
						contentPane.setOpaque(true);
						dialog.setContentPane(contentPane);
						dialog.setSize(new Dimension(400, 150));
						dialog.setLocationRelativeTo(gui);
						dialog.setVisible(true);	
					}
					else if(output.length()>4 && output.substring(0,4).equals("PA: "))
					{
						if(!finish)
						{
							nbPA=Integer.parseInt(output.substring(4));
							updatePA();
						}
						return;
					}
					else if(strcmp(output,"Equip: "))
					{
						String s=extractString(output,"Equip: ");
						updateEquip(s);
						output="";
						//profil.setText("<html>"+s.replace("\n","<br>"));
					}
					else if(strcmp(output,"FullProfil: "))
					{
						String s=extractString(output,"FullProfil: ");
						updateProfil(s);
						output="";
						//profil.setText("<html>"+s.replace("\n","<br>"));
					}
					else if(strcmp(output,"Event "))
					{
						String s=extractString(output,"Event ");
						String [] ls=s.split(" ");
						//System.out.println("pbm avec "+ls[0]);
						int t=Integer.parseInt(ls[0]);
						
						((DefaultTableModel) tableEvent.getModel()).insertRow(0,new Object[]{hour2string(t),s.substring(ls[0].length()+1)});
						TableCellRenderer renderer = tableEvent.getTableHeader().getDefaultRenderer();
						TableColumn c = tableEvent.getColumnModel().getColumn(1);
						Component rc = renderer.getTableCellRendererComponent(tableEvent, c.getHeaderValue(), false, false, -1, -1);
						c.setPreferredWidth(rc.getPreferredSize().width+20);
						c.setWidth(c.getPreferredWidth());
						if(trollId!=-1 && !dead)
						{
							mha.parser("getfullprofil");
							mha.parser("getbm");
						}
						//tableEvent.updateUI();
					}
					else if(strcmp(output,"ArenaSize "))
					{
						String s=extractString(output,"ArenaSize ");
						String [] ls=s.split(" ");
						//System.out.println("pbm avec "+ls[0]);
						int t=Integer.parseInt(ls[0]);
						size=t;
					}
					else if(strcmp(output,"Created: "))
					{
						output=extractString(output,"Created: ");
						String s=output.substring(output.lastIndexOf("(")+1,output.lastIndexOf(")"));
						trollId=Integer.parseInt(s);		
						JMenuItem demarrer = new JMenuItem("Démarrer la partie");
						demarrer.setMnemonic('G');
						demarrer.addActionListener(
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									mha.parser("startgame");
								}
							});
						if(isServer)
						{
							menuFile.remove(1);
							menuFile.insert(demarrer,1);
						}
						else
						{
							menuFile.remove(0);
							menuFile.insert(demarrer,0);
						}
					}
					else if(strcmp(output,"Comp: "))
					{
						String [] ls=extractString(output,"Comp: ").split(";");
						if(ls.length!=Troll.NB_COMP)
							return;
						for(int i=0;i<Troll.NB_COMP;i++)
						{
							String[] lss = ls[i].split("\\|");
							for(int j=0;j<lss.length;j++)
							{
								if(j==0 && !lss[j].equals("0"))
								{
									comps.put(i, new Hashtable<Integer,Integer>());
								}
								if(!lss[j].equals("0"))
									comps.get(i).put(j+1, Integer.parseInt(lss[j]));
							}
						}
						return;
					}
					else if(strcmp(output,"Sort: "))
					{
						String [] ls=extractString(output,"Sort: ").split(";");
						if(ls.length!=Troll.NB_SORT)
							return;
						for(int i=0;i<Troll.NB_SORT;i++)
							sorts[i]=Integer.parseInt(ls[i]);
						return;
					}
					else if(strcmp(output,"Mouches: "))
					{
						String s=extractString(output,"Mouches: ");
						output="";
						if(s.length()>0)
						{
							String [] ls=s.split("\n");
							while(((DefaultTableModel) tableMouches.getModel()).getRowCount()!=0)
								((DefaultTableModel) tableMouches.getModel()).removeRow(0);
							for(int i=0;i<ls.length;i++)
							{
								s=ls[i];
								String nom=s.substring(0,s.lastIndexOf(" "));
								String type=s.substring(s.lastIndexOf(" ")+1);
								((DefaultTableModel) tableMouches.getModel()).addRow(new Object[] {nom,type});
							}
						}
						
					}
					else if(strcmp(output,"BM: "))
					{
						String s=extractString(output,"BM: ");
						output="";
						if(s.length()>0)
						{
							String [] ls=s.split("\n");
							while(((DefaultTableModel) tableBM.getModel()).getRowCount()!=0)
								((DefaultTableModel) tableBM.getModel()).removeRow(0);
							for(int i=0;i<ls.length;i++)
							{
								s=ls[i];
								String nom=s.substring(0,s.lastIndexOf(" : "));
								String descr="Aucune description";
								if(s.lastIndexOf(", Durée ")!=-1)
									descr=s.substring(s.lastIndexOf(" : ")+3,s.lastIndexOf(", Durée "));
								int duree = Integer.parseInt(s.substring(s.lastIndexOf("Durée ")+6,s.lastIndexOf(" tour")));
								((DefaultTableModel) tableBM.getModel()).addRow(new Object[] {nom,descr,duree});
							}
						}
					}
					else if(strcmp(output,"Vue: "))
					{
						if(finish)
							return;
						String s=extractString(output,"Vue: ");
						if(strcmp(s,"Error: "))
							return;
						String [] ls=s.split("\n");
						Object [] tt=null;
						for(int i=0;i<ls.length;i++)
						{
							String[] lss=ls[i].split(" ");
							if(Integer.parseInt(lss[0])==trollId)
							{
								x=Integer.parseInt(lss[1]);
								y=Integer.parseInt(lss[2]);
								n=Integer.parseInt(lss[3]);
								break;
							}
						}
						Vector v=new Vector();
						Vector<Color> cv=new Vector<Color>();
						for(int i=0;i<ls.length;i++)
						{
							String[] lss=ls[i].split(" ");
							InfoTroll it=null;
							for(int j=0;j<trolls.size();j++)
								if(trolls.elementAt(j).getId() == Integer.parseInt(lss[0]))
								{
									it=trolls.elementAt(j);
									break;
								}
							if(it!=null)
								if(Integer.parseInt(lss[0])!=trollId)
									v.add(new Object[]{
									Math.max(Math.max(Math.abs(x-Integer.parseInt(lss[1])),Math.abs(y-Integer.parseInt(lss[2]))),Math.abs(n-Integer.parseInt(lss[3]))),
									it.getId(),
									it.getNom(),
									it.getNiveau(),
									it.getRace(),
									Integer.parseInt(lss[1]),
									Integer.parseInt(lss[2]),
									Integer.parseInt(lss[3])});
								else
									tt=new Object[]{
									Math.max(Math.max(Math.abs(x-Integer.parseInt(lss[1])),Math.abs(y-Integer.parseInt(lss[2]))),Math.abs(n-Integer.parseInt(lss[3]))),
									it.getId(),
									it.getNom(),
									it.getNiveau(),
									it.getRace(),
									Integer.parseInt(lss[1]),
									Integer.parseInt(lss[2]),
									Integer.parseInt(lss[3])};
									
						}
						if(v.size()>1)
							quickSort(v, 0, v.size()-1);
						rendererVue.setCouleurs(cv);
						while(((DefaultTableModel) tableVue.getModel()).getRowCount()!=0)
							((DefaultTableModel) tableVue.getModel()).removeRow(0);
						for(int j=0;j<trolls.size() && mode==MHAGame.MODE_TEAM_DEATHMATCH;j++)
							if(trolls.elementAt(j).getId()==trollId)
							{
								cv.add(getCouleur(trolls.elementAt(j).getTeam()));
								break;
							}
						((DefaultTableModel) tableVue.getModel()).addRow(tt);
						for(int i=0;i<v.size();i++)
						{
							for(int j=0;j<trolls.size() && mode==MHAGame.MODE_TEAM_DEATHMATCH;j++)
								if(trolls.elementAt(j).getId()==(Integer) ((Object[])v.elementAt(i))[1])
								{
									cv.add(getCouleur(trolls.elementAt(j).getTeam()));
//									System.out.println("Le "+i+"eme élément de la vue : "+trolls.elementAt(j).getId()+" est de la couleur "+trolls.elementAt(j).getTeam());
									break;
								}
							((DefaultTableModel) tableVue.getModel()).addRow((Object[]) v.elementAt(i));
						}
						
					}
					else if(strcmp(output,"Lieux: "))
					{
						if(finish)
							return;
						String s=extractString(output,"Lieux: ");
						if(strcmp(s,"Error: "))
							return;
						while(((DefaultTableModel) tableLieux.getModel()).getRowCount()!=0)
							((DefaultTableModel) tableLieux.getModel()).removeRow(0);
						if(s.equals("Aucun lieu"))
							return;
						String [] ls=s.split("\n");
						Vector v=new Vector();
						for(int i=0;i<ls.length;i++)
						{
							String[] lss=ls[i].split(" ");
							for(int j=2;j<lss.length-3;j++)
								lss[1]+=" "+lss[j];
							v.add(new Object[]{
							Math.max(Math.max(Math.abs(x-Integer.parseInt(lss[lss.length-3])),Math.abs(y-Integer.parseInt(lss[lss.length-2]))),Math.abs(n-Integer.parseInt(lss[lss.length-1]))),
							Integer.parseInt(lss[0]),
							lss[1],
							Integer.parseInt(lss[lss.length-3]),
							Integer.parseInt(lss[lss.length-2]),
							Integer.parseInt(lss[lss.length-1])});
									
						}
						if(v.size()>1)
							quickSort(v, 0, v.size()-1);
						for(int i=0;i<v.size();i++)
						{
							((DefaultTableModel) tableLieux.getModel()).addRow((Object[]) v.elementAt(i));
						}
						output="";
						
					}
					else if(strcmp(output,"newTroll "))
					{
						String s=extractString(output,"newTroll ");
						String [] ls=s.split(";");
						int id=Integer.parseInt(ls[0]);
						int race=Integer.parseInt(ls[1]);
						int level=Integer.parseInt(ls[2]);
						int certif=Integer.parseInt(ls[3]);
						String nom=ls[4];
						for(int i=5;i<ls.length;i++)
							nom+=";"+ls[i];
						InfoTroll t=new InfoTroll(nom,id,level,race);
						trolls.add(t);
						if(certif==0)
							((DefaultTableModel) tableTrolls.getModel()).addRow(new Object[]{null,t.getId(),t.getNom(),t.getNiveau(),t.getRace()});
						else
							((DefaultTableModel) tableTrolls.getModel()).addRow(new Object[]{"X",t.getId(),t.getNom(),t.getNiveau(),t.getRace()});
						couleurs.add(null);
						if(id!=trollId)
						{
							JTextArea jta=new JTextArea();
							jta.setEditable(false);
							jta.setLineWrap(true);
							JScrollPane jsp=new JScrollPane(jta);
							jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
							messages.addTab(nom,jsp);
							listeTab.add(id);
						}
						else
						{
							JTextArea jta=new JTextArea();
							jta.setEditable(false);
							jta.setLineWrap(true);
							JScrollPane jsp=new JScrollPane(jta);
							jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
							messages.insertTab("Messages du bot",null,jsp,"",1);
							setTitle(product+" : "+nom);
							
							if(mode==MHAGame.MODE_TEAM_DEATHMATCH)
							{
								Vector <Color> vc=new Vector <Color>();
								for(int i=0;i<Math.min(nbTeam,listeCouleurs.length);i++)
									vc.add(listeCouleurs[i]);
								dialogChooseTeam(vc);
						
								//If a string was returned, say so.
		//						if ((s != null) && (s.length() > 0)) {
		//							setLabel("Green eggs and... " + s + "!");
		//							return;
		//						}
							}
						}
					}
					else if(strcmp(output,"icontroll "))
					{
						String s=extractString(output,"icontroll ");
						output="";
						String [] ls=s.split(" ");
						int id=Integer.parseInt(ls[0]);
						for(int i=0;i<tableTrolls.getModel().getRowCount();i++)
						{
							if((tableTrolls.getModel().getValueAt(i,1)).toString().equals(""+id))
							{
								trolls.elementAt(i).setIcon(ls[1]);
								break;
							}
						}
					}
					else if(strcmp(output,"setteam "))
					{
						String s=extractString(output,"setteam ");
						String [] ls=s.split(" ");
						int id=Integer.parseInt(ls[0]);
						int team=Integer.parseInt(ls[1]);
						for(int i=0;i<tableTrolls.getModel().getRowCount();i++)
						{
							if((tableTrolls.getModel().getValueAt(i,1)).toString().equals(""+id))
							{
								trolls.elementAt(i).setTeam(team);
								couleurs.setElementAt(getCouleur(team),i);
								break;
							}
						}
						if(id==trollId)
						{
							JTextArea jta=new JTextArea();
							jta.setEditable(false);
							jta.setLineWrap(true);
							JScrollPane jsp=new JScrollPane(jta);
							jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
							messages.insertTab("Equipe",null,jsp,"Pour parler avec les membres de votre équipe",2);
							idTeam=team;
						}
						Thread t=new Thread(new Runnable() {
							public void run() {
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										tableTrolls.updateUI();
									}
								});
							}});
						t.start();
						
					}
					else if(output.equals("The game begins"))
					{
						statusBar.setText("Il vous reste 0 PA");
						nbPA=0;
						mha.parser("getcomp");
						mha.parser("getsort");
						mha.parser("getequip");
						mha.parser("getmouches");
						mha.parser("getfullprofil");
						if(isServer)
						{
							menuFile.remove(1);
							menuFile.remove(1);
						}
						else
						{
							menuFile.remove(0);
							menuFile.remove(0);
						}
					}
					else if(strcmp(output,"message all "))
					{
						String s1=extractString(output,"message all ");
						int id=Integer.parseInt(s1.substring(0,s1.indexOf(" ")));
						s1=s1.substring(s1.indexOf(" ")+1);
						String s="????";
						for(int i=0;i<trolls.size();i++)
							if(trolls.elementAt(i).getId()==id)
							{
								s=trolls.elementAt(i).getNom();
								break;
							}
						output="";
						JTextArea jta=((JTextArea) ((JViewport) ((JScrollPane) messages.getComponentAt(0)).getComponent(0) ).getView());
						jta.append("<"+s+">"+s1+System.getProperty("line.separator"));
						jta.setCaretPosition(jta.getLineEndOffset(jta.getLineCount()-1));
						if(messages.getSelectedIndex()!=0)
							messages.setForegroundAt(0,Color.RED);
					}
					else if(strcmp(output,"message groupe "))
					{
						String s1=extractString(output,"message groupe ");
						int id=Integer.parseInt(s1.substring(0,s1.indexOf(" ")));
						s1=s1.substring(s1.indexOf(" ")+1);
						String s="????";
						for(int i=0;i<trolls.size();i++)
							if(trolls.elementAt(i).getId()==id)
							{
								s=trolls.elementAt(i).getNom();
								break;
							}
						output="";
						JTextArea jta=((JTextArea) ((JViewport) ((JScrollPane) messages.getComponentAt(2)).getComponent(0) ).getView());
						jta.append("<"+s+">"+s1+System.getProperty("line.separator"));
						jta.setCaretPosition(jta.getLineEndOffset(jta.getLineCount()-1));
						if(messages.getSelectedIndex()!=2)
							messages.setForegroundAt(2,Color.RED);
					}
					else if(strcmp(output,"message prive "))
					{
						String s1=extractString(output,"message prive ");
						int id=Integer.parseInt(s1.substring(0,s1.indexOf(" ")));
						s1=s1.substring(s1.indexOf(" ")+1);
						int id2=Integer.parseInt(s1.substring(0,s1.indexOf(" ")));
						s1=s1.substring(s1.indexOf(" ")+1);
						int idLocuteur=id;
						if(id==trollId)
							id=id2;
						String s="????";
						for(int i=0;i<trolls.size();i++)
							if(trolls.elementAt(i).getId()==idLocuteur)
							{
								s=trolls.elementAt(i).getNom();
								break;
							}
						int idTab=0;
						for(int i=0;i<listeTab.size();i++)
							if(listeTab.elementAt(i)==id)
							{
								idTab=i+2;
								if(idTeam!=-1)
									idTab++;
								break;
							}
						output="";
						((JTextArea) ((JViewport) ((JScrollPane) messages.getComponentAt(idTab)).getComponent(0) ).getView())
							.append("<"+s+">"+s1+System.getProperty("line.separator"));
						if(messages.getSelectedIndex()!=idTab)
							messages.setForegroundAt(idTab,Color.RED);
					}
					else if(strcmp(output,"InfosTroll: "))
					{
						String s=extractString(output,"InfosTroll: ");
						output="";
						final JDialog dialog = new JDialog(gui,"Profil d'un Troll");
				
						//Add contents to it. It must have a close button,
						//since some L&Fs (notably Java/Metal) don't provide one
						//in the window decorations for dialogs.
						JLabel label = new JLabel("<html><p align=center>"+s.replace("\n","<br>"));
						label.setHorizontalAlignment(JLabel.CENTER);
						Font font = label.getFont();
						label.setFont(label.getFont().deriveFont(font.PLAIN,14.0f));	
						JButton closeButton = new JButton("Fermer");
						closeButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
							dialog.setVisible(false);
							dialog.dispose();
							}
						});
						JPanel closePanel = new JPanel();
						closePanel.setLayout(new BoxLayout(closePanel,
										BoxLayout.LINE_AXIS));
						closePanel.add(Box.createHorizontalGlue());
						closePanel.add(closeButton);
						closePanel.setBorder(BorderFactory.
							createEmptyBorder(0,0,5,5));
				
						JPanel contentPane = new JPanel(new BorderLayout());
						contentPane.add(new JScrollPane(label), BorderLayout.CENTER);
						int id=Integer.parseInt(s.substring(s.indexOf(":")+2,s.indexOf("\n")));
						for(int i=0;i<trolls.size();i++)
							if(trolls.elementAt(i).getId()==id && trolls.elementAt(i).getIcon()!=null &&
								 trolls.elementAt(i).getIcon().getImageLoadStatus()==MediaTracker.COMPLETE)
							{
								JLabel avatar = new JLabel();
								avatar.setIcon(trolls.elementAt(i).getIcon());
								avatar.setFont(avatar.getFont().deriveFont(Font.ITALIC));
								avatar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
								avatar.setPreferredSize(new Dimension(120, 120+10));
								avatar.setMinimumSize(new Dimension(120, 120+10));
								avatar.setMaximumSize(new Dimension(120, 120+10));
								contentPane.add(avatar, BorderLayout.EAST);
								break;
							}
						
						contentPane.add(closePanel, BorderLayout.PAGE_END);
						contentPane.setOpaque(true);
						dialog.setContentPane(contentPane);
				
						//Show it.
						dialog.setSize(new Dimension(300, 300));
						dialog.setLocationRelativeTo(gui);
						dialog.pack();
						dialog.setVisible(true);
					}
					else if(strcmp(output,"InfosLieu: "))
					{
						String s=extractString(output,"InfosLieu: ");
						output="";
						final JDialog dialog = new JDialog(gui,"Lieu");
				
						//Add contents to it. It must have a close button,
						//since some L&Fs (notably Java/Metal) don't provide one
						//in the window decorations for dialogs.
						JLabel label = new JLabel("<html><p align=center>"+s.replace("\n","<br>"));
						label.setHorizontalAlignment(JLabel.CENTER);
						Font font = label.getFont();
						label.setFont(label.getFont().deriveFont(font.PLAIN,14.0f));	
						JButton closeButton = new JButton("Fermer");
						closeButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
							dialog.setVisible(false);
							dialog.dispose();
							}
						});
						JPanel closePanel = new JPanel();
						closePanel.setLayout(new BoxLayout(closePanel,
										BoxLayout.LINE_AXIS));
						closePanel.add(Box.createHorizontalGlue());
						closePanel.add(closeButton);
						closePanel.setBorder(BorderFactory.
							createEmptyBorder(0,0,5,5));
				
						JPanel contentPane = new JPanel(new BorderLayout());
						contentPane.add(new JScrollPane(label), BorderLayout.CENTER);
						contentPane.add(closePanel, BorderLayout.PAGE_END);
						contentPane.setOpaque(true);
						dialog.setContentPane(contentPane);
				
						//Show it.
						dialog.setSize(new Dimension(300, 300));
						dialog.setLocationRelativeTo(gui);
						dialog.setVisible(true);
						return;
					}
					else if(popNext)
					{
						mha.parser("getpa");
						mha.parser("getvue");
						mha.parser("getlieux");
						mha.parser("getbm");
						mha.parser("getfullprofil");
						if(updateEquip)
							mha.parser("getequip");
						updateEquip=false;
						popNext=false;
						JTextArea jta=((JTextArea) ((JViewport) ((JScrollPane) messages.getComponentAt(1)).getComponent(0) ).getView());
						jta.append(System.getProperty("line.separator")+"<"+hour2string(current_time)+">"+System.getProperty("line.separator")+output+System.getProperty("line.separator"));
						jta.setCaretPosition(jta.getLineEndOffset(jta.getLineCount()-1));
						JOptionPane.showMessageDialog(gui, output, "Résultat", 	JOptionPane.PLAIN_MESSAGE);						
					}
/*					Color c = mha.getCurrentPlayerColor();

					if (c != null) {
						StyleConstants.setForeground(style, c.darker() );
					}
					else {
						StyleConstants.setForeground(style, Color.black);
					}*/
					//StyleConstants.setBold(style, true);

				
					//if (doc.getLength() > 12000) {
					//doc.remove(0,2000);
					//}
//					popNext=false;
//					if(updateEquip)
//						mha.parser("getequip");
//					updateEquip=false;
					if(output.length()>0)
						doc.insertString(doc.getLength(), output.replace("\n",System.getProperty("line.separator")) + System.getProperty("line.separator") , style);
					Console.setCaretPosition(doc.getLength());
				}
				catch (Exception e) { e.printStackTrace();}

				if (redrawNeeded) {
					//pprepaintCountries();
				}
				if (repaintNeeded) {
					repaint();
				}

			}

			/**
			 * checks if the the frame needs input
			 * @param s determines what needs input
			 */
			public void needInput(int s) {

			/*	Submit.setEnabled(true);
				Command.setEnabled(true);
				Command.requestFocus();
				statusBar.setText("Done... Ready");*/

			}

			/**
			 * Blocks Input
			 */
			public void noInput() {

			/*	statusBar.setText("Working...");
				Submit.setEnabled(false);
				Command.setEnabled(false);*/

			}

			/**
			 * Displays a message
			 */
			public void setGameStatus(String state) {

			/*	gameStatus.setText(state);
				gameStatus.repaint();*/

			}

			/**
			 * Starts the game
			 * @param s If the game is a local game
			 */
			public void startGame(boolean s) {

			}

			/**
			 * Closes the game
			 */
			public void closeGame() {


			}

		};

	/*	gameStatus = new JLabel("");*/

		mha.addMHAListener( SimpleMHAAdapter );
		history = new Vector();
		pointer=-1;

		Console = new JTextPane();
		Console.setText("");
		doc = (StyledDocument)Console.getDocument();

		Command = new JTextField();
		Submit = new JButton();
		
//		String[] columnNames = {"Date","Evènement"};
//		Object[][] data = {};
//		setIconImage((new ImageIcon(MHAGUI.class.getResource("mha.gif"))).getImage());
		initGUI();

		setResizable(false);

	//	pack();

//		statusBar.setText("Ready");
	}

	private void reInitGui()
	{
		current_time=0;
		nbPA=0;
		trollId=-1;
		idTeam=-1;
		dlaActive=false;
		popNext=false;
		updateEquip=false;
		sorts=new int[Troll.NB_SORT];
		comps=new Hashtable<Integer,Hashtable<Integer,Integer>>();
		Vector<String []> parchemins;
		Vector<String []> potions;
		x=0;
		y=0;
		n=0;
		finish=false;
		couleurs=new Vector<Color>();
		trolls=new Vector<InfoTroll>();
		getContentPane().removeAll();
		
		initGUI();
		if(isServer)
		{
			JMenuItem demarrer = new JMenuItem("Arrêter le serveur");
			demarrer.setMnemonic('K');
			demarrer.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mha.parser("killserver");
					}
				});
			menuFile.remove(0);
			menuFile.insert(demarrer,0);
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
		public Class getColumnClass(int column)
		{
			//if(column==0)
			//	return (new ImageIcon()).getClass();
			return getValueAt(0, column).getClass();
		}
        }
	
	class CenterRenderer extends DefaultTableCellRenderer
	{
		public CenterRenderer()
		{
			setHorizontalAlignment( CENTER );
		}
 
		public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			return this;
		}
	}
	
	public final static String[] NOM_ACTION={"*** Choisissez une action ***","Se déplacer","Attaquer","Utiliser une potion/parchemin","Se concentrer","Terminer de jouer","Finir de jouer plus tard","Décaler sa DLA"};
	public final static int[] COUT_ACTION={0,1,4,2,1,0,1,0};
	public final static int[] COUT_COMP={2,2,2,2,4,4,4,2,4,1,6,2,2,1};
	//public final static int[] COUT_SORT={4,4,4,4,1,2,2,2,2,2,2,6,2,2,2,4,3,2,2,6,2,2,2,2,2,2,2,2};
	public final static int[] COUT_SORT={4,4,4,4,1,2,2,2,2,2,2,6,2,2,2,4,3,2,2,6,2,2,2,2};
	public final int[] convertSort2Check = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,15,16,17,19,21,22,24,25,26,27,14,18,20,23};
	public final int[] convertCheck2Sort = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,25,15,16,17,26,18,27,19,20,28,21,22,23,24};
	
	 
	private void updatePA()
	{
		statusBar.setText("Il vous reste "+nbPA+" PA");
		actionComboBox.removeAllItems();
		actionComboBox.setEnabled(true);
		action.setEnabled(true);
		for(int i=0;i<NOM_ACTION.length;i++)
			if(COUT_ACTION[i]<=nbPA)
				actionComboBox.addItem(NOM_ACTION[i]);
		if(tableLieux.getModel().getRowCount()>0 && tableLieux.getModel().getValueAt(0,0).toString().equals("0") && tableLieux.getModel().getValueAt(0,2).toString().equals("Portail de téléportation") && 4<=nbPA)
		{
			actionComboBox.addItem(SEPARATOR);
			actionComboBox.addItem("Prendre le portail de téléportation");
		}
		actionComboBox.addItem(SEPARATOR);
		for(int i=0;i<Troll.NOM_COMP.length;i++)
			if(COUT_COMP[i]<=nbPA && comps.get(i)!=null)
				actionComboBox.addItem(Troll.NOM_COMP[i]);
		actionComboBox.addItem(SEPARATOR);
		for(int j=0;j<Troll.NOM_SORT.length;j++)
		{
			//int i=convertCheck2Sort[j]-1;
			int i=j;
			//System.out.println(i+" "+j+" "+COUT_SORT.length+" "+sorts.length);
			if(COUT_SORT[i]<=nbPA && sorts[i]>0)
				if((i!=16 || useInvi) && (i!=19 || useTP))
					actionComboBox.addItem(Troll.NOM_SORT[i]);
		}
		if(nbPA==0)
		{
			Object[] options = {"Oui, j'ai fini", "Non, plus tard"};
			int i = JOptionPane.showOptionDialog(gui,
				"Vous n'avez plus de points d'action\nVoulez-vous terminer votre tour ?",
				"Fin du tour",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
			if (i == JOptionPane.YES_OPTION) {
				dlaActive=false;
				mha.parser("enddla");
			} 
		}
		
	}
	 
	private void initGUI() {

		// set title
		setTitle(product);
		//setIconImage(Toolkit.getDefaultToolkit().getImage( MHA.class.getResource("icon.gif") ));

		getContentPane().setLayout(new java.awt.GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.insets = new java.awt.Insets(3, 3, 3, 3);

/*		Dimension ppSize = new Dimension(677,425);

		pp.setPreferredSize(ppSize);
		pp.setMinimumSize(ppSize);
		pp.setMaximumSize(ppSize);

		pp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,0,0),1));

		pp.addMouseListener(this);
		pp.addMouseMotionListener(this);*/
		
		GridBagConstraints c2 = new GridBagConstraints();
		JPanel jp = new JPanel();
		jp.setLayout(new java.awt.GridBagLayout());

		MyTableModel model = new MyTableModel();
		tableEvent = new JTable(model);
		model.addColumn("Date");
		model.addColumn("Evènement");
		
		model = new MyTableModel(1);
		tableTrolls = new JTable(model);
		model.addColumn("");
		model.addColumn("Numéro");
		model.addColumn("Nom");
		model.addColumn("Niveau");
		model.addColumn("Race");
		
		model = new MyTableModel(1);
		tableVue = new JTable(model);
		model.addColumn("Distance");
		model.addColumn("Numéro");
		model.addColumn("Nom");
		model.addColumn("Niveau");
		model.addColumn("Race");
		model.addColumn("X");
		model.addColumn("Y");
		model.addColumn("N");
		
		model = new MyTableModel(1);
		tableLieux = new JTable(model);
		model.addColumn("Distance");
		model.addColumn("Numéro");
		model.addColumn("Nom");
		model.addColumn("X");
		model.addColumn("Y");
		model.addColumn("N");
		

		model = new MyTableModel();
		tableBM = new JTable(model);
		model.addColumn("Nom");
		model.addColumn("Description");
		model.addColumn("Durée");
		
		model = new MyTableModel();
		tableMouches = new JTable(model);
		model.addColumn("Nom");
		model.addColumn("Type");

		statusBar = new JLabel("",JLabel.CENTER);
		hourBar = new JLabel("Pas de partie en cours",JLabel.CENTER);
		
		profil=new JEditorPane("text/html","<html>");
		profil.setOpaque(true);
		profil.setEditable(false);
		profil.setBackground(Color.WHITE);
//		profil.setHorizontalAlignment(SwingConstants.CENTER);
		profil.setFont(profil.getFont().deriveFont(Font.PLAIN,12.0f));
		
		equipement=new JEditorPane("text/html","<html>");
		equipement.setOpaque(true);
		equipement.setEditable(false);
		equipement.setBackground(Color.WHITE);
//		equipement.setHorizontalAlignment(SwingConstants.CENTER);
		equipement.setFont(profil.getFont().deriveFont(Font.PLAIN,12.0f));

		Con = new JScrollPane(Console);
		
		String[] str = { "*** Choisissez une action ***" };
		actionComboBox = new JComboBox(str);
		actionComboBox.setRenderer(new ComboBoxRenderer());
		actionComboBox.addActionListener(new BlockComboListener(actionComboBox));
		actionComboBox.setEnabled(false);
		
		// Console.setBackground(Color.white); // not needed with swing
		Console.setEditable(false);

		Con.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		Con.setPreferredSize(new java.awt.Dimension(691,370));
		Con.setMinimumSize(new java.awt.Dimension(691,370));

		Command.setPreferredSize(new java.awt.Dimension(560,20));
		Command.setMinimumSize(new java.awt.Dimension(560,20));
		Command.setMaximumSize(new java.awt.Dimension(560,20));
		
		statusBar.setPreferredSize(new java.awt.Dimension(220,20));
		statusBar.setMinimumSize(new java.awt.Dimension(220,20));
		statusBar.setMaximumSize(new java.awt.Dimension(220,20));

		tableEvent.setPreferredScrollableViewportSize(new Dimension(690, 100));
		tableEvent.setDragEnabled(false);
		tableEvent.setShowHorizontalLines(true);
		tableEvent.setShowVerticalLines(true);
		tableEvent.getTableHeader().setReorderingAllowed(false);
		tableEvent.getTableHeader().setResizingAllowed(false);
		tableEvent.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn column = tableEvent.getColumnModel().getColumn(0);
		column.setPreferredWidth(100);
		column = tableEvent.getColumnModel().getColumn(1);
		column.setMinWidth(589);
		column.setPreferredWidth(589);
		column.setMaxWidth(2000);

		column.setCellRenderer(new MultiLineCellRenderer());
		 
		JScrollPane scrollPane = new JScrollPane(tableEvent);
		scrollPane.setPreferredSize(new java.awt.Dimension(697,100));
		scrollPane.setMinimumSize(new java.awt.Dimension(697,100));
		
		tableTrolls.setPreferredScrollableViewportSize(new Dimension(690, 100));
		tableTrolls.setDragEnabled(false);
		tableTrolls.setShowHorizontalLines(true);
		tableTrolls.setShowVerticalLines(true);
		tableTrolls.getTableHeader().setReorderingAllowed(false);
		TableCellRenderer centerRenderer = new ColorCellRenderer(couleurs);
		column = tableTrolls.getColumnModel().getColumn(0);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(20);
		column = tableTrolls.getColumnModel().getColumn(1);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(70);
		column.setCellEditor(new TrollEditor(mha));
		column = tableTrolls.getColumnModel().getColumn(2);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(457);
		column = tableTrolls.getColumnModel().getColumn(3);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(70);
		column = tableTrolls.getColumnModel().getColumn(4);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(70);
		tableTrolls.getTableHeader().setResizingAllowed(false);
		tableTrolls.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableTrolls.setRowSelectionAllowed(false);

		JScrollPane scrollTrollPane = new JScrollPane(tableTrolls);
		scrollTrollPane.setPreferredSize(new java.awt.Dimension(697,100));
		scrollTrollPane.setMinimumSize(new java.awt.Dimension(697,100));
				
		centerRenderer = new CenterRenderer();
		tableBM.setPreferredScrollableViewportSize(new Dimension(690, 100));
		tableBM.setDragEnabled(false);
		tableBM.setShowHorizontalLines(true);
		tableBM.setShowVerticalLines(true);
		tableBM.getTableHeader().setReorderingAllowed(false);
		column = tableBM.getColumnModel().getColumn(0);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(200);
		column = tableBM.getColumnModel().getColumn(1);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(418);
		column = tableBM.getColumnModel().getColumn(2);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(70);
		tableBM.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane scrollBMPane = new JScrollPane(tableBM);
		scrollBMPane.setPreferredSize(new java.awt.Dimension(697,100));
		scrollBMPane.setMinimumSize(new java.awt.Dimension(697,100));	
		
		tableMouches.setPreferredScrollableViewportSize(new Dimension(690, 100));
		tableMouches.setDragEnabled(false);
		tableMouches.setShowHorizontalLines(true);
		tableMouches.setShowVerticalLines(true);
		tableMouches.getTableHeader().setReorderingAllowed(false);
		column = tableMouches.getColumnModel().getColumn(0);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(589);
		column = tableMouches.getColumnModel().getColumn(1);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(100);
		tableMouches.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane scrollMouchesPane = new JScrollPane(tableMouches);
		scrollMouchesPane.setPreferredSize(new java.awt.Dimension(697,100));
		scrollMouchesPane.setMinimumSize(new java.awt.Dimension(697,100));
		
		
		tableVue.setPreferredScrollableViewportSize(new Dimension(690, 100));
		tableVue.setDragEnabled(false);
		tableVue.setShowHorizontalLines(true);
		tableVue.setShowVerticalLines(true);
		tableVue.getTableHeader().setReorderingAllowed(false);
		rendererVue=new ColorCellRenderer(new Vector<Color>());
		column = tableVue.getColumnModel().getColumn(0);
		column.setCellRenderer( rendererVue );
		column.setPreferredWidth(70);
		column = tableVue.getColumnModel().getColumn(1);
		column.setCellRenderer( rendererVue );
		column.setPreferredWidth(70);
		column.setCellEditor(new TrollEditor(mha));
		column = tableVue.getColumnModel().getColumn(2);
		column.setCellRenderer( rendererVue );
		column.setPreferredWidth(319);
		column = tableVue.getColumnModel().getColumn(3);
		column.setCellRenderer( rendererVue );
		column.setPreferredWidth(70);
		column = tableVue.getColumnModel().getColumn(4);
		column.setCellRenderer( rendererVue );
		column.setPreferredWidth(70);
		column = tableVue.getColumnModel().getColumn(5);
		column.setCellRenderer( rendererVue );
		column.setPreferredWidth(30);
		column = tableVue.getColumnModel().getColumn(6);
		column.setCellRenderer( rendererVue );
		column.setPreferredWidth(30);
		column = tableVue.getColumnModel().getColumn(7);
		column.setCellRenderer( rendererVue );
		column.setPreferredWidth(30);
		tableVue.getTableHeader().setResizingAllowed(false);
		tableVue.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableVue.setRowSelectionAllowed(false);
		
		tableLieux.setPreferredScrollableViewportSize(new Dimension(690, 100));
		tableLieux.setDragEnabled(false);
		tableLieux.setShowHorizontalLines(true);
		tableLieux.setShowVerticalLines(true);
		tableLieux.getTableHeader().setReorderingAllowed(false);
		column = tableLieux.getColumnModel().getColumn(0);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(70);
		column = tableLieux.getColumnModel().getColumn(1);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(70);
		column.setCellEditor(new LieuEditor(mha));
		column = tableLieux.getColumnModel().getColumn(2);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(460);
		column.setCellRenderer( centerRenderer );
		column = tableLieux.getColumnModel().getColumn(3);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(30);
		column = tableLieux.getColumnModel().getColumn(4);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(30);
		column = tableLieux.getColumnModel().getColumn(5);
		column.setCellRenderer( centerRenderer );
		column.setPreferredWidth(30);
		tableLieux.getTableHeader().setResizingAllowed(false);
		tableLieux.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		JPanel tablePane =new JPanel();
		tablePane.setLayout(new BoxLayout(tablePane,BoxLayout.PAGE_AXIS));
		JLabel jl = new JLabel("Trolls");
		tablePane.add(jl);
		jl.setFont(jl.getFont().deriveFont(jl.getFont().getSize2D()+10));
		tablePane.add(tableVue.getTableHeader() );
		tablePane.add(tableVue);
		tablePane.add(Box.createVerticalStrut(20));
		jl = new JLabel("Lieux", null,	SwingConstants.CENTER);
		jl.setFont(jl.getFont().deriveFont(jl.getFont().getSize2D()+10));
		tablePane.add(jl);
		tablePane.add(tableLieux.getTableHeader() );
		tablePane.add(tableLieux);
//		tablePane.add(Box.createHorizontalStrut(10));
		
		JScrollPane scrollProfilPane = new JScrollPane(profil);
		scrollProfilPane.setPreferredSize(new java.awt.Dimension(697,100));
		scrollProfilPane.setMinimumSize(new java.awt.Dimension(697,100));
		scrollProfilPane.getVerticalScrollBar().setUnitIncrement(10);
		
		JScrollPane scrollEquipPane = new JScrollPane(equipement);
		scrollEquipPane.setPreferredSize(new java.awt.Dimension(697,100));
		scrollEquipPane.setMinimumSize(new java.awt.Dimension(697,100));
		scrollEquipPane.getVerticalScrollBar().setUnitIncrement(10);
		
		messages = new JTabbedPane(JTabbedPane.BOTTOM,JTabbedPane.SCROLL_TAB_LAYOUT);
		JTextArea jta=new JTextArea();
		jta.setLineWrap(true);
		jta.setEditable(false);
		final JButton send = new JButton();
		send.setText("Envoyer");
		JScrollPane scroll=new JScrollPane(jta);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		messages.addTab("Canal public",null,scroll,"Le canal de tous les trolls");
		messages.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				JTabbedPane pane = (JTabbedPane)evt.getSource();
				int sel = pane.getSelectedIndex();
				if(sel==0)
					pane.setForegroundAt(0,Color.BLACK);
				else if(sel==1)
					pane.setForegroundAt(1,Color.BLACK);
				else if(sel==2 && idTeam!=-1)
					pane.setForegroundAt(2,Color.BLACK);
				else if(idTeam!=-1)
				{
					String s="";
					for(int i=0;i<trolls.size();i++)
						if(trolls.elementAt(i).getId()==listeTab.elementAt(sel-3))
						{
							s=trolls.elementAt(i).getNom();
						}
					pane.setForegroundAt(sel,Color.BLACK);
				}
				else
				{
					String s="";
					for(int i=0;i<trolls.size();i++)
						if(trolls.elementAt(i).getId()==listeTab.elementAt(sel-2))
						{
							s=trolls.elementAt(i).getNom();
						}
					pane.setForegroundAt(sel,Color.BLACK);
				}
				if(sel!=1)
					send.setEnabled(true);
				else
					send.setEnabled(false);
			}
		});
		
		texteMessage = new JTextField();
		texteMessage.setPreferredSize(new java.awt.Dimension(550,20));
		texteMessage.setMinimumSize(new java.awt.Dimension(550,20));
		texteMessage.setMaximumSize(new java.awt.Dimension(550,20));
		
		JPanel jp2 = new JPanel();
		jp2.setLayout(new BorderLayout());
		jp2.add(messages);
		JPanel jp3 = new JPanel();
		jp3.setLayout(new BoxLayout(jp3, BoxLayout.LINE_AXIS));
		jp3.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		jp3.add(Box.createHorizontalGlue());
		jp3.add(texteMessage);
		jp3.add(send);
		jp3.add(Box.createHorizontalGlue());
		jp2.add(jp3,BorderLayout.SOUTH);
		
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setPreferredSize(new java.awt.Dimension(697,425));
		tabbedPane.setMinimumSize(new java.awt.Dimension(697,425));
		tabbedPane.setMaximumSize(new java.awt.Dimension(697,425));
		tabbedPane.addTab("Profil", null, scrollProfilPane, "Le profil de votre troll");
		tabbedPane.addTab("Vue", null, tablePane, "La vue de votre troll");
		tabbedPane.addTab("Equipement", null, scrollEquipPane, "L'équipement de votre troll");
		tabbedPane.addTab("Mouches", null, scrollMouchesPane, "Les mouches de votre troll");
		tabbedPane.addTab("Bonus/Malus", null, scrollBMPane, "La liste des bonus/malus actifs");
		tabbedPane.addTab("Trolls", null, scrollTrollPane, "Liste des trolls");
		tabbedPane.addTab("Messages", null, jp2, "Liste des messages");
		tabbedPane.addTab("Evénements", null, scrollPane, "Liste des derniers événements");
		tabbedPane.addTab("Console", null, jp, "La console de débugage");
//		tabbedPane.setTabPlacement(JTabbedPane.LEFT);
		
		Submit.setText("Envoyer");

		action = new JButton();
		action.setText("Action");
		action.setPreferredSize(new java.awt.Dimension(100,20));
		action.setMinimumSize(new java.awt.Dimension(100,20));
		action.setMaximumSize(new java.awt.Dimension(100,20));
		action.setEnabled(false);
		
		actionComboBox.setPreferredSize(new java.awt.Dimension(300,20));
		actionComboBox.setMinimumSize(new java.awt.Dimension(300,20));
		actionComboBox.setMaximumSize(new java.awt.Dimension(300,20));
		
		statusBar.setPreferredSize(new java.awt.Dimension(200,20));
		statusBar.setMinimumSize(new java.awt.Dimension(200,20));
		statusBar.setMaximumSize(new java.awt.Dimension(200,20));

		c.gridx = 0; // col
		c.gridy = 0; // row
		c.gridwidth = 3; // width
		c.gridheight = 1; // height
		// add hour bar
		getContentPane().add(hourBar, c);
		
		c.gridx = 0; // col
		c.gridy = 1; // row
		c.gridwidth = 3; // width
		c.gridheight = 2; // height

		getContentPane().add(tabbedPane, c); // Pix

//		c.fill = GridBagConstraints.BOTH;

		c.gridx = 2; // col
		c.gridy = 3; // row
		c.gridwidth = 1; // width
		c.gridheight = 1; // height
		getContentPane().add(action, c);

		c2.gridx = 1; // col
		c2.gridy = 1; // row
		c2.gridwidth = 1; // width
		c2.gridheight = 1; // height
		jp.add(Submit, c2);
		c2.gridx = 0; // col
		c2.gridy = 1; // row
		c2.gridwidth = 1; // width
		c2.gridheight = 1; // height
		jp.add(Command, c2);
		c2.gridx = 0; // col
		c2.gridy = 0; // row
		c2.gridwidth = 2; // width
		c2.gridheight = 1; // height
		jp.add(Con, c2);

/*		c.gridx = 0; // col
		c.gridy = 2; // row
		c.gridwidth = 3; // width
		c.gridheight = 1; // height
		getContentPane().add(Con, c);
*/
		c.gridx = 1; // col
		c.gridy = 3; // row
		c.gridwidth = 1; // width
		c.gridheight = 1; // height
		getContentPane().add(actionComboBox, c);

		c.gridx = 0; // col
		c.gridy = 3; // row
		c.gridwidth = 1; // width
		c.gridheight = 1; // height
		// add status bar
		getContentPane().add(statusBar, c);

		ActionListener readCommand = new ActionListener() {
			public void actionPerformed(ActionEvent a) {

				String input = Command.getText();
				Command.setText("");

				history.add(input);
				pointer = history.size()-1;
				go(input);

			}
		};
		
		ActionListener sendAction = new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				String commande = (String) actionComboBox.getSelectedItem();
				analyseAction(commande);
			}
		};
		
		ActionListener readMessage = new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				String input = texteMessage.getText();
				if(input.equals(""))
					return;
				texteMessage.setText("");
				String s="all";
				if(idTeam!=-1 && messages.getSelectedIndex()==2)
					s="groupe";
				else if(messages.getSelectedIndex()>1 && idTeam!=-1)
					s=""+listeTab.elementAt(messages.getSelectedIndex()-3);
				else if(messages.getSelectedIndex()>1)
					s=""+listeTab.elementAt(messages.getSelectedIndex()-2);
				if(messages.getSelectedIndex()!=1)
					mha.parser("message "+s+" "+input);
			}
		};

		Submit.addActionListener( readCommand );
		action.addActionListener( sendAction );
		Command.addActionListener( readCommand );
		send.addActionListener( readMessage );
		texteMessage.addActionListener( readMessage );


		class CommandKeyAdapter extends KeyAdapter {
			MHAGUI adaptee;


			CommandKeyAdapter(MHAGUI adaptee) {
				this.adaptee = adaptee;
			}
			public void keyPressed(KeyEvent key) {


				if (key.getKeyCode() == 38) {

					if (pointer < 0) {
						Toolkit.getDefaultToolkit().beep();
					}
					else {
						if (pointer == history.size()-1) { temptext=Command.getText(); }
						Command.setText( (String)history.elementAt(pointer) );
						pointer--;
					}
				}
				else if(key.getKeyCode() == 40) {

					if (pointer > history.size()-2 ) {
						Toolkit.getDefaultToolkit().beep();
					}
					else if (pointer == history.size()-2 ) {
						Command.setText(temptext);
						pointer++;
					}
					else {
						pointer=pointer+2;
						Command.setText( (String)history.elementAt(pointer) );
						pointer--;
					}

				}
				else {
					pointer = history.size()-1;
				}

			}
		}

		Command.addKeyListener( new CommandKeyAdapter(this) );


		// add menu bar
		JMenuBar menuBar = new JMenuBar();
		menuFile = new JMenu("Partie");
		menuFile.setMnemonic('F');

		// create menu item
		JMenu menuLAF = new JMenu("Apparence");
		UIManager.LookAndFeelInfo[] lafi = UIManager.getInstalledLookAndFeels();
		ButtonGroup group = new ButtonGroup();
		for(int i=0;i<lafi.length;i++)
		{
			final JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem(lafi[i].getName());
			final UIManager.LookAndFeelInfo laf=lafi[i];
			//rbMenuItem.setSelected(true);
			rbMenuItem.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try{
						UIManager.setLookAndFeel(laf.getClassName());
						rbMenuItem.setSelected(true);
						SwingUtilities.updateComponentTreeUI(gui);
						}
						catch(Exception ex) {ex.printStackTrace();}
					}
				});
			if(laf.getName().equals(UIManager.getLookAndFeel().getName()))
				rbMenuItem.setSelected(true);
			group.add(rbMenuItem);
			menuLAF.add(rbMenuItem);
		}

		// create About menu item
		JMenu menuHelp = new JMenu("Aide");
		menuHelp.setMnemonic('H');

		JMenuItem Commands = new JMenuItem("Liste des commandes");
		Commands.setMnemonic('C');
		Commands.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Commands();
					}
				});
		menuHelp.add(Commands);

		JMenuItem helpMan = new JMenuItem("A propos");
		helpMan.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						openAbout();
					}
				});
		menuHelp.add(helpMan);

		// create Clear menu item
		JMenu menuClear = new JMenu("Effacement");
		menuClear.setMnemonic('C');

		JMenuItem ClearConsole = new JMenuItem("Vider la console");
		ClearConsole.setMnemonic('C');
		ClearConsole.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Console.setText("");
					}
				});
		menuClear.add(ClearConsole);

		JMenuItem ClearHistory = new JMenuItem("Vider l'historique");
		ClearHistory.setMnemonic('H');
		ClearHistory.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						history.clear();
						pointer = -1;
					}
				});
		menuClear.add(ClearHistory);

		// create Open menu item
		
		JMenuItem demarrer = new JMenuItem("Démarrer le serveur");
		demarrer.setMnemonic('S');
		demarrer.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new LaunchServer(gui,mha);
					}
				});
		menuFile.add(demarrer);

		JMenuItem join = new JMenuItem("Rejoindre une partie");
		join.setMnemonic('J');
		join.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String s = (String)JOptionPane.showInputDialog(gui,
                                        		"Indiquez l'adresse du serveur:",
                                        		"Rejoindre une partie",
                                        		JOptionPane.PLAIN_MESSAGE,
                                        		null,
                                        		null,
                                        		"localhost");
                    				//If a string was returned, say so.
                    				if ((s != null) && (s.length() > 0)) {
                        				mha.parser("join " + s);
						}
					}
				});
		menuFile.add(join);
		JMenuItem importation = new JMenuItem("Importer un troll");
		importation.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ImportationTroll it=new ImportationTroll(gui);
					}
				});
		menuFile.add(importation);

		//création de troll
		JMenuItem creation = new JMenuItem("Créer un troll");
		creation.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						CreationTroll ct=new CreationTroll(gui);
					}
				});
		menuFile.add(creation);
		
		
		// create Exit menu item
		JMenuItem fileExit = new JMenuItem("Quitter");
		fileExit.setMnemonic('E');
		fileExit.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
		menuFile.add(fileExit);

		menuBar.add(menuFile);
		menuBar.add(menuClear);
		menuBar.add(menuLAF);
		menuBar.add(menuHelp);

		// sets menu bar
		setJMenuBar(menuBar);
//		setBounds(new java.awt.Rectangle(0,0,915,609));
		addWindowListener(
				new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent evt) {
						exitForm();
					}
				});

		pack();
		setSize(getWidth()+10,getHeight()+10);

	}

	private void exitForm() {

		System.exit(0);
	}
	
	private void analyseAction(String s)
	{
		int id=-1;
		if(s.equals("Prendre le portail de téléportation"))
		{
			Object[] options = {"Oui", "Non"};
			int n = JOptionPane.showOptionDialog(gui,
					"Êtes vous sûr de vouloir prendre ce portail de téléportation ?",
					"Téléportation",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]);
			if (n == JOptionPane.YES_OPTION)
			{
				popNext=true;
				mha.parser("prendretp");
			}
			return;
		}
		for(int i=0;i<NOM_ACTION.length;i++)
		{
			if(s.equals(NOM_ACTION[i]))
			{
				id=i;
				break;
			}
		}
		switch(id)
		{
			case 0: return;
			case 1: dialogDeplacement("deplace ");return;
			case 2: dialogTrollCible("Attaquer :","attaque ");return;
			case 3: dialogTrollPopo(false);return;
			case 4: String s1 = (String)JOptionPane.showInputDialog(gui,
					"Pendant combien de PA voulez-vous vous concentrer ?",
					"Se concentrer",
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					"");
				if ((s1 != null) && (s1.length() > 0)) {
					popNext=true;
					mha.parser("concentre "+s1);
				}
				return;
			case 5: mha.parser("enddla");dlaActive=false;return;
			case 6: s = (String)JOptionPane.showInputDialog(gui,
					"Dans combien de minutes voulez-vous finir votre tour de jeu ?",
					"Jouer plus tard",
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					"");
				if ((s != null) && (s.length() > 0)) {
					int i=Integer.parseInt(s);
					mha.parser("decaletour "+i);
				}
				return;
			case 7: s = (String)JOptionPane.showInputDialog(gui,
					"De combien de minutes voulez-vous décaler votre DLA ?",
					"Décalage de DLA",
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					"");
				if ((s != null) && (s.length() > 0)) {
					int i=Integer.parseInt(s);
					mha.parser("decaledla "+i);
				}
				return;
		}
		for(int i=0;i<Troll.NOM_COMP.length;i++)
		{
			if(s.equals(Troll.NOM_COMP[i]))
			{
				id=i;
				break;
			}
		}
		if(id!=-1)
		{
			if(MHAServer.listeCompetences[id].length==1)
			{
				popNext=true;
				mha.parser("comp "+(id+1));
			}
			//les cas Charger et pistage sont différents
			else if(MHAServer.listeCompetences[id].length==2 && MHAServer.listeCompetences[id][1].equals("Troll") && id!=5 && id!=13)
				dialogTrollCible("Utiliser la compétence "+s+" sur :","comp "+(id+1)+" ");
			else if(id==5)
				dialogAllVisibleTroll("Utiliser la compétence "+s+" sur :","comp "+(id+1)+" ");
			else if(id==13)
				dialogAllTroll("Utiliser la compétence "+s+" sur :","comp "+(id+1)+" ");
			else if(id==9)
				dialogDeplacement("comp 10 ");
			else if(id==2)
			{
				String s1 = (String)JOptionPane.showInputDialog(gui,
					"De combien de point de vie voulez vous accélérer ?",
					"Accélérer",
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					"");
				if ((s1 != null) && (s1.length() > 0)) {
					popNext=true;
					mha.parser("comp 3 "+s1);
				}
			}
			else if(id==11)
				dialogTrollPopo(true);
			return;
		}
		for(int i=0;i<Troll.NOM_SORT.length;i++)
		{
			if(s.equals(Troll.NOM_SORT[i]))
			{
				id=i;
				break;
			}
		}
		if(id!=-1)
		{
			if(MHAServer.listeSortileges[id].length==1)
			{
				popNext=true;
				mha.parser("sort "+(id+1));
			}
			//les cas AA et Projo et Glue sont différents
			else if(MHAServer.listeSortileges[id].length==2 && MHAServer.listeSortileges[id][1].equals("Troll") && id!=3 && id!=4&& id!=14 && id!=12 && id!=23 && id!=24)
				dialogTrollCible("Utiliser le sortilège "+s+" sur :","sort "+(id+1)+" ");
			else if(MHAServer.listeSortileges[id].length==4)
				dialogCase("Utiliser le sortilège "+s+" sur la case :","sort "+(id+1)+" ");
			else if(id==3 || id==4 || id==14)
				dialogAllVisibleTroll("Utiliser le sortilège "+s+" sur :","sort "+(id+1)+" ");
			else if(id==12 || id==23 || id==24)
				dialogTrollCible("Utiliser le sortilège "+s+" sur :","sort "+(id+1)+" ",1,0);
			else if(id==18)
				dialogTrollSacro("sort 19 ");
			return;
		}		
		return;
	}
	
	private void lowLevelDialog(final JDialog dialog,JLabel label,JButton closeButton, JComboBox combo)
	{
		JPanel closePanel = new JPanel();
		JButton otherButton = new JButton("Annuler");
		otherButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		});
		combo.setPreferredSize(new Dimension(200, 25));
		combo.setMaximumSize(new Dimension(200, 25));
		combo.setMinimumSize(new Dimension(200, 25));
		closePanel.setLayout(new BoxLayout(closePanel,BoxLayout.LINE_AXIS));
		closePanel.add(Box.createHorizontalGlue());
		closePanel.add(label);
		closePanel.add(Box.createHorizontalGlue());
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.PAGE_AXIS));
		contentPane.add(Box.createVerticalGlue());
		
		contentPane.add(closePanel);
		contentPane.add(Box.createVerticalStrut(5));
		closePanel = new JPanel();
		closePanel.setLayout(new BoxLayout(closePanel,BoxLayout.LINE_AXIS));
		closePanel.add(Box.createHorizontalGlue());
		closePanel.add(otherButton);
		closePanel.add(Box.createHorizontalStrut(5));
		closePanel.add(combo);
		closePanel.add(Box.createHorizontalStrut(5));
		closePanel.add(closeButton);
		closePanel.add(Box.createHorizontalGlue());
		contentPane.add(closePanel);
		contentPane.add(Box.createVerticalGlue());
		contentPane.setOpaque(true);
		dialog.setContentPane(contentPane);
		dialog.setSize(new Dimension(400, 100));
		dialog.setLocationRelativeTo(gui);
		dialog.setVisible(true);
	}
	
	private void dialogAllVisibleTroll(String s1, String s2)
	{
		final String s=s2;
		final JDialog dialog = new JDialog(gui,"Action");
		JLabel label = new JLabel(s1);
		final JComboBox combo=new JComboBox();
		combo.addItem("*** Choisissez une cible ***");
		for(int i=1;i<tableVue.getModel().getRowCount();i++)
		{
			combo.addItem(tableVue.getModel().getValueAt(i,2)+" ("+tableVue.getModel().getValueAt(i,1)+")");
		}
		JButton closeButton = new JButton("Action");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(combo.getSelectedIndex()==0)
					return;
				dialog.setVisible(false);
				dialog.dispose();
				popNext=true;
				mha.parser(s+tableVue.getModel().getValueAt(combo.getSelectedIndex(),1));
			}
		});
		lowLevelDialog(dialog,label,closeButton,combo);
	}
	
	private void dialogAllTroll(String s1, String s2)
	{
		final String s=s2;
		final JDialog dialog = new JDialog(gui,"Action");
		JLabel label = new JLabel(s1);
		final JComboBox combo=new JComboBox();
		combo.addItem("*** Choisissez une cible ***");
		for(int i=0;i<tableTrolls.getModel().getRowCount();i++)
		{
			combo.addItem(tableTrolls.getModel().getValueAt(i,2)+" ("+tableTrolls.getModel().getValueAt(i,1)+")");
		}
		JButton closeButton = new JButton("Action");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(combo.getSelectedIndex()==0)
					return;
				dialog.setVisible(false);
				dialog.dispose();
				popNext=true;
				mha.parser(s+tableTrolls.getModel().getValueAt(combo.getSelectedIndex()-1,1));
			}
		});
		lowLevelDialog(dialog,label,closeButton,combo);
	}	
	
	private void dialogTrollSacro(final String s)
	{
		final JDialog dialog = new JDialog(gui,"Action");
		final JTextField nb=new JTextField(3);
		nb.setMaximumSize(new Dimension(30,20));
		final JComboBox combo=new JComboBox();
		combo.addItem("*** Choisissez une cible ***");
		for(int i=1;i<tableVue.getModel().getRowCount();i++)
		{
			if(((Integer) tableVue.getModel().getValueAt(i,0))>1)
				break;
			if(((Integer) tableVue.getModel().getValueAt(i,7))==n)
				combo.addItem(tableVue.getModel().getValueAt(i,2)+" ("+tableVue.getModel().getValueAt(i,1)+")");
		}
		JButton closeButton = new JButton("Action");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(combo.getSelectedIndex()==0)
					return;
				dialog.setVisible(false);
				dialog.dispose();
				popNext=true;
				mha.parser(s+((String) combo.getSelectedItem()).substring(((String) combo.getSelectedItem()).lastIndexOf("(")+1,((String) combo.getSelectedItem()).lastIndexOf(")"))+" "+nb.getText());
			}
		});
				JPanel closePanel = new JPanel();
		JButton otherButton = new JButton("Annuler");
		otherButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		});
		combo.setPreferredSize(new Dimension(200, 25));
		combo.setMaximumSize(new Dimension(200, 25));
		combo.setMinimumSize(new Dimension(200, 25));
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.LINE_AXIS));
		contentPane.add(Box.createHorizontalGlue());	
		contentPane.add(otherButton);
		contentPane.add(Box.createHorizontalStrut(20));
		contentPane.add(new JLabel("Soigner "));
		contentPane.add(Box.createHorizontalStrut(5));
		contentPane.add(combo);
		contentPane.add(Box.createHorizontalStrut(5));
		contentPane.add(new JLabel(" de "));
		contentPane.add(Box.createHorizontalStrut(5));
		contentPane.add(nb);
		contentPane.add(Box.createHorizontalStrut(5));
		contentPane.add(new JLabel(" points de vie."));
		contentPane.add(Box.createHorizontalStrut(20));
		contentPane.add(closeButton);
		contentPane.add(Box.createHorizontalGlue());
		contentPane.setOpaque(true);
		dialog.setContentPane(contentPane);
		dialog.setSize(new Dimension(700, 100));
		dialog.setLocationRelativeTo(gui);
		dialog.setVisible(true);
	}

	private void dialogTrollPopo(final boolean isLdP)
	{
		final JDialog dialog = new JDialog(gui,"Action");
		final JComboBox combo=new JComboBox();
		final JComboBox combo2=new JComboBox();
		combo.addItem("*** Choisissez une cible ***");
		if(isLdP)
		{
			for(int i=0;i<tableVue.getModel().getRowCount();i++)
			{
				combo.addItem(tableVue.getModel().getValueAt(i,2)+" ("+tableVue.getModel().getValueAt(i,1)+")");
			}
		}
		else
			for(int i=0;i<tableVue.getModel().getRowCount();i++)
			{
				if(!((Integer) tableVue.getModel().getValueAt(i,0)).equals(0))
					break;
				combo.addItem(tableVue.getModel().getValueAt(i,2)+" ("+tableVue.getModel().getValueAt(i,1)+")");
			}
		JButton closeButton = new JButton("Action");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(combo.getSelectedIndex()==0 || combo2.getSelectedIndex()==0)
					return;
				dialog.setVisible(false);
				dialog.dispose();
				popNext=true;
				updateEquip=true;
				//Mettre à jour l'équipement !!
				if(!isLdP)
				{
					String s="";
					if(combo2.getSelectedIndex()<=potions.size())
						s=potions.elementAt(combo2.getSelectedIndex()-1)[0];
					else
						s=parchemins.elementAt(combo2.getSelectedIndex()-1-potions.size())[0];
					mha.parser("utilise "+tableVue.getModel().getValueAt(combo.getSelectedIndex()-1,1)+" "+s);
				}
				else
				{
					mha.parser("comp 12 "+tableVue.getModel().getValueAt(combo.getSelectedIndex()-1,1)+" "+potions.elementAt(combo2.getSelectedIndex()-1)[0]);
				}
			}
		});
				JPanel closePanel = new JPanel();
		JButton otherButton = new JButton("Annuler");
		otherButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		});
		combo2.addItem("*** Choisissez un équipement ***");
		for(int i=0;i<potions.size();i++)
			combo2.addItem("["+potions.elementAt(i)[0]+"] "+potions.elementAt(i)[4]);
		if(!isLdP)
			for(int i=0;i<parchemins.size();i++)
				combo2.addItem("["+parchemins.elementAt(i)[0]+"] "+parchemins.elementAt(i)[4]);	
		combo.setPreferredSize(new Dimension(200, 25));
		combo.setMaximumSize(new Dimension(300, 25));
		combo.setMinimumSize(new Dimension(200, 25));
		combo2.setPreferredSize(new Dimension(250, 25));
		combo2.setMaximumSize(new Dimension(300, 25));
		combo2.setMinimumSize(new Dimension(200, 25));
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.LINE_AXIS));
		contentPane.add(Box.createHorizontalGlue());	
		contentPane.add(otherButton);
		contentPane.add(Box.createHorizontalStrut(5));
		if(isLdP)
			contentPane.add(new JLabel("Lancer "));
		else
			contentPane.add(new JLabel("Utiliser "));
		contentPane.add(Box.createHorizontalStrut(5));
		contentPane.add(combo2);
		contentPane.add(Box.createHorizontalStrut(5));
		contentPane.add(new JLabel("sur"));
		contentPane.add(Box.createHorizontalStrut(5));
		contentPane.add(combo);
		contentPane.add(Box.createHorizontalStrut(5));
		contentPane.add(closeButton);
		contentPane.add(Box.createHorizontalGlue());
		contentPane.setOpaque(true);
		dialog.setContentPane(contentPane);
		dialog.setSize(new Dimension(750, 100));
		dialog.setLocationRelativeTo(gui);
		dialog.setVisible(true);
	}

	private void dialogTrollCible(String s1, String s2)
	{
		final String s=s2;
		final JDialog dialog = new JDialog(gui,"Action");
		JLabel label = new JLabel(s1);
		final JComboBox combo=new JComboBox();
		combo.addItem("*** Choisissez une cible ***");
		for(int i=1;i<tableVue.getModel().getRowCount();i++)
		{
			if(!((Integer) tableVue.getModel().getValueAt(i,0)).equals(0))
				break;
			combo.addItem(tableVue.getModel().getValueAt(i,2)+" ("+tableVue.getModel().getValueAt(i,1)+")");
		}
		JButton closeButton = new JButton("Action");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(combo.getSelectedIndex()==0)
					return;
				dialog.setVisible(false);
				dialog.dispose();
				popNext=true;
				mha.parser(s+tableVue.getModel().getValueAt(combo.getSelectedIndex(),1));
			}
		});
		lowLevelDialog(dialog,label,closeButton,combo);
	}	
	
	private void dialogTrollCible(String s1, String s2,int dh,int dv)
	{
	try
	{
		final String s=s2;
		final JDialog dialog = new JDialog(gui,"Action");
		JLabel label = new JLabel(s1);
		int x=((Integer) tableVue.getModel().getValueAt(0,5));
		int y=((Integer) tableVue.getModel().getValueAt(0,6));
		int n=((Integer) tableVue.getModel().getValueAt(0,7));
		final JComboBox combo=new JComboBox();
		combo.addItem("*** Choisissez une cible ***");
		for(int i=1;i<tableVue.getModel().getRowCount();i++)
		{
			if(((Integer) tableVue.getModel().getValueAt(i,0))>dh)
				break;
			if(Math.max(Math.abs(x-((Integer) tableVue.getModel().getValueAt(i,5))),Math.abs(y-((Integer) tableVue.getModel().getValueAt(i,6))))<= dh && 
				Math.abs(n-((Integer) tableVue.getModel().getValueAt(i,7)))<=dv)
				combo.addItem(tableVue.getModel().getValueAt(i,2)+" ("+tableVue.getModel().getValueAt(i,1)+")");
		}
		JButton closeButton = new JButton("Action");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(combo.getSelectedIndex()==0)
					return;
				dialog.setVisible(false);
				dialog.dispose();
				String tmp=(String) combo.getSelectedItem();
				tmp=tmp.substring(tmp.lastIndexOf("(")+1,tmp.lastIndexOf(")"));
				popNext=true;
				mha.parser(s+tmp);
			}
		});
		lowLevelDialog(dialog,label,closeButton,combo);
	}
	catch(Exception e) {e.printStackTrace();}
	}
	
	private void dialogChooseTeam(Vector <Color> vc)
	{
	try
	{
		final JDialog dialog = new JDialog(gui,"Choix de l'équipe");
		JLabel label = new JLabel("Sélection de votre équipe :");
		final JComboBox combo=new JComboBox();
		combo.addItem("*** Choisissez une équipe ***");
		combo.setRenderer(new ComboBoxRenderer());
		for(int i=0;i<vc.size();i++)
		{
			combo.addItem(vc.elementAt(i));
		}
		JButton closeButton = new JButton("Choisir");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(combo.getSelectedIndex()==0)
					return ;
				dialog.setVisible(false);
				dialog.dispose();
				mha.parser("setteam "+(combo.getSelectedIndex()-1));
				return ;
			}
		});
		JPanel closePanel = new JPanel();
		JButton otherButton = new JButton("Annuler");
		otherButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
				return ;
			}
		});
		combo.setPreferredSize(new Dimension(200, 25));
		combo.setMaximumSize(new Dimension(200, 25));
		combo.setMinimumSize(new Dimension(200, 25));
		closePanel.setLayout(new BoxLayout(closePanel,BoxLayout.LINE_AXIS));
		closePanel.add(Box.createHorizontalGlue());
		closePanel.add(label);
		closePanel.add(Box.createHorizontalGlue());
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.PAGE_AXIS));
		contentPane.add(Box.createVerticalGlue());
		
		contentPane.add(closePanel);
		contentPane.add(Box.createVerticalStrut(5));
		closePanel = new JPanel();
		closePanel.setLayout(new BoxLayout(closePanel,BoxLayout.LINE_AXIS));
		closePanel.add(Box.createHorizontalGlue());
		closePanel.add(otherButton);
		closePanel.add(Box.createHorizontalStrut(5));
		closePanel.add(combo);
		closePanel.add(Box.createHorizontalStrut(5));
		closePanel.add(closeButton);
		closePanel.add(Box.createHorizontalGlue());
		contentPane.add(closePanel);
		contentPane.add(Box.createVerticalGlue());
		contentPane.setOpaque(true);
		dialog.setContentPane(contentPane);
		dialog.setSize(new Dimension(400, 100));
		dialog.setLocationRelativeTo(gui);
		dialog.setVisible(true);
	}
	catch(Exception e) {e.printStackTrace();}
	}
	
	private void dialogDeplacement(final String s)
	{
		try
		{
			int x=((Integer) tableVue.getModel().getValueAt(0,5));
			int y=((Integer) tableVue.getModel().getValueAt(0,6));
			int n=((Integer) tableVue.getModel().getValueAt(0,7));
			final ButtonGroup groupX = new ButtonGroup();
			final ButtonGroup groupY = new ButtonGroup();
			final ButtonGroup groupN = new ButtonGroup();
			
			final JDialog dialog = new JDialog(gui,"Déplacement");
			
			JButton closeButton = new JButton("Action");
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.setVisible(false);
					dialog.dispose();
					popNext=true;
					mha.parser(s+groupX.getSelection().getActionCommand()+" "+groupY.getSelection().getActionCommand()+" "+groupN.getSelection().getActionCommand());
				}	
			});
			JButton otherButton = new JButton("Annuler");
			otherButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
		
			JRadioButton x0 = new JRadioButton("-1 ("+(x-1)+")");
			x0.setActionCommand("-1");
			if(x-1<0)
				x0.setEnabled(false);
			JRadioButton x1 = new JRadioButton("0 ("+(x)+")");
			x1.setActionCommand("0");
			x1.setSelected(true);
			JRadioButton x2 = new JRadioButton("1 ("+(x+1)+")");
			x2.setActionCommand("1");
			if(x+1>=size)
				x2.setEnabled(false);
			groupX.add(x0);
			groupX.add(x1);
			groupX.add(x2);
			
			JRadioButton y0 = new JRadioButton("-1 ("+(y-1)+")");
			y0.setActionCommand("-1");
			if(y-1<0)
				y0.setEnabled(false);
			JRadioButton y1 = new JRadioButton("0 ("+(y)+")");
			y1.setActionCommand("0");
			y1.setSelected(true);
			JRadioButton y2 = new JRadioButton("1 ("+(y+1)+")");
			y2.setActionCommand("1");
			if(y+1>=size)
				y2.setEnabled(false);
			groupY.add(y0);
			groupY.add(y1);
			groupY.add(y2);
			
			JRadioButton n0 = new JRadioButton("-1 ("+(n-1)+")");
			n0.setActionCommand("-1");
			if(n-1<-(size+1)/2)
				n0.setEnabled(false);
			JRadioButton n1 = new JRadioButton("0 ("+(n)+")");
			n1.setActionCommand("0");
			n1.setSelected(true);
			JRadioButton n2 = new JRadioButton("1 ("+(n+1)+")");
			n2.setActionCommand("1");
			if(n+1>=0)
				n2.setEnabled(false);
			groupN.add(n0);
			groupN.add(n1);
			groupN.add(n2);
			
			JPanel tablePane =new JPanel();
			tablePane.setLayout(new GridLayout(3,4,2,2));
			tablePane.add(new JLabel("X ->"));
			tablePane.add(x0);
			tablePane.add(x1);
			tablePane.add(x2);
			tablePane.add(new JLabel("Y ->"));
			tablePane.add(y0);
			tablePane.add(y1);
			tablePane.add(y2);
			tablePane.add(new JLabel("N ->"));
			tablePane.add(n0);
			tablePane.add(n1);
			tablePane.add(n2);
			
			JPanel contentPane = new JPanel();
			contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.LINE_AXIS));
			contentPane.add(Box.createHorizontalGlue());
		
			contentPane.add(otherButton);
			contentPane.add(Box.createHorizontalStrut(5));
			contentPane.add(tablePane);
			contentPane.add(Box.createHorizontalStrut(5));
			contentPane.add(closeButton);
			contentPane.add(Box.createHorizontalGlue());
			contentPane.setOpaque(true);
			dialog.setContentPane(contentPane);
			dialog.setSize(new Dimension(500, 100));
			dialog.setLocationRelativeTo(gui);
			dialog.setVisible(true);
		}
		catch(Exception e) {e.printStackTrace();}
	}
	
	
	
	
	private void dialogCase(String s1,final String s2)
	{
		try
		{
			final JTextField x=new JTextField(3);
			final JTextField y=new JTextField(3);
			final JTextField n=new JTextField(3);
			
			x.setMaximumSize(new Dimension(30,20));
			y.setMaximumSize(new Dimension(30,20));
			n.setMaximumSize(new Dimension(30,20));
			
			final JDialog dialog = new JDialog(gui,"Action");
			
			JButton closeButton = new JButton("Action");
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.setVisible(false);
					dialog.dispose();
					popNext=true;
					mha.parser(s2+x.getText()+" "+y.getText()+" "+n.getText());
				}	
			});
			JButton otherButton = new JButton("Annuler");
			otherButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			
			JPanel tablePane =new JPanel();
			tablePane.setLayout(new BoxLayout(tablePane,BoxLayout.LINE_AXIS));
			tablePane.add(Box.createHorizontalGlue());
			tablePane.add(otherButton);
			tablePane.add(Box.createHorizontalStrut(10));
			tablePane.add(new JLabel("X ="));
			tablePane.add(Box.createHorizontalStrut(5));
			tablePane.add(x);
			tablePane.add(Box.createHorizontalStrut(10));
			tablePane.add(new JLabel("Y ="));
			tablePane.add(Box.createHorizontalStrut(5));
			tablePane.add(y);
			tablePane.add(Box.createHorizontalStrut(10));
			tablePane.add(new JLabel("N ="));
			tablePane.add(Box.createHorizontalStrut(5));
			tablePane.add(n);
			tablePane.add(Box.createHorizontalStrut(10));
			tablePane.add(closeButton);
			tablePane.add(Box.createHorizontalGlue());
			
			JPanel contentPane = new JPanel();
			contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.PAGE_AXIS));
			contentPane.add(Box.createVerticalGlue());
			contentPane.add(new JLabel(s1,SwingConstants.CENTER));
			contentPane.add(Box.createVerticalStrut(5));
			contentPane.add(tablePane);
			contentPane.add(Box.createVerticalGlue());
			contentPane.setOpaque(true);
			dialog.setContentPane(contentPane);
			dialog.setSize(new Dimension(500, 100));
			dialog.setLocationRelativeTo(gui);
			dialog.setVisible(true);
		}
		catch(Exception e) {e.printStackTrace();}
	}
			
	/**
	 * Submits input to parser if neccessary
	 * @param input The string that is checked
	 */
	public void go(String input) {

		if (input.equals("exit") ) {
			System.exit(0);
		}
		else if (input.equals("help") ) {
			Commands();
		}
		else if (input.equals("clear") ) {
			Console.setText("");
		}
		else {
			if(strcmp(input,"enddla"))
				dlaActive=false;
			if(strcmp(input,"comp ") || strcmp(input,"sort ") || strcmp(input,"deplace ") || strcmp(input,"attaque "))
				popNext=true;
			//statusBar.setText("Working...");
			//Submit.setEnabled(false);
			//Command.setEnabled(false);
			mha.parser(input);
		}

	}

	
	private void updateProfil(String s)
	{
		try 
		{
			InfoTroll it=null;
			String [] ls=s.split(";");
			for(int i=0;i<trolls.size();i++)
				if(trolls.elementAt(i).getId()==trollId)
				{
					it=trolls.elementAt(i);
					break;
				}
			String pro="<html>"+
				"<h2 align=\"center\"><b>MON PROFIL</b></h2>"+
				"<center>"+
				"<table align=\"center\" border=\"1\" cellpadding=\"5\" cellspacing=\"1\">"+
				"  <tr > "+
				"    <td valign=\"top\" width=\"150\"><b>Description</b></td>"+
				"    <td valign=\"top\"> Identifiants..............: "+it.getId()+" - "+it.getNom()+"<br>Race........................: "+it.getRace()+" <br></td>"+
				"  </tr>"+
				"  <tr> "+
				"    <td valign=\"top\"><b>Echéance du Tour</b></td>"+
				"    <td valign=\"top\">Date Limite d'Action : <b> "+hour2string(Integer.parseInt(ls[0]))+" </b><br>Il me reste <b> "+nbPA+" PA</b> sur un total de 6 "+
				"       <p>Durée normale de mon Tour.............: "+Troll.convertTime(Integer.parseInt(ls[1]))+" <br>"+
				"          Bonus/Malus sur la durée.................: "+Troll.convertTime(Integer.parseInt(ls[2]))+".<br>"+
				"          Augmentation due aux blessures.......: "+Troll.convertTime(Integer.parseInt(ls[3]))+" <br>"+
				"          Poids de l'équipement......................: "+Troll.convertTime(Integer.parseInt(ls[4]))+".<br></p><p>"+
				"          <b>Durée de mon prochain Tour.....: "+
					Troll.convertTime(Math.max(Integer.parseInt(ls[1]),Integer.parseInt(ls[1])+Integer.parseInt(ls[2])+Integer.parseInt(ls[3])+Integer.parseInt(ls[4])))+".</b>"+
				"       </p>"+
				"    </td>"+
				"  </tr>"+
				"  <tr> "+
				"    <td valign=\"top\"><b>Position</b></td>"+
				"    <td valign=\"top\">X = "+ls[5]+" | Y = "+ls[6]+" | N = "+ls[7]+"<br>Vue.......: "+ls[8]+" Cases "+pm(ls[9])+" <br>";
			if(ls.length>33 && ls[32].equals("1"))
				pro+="<B><FONT COLOR=\"#FF0000\">[Camouflé]</FONT></B><br>";
			if(ls.length>34 && ls[33].equals("1"))
				pro+="<B><FONT COLOR=\"#FF0000\">[Invisible]</FONT></B><br>";
			pro+=	"</td>"+
				"  </tr>"+
				"  <tr> "+
				"    <td valign=\"top\"><b>Expérience</b></td>"+
				"    <td valign=\"top\"> Niveau........: "+ls[10]+"</td>"+
				"  </tr>"+
				"  <tr> "+
				"    <td valign=\"top\"><b>Point de Vie</b></td>"+
				"    <td valign=\"top\"> "+
				"      Actuels............: <b> "+ls[11]+" </b><br>"+
				"      Maximum.........: "+ls[12]+"<br>";
			if(Integer.parseInt(ls[11])<=0)
				dead=true;
			if(it.getRaceById()==2)
			{
				int f=Integer.parseInt(ls[15]);
				if(f<=4)
					pro+="      Fatigue du Kastar : "+f+" (1 PV = 30')<br>";
				else
					pro+="      Fatigue du Kastar : "+f+" (1 PV = "+((120/((f/10+1)*f)))+"')<br>";
			}
			pro+="      Régénération....: "+ls[13]+" D3 "+ls[14]+" (moyenne : "+(2*Integer.parseInt(ls[13])+Integer.parseInt(ls[14].split("/")[0].replaceAll("\\+", ""))+Integer.parseInt(ls[14].split("/")[1].replaceAll("\\+", "")))+")"+
				"    </td>"+
				"  </tr>"+
				"  <tr>"+
				"    <td valign=\"top\"><b>Combat</b></td>"+
				"    <td valign=\"top\">Attaque.....: "+ls[16]+" D6 "+ls[17]+" (moyenne : "+(7*Integer.parseInt(ls[16])/2+Integer.parseInt(ls[17].split("/")[0].replaceAll("\\+", "")))+")<br>"+
				"      Esquive.....: "+ls[18]+" D6 "+ls[19]+" (moyenne : "+(7*Integer.parseInt(ls[18])/2+Integer.parseInt(ls[19].split("/")[0].replaceAll("\\+", ""))+Integer.parseInt(ls[19].split("/")[1].replaceAll("\\+", "")))+")<br>"+
				"      Dégâts.......: "+ls[20]+" D3 "+ls[21]+" (moyenne : "+(2*Integer.parseInt(ls[20])+Integer.parseInt(ls[21].split("/")[0].replaceAll("\\+", "")))+")<br>"+
				"      Armure......: "+ls[22]+" "+pm(ls[23])+" <br> "+
				"      Nb d'Attaques subies ce Tour................: "+ls[24]+"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; (Moyenne esquive : "+
				Math.max(0,7*(Integer.parseInt(ls[18])-Integer.parseInt(ls[24]))/2+Integer.parseInt(ls[19].split("/")[0].replaceAll("\\+", ""))+Integer.parseInt(ls[19].split("/")[1].replaceAll("\\+", "")))+")"+
				"    </td>"+
				"  </tr>"+
				"  <tr>"+
				"    <td valign=\"top\"> <b>Action(s) Programmée(s)</b></td>";
			if(Integer.parseInt(ls[25])+Integer.parseInt(ls[26])==0)
				pro+="    <td valign=\"top\"> <b>Vous n'avez actuellement aucune Action Programmée</b></td>";
			else if(Integer.parseInt(ls[25])*Integer.parseInt(ls[26])!=0)
				pro+="    <td valign=\"top\"> <b>Vous avez actuellement "+ls[25]+" parade(s) et "+ls[26]+" contre-attaque(s) programmées</b></td>";
			else if(Integer.parseInt(ls[25])!=0)
				pro+="    <td valign=\"top\"> <b>Vous avez actuellement "+ls[25]+" parade(s) programmée(s)</b></td>";
			else
				pro+="    <td valign=\"top\"> <b>Vous avez actuellement "+ls[26]+" contre-attaque(s) programmée(s)</b></td>";
			pro+="  </tr>"+
				"  <tr>"+
				"    <td valign=\"top\"><b>Magie</b></td>"+
				"    <td valign=\"top\">Résistance à la Magie...................: "+ls[27]+" "+pm((Integer.parseInt(ls[28])*Integer.parseInt(ls[27]))/100)+" (Total : "+
					(Integer.parseInt(ls[27])+((Integer.parseInt(ls[28])*Integer.parseInt(ls[27]))/100))+")<br>"+
				"      Maîtrise de la Magie....................: "+ls[29]+" "+pm((Integer.parseInt(ls[30])*Integer.parseInt(ls[29]))/100)+" (Total : "+
					(Integer.parseInt(ls[29])+((Integer.parseInt(ls[30])*Integer.parseInt(ls[29]))/100))+")<br>"+
				"      Bonus de Concentration : "+ls[31]+" % "+
				"    </td>"+
				"  </tr>"+
				"</table></center>";
				profil.setText(pro);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private String[] nomEquip = {"Armure","Bouclier","Casque","Arme à 1 main","Talisman","Bottes","Bidouille","Anneau","Bric à Brac","Arme à deux mains","Composant","Parchemin",
		"Potion","Tarot","Champignon","Minerai"};
	
	private String pm(int i)
	{
		if(i>=0)
			return "+"+i;
		return ""+i;
	}
	
	private String pm(String s)
	{
		
		if(s.substring(0,1).equals("-"))
			return s;
		return "+"+s;
	}
		
	private void updateEquip(String s)
	{
		try
		{
		int torse=-1,mainGauche=-1,mainDroite=-1,tete=-1,cou=-1,pieds=-1;
		String []ls=s.split("\n");
		String [][]lss=new String[ls.length] [];
		Vector<String []> [] lv=new Vector[16];
		for(int i=0;i<lv.length;i++)
			lv[i]=new Vector<String []>();
		for(int i=0;i<ls.length;i++)
		{
			if(ls[i].length()==0)
				continue;
			lss[i]=ls[i].split(";");
			if(lss[i].length<5)
				continue;
			for(int j=5;j<lss[i].length;j++)
				lss[i][4]+=";"+lss[i][j];
			int type=Integer.parseInt(lss[i][1]);
			if(type==Equipement.ARMURE && lss[i][2].equals("1"))
				torse=i;
			else if(type==Equipement.BOUCLIER && lss[i][2].equals("1"))
				mainGauche=i;
			else if(type==Equipement.CASQUE && lss[i][2].equals("1"))
				tete=i;
			else if(type==Equipement.ARME_1_MAIN && lss[i][2].equals("1"))
				mainDroite=i;
			else if(type==Equipement.TALISMAN && lss[i][2].equals("1"))
				cou=i;
			else if(type==Equipement.BOTTES && lss[i][2].equals("1"))
				pieds=i;
			else if(type==Equipement.ARME_2_MAINS && lss[i][2].equals("1"))
			{	mainDroite=i;mainGauche=i; }
			else
				lv[type].add(lss[i]);
		}
		String stringEquip = "<html><center>"+
			"<h2>MON ÉQUIPEMENT</h2>"+
			"	<table align=\"center\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">"+
			"		<tr height=\"18\"> "+
			"			<td colspan=\"3\" align=\"center\" height=\"18\" valign=\"top\" width=\"327\"><b>Tête</b></td>"+
			"		</tr>"+
			"		<tr> "+
			"		<td colspan=\"3\" align=\"center\" height=\"19\" valign=\"top\">";
		if(tete!=-1)
			stringEquip+="["+lss[tete][0]+"] "+lss[tete][4]+"<br><b>"+lss[tete][3]+"</b>";
		stringEquip+="		</td>"+
			"	</tr>"+
			"	<tr>"+
			"		<td colspan=\"3\" class=\"TitreEqTable\" align=\"center\" height=\"21\" valign=\"top\"><b>Cou</b></td>"+
			"			</tr>"+
			"			<tr> "+
			"				<td colspan=\"3\" align=\"center\" height=\"48\" valign=\"top\"> ";
		if(cou!=-1)
			stringEquip+="["+lss[cou][0]+"] "+lss[cou][4]+"<br><b>"+lss[cou][3]+"</b>";
		stringEquip+="</td>"+
			"			</tr>"+
			"			<tr> "+
			"				<td align=\"center\" height=\"21\" valign=\"top\" width=\"97\"><b>Main droite</b></td>"+
			"				<td align=\"center\" valign=\"top\" width=\"116\"><b>Torse</b></td>"+
			"				<td align=\"center\" valign=\"top\" width=\"97\"><b>Main gauche</b></td>"+
			"			</tr>"+
			"			<tr> "+
			"				<td align=\"center\" height=\"181\" valign=\"top\" width=\"33%\"> ";			
			
		if(mainDroite!=-1)
		{
			stringEquip+="<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">"+
				"	<tr><td align=\"center\">"+lss[mainDroite][0]+"</td></tr>"+
				"	<tr><td height=\"10\"></td></tr>"+
				"	<tr><td align=\"center\">"+lss[mainDroite][4]+"</td></tr>"+
				"	<tr><td height=\"10\"></td></tr><tr><td align=\"left\">";
			String [] t=lss[mainDroite][3].split(" \\| ");
			for(int i=0;i<t.length;i++)
				stringEquip+="<li>"+t[i]+"</li>";
			stringEquip+="</td></tr></table>";
		}
		stringEquip+=
			"				</td>"+
			"				<td align=\"center\" valign=\"top\" width=\"33%\"> ";
		if(torse!=-1)
		{
			stringEquip+="<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">"+
				"	<tr><td align=\"center\">"+lss[torse][0]+"</td></tr>"+
				"	<tr><td height=\"10\"></td></tr>"+
				"	<tr><td align=\"center\">"+lss[torse][4]+"</td></tr>"+
				"	<tr><td height=\"10\"></td></tr><tr><td align=\"left\">";
			String [] t=lss[torse][3].split(" \\| ");
			for(int i=0;i<t.length;i++)
				stringEquip+="<li>"+t[i]+"</li>";
			stringEquip+="</td></tr></table>";
		}
		stringEquip+=
			"				</td>"+
			"				<td align=\"center\" valign=\"top\" width=\"33%\">";
		if(mainGauche!=-1)
		{
			stringEquip+="<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">"+
				"	<tr><td align=\"center\">"+lss[mainGauche][0]+"</td></tr>"+
				"	<tr><td height=\"10\"></td></tr>"+
				"	<tr><td align=\"center\">"+lss[mainGauche][4]+"</td></tr>"+
				"	<tr><td height=\"10\"></td></tr><tr><td align=\"left\">";
			String [] t=lss[mainGauche][3].split(" \\| ");
			for(int i=0;i<t.length;i++)
				stringEquip+="<li>"+t[i]+"</li>";
			stringEquip+="</td></tr></table>";
		}
		stringEquip+=
			"				</td>"+
			"			</tr>"+
			"			<tr><td colspan=\"3\" align=\"center\" height=\"22\" valign=\"top\"><b>Pieds</b></td></tr>"+
			"			<tr> "+
			"				<td colspan=\"3\" align=\"center\" height=\"97\" valign=\"top\"> ";
		if(pieds!=-1)
			stringEquip+="["+lss[pieds][0]+"] "+lss[pieds][4]+"<br><b>"+lss[pieds][3]+"</b>";
		stringEquip+=
			"				</td>"+
			"			</tr>"+
			"		</table>"+
			"	</td>"+
			"</tr>"+
			"<tr height=\"18\"><td align=\"center\" valign=\"top\"><b>Equipement</b></td></tr>"+
			"<tr> "+
			"	<td valign=\"top\"> "+
			"		<table class=\"mh_tdpage\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\" width=\"100%\">";
		for(int i=0;i<lv.length;i++)
		{
			if(lv[i].size()==0)
				continue;
			stringEquip+="<tr><td colspan=\"2\" align=\"left\" valign=\"top\"><b>"+nomEquip[i]+"</b></td></tr>";
			for(int j=0;j<lv[i].size();j++)
			{
				stringEquip+="<tr><td align=\"left\" valign=\"top\" width=\"20\"></td>"+
					"<td align=\"left\" valign=\"top\"><li>["+lv[i].elementAt(j)[0]+"] "+lv[i].elementAt(j)[4]+"<br>"+lv[i].elementAt(j)[3]+"</li></td></tr>";
			}
		}
		stringEquip+="		</table>"+
			"	</td>"+
			"</tr>"+
			"</table>"+
			"</center>";
		equipement.setText(stringEquip);
		parchemins=lv[11];
		potions=lv[12];
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void Commands() {

		String commands="";

		try {
			FileReader filein = new FileReader("commandes.txt");
			BufferedReader bufferin = new BufferedReader(filein);
			String input = bufferin.readLine();
			while(input != null) {
				if (commands.equals("")) { commands = input; }
				else { commands = commands + "\n" + input; }
				input = bufferin.readLine();
			}
			bufferin.close();
			//JDialog comDialog = new JDialog(gui,"Liste des commandes de Mountyhall Arena");
			JFrame comDialog = new JFrame("Liste des commandes de Mountyhall Arena");
			JTextArea comTextArea = new JTextArea(commands);
			comTextArea.setEditable(false);
			JScrollPane comScrollPane = new JScrollPane(comTextArea);
			comDialog.setContentPane(comScrollPane);
			comDialog.setSize(new Dimension(800, 600));
			comDialog.setLocationRelativeTo(gui);
			comDialog.setVisible(true);
			//JOptionPane.showMessageDialog(this, commands, "Commands:", JOptionPane.PLAIN_MESSAGE);
		}
		catch (FileNotFoundException e) {
			//Testing.append("Unable to find file commands.txt\n");
		}
		catch (IOException e) {
			//Testing.append("Unable to read file commands.txt\n");
		}

	}
	
	public void openAbout() {

		JOptionPane.showMessageDialog((Component) gui,
                                    "Mountyhall Arena a été développé par \n"+
                                    "Mini TilK (mini@tilk.info)\n"+
                                    "Raistlin (raistlin@nerim.net)\n"+
                                    "Les graphismes sont de \n"+
                                    "Eidarloy \n"+
                                    "Il est basé en partie sur jRisk :\n"+
                                    "http://jrisk.sourceforge.net/\n"+
                                    "et est sous license GPL\n\nVersion :"+version,
                                    "A propos de Mountyhall Arena",
                                    JOptionPane.INFORMATION_MESSAGE,
                                    new ImageIcon(MHAGUI.class.getResource("mha.png")));
	}
	//**********************************************************************
	//                     MouseListener Interface
	//**********************************************************************

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {


	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {
	}
	public void mouseMoved(MouseEvent e) {


	}
	
	private boolean strcmp(String s,String s2)
	{
		if(s.length()>=s2.length() && s.substring(0,s2.length()).toLowerCase().equals(s2.toLowerCase()))
			return true;
		return false;
	}
	
	private String extractString(String s,String s2)
	{
		if(!strcmp(s,s2))
			return s;
		return s.substring(s2.length());
	}
	
	private String convertTime(int t)
	{
		if(t%60>9)
			return (t/60)+":"+(t%60);
		return (t/60)+":0"+(t%60);
	}
    
	private String hour2string(int t)
	{
		return "Jour "+(t/(60*24)+1)+" à "+convertTime(t%(60*24));
	}
	
	private void quickSort(Vector elements, int lowIndex, int highIndex)
	{ 
		int lowToHighIndex;
		int highToLowIndex;
		int pivotIndex;
		Integer pivotValue;  // values are Strings in this demo, change to suit your application
		Integer lowToHighValue;
		Integer highToLowValue;
		Object[] parking;
		int newLowIndex;
		int newHighIndex;
		int compareResult;

		lowToHighIndex = lowIndex;
		highToLowIndex = highIndex;
		/** Choose a pivot, remember it's value
		*  No special action for the pivot element itself.
		*  It will be treated just like any other element.
		*/
		pivotIndex = (lowToHighIndex + highToLowIndex) / 2;
		pivotValue = ((Integer) ((Object[]) elements.elementAt(pivotIndex))[0]);
		
		/** Split the Vector in two parts.
		*
		*  The lower part will be lowIndex - newHighIndex,
		*  containing elements <= pivot Value
		*
		*  The higher part will be newLowIndex - highIndex,
		*  containting elements >= pivot Value
		*/
		newLowIndex = highIndex + 1;
		newHighIndex = lowIndex - 1;
		// loop until low meets high
		while ((newHighIndex + 1) < newLowIndex) // loop until partition complete
		{ // loop from low to high to find a candidate for swapping
			lowToHighValue = ((Integer) ((Object[]) elements.elementAt(lowToHighIndex))[0]);
			while (lowToHighIndex < newLowIndex & lowToHighValue.compareTo(pivotValue)<0 )
			{ 
				newHighIndex = lowToHighIndex; // add element to lower part
				lowToHighIndex ++;
				lowToHighValue = ((Integer) ((Object[]) elements.elementAt(lowToHighIndex))[0]);
			}

			// loop from high to low find other candidate for swapping
			highToLowValue = ((Integer) ((Object[]) elements.elementAt(highToLowIndex))[0]);
			while (newHighIndex <= highToLowIndex & (highToLowValue.compareTo(pivotValue)>0))
			{ 
				newLowIndex = highToLowIndex; // add element to higher part
				highToLowIndex --;
				highToLowValue = ((Integer) ((Object[]) elements.elementAt(highToLowIndex))[0]);
			}
			
			// swap if needed
			if (lowToHighIndex == highToLowIndex) // one last element, may go in either part
			{ 
				newHighIndex = lowToHighIndex; // move element arbitrary to lower part
			}
			else if (lowToHighIndex < highToLowIndex) // not last element yet
			{ 
				compareResult = lowToHighValue.compareTo(highToLowValue);
				if (compareResult >= 0) // low >= high, swap, even if equal
				{ 
					parking = ((Object[]) elements.elementAt(lowToHighIndex));
					elements.setElementAt(((Object[]) elements.elementAt(highToLowIndex)), lowToHighIndex);
					elements.setElementAt(parking, highToLowIndex);
			
					newLowIndex = highToLowIndex;
					newHighIndex = lowToHighIndex;
			
					lowToHighIndex ++;
					highToLowIndex --;
				}
			}
		}

		// Continue recursion for parts that have more than one element
		if (lowIndex < newHighIndex)
		{ 
			this.quickSort(elements, lowIndex, newHighIndex); // sort lower subpart
		}
		if (newLowIndex < highIndex)
		{ 
			this.quickSort(elements, newLowIndex, highIndex); // sort higher subpart
		}
	}
	
	class SplashScreen extends JWindow
{
    public SplashScreen(String filename, int waitTime)
    {
        super();
        JLabel l = new JLabel(new ImageIcon(MHAGUI.class.getResource(filename)));
        getContentPane().add(l, BorderLayout.CENTER);
        pack();
        Dimension screenSize =
          Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = l.getPreferredSize();
        setLocation(screenSize.width/2 - (labelSize.width/2),
                    screenSize.height/2 - (labelSize.height/2));
        final int pause = waitTime;
        addMouseListener(new MouseAdapter()
            {
                public void mousePressed(MouseEvent e)
                {
                    setVisible(false);
                    dispose();
                }
            });
        final Runnable closerRunner = new Runnable()
            {
                public void run()
                {
                    setVisible(false);
                    dispose();
                }
            };
        Runnable waitRunner = new Runnable()
            {
                public void run()
                {
                    try
                        {
                            Thread.sleep(pause);
                            SwingUtilities.invokeAndWait(closerRunner);
                        }
                    catch(Exception e)
                        {
                            e.printStackTrace();
                            // can catch InvocationTargetException
                            // can catch InterruptedException
                        }
                }
            };
        setVisible(true);
        Thread splashThread = new Thread(waitRunner, "SplashThread");
        splashThread.start();
    }
}
  
	class ComboBoxRenderer extends JLabel implements ListCellRenderer {
		JSeparator separator;
		
		public ComboBoxRenderer() {
			setOpaque(true);
			setBorder(new EmptyBorder(1, 1, 1, 1));
			separator = new JSeparator(JSeparator.HORIZONTAL);
		}
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			String str = (value == null) ? "" : value.toString();
			if (SEPARATOR.equals(str)) {
				return separator;
			}
			if (isSelected) {
				super.setBackground(list.getSelectionBackground());
				super.setForeground(list.getSelectionForeground());
			} else {
				super.setBackground(list.getBackground());
				super.setForeground(list.getForeground());
			}
			if(index == -1 && value instanceof Color){
				Color scolor = (Color) value;
				super.setBackground(scolor);
//				super.setForeground(scolor);
				setText("Equipe "+(list.getSelectedIndex()));
				return this;	
			}
			setFont(list.getFont());
			if(value instanceof Color)
			{
				super.setBackground((Color) value);
				setText("Equipe "+(index));
			}
			else
				setText(str);
			return this;
		}
		public void setForeground(Color fg){}
		public void setBackground(Color bg){}
	}
	
	final String SEPARATOR = "SEPARATOR";
	class BlockComboListener implements ActionListener {
		JComboBox combo;
		Object currentItem;

		BlockComboListener(JComboBox combo) {
			this.combo = combo;
			combo.setSelectedIndex(0);
			currentItem = combo.getSelectedItem();
		}

		public void actionPerformed(ActionEvent e) {
			String tempItem = (String) combo.getSelectedItem();
			if (SEPARATOR.equals(tempItem)) {
				combo.setSelectedItem(currentItem);
			} else {
				currentItem = tempItem;
			}
		}
	}
	
	private final Color[] listeCouleurs={Color.CYAN,Color.GREEN,Color.LIGHT_GRAY,Color.ORANGE,Color.MAGENTA,Color.WHITE,Color.YELLOW,Color.PINK};
	
	private Color getCouleur(int i)
	{
		if(i>=0 && i<listeCouleurs.length)
			return listeCouleurs[i];
		if(i%2==0)
			return Color.BLUE;
		return Color.PINK;
	}
  
  
  
  
  
  
	/**
	 * This runs the program
	 * @param argv
	 */
	public static void main(String[] argv) {

		// set up system Look&Feel
		try {

			String os = System.getProperty("os.name");
			String jv = System.getProperty("java.version");

/*			if ( jv.startsWith("1.4.2") && os != null && os.startsWith("Linux")) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			}
			else {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}*/
//		UIManager.LookAndFeelInfo[] laf = UIManager.getInstalledLookAndFeels();
//		for(int i=0;i<laf.length;i++)
//			System.out.println(laf[i].toString());
//		UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//		UIManager.setLookAndFeel("gnu.javax.swing.plaf.gnu.GNULookAndFeel");
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		try
		{
			//Ca permet de ne pas ajouter ces lookandfeel si les jar ne sont pas dans le classpath
			Class c=Class.forName("net.sourceforge.napkinlaf.NapkinLookAndFeel");
			UIManager.installLookAndFeel("Napkin","net.sourceforge.napkinlaf.NapkinLookAndFeel");
		}
		catch(Exception e){e.printStackTrace();}
		try
		{
			Class c=Class.forName("com.birosoft.liquid.LiquidLookAndFeel");
			UIManager.installLookAndFeel("Liquid","com.birosoft.liquid.LiquidLookAndFeel");
		}
		catch(Exception e){e.printStackTrace();}
//		UIManager.installLookAndFeel("Napkin","net.sourceforge.napkinlaf.NapkinLookAndFeel");
//		UIManager.installLookAndFeel("Liquid","com.birosoft.liquid.LiquidLookAndFeel");
//		UIManager.installLookAndFeel("Substance","org.jvnet.substance.SubstanceLookAndFeel");
//		UIManager.installLookAndFeel("Windows JG","com.jgoodies.looks.windows.WindowsLookAndFeel");
//		UIManager.installLookAndFeel("Plastic","com.jgoodies.looks.plastic.PlasticLookAndFeel");
//		UIManager.installLookAndFeel("Plastic 3D","com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
//		UIManager.installLookAndFeel("Plastic XP","com.jgoodies.looks.plastic.PlasticXPLookAndFeel");

//		JFrame.setDefaultLookAndFeelDecorated(true);
//		JDialog.setDefaultLookAndFeelDecorated(true);
		gui = new MHAGUI( new MHA() );
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = gui.getSize();
		frameSize.height = ((frameSize.height > screenSize.height) ? screenSize.height : frameSize.height);
		frameSize.width = ((frameSize.width > screenSize.width) ? screenSize.width : frameSize.width);
		gui.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		gui.setVisible(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
