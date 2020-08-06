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

import java.io.*;
import java.net.*;
import java.awt.*;

 // This thread reads in input stream from a socket and 
 // appends the output to a TextArea object

public class ChatDisplayThread extends Thread {    
   MHA mha; 
   BufferedReader inChat = null;

    ChatDisplayThread (MHA m, BufferedReader in) 
    { 
       mha = m;
       inChat = in; 
    }

    public void run() {
	//System.out.println("Start DisplayThread ");
	String str;
	boolean badexit=true;

	try {
               while ((str = inChat.readLine()) != null) 
               {
                       if (str.length() > 0)
                       {
                           mha.GameParser(str.replaceAll("~=~","\n"));
                       }
               }   
        }
	catch (IOException e) {
       }

	mha.kickedOff();

    } 
} 
