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

package mha.engine.core;

//C'est mal !!!
import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

import mha.engine.dice.DiceHelper;

public class MHAGame implements Serializable {

	private static Random r = new Random();
	private final DiceHelper diceHelper;

	//private ListeTrolls trolls;
	public Vector listeEquipement;
	private Vector<Troll> trolls;
	public Vector<String> events;
	private Vector<Lieu> listeLieux=new Vector<Lieu>();
	static final long serialVersionUID = 1231521531;

	private Troll currentTroll;
	private int current_time=0;
	private int gameState;

	// La config de la partie !

	private static int gameMode;
	private static int sizeArena=0;
	private static int nbteam=-1;
	private static boolean useTP=false;
	private static boolean useInvisibilite=false;
	private static boolean tomCamoufle=false;
	private static boolean regroupe=false;
	private static int nbRespawn=0;
	private int vainqueur=-2;
	public String presentation="Bienvenue sur ce serveur !";

	//Les valeurs pr�d�finies

	public final static int STATE_NEW_GAME        = 0;
	public final static int STATE_PLAYING         = 1;
	public final static int STATE_SYNCHRO         = 2;
	public final static int STATE_GAMEOVER        = 3;

	public final static int MODE_DEATHMATCH      = 0;
	public final static int MODE_TEAM_DEATHMATCH = 1;

	public final static int DRAW_DRAW       = 0;
	public final static int DRAW_MAX_DEGAT  = 1;
	public final static int DRAW_MAX_NIVEAU = 2;


	public MHAGame() {
		diceHelper = new DiceHelper();
		trolls = new Vector<Troll>();
		events = new Vector<String>();
		//currentPlayer=null;
		gameState=STATE_NEW_GAME;
		//System.out.print("New Game created\n"); // testing
	}

	public MHAGame(int mode,boolean tp,boolean invi,int nbt, int nbr) {
		this();
		if(mode==MODE_TEAM_DEATHMATCH)
		{
			gameMode=MODE_TEAM_DEATHMATCH;
			nbteam=nbt;
		}
		else
			gameMode=MODE_DEATHMATCH;
		useTP=tp;
		useInvisibilite=invi;
		nbRespawn=nbr;
	}

	public int getVainqueur()
	{
		if(gameState!=STATE_GAMEOVER)
			return -2;
		return vainqueur;
	}

	public int getState()
	{
		return gameState;
	}

	public static int getMode()
	{
		return gameMode;
	}

	public static void setMode(int m)
	{
		if(m==0 || m==1)
			gameMode=m;
	}

	public static void setNbrResu(int i)
	{
		nbRespawn=i;
	}

	public static int getNbrResu()
	{
		return nbRespawn;
	}

	public static boolean isTPPossible()
	{
		return useTP;
	}

	public static void setTPPossible(boolean b)
	{
		useTP=b;
	}

	public static  boolean isInviPossible()
	{
		return useInvisibilite;
	}

	public static void setInviPossible(boolean b)
	{
		useInvisibilite=b;
	}

	public static void setTomCamoufl�s(boolean tc){
		tomCamoufle=tc;
	}

	public static boolean isTomCamoufle(){
		return tomCamoufle;
	}

	public int getNumberOfTrolls()
	{
		return trolls.size();
	}

	public boolean addTroll(Troll t)
	{
		if(gameState!=STATE_NEW_GAME)
			return false;
		for(int i=0;i<trolls.size();i++)
		{
			if(t.compare(trolls.elementAt(i)))
				return false;
		}
		trolls.add(t);
		if(t.getRace() == Troll.RACE_TOMAWAK){t.setCamouflage(isTomCamoufle());}
		t.setResu(nbRespawn);
		return true;
	}

	public Troll getTrollBySocketId(int id)
	{
		for(int i=0;i<trolls.size();i++)
		{
			if(id == trolls.elementAt(i).getSocketId() )
				return trolls.elementAt(i);
		}
		return null;
	}

	public Troll getTrollById(int id)
	{
		for(int i=0;i<trolls.size();i++)
		{
			if(id == trolls.elementAt(i).getId() )
				return trolls.elementAt(i);
		}
		return null;
	}

	public void removeTroll(Troll t)
	{
		trolls.remove(t);
	}

	public static int getNbrTeam()
	{
		return nbteam;
	}

	public static void setNbrTeam(int i)
	{
		nbteam=i;
	}

	public static void placeTrollInHisTeam(Troll t)
	{
		int team = t.getTeam();
		switch(nbteam)
		{
		case 2:
			switch(team)
			{
			case 0:
				t.setPos(0,0,-1);
				break;
			case 1:
				t.setPos(sizeArena-1,sizeArena-1,-(sizeArena+1)/2);
				break;
			}
			break;
		case 3:
			switch(team)
			{
			case 0:
				t.setPos(0,0,-1);
				break;
			case 1:
				t.setPos(sizeArena-1,sizeArena-1,-(sizeArena+1)/4);
				break;
			case 2:
				t.setPos(sizeArena-1,0,-(sizeArena+1)/2);
				break;
			}
			break;
		case 4:
			switch(team)
			{
			case 0:
				t.setPos(0,0,-1);
				break;
			case 1:
				t.setPos(sizeArena-1,sizeArena-1,-1);
				break;
			case 2:
				t.setPos(0,sizeArena-1,-(sizeArena+1)/2);
				break;
			case 3:
				t.setPos(sizeArena-1,0,-(sizeArena+1)/2);
				break;
			}
			break;
		case 5:
			switch(team)
			{
			case 0:
				t.setPos(0,0,-1);
				break;
			case 1:
				t.setPos(sizeArena-1,sizeArena-1,-1);
				break;
			case 2:
				t.setPos(0,sizeArena-1,-(sizeArena+1)/2);
				break;
			case 3:
				t.setPos(sizeArena-1,0,-(sizeArena+1)/2);
				break;
			case 4:
				t.setPos((sizeArena-1)/2,(sizeArena-1)/2,-(sizeArena+1)/4);
				break;
			}
			break;
		case 6:
			switch(team)
			{
			case 0:
				t.setPos(0,0,-1);
				break;
			case 1:
				t.setPos(sizeArena-1,0,-1);
				break;
			case 2:
				t.setPos((sizeArena-1)/2,sizeArena-1,-1);
				break;
			case 3:
				t.setPos(0,0,-(sizeArena+1)/2);
				break;
			case 4:
				t.setPos(sizeArena-1,0,-(sizeArena+1)/2);
				break;
			case 5:
				t.setPos((sizeArena-1)/2,sizeArena-1,-(sizeArena+1)/2);
				break;
			}
			break;
		case 7:
			switch(team)
			{
			case 0:
				t.setPos(0,0,-1);
				break;
			case 1:
				t.setPos(sizeArena-1,0,-1);
				break;
			case 2:
				t.setPos((sizeArena-1)/2,sizeArena-1,-1);
				break;
			case 3:
				t.setPos(0,0,-(sizeArena+1)/2);
				break;
			case 4:
				t.setPos(sizeArena-1,0,-(sizeArena+1)/2);
				break;
			case 5:
				t.setPos((sizeArena-1)/2,sizeArena-1,-(sizeArena+1)/2);
				break;
			case 6:
				t.setPos((sizeArena-1)/2,(sizeArena-1)/2,-(sizeArena+1)/4);
				break;
			}
			break;
		case 8:
			switch(team)
			{
			case 0:
				t.setPos(0,0,-1);
				break;
			case 1:
				t.setPos(sizeArena-1,0,-1);
				break;
			case 2:
				t.setPos(0,sizeArena-1,-1);
				break;
			case 3:
				t.setPos(sizeArena-1,sizeArena-1,-1);
				break;
			case 4:
				t.setPos(0,0,-(sizeArena+1)/2);
				break;
			case 5:
				t.setPos(sizeArena-1,0,-(sizeArena+1)/2);
				break;
			case 6:
				t.setPos(0,sizeArena-1,-(sizeArena+1)/2);
				break;
			case 7:
				t.setPos(sizeArena-1,sizeArena-1,-(sizeArena+1)/2);
				break;
			}
			break;
		}
	}

	public boolean startGame()
	{
		if(gameState!=STATE_NEW_GAME)
			return false;
		if(gameMode==MODE_TEAM_DEATHMATCH)
		{
			for(int i=0;i<trolls.size();i++)
			{
				if(trolls.elementAt(i).getTeam()<0 || (nbteam!=-1 && trolls.elementAt(i).getTeam()>=nbteam) )
					return false;
			}
		}
		gameState=STATE_PLAYING;
		if(sizeArena<1)
		{
			sizeArena=trolls.size()*3;
		}
		if(gameMode==MODE_TEAM_DEATHMATCH && regroupe)
		{
			for(int i=0;i<trolls.size();i++)
			{
				placeTrollInHisTeam(trolls.elementAt(i));
			}
		}
		else
		{
			for(int i=0;i<trolls.size();i++)
			{
				trolls.elementAt(i).setPos(diceHelper.roll(1, sizeArena) - 1, diceHelper.roll(1, sizeArena) - 1,
						-diceHelper.roll(1, (sizeArena + 1) / 2));
			}
		}
		return true;
	}

	public void newTurn()
	{
		if(gameState!=STATE_PLAYING)
			return;
		int ct=5000000;
		int ctr=-1;
		int nbActifs=0;
		int lastActive=-1;
		boolean [] team={true};
		if(gameMode==MODE_TEAM_DEATHMATCH)
			team=new boolean[nbteam];
		for(int i=0;i<trolls.size();i++)
		{
			if(gameMode==MODE_TEAM_DEATHMATCH)
			{
				if(!trolls.elementAt(i).isDead() || (trolls.elementAt(i).isDead() && trolls.elementAt(i).getResu()>0))
				{
					if(!team[trolls.elementAt(i).getTeam()])
						nbActifs++;
					team[trolls.elementAt(i).getTeam()]=true;
					lastActive=trolls.elementAt(i).getTeam();
				}
			}
			else
				if(!trolls.elementAt(i).isDead() || (trolls.elementAt(i).isDead() && trolls.elementAt(i).getResu()>0))
				{
					nbActifs++;
					lastActive=trolls.elementAt(i).getId();
				}
			if((trolls.elementAt(i).getDateJeu()<ct && !trolls.elementAt(i).isDead()))
			{
				ct=trolls.elementAt(i).getDateJeu();
				ctr=i;
			}
			else if(trolls.elementAt(i).isDead() && trolls.elementAt(i).getResu()>0)
			{
				ct=current_time;
				ctr=i;
			}
		}
		if(nbActifs>1)
		{
			current_time=Math.max(ct,current_time);
			currentTroll=trolls.elementAt(ctr);
			for(int i=0;i<listeLieux.size();i++)
			{
				if((listeLieux.elementAt(i) instanceof Portail) && (((Portail) listeLieux.elementAt(i)).getDuree()<current_time))
				{
					listeLieux.remove(i);
					i--;
				}
			}
		}
		else
		{
			vainqueur=lastActive;
			gameState=STATE_GAMEOVER;
		}

	}

	public int getTime()
	{
		return current_time;
	}

	public Troll getCurrentTroll()
	{
		return currentTroll;
	}

	public String getPosition(int id)
	{
		if(gameState!=STATE_PLAYING)
			return "Error: The game is not started";
		Troll t=getTrollBySocketId(id);
		if(t==null)
			return "Error: unknown troll";
		return "Position: "+t.getPos();
	}

	public String getVue(int id)
	{
		if(gameState!=STATE_PLAYING)
			return "Error: The game is not started";
		Troll t=getTrollBySocketId(id);
		if(t==null)
			return "Error: unknown troll";
		if(t!=currentTroll && currentTroll.isActive())
			return "Error:  it is not your turn";
		String[] liste=t.getPos().split(" ");
		String s="";
		int x=Integer.parseInt(liste[0]);
		int y=Integer.parseInt(liste[1]);
		int n=Integer.parseInt(liste[2]);
		int v=t.getVue()+t.getBMVue()+t.getBMMVue();
		for(int i=0;i<trolls.size();i++)
		{
			if(trolls.elementAt(i).isVisibleFrom(x,y,n,v))
				s+="\n"+trolls.elementAt(i).getId()+" "+trolls.elementAt(i).getPos();
			else if(trolls.elementAt(i)==t)
				s+="\n"+trolls.elementAt(i).getId()+" "+trolls.elementAt(i).getPos();
		}
		return s.substring(1);
	}

	public String getInfosLieu(int id)
	{
		if(gameState!=STATE_PLAYING)
			return "Error: The game is not started";
		Troll t=getTrollBySocketId(id);
		if(t==null)
			return "Error: unknown troll";
		String[] liste=t.getPos().split(" ");
		int x=Integer.parseInt(liste[0]);
		int y=Integer.parseInt(liste[1]);
		int n=Integer.parseInt(liste[2]);
		for(int i=0;i<listeLieux.size();i++)
		{
			if(x==listeLieux.elementAt(i).getPosX() && y==listeLieux.elementAt(i).getPosY() && n==listeLieux.elementAt(i).getPosN())
			{
				return listeLieux.elementAt(i).getInfos();
			}
		}
		return "Vous n'�tes pas sur un lieu particulier";
	}

	public String getLieux(int id)
	{
		if(gameState!=STATE_PLAYING)
			return "Error: The game is not started";
		Troll t=getTrollBySocketId(id);
		if(t==null)
			return "Error: unknown troll";
		if(t!=currentTroll && currentTroll.isActive())
			return "Error:  it is not your turn";
		String[] liste=t.getPos().split(" ");
		String s="";
		int x=Integer.parseInt(liste[0]);
		int y=Integer.parseInt(liste[1]);
		int n=Integer.parseInt(liste[2]);
		int v=t.getVue()+t.getBMVue()+t.getBMMVue();
		for(int i=0;i<listeLieux.size();i++)
		{
			if(Math.abs(x-listeLieux.elementAt(i).getPosX())<=v && Math.abs(y-listeLieux.elementAt(i).getPosY())<=v && Math.abs(n-listeLieux.elementAt(i).getPosN())<=(v+1)/2)
			{
				if(listeLieux.elementAt(i) instanceof Piege && ((Piege) listeLieux.elementAt(i)).getCreateur()!=t  && !listeLieux.elementAt(i).getPos().equals(t.getPos()))
					continue;
				s+="\n"+listeLieux.elementAt(i).toString();
			}
		}
		if(s.equals(""))
			return "Aucun lieu";
		return s.substring(1);
	}

	public Vector<Troll> getListeTrolls()
	{
		return trolls;
	}

	public Vector<Lieu> getListeLieux()
	{
		return listeLieux;
	}

	public Lieu getLieuFromPosition(int x,int y,int n)
	{
		for(int i=0;i<listeLieux.size();i++)
		{
			Lieu l=listeLieux.elementAt(i);
			if(l.getPosX()==x && l.getPosY()==y && l.getPosN()==n)
				return l;
		}
		return null;
	}

	//dD+dD/2
	private String lowLevelAttaque(String nomA,Troll a,Troll d,int dA,int bmA,int dE,int bmE, int seuil, int dD,int dDC,int bmD, int dArN, int armure,int seuil_decamoufle,boolean peutEtreContreAttaque)
	{
		String s1,s2;
		int jetAttaque,jetEsquive, jetArN;
		s1="Vous avez attaqu� "+d.getName()+" ("+d.getId()+")\n";
		s2=a.getName()+" ("+a.getId()+") vous a attaqu�"+nomA+".\n";
		a.setInvisible(false);
		d.setInvisible(false);
		if(dA!=-1)
		{
			jetAttaque = Math.max(0, diceHelper.roll(dA, 6) + bmA);
			if (d.getFrenetique())
			{ jetEsquive=0; }
			else
			{
				jetEsquive = Math.max(0, diceHelper.roll(dE, 6) + bmE);
			}
			s1+="Votre Jet d'Attaque est de...................: "+jetAttaque+"\nLe Jet d'Esquive de votre adversaire est de...: "+jetEsquive+"\n";
			s2+="Son jet d'Attaque est de.....................: "+jetAttaque+"\nVotre jet d'Esquive est de...................: "+jetEsquive+"\n";
		}
		else
		{
			jetEsquive=-1;
			jetAttaque=5000;
		}
		if(jetEsquive>=2*jetAttaque)
		{//Esquive parfaite
			s1+="Vous avez donc RAT� votre adversaire grace � une esquive parfaite.\nIl ne sera donc pas fragilis� lors des prochaines esquives.";
			s2+="Votre adversaire vous a donc RAT� grace � une esquive parfaite.\nVous ne serez donc pas fragilis� lors des prochaines esquives.";
			events.add(current_time+" "+a.getName()+" ("+a.getId()+") a frapp�"+nomA+" "+d.getName()+" ("+d.getId()+") qui a esquiv� parfaitement l'attaque");
		}
		else if(jetEsquive>=jetAttaque)
		{
			s1+="Vous avez donc RAT� votre adversaire.\nIl sera de plus fragilis� lors des prochaines esquives.";
			s2+="Votre adversaire vous a donc RAT�.\nVous serez, cependant, fragilis� lors des prochaines esquives. ";
			events.add(current_time+" "+a.getName()+" ("+a.getId()+") a frapp�"+nomA+" "+d.getName()+" ("+d.getId()+") qui a esquiv� l'attaque");
			d.getTouch();
		}
		else
		{
			int jetDegat;
			String s="";
			if(jetEsquive==-1 && jetAttaque==5000)
			{
				jetDegat = diceHelper.roll(dD, 3);
				s1+="Votre Attaque est automatiquement r�ussie.\n";
				s2+="Son Attaque est automatiquement r�ussie. \n";
			}
			else
			{
				if(d.getNbParade()>0)
				{
					int jetParade = diceHelper.roll(d.getAttaque() / 2, 3) + d.getBMAttaque();
					s1+="Une parade avait �t� programm�e :\nLe Jet de parade de votre adversaire est de...:"+jetParade+"\n";
					s2+="Une parade avait �t� programm�e :\nVotre Jet de parade est de....................:"+jetParade+"\n";
					if(jetParade>jetAttaque-jetEsquive)
					{
						s1+="Votre adversaire a PAR� le coup.\nIl sera cependant fragilis� lors des prochaines esquives.";
						s2+="Voous avez r�ussi � PARER le coup.\nVous serez, cependant, fragilis� lors des prochaines esquives. ";
						events.add(current_time+" "+a.getName()+" ("+a.getId()+") a frapp� "+d.getName()+" ("+d.getId()+") qui a esquiv� l'attaque");
						d.getTouch();
						d.getParade();
						d.getInbox().add(s2);
						if(a.getCamouflage() && seuil_decamoufle!=0)
						{
							int jetCa = diceHelper.roll(1, 100);
							if(jetCa<=seuil_decamoufle)
							{
								s1+="\nDe plus votre camouflage est rest� actif";
							}
							else
							{
								s1+="\nDe plus votre camouflage a �t� annul�";
								a.setCamouflage(false);
							}
						}
						return s1;
					}
				}
				if(2*jetEsquive>=jetAttaque)
				{
					jetDegat = diceHelper.roll(dD, 3);
					s1+="Vous avez donc TOUCH� votre adversaire.\n";
					s2+="Vous avez donc �t� TOUCH�.\n";
				}
				else
				{
					jetDegat = diceHelper.roll(dDC, 3);
					s1+="Vous avez donc TOUCH� votre adversaire par un coup critique.\n";
					s2+="Vous avez donc �t� TOUCH� par un coup critique.\n";
					s=" d'un coup critique";
				}
			}
			jetDegat+=bmD;
			jetDegat=Math.max(0,jetDegat);
			if(seuil==0)
			{
				int degats=0;
				if(jetDegat>0) {
					jetArN = Math.max(0, diceHelper.roll(dArN, 3));
					degats=Math.max(jetDegat-jetArN-armure,1);
					d.blesse(degats);
				}
				a.addDeg(degats);
				s1+="Vous lui avez inflig� "+jetDegat+" points de d�g�ts.";
				s2+="Il vous a inflig� "+jetDegat+" points de d�g�ts. ";
				if(armure!=0 && degats!=0)
				{
					s1+="\nSon Armure le prot�ge et il ne perdra que "+degats+" points de vie.";
					s2+="\nVotre Armure vous prot�ge et vous ne perdrez que "+degats+" points de vie.";
				}
			}
			else
			{
				int jetSR = diceHelper.roll(1, 100);
				if(jetSR<=seuil)
				{
					String s3="\nSeuil de R�sistance de la Cible.....: "+seuil+" %\nJet de R�sistance...........................: "+jetSR+"\nLe sort a donc un EFFET REDUIT de moiti�.";
					s1+=s3;
					s2+=s3;
					jetDegat=jetDegat/2;
				}
				else
				{
					String s3="\nSeuil de R�sistance de la Cible.....: "+seuil+" %\nJet de R�sistance...........................: "+jetSR+"\nLa Cible subit donc pleinement l'effet du sortil�ge.";
					s1+=s3;
					s2+=s3;
				}
				int degats=0;
				if(jetDegat>0)
					degats=Math.max(jetDegat-armure,1);
				d.blesse(degats);
				a.addDeg(degats);
				s1+="\nVous lui avez inflig� "+jetDegat+" points de d�g�ts.";
				s2+="\nIl vous a inflig� "+jetDegat+" points de d�g�ts. ";
				if(armure!=0 && degats!=0)
				{
					s1+="\nSon Armure le prot�ge et il ne perdra que "+degats+" points de vie.";
					s2+="\nVotre Armure vous prot�ge et vous ne perdrez que "+degats+" points de vie.";
				}
			}
			if(!d.isDead() && (jetEsquive!=-1 || jetAttaque!=5000))
			{
				s1+="\nIl sera, de plus, fragilis� lors des prochaines esquives.";
				s2+="\nVous serez, de plus, fragilis� lors des prochaines esquives.";
				d.getTouch();
				events.add(current_time+" "+a.getName()+" ("+a.getId()+") a frapp�"+nomA+""+s+" "+d.getName()+" ("+d.getId()+") qui a survecu");
			}
			else if(d.isDead())
			{
				s1+="\nVous l'avez tu� !!";
				s2+="\nVous �tes mort mort et remort !!";
				a.addKill();
				events.add(current_time+" "+a.getName()+" ("+a.getId()+") a tu�"+nomA+""+s+" "+d.getName()+" ("+d.getId()+")");
			}
			else if(jetEsquive==-1 && jetAttaque==5000)
			{
				events.add(current_time+" "+a.getName()+" ("+a.getId()+") a frapp�"+nomA+""+s+" "+d.getName()+" ("+d.getId()+") qui a survecu");
			}


		}
		if(peutEtreContreAttaque && !d.isDead() && d.getNbCA()>0)
		{
			d.setNbCA(d.getNbCA()-1);
			s2+="\nVous aviez programm� une contre-attaque\n"+lowLevelAttaque(" avec une comp�tence", d , a , (d.getAttaque())/2,  d.getBMAttaque()/2, a.getEsquive() , a.getBMEsquive(), 0 , d.getDegat(),(3*d.getDegat())/2,d.getBMDegat(),a.getArN(), a.getArmurePhy()+a.getArmureMag(),100,false);
			s1+="\nUne contre-attaque avait �t� programm�e\n"+a.getInbox().remove(a.getInbox().size()-1);
		}
		d.getInbox().add(s2);
		if(a.getCamouflage() && seuil_decamoufle!=0)
		{
			int jetCa = diceHelper.roll(1, 100);
			if(jetCa>seuil_decamoufle)
			{
				s1+="\nDe plus votre camouflage est rest� actif";
			}
			else
			{
				s1+="\nDe plus votre camouflage a �t� annul�";
				a.setCamouflage(false);
			}
		}
		return s1;
	}

	private String lowLevelPotionParchemin(Equipement e,Troll t, int distance) {

		String typeEquipement;
		String s,s2,s3,returnstring;

		s = "\nVous avez �t� utilis� ";
		s2 = currentTroll.getName() + "(" + currentTroll.getId() + ") a utilis� ";
		s3 = "Ses effets sont :\n";
		returnstring="";
		int duree = 3; // dur�e par d�faut, � v�rifier

		if (e.getType() == Equipement.POTION) {
			typeEquipement = "une potion";
			duree = 2 + diceHelper.roll(1, 3);
		}
		else if (e.getType() == Equipement.PARCHEMIN) {typeEquipement = "un Parchemin ";}
		else {return "Error: l'objet n'est ni une potion ni un parchemin";}

		if (Math.max(Math.abs(t.getPosX()-currentTroll.getPosX()),Math.abs(t.getPosY()-currentTroll.getPosY())) > distance || t.getPosN() != currentTroll.getPosN()) {
			return "Error: cible hors de port�e";
		}

		BM bm;

		if (!e.isZone()) {
			s = "\nVous avez �t� utilis� " + typeEquipement + " sur " + t.getName() + "(" + t.getId() + "). ";
			s2 = currentTroll.getName() + "(" + currentTroll.getId() + ") a utilis� " + typeEquipement + " sur vous. ";
			s3 = "Ses effets sont :\n";
			int pv=0;
			String dead1="";
			String dead2="";
			if(e.getPV()>0)
			{
				pv = diceHelper.roll(e.getPV(), 3);
				t.addPVReg(Math.min(pv,t.getPVTotaux()-t.getPV()));
				t.setPV(Math.min(t.getPVTotaux(),t.getPV()+pv));
			}
			else if(e.getPV()<0)
			{
				pv = diceHelper.roll(e.getPV(), 3);
				t.blesse(-pv);
				currentTroll.addDeg(-pv);
				if(t.isDead() && t!=currentTroll)
				{
					dead1="\nVous l'avez tu�, bravo";
					dead2="\nCel� vous a achev�";
					currentTroll.addKill();
				}
				else if(t.isDead() && t==currentTroll)
					dead1="\nCel� vous a achev�";
			}
			if(e.getBMAttaque()!=0 || e.getBMEsquive()!=0 || e.getBMDegat()!=0 || e.getBMDLA()!=0 || e.getBMRegeneration()!=0 || e.getBMVue() !=0 || e.getBMArmurePhysique()!=0
					|| e.getBMArmureMagique()!=0 || e.getBMMM()!=0 || e.getBMRM()!=0)
			{
				bm = new BM(e.getName(), diceHelper.roll(e.getBMAttaque(), 3), diceHelper.roll(e.getBMEsquive(), 3),
						e.getBMDegat(), e.getBMDLA(), e.getBMRegeneration(), e.getBMVue(), 0, e.getBMArmurePhysique(),
						e.getBMArmureMagique(), e.getBMMM(), e.getBMRM(), false, duree);
				BM realBM = t.addBM(bm);
				events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une potion sur "+t.getName()+" ("+t.getId()+")");
				s3 += realBM.getName() + " : " + formate("PV", pv) + formate("Att", realBM.getBMAttaque())
						+ formate("Att Mag", realBM.getBMMAttaque()) + formate("Esq", realBM.getBMEsquive())
						+ formate("Deg", realBM.getBMDegat()) + formate("Deg Mag", realBM.getBMMDegat())
						+ formate("DLA", realBM.getBMDLA()) + formate("Reg", realBM.getBMRegeneration())
						+ formate("Vue", realBM.getBMVue())
						+ formate("Arm", realBM.getBMArmureMagique() + realBM.getBMArmurePhysique())
						+ formate("MM", bm.getBMMM()) + formate("RM", realBM.getBMRM()) + "Dur�e " + duree + " tour(s)";
				s3 += "\nLes effets se feront ressentir pendant " + duree + " tours.";
			}
			else
			{
				events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une potion sur "+t.getName()+" ("+t.getId()+")");
				s3 += e.getName() + " : " + formate("PV",pv);
			}
			if(t!=currentTroll)
				t.getInbox().add(s2+s3+dead2);
			s+=s3+dead1;
			returnstring+=s;
		}
		else {
			for(int i=0;i<trolls.size();i++)
			{
				Troll t2=trolls.elementAt(i);
				if(!t2.getPos().equals(t.getPos()) ||  t.isDead())
					continue;
				s = "\nVous avez �t� utilis� " + typeEquipement + " sur " + t2.getName() + "(" + t2.getId() + "). ";
				s2 = currentTroll.getName() + "(" + currentTroll.getId() + ") a utilis� " + typeEquipement + " sur vous. ";
				s3 = "\nSes effets sont :\n";

				int pv=0;
				String dead1="";
				String dead2="";
				if(e.getPV()>0)
				{
					pv = diceHelper.roll(e.getPV(), 3);
					t2.addPVReg(Math.min(pv,t2.getPVTotaux()-t2.getPV()));
					t2.setPV(Math.min(t2.getPVTotaux(),t2.getPV()+pv));
				}
				else if(e.getPV()<0)
				{
					pv = diceHelper.roll(e.getPV(), 3);
					t2.blesse(-pv);
					currentTroll.addDeg(-pv);
					t2.setInvisible(false);
					if(t2.isDead() && t2!=currentTroll)
					{
						dead1="\nVous l'avez tu�, bravo";
						dead2="\nCel� vous a achev�";
						currentTroll.addKill();
					}
					else if(t2.isDead() && t2==currentTroll)
						dead1="\nCel� vous a achev�";
				}

				if(e.getBMAttaque()!=0 || e.getBMEsquive()!=0 || e.getBMDegat()!=0 || e.getBMDLA()!=0 || e.getBMRegeneration()!=0 || e.getBMVue() !=0 || e.getBMArmurePhysique()!=0 || e.getBMArmureMagique()!=0 || e.getBMMM()!=0 || e.getBMRM()!=0)
				{
					bm = new BM(e.getName(), diceHelper.roll(e.getBMAttaque(), 3), diceHelper.roll(e.getBMEsquive(), 3),
							e.getBMDegat(), e.getBMDLA(), e.getBMRegeneration(), e.getBMVue(), 0,
							e.getBMArmurePhysique(), e.getBMArmureMagique(), e.getBMMM(), e.getBMRM(), false, duree);
					BM realBM = t2.addBM(bm);
					events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une potion sur "+t2.getName()+" ("+t2.getId()+")");
					s3 += realBM.getName() + " : " + formate("PV", pv) + formate("Att", realBM.getBMAttaque())
							+ formate("Esq", realBM.getBMEsquive()) + formate("Deg", realBM.getBMDegat())
							+ formate("DLA", realBM.getBMDLA()) + formate("Reg", realBM.getBMRegeneration())
							+ formate("Vue", realBM.getBMVue())
							+ formate("Arm", realBM.getBMArmureMagique() + bm.getBMArmurePhysique())
							+ formate("MM", realBM.getBMMM()) + formate("RM", realBM.getBMRM()) + "Dur�e " + duree
							+ " tour(s)";
					s3 += "\nLes effets se feront ressentir pendant " + duree + " tours.\n";
				}
				else
				{
					events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une potion sur "+t.getName()+" ("+t.getId()+")");
					s3 += e.getName() + " : " + formate("PV",pv);
				}
				if(t2!=currentTroll)
					t2.getInbox().add(s2+s3+dead2);
				s+=s3+dead1;
				returnstring+=s;
			}
		}
		return returnstring;
	}


	/*********************************************************

                  Les comp�tences

	 **********************************************************/


	public String accelerationDuMetabolisme(int pv_sacrifies)
	{
		int f=currentTroll.getFatigue();
		int decale=0;
		if(f<=4)
			decale=30*pv_sacrifies;
		else
			decale=(120/((f/10+1)*f))*pv_sacrifies;
		if(pv_sacrifies>=currentTroll.getPV() || pv_sacrifies<=0 || decale>currentTroll.getDureeTourTotale())
			return  "Vous ne pouvez pas sacrifier autant de PV";
		Object[] lo=competence(currentTroll, Troll.COMP_ACCELERATION_DU_METABOLISME,2);
		if(((Integer) lo[0]) > 0)
		{
			currentTroll.setPV(currentTroll.getPV()-pv_sacrifies);
			currentTroll.setNouveauTour(currentTroll.getNouveauTour()-decale);
			currentTroll.setFatigue(f+pv_sacrifies);
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une comp�tence");
			return lo[1].toString()+"\nVous avez sacrifi� "+pv_sacrifies+" Points de vie\nVotre nouvelle Date limite d'action est le "+Troll.hour2string(currentTroll.getNouveauTour());
		}
		else return lo[1].toString();
	}

	public String attaquePrecise(Troll t)
	{
		if(!t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),0))
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas de flagellation !";
		}
		Object[] lo=competence(currentTroll, Troll.COMP_AP,4);
		String s= lo[1].toString();
		if(((Integer) lo[0]) > 0)
			return s + "\n" + lowLevelAttaque(" avec une comp�tence", currentTroll , t , currentTroll.getAttaque() + Math.min(((Integer) lo[0])*3, currentTroll.getAttaque()/2),  currentTroll.getBMAttaque(), t.getEsquive() , t.getBMEsquive(), 0 , currentTroll.getDegat(),(3*currentTroll.getDegat())/2,currentTroll.getBMDegat(),t.getArN(),t.getArmurePhy()+t.getArmureMag(),100,true);
		else return s;
	}

	public String botteSecrete(Troll t)
	{
		if(!t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),0))
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas de flagellation !";
		}
		Object[] lo=competence(currentTroll, Troll.COMP_BOTTE_SECRETE,2);
		String s= lo[1].toString();
		if(((Integer) lo[0]) > 0)
			return s + "\n" + lowLevelAttaque(" avec une comp�tence", currentTroll , t , currentTroll.getAttaque()/2,  currentTroll.getBMAttaque()/2, t.getEsquive() , t.getBMEsquive(), 0 , currentTroll.getAttaque()/2,(3*(currentTroll.getAttaque()/2))/2,currentTroll.getBMDegat()/2,t.getArN(), (t.getArmurePhy()+t.getArmureMag())/2,100,true);
		else return s;
	}

	public String balayage(Troll t)
	{
		if(!t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),0))
		{
			return "Error: Cible hors de port�e";
		}
		if(t.isATerre())
		{
			return "Error: Cible d�j� � terre";
		}
		if(currentTroll==t)
		{
			return "Error: Pas de flagellation !";
		}
		Object[] lo=competence(currentTroll, Troll.COMP_BALAYAGE,2);
		String s= lo[1].toString();
		if(((Integer) lo[0]) > 0)
		{
			int stabilite = currentTroll.getAttaque();
			int jetStabilite = diceHelper.roll(stabilite, 6)+currentTroll.getBMAttaque()+currentTroll.getBMMAttaque();
			int agilite = 2 * (t.getRegeneration() + t.getEsquive()) / 3;
			int jetAgilite = diceHelper.roll(agilite, 6)+t.getBMEsquive()+t.getBMMEsquive();

			s+= "\nVotre jet de d�stabilisation est de : " + jetStabilite + ".\n";
			s+= "\nLe jet de stabilit� de votre adversaire est de : "+ jetAgilite +".\n";

			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une comp�tence");

			//Balayage critique : on d�clenche les pi�ges
			String targetMsg = "Vous avez �t� mis � terre par " + currentTroll.getName() + " ( " + currentTroll.getId() + " ) ";
			if(jetStabilite >= 2* jetAgilite) {
				t.metATerre();
				s += "\nVous avez donc MIS A TERRE votre adversaire par un coup critique.\n";
				t.getInbox().add(targetMsg);
				Lieu l=getLieuFromPosition(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN());
				if(l!= null && l instanceof Piege)
					s+=appliquePiege((Piege) l);
			}
			//Si le balayage reussi, on met � terre le troll
			else if(jetStabilite > jetAgilite) {
				t.metATerre();
				t.getInbox().add(targetMsg);
				s+="\nVous avez donc MIS A TERRE votre adversaire.\n";
			}
			else {
				s+="\nLe troll "+t.getId()+" a esquiv� votre balayage.";
			}
		}
		
		return s;
	}

	public String camouflage()
	{
		if(currentTroll.getCamouflage())
			return  "Vous �tes d�ja camoufl�";
		Object[] lo=competence(currentTroll, Troll.COMP_CAMOUFLAGE,2);
		String s= lo[1].toString();
		if(((Integer) lo[0]) > 0)
		{
			currentTroll.setCamouflage(true);
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une comp�tence");
			return s+"\nVous vous �tes camoufl�";
		}
		else return s;
	}

	public String charger(Troll t)
	{
		int porteeCharge=0;
		int max=0;
		int add=4;
		while((currentTroll.getPV()/10)+currentTroll.getRegeneration()>max)
		{
			max+=add;
			add++;
			porteeCharge++;
		}
		if(currentTroll.getPos().compareTo(t.getPos())==0) {
			return "Error: Impossible de charger sur la case";
		}
		if(currentTroll.getPosN()!=t.getPosN() || !t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),porteeCharge) ||!t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),currentTroll.getVue()+currentTroll.getBMVue()+currentTroll.getBMMVue()))
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas de flagellation !";
		}
		int nbpa=4;
		if(currentTroll.isGlue())
			nbpa=6;
		Object[] lo=competence(currentTroll, Troll.COMP_CHARGER,nbpa);
		String s= lo[1].toString();
		if(((Integer) lo[0]) > 0)
		{
			currentTroll.setPos(t.getPosX(),t.getPosY(),t.getPosN());
			// Faudrait regarder s'il y a un piege !
			s += "\n" + lowLevelAttaque(" avec une comp�tence", currentTroll , t , currentTroll.getAttaque(),  currentTroll.getBMAttaque(), t.getEsquive() , t.getBMEsquive(), 0 , currentTroll.getDegat(),(3*currentTroll.getDegat())/2,currentTroll.getBMDegat(),t.getArN(), t.getArmurePhy()+t.getArmureMag(),100,true);
			s += "\nVotre position est X = " + currentTroll.getPosX() + ", Y = " + currentTroll.getPosY() + ", N = " + currentTroll.getPosN();
			Lieu l=getLieuFromPosition(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN());
			if(l!= null && l instanceof Piege)
				s+=appliquePiege((Piege) l);
			return s;
		}
		else return s;
	}

	public String construireUnPiege()
	{
		if(getLieuFromPosition(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN())!=null)
			return "Error: Il y a d�ja un lieu sur la case";
		Object[] lo=competence(currentTroll, Troll.COMP_PIEGE,4);
		String s= lo[1].toString();
		if(((Integer) lo[0]) > 0)
		{
			listeLieux.add(new Piege(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),currentTroll,(currentTroll.getVue()+currentTroll.getEsquiveTotale())/2));
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une comp�tence");
			return s + "\nVous avez pos� un pi�ge en X = " + currentTroll.getPosX() + ", Y = " + currentTroll.getPosY() + ", N = " + currentTroll.getPosN();
		}
		else return s;
	}

	public String contreAttaque()
	{
		Object[] lo=competence(currentTroll, Troll.COMP_CONTREATTAQUE,2);
		String s= lo[1].toString();
		if(((Integer) lo[0]) > 0)
		{
			currentTroll.setNbCA(currentTroll.getNbCA()+1);
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une comp�tence");
			return s+"\nVous avez programm� une contre-attaque";
		}
		else return s;
	}

	public String coupDeButoir(Troll t)
	{
		if(!t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),0))
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas de flagellation !";
		}
		Object[] lo=competence(currentTroll, Troll.COMP_CDB,4);
		String s= lo[1].toString();
		if(((Integer) lo[0]) > 0)
			return s + "\n" + lowLevelAttaque(" avec une comp�tence", currentTroll , t , currentTroll.getAttaque(),  currentTroll.getBMAttaque(), t.getEsquive() , t.getBMEsquive(), 0 , currentTroll.getDegat() + Math.min(((Integer) lo[0])*3, currentTroll.getDegat()/2),(3*currentTroll.getDegat())/2 + Math.min(((Integer) lo[0])*3, currentTroll.getDegat()/2),currentTroll.getBMDegat(),t.getArN(),t.getArmurePhy()+t.getArmureMag(),100,true);
		else return s;
	}

	public String deplacementEclair(int x,int y,int n)
	{
		if(!(Math.abs(x)<=1 && Math.abs(y)<=1 && Math.abs(n)<=1))
			return "Error: Bad data formating in deplace instruction";
		if(x==y && x==n && x==0)
			return "Error: You have to move";
		int nbpa=2;
		int nbidentique=0;
		for(int i=0;i<trolls.size();i++)
			if(trolls.elementAt(i).isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),0))
				nbidentique++;
		if(nbidentique>1)
			nbpa++;
		if(n!=0)
			nbpa++;

		if(currentTroll.isGlue())
			nbpa=nbpa*2;
		nbpa=Math.min(6,nbpa);
		nbpa--;
		int px=currentTroll.getPosX();
		int py=currentTroll.getPosY();
		int pn=currentTroll.getPosN();
		if(px+x<0 || px+x>=sizeArena || py+y<0 || py+y>=sizeArena || pn+n<-(sizeArena+1)/2-1 || pn+n>=0)
			return "Error: Vous ne pouvez sortir de l'ar�ne";
		if(nbpa>currentTroll.getPA())
			return "Error: Vous avez besoin de "+nbpa+" PA pour r�aliser ce mouvement";
		Object[] lo=competence(currentTroll, Troll.COMP_DE,nbpa);
		String s= lo[1].toString();
		if(((Integer) lo[0]) > 0)
		{
			s+="\n"+deplace(x,y,n,true);
			return s;
		}
		return s;
	}

	public String frenesie(Troll t)
	{
		if(!t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),0))
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas de flagellation !";
		}
		Object[] lo=competence(currentTroll, Troll.COMP_FRENESIE,6);
		String s= lo[1].toString();
		if(((Integer) lo[0]) > 0) {
			s += "\n" + lowLevelAttaque(" avec une comp�tence", currentTroll , t , currentTroll.getAttaque(),  currentTroll.getBMAttaque(), t.getEsquive() , t.getBMEsquive(), 0 , currentTroll.getDegat(),(3*currentTroll.getDegat())/2,currentTroll.getBMDegat(),t.getArN(),t.getArmurePhy()+t.getArmureMag(),100,true);
			if (!t.isDead()) {
				s += "\n" + lowLevelAttaque(" avec une comp�tence", currentTroll , t , currentTroll.getAttaque(),  currentTroll.getBMAttaque(), t.getEsquive() , t.getBMEsquive(), 0 , currentTroll.getDegat(),(3*currentTroll.getDegat())/2,currentTroll.getBMDegat(),t.getArN(),t.getArmurePhy()+t.getArmureMag(),100,true);
			}
			currentTroll.setFrenetique(true);
		}
		return s;
	}

	public String lancerDePotion(Troll t, Equipement e)
	{
		int portee=2 + (currentTroll.getVue() + currentTroll.getBMVue()+currentTroll.getBMMVue()) / 5;
		int distance = Math.max(Math.abs(currentTroll.getPosX()-t.getPosX()),Math.abs(currentTroll.getPosY()-t.getPosY()));

		if (e.getType() != Equipement.POTION) { return "Error : ceci n'est pas une potion";}

		if(currentTroll.getPosN()!=t.getPosN() || distance > portee)
		{
			return "Error: Cible hors de port�e";
		}

		Object[] lo=competence(currentTroll, Troll.COMP_LDP,2);
		String s= lo[1].toString();
		if(((Integer) lo[0]) > 0) {

			int bonustoucher = Math.min(10 * (1 - distance) + currentTroll.getVue() + currentTroll.getBMVue(),10);

			if (diceHelper.roll(1, 100) < currentTroll.getReussiteComp(Troll.COMP_LDP, 1)
					+ currentTroll.getConcentration() + bonustoucher)
			{
				s += lowLevelPotionParchemin(e,t,portee);
				currentTroll.removeEquipement(e);
				events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une comp�tence sur "+t.getName()+" ("+t.getId()+")");
			}
			else
			{
				s+="\nD�sol� mais vous avez rat� votre cible.\nLa potion est quand m�me d�truite";
				events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une comp�tence");
				currentTroll.removeEquipement(e);
			}
			if(currentTroll.getCamouflage())
			{
				s+="\nDe plus votre camouflage a �t� annul�";
				currentTroll.setCamouflage(false);
			}
		}
		return s;
	}

	public String parer()
	{
		Object[] lo=competence(currentTroll, Troll.COMP_PARER,2);
		String s= lo[1].toString();
		if(((Integer) lo[0]) > 0)
		{
			currentTroll.setNbParade(currentTroll.getNbParade()+(Integer) lo[0]);
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une comp�tence");
			return s+"\nVous avez programm� une parade";
		}
		else return s;
	}


	public String pistage(Troll t) {
		Object[] lo=competence(currentTroll,Troll.COMP_PISTAGE,1);
		String s= lo[1].toString();
		if(((Integer) lo[0]) > 0)
		{
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une comp�tence");
			if (Math.abs(currentTroll.getPosX()-t.getPosX()) <= Math.max(0,(currentTroll.getVue()+currentTroll.getBMVue()+currentTroll.getBMMVue())*2) && Math.abs((currentTroll.getPosY()-t.getPosY())) <= Math.max(0,(currentTroll.getVue()+currentTroll.getBMVue())*2) && Math.abs(currentTroll.getPosN()-t.getPosN()) <= Math.max(0,currentTroll.getVue()+currentTroll.getBMVue())) {
				s += "\nVous avez retrouv� la trace de votre cible\nElle se trouve:  ";
				if(currentTroll.getPos().compareTo(t.getPos())==0) {
					return s + "dans la m�me zone que vous";
				}
				if (currentTroll.getPosX() < t.getPosX())
					s += "plus vers l'Ostikhan (X+)\n"; //+ Character.toString((char)26);
				if (currentTroll.getPosX() > t.getPosX())
					s += "plus vers l'Estikhan (X-)\n"; //+ Character.toString((char)26);
				if (currentTroll.getPosY() < t.getPosY())
					s += " plus vers le Nordikhan (Y+)\n"; //+ Character.toString((char)26);
				if (currentTroll.getPosY() > t.getPosY())
					s += " plus vers le Sudikhan (Y-)\n"; //+ Character.toString((char)26);
				if (currentTroll.getPosN() < t.getPosN())
					s += " et plus haut\n";
				if (currentTroll.getPosN() > t.getPosN())
					s += " et plus bas\n";
				return s.substring(0,s.length()-1) + " que vous";
			}
			else {
				return "Cible Hors de port�e";
			}
		}
		else return s;
	}

	public String regenerationAccrue()
	{
		if(currentTroll.getPV()==currentTroll.getPVTotaux())
			return  "Vous avez tous vos points de vie";
		Object[] lo=competence(currentTroll, Troll.COMP_REGENERATION_ACCRUE,2);
		String s= lo[1].toString();
		if(((Integer) lo[0]) > 0)
		{
			int jet = diceHelper.roll(currentTroll.getPVTotaux() / 20, 3);
			currentTroll.addPVReg(Math.min(jet,currentTroll.getPVTotaux()-currentTroll.getPV()));
			currentTroll.setPV(Math.min(currentTroll.getPV()+jet,currentTroll.getPVTotaux()));
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une comp�tence");
			return s+"\nVous avez r�g�n�r� de "+jet+" points de vie.\nVous en avez maintenant "+currentTroll.getPV();
		}
		else return s;
	}

	/*********************************************************

                  Les sortil�ges

	 **********************************************************/

	public String analyseAnatomique(Troll t)
	{
		int vue=currentTroll.getVue()+currentTroll.getBMVue()+currentTroll.getBMMVue();
		if(!t.isVisibleFrom(currentTroll.getPosX(), currentTroll.getPosY(), currentTroll.getPosN(), vue/2))
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: pas d'auto-analyse !";
		}
		String s=sortilege(currentTroll, Troll.SORT_AA,1);
		if(s.indexOf("R�USSI")!=-1)
		{
			s += "\nLes caract�ristiques du Troll " + t.getName() + " (" + t.getId() + ") sont\n";
			s += "Attaque : " + rendflou(t.getAttaque(),1,3,20) + "\n";
			s += "Degats : " + rendflou(t.getDegat(),1,3,20) + "\n";
			s += "Esquive : " + rendflou(t.getEsquiveTotale(),1,3,20) + "\n";
			s += "Vie : " + rendflou(t.getPVTotaux(),10,30,200) + "\n";
			s += "Blessure : " + Math.min(95,100-((10*t.getPV())/t.getPVTotaux())*10)+"%\n";
			s += "R�g�n�ration : " + rendflou(t.getRegeneration(),1,1,10) + "\n";
			s += "Armure Naturelle : " + rendflou(t.getArN(),1,1,10) + "\n";
			s += "Vue : " + rendflou(t.getVue(),1,3,20) + "\n";
			s += "Armure : " + rendflou(t.getArmurePhy(),1,3,20);
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge sur "+t.getName()+" ("+t.getId()+")");
		}
		return s;
	}

	public String armureEtheree()
	{
		String s=sortilege(currentTroll, Troll.SORT_AE,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			BM bm=new BM("Armure �th�r�e", 0,0,0,0,0,0,0,0,currentTroll.getRegeneration(),0,0,false,2);
			BM realBM = currentTroll.addBM(bm);
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge");
			return s + "\nVous avez gagn� " + realBM.getBMArmureMagique() + " point d'armure pendant 2 tour(s)";

		}
		else return s;
	}

	public String augmentationDeLAttaque()
	{
		String s=sortilege(currentTroll, Troll.SORT_ADA,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			int bonus=1+((currentTroll.getAttaque()-3)/2);
			BM bm;
			bm=new BM("Augmentation de l'attaque", bonus,0,0,0,0,0,0,0,0,0,0,false,2);
			BM realBM = currentTroll.addBM(bm);
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge");
			return s + "\nVous avez gagn� " + realBM.getBMAttaque() + " points d'attaque pendant 2 tour(s)";

		}
		else return s;
	}

	public String augmentationDeLEsquive()
	{
		String s=sortilege(currentTroll, Troll.SORT_ADE,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			BM bm=new BM("Augmentation de l'esquive",0,1+((currentTroll.getEsquiveTotale()-3)/2),0,0,0,0,0,0,0,0,0,false,2);
			BM realBM = currentTroll.addBM(bm);
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge");
			return s + "\nVous avez gagn� " + realBM.getBMEsquive() + " points d'esquive pendant 2 tour(s)";

		}
		else return s;
	}

	public String augmentationDesDegats()
	{
		String s=sortilege(currentTroll, Troll.SORT_ADD,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			int bonus=1+((currentTroll.getDegat()-3)/2);
			BM bm;
			bm=new BM("Augmentation des d�gats",0,0,bonus,0,0,0,0,0,0,0,0,false,2);
			BM realBM = currentTroll.addBM(bm);
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge");
			return s + "\nVous avez gagn� " + realBM.getBMDegat() + " points de d�gats pendant 2 tour(s)";

		}
		else return s;
	}

	public String bulleDAntiMagie()
	{
		String s=sortilege(currentTroll, Troll.SORT_BAM,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			currentTroll.addBM(new BM("Bulle d'anti-magie",0,0,0,0,0,0,0,0,0,0,100,false,2));
			currentTroll
			.addBM(new BM("Bulle d'anti-magie (contre-coup)", 0, 0, 0, 0, 0, 0, 0, 0, 0, -100, 0, false, 4));
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge");
			return s+"\nLes effets du sortil�ge se fera donc sentir pendant 2 et 4 tours";

		}
		else return s;
	}

	public String bulleMagique()
	{
		String s=sortilege(currentTroll, Troll.SORT_BUM,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			currentTroll.addBM(new BM("Bulle magique",0,0,0,0,0,0,0,0,0,100,0,false,2));
			currentTroll.addBM(new BM("Bulle magique (contre-coup)", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -100, false, 4));
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge");
			return s+"\nLes effets du sortil�ge se fera donc sentir pendant 2 et 4 tours";

		}
		else return s;
	}

	public String explosion()
	{
		//    	if(getLieuFromPosition(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN())!=null)
		//		return "Error: Il y a un lieu sur la case";
		String s=sortilege(currentTroll, Troll.SORT_EXPLOSION,6);
		if(s.indexOf("R�USSI")!=-1)
		{
			int deg=1 + ((currentTroll.getDegat()+(currentTroll.getPVTotaux()/10))/2);
			boolean b=false;
			for(int i=0;i<trolls.size();i++)
			{
				Troll t=trolls.elementAt(i);
				if(t!=currentTroll && t.getPos().equals(currentTroll.getPos()) && !t.isDead())
				{
					s += "\n" + lowLevelAttaque(" avec un sortil�ge", currentTroll , t , -1,  0, 0 , 0, calculeSeuil(currentTroll.getMM(),t.getRM()) , deg,0,0,0,0,100,false);
					b=true;
				}
			}
			if(!b)
			{
				s+="\nD�sol�, il n'y a personne sur la case";
				events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge");
			}
		}
		return s;
	}

	public String faiblessePassag�re(Troll t)
	{
		if(t.getPosN() != currentTroll.getPosN() || Math.max(Math.abs(t.getPosX()-currentTroll.getPosX()),Math.abs(t.getPosY()-currentTroll.getPosY()))>1
				|| !t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),currentTroll.getVue()+currentTroll.getBMVue()+currentTroll.getBMMVue()))
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas de flagellation !";
		}
		String s=sortilege(currentTroll,Troll.SORT_FP,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			int malus=((currentTroll.getPV()-30)/10 + currentTroll.getDegat())/2;
			int seuil=calculeSeuil(currentTroll.getMM(),t.getRM());
			String s1=s+"\nVous avez tent� d'utiliser Faiblesse Passag�re sur le troll "+t.getName()+" ("+t.getId()+")";
			String s2=currentTroll.getName()+" ("+currentTroll.getId()+") a tent� d'utiliser Faiblesse Passag�re  sur vous";
			int jet = diceHelper.roll(1, 100);
			BM bm;
			if(jet<=seuil) {
				int bonus=Math.max(1,malus/2);
				String s3="\nSeuil de R�sistance de la Cible.....: "+seuil+" %\nJet de R�sistance...........................: "+jet+"\nLe sort a donc un EFFET REDUIT.";
				bm = new BM("Faiblesse Passag�re ",0,0,0,-bonus,0,0,0,0,0,0,0,0,0,false,1);
				BM realBM = t.addBM(bm);
				s1 += s3 + "\nLa cible aura donc un malus de " + realBM.getBMDegat() + " points de d�gats 1 tour.";
				s2 += s3 + "\nVous avez donc un malus de " + realBM.getBMDegat() + " points de d�gats pendant 1 tour.";
			}
			else {
				int bonus=Math.max(1,malus);
				String s3="\nSeuil de R�sistance de la Cible.....: "+seuil+" %\nJet de R�sistance...........................: "+jet+"\nLa Cible subit donc pleinement l'effet du sortil�ge.";
				bm = new BM("Faiblesse Passag�re ",0,0,0,-bonus,0,0,0,0,0,0,0,0,0,false,2);
				BM realBM = t.addBM(bm);
				s1 += s3 + "\nLa cible aura donc un malus de " + realBM.getBMDegat()
				+ " points de d�gats pendant 2 tours.";
				s2 += s3 + "\nVous avez donc un malus de " + realBM.getBMDegat() + " points de d�gats pendant 2 tours.";
			}
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge sur "+t.getName()+" ("+t.getId()+")");
			t.getInbox().add(s2);
			return s1;
		}
		return s;
	}

	public String flashAveuglant()
	{
		String s=sortilege(currentTroll, Troll.SORT_FA,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			int malus=(1 + currentTroll.getVue()/5);
			boolean b=false;
			for(int i=0;i<trolls.size();i++)
			{
				BM bm;
				Troll t=trolls.elementAt(i);
				if(currentTroll.getPos().compareTo(t.getPos())!=0 || t.isDead())
				{
					continue;
				}
				if(currentTroll==t)
				{
					continue;
				}
				b=true;
				int seuil=calculeSeuil(currentTroll.getMM(),t.getRM());
				s+="\nVous avez tent� d'aveugler "+t.getName()+" ("+t.getId()+")";
				String s2=currentTroll.getName()+" ("+currentTroll.getId()+") a tent� de vous aveugler";
				int jet = diceHelper.roll(1, 100);
				if(jet<=seuil)
				{
					int bonus=Math.max(1,malus/2);
					String s3="\nSeuil de R�sistance de la Cible.....: "+seuil+" %\nJet de R�sistance...........................: "+jet+"\nLe sort a donc un EFFET REDUIT.";
					bm = new BM("Flash Aveuglant",-bonus,0,-Math.max(1,malus/2), 0, 0, 0, 0,-Math.max(1,malus/2),0, 0, 0,0,0, false, 1);
					BM realBM = t.addBM(bm);
					s += s3 + "\nLa cible aura donc un malus de " + realBM.getBMAttaque()
					+ " points en attaque, esquive et vue.";
					s2 += s3 + "\nVous avez donc un malus de " + realBM.getBMAttaque()
					+ " points en attaque, esquive et vue.";
				}
				else
				{
					int bonus=Math.max(1,malus);
					String s3="\nSeuil de R�sistance de la Cible.....: "+seuil+" %\nJet de R�sistance...........................: "+jet+"\nLa Cible subit donc pleinement l'effet du sortil�ge.";
					bm = new BM("Flash Aveuglant",-bonus,0,-Math.max(1,malus), 0, 0, 0, 0,-Math.max(1,malus),0, 0, 0,0,0, false, 2);
					BM realBM = t.addBM(bm);
					s += s3 + "\nLa cible aura donc un malus de " + realBM.getBMAttaque()
					+ " points en attaque, esquive et vue.";
					s2 += s3 + "\nVous avez donc un malus de " + realBM.getBMAttaque()
					+ " points en attaque, esquive et vue.";
				}
				events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge sur "+t.getName()+" ("+t.getId()+")");
				t.getInbox().add(s2);
			}
			if(!b)
			{
				events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge");
				return s+"\nIl n'y avait aucun troll dans votre caverne";
			}
			else return s;
		}
		else return s;
	}

	public String glue(Troll t)
	{
		int vue=currentTroll.getVue()+currentTroll.getBMVue()+currentTroll.getBMMVue();
		int portee=1+(vue/3);
		if(currentTroll.getPosN()!=t.getPosN() || !t.isVisibleFrom(currentTroll.getPosX(), currentTroll.getPosY(), currentTroll.getPosN(), portee)
				|| !t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),currentTroll.getVue()+currentTroll.getBMVue()+currentTroll.getBMMVue()) )
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas d'auto gluage !";
		}
		String s=sortilege(currentTroll, Troll.SORT_GLUE,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			int seuil=calculeSeuil(currentTroll.getMM(),t.getRM());
			String s1=s+"\nVous avez tent� de gluer "+t.getName()+" ("+t.getId()+")";
			String s2=currentTroll.getName()+" ("+currentTroll.getId()+") a tent� de vous gluer";
			int jet = diceHelper.roll(1, 100);
			if(jet<=seuil)
			{
				String s3="\nSeuil de R�sistance de la Cible.....: "+seuil+" %\nJet de R�sistance...........................: "+jet+"\nLe sort a donc AUCUN EFFET.";
				s1+=s3;
				s2+=s3;
			}
			else
			{
				String s3="\nSeuil de R�sistance de la Cible.....: "+seuil+" %\nJet de R�sistance...........................: "+jet+"\nLa Cible subit donc pleinement l'effet du sortil�ge.";
				s1+=s3+"\nLa cible est donc glu�e pendant 2 tours";
				s2+=s3+"\nVous �tes donc glu� pendant 2 tours";
				BM bm=new BM("Glue",0,0,0,0,0,0,0,0,0,0,0,true,2);
				t.addBM(bm);
			}
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge sur "+t.getName()+" ("+t.getId()+")");
			t.getInbox().add(s2);
			return s1;
		}
		else return s;
	}

	public String griffeDuSorcier(Troll t, int typeVenin)
	{
		if(!t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),0))
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas de flagellation !";
		}
		String s=sortilege(currentTroll, Troll.SORT_GDS,4);
		if(s.indexOf("R�USSI")!=-1)
		{
			s += "\n" + lowLevelAttaque(" avec un sortil�ge", currentTroll , t , currentTroll.getAttaque(),  currentTroll.getBMMAttaque(), t.getEsquive() , t.getBMEsquive()+t.getBMMEsquive(), calculeSeuil(currentTroll.getMM(),t.getRM()) , 2 * currentTroll.getDegat() / 3, (int)((2 * currentTroll.getDegat() / 3) * 1.5),currentTroll.getBMMDegat(),0,t.getArmureMag(),100,true);
			if(s.indexOf("TOUCH�")!=-1)
			{
				String veninMsg = "";
				BM bm;
				if(typeVenin == 0) {
					//Venin virulent
					int veninVirulent = (int) (1.5 * (1 + (currentTroll.getPVTotaux() / 10 + currentTroll.getRegeneration()) / 3));
					int jetVeninVirulent = diceHelper.roll(veninVirulent, 3);
					if(s.indexOf("EFFET REDUIT")==-1) {
						bm = new BM("Venin virulent", 0, 0, 0, 0, 0, 0,
								jetVeninVirulent, 0, 0, 0, 0, false,
								1 + currentTroll.getVue() / 10);
					}
					else {
						bm = new BM("Venin virulent", 0, 0, 0, 0, 0, 0,
								jetVeninVirulent / 2, 0, 0, 0, 0, false,
								(1 + currentTroll.getVue() / 10) / 2);
					}
					BM veninVirulentBM = t.addBM(bm);
					veninMsg += "\nCette attaque provoque un malus de poison virulent de  " + Math.abs(veninVirulentBM.getVenin())
					+ " pour les " + veninVirulentBM.getDuree() + " prochains tour(s)";
				}
				else {
					//Venin insidieux
					int veninInsidieux = (int) (1 + (currentTroll.getPVTotaux() / 10 + currentTroll.getRegeneration()) / 3);
					int jetVeninInsidieux = diceHelper.roll(veninInsidieux, 3);
					if(s.indexOf("EFFET REDUIT")==-1) {
						bm = new BM("Venin insidieux", 0, 0, 0, 0, 0, 0,
								jetVeninInsidieux, 0, 0, 0, 0, false,
								2 + currentTroll.getVue() / 5);
					}
					else {
						bm = new BM("Venin insidieux", 0, 0, 0, 0, 0, 0,
								jetVeninInsidieux / 2, 0, 0, 0, 0, false,
								(2 + currentTroll.getVue() / 5) / 2);
					}
					
					BM veninInsidieuxBM = t.addBM(bm);
					veninMsg += "\nCette attaque provoque un malus de poison insidieux de  " + Math.abs(veninInsidieuxBM.getVenin())
					+ " pour les " + veninInsidieuxBM.getDuree() + " prochains tour(s)";
				}

				t.getInbox()
				.add(t.getInbox().remove(t.getInbox().size() - 1)
						+ veninMsg);

				return s += veninMsg;
			}
		}
		return s;
	}

	public String hypnotisme(Troll t)
	{
		if(!t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),0))
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas d'auto hypnotisme !";
		}
		String s=sortilege(currentTroll, Troll.SORT_HYPNOTISME,4);
		if(s.indexOf("R�USSI")!=-1)
		{
			int seuil=calculeSeuil(currentTroll.getMM(),t.getRM());
			String s1=s+"\nVous avez tent� d'hypnotiser "+t.getName()+" ("+t.getId()+")";
			String s2=currentTroll.getName()+" ("+currentTroll.getId()+") a tent� de vous hypnotiser";
			int jet = diceHelper.roll(1, 100);
			if(jet<=seuil)
			{
				String s3="\nSeuil de R�sistance de la Cible.....: "+seuil+" %\nJet de R�sistance...........................: "+jet+"\nLe sort a donc un EFFET REDUIT.";
				s1+=s3+"\nVous avez donc fait perdre "+(currentTroll.getEsquiveTotale()/3)+" d�s d'esquive � la cible";
				s2+=s3+"\nVous avez donc perdu "+(currentTroll.getEsquiveTotale()/3)+" d�s d'esquive";
				for(int i=0;i<(currentTroll.getEsquiveTotale()/3);i++)
					t.getTouch();

			}
			else
			{
				String s3="\nSeuil de R�sistance de la Cible.....: "+seuil+" %\nJet de R�sistance...........................: "+jet+"\nLa Cible subit donc pleinement l'effet du sortil�ge.";
				s1+=s3+"\nVous avez donc fait perdre "+((3*currentTroll.getEsquiveTotale())/2)+" d�s d'esquive � la cible";
				s2+=s3+"\nVous avez donc perdu "+((3*currentTroll.getEsquiveTotale())/2)+" d�s d'esquive";
				for(int i=0;i<((3*currentTroll.getEsquiveTotale())/2);i++)
					t.getTouch();
				t.setPA(0);
				t.setNbParade(0);
				t.setNbCA(0);
			}
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge sur "+t.getName()+" ("+t.getId()+")");
			t.getInbox().add(s2);
			return s1;
		}
		else return s;
	}

	public String invisibilite()
	{
		if(!useInvisibilite)
			return "Error: utilisation d'invisibilit� interdite durant cette partie";
		String s=sortilege(currentTroll, Troll.SORT_INVISIBILITE,3);
		if(s.indexOf("R�USSI")!=-1)
		{
			currentTroll.setInvisible(true);
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge");
			return s+"\nVous �tes invisible";

		}
		else return s;
	}

	public String projectileMagique(Troll t)
	{
		int vue=currentTroll.getVue()+currentTroll.getBMVue()+currentTroll.getBMMVue();
		int portee=0;
		int max=0;
		int add=4;
		while(vue>max)
		{
			max+=add;
			add++;
			portee++;
		}
		if(currentTroll.getPosN()!=t.getPosN() || !t.isVisibleFrom(currentTroll.getPosX(), currentTroll.getPosY(), currentTroll.getPosN(), portee)
				|| !t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),currentTroll.getVue()+currentTroll.getBMVue()+currentTroll.getBMMVue()) )
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas de flagellation !";
		}
		String s=sortilege(currentTroll, Troll.SORT_PROJECTILE_MAGIQUE,4);
		if(s.indexOf("R�USSI")!=-1)
		{
			s += "\n" + lowLevelAttaque(" avec un sortil�ge", currentTroll , t , currentTroll.getVue(), currentTroll.getBMMAttaque(), t.getEsquive() , t.getBMEsquive()+t.getBMMEsquive(), calculeSeuil(currentTroll.getMM(),t.getRM()) , currentTroll.getVue()/2,(3*(currentTroll.getVue()/2))/2,currentTroll.getBMMDegat(),0,t.getArmureMag(),75,currentTroll.getPos().compareTo(t.getPos())==0);
			//Et le camouflage
			return s;
		}
		else return s;
	}

	public String siphonAmes(Troll t)
	{
		if(!t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),0))
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas de flagellation !";
		}
		String s=sortilege(currentTroll, Troll.SORT_SIPHON_AMES,4);
		if(s.indexOf("R�USSI")!=-1)
		{
			s += "\n" + lowLevelAttaque(" avec un sortil�ge", currentTroll , t , currentTroll.getAttaque(), currentTroll.getBMMAttaque() , t.getEsquive(), t.getBMEsquive()+t.getBMMEsquive(), calculeSeuil(currentTroll.getMM(),t.getRM()) , currentTroll.getRegeneration(),(3*(currentTroll.getRegeneration()/2))/2,currentTroll.getBMMDegat(),t.getArN(),0,100,false);
			if(s.indexOf("TOUCH�")!=-1) {
				BM bm;
				if(s.indexOf("EFFET REDUIT")==-1)
					bm=new BM("N�crose", -currentTroll.getRegeneration(),0,0,0,0,0,0,0,0,0,0,false,2);
				else
					bm=new BM("N�crose", -currentTroll.getRegeneration(),0,0,0,0,0,0,0,0,0,0,false,1);
				BM realBM = t.addBM(bm);
				t.getInbox().add(t.getInbox().remove(t.getInbox().size() - 1) + "\nDe plus vous aurez un malus de "
						+ Math.abs(realBM.getBMAttaque()) + " d'attaque pendant " + realBM.getDuree()
						+ " tour(s)");
				return s += "\nDe plus il aura un malus de " + Math.abs(realBM.getBMAttaque())
				+ " d'attaque pendant " + realBM.getDuree() + " tour(s)";
			}
			else return s;
		}
		else return s;
	}

	public String projection(Troll t)
	{
		if(!t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),0))
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas d'auto projection !";
		}
		String s=sortilege(currentTroll, Troll.SORT_PROJECTION,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			int seuil=calculeSeuil(currentTroll.getMM(),t.getRM());
			String s1=s+"\nVous avez tent� de projeter "+t.getName()+" ("+t.getId()+")";
			String s2=currentTroll.getName()+" ("+currentTroll.getId()+") a tent� de vous projeter";
			int jet = diceHelper.roll(1, 100);
			if(jet<=seuil)
			{
				String s3="\nSeuil de R�sistance de la Cible.....: "+seuil+" %\nJet de R�sistance...........................: "+jet+"\nLe sort a donc un EFFET REDUIT.";
				s1+=s3+"\nVous avez donc fait perdre 1 d� d'esquive � la cible";
				s2+=s3+"\nVous avez donc perdu 1 d� d'esquive";
				t.getTouch();

			}
			else
			{
				int x=0;int y=0;
				while(x==0 && y==0)
				{
					x = diceHelper.roll(1, 3) - 2;
					y = diceHelper.roll(1, 3) - 2;
				}
				String s3="\nSeuil de R�sistance de la Cible.....: "+seuil+" %\nJet de R�sistance...........................: "+jet+"\nLa Cible subit donc pleinement l'effet du sortil�ge.";
				s1+=s3+"\nVous avez donc fait perdre 1 d� d'esquive � la cible et l'avez projet� sur la case X="+(t.getPosX()+x)+"|Y="+(t.getPosY()+y)+"|N="+t.getPosN();
				s2+=s3+"\nVous avez donc perdu 1 d� d'esquive et avez �t� projet� sur la case X="+(t.getPosX()+x)+"|Y="+(t.getPosY()+y)+"|N="+t.getPosN();
				t.getTouch();
				t.setPos(t.getPosX()+x,t.getPosY()+y,t.getPosN());
				Lieu l=getLieuFromPosition(t.getPosX(),t.getPosY(),t.getPosN());
				if(l!= null && l instanceof Piege)
					s2+=appliquePiege((Piege) l);
			}
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge sur "+t.getName()+" ("+t.getId()+")");
			t.getInbox().add(s2);
			return s1;
		}
		else return s;
	}

	public String rafalePsychique(Troll t)
	{
		if(!t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),0))
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas de flagellation !";
		}
		String s=sortilege(currentTroll, Troll.SORT_RAFALE_PSYCHIQUE,4);
		if(s.indexOf("R�USSI")!=-1)
		{
			s += "\n" + lowLevelAttaque(" avec un sortil�ge", currentTroll , t , -1,  0, 0 , 0, calculeSeuil(currentTroll.getMM(),t.getRM()) , currentTroll.getDegat(),(3*currentTroll.getDegat())/2,currentTroll.getBMMDegat(),0,t.getArmureMag(),100,false);
			BM bm;
			if(s.indexOf("EFFET REDUIT")==-1)
				bm=new BM("Rafale Psychique", 0,0,0,0,-currentTroll.getDegat(),0,0,0,0,0,0,false,2);
			else
				bm=new BM("Rafale Psychique", 0,0,0,0,-currentTroll.getDegat(),0,0,0,0,0,0,false,1);
			BM realBM = t.addBM(bm);
			t.getInbox().add(t.getInbox().remove(t.getInbox().size() - 1) + "\nDe plus vous aurez un malus de "
					+ Math.abs(realBM.getBMRegeneration()) + " de r�g�n�ration pendant " + realBM.getDuree()
					+ " tour(s)");
			return s += "\nDe plus il aura un malus de " + Math.abs(realBM.getBMRegeneration())
			+ " de r�g�n�ration pendant " + realBM.getDuree() + " tour(s)";

		}
		else return s;
	}

	public String sacrifice(Troll t,int pv)
	{
		if(!t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),1) || t.getPosN()!=currentTroll.getPosN())
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: pas d'auto-soin !";
		}
		pv=Math.min(pv,t.getPVTotaux()-t.getPV());
		if(pv<0)
			return "Error: Petit malin !!!";
		if(pv>=currentTroll.getPV()/2)
			return "Error: Vous ne pouvez pas sacrifier plus de la moiti� de vos PV";
		String s=sortilege(currentTroll, Troll.SORT_SACRIFICE,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			t.addPVReg(pv);
			t.setPV(t.getPV()+pv);
			int pvPerdus = pv + diceHelper.roll((1 + pv / 5), 3);
			currentTroll.setPV(currentTroll.getPV()-(pvPerdus));
			s += "\nVous avez soign� le troll " + t.getName() + " (" + t.getId() + ") de " + pv + "PV.\nVous avez perdu " + pvPerdus + "PV.";
			t.getInbox().add("Vous avez �t� la cible du Sortil�ge Sacrifice lanc� par "+currentTroll.getName()+" ("+currentTroll.getId()+")\nVous avez �t� soign� de " + pv + "PV.");
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge sur "+t.getName()+" ("+t.getId()+")");
		}
		return s;

	}

	public String teleportation(int x,int y,int n)
	{
		if(!useTP)
			return "Error: Utilisation de t�l�portation interdite pendant cette partie";
		if(getLieuFromPosition(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN())!=null)
			return "Error: Il y a d�ja un lieu sur la case";
		int dist=(((int) Math.sqrt(19+8*((currentTroll.getMM()/5)+3)))-7)/2;
		int portH=dist+20+currentTroll.getVue();
		int portV=dist/3+3;
		if(Math.abs(x-currentTroll.getPosX())>portH || Math.abs(y-currentTroll.getPosY())>portH || Math.abs(n-currentTroll.getPosN())>portV)
			return "Error: Vous ne pouvez pas faire de portail pour aller aussi loin.";
		if(x<0 || x>=sizeArena || y<0 || y>=sizeArena || n<-(sizeArena+1)/2-1 || n>=0)
			return "Error: Vous ne pouvez pas vosu t�l�porter en dehors de l'ar�ne";
		//	Port�e Horizontale : Distance + 20 + Vue (arrondit inf�rieur)
		//	Port�e Verticale : Distance / 3 + 3 (arrondit inf�rieur)
		int duree = (diceHelper.roll(1, 6) + 1) * 60 * 24 + current_time;
		int dispersionX = Math.max(Math.abs(x-currentTroll.getPosX()),Math.abs(y-currentTroll.getPosY()))/10;
		int realX=-1;
		int realY=-1;
		int realN=-1;
		while(realX<0 || realX>=sizeArena || realY<0 || realY>=sizeArena || realN<-(sizeArena+1)/2-1 || realN>=0)
		{
			if(dispersionX!=0)
			{
				realX = x + (2 * diceHelper.roll(1, 2) - 3) * diceHelper.roll(dispersionX, 3);
				realY = y + (2 * diceHelper.roll(1, 2) - 3) * diceHelper.roll(dispersionX, 3);
			}
			else
			{
				realX=x;
				realY=y;
			}
			if(Math.abs(n-currentTroll.getPosN())>=5)
				realN = n + (2 * diceHelper.roll(1, 2) - 3)
				* (diceHelper.roll(Math.abs(n - currentTroll.getPosN()) / 5, 2) - 1);
			else
				realN=n;
		}
		String s=sortilege(currentTroll, Troll.SORT_TELEPORTATION,6);
		if(s.indexOf("R�USSI")!=-1) {
			listeLieux.add(new Portail(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),x, y, n, duree));
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge");
			s += "\nVous avez cr�� un portail en X = " + currentTroll.getPosX() + ", Y = " + currentTroll.getPosY() + ", N = " + currentTroll.getPosN();
			return s + "\nIl m�ne en X = " + realX + ", Y = " + realY + ", N =" + realN+"\nErreur de calcul : X = "+(realX-x)+" | Y = "+(realY-y)+" | N = "+(realN-n)+".";
		}
		else return s;
	}

	public String vampirisme(Troll t)
	{
		if(!t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),0))
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas de flagellation !";
		}
		String s=sortilege(currentTroll, Troll.SORT_VAMPIRISME,4);
		if(s.indexOf("R�USSI")!=-1)
		{
			int pv=t.getPV();
			s += "\n" + lowLevelAttaque(" avec un sortil�ge ", currentTroll , t , (2*currentTroll.getDegat())/3,  currentTroll.getBMMAttaque(), t.getEsquive() , t.getBMEsquive()+t.getBMMEsquive(), calculeSeuil(currentTroll.getMM(),t.getRM()) , currentTroll.getDegat(),(3*currentTroll.getDegat())/2,currentTroll.getBMMDegat(),0,t.getArmureMag(),100,true);
			pv-=Math.max(t.getPV(),0);
			if(pv>0)
			{
				currentTroll.addPVReg(Math.min(Math.min((pv+1)/2,currentTroll.getDegat()),currentTroll.getPVTotaux()-currentTroll.getPV()));
				currentTroll.setPV(Math.min(currentTroll.getPV()+Math.min((pv+1)/2,currentTroll.getDegat()),currentTroll.getPVTotaux()));
				s+="\nDe plus vous avez r�cup�r� "+(Math.min((pv+1)/2,currentTroll.getDegat()))+" points de vie";
			}
			return s;
		}
		else return s;
	}

	public String visionAccrue()
	{
		String s=sortilege(currentTroll,Troll.SORT_VA,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			BM bm = new BM("Vision Accrue",0,0,0,0,0,currentTroll.getVue()/2,0,0,0,0,0,false,2);
			BM realBM = currentTroll.addBM(bm);
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge");
			s += "\nvous b�n�ficiez d'un bonus de vue de " + realBM.getBMVue() + " cases pour les 2 prochains tours";
		}
		return s;
	}

	public String visionLointaine(int x,int y,int n)
	{
		String s=sortilege(currentTroll,Troll.SORT_VA,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			for(int i=0;i<trolls.size();i++)
			{
				if(trolls.elementAt(i).isVisibleFrom(x,y,n,currentTroll.getVue()))
					s+="\n"+trolls.elementAt(i).getId()+" "+trolls.elementAt(i).getPos();
			}
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge");
		}
		return s;
	}

	public String voirLeCache(int x, int y, int n)
	{
		String s=sortilege(currentTroll,Troll.SORT_VLC,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			for(int i=0;i<trolls.size();i++)
			{
				Troll tmpTroll=trolls.elementAt(i);
				if(Math.abs(tmpTroll.getPosX()-x) <= 1 && Math.abs(tmpTroll.getPosY()-y) <= 1 && Math.abs(tmpTroll.getPosN()-n) <= 1 )
					s+="\n"+trolls.elementAt(i).getId()+" "+trolls.elementAt(i).getPos();
			}
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge");
		}
		return s;
	}

	public String vueTroublee(Troll t)
	{
		if(t.getPosN() != currentTroll.getPosN() || Math.max(Math.abs(t.getPosX()-currentTroll.getPosX()),Math.abs(t.getPosY()-currentTroll.getPosY()))>1)
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas de flagellation !";
		}
		String s=sortilege(currentTroll,Troll.SORT_VT,2);
		if(s.indexOf("R�USSI")!=-1)
		{
			int seuil=calculeSeuil(currentTroll.getMM(),t.getRM());
			String s1=s+"\nVous avez tent� d'utiliser Vue Troubl�e sur le troll "+t.getName()+" ("+t.getId()+")";
			String s2=currentTroll.getName()+" ("+currentTroll.getId()+") a tent� d'utiliser Vue Troubl�e sur vous";
			int jet=diceHelper.roll(1,100);
			BM bm;
			if(jet<=seuil) {
				String s3="\nSeuil de R�sistance de la Cible.....: "+seuil+" %\nJet de R�sistance...........................: "+jet+"\nLe sort a donc un EFFET REDUIT.";
				bm = new BM("Vue Troubl�e",0,0,0,0,0,-Math.max(1,(currentTroll.getVue()/3)/2),0,0,0,0,0,false,1);
				BM realBM = t.addBM(bm);
				s1 += s3 + "\nLa cible aura donc un malus de " + realBM.getBMVue() + " en vue pendant 1 tour.";
				s2 += s3 + "\nVous avez donc un malus de " + realBM.getBMVue() + " en vue pendant 1 tour.";
			}
			else {
				String s3="\nSeuil de R�sistance de la Cible.....: "+seuil+" %\nJet de R�sistance...........................: "+jet+"\nLa Cible subit donc pleinement l'effet du sortil�ge.";
				bm = new BM("Vue Troubl�e",0,0,0,0,0,-Math.max(1,currentTroll.getVue()/3),0,0,0,0,0,false,2);
				BM realBM = t.addBM(bm);
				s1 += s3 + "\nLa cible aura donc un malus de " + realBM.getBMVue() + " en vue pendant 1 tour.";
				s2 += s3 + "\nVous avez donc un malus de " + realBM.getBMVue() + " en vue pendant 1 tour.";
			}
			events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� un sortil�ge sur "+t.getName()+" ("+t.getId()+")");
			t.getInbox().add(s2);
			return s1;
		}
		return s;
	}




	/*---------------------------------------------------------------------------------------------------------------------------------------------
     -- Utilitaires
     ---------------------------------------------------------------------------------------------------------------------------------------------*/

	public int calculeSeuil(int mm,int rm)
	{
		if(rm>mm)
			return Math.min(100-(mm*50)/rm,90);
		return Math.max((rm*50)/mm,10);
	}

	private String sortilege(Troll t, int id_sort,int cout)
	{
		int seuil=t.getReussiteSort(id_sort);
		int con=t.getConcentration();
		if(t.getPA()<cout)
		{
			return "Error: Vous avez besoin de "+cout+" PA pour r�aliser cette action";
		}
		if(seuil==0)
			return "Error: Vous ne connaissez pas ce sortil�ge";
		if(id_sort<=4 && t.getSortReserve())
			return "Error: Vous avez d�ja lanc� un sortil�ge r�serv� ce tour ci";
		t.setConcentration(0);
		if(id_sort<=4)
			t.setSortReserve(true);
		int jet=diceHelper.roll(1,100);
		String s="";
		if(con!=0)
			s+="Vous vous �tiez concentr� et b�n�ficiez d'un bonus de "+con+" %.\n";
		if(jet<=con+seuil)
		{
			s+="Vous avez R�USSI � utiliser ce sortil�ge ("+jet+" sur "+(con+seuil)+" %).\n";
			if(seuil>=80)
				s+="Il ne vous est plus possible d'am�liorer ce sortil�ge.";
			else if(seuil>=50)
			{
				jet=diceHelper.roll(1,100);
				s+="Votre Jet d'am�lioration est de "+jet+".\n";
				if(jet>seuil)
				{
					jet=diceHelper.roll(1,3);
					s+="Vous avez donc r�ussi � am�liorer ce sortil�ge de "+jet+" %.";
					t.augmentSort(id_sort,jet);
				}
				else
					s+="Vous n'avez donc pas r�ussi � am�liorer ce sortil�ge.";
			}
			else
			{
				jet=diceHelper.roll(1,100);
				s+="Votre Jet d'am�lioration est de "+jet+".\n";
				if(jet>seuil)
				{
					jet=diceHelper.roll(1,6);
					s+="Vous avez donc r�ussi � am�liorer ce sortil�ge de "+jet+" %.";
					t.augmentSort(id_sort,jet);
				}
				else
					s+="Vous n'avez donc pas r�ussi � am�liorer ce sortil�ge.";
			}
			currentTroll.setPA(currentTroll.getPA()-cout);
			currentTroll.addPAUtil(cout);

		}
		else
		{
			s+="Vous avez RAT� l'utilisation de ce sortil�ge ("+jet+" sur "+(con+seuil)+" %).\n";
			if(seuil==80)
				s+="Il ne vous est plus possible d'am�liorer ce sortil�ge.";
			else if(seuil>=50)
				s+="Il ne vous est plus possible d'am�liorer ce sortil�ge sans une r�ussite.";
			else
			{
				jet=diceHelper.roll(1,100);
				s+="Votre Jet d'am�lioration est de "+jet+".\n";
				if(jet>seuil)
				{
					s+="Vous avez donc r�ussi � am�liorer ce sortil�ge de 1 %.";
					t.augmentSort(id_sort,1);
				}
				else
					s+="Vous n'avez donc pas r�ussi � am�liorer ce sortil�ge.";
			}
			if(cout==2)
				jet=2;
			else
				jet=(cout+1)/2;
			s+="\nCel� vous a n�anmoins cout� "+jet+" Points d'Action mais l'�v�nement ne sera pas enregistr�.";
			currentTroll.setPA(currentTroll.getPA()-jet);
		}
		return s;
	}

	private Object[] competence(Troll t, int id_comp,int cout)
	{
		int level = t.getLevelComp(id_comp);
		int seuil=t.getReussiteComp(id_comp,level);
		int con=t.getConcentration();
		int levelSuccess = 0;
		if(t.getPA()<cout)
		{
			return new Object[] {-1,"Error: Vous avez besoin de "+cout+" PA pour r�aliser cette action"};
		}
		if(seuil==0)
			return new Object[] {-1,"Error: Vous ne connaissez pas cette comp�tence"};
		if(id_comp<=4 && t.getCompReservee())
			return new Object[] {-1,"Error: Vous avez d�ja lanc� une comp�tence r�serv�e ce tour ci"};
		t.setConcentration(0);
		if(id_comp<=4)
			t.setCompReservee(true);
		int jet=diceHelper.roll(1,100);
		String s="";
		if(con!=0)
			s+="Vous vous �tiez concentr� et b�n�ficiez d'un bonus de "+con+" %.\n";
		if(jet<=con+seuil)
		{
			s+="Vous avez R�USSI � utiliser cette comp�tence ("+jet+" sur "+(con+seuil)+" %).\n";
			levelSuccess = level;
			if(seuil>=90)
				s+="Il ne vous est plus possible d'am�liorer cette comp�tence.";
			else if(seuil>=75)
			{
				jet=diceHelper.roll(1,100);
				s+="Votre Jet d'am�lioration est de "+jet+".\n";
				if(jet>seuil)
				{
					s+="Vous avez donc r�ussi � am�liorer cette comp�tence de 1 %.";
					if(level>2)
					{
						int seuil_precedent=t.getReussiteComp(id_comp,level-1);
						if(seuil_precedent<jet && seuil_precedent<90)
						{
							s+="\nDe plus, vous avez r�ussi � am�liorer le niveau pr�c�dent de cette comp�tence de 1 % ("+seuil_precedent+" %).";
							t.augmentComp(id_comp,level-1,1);
						}
					}
					t.augmentComp(id_comp,level,1);
				}
				else
					s+="Vous n'avez donc pas r�ussi � am�liorer cette comp�tence.";
			}
			else if(seuil>=50)
			{
				jet=diceHelper.roll(1,100);
				s+="Votre Jet d'am�lioration est de "+jet+".\n";
				if(jet>seuil)
				{
					jet=diceHelper.roll(1,3);
					s+="Vous avez donc r�ussi � am�liorer cette comp�tence de "+jet+" %.";
					if(level>2)
					{
						int seuil_precedent=t.getReussiteComp(id_comp,level-1);
						if(seuil_precedent<jet && seuil_precedent<90)
						{
							s+="\nDe plus, vous avez r�ussi � am�liorer le niveau pr�c�dent de cette comp�tence de 1 % ("+seuil_precedent+" %).";
							t.augmentComp(id_comp,level-1,1);
						}
					}
					t.augmentComp(id_comp,level,jet);
				}
				else
					s+="Vous n'avez donc pas r�ussi � am�liorer cette comp�tence.";
			}
			else
			{
				jet=diceHelper.roll(1,100);
				s+="Votre Jet d'am�lioration est de "+jet+".\n";
				if(jet>seuil)
				{
					jet=diceHelper.roll(1,6);
					s+="Vous avez donc r�ussi � am�liorer cette comp�tence de "+jet+" %.";
					if(level>2)
					{
						int seuil_precedent=t.getReussiteComp(id_comp,level-1);
						if(seuil_precedent<jet && seuil_precedent<90)
						{
							s+="\nDe plus, vous avez r�ussi � am�liorer le niveau pr�c�dent de cette comp�tence de 1 % ("+seuil_precedent+" %).";
							t.augmentComp(id_comp,level-1,1);
						}
					}
					t.augmentComp(id_comp,level,jet);
				}
				else
					s+="Vous n'avez donc pas r�ussi � am�liorer cette comp�tence.";
			}
		}
		else
		{
			s+="Vous avez RAT� l'utilisation de cette comp�tence ("+jet+" sur "+(con+seuil)+" %).\n";
			for(int i=level-1;i>0;i--)
			{
				int pourcomp = t.getReussiteComp(id_comp,i);
				if(jet<=con+pourcomp)
				{
					s+="Mais vous avez R�USSI � utiliser le niveau inf�rieur (niveau "+i+") cette comp�tence ("+jet+" sur "+(con+pourcomp)+" %).\n";
					levelSuccess = i;
					break;
				}
			}

			if(levelSuccess==0)
			{
				if(seuil>=50)
					s+="Il ne vous est plus possible d'am�liorer cette comp�tence sans une r�ussite.\n";
				else
				{
					jet=diceHelper.roll(1,100);
					s+="Votre Jet d'am�lioration est de "+jet+".\n";
					if(jet>=seuil)
					{
						s+="Vous avez donc r�ussi � am�liorer cette comp�tence de 1 %.\n";
						t.augmentComp(id_comp,level,1);
					}
					else
						s+="Vous n'avez donc pas r�ussi � am�liorer cette comp�tence.\n";
				}
			}
			else
			{
				if(seuil>=90)
					s+="Il ne vous est plus possible d'am�liorer cette comp�tence sans une r�ussite.\n";
				else
				{
					jet=diceHelper.roll(1,100);
					s+="Votre Jet d'am�lioration est de "+jet+".\n";
					if(jet>=seuil)
					{
						s+="Vous avez donc r�ussi � am�liorer cette comp�tence de 1 %.\n";
						t.augmentComp(id_comp,level,1);
					}
					else
						s+="Vous n'avez donc pas r�ussi � am�liorer cette comp�tence.\n";
				}
			}

		}
		if(levelSuccess!=0)
		{
			if(Troll.COMP_DE!=id_comp)
			{
				currentTroll.setPA(currentTroll.getPA()-cout);
				currentTroll.addPAUtil(cout);
			}
		}
		else
		{
			if(cout==2)
				jet=2;
			else
				jet=(cout+1)/2;
			if(Troll.COMP_DE==id_comp)
				jet=1;
			s+="Cel� vous a n�anmoins cout� "+jet+" Points d'Action mais l'�v�nement ne sera pas enregistr�.\n";
			currentTroll.setPA(currentTroll.getPA()-jet);
		}
		return new Object[] {levelSuccess,s};
	}


	private String appliquePiege(Piege p)
	{
		String s="\nVous avez march� sur un pi�ge\n";
		String s1="Le pi�ge � la position X="+p.getPosX()+", Y="+p.getPosY()+", N="+p.getPosN()+" a �t� d�clench�";

		for(int i=0;i<trolls.size();i++)
		{
			Troll t=trolls.elementAt(i);
			if(t.getPosX()==p.getPosX() && t.getPosY()==p.getPosY() && t.getPosN()==p.getPosN() && !t.isDead())
			{
				int seuil=calculeSeuil(p.getCreateur().getMM(),t.getRM());
				s1+="\n"+lowLevelAttaque(" avec une comp�tence",p.getCreateur(),t,-1,0,0,0,seuil,p.getDegat(),0,0,0,0,0,false);
				if(t==currentTroll)
				{
					s+=t.getInbox().remove(t.getInbox().size()-1);
				}
				events.remove(events.size()-1);
				if(t.getPV()>0)
					events.add(current_time+" "+t.getName()+" ("+t.getId()+") a �t� victime d'un pi�ge de "+p.getCreateur().getName()+" ("+p.getCreateur().getId()+")");
				else
					events.add(current_time+" "+t.getName()+" ("+t.getId()+") est mort � cause d'un pi�ge de "+p.getCreateur().getName()+" ("+p.getCreateur().getId()+")");
			}
		}
		p.getCreateur().getInbox().add(s1);
		listeLieux.removeElement(p);
		return s;
	}

	public String releve(Troll t)
	{
		String s="\nVous vous �tes relev�.\n";
		if(currentTroll.isDead())
		{
			return "Error: Un troll mort ne peut se relever";
		}
		if(2>currentTroll.getPA())
		{
			return "Error: You need 2 PA to get up";
		}
		if(!currentTroll.isATerre())
		{
			return "Error: Le troll n'est pas � terre.";
		}
		currentTroll.setPA(currentTroll.getPA()-2);
		currentTroll.addPAUtil(2);
		currentTroll.setConcentration(Math.max(0,t.getConcentration()-2));
		currentTroll.releve();
		events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") s'est relev�.");
		currentTroll.getInbox().add("Vous vous �tes relev�.");

		return s;
	}

	public String attaque(Troll t)
	{
		if(currentTroll.getPos().compareTo(t.getPos())!=0 || t.isDead())
		{
			return "Error: Cible hors de port�e";
		}
		if(currentTroll==t)
		{
			return "Error: Pas de flagellation !";
		}
		if(4>currentTroll.getPA())
		{
			return "Error: You need 4 PA to attack";
		}
		currentTroll.setPA(currentTroll.getPA()-4);
		currentTroll.addPAUtil(4);
		currentTroll.setConcentration(Math.max(0,currentTroll.getConcentration()-4));
		return lowLevelAttaque("",currentTroll,t,currentTroll.getAttaque(),currentTroll.getBMAttaque()+currentTroll.getBMMAttaque(),t.getEsquive(),t.getBMEsquive()+t.getBMMEsquive(),0,currentTroll.getDegat(),(3*currentTroll.getDegat())/2,currentTroll.getBMDegat()+currentTroll.getBMMDegat(),t.getArN(),t.getArmurePhy()+t.getArmureMag(),100,true);
	}

	public String concentration(int nb)
	{
		if(nb>currentTroll.getPA())
		{
			return "Error: Vous n'avez pas assez de PA";
		}
		currentTroll.setConcentration(Math.max(0,currentTroll.getConcentration()-nb));
		currentTroll.setPA(currentTroll.getPA()-nb);
		currentTroll.setConcentration(currentTroll.getConcentration()+5*nb);
		return "Vous vous �tes concentr� pendant "+nb+" PA\nVotre prochaine action profitera d'un bonus de "+currentTroll.getConcentration()+" %";
	}

	public String prendreTP()
	{
		if(4>currentTroll.getPA())
			return "Error: Vous avez besoin de 4PA pour prendre un TP";
		Lieu l=getLieuFromPosition(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN());
		if(l!= null && !(l instanceof Portail))
			return "Error: Il n'y a pas de portail de t�l�portation sur votre case";
		Portail p=(Portail) l;
		currentTroll.setPA(currentTroll.getPA()-4);
		currentTroll.addPAUtil(4);
		int x=-1;
		int y=-1;
		int n=-1;
		while(x<0 || x>=sizeArena || y<0 || y>=sizeArena || n<-(sizeArena+1)/2-1 || n>=0)
		{
			x=p.getCibleX()+(2*diceHelper.roll(1,2)-3)*(diceHelper.roll(1,3)-1);
			y=p.getCibleY()+(2*diceHelper.roll(1,2)-3)*(diceHelper.roll(1,3)-1);
			n=p.getCibleN()+(2*diceHelper.roll(1,2)-3)*(diceHelper.roll(1,2)-1);
		}
		currentTroll.setPos(x,y,n);
		int dist=Math.max(Math.max(Math.abs(p.getCibleX()-p.getPosX()),Math.abs(p.getCibleY()-p.getPosY())),Math.abs(p.getCibleN()-p.getPosN()));
		//-1D6/10 distance en attaque (minimum 1D6) | -1D6/10 distance en esquive (minimum 1D6).
		if(dist<10)
			dist=10;
		//BM(String n, int a,int e,int d,int dla,int r,int vue,int venin,int ap,int am,int mm, int rm, boolean g,int dur)
		BM bm = new BM("D�sorientation",-diceHelper.roll(dist/10,6),-diceHelper.roll(dist/10,6),0,0,0,0,0,0,0,0,0,false,1);
		//	Vous avez �t� t�l�port� en X = 2002 | Y = 1998 | N = -600.
		//	Vous �tes d�sorient� et subirez donc les malus suivants pendant 1 tour : Attaque : -1D6 (-6) | Esquive : -1D6 (-2).
		//	Ce d�placement vous a cout� 4 PA et il vous en reste 2
		events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") s'est d�plac�");
		String s="Vous avez �t� t�l�port� en X = "+x+" | Y = "+y+" | N = "+n+".\n"+
				"Vous �tes d�sorient� et subirez donc les malus suivants pendant 1 tour : Attaque : -"+(dist/10)+"D6 ("+bm.getBMAttaque()+") | Esquive : -"+(dist/10)+"D6 ("+bm.getBMEsquive()+").\n"+
				"Cela vous a cout� 4 PA et il vous en reste "+currentTroll.getPA();
		l=getLieuFromPosition(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN());
		Troll t=currentTroll;
		if(l!= null && l instanceof Piege)
			s+=appliquePiege((Piege) l);
		else
			s+="\nAucun �v�nement n'est venu troubler votre Action.";
		if(t.getInvisible())
		{
			s+="\nVous �tes redevenu visible";
			t.setInvisible(false);
		}
		if(t.getCamouflage())
		{
			s+="\nDe plus votre camouflage a �t� annul�";
			t.setCamouflage(false);
		}
		currentTroll.setConcentration(Math.max(0,currentTroll.getConcentration()-4));
		return s;
	}

	public String deplace(int x,int y,int n,boolean isDE)
	{
		if(!(Math.abs(x)<=1 && Math.abs(y)<=1 && Math.abs(n)<=1))
			return "Error: Bad data formating in deplace instruction";
		if(x==y && x==n && x==0)
			return "Error: You have to move";
		int nbpa=2;
		int nbidentique=0;
		for(int i=0;i<trolls.size();i++)
			if(trolls.elementAt(i).isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),0))
				nbidentique++;
		if(nbidentique>1)
			nbpa++;
		if(n!=0)
			nbpa++;
		if(currentTroll.isGlue())
			nbpa=nbpa*2;
		nbpa=Math.min(6,nbpa);
		if(isDE)
			nbpa--;
		int px=currentTroll.getPosX();
		int py=currentTroll.getPosY();
		int pn=currentTroll.getPosN();
		if(px+x<0 || px+x>=sizeArena || py+y<0 || py+y>=sizeArena || pn+n<-(sizeArena+1)/2-1 || pn+n>=0)
			return "Error: You can't go out to the arena";
		if(nbpa>currentTroll.getPA())
			return "Error: You need "+nbpa+" PA to make this move";
		currentTroll.setPA(currentTroll.getPA()-nbpa);
		currentTroll.addPAUtil(nbpa);
		currentTroll.move(x,y,n);
		events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") s'est d�plac�");
		String s="Votre nouvelle position a �t� calcul�e : "+currentTroll.getPos()+"\nCe d�placement vous a cout� "+nbpa+" PA et il vous en reste "+currentTroll.getPA();
		Lieu l=getLieuFromPosition(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN());
		Troll t=currentTroll;
		if(l!= null && l instanceof Piege)
			s+=appliquePiege((Piege) l);
		else
			s+="\nAucun �v�nement n'est venu troubler votre Action.";
		if(t.getInvisible())
		{
			s+="\nVous �tes redevenu visible";
			t.setInvisible(false);
		}
		if(t.getCamouflage())
		{
			int roll=diceHelper.roll(1,100);
			if(roll<=t.getReussiteComp(Troll.COMP_CAMOUFLAGE,1))
				s+="\nDe plus votre camouflage est rest� actif";
			else
			{
				s+="\nDe plus votre camouflage a �t� annul�";
				t.setCamouflage(false);
			}
		}
		currentTroll.setConcentration(Math.max(0,currentTroll.getConcentration()-nbpa));
		return s;
	}

	public String utilisePotionParchemin(Equipement e, Troll t) {
		if(!t.isVisibleFrom(currentTroll.getPosX(),currentTroll.getPosY(),currentTroll.getPosN(),0) || t.isDead())
		{
			return "Error: Cible hors de port�e";
		}
		if(2>currentTroll.getPA())
		{
			return "Error: Il faut 2 PA pour utiliser une potion ou un parchemin";
		}
		currentTroll.setPA(currentTroll.getPA()-2);
		currentTroll.addPAUtil(2);
		currentTroll.setConcentration(Math.max(0,currentTroll.getConcentration()-2));
		String s=lowLevelPotionParchemin(e,t,0);
		//    	events.add(current_time+" "+currentTroll.getName()+" ("+currentTroll.getId()+") a utilis� une potion/parchemin sur "+t.getName()+" ("+t.getId()+")");
		currentTroll.removeEquipement(e);
		return s;
	}

	public boolean gameOver()
	{
		return gameState==STATE_GAMEOVER;
	}

	private String rendflou(int i, int flou,int min_flou, int max_flou)
	{
		int i_bas = i - r.nextInt(2)*flou;
		int i_haut = i_bas + 2*flou;
		if(i_bas>=max_flou)
			return "sup�rieur � "+max_flou+" ";
		if(i_haut<=min_flou)
			return "inf�rieur � "+min_flou+" ";
		return ( "entre " + i_bas + " et " + i_haut + " ");
	}

	private String formate(String s,int v)
	{
		if(v>0)
			return s+" +"+v+", ";
		if(v<0)
			return s+" "+v+", ";
		return "";
	}

	public static boolean isRegroupe() {
		return regroupe;
	}

	public static void setRegroupe(boolean r) {
		regroupe = r;
	}

	public static int getSizeArena() {
		return sizeArena;
	}

	public static void setSizeArena(int sizeArena) {
		MHAGame.sizeArena = sizeArena;
	}
}