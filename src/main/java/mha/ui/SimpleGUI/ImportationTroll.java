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
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.math.BigInteger;
import javax.swing.*;
import javax.swing.filechooser.*;
import mha.engine.*;
import java.security.*;
import java.net.*;
import javax.imageio.ImageIO;

public class ImportationTroll extends JPanel
                             implements ActionListener {
    final JButton cancelButton, saveButton, executeButton;
    final JTextField nomField, numeroField, saveField;
    final JPasswordField passField;
    final JCheckBox checkBlason,checkCertif;
//    JTextArea log;
    final JFileChooser fc;
//    final String url="http://mountyzilla.tilk.info/mha/";
    final String url="http://sp.mountyhall.com/";
    final JDialog dialog;
    File file;
    JFrame gui;

    public ImportationTroll(JFrame g) {
	super();
	gui=g;
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	c.insets = new java.awt.Insets(5, 2, 3, 3);
	setLayout(gridbag);
	c.fill = GridBagConstraints.BOTH;
	c.gridx = 0;
	c.gridy = 0;
	JLabel jl=new JLabel("Nom du troll : ",null,SwingConstants.RIGHT);
	gridbag.setConstraints(jl, c);
	add(jl);
	c.gridx = 1;
	nomField=new JTextField(20);
	gridbag.setConstraints(nomField, c);
	add(nomField);
	c.gridx = 0;
	c.gridy = 1;
	jl=new JLabel("Numéro du troll : ",null,SwingConstants.RIGHT);
	gridbag.setConstraints(jl, c);
	add(jl);
	c.gridx = 1;
	numeroField=new JTextField(20);
	gridbag.setConstraints(numeroField, c);
	add(numeroField);
	c.gridx = 0;
	c.gridy = 2;
	jl=new JLabel("Mot de passe du troll : ",null,SwingConstants.RIGHT);
	gridbag.setConstraints(jl, c);
	add(jl);
	c.gridx = 1;
	passField=new JPasswordField(20);
	gridbag.setConstraints(passField, c);
	add(passField);
	c.gridx = 0;
	c.gridy = 3;
	jl=new JLabel("Enregistrer dans : ",null,SwingConstants.RIGHT);
	gridbag.setConstraints(jl, c);
	add(jl);
	c.gridx = 1;
	JPanel jp=new JPanel();
	saveButton=new JButton("Parcourir");
	saveButton.addActionListener(this);
	saveField=new JTextField(20);
	jp.add(saveField);
	jp.add(saveButton);
	gridbag.setConstraints(jp, c);
	add(jp);
	c.gridx = 0;
	c.gridy = 4;
	jl=new JLabel("Importer le blason : ",null,SwingConstants.RIGHT);
	gridbag.setConstraints(jl, c);
	add(jl);
	c.gridx = 1;
	checkBlason=new JCheckBox();
	gridbag.setConstraints(checkBlason, c);
	add(checkBlason);
	c.gridx = 0;
	c.gridy = 5;
	jl=new JLabel("Certifier conforme : ",null,SwingConstants.RIGHT);
	gridbag.setConstraints(jl, c);
	add(jl);
	c.gridx = 1;
	checkCertif=new JCheckBox();
	checkCertif.addActionListener(this);
	gridbag.setConstraints(checkCertif, c);
	add(checkCertif);
	c.gridx = 0;
	c.gridy = 6;
	cancelButton=new JButton("Annuler");
	cancelButton.addActionListener(this);
	gridbag.setConstraints(cancelButton, c);
	add(cancelButton);
	c.gridx = 1;
	executeButton=new JButton("Importer");
	executeButton.addActionListener(this);
	gridbag.setConstraints(executeButton, c);
	add(executeButton);

        //Create a file chooser
	if(MHAGUI.lastDirectory.length()==0)
		fc = new JFileChooser(new File ("."));
	else
		fc = new JFileChooser(MHAGUI.lastDirectory);
	MHAFileFilter filter = new MHAFileFilter("mha","Fiche de perso pour Mountyhall Arena");
	fc.setFileFilter(filter);
//	filter.addExtension("gif");
//	filter.setDescription("Perso Mountyhall Arena");
	//fc.setFileFilter(filter);
	
	dialog = new JDialog(gui,"Importer un troll de Mountyhall");
	dialog.setContentPane(this);
//	dialog.setSize(new Dimension(500,210));
	dialog.pack();
	dialog.setSize(dialog.getWidth()+10,dialog.getHeight()+10);
	dialog.setResizable(false);
	dialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == cancelButton) {
            dialog.setVisible(false);
            dialog.dispose();

        //Handle save button action.
	} else if (e.getSource() == checkCertif) {
		checkBlason.setEnabled(!checkCertif.isSelected());
        } else if (e.getSource() == saveButton) {
            int returnVal = fc.showDialog(this,"Choisir");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
		try { MHAGUI.lastDirectory = file.getCanonicalPath(); } catch(Exception ex){}
		saveField.setText(file.getAbsolutePath());
		if (!saveField.getText().endsWith(".mha")) {
			saveField.setText(saveField.getText() + ".mha");
		}
                //This is where a real application would save the file.
              //  log.append("Saving: " + file.getName() + "." + newline);
            } 
        } else if(e.getSource() == executeButton)
	{
		int num;
		if(nomField.getText().length()==0 || numeroField.getText().length()==0 ||(new String(passField.getPassword())).length()==0 || saveField.getText().length()==0)
		{
			JOptionPane.showMessageDialog(dialog, "Merci de remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try
		{
			num=Integer.parseInt(numeroField.getText());
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(dialog, "Le numéro de troll doit être un nombre", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(!checkCertif.isSelected())
			importeTroll(nomField.getText(),num,new String(passField.getPassword()),saveField.getText(),checkBlason.isSelected());
		else
		{
			try
			{
				FileWriter writer=null;
				BufferedReader in;
				URL     theURL = null;
				String line="";
				Proxy proxy=null;
				URLConnection uc;
				writer = new FileWriter(saveField.getText());
				System.setProperty("java.net.useSystemProxies","true");
				//System.setProperty("java.net.useSystemProxies","false");
				java.util.List<Proxy> l = ProxySelector.getDefault().select(new URI("http://www.yahoo.com/"));
				java.util.Iterator iter = l.iterator();
				if(iter.hasNext()) {
					Proxy p = (Proxy) iter.next();
					InetSocketAddress addr = (InetSocketAddress) p.address();
					if(addr!=null)
					{
		//				System.out.println("proxy hostname : " + addr.getHostName());
		//				System.out.println("proxy port : " + addr.getPort());
						proxy=p;
					}
				}
				theURL = new URL("http://mountypedia.free.fr/signature/createMHAProfile.php?num="+num+"&pass="+md5(new String(passField.getPassword()))+"&nom="+URLEncoder.encode(nomField.getText(),"UTF-8"));
				if(proxy == null) {                    
					System.out.println("No Proxy");
					uc=theURL.openConnection();            
				}
				else
					uc=theURL.openConnection(proxy);
				in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
				
				while((line=in.readLine())!=null)
				{
					if(line.substring(0,6).equals("Erreur"))
					{
						JOptionPane.showMessageDialog(dialog, line, "Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}
					writer.write(line+"\n");
				}
				writer.close();
				JOptionPane.showMessageDialog(dialog, "L'import s'est bien déroulé", "Import de troll", 	JOptionPane.PLAIN_MESSAGE);
				dialog.setVisible(false);
				dialog.dispose();
			}
			catch (IOException ex) {
				JOptionPane.showMessageDialog(dialog, "Désolé, vous ne pouvez pas écrire ce fichier :\n"+ex.toString(), "Erreur", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
    }
    
    private void importeTroll(String nom,int num,String pass,String fich,boolean b)
    {
    	try
	{
		BufferedReader in;
		URL     theURL = null;
		String line="";
		Proxy proxy=null;
		URLConnection uc;
		int race=-1;
		String s="";
		String [] ls;
//		theURL = new URL(url+"SP_Profil2.php?Numero="+num+"&Motdepasse="+md5(pass));
//		System.out.println(url+"SP_Profil2.php?Numero="+num+"&Motdepasse="+md5(pass));
		//On détecte les proxies
		
		FileWriter writer=null;
		writer = new FileWriter(fich);
		
		System.setProperty("java.net.useSystemProxies","true");
		java.util.List<Proxy> l = ProxySelector.getDefault().select(new URI("http://www.yahoo.com/"));
		java.util.Iterator iter = l.iterator();
		if(iter.hasNext()) {
			Proxy p = (Proxy) iter.next();
			InetSocketAddress addr = (InetSocketAddress) p.address();
			if(addr!=null)
			{
//				System.out.println("proxy hostname : " + addr.getHostName());
//				System.out.println("proxy port : " + addr.getPort());
				proxy=p;
			}
		}
		
		//Je récupère les compétences/sorts et donc la race :)
		
		theURL = new URL(url+"SP_Aptitudes2.php?Numero="+num+"&Motdepasse="+md5(pass));
		if(proxy == null) {                    
//			System.out.println("No Proxy");
			uc=theURL.openConnection();            
                }
		else
			uc=theURL.openConnection(proxy);
		in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		
		while((line=in.readLine())!=null)
		{
			if(line.substring(0,6).equals("Erreur"))
			{
				JOptionPane.showMessageDialog(dialog, line, "Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}
			ls=line.split(";");
			if(ls[0].equals("C"))
			{
				int id=Integer.parseInt(ls[1]);
				int pour=Integer.parseInt(ls[2])+Integer.parseInt(ls[3]);
				int level=Integer.parseInt(ls[4]);
				if(id<=4)
					race=id-1;
				if(id<comp.length && comp[id]!=0)
					s+="addcomp "+comp[id]+" "+pour+" "+level+"\n";
			}
			else if(ls[0].equals("S"))
			{
				int id=Integer.parseInt(ls[1]);
				int pour=Integer.parseInt(ls[2])+Integer.parseInt(ls[3]);
				if(id<sort.length && sort[id]!=0)
					s+="addsort "+sort[id]+" "+pour+"\n";
			}
			
		}
		
		//Je récupère le niveau du troll
		
		theURL = new URL(url+"SP_ProfilPublic.php?Numero="+num+"&Motdepasse="+md5(pass));
		
		
		if(proxy == null) 
			uc=theURL.openConnection();
		else
			uc=theURL.openConnection(proxy);
		in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		line = in.readLine();
		if(line==null)
		{
			JOptionPane.showMessageDialog(dialog, "Problème sur le serveur MH (1)", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(line.length()>6 && line.substring(0,6).equals("Erreur"))
		{
			JOptionPane.showMessageDialog(dialog, line, "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String pp_line=line;
		int niveau=Integer.parseInt(line.split(";")[2]);
		
		//Les caracs du trolls
		
		theURL = new URL(url+"SP_Profil2.php?Numero="+num+"&Motdepasse="+md5(pass));
		
		
		if(proxy == null) 
			uc=theURL.openConnection();
		else
			uc=theURL.openConnection(proxy);
		in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		line = in.readLine();
		if(line==null)
		{
			JOptionPane.showMessageDialog(dialog, "Problème sur le serveur MH (2)", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(line.length()>6 && line.substring(0,6).equals("Erreur"))
		{
			JOptionPane.showMessageDialog(dialog, line, "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ls=line.split(";");
		writer.write("addtroll "+num+" "+nom+" "+race+"\n");
		writer.write(s);
		//carac 24 3 9 20 4 90 6 600 2067 1001
		writer.write("carac "+niveau+" "+ls[8]+" "+ls[9]+" "+ls[10]+" "+ls[11]+" "+ls[5]+" "+ls[12]+" "+ls[23]+" "+ls[14]+" "+ls[15]+"\n");
		
		//Bon les mouches !!
		
		theURL = new URL(url+"SP_Mouche.php?Numero="+num+"&Motdepasse="+md5(pass));
		if(proxy == null)
			uc=theURL.openConnection(); 
		else
			uc=theURL.openConnection(proxy);
		in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		
		while((line=in.readLine())!=null)
		{
			if(line.length()>6 && line.substring(0,6).equals("Erreur"))
			{
				JOptionPane.showMessageDialog(dialog, line, "Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}
			ls=line.split(";");
			if(ls.length<3)
				continue;
			for(int i=2;i<ls.length-3;i++)
				ls[1]+=";"+ls[i];
			if(ls[1].length()==0)
				ls[1]="Aucun nom";
			if(ls[ls.length-3].equals("Crobate"))
				writer.write("addmouche 0 "+ls[1]+"\n");
			else if(ls[ls.length-3].equals("Vertie"))
				writer.write("addmouche 1 "+ls[1]+"\n");
			else if(ls[ls.length-3].equals("Lunettes"))
				writer.write("addmouche 2 "+ls[1]+"\n");
			else if(ls[ls.length-3].equals("Miel"))
				writer.write("addmouche 3 "+ls[1]+"\n");
			else if(ls[ls.length-3].equals("Xidant"))
				writer.write("addmouche 4 "+ls[1]+"\n");
			else if(ls[ls.length-3].equals("Rivatant"))
				writer.write("addmouche 5 "+ls[1]+"\n");
			else if(ls[ls.length-3].equals("Héros"))
				writer.write("addmouche 6 "+ls[1]+"\n");
			else if(ls[ls.length-3].equals("Carnation"))
				writer.write("addmouche 7 "+ls[1]+"\n");
			else if(ls[ls.length-3].equals("Nabolisants"))
				writer.write("addmouche 8 "+ls[1]+"\n");
		}
		//Et l'équipement !!
		//addequip 473437 5 1 -1 1 0 1 0 1 4 0 0 0 13 0 6 1 Jambières Flamboyantes de la salamandre
		//473437;32;Bottes;1;Jambi&egrave;res Flamboyantes;de la Salamandre;ATT : +1 | ESQ : -1 | DEG : +1 | REG : +1 | Vue : +1 | Armure : +4 | RM : +6 %;12.5
		theURL = new URL(url+"SP_Equipement.php?Numero="+num+"&Motdepasse="+md5(pass));
		if(proxy == null)
			uc=theURL.openConnection(); 
		else
			uc=theURL.openConnection(proxy);
		in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		
		while((line=in.readLine())!=null)
		{
			if(line.length()>6 && line.substring(0,6).equals("Erreur"))
			{
				JOptionPane.showMessageDialog(dialog, line, "Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}
			ls=line.split(";");
			if(ls.length<3 || ls[3].equals("0"))
				continue;
			for(int i=5;i<ls.length-3;i++)
				ls[4]+=";"+ls[i];
			if(ls[ls.length-3].length()>0)
				ls[4]+=" "+ls[ls.length-3];
			int [] carac=parseCarac(ls[ls.length-2]);
			int equip=0;
			if(!ls[1].equals("0"))
				equip=1;
			if(ls[2].equals("Armure"))
				writer.write("addequip "+ls[0]+" 0 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" "+carac[7]+" 0 "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Bouclier"))
				writer.write("addequip "+ls[0]+" 1 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" "+carac[7]+" 0 "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Casque"))
				writer.write("addequip "+ls[0]+" 2 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" "+carac[7]+" 0 "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Arme (1 main)"))
				writer.write("addequip "+ls[0]+" 3 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" 0 "+carac[7]+" "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Talisman"))
				writer.write("addequip "+ls[0]+" 4 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" "+carac[7]+" 0 "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Bottes"))
				writer.write("addequip "+ls[0]+" 5 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" "+carac[7]+" 0 "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Bidouille"))
				writer.write("addequip "+ls[0]+" 6 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" "+carac[7]+" 0 "+carac[8]+" 1 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Anneau"))
				writer.write("addequip "+ls[0]+" 7 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" "+carac[7]+" 0 "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Bric à Brac"))
				writer.write("addequip "+ls[0]+" 8 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" "+carac[7]+" 0 "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Arme (2 mains)"))
				writer.write("addequip "+ls[0]+" 9 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" 0 "+carac[7]+" "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Composant"))
				writer.write("addequip "+ls[0]+" 10 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" "+carac[7]+" 0 "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Parchemin"))
				writer.write("addequip "+ls[0]+" 11 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" "+carac[7]+" 0 "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Potion"))
				writer.write("addequip "+ls[0]+" 12 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" "+carac[7]+" 0 "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Tarot"))
				writer.write("addequip "+ls[0]+" 13 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" "+carac[7]+" 0 "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Champignon"))
				writer.write("addequip "+ls[0]+" 14 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" "+carac[7]+" 0 "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
			else if(ls[2].equals("Minerai"))
				writer.write("addequip "+ls[0]+" 15 "+carac[0]+" "+carac[1]+" "+carac[2]+" "+carac[3]+" "+carac[4]+" "+carac[5]+" "+carac[6]+" "+carac[7]+" 0 "+carac[8]+" 0 "+((int) Float.parseFloat(ls[ls.length-1]))+" "+carac[10]+" "+carac[9]+" "+equip+" "+ls[4]+"\n");
		}
		line = pp_line;
//		line="36216;0;25;2004-05-04 15:32:41;;http://mini.tilk.info/blason/Tilk_essai3.gif;0;24;232;21;19;<b><font color=blue>Prince du Haut-Conseil</font>;[Survivant de la Peste Scrofuleuse]|[Immunisé à la Peste Scrofuleuse]|;1,Le Tamanoir|0,Heaume Titanesque|0,Collier de pierre,de la Salamandre|0,Cuirasse d'Ossements,du Temps|0,Rondache en métal,en Mithril|0,Gantelet,en Mithril|0,Jambi&egrave;res Flamboyantes,de la Salamandre|";
		ls=line.split(";");
		if(ls[5].length()>0 && b)
		{
			if(ls[5].substring(0,6).equals("Blason"))
				ls[5]="http://blason.mountyhall.com/"+ls[5];
			ls[5]=ls[5].replace("http://www.mountyhall.com/images/Blasons","http://blason.mountyhall.com");
			theURL = new URL(ls[5]);
			ImageIcon inImage;
			byte buf[];
			if(proxy == null)
				uc=theURL.openConnection(); 
			else
				uc=theURL.openConnection(proxy);
			BufferedInputStream imgStream = new BufferedInputStream(uc.getInputStream());
			if (imgStream != null) {
				buf = new byte[2000000];
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				try {
					for (int read=0;(read=imgStream.read(buf))!=-1;out.write(buf,0,read));
					buf=out.toByteArray();
				} catch (IOException ieo) {
					writer.write("validtroll");
					writer.close();
					JOptionPane.showMessageDialog(dialog, "Le fichier "+ls[5]+" ne peut pas être lu", "Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					imgStream.close();
				} catch (IOException ieo) {
					writer.write("validtroll");
					writer.close();
					JOptionPane.showMessageDialog(dialog, "Le fichier "+ls[5]+" ne peut pas être fermé", "Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
			
				if (out.size() <= 0) {
					writer.write("validtroll");
					writer.close();
					JOptionPane.showMessageDialog(dialog, "Le fichier "+ls[5]+" est vide", "Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try{
					inImage=new ImageIcon(Toolkit.getDefaultToolkit().createImage(buf));
				}
				catch(Exception e)
				{
					writer.write("validtroll");
					writer.close();
					JOptionPane.showMessageDialog(dialog, "Le fichier "+ls[5]+" est corrompu", "Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			else
			{
				writer.write("validtroll");
				writer.close();
				JOptionPane.showMessageDialog(dialog, "Le fichier "+ls[5]+" est introuvable", "Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}
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
			int w = img.getImage().getWidth(null);
			int h = img.getImage().getHeight(null);
			BufferedImage bImage = new BufferedImage(w, h,BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bImage.createGraphics();
			g2.drawImage(img.getImage(), 0, 0, null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			try {
				ImageIO.write(bImage, "png", baos);
			} catch (IOException e) {
				writer.write("validtroll");
				writer.close();
				JOptionPane.showMessageDialog(dialog, "Problème de conversion des images", "Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}
			byte[] imageBytes = baos.toByteArray();
			writer.write("icontroll ");
			BigInteger bi = new BigInteger(imageBytes);    
			writer.write(bi.toString(16)+"\n");
/*			for(int i=0;i<imageBytes.length;i++)
				writer.write(Integer.toHexString(imageBytes[i]).substring(6,8));
			writer.write("\n");
			JOptionPane.showMessageDialog((Component) gui,
				"Voici votre avatar "+new String(imageBytes).substring(0,20)+" "+Integer.toHexString(imageBytes[0]),
				"Avatar",
				JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(imageBytes)));*/
		}
		writer.write("validtroll");
		writer.close();
		JOptionPane.showMessageDialog(dialog, "L'import s'est bien déroulé", "Import de troll", 	JOptionPane.PLAIN_MESSAGE);
		dialog.setVisible(false);
        	dialog.dispose();

	}
	catch (IOException e) {
		JOptionPane.showMessageDialog(dialog, "Désolé, vous ne pouvez pas écrire ce fichier :\n"+e.toString(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
    }
	
	final private int[] comp = {0,1,2,3,4,0,0,11,9,5,13,8,10,0,6,7,0,0,0,0,0,14,0,12};
	final private int[] sort = {0,4,1,3,2,9,7,8,12,22,0,24,13,20,0,17,6,19,15,14,5,18,21,23,0,0,0,10,16,11};
	
	private int [] parseCarac(String s)
	{
		int [] carac={0,0,0,0,0,0,0,0,0,0,0};
		try
		{
			//System.out.println(s);
			String [] ls=s.split(" \\| ");
			for(int i=0;i<ls.length;i++)
			{
				if(ls[i].substring(ls[i].length()-3).equals(" D3"))
					ls[i]=ls[i].substring(0,ls[i].length()-3);
				if(ls[i].substring(ls[i].length()-4).equals(" min"))
					ls[i]=ls[i].substring(0,ls[i].length()-4);
				if(ls[i].substring(ls[i].length()-2).equals(" %"))
					ls[i]=ls[i].substring(0,ls[i].length()-2);
				if(ls[i].length()>6 && ls[i].substring(0,6).equals("ATT : "))
					carac[0]=Integer.parseInt(ls[i].substring(6).replace("+",""));
				else if(ls[i].length()>6 && ls[i].substring(0,6).equals("ESQ : "))
					carac[1]=Integer.parseInt(ls[i].substring(6).replace("+",""));
				else if(ls[i].length()>6 && ls[i].substring(0,6).equals("DEG : "))
					carac[2]=Integer.parseInt(ls[i].substring(6).replace("+",""));
				else if(ls[i].length()>7 && ls[i].substring(0,7).equals("TOUR : "))
					carac[3]=Integer.parseInt(ls[i].substring(7).replace("+",""));
				else if(ls[i].length()>6 && ls[i].substring(0,6).equals("REG : "))
					carac[4]=Integer.parseInt(ls[i].substring(6).replace("+",""));
				else if(ls[i].length()>5 && ls[i].substring(0,5).equals("PV : "))
					carac[5]=Integer.parseInt(ls[i].substring(5).replace("+",""));
				else if(ls[i].length()>6 && ls[i].substring(0,6).equals("Vue : "))
					carac[6]=Integer.parseInt(ls[i].substring(6).replace("+",""));
				else if(ls[i].length()>9 && ls[i].substring(0,9).equals("Armure : "))
					carac[7]=Integer.parseInt(ls[i].substring(9).replace("+",""));
				else if(ls[i].equals("Effet de Zone"))
					carac[8]=1;
				else if(ls[i].length()>5 && ls[i].substring(0,5).equals("RM : "))
					carac[9]=Integer.parseInt(ls[i].substring(5).replace("+",""));
				else if(ls[i].length()>5 && ls[i].substring(0,5).equals("MM : "))
					carac[10]=Integer.parseInt(ls[i].substring(5).replace("+",""));
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return carac;
	}
	
	private  String md5 (String cle) {
		byte[] uniqueKey = cle.getBytes();
		byte[] hash = null;
		try {
			hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
		}
		catch (NoSuchAlgorithmException e) {
			System.out.println("Pas de cryptage en md5");
			return cle;
		}
		StringBuffer hashString = new StringBuffer();
		for (int i = 0; i < hash.length; ++i) {
			String hex = Integer.toHexString(hash[i]);
			if (hex.length() == 1) {	
				hashString.append('0');	
				hashString.append(hex.charAt(hex.length() - 1));
			}
			else {
				hashString.append(hex.substring(hex.length() - 2));
			}
		}
		return hashString.toString();
	}
}
