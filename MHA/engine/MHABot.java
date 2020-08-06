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

public class MHABot {
	
	private Troll t;
	private MHAGame game;
	String vue="";
	String [] trolls=null;
	int nbTrollsCase=0;
	boolean dejaFrappe=false;
	boolean isCibleTom=false;
	boolean firstTime=false;
	
	
	public MHABot(MHAGame m,Troll tr) {
		game=m;
		t=tr;
	}

	public void play()
	{
		if(game.getCurrentTroll()!=t)
			return;
		synchronized(this) {
			try{
				wait(3000);
			}
			catch(Exception e){e.printStackTrace();}
		}
		updateVue();
		dejaFrappe=false;
		isCibleTom=false;
		firstTime=true;
		if(t.getReussiteComp(Troll.COMP_CAMOUFLAGE,1)!=0 && nbTrollsCase==1 && !t.getCamouflage())
		{
			t.beginTurn(game.getTime());
			if(t.getPV()<=0)
			{	
				game.events.add(game.getTime()+" "+t.getName()+" ("+t.getId()+") est mort empoisonné");
				//game.newTurn();
				return;
			}
			game.camouflage();
		}
		else if(t.getReussiteComp(Troll.COMP_ACCELERATION_DU_METABOLISME,1)!=0 && nbTrollsCase>1 && t.getFatigue()<5 && t.getDureeTourTotale()/15<=t.getPV())
		{
		//	System.out.println("J'accélère");
			t.beginTurn(game.getTime());
			if(t.getPV()<=0)
			{	
				game.events.add(game.getTime()+" "+t.getName()+" ("+t.getId()+") est mort empoisonné");
				//game.newTurn();
				return;
			}
			game.accelerationDuMetabolisme(t.getDureeTourTotale()/30);
		}
		else if(t.getReussiteComp(Troll.COMP_REGENERATION_ACCRUE,1)!=0 && t.getPV()<(30*t.getPVTotaux())/100)
		{
			t.beginTurn(game.getTime());
			if(t.getPV()<=0)
			{	
				game.events.add(game.getTime()+" "+t.getName()+" ("+t.getId()+") est mort empoisonné");
				//game.newTurn();
				return;
			}
			game.regenerationAccrue();
		}
		else
		{
			if(nbTrollsCase==1 && t.getNouveauTour()==game.getTime())
			{
				String[] s=t.getFullProfil().split(";");
				try
				{
					t.decale(Integer.parseInt(s[1])-1);
				}
				catch(Exception e){e.printStackTrace();}
				return;
			}
			t.beginTurn(game.getTime());
			if(t.getPV()<=0)
			{
				game.events.add(game.getTime()+" "+t.getName()+" ("+t.getId()+") est mort empoisonné");
				//game.newTurn();
				return;
			}
		}
		//Si j'ai pas assez de PV, je fuis
		if(t.getPV()<t.getPVTotaux()/10)
			deplace();
		//Si y a du troll à frapper, je frappe
		else if(nbTrollsCase>1)
			frappe();
		//Sinon je me cherche une cible
		else
			findCible();
		t.endTurn();
	}

	private void updateVue()
	{
		int x=t.getPosX();
		int y=t.getPosY();
		int n=t.getPosN();
		vue=game.getVue(t.getSocketId());
		trolls=vue.split("\n");
		int nbidentique=0;
		try
		{
		for(int i=0;i<trolls.length;i++)
		{
			String [] pos=trolls[i].split(" ");
			int dx=Integer.parseInt(pos[1]);
			int dy=Integer.parseInt(pos[2]);
			int dn=Integer.parseInt(pos[3]);
			//System.out.println(trolls[i]+"\n"+x+"=="+pos[1]+" "+y+"=="+pos[2]+" "+n+"=="+pos[3]+" "+(Integer.parseInt(pos[1])==x && Integer.parseInt(pos[2])==y && Integer.parseInt(pos[3])==n));
			if(dx==x && dy==y && dn==n)
				nbidentique++;
		}
		}
		catch(Exception e){e.printStackTrace();}
		nbTrollsCase=nbidentique;
	}

	private void frappe()
	{
//		System.out.println("J'essaie de frapper");
		int pa=t.getPA();
		int x=t.getPosX();
		int y=t.getPosY();
		int n=t.getPosN();
		int degExplo=0;
		int maxDegats=0;
		int meilleureAttaque=0;
		Troll meilleureCible=null;
		//Bon a va charger le troll sur qui je peux faire le plus de dégats et avec quelle attaque
		for(int i=0;i<trolls.length;i++)
		try
		{
			String [] pos=trolls[i].split(" ");
			int di=Integer.parseInt(pos[0]);
			int dx=Integer.parseInt(pos[1]);
			int dy=Integer.parseInt(pos[2]);
			int dn=Integer.parseInt(pos[3]);
			if(dx==x && dy==y && dn==n && di!=t.getId() && pa>=4)
			{
				Troll d=game.getTrollById(di);
				if((7*d.getEsquive())/2+d.getBMEsquive()+d.getBMMEsquive()<=(7*t.getAttaque())/2+t.getBMAttaque()+t.getBMMAttaque())
				{
					int deg=2*t.getDegat()+t.getBMDegat()+t.getBMMDegat()-t.getArmurePhy()-t.getArmureMag();
					if((7*d.getEsquive())+2*d.getBMEsquive()+2*d.getBMMEsquive()<=(7*t.getAttaque())/2+t.getBMAttaque()+t.getBMMAttaque())
						deg+=t.getDegat();
					deg=Math.max(1,deg/4);
					if(maxDegats<deg)
					{
						maxDegats=deg;
						meilleureAttaque=1;
						meilleureCible=d;
					}
				}
			}
			if(dx==x && dy==y && dn==n && di!=t.getId() && t.getReussiteComp(Troll.COMP_CDB,1)!=0 &&  pa>=4)
			{
				Troll d=game.getTrollById(di);
				if((7*d.getEsquive())/2+d.getBMEsquive()+d.getBMMEsquive()<=(7*t.getAttaque())/2+t.getBMAttaque()+t.getBMMAttaque())
				{
					int level = t.getLevelComp(Troll.COMP_CDB);
					for(int j=1;j<=level;j++)
					{
						int deg=2*t.getDegat()+(2*Math.min(3*level, t.getDegat()/2))+t.getBMDegat()+t.getBMMDegat()-t.getArmurePhy()-t.getArmureMag();
						if((7*d.getEsquive())+2*d.getBMEsquive()+2*d.getBMMEsquive()<=(7*t.getAttaque())/2+t.getBMAttaque()+t.getBMMAttaque())
							deg+=t.getDegat();
						deg=Math.max(1,(deg*t.getReussiteComp(Troll.COMP_CDB,j))/400);
						if(maxDegats<deg)
						{
							maxDegats=deg;
							meilleureAttaque=2;
							meilleureCible=d;
						}
					}
				}
			}
			if(dx==x && dy==y && dn==n && di!=t.getId() && t.getReussiteComp(Troll.COMP_AP,1)!=0 && pa>=4)
			{
				Troll d=game.getTrollById(di);
				if((7*d.getEsquive())/2+t.getBMEsquive()+t.getBMMEsquive()<=(21*t.getAttaque())/4+t.getBMAttaque()+t.getBMMAttaque())
				{
					int level = t.getLevelComp(Troll.COMP_AP);
					for(int j=1;j<=level;j++)
					{
						int deg=2*t.getDegat()+t.getBMDegat()+t.getBMMDegat()-t.getArmurePhy()-t.getArmureMag();
						if((7*d.getEsquive())+2*d.getBMEsquive()+2*d.getBMMEsquive()<=(7*t.getAttaque())/2+(7*Math.min(3*level, t.getAttaque()/2)/2)+t.getBMAttaque()+t.getBMMAttaque())
							deg+=t.getDegat();
						deg=Math.max(1,(deg*t.getReussiteComp(Troll.COMP_AP,j))/400);
						if(maxDegats<deg)
						{
							maxDegats=deg;
							meilleureAttaque=3;
							meilleureCible=d;
						}
					}
				}
			}
			if(dx==x && dy==y && dn==n && di!=t.getId() && t.getReussiteComp(Troll.COMP_FRENESIE,1)!=0 && pa>=6)
			{
				Troll d=game.getTrollById(di);
				if((7*d.getEsquive())/2+d.getBMEsquive()+d.getBMMEsquive()<=(7*t.getAttaque())/2+t.getBMAttaque()+t.getBMMAttaque())
				{
					int deg=2*t.getDegat()+t.getBMDegat()+t.getBMMDegat()-t.getArmurePhy()-t.getArmureMag();
					if((7*d.getEsquive())+2*d.getBMEsquive()+2*d.getBMMEsquive()<=(7*t.getAttaque())/2+t.getBMAttaque()+t.getBMMAttaque())
						deg+=t.getDegat();
					deg=deg*2;
					deg=Math.max(1,(deg*t.getReussiteComp(Troll.COMP_FRENESIE,1))/600);
					if(maxDegats<deg)
					{
						maxDegats=deg;
						meilleureAttaque=4;
						meilleureCible=d;
					}
				}
			}
			if(dx==x && dy==y && dn==n && di!=t.getId() && pa>=2 && t.getReussiteComp(Troll.COMP_BOTTE_SECRETE,1)!=0 && !t.getCompReservee())
			{
				Troll d=game.getTrollById(di);
				if((7*d.getEsquive())/2+d.getBMEsquive()+d.getBMMEsquive()<=(7*t.getAttaque())/4+(t.getBMAttaque()+t.getBMMAttaque())/2)
				{
					int deg=3*t.getDegat()+t.getBMDegat()+t.getBMMDegat()-t.getArmurePhy()-t.getArmureMag();
					if((7*d.getEsquive())+2*d.getBMEsquive()+2*d.getBMMEsquive()<=(7*t.getAttaque())/4+(t.getBMAttaque()+t.getBMMAttaque())/2)
						deg+=t.getDegat();
					deg=deg/2;
					deg=Math.max(1,(deg*t.getReussiteComp(Troll.COMP_BOTTE_SECRETE,1))/200);
					if(maxDegats<deg)
					{
						maxDegats=deg;
						meilleureAttaque=5;
						meilleureCible=d;
					}
				}
			}
			if(dx==x && dy==y && dn==n && di!=t.getId() && t.getReussiteSort(Troll.SORT_RAFALE_PSYCHIQUE)!=0 && pa>=4 && !t.getSortReserve())
			{
				Troll d=game.getTrollById(di);
				int deg=3*t.getDegat()+t.getBMMDegat()-t.getArmureMag();
				deg=Math.max(1,(deg*t.getReussiteSort(Troll.SORT_RAFALE_PSYCHIQUE))/400);
				if(maxDegats<deg)
				{
					maxDegats=deg;
					meilleureAttaque=6;
					meilleureCible=d;
				}
			}
			if(dx==x && dy==y && dn==n && di!=t.getId() && pa>=4 && t.getReussiteSort(Troll.SORT_VAMPIRISME)!=0 && !t.getSortReserve())
			{
				Troll d=game.getTrollById(di);
				if((7*d.getEsquive())/2+d.getBMEsquive()+d.getBMMEsquive()<=(7*t.getDegat())/3+t.getBMMAttaque())
				{
					int deg=3*t.getDegat()+t.getBMMDegat()-t.getArmureMag();
					if((7*d.getEsquive())+2*d.getBMEsquive()+2*d.getBMMEsquive()<=(7*t.getDegat())/3+t.getBMMAttaque())
						deg+=t.getDegat();
					deg=Math.max(1,(deg*t.getReussiteSort(Troll.SORT_VAMPIRISME))/400);
					if(maxDegats<deg)
					{
						maxDegats=deg;
						meilleureAttaque=7;
						meilleureCible=d;
					}
				}
			}
			if(dx==x && dy==y && dn==n && di!=t.getId() && pa>=4 && t.getReussiteSort(Troll.SORT_GDS)!=0)
			{
				Troll d=game.getTrollById(di);
				if((7*d.getEsquive())/2+d.getBMEsquive()+d.getBMMEsquive()<=(7*t.getAttaque())/2+t.getBMMAttaque())
				{
					int deg=t.getDegat()+t.getBMMDegat()+(1+t.getVue()/5)*(1+t.getPVTotaux()/30)*2-t.getArmureMag();
					if((7*d.getEsquive())+2*d.getBMEsquive()+2*d.getBMMEsquive()<=(7*t.getAttaque())/2+t.getBMMAttaque())
						deg+=t.getDegat()/2;
					deg=Math.max(1,(deg*t.getReussiteSort(Troll.SORT_GDS))/400);
					if(maxDegats<deg)
					{
						maxDegats=deg;
						meilleureAttaque=8;
						meilleureCible=d;
					}
				}
			}
			if(dx==x && dy==y && dn==n && di!=t.getId() && pa>=6 && t.getReussiteSort(Troll.SORT_EXPLOSION)!=0)
			{
				Troll d=game.getTrollById(di);
					degExplo+=(((2+(t.getDegat()-3+(t.getPVTotaux()/10-3))))*t.getReussiteSort(Troll.SORT_EXPLOSION))/600;
					if(maxDegats<degExplo)
					{
						maxDegats=degExplo;
						meilleureAttaque=9;
						meilleureCible=d;
					}
			}
			int porteeCharge=0;
        		int max=0;
			int add=4;
			while((t.getPV()/10)+t.getRegeneration()>max)
			{
				max+=add;
				add++;
				porteeCharge++;
			}
			if(t.getReussiteComp(Troll.COMP_CHARGER,1)!=0 && Math.max(Math.abs(dx-x),Math.abs(dy-y))<=porteeCharge && Math.max(Math.abs(dx-x),Math.abs(dy-y))>0 && dn==n && di!=t.getId() && pa>=4)
			{
				Troll d=game.getTrollById(di);
				if((7*d.getEsquive())/2+d.getBMEsquive()+d.getBMMEsquive()<=(7*t.getAttaque())/2+t.getBMAttaque()+t.getBMMAttaque())
				{
					int deg=2*t.getDegat()+t.getBMDegat()+t.getBMMDegat()-t.getArmurePhy()-t.getArmureMag();
					if((7*d.getEsquive())+2*d.getBMEsquive()+2*d.getBMMEsquive()<=(7*t.getAttaque())/2+t.getBMAttaque()+t.getBMMAttaque())
						deg+=t.getDegat();
					deg=Math.max(1,(deg*t.getReussiteComp(Troll.COMP_CHARGER,1))/400);
					if(maxDegats<deg)
					{
						maxDegats=deg;
						meilleureAttaque=10;
						meilleureCible=d;
					}
				}
			}
			int vue=t.getVue()+t.getBMVue()+t.getBMMVue();
			int portee=0;
			max=0;
			add=4;
			while(vue>max)
			{
				max+=add;
				add++;
				portee++;
			}
			if(t.getReussiteSort(Troll.SORT_PROJECTILE_MAGIQUE)!=0 && Math.max(Math.abs(dx-x),Math.abs(dy-y))<=portee && dn==n && di!=t.getId() && pa>=4 && !t.getSortReserve())
			{
				Troll d=game.getTrollById(di);
				if((7*d.getEsquive())/2+d.getBMEsquive()+d.getBMMEsquive()<=(7*t.getVue())/2+t.getBMMAttaque())
				{
					int deg=t.getVue()+t.getBMMDegat()-t.getArmureMag();
					if((7*d.getEsquive())+2*d.getBMEsquive()+2*d.getBMMEsquive()<=(7*t.getVue())/2+t.getBMMAttaque())
						deg+=t.getVue()/2;
					deg=Math.max(1,(deg*t.getReussiteSort(Troll.SORT_PROJECTILE_MAGIQUE))/400);
					if(maxDegats<deg)
					{
						maxDegats=deg;
						meilleureAttaque=11;
						meilleureCible=d;
					}
				}
			}
		}
		catch(Exception e){e.printStackTrace();}
		if(maxDegats==0)
		{
			//Tu peux pas faire de dégats, donc cours forest!!!
			//S'il a 6PA, faudrait peut etre vérifier s'il peut pas se booster pour faire des dégats
			//S'il reste 2 PA et que je suis pas camouflé (et qu'il y a personne sur la case), je peux peut etre me camoufler
			//deplace();
			findCible();
			return;
		}
		String s="";
		//SI j'ai 6PA et que je n'attaque qu'à 4PA et que je suis pas skrim, j'ai une chance de me booster un peu
		if(pa==6 && meilleureAttaque==3 && (t.getReussiteSort(Troll.SORT_ADA)!=0 || t.getReussiteSort(Troll.SORT_ADD)!=0) && t.getReussiteComp(Troll.COMP_BOTTE_SECRETE,1)==0 )
		{
			if(t.getReussiteSort(Troll.SORT_ADA)!=0 && MHAGame.roll(1,160)<t.getReussiteSort(Troll.SORT_ADA))
				game.augmentationDeLAttaque();
			else if(t.getReussiteSort(Troll.SORT_ADD)!=0 && MHAGame.roll(1,160)<t.getReussiteSort(Troll.SORT_ADD))
				game.augmentationDesDegats();
			pa=t.getPA();
		}
		else if(pa==6 && meilleureAttaque==2 && (t.getReussiteSort(Troll.SORT_ADA)!=0 || t.getReussiteSort(Troll.SORT_ADD)!=0) && t.getReussiteComp(Troll.COMP_BOTTE_SECRETE,1)==0 )
		{
			if(t.getReussiteSort(Troll.SORT_ADD)!=0 && MHAGame.roll(1,160)<t.getReussiteSort(Troll.SORT_ADD))
				game.augmentationDesDegats();
			else if(t.getReussiteSort(Troll.SORT_ADA)!=0 && MHAGame.roll(1,160)<t.getReussiteSort(Troll.SORT_ADA))
				game.augmentationDeLAttaque();
			pa=t.getPA();
		}
		else if(pa==6&&(meilleureAttaque==6 || meilleureAttaque==7 || meilleureAttaque==8)&& t.getReussiteSort(Troll.SORT_BUM)!=0 && t.getReussiteComp(Troll.COMP_BOTTE_SECRETE,1)==0 )
		{
			if(MHAGame.roll(1,100)<t.getReussiteSort(Troll.SORT_BUM)*(100-game.calculeSeuil(t.getMM(),meilleureCible.getRM()))/100)
				game.bulleMagique();
			pa=t.getPA();
		}
		switch(meilleureAttaque)
		{
			case 1: s=game.attaque(meilleureCible);break;
			case 2: s=game.coupDeButoir(meilleureCible);break;
			case 3: s=game.attaquePrecise(meilleureCible);break;
			case 4: s=game.frenesie(meilleureCible);break;
			case 5: s=game.botteSecrete(meilleureCible);break;
			case 6: s=game.rafalePsychique(meilleureCible);break;
			case 7: s=game.vampirisme(meilleureCible);break;
			case 8: s=game.griffeDuSorcier(meilleureCible);break;
			case 9: s=game.explosion();break;
			case 10: s=game.charger(meilleureCible);break;
			case 11: s=game.projectileMagique(meilleureCible);break;
			default : return;
		}
		dejaFrappe=true;
		if(meilleureCible.getRace()==Troll.RACE_TOMAWAK)
			isCibleTom=true;
		//System.out.println(s);
		updateVue();
		frappe();
		

	}

	private void findCible()
	{
		int pa=t.getPA();
		int x=t.getPosX();
		int y=t.getPosY();
		int n=t.getPosN();
		int distMin=500;
		Troll cible=null;
		if(pa>=2 && (t.getReussiteSort(Troll.SORT_AE)!=0 || t.getReussiteSort(Troll.SORT_ADE)!=0))
		{
			if(MHAGame.roll(1,160)<t.getReussiteSort(Troll.SORT_AE))
				game.armureEtheree();
			else if(MHAGame.roll(1,160)<t.getReussiteSort(Troll.SORT_ADE))
				game.augmentationDeLEsquive();
			pa=t.getPA();
		}
		//Je cherche une cible
		//Mais y a peut etre quelqu'un à portée de charge ou de projo
		for(int i=0;i<trolls.length;i++)
		try
		{
			String [] pos=trolls[i].split(" ");
			int di=Integer.parseInt(pos[0]);
			int dx=Integer.parseInt(pos[1]);
			int dy=Integer.parseInt(pos[2]);
			int dn=Integer.parseInt(pos[3]);
			int porteeCharge=0;
        		int max=0;
			int add=4;
			int dist=(Math.max(Math.max(Math.abs(dx-x),Math.abs(dy-y)),Math.abs(dn-n))+Math.abs(dn-n));
			if(di!=t.getId() && distMin>dist)
			{
				distMin=dist;
				cible=game.getTrollById(di);
			}
			while((t.getPV()/10)+t.getRegeneration()>max)
			{
				max+=add;
				add++;
				porteeCharge++;
			}
			if(t.getReussiteComp(Troll.COMP_CHARGER,1)!=0 && Math.max(Math.abs(dx-x),Math.abs(dy-y))<=porteeCharge && Math.max(Math.abs(dx-x),Math.abs(dy-y))>0 && dn==n && di!=t.getId() && pa>=4 && firstTime)
			{
				firstTime=false;
				frappe();
				return;
			}
			int vue=t.getVue()+t.getBMVue()+t.getBMMVue();
			int portee=0;
			max=0;
			add=4;
			while(vue>max)
			{
				max+=add;
				add++;
				portee++;
			}
			if(t.getReussiteSort(Troll.SORT_PROJECTILE_MAGIQUE)!=0 && Math.max(Math.abs(dx-x),Math.abs(dy-y))<=portee && dn==n && di!=t.getId() && pa>=4 && !t.getSortReserve())
			{
				frappe();
				return;
			}
		}
		catch(Exception e){e.printStackTrace();}
		if(t.getReussiteComp(Troll.COMP_PIEGE,1)!=0 && pa>=4 && game.getLieuFromPosition(x,y,n)==null && MHAGame.roll(1,160)<t.getReussiteComp(Troll.COMP_PIEGE,1) )
		{
			game.construireUnPiege();
			findCible();
			return;
		}
		if(t.getReussiteComp(Troll.COMP_REGENERATION_ACCRUE,1)!=0 && pa>=2 && !t.getCompReservee() &&
		          ((MHAGame.roll(1,50)+90)*t.getPV())/t.getPVTotaux()<t.getReussiteComp(Troll.COMP_REGENERATION_ACCRUE,1) )
		{
			game.regenerationAccrue();
			findCible();
			return;
		}
//		System.out.println("Je cherche une cible");
		if(cible==null)
		{
			if(deplaceOnce())
			{
				if(nbTrollsCase>1)
					frappe();
				findCible();
				return;
			}
			finishPA();
			return;
		}
		if(dejaFrappe && nbTrollsCase>1 && !isCibleTom)
		{
			//J'ai déja fait mal, et y a quelqu'un avec moi
			//--> On se casse de la sauf si c'est un tom...
			deplace();
		}
		if(dejaFrappe && nbTrollsCase==1)
		{
			//Je viens de toucher quelqu'un, mais il est pas sur ma case
			//Je suis tom ou alors je viens de le tuer
			//On va alors se booster un peu
			finishPA();
		}
		boolean de=(t.getReussiteComp(Troll.COMP_DE,1)!=0);
		int nbpa=paDeplacement();
//		if(nbTrollsCase>1)
//			return;
		if(nbpa+((t.isGlue())?2:1)*sens(nbTrollsCase-1)<=pa && nbTrollsCase==1)
		{
			String result="";
			//J'ai aps assez de PA pour me déplacer en vertical et je suis juste en dessous de ma cible
			if(cible.getPosX()-x==0 && cible.getPosY()-y==0 && ((nbpa+((t.isGlue())?2:1)*sens(nbTrollsCase-1)==pa)))
			{
				finishPA();
				return;
			}
			if(de)
				result=game.deplacementEclair(sens(cible.getPosX()-x),sens(cible.getPosY()-y),((nbpa+((t.isGlue())?2:1)*sens(nbTrollsCase-1)==pa)?0:sens(cible.getPosN()-n)));
			else
				result=game.deplace(sens(cible.getPosX()-x),sens(cible.getPosY()-y),((nbpa+((t.isGlue())?2:1)*sens(nbTrollsCase-1)==pa)?0:sens(cible.getPosN()-n)),false);
			if(pa==t.getPA())
			{
				System.out.println(result);
				return;
			}
			updateVue();
			findCible();
			return;
		}
		if(firstTime && nbTrollsCase>1 && (pa>=4 || pa>=2 && t.getReussiteComp(Troll.COMP_BOTTE_SECRETE,1)!=0 && !t.getCompReservee()))
		{
			firstTime=false;
			frappe();
			return;
		}
		finishPA();
	}

	private void deplace()
	{
//		System.out.println("Je fuis");
//		Bon faudrait penser à se camoufler quand même !!!
		while(deplaceOnce())
			if(t.getPA()>=2 && t.getReussiteComp(Troll.COMP_CAMOUFLAGE,1)!=0 && nbTrollsCase==1 && !t.getCamouflage() && !t.getCompReservee())
			{
				game.camouflage();
			}
	}

	private int sens(int i)
	{
		if(i<0)
			return -1;
		if(i==0)
			return 0;
		return 1;
	}

	private void finishPA()
	{
		int pa=t.getPA();
//		if(pa<=1)
//			return;
		if(pa>=2 && t.getReussiteComp(Troll.COMP_CAMOUFLAGE,1)!=0 && nbTrollsCase==1 && !t.getCamouflage() && !t.getCompReservee())
		{
			game.camouflage();
			finishPA();
			return;
		}
		if(pa>=2 && t.getReussiteComp(Troll.COMP_REGENERATION_ACCRUE,1)!=0 && t.getPV()<t.getPVTotaux() && !t.getCompReservee())
		{
			game.regenerationAccrue();
			finishPA();
			return;
		}
//		if(paDeplacement()+((t.isGlue())?2:1)*sens(nbTrollsCase-1)<=pa)
//			findCible();
	}

	private int paDeplacement()
	{
		boolean de=(t.getReussiteComp(Troll.COMP_DE,1)!=0);
		int nbpa=2;
		if(t.isGlue())
			nbpa=nbpa*2;
		if(de)
			nbpa--;
		nbpa=Math.min(6,nbpa);
		return nbpa;
	}

	private boolean deplaceOnce()
	{
		if(t.getPV()<=0)
			return false;
		int x=t.getPosX();
		int y=t.getPosY();
		int n=t.getPosN();
		int pa=t.getPA();
		int nbidentique=0;
		boolean de=(t.getReussiteComp(Troll.COMP_DE,1)!=0);
		
		int nbpa=paDeplacement()+((t.isGlue())?2:1)*sens(nbTrollsCase-1);

		if(nbpa>pa)
			return false;
		String result;
		do
		{
			int dx=MHAGame.roll(1,3)-2;
			int dy=MHAGame.roll(1,3)-2;
			int dn=((nbpa==pa)?0:MHAGame.roll(1,3)-2);
			//System.out.println("J'essaie d'aller en "+(x+dx)+" "+(y+dy)+" "+(n+dn));
			if(de)
				result=game.deplacementEclair(dx,dy,dn);
			else
				result=game.deplace(dx,dy,dn,false);
	//		if(result.substring(0,6).equals("Error:"))
	//			System.out.println("J'essaie d'aller en "+(x+dx)+" "+(y+dy)+" "+(n+dn)+"\n"+result);
		}
		while(result.substring(0,6).equals("Error:"));
		updateVue();
		return true;
	}
}
