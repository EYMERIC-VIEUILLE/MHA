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

// The ChatReader thread reads incomming socket data and puts it into the
// Chat Area so that all outbound threads can send it out

public class ChatReader extends Thread{
   BufferedReader mySocketInput;
   int myIndex;
   ChatArea myChatArea;
   public boolean open=false;

    ChatReader(BufferedReader in,  ChatArea cArea, int index) {
       super("ChatReaderThread");
       mySocketInput = in;
       myIndex = index;
       myChatArea = cArea;
   }

    public void run(){
       String inputLine;
       open=true;
       myChatArea.getServer().analyseCommand(myIndex,"login");
       try {
               while ((inputLine = mySocketInput.readLine()) != null)
	       {
	           //System.out.println("je lit quelque chose :"+inputLine);
		   if(inputLine.toLowerCase().equals("logout"))
		   	break;
                   myChatArea.getServer().analyseCommand(myIndex, inputLine);
		}
           }
       catch (IOException e) {
                       //System.out.println("ChatReader IOException: "+
                       //    e.getMessage());
                       //e.printStackTrace();
               }
       //System.out.println("ChatReader Terminating: " + myIndex);
       open=false;
       if(!myChatArea.isBot(myIndex))
       		myChatArea.getServer().analyseCommand(myIndex,"logout");
   }
}
