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

package mha.ui.CommandLine;

import mha.engine.*;
import mha.engine.core.Troll;


import java.io.*;

public class CommandText extends Thread {

    private final static String version="1.0.4.0";

    private MHA mha;
    private BufferedReader br;

    private boolean IgnoreNext;

    CommandText(MHA m) {

	mha = m;

	mha.addMHAListener( new MHAAdapter() {

	    public void sendMessage(String output, boolean redrawNeeded, boolean repaintNeeded) {

		if (IgnoreNext) {
		    IgnoreNext=false;
		}
		else {
		    try
		    {
		    	String[] liste=output.trim().split(" ");
			if(liste[0].equals("Time:"))
			{
				int id=Integer.parseInt(liste[1]);
				output="Nous sommes actuellement le "+Troll.hour2string(id);
			}
				
		    }
		    catch(NumberFormatException e)
		    { }
		    if(!output.equals(""))
		    	System.out.print(output+"\n>");
		}

	    }

	    public void needInput(int s) {

		synchronized(CommandText.this) {
		    CommandText.this.notify();
		}

	    }

	    public void noInput() {

	// This does not work, something should be here

	//	synchronized(CommandText.this) {
	//	    try { CommandText.this.wait(); }
	//	    catch(InterruptedException e){}
	//	}

	    }

	} );


	InputStreamReader in = new InputStreamReader(System.in);
	br = new BufferedReader(in);

	System.out.print("Welcome to Command Line Mountyhall Arena!\n");

	this.start();

    }

    public static void Help() {

	String commands="";

	try {
	    FileReader filein = new FileReader("commandes.txt");
	    BufferedReader bufferin = new BufferedReader(filein);
	    String input = bufferin.readLine();
	    System.out.print("Commandes:\n");
	    while(input != null) {
		System.out.print(input+"\n");
		input = bufferin.readLine();
	    }
	    bufferin.close();

	}
	catch (FileNotFoundException e) {
	    System.out.print("Unable to find file commands.txt\n");
	}
	catch (IOException e) {
	    System.out.print("Unable to read file commands.txt\n");
	}

    }

    public void run() {
      System.out.print(">");
      while(true) {

	String input="";

	try {
	    input = br.readLine();
	    if (input.equals("help")) {
		Help();
		continue;
	    }
	    else if (input.equals("about")) {
		System.out.print("Command Line for Risk, version: "+version+"\nMade by Yura Mamyrin (yura@yura.net)\nModify by mini TilK (mini@tilk.info)");

		String os = System.getProperty("os.name");
		String jv = System.getProperty("java.version");

		System.out.print("Java version: " + jv + "\n");
		System.out.print("Operating System: " + os + "\n");

		continue;
	    }
	    else if(input.equals(""))
	    {
	         System.out.print(">");
	    }
	    else if (input.equals("exit")) {
		System.out.print("Thank you for Playing Mountyhall Arena.\n");
		System.exit(0);
	    }
	    else if (input.trim().split(" ")[0].toLowerCase().equals("execute"))
	    {
	    	String[] liste=input.trim().split(" ");
		if(liste.length<2)
		{
			System.out.print("Argument manquant pour la fonction execute");
			continue;
		}
		for(int i=2;i<liste.length;i++)
			liste[1]+=" "+liste[i];
		try {
			FileReader filein = new FileReader(liste[1]);
			BufferedReader bufferin = new BufferedReader(filein);
			input = bufferin.readLine();
			System.out.print("Exécution du script "+liste[1]+":\n");
			while(input != null) {
				synchronized(this) {
					mha.parser(input);
					try { this.wait(); }
					catch(InterruptedException e){}
				}
				input = bufferin.readLine();
			}
			bufferin.close();
		}
		catch (FileNotFoundException e) {
			System.out.print("Error: Impossible d'ouvrir le fichier "+liste[1]+"\n");
		}
		catch (IOException e) {
			System.out.print("Error: Impossible de lire le fichier "+liste[1]+"\n");
		}
		
	    }
	    else {

		synchronized(this) {

		    //IgnoreNext=true;
		    mha.parser(input);

		    try { this.wait(); }
		    catch(InterruptedException e){}

		}

	    }

	}
	catch (Exception e) {
	    System.out.print("\nThank you for Playing Mountyhall Arena.\n");
	    System.exit(0);
	}

      }

    }

    public static void main(String[] argv) {

	new CommandText( new MHA() );

    }

}
