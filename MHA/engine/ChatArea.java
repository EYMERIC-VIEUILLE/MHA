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

import java.util.*;
import mha.engine.core.*;

import java.net.*;
import java.io.*;


public class ChatArea extends Thread {


    private ServerSocket serverSocket = null;
    private MHAController gui;
    private MHAServer engine;
    private MHA mha;
    int nThreadCount=0;
    int maxConnect=-1;


    // You could make this more dynamic, but it's a little
    // simpler to keep the simple array approach
    private ChatServerThread chatArr[]= new ChatServerThread[100];
    private boolean stopFlag = false;



    ChatArea(MHAController g,MHA m) throws Exception {

	gui = g;
	mha=m;
        InetAddress iaddr = InetAddress.getLocalHost();

	serverSocket = new ServerSocket(MHA.port);

	gui.sendMessage("getHostName = " + iaddr.getHostName() , false, false);
	gui.sendMessage("getHostAddress = " + iaddr.getHostAddress() , false, false);
	gui.sendMessage("Port = " + MHA.port, false, false);
	engine=new MHAServer(this);
	start();

    }
    
   public ChatArea(MHAController g,MHA m,int n) throws Exception {
    	this(g,m);
	maxConnect=n;
    }

    public MHAServer getServer()
    {
       return engine;
    }

    public MHAController getController()
    {
    	return gui;
    }

   synchronized public void restartServer()
    {
    	try {
    	wait(2000);
    	closeSocket();
    	wait(1000);
    	ChatArea ca=new ChatArea(gui,mha,maxConnect);
    	mha.setChatArea(ca);
	ca.getServer().getGame().setTPPossible(engine.getGame().isTPPossible());
	ca.getServer().getGame().setInviPossible(engine.getGame().isInviPossible());
	ca.getServer().getGame().setNbrResu(engine.getGame().getNbrResu());
	if(engine.getGame().getMode()==MHAGame.MODE_TEAM_DEATHMATCH)
	{
		ca.getServer().getGame().setMode(MHAGame.MODE_TEAM_DEATHMATCH);
		ca.getServer().getGame().setNbrTeam(engine.getGame().getNbrTeam());
	}
	else
		ca.getServer().getGame().setMode(MHAGame.MODE_DEATHMATCH);
	} catch (Exception e) {e.printStackTrace();}
    }
    
    public void run() {

	//System.out.print("Server Started\n");

        Socket nextSock;
        ChatServerThread childThread;
	engine.start();
	Vector sockets = new Vector();

        try {

		while(true) {// Forever loop
			// We only exit when someone (main) closes
			// our socket.  At that point we get an 
			// IOException

                       nextSock = serverSocket.accept();


/*			boolean bad=false;
			for (int c=0; c < sockets.size();c++) {

				if (nextSock.getInetAddress().equals( ((Socket)sockets.elementAt(c)).getInetAddress() )) { bad=true; break; }

			}
			if (bad) {

				nextSock.shutdownOutput();
				nextSock.shutdownInput();
				nextSock.close();

				continue;

			}



			sockets.add(nextSock);*/


                       //System.out.println(nThreadCount +
                       // " Another Thread Created");
		       //System.out.println("new client");
		       if(maxConnect==-1 || maxConnect>getNumberOfClient())
		       {
				gui.sendMessage( "Un client vient de se connecter: " + nThreadCount, false, false);
				chatArr[nThreadCount] = childThread = new ChatServerThread(nextSock, this, nThreadCount++);
				if (childThread != null) childThread.start();
			}
			else
			{

				nextSock.close();
			}
		}              

	} 
	catch (IOException e) {
                   //System.err.println("IOException in Server: " +
                   //e.getMessage());
                   //e.printStackTrace();
                   //System.exit(-1);

	}
	// The following call should terminate all child threads
	// myChatArea.setStopFlag();
	//System.out.println("Terminating ChatServer");

	gui.sendMessage("Plus personne ne peut se connecter maintenant",false,false);

    }

    // Calling this routine should tell all of the chatserver
    // threads to terminate

    public synchronized void closeSocket() throws IOException {
	if(!serverSocket.isClosed())
		serverSocket.close();

	stopFlag=true;
	notifyAll();


    }

    public boolean isOff() {

	if ( serverSocket.isClosed() ) { return true; }

	return false;

    }
    
    public int getNumberOfClient()
    {
    	int n=0;
    	for(int i=0;i<nThreadCount;i++)
	  if(chatArr[i]!=null && chatArr[i].isOpen())
	     n++;
	return n;
	
    }


    // Add a new string to all linked lists
    public synchronized void putString(int index, String s) {

       if (chatArr[index] != null && !chatArr[index].isBot)
       {
          chatArr[index].m_lList.addLast(s);
          notifyAll();
       }

    }

    void stopIncommingConnections()
    {
       try { serverSocket.close(); } catch (IOException e) { }
    }
    
    public synchronized void broadcast(String s)
    {
       for (int i= 0; i < chatArr.length; i++)
           if (chatArr[i] != null && !chatArr[i].isBot)
               chatArr[i].m_lList.addLast(s);
       notifyAll();
    }    
    
    // called to get the list of strings awaiting any given
    // thread
    synchronized String getStrings(int index) {
       int i, num;        
//       String str;
//       StringBuffer sb = new StringBuffer("");
       LinkedList lList;

       lList = chatArr[index].m_lList;
       num=lList.size();
       try
       {
         if(num>0)
         {
           return ((String)lList.removeFirst()).replaceAll("\n","~=~");
         }
/*       try{
           for (i=0; i < num; i++)
           {
               str = (String)lList.removeFirst();
               sb.append( str.replace('\n','£'));
               sb.append("\n");
           }*/
       }
       catch (NoSuchElementException e)
      {
           //System.out.println("Our List Count is Messed Up???");
       }
      
       return "";
    }

    // called to wait for any new messages for a given thread

    synchronized String waitForString(int index) {
       String str= getStrings(index);

       do {
	       if(str.length() != 0)
	           return str;
               try {            
               	           if(stopFlag)
               	                return null;
                           wait(); 
                   }
                   catch (InterruptedException e)
                           {
                           //System.out.println("Interrupted wait call");
                       }
		str = getStrings(index);
       } while (!stopFlag || str.length() != 0);
       return null;
    }

    public boolean isBot(int i)
    {
    	if (chatArr[i] != null)
               return chatArr[i].isBot;
        return false;
    }
    
    public void setBot(int i)
    {
    	if (chatArr[i] != null)
               chatArr[i].isBot=true;
    }
    // Cread a new chat data structure
    //synchronized void addNewChat(int index) {
    //   chatArr[index] = new Chat();
    //}

    //synchronized void removeChat(int index) {
    //   chatArr[index] = null;
    //}
}
