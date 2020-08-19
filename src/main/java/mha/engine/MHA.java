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


package mha.engine;

import mha.engine.core.*;

import java.util.StringTokenizer;
import java.awt.Color;
import java.util.Vector;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.ResourceBundle;
import java.awt.Toolkit;
import javax.crypto.SealedObject;
import javax.crypto.NullCipher;
import java.util.Enumeration;

public class MHA extends Thread {

	public final static String MHA_VERSION="0.2";

	private StringTokenizer StringT;
	private MHAController controller;
	private boolean game=false;
	private String message;

	private PrintWriter outChat = null;
	private BufferedReader inChat = null;
	private ChatArea chatter = null;
	private Socket chatSocket = null;
	private ChatDisplayThread myReader = null;

	public static int port = 4444;

	private String myAddress;
	private String serveur="";

	private SealedObject Undo;
	private boolean autoplaceall;
	private boolean battle;
	private boolean replay;

	private Vector inbox;

	public String GetNext() {
		return StringT.nextToken();
	}

	public MHA(String a,String b) {
		this();
	}
	
	public ChatArea getChatArea()
	{
		return chatter;
	}
	
	public void setChatArea(ChatArea c)
	{
		chatter=c;
	}
	
	public MHAController getControler()
	{
		return controller;
	}

	public MHA() {

		try {

			//InetAddress localAddr = InetAddress.getLocalHost();

			//myAddress = localAddr.getHostAddress();
			try
			{
				if(System.getProperty("mha.port")!=null && System.getProperty("mha.port").length()>0)
					port=Integer.parseInt(System.getProperty("mha.port"));
			}
			catch(Exception e) {e.printStackTrace();}
				
			myAddress=null;
			Enumeration ifaces = NetworkInterface.getNetworkInterfaces();

			search:
			while (ifaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface)ifaces.nextElement();
				//System.out.println(ni.getName() + ":");
  
				Enumeration addrs = ni.getInetAddresses();
  
				while (addrs.hasMoreElements()) {
					InetAddress ia = (InetAddress)addrs.nextElement();
					//System.out.println(" " + ia.getHostAddress());


					String tmpAddr = ia.getHostAddress();
					if (!tmpAddr.equals("127.0.0.1")) {

						myAddress = tmpAddr;
						break search;

					}


				}
			}

			if (myAddress==null) {
				throw new Exception("no IP found");
			}


		}
		catch (Exception e) { // if network has not been setup
			myAddress = "nonet";
		}

		battle = false;
		replay = false;

		controller = new MHAController();
		
		inbox = new Vector();
		this.start();

	}

	public void addMHAListener(MHAListener o) {

		controller.addListener(o);

		setHelp();

	}

	public void deleteMHAListener(MHAListener o) {

		controller.deleteListener(o);

	}

	public synchronized void parser(String m) {

		//System.out.print("GOT: "+m+"\n");

		inbox.add(m);
		this.notify();

	}

	public void run() {

		while (true) {

			synchronized(this) {

				if( inbox.isEmpty() ) {
					try { this.wait(); }
					catch(InterruptedException e){}
				}

				message = (String)inbox.remove(0);

			}

			//System.out.print("PROCESSING: "+message+"\n");

			String input;
			String output;

			StringT = new StringTokenizer( message );

			if (StringT.hasMoreTokens() == false) {
				//controller.sendMessage(">", false, false );
				getInput();
				continue; // used to be return; when this was not a thread
			}
			else {
				input=GetNext();
				output="";
			}

			// Show version
			if (message.equals("ver")) {
				//controller.sendMessage(">" + message, false, false );
				controller.sendMessage("Mountyhall Arena Game Engine [Version " + MHA_VERSION + "]", false, false );

				getInput();
			}
			// take no action
			else if (input.equals("rem")) {
				//controller.sendMessage(">" + message, false, false );
				controller.sendMessage("no action", false, false );
				getInput();
			}
			// KILL SERVER
			else if (input.equals("killserver")) {

				if (StringT.hasMoreTokens()==false) {

					if ( chatter != null ) {

						try {

							// shut down the server
							//if (chatter.serverSocket != null) {
							//	chatter.serverSocket.close();
							//	chatter=null;
							//}

							if (chatter != null) {
								chatter.closeSocket();
								chatter=null;
							}
							output="Le serveur a été tué.";
							controller.serverState(false);
							//controller.needInput(-1);


						}
						catch (Exception except) {
							output="Error: Impossible de tuer le serveur.";
						}


					}
					else {
						output="Error: Pas de serveur.";
					}
				}
				else { output="Error: La syntaxe de la commande est \"killserver\"."; }
				if(output!= null && output.length()>0)
					controller.sendMessage(output, false, true );

			}
			// out of game commands
			else if (!game) { // if no game

				//controller.sendMessage(">" + message, false, false );

				// NEW GAME
				if (input.equals("startserver")) {

					if (StringT.hasMoreTokens()==false) {

						if (!game && chatter == null ) {

							// CREATE A SERVER
							try {

								chatter = new ChatArea(controller,this);

								output="Le serveur a démarré";
								controller.serverState(true);
								//controller.needInput(-1);

							}
							catch(Exception e) {
								e.printStackTrace();
								chatter = null;
								output="Error: Impossible de créer un serveur.";

							}

						}
						else {
							output="Error: Impossible de créer un serveur.";
						}
					}
					else { output="Error: La syntaxe de la commande est \"startserver\"."; }

				}
				else if (input.equals("join")) {
					if (StringT.countTokens() == 1) {

						if (!game) {

							// CREATE A CLIENT
							try {
								String se=GetNext();
								chatSocket = new Socket( se , port);

								// Create a PrintWriter object for socket output

								outChat = new PrintWriter(
										new OutputStreamWriter(chatSocket.getOutputStream(),"UTF-8"), true);

								// Create a BufferedReader object for socket input

								inChat = new BufferedReader(
										new InputStreamReader(
												chatSocket.getInputStream(),"UTF-8"));

								myReader = new ChatDisplayThread(this, inChat);
								myReader.start();
								game=true;
								

								if ( chatSocket==null ) { controller.closeGame(); throw new ConnectException("connection refused"); }
								serveur=se;

							}
							catch (UnknownHostException e) {
								game = false;
								output="Error: Impossible de rejoindre la partie, hote inconnu !";
							}
							catch (ConnectException e) {
								game = false;
								output="Error: Impossible de rejoindre la partie, impossible de se connecter !";
							}
							catch (IllegalArgumentException e) {
								game = false;
								output="Error: Impossible de rejoindre la partie, ce n'est pas un nom de hote !";
							}
							catch (IOException e) {
								game = false;
								output="Error: Impossible de rejoindre la partie.";
							}
							//catch (Exception e) {

								//output="Connection refused";
							//}
						}
						else {
							output="Error: Impossible de rejoindre la partie.";
						}
					}
					else { output="Error: La syntaxe de la commande est \"join server\"."; }

				}
				else { // if there is no game and the command was unknown
					output="Error: Pas de partie. Entrez \"newgame\" pour commencer une nouvelle partie or \"loadgame filename\" pour charger une partie.";
					
				}

				// if there was NO game
				if(output!= null && output.length()>0)
				{
					controller.sendMessage(output, false, true );
					//System.out.println(output);
				}

				setHelp();

				getInput();

			}
			// IN GAME COMMANDS
			else {

				//replace all country names with there numbers

				// 5 commands have to be checked:

				// capital	x1
				// trade	x3
				// placearmies	x1
				// attack	x2
				// movearmies	x2

				// send the message to all the clients
				if ( chatSocket != null ) { // if this is a network game
					//outChat.println( myAddress + " " + message);
					outChat.println(message);
				}
				else {
					controller.sendMessage("Ne peut envoyer la commande game " + message,false,false );
				}
				//getInput();

			}

			//System.out.print("END PROCESSING\n");

		}

	}

	public void GameParser(String mem) {

		controller.sendDebug(mem);
		controller.sendMessage(mem, false, false );
		getInput();

	}

	public void getInput() {
		controller.needInput( -1 );
	}

	public void setHelp() {

	}

	public String getServeur()
	{
		if ( chatSocket==null || chatSocket.isClosed() )
			return "";
		return serveur;
	}

	public synchronized void kickedOff() {

		//System.out.print("Got kicked off the server!\n");

		try {

			game = false;
			serveur="";
			outChat.close();
			inChat.close();
			chatSocket.close();
			chatSocket = null;

		}
		catch (IOException except) {
			//System.out.println("IOException in main");
		}
		controller.sendMessage("Vous avez été déconnecté du serveur !",false,false);

	}

	public static Color getTextColorFor(Color c) {

		int r = c.getRed();
		int g = c.getGreen();
		// int b = c.getBlue();


		if ((r > 240 || g > 240) || (r > 150 && g > 150)) {
			return Color.black;
		}
		else {
			return Color.white;
		}

	}

	public static void main(String[] argv) {
		MHA mha=new MHA();
		ChatArea ca=null;
		MHAAdapter SimpleMHAAdapter = new MHAAdapter() {
			public void sendMessage(String output, boolean redrawNeeded, boolean repaintNeeded) { System.out.println(output); }};
		mha.addMHAListener( SimpleMHAAdapter );
		int i=5;
		try
		{
			if(System.getProperty("mha.nb_joueurs")!=null)
				i=Integer.parseInt(System.getProperty("mha.nb_joueurs"));
		}
		catch(Exception e){}
		try{
			ca=new ChatArea(mha.getControler(),mha,5);
		}
		catch(Exception e){e.printStackTrace();System.exit(1);}
		mha.setChatArea(ca);
		mha.getControler().serverState(true);
		if(System.getProperty("mha.tp")!=null && System.getProperty("mha.tp").equals("0"))
			ca.getServer().getGame().setTPPossible(false);
		if(System.getProperty("mha.invi")!=null && System.getProperty("mha.invi").equals("0"))
			ca.getServer().getGame().setInviPossible(false);
		if(System.getProperty("mha.camou")!=null && System.getProperty("mha.camou").equals("1"))
			ca.getServer().getGame().setTomCamouflés(true);
		try
		{
			if(System.getProperty("mha.temps")!=null)
				ca.getServer().time_blitz=Integer.parseInt(System.getProperty("mha.temps"));
		}
		catch(Exception e){}
		try
		{
			if(System.getProperty("mha.nb_resu")!=null)
				mha.getChatArea().getServer().getGame().setNbrResu(Integer.parseInt(System.getProperty("mha.nb_resu")));
		}
		catch(Exception e){}
		if(System.getProperty("mha.type")!=null && System.getProperty("mha.type").equals("TDM"))
		{	
			mha.getChatArea().getServer().getGame().setMode(MHAGame.MODE_TEAM_DEATHMATCH);
			try
			{
				if(System.getProperty("mha.nb_equipes")!=null)
					mha.getChatArea().getServer().getGame().setNbrTeam(Integer.parseInt(System.getProperty("mha.nb_equipes")));
				else
					mha.getChatArea().getServer().getGame().setNbrTeam(5);
			}
			catch(Exception e){mha.getChatArea().getServer().getGame().setNbrTeam(5);}
		}
		else
			mha.getChatArea().getServer().getGame().setMode(MHAGame.MODE_DEATHMATCH);
				
//		mha.getControler().sendMessage("Le serveur a démarré", false, true );
		//mha.start();
	}

}
