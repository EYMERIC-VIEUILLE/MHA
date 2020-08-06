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
import javax.swing.Timer;
import java.awt.event.*;
import mha.engine.core.*;
import java.util.Vector;
import java.lang.reflect.*;

// The main child thread waits for new information in the ChatArea, and 
// sends it out to the eagerly waiting clients

public class MHAServer extends Thread {

    private ChatArea myChatArea;
    private Vector<String> com;
    private Vector<String> caracTrolls; 
    private Vector<Integer> source;
    public int time_blitz=0;
    private Timer currentTimer=null;
    private MHAGame game;
    public static final String version="V0.6.4";
    

    public MHAServer(ChatArea cArea) {
           super("MHAServer");
           myChatArea = cArea;
	   com=new Vector<String>();
	   caracTrolls=new Vector<String>();
	   source=new Vector<Integer>();
	   game=new MHAGame();
    }

    public synchronized void run() {
           String line;
	   int s; 
	   while(true)
	   {
	   	try {
	   		if(com.size()==0)
			  wait();
		}
		catch(Exception e) {}
		line=com.elementAt(0);
		com.removeElementAt(0);
		s=source.elementAt(0);
		source.removeElementAt(0);
		if(analyse(s,line)==true)
		  break;
	   }
    }
    
    public synchronized void analyseCommand(int myIndex,String inputLine)
    {
    	com.add(inputLine);
	source.add(myIndex);
	notify();
    }
    
    public MHAGame getGame()
    {
    	return game;
    }
    
    private boolean analyse(int myIndex,String inputLine)
    {
        String[] liste=inputLine.trim().split(" ");
	Troll t;
	if(liste.length>=4 && liste[0].toLowerCase().equals("addtroll"))
	{
		int id,color,race;
		try
		{
			if(game.getState()==MHAGame.STATE_PLAYING) {
				myChatArea.putString(myIndex,"Error: game already started");
				return false;
			}
			id=Integer.parseInt(liste[1]);
			//color=Integer.parseInt(liste[3]);
			color=-1;
			race=Integer.parseInt(liste[liste.length-1]);
			for(int i=3;i<liste.length-1;i++)
				liste[2]+=" "+liste[i];
			//race=Integer.parseInt(liste[4]);
			if(game.getTrollBySocketId(myIndex)!=null)
			{
				myChatArea.putString(myIndex,"Error: You have already a troll");
				return false;
			}
			Troll tr=new Troll(liste[2],id,myIndex,color,race);
			if(!game.addTroll(tr))
			{
				myChatArea.putString(myIndex,"Error: there is already a troll with this name/id");
				return false;
			}
			tr.setTempsRestant(time_blitz);
			String nomRace="";
			if(race==Troll.RACE_SKRIM)
				nomRace="skrim";
			else if(race==Troll.RACE_DURAKUIR)
				nomRace="durakuir";
			else if(race==Troll.RACE_KASTAR)
				nomRace="kastar";
			else if(race==Troll.RACE_TOMAWAK)
				nomRace="tomawak";
			if(caracTrolls.size()<=myIndex)
				caracTrolls.setSize(myIndex+1);
			caracTrolls.set(myIndex,inputLine+"\n");
			//myChatArea.broadcast(liste[2]+" ("+id+"), "+nomRace+" de son état, vient d'arriver dans ce monde");
			myChatArea.putString(myIndex,"Created: "+liste[2]+" ("+id+"), "+nomRace+" vient d'être créé");
			return false;
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in addtroll instruction");
			return false;
		}
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("login"))
	{
		myChatArea.putString(myIndex,"MHA serveur "+version+"\n"+game.presentation);
		myChatArea.putString(myIndex,"Rules: "+game.getMode()+" "+game.isInviPossible()+" "+game.isTPPossible()+" "+game.getNbrTeam()); 
		Vector<Troll> liste2=game.getListeTrolls();
		//myChatArea.putString(myIndex,"Infos: ");
		for(int i=0;i<liste2.size();i++)
			if(liste2.elementAt(i).isCreated())
			{
				if(liste2.elementAt(i).isCertif())
					myChatArea.putString(myIndex,"newTroll "+liste2.elementAt(i).getId()+";"+liste2.elementAt(i).getRace()+";"+liste2.elementAt(i).getNiveau()+";1;"+liste2.elementAt(i).getName());
				else
					myChatArea.putString(myIndex,"newTroll "+liste2.elementAt(i).getId()+";"+liste2.elementAt(i).getRace()+";"+liste2.elementAt(i).getNiveau()+";0;"+liste2.elementAt(i).getName());
				if(game.getMode()==MHAGame.MODE_TEAM_DEATHMATCH && liste2.elementAt(i).getTeam()!=-1)
					myChatArea.putString(myIndex,"setteam "+liste2.elementAt(i).getId()+" "+liste2.elementAt(i).getTeam());
				if(liste2.elementAt(i).getIcon().length()!=0)
					myChatArea.putString(myIndex,"icontroll "+liste2.elementAt(i).getId()+" "+liste2.elementAt(i).getIcon());
			}
		
	}
	else if(liste.length>=1 && liste[0].toLowerCase().equals("validtroll"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre troll n'a pas encore été créé");
			return false;
		}
		if(t.isCreated())
		{
			myChatArea.putString(myIndex,"Error: votre troll a déja été validé");
			return false;
		}
		if(!t.valideTroll())
		{	
			myChatArea.putString(myIndex,"Error: le niveau de votre troll est incorrect");
			return false;
		}
		//System.out.println("Fichier :\n"+caracTrolls.get(myIndex));
		//System.out.println("Signature :\n"+liste[1]);
		if(liste.length==2 && TestSign.verifSignature(caracTrolls.get(myIndex),liste[1]))
		{
			myChatArea.broadcast("newTroll "+t.getId()+";"+t.getRace()+";"+t.getNiveau()+";1;"+t.getName());
			t.setCertif(true);
		}
		else
			myChatArea.broadcast("newTroll "+t.getId()+";"+t.getRace()+";"+t.getNiveau()+";0;"+t.getName());
		caracTrolls.set(myIndex,null);
		if(t.getIcon().length()!=0)
			myChatArea.broadcast("icontroll "+t.getId()+" "+t.getIcon());
	}
	else if(liste.length>=1 && liste[0].toLowerCase().equals("validbot"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre bot n'a pas encore été créé");
			return false;
		}
		if(t.isCreated())
		{
			myChatArea.putString(myIndex,"Error: votre bot a déja été validé");
			return false;
		}
		if(!t.valideTroll())
		{	
			myChatArea.putString(myIndex,"Error: le niveau de votre bot est incorrect");
			return false;
		}
		t.bot=new MHABot(game,t);
		if(liste.length==2 && TestSign.verifSignature(caracTrolls.get(myIndex),liste[1]))
			myChatArea.broadcast("newTroll "+t.getId()+";"+t.getRace()+";"+t.getNiveau()+";1;"+t.getName());
		else
			myChatArea.broadcast("newTroll "+t.getId()+";"+t.getRace()+";"+t.getNiveau()+";0;"+t.getName());
		caracTrolls.set(myIndex,null);
		if(t.getIcon().length()!=0)
			myChatArea.broadcast("icontroll "+t.getId()+" "+t.getIcon());
		myChatArea.putString(myIndex,"logout");
		myChatArea.setBot(myIndex);
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("startgame"))
	{
		if(myChatArea.getNumberOfClient() != game.getNumberOfTrolls())
		{
			myChatArea.putString(myIndex,"Error: Not all the clients have created a troll "+myChatArea.getNumberOfClient()+"/"+game.getNumberOfTrolls());
			return false;
		}
		Vector<Troll> liste3=game.getListeTrolls();
		for(int i=0;i<liste3.size();i++)
			if(!liste3.elementAt(i).isCreated())
			{
				myChatArea.putString(myIndex,"Error: Tous les trolls ne sont pas créés");
				return false;
			}
		if(!game.startGame())
		{
			myChatArea.putString(myIndex,"Error: there is already a game started");
			return false;
		}
		myChatArea.stopIncommingConnections();
		myChatArea.broadcast("The game begins");
		myChatArea.broadcast("ArenaSize "+game.getSizeArena());
		Vector<Troll> list=game.getListeTrolls();
		for(int i=0;i<list.size();i++)
			myChatArea.putString(list.elementAt(i).getSocketId(),game.getPosition(list.elementAt(i).getSocketId()));
		newTurn();
		return false;
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("getpos"))
	{
		myChatArea.putString(myIndex,game.getPosition(myIndex));
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("getvue"))
	{
		String s=game.getVue(myIndex);
		if(s.indexOf("Error:")==-1)
			s="Vue: "+s;
		myChatArea.putString(myIndex,s);
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("getequip"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre troll n'a pas encore été créé");
			return false;
		}
		myChatArea.putString(myIndex,"Equip: "+t.getEquip());
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("getcomp"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre troll n'a pas encore été créé");
			return false;
		}
		String s="Comp: ";
		
		for(int i=1;i<=Troll.NB_COMP;i++)
		{
			int level = t.getLevelComp(i);
			s+=t.getReussiteComp(i,1);
			for(int j=2;j<=level;j++)
				s+="|"+t.getReussiteComp(i,j);
			s+=";";
		}
		myChatArea.putString(myIndex,s.substring(0, s.length()-1));
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("getsort"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre troll n'a pas encore été créé");
			return false;
		}
		String s="Sort: ";
		for(int i=1;i<Troll.NB_SORT;i++)
			s+=t.getReussiteSort(i)+";";
		myChatArea.putString(myIndex,s+t.getReussiteSort(Troll.NB_SORT));
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("getlieux"))
	{
		myChatArea.putString(myIndex,"Lieux: "+game.getLieux(myIndex));
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("getinfoslieu"))
	{
		myChatArea.putString(myIndex,"InfosLieu: "+game.getInfosLieu(myIndex));
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("getprofil"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre troll n'a pas encore été créé");
			return false;
		}
		myChatArea.putString(myIndex,"Profil: "+t.getProfil());
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("getfullprofil"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre troll n'a pas encore été créé");
			return false;
		}
		myChatArea.putString(myIndex,"FullProfil: "+game.getTrollBySocketId(myIndex).getFullProfil());
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("getpa"))
	{
		if(!isTurn(myIndex))
			return false;
		myChatArea.putString(myIndex,"PA: "+game.getCurrentTroll().getPA());
		return false;
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("logout"))
	{
		t=game.getTrollBySocketId(myIndex);
		myChatArea.putString(myIndex,"logout");
		if(t!=null)
		{
			
			myChatArea.broadcast("Deconnexion: "+t.getId()+" "+t.getName());
			game.removeTroll(t);
			if(game.getCurrentTroll()==t && game.getState()==MHAGame.STATE_PLAYING)
			{
				t.endTurn();
				newTurn();
			}
			return false;
		}
		return false;
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("enddla"))
	{
		if(!isActiveTurn(myIndex))
		{
//			myChatArea.putString(myIndex,"Error: Vous avez déja fini de jouer votre DLA");
			return false;
		}
		if(time_blitz>0)
			currentTimer.stop();
		game.getCurrentTroll().endTurn();
		if(time_blitz>0)
			game.getCurrentTroll().setTempsRestant(time_blitz);
		newTurn();
		return false;
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("begindla"))
	{
		if((!isUnactiveTurn(myIndex) || game.getCurrentTroll().getNouveauTour()>game.getTime()) && !game.getCurrentTroll().isDead())
		{
			myChatArea.putString(myIndex,"Error: Vous avez déja activé votre DLA");
			return false;
		}
		myChatArea.putString(myIndex,"Newturn: "+game.getCurrentTroll().beginTurn(game.getTime()));
		//Suicide du à un venin
		if(game.getCurrentTroll().getPV()<=0)
		{
			game.events.add(game.getTime()+" "+game.getCurrentTroll().getName()+" ("+game.getCurrentTroll().getId()+") est mort empoisonné");
			newTurn();
		}
		return false;
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("activeturn"))
	{
		if(!isUnactiveTurn(myIndex)) 
		{
			myChatArea.putString(myIndex,"Error: Vous pouvez déja jouer");
			return false;
		}
		if(game.getCurrentTroll().getNouveauTour()<=game.getTime())
		{
			myChatArea.putString(myIndex,"Error: Vous devez activer votre DLA");
			return false;
		}
		game.getCurrentTroll().setActive(true);
		myChatArea.putString(myIndex,"Activeturn");
		return false;
	}
	else if(liste.length>1 && (liste[0].toLowerCase().equals("comp") || liste[0].toLowerCase().equals("sort") ))
	{
		if(!isActiveTurn(myIndex))
			return false;
		competence(liste);
		endAction();
		return false;
	}
	else if(liste.length==3 && liste[0].toLowerCase().equals("addsort"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre troll n'a pas encore été créé");
			return false;
		}
		if(t.isCreated())
		{
			myChatArea.putString(myIndex,"Error: votre troll a déja été validé");
			return false;
		}
		try
		{
			int idSort=Math.abs(Integer.parseInt(liste[1]));
			int pourSort=Math.abs(Integer.parseInt(liste[2]));
			caracTrolls.set(myIndex,caracTrolls.get(myIndex)+inputLine+"\n");
 			if(idSort<=0 || idSort>Troll.NB_SORT+1)
			{
				myChatArea.putString(myIndex,"Error: Ce sortilège n'existe pas");
				return false;
			}
			if(t.addSort(idSort,pourSort))
				myChatArea.putString(myIndex,"Vous connaissez maintenant le sortilège "+Troll.NOM_SORT[idSort-1]+" à "+pourSort+"%");
			else
				myChatArea.putString(myIndex,"Vous ne pouvez pas apprendre le sortilège "+Troll.NOM_SORT[idSort-1]+" à "+pourSort+"%");
			return false;
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in addsort instruction");
			return false;
		}
	}
	else if((liste.length==3 || liste.length==4) && liste[0].toLowerCase().equals("addcomp"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre troll n'a pas encore été créé");
			return false;
		}
		if(t.isCreated())
		{
			myChatArea.putString(myIndex,"Error: votre troll a déja été validé");
			return false;
		}
		try
		{
			int idComp=Math.abs(Integer.parseInt(liste[1]));
			int pourComp=Math.abs(Integer.parseInt(liste[2]));
			int level = 1;
			if (liste.length==4)
				level = Math.abs(Integer.parseInt(liste[3]));
			caracTrolls.set(myIndex,caracTrolls.get(myIndex)+inputLine+"\n");
 			if(idComp<=0 || idComp>Troll.NB_COMP+1)
			{
				myChatArea.putString(myIndex,"Error: Cete compétence n'existe pas");
				return false;
			}
			if(t.addComp(idComp,pourComp,level))
				myChatArea.putString(myIndex,"Vous connaissez maintenant la compétence "+Troll.NOM_COMP[idComp-1]+" au niveau "+level+" à "+pourComp+"%");
			else
				myChatArea.putString(myIndex,"Vous ne pouvez pas apprendre la compétence "+Troll.NOM_COMP[idComp-1]+" au niveau "+level+" à "+pourComp+"%");
			return false;
			
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in addcomp instruction");
			return false;
		}
	}
	else if(liste.length==2 && liste[0].toLowerCase().equals("icontroll"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre troll n'a pas encore été créé");
			return false;
		}
		if(t.isCreated())
		{
			myChatArea.putString(myIndex,"Error: votre troll a déja été validé");
			return false;
		}
		try
		{
			t.setIcon(liste[1]);
			//myChatArea.broadcast("icontroll "+t.getId()+" "+liste[1]);
			return false;
			
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in icontroll instruction");
			return false;
		}
	}
	else if(liste.length==2 && liste[0].toLowerCase().equals("setteam"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: Votre troll n'a pas encore été créé");
			return false;
		}
		if(game.getCurrentTroll()!=null)
		{
			myChatArea.putString(myIndex,"Error: La partie a déja commencé");
			return false;
		}
		if(game.getMode()!=MHAGame.MODE_TEAM_DEATHMATCH)
		{
			myChatArea.putString(myIndex,"Error: Le mode ne requiert pas d'équipe");
			return false;
		}
		try
		{
			int idteam=Math.abs(Integer.parseInt(liste[1]));

 			if(idteam<0 || idteam>=game.getNbrTeam())
			{
				myChatArea.putString(myIndex,"Error: Mauvais numéro d'équipe");
				return false;
			}
			t.setTeam(idteam);
			myChatArea.broadcast("setteam "+t.getId()+" "+idteam);
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in addsort instruction");
			return false;
		}
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("getbm"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre troll n'a pas encore été créé");
			return false;
		}
		myChatArea.putString(myIndex,"BM: "+t.getBM());
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("getmouches"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre troll n'a pas encore été créé");
			return false;
		}
		myChatArea.putString(myIndex,"Mouches: "+t.getMouches());
	}
	else if(liste.length==11 && liste[0].toLowerCase().equals("carac"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre troll n'a pas encore été créé");
			return false;
		}
		if(t.isCreated())
		{
			myChatArea.putString(myIndex,"Error: votre troll a déja été validé");
			return false;
		}
		try
		{
			int niv=Math.abs(Integer.parseInt(liste[1]));
			int att=Math.abs(Integer.parseInt(liste[2]));
			int esq=Math.abs(Integer.parseInt(liste[3]));
			int deg=Math.abs(Integer.parseInt(liste[4]));
			int reg=Math.abs(Integer.parseInt(liste[5]));
			int pv=Math.abs(Integer.parseInt(liste[6]));
			int vue=Math.abs(Integer.parseInt(liste[7]));
			int dla=Math.abs(Integer.parseInt(liste[8]));
			int mm=Math.abs(Integer.parseInt(liste[9]));
			int rm=Math.abs(Integer.parseInt(liste[10]));
			t.setProfil(niv,att,esq,deg,reg,pv,vue,dla,rm,mm);
			caracTrolls.set(myIndex,caracTrolls.get(myIndex)+inputLine+"\n");
			myChatArea.putString(myIndex,"Caractéristiques de votre troll entrées");
			return false;
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in carac instruction");
			return false;
		}
	}
	else if(liste.length==2 && liste[0].toLowerCase().equals("getinfostroll"))
	{
		try
		{
			int idTroll=Math.abs(Integer.parseInt(liste[1]));
			t=game.getTrollById(idTroll);
			if(t==null)
			{
				myChatArea.putString(myIndex,"Error: Ce troll n'existe pas");
				return false;
			}	
			myChatArea.putString(myIndex,"InfosTroll: "+t.getInfosTroll());
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in getinfostroll instruction");
			return false;
		}
	}
	else if(liste.length>=2 && liste[0].toLowerCase().equals("addmouche"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre troll n'a pas encore été créé");
			return false;
		}
		if(t.isCreated())
		{
			myChatArea.putString(myIndex,"Error: votre troll a déja été validé");
			return false;
		}
		try
		{
			int typeMouche=Math.abs(Integer.parseInt(liste[1]));
			if(typeMouche<0 || typeMouche>8)
			{
				myChatArea.putString(myIndex,"Error: Ce type de mouche n'existe pas");
				return false;
			}
			String nomMouche="Sans nom";
			if(liste.length>=3)
				nomMouche=liste[2];
			for(int i=3;i<liste.length;i++)
				nomMouche+=" "+liste[i];
			t.addMouche(new Mouche(nomMouche,typeMouche));
			caracTrolls.set(myIndex,caracTrolls.get(myIndex)+inputLine+"\n");
			myChatArea.putString(myIndex,"La mouche "+nomMouche+" a été ajoutée");
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in addmouche instruction");
			return false;
		}
	}
	else if(liste.length>=19 && liste[0].toLowerCase().equals("addequip"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: votre troll n'a pas encore été créé");
			return false;
		}
		if(t.isCreated())
		{
			myChatArea.putString(myIndex,"Error: votre troll a déja été validé");
			return false;
		}
		try
		{
			boolean smallbmm=false,fullbmm=false;
			String nomEquip="";
			try{Integer.parseInt(liste[18]);Integer.parseInt(liste[19]);smallbmm=true;}catch(NumberFormatException e){}
			try{Integer.parseInt(liste[21]);Integer.parseInt(liste[22]);fullbmm=true;smallbmm=false;}catch(Exception e){}
			if(fullbmm)
			{
				int idEquip=Integer.parseInt(liste[1]);
				int typeEquip=Integer.parseInt(liste[2]);
				int attEquip=Integer.parseInt(liste[3]);
				int attMEquip=Integer.parseInt(liste[4]);
				int esqEquip=Integer.parseInt(liste[5]);
				int esqMEquip=Integer.parseInt(liste[6]);
				int degEquip=Integer.parseInt(liste[7]);
				int degMEquip=Integer.parseInt(liste[8]);
				int dlaEquip=Integer.parseInt(liste[9]);
				int regEquip=Integer.parseInt(liste[10]);
				int regMEquip=Integer.parseInt(liste[11]);
				int pvEquip=Integer.parseInt(liste[12]);
				int vueEquip=Integer.parseInt(liste[13]);
				int vueMEquip=Integer.parseInt(liste[14]);
				int apEquip=Integer.parseInt(liste[15]);
				int amEquip=Integer.parseInt(liste[16]);
				//int zoneEquip=Math.abs(Integer.parseInt(liste[17]));
				//int dropEquip=Math.abs(Integer.parseInt(liste[18]));
				int poidsEquip=Integer.parseInt(liste[19]);
				int mmEquip=Integer.parseInt(liste[20]);
				int rmEquip=Integer.parseInt(liste[21]);
				//int equipEquip=Math.abs(Integer.parseInt(liste[22]));
				if(typeEquip<0 || typeEquip>15)
				{
					myChatArea.putString(myIndex,"Error: Ce type d'objet n'existe pas");
					return false;
				}
				boolean zoneEquip=false;
				if(liste[17].equals("1"))
					zoneEquip=true;
				boolean dropEquip=false;
				if(liste[18].equals("1"))
					dropEquip=true;	
				nomEquip=liste[23];
				for(int i=23;i<liste.length;i++)
					nomEquip+=" "+liste[i];
				Equipement equip=new Equipement(idEquip,nomEquip,typeEquip,attEquip,attMEquip,esqEquip,esqMEquip,degEquip,degMEquip,dlaEquip,regEquip,regMEquip,vueEquip,vueMEquip,pvEquip,apEquip,amEquip,mmEquip,rmEquip,zoneEquip,dropEquip,poidsEquip);
				t.addEquipement(equip);
				caracTrolls.set(myIndex,caracTrolls.get(myIndex)+inputLine+"\n");
				if(liste[22].equals("1"))
					t.equipe(equip);
			}
			if(!smallbmm)
			{
				int idEquip=Integer.parseInt(liste[1]);
				int typeEquip=Integer.parseInt(liste[2]);
				int attEquip=Integer.parseInt(liste[3]);
				int esqEquip=Integer.parseInt(liste[4]);
				int degEquip=Integer.parseInt(liste[5]);
				int dlaEquip=Integer.parseInt(liste[6]);
				int regEquip=Integer.parseInt(liste[7]);
				int pvEquip=Integer.parseInt(liste[8]);
				int vueEquip=Integer.parseInt(liste[9]);
				int apEquip=Integer.parseInt(liste[10]);
				int amEquip=Integer.parseInt(liste[11]);
				//int zoneEquip=Math.abs(Integer.parseInt(liste[12]));
				//int dropEquip=Math.abs(Integer.parseInt(liste[13]));
				int poidsEquip=Integer.parseInt(liste[14]);
				int mmEquip=Integer.parseInt(liste[15]);
				int rmEquip=Integer.parseInt(liste[16]);
				//int equipEquip=Math.abs(Integer.parseInt(liste[17]));
				if(typeEquip<0 || typeEquip>15)
				{
					myChatArea.putString(myIndex,"Error: Ce type d'objet n'existe pas");
					return false;
				}
				boolean zoneEquip=false;
				if(liste[12].equals("1"))
					zoneEquip=true;
				boolean dropEquip=false;
				if(liste[13].equals("1"))
					dropEquip=true;	
				nomEquip=liste[18];
				for(int i=19;i<liste.length;i++)
					nomEquip+=" "+liste[i];
				Equipement equip=new Equipement(idEquip,nomEquip,typeEquip,attEquip,esqEquip,degEquip,dlaEquip,regEquip,vueEquip,pvEquip,apEquip,amEquip,mmEquip,rmEquip,zoneEquip,dropEquip,poidsEquip);
				t.addEquipement(equip);
				caracTrolls.set(myIndex,caracTrolls.get(myIndex)+inputLine+"\n");
				if(liste[17].equals("1"))
					t.equipe(equip);
			}
			else
			{
				int idEquip=Integer.parseInt(liste[1]);
				int typeEquip=Integer.parseInt(liste[2]);
				int attEquip=Integer.parseInt(liste[3]);
				int attMEquip=Integer.parseInt(liste[4]);
				int esqEquip=Integer.parseInt(liste[5]);
				int degEquip=Integer.parseInt(liste[6]);
				int degMEquip=Integer.parseInt(liste[7]);
				int dlaEquip=Integer.parseInt(liste[8]);
				int regEquip=Integer.parseInt(liste[9]);
				int pvEquip=Integer.parseInt(liste[10]);
				int vueEquip=Integer.parseInt(liste[11]);
				int apEquip=Integer.parseInt(liste[12]);
				int amEquip=Integer.parseInt(liste[13]);
				//int zoneEquip=Math.abs(Integer.parseInt(liste[14]));
				//int dropEquip=Math.abs(Integer.parseInt(liste[15]));
				int poidsEquip=Integer.parseInt(liste[16]);
				int mmEquip=Integer.parseInt(liste[17]);
				int rmEquip=Integer.parseInt(liste[18]);
				//int equipEquip=Math.abs(Integer.parseInt(liste[19]));
				if(typeEquip<0 || typeEquip>15)
				{
					myChatArea.putString(myIndex,"Error: Ce type d'objet n'existe pas");
					return false;
				}
				boolean zoneEquip=false;
				if(liste[14].equals("1"))
					zoneEquip=true;
				boolean dropEquip=false;
				if(liste[15].equals("1"))
					dropEquip=true;	
				nomEquip=liste[20];
				for(int i=21;i<liste.length;i++)
					nomEquip+=" "+liste[i];
				Equipement equip=new Equipement(idEquip,nomEquip,typeEquip,attEquip,attMEquip,esqEquip,degEquip,degMEquip,dlaEquip,regEquip,vueEquip,pvEquip,apEquip,amEquip,mmEquip,rmEquip,zoneEquip,dropEquip,poidsEquip);
				t.addEquipement(equip);
				caracTrolls.set(myIndex,caracTrolls.get(myIndex)+inputLine+"\n");
				if(liste[19].equals("1"))
					t.equipe(equip);
			}
			myChatArea.putString(myIndex,"L'objet "+nomEquip+" a été ajouté");
			return false;
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in addequip instruction");
			return false;
		}
	}
	else if(liste.length==2 && liste[0].toLowerCase().equals("decaletour"))
	{
		try
		{
			if(!isTurn(myIndex))
				return false;
			int tmps=Math.abs(Integer.parseInt(liste[1]));
			if(time_blitz>0)
				currentTimer.stop();
			if(time_blitz>0)
				game.getCurrentTroll().setTempsRestant(currentTimer.getDelay());
			game.getCurrentTroll().decale(tmps);
			newTurn();
			return false;
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in decale instruction");
			return false;
		}
	}
	else if(liste.length==2 && liste[0].toLowerCase().equals("decaledla"))
	{
		try
		{
			if(!isTurn(myIndex))
				return false;
			int tmps=Math.abs(Integer.parseInt(liste[1]));
			game.getCurrentTroll().setNouveauTour(tmps+game.getCurrentTroll().getNouveauTour());
			myChatArea.putString(myIndex,"Vous avez décalé votre tour de "+tmps+" minutes\n"+
				"Votre nouvelle DLA est le "+Troll.hour2string(game.getCurrentTroll().getNouveauTour()));
			newTurn();
			return false;
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in decale instruction");
			return false;
		}
	}
	else if(liste.length==2 && liste[0].toLowerCase().equals("attaque"))
	{
		try
		{
			if(!isActiveTurn(myIndex))
				return false;
			int id=Integer.parseInt(liste[1]);
			t=game.getTrollById(id);
			if(t==null)
			{
				myChatArea.putString(myIndex,"Error: Unknown troll");
				return false;
			}
			String s=game.attaque(t);
			if(s.substring(0,6).compareTo("Error:")!=0)
			{
				myChatArea.putString(myIndex,s);
			}
			else
				myChatArea.putString(myIndex,s);
			endAction();
			return false;
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in attaque instruction");
			return false;
		}
	}
	else if(liste.length==2 && liste[0].toLowerCase().equals("concentre"))
	{
		try
		{
			if(!isActiveTurn(myIndex))
				return false;
			int id=Integer.parseInt(liste[1]);
			if(id<=0 || id>6)
			{
				myChatArea.putString(myIndex,"Error: vous ne pouvez pas vous concentrer pendant "+id+" PA");
				return false;
			}
			String s=game.concentration(id);
			myChatArea.putString(myIndex,s);
			endAction();
			return false;
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in concentre instruction");
			return false;
		}
	}
	else if(liste.length==3 && liste[0].toLowerCase().equals("utilise"))
	{
		try
		{
			if(!isActiveTurn(myIndex))
				return false;
			int id=Integer.parseInt(liste[1]);
			t=game.getTrollById(id);
			if(t==null)
			{
				myChatArea.putString(myIndex,"Error: Unknown troll");
				return false;
			}
			Equipement e=game.getCurrentTroll().getEquipementById(Integer.parseInt(liste[2]));
			if(e==null)
			{
				myChatArea.putString(game.getCurrentTroll().getSocketId(),"Error: Objet n°"+Integer.parseInt(liste[2])+" inconnu");
				return false;
			}
			String s=game.utilisePotionParchemin(e,t);
			myChatArea.putString(myIndex,s);
			endAction();
			return false;
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in utiliss instruction");
			return false;
		}
	}
	else if(liste.length==1 && liste[0].toLowerCase().equals("prendretp"))
	{
		if(!isActiveTurn(myIndex))
			return false;
		String s=game.prendreTP();
		myChatArea.putString(myIndex,s);
		endAction();
		return false;
	}
	else if(liste.length==4 && liste[0].toLowerCase().equals("deplace"))
	{
		int x,y,n;
		try
		{
			x=Integer.parseInt(liste[1]);
			y=Integer.parseInt(liste[2]);
			n=Integer.parseInt(liste[3]);
			if(!isActiveTurn(myIndex))
				return false;
			String s=game.deplace(x,y,n,false);
			if(s.substring(0,6).compareTo("Error:")!=0)
				myChatArea.putString(myIndex,s);
			else
				myChatArea.putString(myIndex,s);
			endAction();
			return false;
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in deplace instruction");
			return false;
		}
	}
	else if(liste.length>2 && liste[0].toLowerCase().equals("message"))
	{
		t=game.getTrollBySocketId(myIndex);
		if(t==null)
		{
			myChatArea.putString(myIndex,"Error: Vous devez avoir créé un troll.");
			return false;
		}
		if(liste[1].toLowerCase().equals("all"))
		{
			String mes=inputLine.substring(inputLine.indexOf(" ")+1);
			mes="message all "+game.getTrollBySocketId(myIndex).getId()+mes.substring(mes.indexOf(" "));
			for(int i=0;i<game.getListeTrolls().size();i++)
				myChatArea.putString(game.getListeTrolls().elementAt(i).getSocketId(),mes);
		}
		else if(liste[1].toLowerCase().equals("groupe"))
		{
			String mes=inputLine.substring(inputLine.indexOf(" ")+1);
			mes="message groupe "+game.getTrollBySocketId(myIndex).getId()+mes.substring(mes.indexOf(" "));
			t=game.getTrollBySocketId(myIndex);
			int id=t.getTeam();
			for(int i=0;i<game.getListeTrolls().size();i++)
				if(game.getListeTrolls().elementAt(i).getTeam()==id)
					myChatArea.putString(game.getListeTrolls().elementAt(i).getSocketId(),mes);
		}
		else try
		{
			int id=Integer.parseInt(liste[1]);
			String mes=inputLine.substring(inputLine.indexOf(" ")+1);
			
			t=game.getTrollById(id);
			if(t==null)
				myChatArea.putString(myIndex,"Error: Destinataire inexistant");
			else
			{
				mes="message prive "+game.getTrollBySocketId(myIndex).getId()+" "+t.getId()+mes.substring(mes.indexOf(" "));
				myChatArea.putString(myIndex,mes);
				if(t.getSocketId()!=myIndex)
					myChatArea.putString(t.getSocketId(),mes);
			}
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(myIndex,"Error: Bad data formating in message instruction");
			return false;
		}
		return false;
	}
	else
	{
		myChatArea.putString(myIndex,"Error: command unknown");
    	//	System.out.println(myIndex+" "+inputLine);
	}
	return false;
    }
    
	public void endAction()
	{
		for(int i=0;i<game.getListeTrolls().size();i++)
			while(game.getListeTrolls().elementAt(i).getInbox().size()!=0)
				myChatArea.putString(game.getListeTrolls().elementAt(i).getSocketId(),"Action: "+game.getListeTrolls().elementAt(i).getInbox().remove(0));
		if(game.getCurrentTroll().getPV()<=0)
			newTurn();
	}
    
	public void newTurn()
	{
		game.newTurn();
		while(game.events.size()!=0)
			myChatArea.broadcast("Event "+game.events.remove(0));
		if(!game.gameOver())
		{
			for(int i=0;i<game.getListeTrolls().size();i++)
			{
				if(!game.getListeTrolls().elementAt(i).isDead())
				{
					myChatArea.putString(game.getListeTrolls().elementAt(i).getSocketId(),"Vue: "+game.getVue(game.getListeTrolls().elementAt(i).getSocketId()));
					myChatArea.putString(game.getListeTrolls().elementAt(i).getSocketId(),"Lieux: "+game.getLieux(game.getListeTrolls().elementAt(i).getSocketId()));
				}
			}
			myChatArea.broadcast("Time: "+game.getTime());
			if(game.getCurrentTroll().bot==null)
			{
				myChatArea.putString(game.getCurrentTroll().getSocketId(),"begin "+game.getCurrentTroll().getNouveauTour());
				if(time_blitz>0)
				{
					ActionListener taskPerformer = new ActionListener() {
						public void actionPerformed(ActionEvent evt) 
						{
							if(!game.getCurrentTroll().isActive())
								game.getCurrentTroll().beginTurn(game.getTime());
							game.getCurrentTroll().endTurn();
							//System.out.println("Timer a claqué");
							game.getCurrentTroll().setTempsRestant(time_blitz);
							newTurn();
						}
					};
					currentTimer=new Timer(game.getCurrentTroll().getTempsRestant(), taskPerformer);
					currentTimer.setRepeats(false);
					currentTimer.start();
					//System.out.println("Timer démarré pour une durée de "+game.getCurrentTroll().getTempsRestant()+"ms");
					//new BlitzTimer(myChatArea,game.getCurrentTroll(),this);
				}
			}
			else
			{
				game.getCurrentTroll().bot.play();
				endAction();
				newTurn();
			}
		}
		else
		{
			String s="Partie terminée\n";
			int vainqueur=game.getVainqueur();
			if(game.getMode() == MHAGame.MODE_TEAM_DEATHMATCH)
			{
				if(vainqueur==-1)
					s+="Il y a égalité ! Aucune équipe n'a su l'emporter\nIl faudrait apprendre à jouer !!\n\n";
				else
					s+="L'équipe "+vainqueur+" a mis la patée aux autres Trolls\nElle remporte donc la victoire haut la main\n\n";
			}
			else if(game.getMode() == MHAGame.MODE_DEATHMATCH)
			{
				//System.out.println("Vainqueur "+vainqueur);
				if(vainqueur==-1)
					s+="Il y a égalité ! Aucun Troll n'a su l'emporter\nIl faudrait apprendre à jouer !!\n\n";
				else if(game.getTrollById(vainqueur)!=null)
					s+=game.getTrollById(vainqueur).getName()+" a su mettre la patée aux autres Trolls\nIl remporte donc la victoire\n\n";
			}
			Vector<Troll> vt=game.getListeTrolls();
			if(vt.size()==0)
			{
				myChatArea.broadcast(s);
				return;
			}	
			Troll kill=vt.elementAt(0),regen=vt.elementAt(0),PA=vt.elementAt(0), deg=vt.elementAt(0);
			for(int i=1;i<vt.size();i++)
			{
				if(kill.getNbKills()<vt.elementAt(i).getNbKills())
					kill=vt.elementAt(i);
				if(regen.getPVRegen()<vt.elementAt(i).getPVRegen())
					regen=vt.elementAt(i);
				if(PA.getPAUtil()>vt.elementAt(i).getPAUtil())
					PA=vt.elementAt(i);
				if(deg.getDegInflig()<vt.elementAt(i).getDegInflig())
					deg=vt.elementAt(i);
				
			}
			s+="Troll le plus meurtrier : "+kill.getName()+"\n";
			s+="Troll le plus dangereux : "+deg.getName()+"\n";
			s+="Troll le plus régénérant : "+regen.getName()+"\n";
			s+="Troll le plus inutile : "+PA.getName();
			myChatArea.broadcast(s);
			myChatArea.restartServer();
		}
	}
    
    private boolean isActiveTurn(int i)
    {
    	if(game.getCurrentTroll()==null)
	{
		myChatArea.putString(i,"Error: the game did not begin");
		return false;
	}
    	if(game.getTrollBySocketId(i)!=game.getCurrentTroll())
	{
		myChatArea.putString(i,"Error: it is not your turn");
		return false;
	}
	if(!game.getCurrentTroll().isActive())
	{
		myChatArea.putString(i,"Error: you do not active your turn");
		return false;
	}
	return true;
    }
    
    private boolean isTurn(int i)
    {
    	if(game.getCurrentTroll()==null)
	{
		myChatArea.putString(i,"Error: the game did not begin");
		return false;
	}
    	if(game.getTrollBySocketId(i)!=game.getCurrentTroll())
	{
		myChatArea.putString(i,"Error: it is not your turn");
		return false;
	}
	return true;
   }
   
    private boolean isUnactiveTurn(int i)
    {
    	if(game.getCurrentTroll()==null)
	{
		myChatArea.putString(i,"Error: the game did not begin");
		return false;
	}
    	if(game.getTrollBySocketId(i)!=game.getCurrentTroll())
	{
		myChatArea.putString(i,"Error: it is not your turn");
		return false;
	}
	if(game.getCurrentTroll().isActive())
	{
		myChatArea.putString(i,"Error: you have a turn still active");
		return false;
	}
	return true;
    }
    
    public void sendMessage(int i, String s)
    {
    	myChatArea.putString(i,s);
    }
    
    public static final String[][] listeSortileges=
	{{"hypnotisme","Troll"},
	{"rafalePsychique","Troll"},
	{"vampirisme","Troll"},
	{"projectileMagique","Troll"},
	{"analyseAnatomique","Troll"},
	{"armureEtheree"},
	{"augmentationDeLAttaque"},
	{"augmentationDeLEsquive"},
	{"augmentationDesDegats"},
	{"bulleDAntiMagie"},
	{"bulleMagique"},
	{"explosion"},
	{"faiblessePassagère","Troll"},
	{"flashAveuglant"},
	{"glue","Troll"},
	{"griffeDuSorcier","Troll"},
	{"invisibilite"},
	{"projection","Troll"},
	{"sacrifice","Troll","int"},
	{"teleportation","int","int","int"},
	{"visionAccrue"},
	{"visionLointaine","int","int","int"},
	{"voirLeCache","int","int","int"},
	{"vueTroublee","Troll"}
	};
    
    public static final String[][] listeCompetences=
    	{{"botteSecrete","Troll"},
	{"regenerationAccrue"},
	{"accelerationDuMetabolisme","int"},
	{"camouflage"},
	{"attaquePrecise","Troll"},
	{"charger","Troll"},
	{"construireUnPiege"},
	{"contreAttaque"},
	{"coupDeButoir","Troll"},
	{"deplacementEclair","int","int","int"},
	{"frenesie","Troll"},
	{"lancerDePotion","Troll","Objet"},
	{"parer"},
	{"pistage","Troll"}
	};
    
	private void competence(String[] liste)
	{
		String[][] infos=listeCompetences;
		if(liste[0].equals("sort"))
			infos=listeSortileges;
		try 
		{
			int id=Integer.parseInt(liste[1]);
			if(id<1 || id>infos.length)
			{
				myChatArea.putString(game.getCurrentTroll().getSocketId(),"Error: Bad data formating");
				return ;
			}
			if(liste.length!=infos[id-1].length+1)
			{
				myChatArea.putString(game.getCurrentTroll().getSocketId(),"Error: Mauvais nombre d'arguments");
				return ;
			}
			Class [] argsClass = new Class[infos[id-1].length-1];
			Object [] myArgs=new Object[infos[id-1].length-1];
			Class thisClass=game.getClass();
			for(int i=1;i<infos[id-1].length;i++)
			{
				if(infos[id-1][i].equals("int"))
				{
					argsClass[i-1]=Integer.TYPE;
					myArgs[i-1]=Integer.parseInt(liste[i+1]);
				}
				else if(infos[id-1][i].equals("Troll"))
				{
					Troll t=game.getTrollById(Integer.parseInt(liste[i+1]));
					if(t==null)
					{
						myChatArea.putString(game.getCurrentTroll().getSocketId(),"Error: Troll n°"+Integer.parseInt(liste[i+1])+" inconnu");
						return ;
					}
					argsClass[i-1]=t.getClass();
					myArgs[i-1]=t;
				}
				else if(infos[id-1][i].equals("Objet"))
				{
					Equipement e=game.getCurrentTroll().getEquipementById(Integer.parseInt(liste[i+1]));
					if(e==null)
					{
						myChatArea.putString(game.getCurrentTroll().getSocketId(),"Error: Objet n°"+Integer.parseInt(liste[i+1])+" inconnu");
						return ;
					}
					argsClass[i-1]=e.getClass();
					myArgs[i-1]=e;
				}
			}
			Method m=thisClass.getMethod(infos[id-1][0],argsClass);
			myChatArea.putString(game.getCurrentTroll().getSocketId(),(String) m.invoke(game,myArgs));	
		}
		catch(NumberFormatException e)
		{
			myChatArea.putString(game.getCurrentTroll().getSocketId(),"Error: Bad data formating");
			return ;
		}
		catch(InvocationTargetException e)
		{
			myChatArea.putString(game.getCurrentTroll().getSocketId(),"Error: Internal server error");
			e.printStackTrace();
			return ;
		}
		catch (Exception e)
		{
			myChatArea.putString(game.getCurrentTroll().getSocketId(),"Error: Internal server error (2)");
			return ;
		}
	}
}
