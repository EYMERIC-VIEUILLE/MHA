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

import java.net.*;
import java.io.*;

import java.util.LinkedList;

// The main child thread waits for new information in the ChatArea, and 
// sends it out to the eagerly waiting clients

public class ChatServerThread extends Thread {

   LinkedList m_lList = new LinkedList();

    private Socket socket = null;
    int myIndex;
    ChatReader myReaderThread; 
    ChatArea myChatArea;
    public boolean isBot=false;

    public ChatServerThread(Socket socket, ChatArea cArea, int me) {
           super("ChatServerThread");
           this.socket = socket;
           myChatArea = cArea;
           myIndex= me;
           //System.out.println("Creating Thread "+myIndex);
    }
    
    public boolean isOpen()
    {
    	return (myReaderThread.open || isBot);
    }

    public void run() {
           String outputLine;

           try {
           // First create the Streams associated with the Socket

           // Outbound Stream (actually a PrintWriter)

                   PrintWriter outChat = new PrintWriter(
                		   new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);

           // Inbound Stream (actually a BufferedReader)

                   BufferedReader inChat = new BufferedReader(
                               new InputStreamReader(
                                   socket.getInputStream(),"UTF-8"));
            
           // Create a separate thread to handle the incomming socket data      
                   myReaderThread = new ChatReader(inChat, myChatArea, myIndex);
                   myReaderThread.start();

           // meanwhile, this thread will wait for new chatArea data and when
           // received, it will be dispersed to the connected client. 

                   do  {
                       outputLine = myChatArea.waitForString(myIndex);
		       if(outputLine==null || outputLine.equals("logout"))
		            break;
                       if (outputLine != null)
		       {
                           outChat.println(outputLine);
			}
                   }while (outputLine != null);
                   socket.close();
                   inChat.close();
                   outChat.close(); 
                   myChatArea.getController().sendMessage( "Un client vient de se déconnecter: " + myIndex, false, false);
                   

               } catch (IOException e) {
                       //System.out.println("ChatServerThread IOException: "+
                       //e.getMessage());
                       //e.printStackTrace();
               }
//       System.out.println("ChatServerThread Terminating: " + myIndex);

    }
}
