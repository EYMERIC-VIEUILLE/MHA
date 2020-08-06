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

import mha.engine.MHABot;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;


public class Troll {

    public final static int RACE_SKRIM     = 0;
    public final static int RACE_DURAKUIR   = 1;
    public final static int RACE_KASTAR        = 2;
    public final static int RACE_TOMAWAK = 3;
    
    public final static int NB_COMP=14;
    public final static int COMP_BOTTE_SECRETE = 1;
    public final static int COMP_REGENERATION_ACCRUE = 2;
    public final static int COMP_ACCELERATION_DU_METABOLISME = 3;
    public final static int COMP_CAMOUFLAGE = 4;
    public final static int COMP_AP = 5;
    public final static int COMP_CHARGER = 6;
    public final static int COMP_PIEGE = 7;
    public final static int COMP_CONTREATTAQUE = 8;
    public final static int COMP_CDB = 9;
    public final static int COMP_DE = 10;
    public final static int COMP_FRENESIE = 11;
    public final static int COMP_LDP = 12;
    public final static int COMP_PARER = 13;
    public final static int COMP_PISTAGE = 14;

    private final int[] coutPX={0,1,1,1,1,5,5,5,2,5,2,10,3,2,1};
    
    public final static String[] NOM_COMP={"Botte secrète","Régénération accrue","Accélération du métabolisme","Camouflage","Attaque précise","Charger","Poser un piège","Contre-attaque","Coup de butoir","Déplacement éclair","Frénésie","Lancer de potion","Parer","Pistage"};
    
    public final static int NB_SORT=24;
    public final static int SORT_HYPNOTISME = 1;
    public final static int SORT_RAFALE_PSYCHIQUE = 2;
    public final static int SORT_VAMPIRISME = 3;
    public final static int SORT_PROJECTILE_MAGIQUE = 4;
    public final static int SORT_AA = 5;
    public final static int SORT_AE = 6;
    public final static int SORT_ADA = 7;
    public final static int SORT_ADE = 8;
    public final static int SORT_ADD = 9;
    public final static int SORT_BAM = 10;
    public final static int SORT_BUM = 11;
    public final static int SORT_EXPLOSION = 12;
    public final static int SORT_FP = 13;
    public final static int SORT_FA = 14;
    public final static int SORT_GLUE = 15;
    public final static int SORT_GDS = 16;
    public final static int SORT_INVISIBILITE = 17;
    public final static int SORT_PROJECTION = 18;
    public final static int SORT_SACRIFICE = 19;
    public final static int SORT_TELEPORTATION = 20;
    public final static int SORT_VA = 21;
    public final static int SORT_VL = 22;
    public final static int SORT_VLC = 23;
    public final static int SORT_VT = 24;
    
    public final static String[] NOM_SORT={"Hypnotisme","Rafale Psychique","Vampirisme","Projectile magique","Analyse anatomique","Armure éthérée","Augmentation de l'attaque","Augmentation de l'esquive","Augmentation des dégats","Bulle Anti-magie","Bulle magique","Explosion","Faiblesse passagère","Flash aveuglant","Glue","Griffe du sorcier","Invisibilité","Projection","Sacrifice","Téléportation","Vision accrue","Vision lointaine","Voir le caché","Vue troublée"};
    
    private Hashtable<Integer,Hashtable<Integer,Integer>> pourcentagesComp=new Hashtable<Integer,Hashtable<Integer,Integer>>();
    private int [] pourcentagesSort=new int[NB_SORT+1];
    private int deginflig=0;
    private int kill=0;
    private int nbPA_utile=0;
    private int nbPVRegen=0;

    
    private String name;
    private int id;
    private int idSocket;
    private int team;
    private int race;
    private int niveau=1;
    private int tempsRestant=0;
    private int des_Attaque;
    private int des_Esquive;
    private int des_Degat;
    private int pv_totaux;
    private int pv_actuels;
    private int des_Regeneration;
    private int vue;
    private int duree_tour;
    private int date_jeu; //en minutes depuis le début
    private int debut_tour; //en minutes depuis le début
    private boolean isCreated=false; //Est ce que le joueur a validé son troll
    private boolean isActive=false; //Est ce que la DLA est jouée ?
    private boolean compReservee=false; //A t il déja essayer de faire une comp réservée ?
    private boolean sortReserve=false;
    private boolean isCertif=false;
    private String icon="";
    
    private int malus_des_esquive=0;
    private int malus_des_attaque=0;
    private int bmAttaque=0;
    private int bmmAttaque=0;
    private int bmEsquive=0;
    private int bmmEsquive=0;
    private int bmDegat=0;
    private int bmmDegat=0;
    private int bmRegeneration=0;
    private int bmmRegeneration=0;
    private int bmVue=0;
    private int bmmVue=0;
    private int bmDLA=0;  
    private int bmMM=0; 
    private int bmRM=0;   
    private int venin=0;
    private int glue=0;
    private int nbParade=0;
    private int nbCA=0;
    
    private int rm=25;
    private int mm=25;
    
    private int armure_phy=0;
    private int armure_mag=0;
    
    private int fatigue=0;
    private int pa;
    private int posx=0;
    private int posy=0;
    private int posn=0;
    private int concentration=0;
    private int poids=0;
    
    private int dureeTourTotale=0;
    
    private boolean camoufle=false;
    private boolean invisible=false;
    private boolean frenetique=false;
    private int nbResu=0;
    private Vector<String> inbox=new Vector<String>();
    private Vector<BM> listeBM=new Vector<BM>();
    private Vector<Mouche> listeMouches=new Vector<Mouche>();
    
    //  Gestion de l'équipement
    
    private Vector<Equipement> listeEquipement=new Vector<Equipement>();
    //null signifie qu'il n'y a rien
    private Equipement tete=null;
    private Equipement cou=null;
    private Equipement mainDroite=null;
    private Equipement mainGauche=null;
    private Equipement torse=null;
    private Equipement pieds=null;

    public MHABot bot=null;
    

    public Troll(String n, int i,int s, int c,int r) {
	name = n;
	id = i;
	team=c;
	idSocket=s;
	des_Attaque=3;
	des_Esquive=3;
	des_Degat=3;
	pv_totaux=30;
	pv_actuels=30;
	des_Regeneration=1;
	vue=3;
	duree_tour=12*60;
	pa=0;
	debut_tour=MHAGame.roll(1,duree_tour)-1;
	date_jeu=debut_tour;
	if(r==RACE_SKRIM)
	{
		race=r;
		des_Attaque=4;
		setComp(COMP_BOTTE_SECRETE,25,1);
		pourcentagesSort[SORT_HYPNOTISME]=25;
	} else if(r==RACE_DURAKUIR)
	{
		race=r;
		pv_totaux=pv_actuels=40;
		setComp(COMP_REGENERATION_ACCRUE,25,1);
		pourcentagesSort[SORT_RAFALE_PSYCHIQUE]=25;
	} else if(r==RACE_KASTAR)
	{
		race=r;
		des_Degat=4;
		setComp(COMP_ACCELERATION_DU_METABOLISME,25,1);
		pourcentagesSort[SORT_VAMPIRISME]=25;
	} else if(r==RACE_TOMAWAK)
	{
		race=r;
		vue=4;
		setComp(COMP_CAMOUFLAGE,25,1);
		pourcentagesSort[SORT_PROJECTILE_MAGIQUE]=25;
	}
	/* jeter une exception */
    }
    
    public boolean isActive()
    {
    	return isActive;
    }
    
    public void setActive(boolean b)
    {
    	isActive=b;
    }
    
    public void addBM(BM bm)
    {
    	listeBM.add(bm);
	if(bm.getBMAttaque()!=0)
		bmAttaque+=bm.getBMAttaque();
	if(bm.getBMMAttaque()!=0)
		bmmAttaque+=bm.getBMMAttaque();
	if(bm.getBMEsquive()!=0)
		bmEsquive+=bm.getBMEsquive();
	if(bm.getBMMEsquive()!=0)
		bmmEsquive+=bm.getBMMEsquive();
	if(bm.getBMDegat()!=0)
		bmDegat+=bm.getBMDegat();
	if(bm.getBMMDegat()!=0)
		bmmDegat+=bm.getBMMDegat();
	if(bm.getBMMRegeneration()!=0)
		bmRegeneration+=bm.getBMMRegeneration();
	if(bm.getBMRegeneration()!=0)
		bmmRegeneration+=bm.getBMRegeneration();
	if(bm.getBMVue()!=0)
		bmVue+=bm.getBMVue();
	if(bm.getBMMVue()!=0)
		bmmVue+=bm.getBMMVue();
	if(bm.getBMDLA()!=0)
		bmDLA+=bm.getBMDLA();
	if(bm.getBMArmurePhysique()!=0)
		armure_phy+=bm.getBMArmurePhysique();
	if(bm.getBMArmureMagique()!=0)
		armure_mag+=bm.getBMArmureMagique();
	if(bm.isGlue())
		glue++;
	if(bm.getVenin()!=0)
		venin+=bm.getVenin();
	if(bm.getBMMM()!=0)
		bmMM+=bm.getBMMM();
	if(bm.getBMRM()!=0)
		bmRM+=bm.getBMRM();
	
    }
    
    private void setComp(int idComp,int pourcentage, int level)
    {
    	Hashtable<Integer,Integer> comp = pourcentagesComp.get(idComp);
    	if(comp==null)
    		pourcentagesComp.put(idComp, comp=new Hashtable<Integer,Integer>());
    	comp.put(level, pourcentage);
    }
    
    public String getBM()
    {
    	if(listeBM.size()==0) return "";
    	String s=listeBM.elementAt(0).toString();
	for(int i=1;i<listeBM.size();i++)
		s+="\n"+listeBM.elementAt(i).toString();
	return s;
    }
    
    public String getMouches()
    {
    	if(listeMouches.size()==0) return "";
    	String s=listeMouches.elementAt(0).toString();
	for(int i=1;i<listeMouches.size();i++)
		s+="\n"+listeMouches.elementAt(i).toString();
	return s;
    }
    
    public void addMouche(Mouche m)
    {
    	switch(m.getType())
	{
		case Mouche.CROBATE : bmmAttaque++;break;
		case Mouche.VERTIE : bmmEsquive++;break;
		case Mouche.LUNETTES : bmmVue++;break;
		case Mouche.MIEL : bmmRegeneration++;break;
		case Mouche.XIDANT : armure_mag++;break;
		case Mouche.RIVATANT : bmDLA-=20;break;
		case Mouche.HEROS : break;
		case Mouche.CARNATION : break;
		case Mouche.NABOLISANT : bmmDegat++;break;
		
	}
//	System.out.println("Mouche de type "+m.getType()+" ajoutée");
	listeMouches.add(m);
    }
    
    public void setTeam(int i)
    {
    	team=i;
    }
    
    public void setResu(int i)
    {
    	nbResu=i;
    }
    
    public int getResu()
    {
    	return nbResu;
    }

    public void setIcon(String i)
    {
    	icon=i;
    }
    
    public String getIcon()
    {
    	return icon;
    }


    public void addKill()
    {
    	kill++;
    }

    public int getNbKills()
    {
    	return kill;
    }

    public void addDeg(int i)
    {
    	deginflig+=i;
    }

    public int getDegInflig()
    {
    	return deginflig;
    }

    public void addPAUtil(int i)
    {
    	nbPA_utile+=i;
    }

    public int getPAUtil()
    {
    	return nbPA_utile;
    }

    public void addPVReg(int i)
    {
    	nbPVRegen+=i;
    }

    public int getPVRegen()
    {
    	return nbPVRegen;
    }
    
    private void removeBMs()
    {
    	for(int i=0;i<listeBM.size();i++)
	{
		BM bm=listeBM.elementAt(i);
		if(bm.newTurn())
		{
			listeBM.remove(i);
			i--;
			bmAttaque-=bm.getBMAttaque();
			bmmAttaque-=bm.getBMMAttaque();
			bmEsquive-=bm.getBMEsquive();
			bmmEsquive-=bm.getBMMEsquive();
			bmDegat-=bm.getBMDegat();
			bmmDegat-=bm.getBMMDegat();
			bmRegeneration-=bm.getBMRegeneration();
			bmmRegeneration-=bm.getBMMRegeneration();
			bmVue-=bm.getBMVue();
			bmmVue-=bm.getBMMVue();
			bmDLA-=bm.getBMDLA();
			armure_phy-=bm.getBMArmurePhysique();
			armure_mag-=bm.getBMArmureMagique();
			bmMM-=bm.getBMMM();
			bmRM-=bm.getBMRM();
			if(bm.isGlue())
				glue--;
			venin-=bm.getVenin();
			
		}
	}
    }
    
    private void removeAllBMs()
    {
    	for(int i=0;i<listeBM.size();i++)
	{
		BM bm=listeBM.elementAt(i);
		listeBM.remove(i);
		i--;
		bmAttaque-=bm.getBMAttaque();
		bmmAttaque-=bm.getBMMAttaque();
		bmEsquive-=bm.getBMEsquive();
		bmmEsquive-=bm.getBMMEsquive();
		bmDegat-=bm.getBMDegat();
		bmmDegat-=bm.getBMMDegat();
		bmRegeneration-=bm.getBMRegeneration();
		bmmRegeneration-=bm.getBMMRegeneration();
		bmVue-=bm.getBMVue();
		bmmVue-=bm.getBMMVue();
		bmDLA-=bm.getBMDLA();
		armure_phy-=bm.getBMArmurePhysique();
		armure_mag-=bm.getBMArmureMagique();
		bmMM-=bm.getBMMM();
		bmRM-=bm.getBMRM();
		if(bm.isGlue())
			glue--;
		venin-=bm.getVenin();
	}
    }   
    
    public void addEquipement(Equipement e)
    {
    	if(!listeEquipement.contains(e))
	{
		listeEquipement.add(e);
		poids+=e.getPoids();
	}
    }
    
    public void removeEquipement(Equipement e)
    {
    	if(listeEquipement.contains(e))
	{
		listeEquipement.remove(e);
		poids-=e.getPoids();
	}
    }
    
    private void bonusEquip(Equipement e,boolean equip)
    {
    	int coeff=1;
	if(!equip)
		coeff=-1;
	bmAttaque+=e.getBMAttaque()*coeff;
	bmmAttaque+=e.getBMMAttaque()*coeff;
	bmEsquive+=e.getBMEsquive()*coeff;
	bmmEsquive+=e.getBMMEsquive()*coeff;
	bmDegat+=e.getBMDegat()*coeff;
	bmmDegat+=e.getBMMDegat()*coeff;
	bmRegeneration+=e.getBMRegeneration()*coeff;
	bmmRegeneration+=e.getBMMRegeneration()*coeff;
	bmVue+=e.getBMVue()*coeff;
	bmmVue+=e.getBMMVue()*coeff;
	bmDLA+=e.getBMDLA()*coeff;
	armure_phy+=e.getBMArmurePhysique()*coeff;
	armure_mag+=e.getBMArmureMagique()*coeff;
	bmMM+=e.getBMMM()*coeff;
	bmRM+=e.getBMRM()*coeff;
    }

    public void setProfil(int niv,int att,int esq,int deg,int reg,int pv, int v,int dla,int resm, int maim)
    {
	niveau=niv;
	des_Attaque=att;
	des_Esquive=esq;
	des_Degat=deg;
	pv_totaux=pv_actuels=pv;
	des_Regeneration=reg;
	vue=v;
	duree_tour=dla;
	rm=resm;
	mm=maim;
    }
    
    public String getInfosTroll()
    {
        if(!isCreated)
		return "Error: Le troll n'a pas encore été validé !";
    	String r="Skrim";
	if(race==1)
		r="Durakuir";
	else if(race==2)
		r="Kastar";
	else if(race==3)
		r="Tomawak";
	r= ("Numéro      : "+id+"\n"+
		"Nom         : "+name+"\n"+
	        "Race        : "+r+"\n"+
	        "Niveau      : "+niveau+"\n"+
	        "Mouches     : "+listeMouches.size()+"\n"+
		formatEquipePorte(tete)+
		formatEquipePorte(cou)+
		formatEquipePorte(mainDroite)+
		formatEquipePorte(mainGauche)+
		formatEquipePorte(torse)+
		formatEquipePorte(pieds));
	return r.substring(0,r.length()-1);
    }

    private String formatEquipePorte(Equipement e)
    {
    	if(e==null)
		return "";
	switch(e.getType())
	{
		case Equipement.ARMURE: 
			return "Torse       : "+e.getId()+" "+e.getName()+"\n";
		case Equipement.BOUCLIER: 
			return "Main gauche : "+e.getId()+" "+e.getName()+"\n";
		case Equipement.CASQUE:
			return "Tête        : "+e.getId()+" "+e.getName()+"\n";
		case Equipement.ARME_1_MAIN: 
			return "Main droite : "+e.getId()+" "+e.getName()+"\n";
		case Equipement.TALISMAN: 
			return "Cou         : "+e.getId()+" "+e.getName()+"\n";
		case Equipement.BOTTES: 
			return "Pieds       : "+e.getId()+" "+e.getName()+"\n";
		case Equipement.ARME_2_MAINS:
			return "Deux mains  : "+e.getId()+" "+e.getName()+"\n";
	}
	return "";
    }
    
    public String getProfil()
    {
	String r="Skrim";
	if(race==1)
		r="Durakuir";
	else if(race==2)
		r="Kastar";
	else if(race==3)
		r="Tomawak";
	String returnstring="Nom : "+name+"\n"+
		"Niveau "+niveau+"\n"+
		"Race : "+r+"\n\n"+
		"Attaque : "+des_Attaque+"D6 + "+bmAttaque;
	returnstring+="/"+bmmAttaque;
	returnstring+="\n"+
		"Esquive : "+des_Esquive+"D6 + "+bmEsquive+"/"+bmmEsquive+"\n"+
		"Dégats : "+des_Degat+"D3 + "+bmDegat;
	returnstring+="/"+bmmDegat;
	returnstring+="\n"+
		"Régénération : "+des_Regeneration+"D3 + "+bmRegeneration+"/"+bmmRegeneration+"\n"+
		"Vue : "+vue+" cases + "+bmVue+"/"+bmmVue+"\n"+
		"PV : "+pv_actuels+"/"+pv_totaux+"\n\n"+
		"Durée DLA : "+convertTime(duree_tour)+"\n"+
		"Poids équipement : "+convertTime(poids)+"\n"+
		"Bonus/malus DLA : "+convertTime(bmDLA)+"\n\n"+
		"Armure : "+armure_phy+" + "+armure_mag+"\n"+
		"MM : "+mm+" + "+((mm*bmMM)/100)+"\n"+
		"RM : "+rm+" + "+((rm*bmRM)/100)+ "\n";
		
		returnstring += "Compétences : \n";
		
		Iterator<Integer> it = pourcentagesComp.keySet().iterator();
		while(it.hasNext())
		{
			int idComp = it.next();
			Iterator<Integer> it2 = pourcentagesComp.get(idComp).keySet().iterator();
			while(it2.hasNext())
			{
				int level = it2.next();
				int pour = pourcentagesComp.get(idComp).get(level);
				if(pour>0)
					returnstring += NOM_COMP[idComp-1] + " : " + pour + "% (Niveau "+level+")\n";
			}
		}
	returnstring += "Sorts : \n";
		for(int i=0;i<NB_SORT;i++) {
			if (pourcentagesSort[i] > 0) {
				returnstring += NOM_SORT[i-1] + " : " + pourcentagesSort[i] + "%\n";
			}
		}
	return returnstring.substring(0,returnstring.length()-1);				
    }
    
    public String getFullProfil()
    {
	String s=debut_tour+";"+duree_tour+";"+
		bmDLA+";"+
		(250*(pv_totaux-pv_actuels)/pv_totaux)+";"+
		poids+";"+
		posx+";"+
		posy+";"+
		posn+";"+
		vue+";"+
		formateCarac(bmVue)+"/"+formateCarac(bmmVue)+";"+
		niveau+";"+
		pv_actuels+";"+
		pv_totaux+";"+
		des_Regeneration+";"+
		formateCarac(bmRegeneration)+"/"+formateCarac(bmmRegeneration)+";"+
		fatigue+";"+
		des_Attaque+";";
		s+=formateCarac(bmAttaque)+"/"+formateCarac(bmmAttaque)+";";
	s+=des_Esquive+";"+
		formateCarac(bmEsquive)+"/"+formateCarac(bmmEsquive)+";"+
		des_Degat+";";
	s+=formateCarac(bmDegat)+"/"+formateCarac(bmmDegat)+";";
	s+=armure_phy+";"+
		armure_mag+";"+
		malus_des_esquive+";"+
		nbParade+";"+
		nbCA+";"+
		rm+";"+
		bmRM+";"+
		mm+";"+
		bmMM+";"+
		concentration+";";
	if(camoufle)
		s+="1;";
	else
		s+="0;";
	if(invisible)
		s+="1";
	else
		s+="0";
	return s;
    }

    public boolean addComp(int id,int pour,int level)
    {
	if(id<1 || id>NB_COMP)
		return false;
	if(id<=4 && id!=race+1)
		return false;
	if(pour>95)
		return false;
	if((id!=5 && id!=9) || level<1 || level>5)
		level = 1;
	setComp(id,pour,level);
	return true;
    }

    public boolean addSort(int id,int pour)
    {
	if(id<1 || id>NB_SORT)
		return false;
	if(id<=4 && id!=race+1)
		return false;
	if(pour>85)
		return false;
	pourcentagesSort[id]=pour;
	return true;
    }

    private int somme(int i)
    {
	return ((i+1)*i)/2;
    }

    public boolean valideTroll()
    {
	if(!verifNiveau())
		return false;
	isCreated=true;
	return true;
    }

    public boolean isCreated()
    {
        return isCreated;
    }
    
    private String formateCarac(int carac)
    {
    	if(carac>0)
    		return "+"+carac;
    	return ""+carac;
    }

    public boolean verifNiveau()
    {
		int px=0;
		if(race==RACE_SKRIM)
			px+=somme(des_Attaque-4)*12;
		else
			px+=somme(des_Attaque-3)*16;
		if(race==RACE_DURAKUIR)
			px+=somme((pv_totaux-40)/10)*12;
		else
			px+=somme((pv_totaux-30)/10)*16;
		if(race==RACE_KASTAR)
			px+=somme(des_Degat-4)*12;
		else
			px+=somme(des_Degat-3)*16;
		if(race==RACE_TOMAWAK)
			px+=somme(vue-4)*12;
		else
			px+=somme(vue-3)*16;
		px+=somme(des_Esquive-3)*16;
		px+=somme(des_Regeneration-1)*30;
		switch(duree_tour)
		{
			case 720:	break;
			case 690:	px+=1*18;break;
			case 663:	px+=3*18;break;
			case 639:	px+=6*18;break;
			case 618:	px+=10*18;break;
			case 600:	px+=15*18;break;
			case 585:	px+=21*18;break;
			case 573:	px+=28*18;break;
			case 564:	px+=36*18;break;
			case 558:	px+=45*18;break;
			case 555:	px+=55*18;break;
			default:	if(duree_tour>555)	return false;px+=(somme((555-duree_tour)*2/5)+55+10*((555-duree_tour)*2/5))*18;
		}
		Iterator<Integer> it = pourcentagesComp.keySet().iterator();
		while(it.hasNext())
		{
			int idComp = it.next();
			int level = 0;
			Iterator<Integer> it2 = pourcentagesComp.get(idComp).keySet().iterator();
			while(it2.hasNext())
			{
				int lev = it2.next();
				if(lev>level)
					level = lev;
			}
			for(int i=1;i<level;i++)
				if(pourcentagesComp.get(idComp).get(i)==null || pourcentagesComp.get(idComp).get(i) <75)
					return false;
			px+=coutPX[idComp]*((level*(level+1))/2);
		}
	//	if((niveau)*(niveau+3)*5<=px)
	//		System.out.println("Problème : Un niveau "+niveau+" a au max "+((niveau)*(niveau+3)*5)+" et vous en avez dépensé "+px);
		return (niveau)*(niveau+3)*5>px;
    }

    public void equipe(Equipement e)
    {
    	if(!listeEquipement.contains(e))
		return;
    	switch(e.getType())
	{
		case Equipement.ARMURE: 
			if(torse!=null)
				bonusEquip(torse,false);
			torse=e;
			bonusEquip(e,true);
			return;
		case Equipement.BOUCLIER: 
			if(mainGauche!=null)
				bonusEquip(mainGauche,false);
			if(mainDroite!=null && mainDroite.getType()==Equipement.ARME_2_MAINS)
			{
				bonusEquip(mainDroite,false);
				mainDroite=null;
			}
			mainGauche=e;
			bonusEquip(e,true);
			return;
		case Equipement.CASQUE:
			if(tete!=null)
				bonusEquip(tete,false);
			tete=e;
			bonusEquip(e,true);
			return;
		case Equipement.ARME_1_MAIN: 
			if(mainDroite!=null)
				bonusEquip(mainDroite,false);
			mainDroite=e;
			bonusEquip(e,true);
			return;
		case Equipement.TALISMAN: 
			if(cou!=null)
				bonusEquip(cou,false);
			cou=e;
			bonusEquip(e,true);
			return;
		case Equipement.BOTTES: 
			if(pieds!=null)
				bonusEquip(pieds,false);
			pieds=e;
			bonusEquip(e,true);
			return;
		case Equipement.ARME_2_MAINS: 
			if(mainDroite!=null)
				bonusEquip(mainDroite,false);
			if(mainGauche!=null)
			{
				bonusEquip(mainGauche,false);
				mainGauche=null;
			}
			mainDroite=e;
			bonusEquip(e,true);
			return;
	}
	return;
    }
    
    public Equipement getEquipementById(int id)
    {
    	for(int i=0;i<listeEquipement.size();i++)
	{
		if(listeEquipement.elementAt(i).getId()==id)
			return listeEquipement.elementAt(i);
	}
	return null;
    }
    
    public String getEquip()
    {
    	String s="";
    	for(int i=0;i<listeEquipement.size();i++)
	{
		int equip=0;
		Equipement e=listeEquipement.elementAt(i);
		if(e==torse || e==mainGauche || e==mainDroite || e==tete || e==cou || e==pieds)
			equip=1;
		s+=e.getId()+";"+e.getType()+";"+equip+";"+e.getDescr()+";"+e.getName()+"\n";
	}
	if(s.equals(""))
		return "Aucun équipement";
	return s.substring(0,s.length()-1);
    }
    
    public boolean isEquipe(Equipement e)
    {
    	switch(e.getType())
	{
    		case Equipement.ARMURE: 
			return torse==e;
		case Equipement.BOUCLIER: 
			return mainGauche==e;
		case Equipement.CASQUE:
			return tete==e;
		case Equipement.ARME_1_MAIN:
		case Equipement.ARME_2_MAINS:
			return mainDroite==e;
		case Equipement.TALISMAN: 
			return cou==e;
		case Equipement.BOTTES: 
			return pieds==e;
	}
	return false;
    }
    
    public int getBMAttaque()
    {
    	return bmAttaque;
    }

    public int getBMMAttaque()
    {
    	return bmmAttaque;
    }
    
    public int getBMEsquive()
    {
    	return bmEsquive;
    }
    
    public int getBMMEsquive()
    {
    	return bmmEsquive;
    } 

    public int getBMDegat()
    {
    	return bmDegat;
    }

    public int getBMMDegat()
    {
    	return bmmDegat;
    }

    public int getBMVue()
    {
    	return bmVue;
    }
    
    public int getBMMVue()
    {
    	return bmmVue;
    }
            
    public int getRM()
    {
    	return rm+((rm*bmRM)/100);
    }
    
    public int getMM()
    {
   	return mm+((mm*bmMM)/100);
    }
    
    public boolean isGlue()
    {
    	return glue>0;
    }
        
    public int getReussiteSort(int id)
    {
    	if(id<1 || id>NB_SORT)
		return 0;
	return pourcentagesSort[id];
    }
    
    public int getLevelComp(int id)
    {
    	Hashtable<Integer,Integer> h = pourcentagesComp.get(id);
    	if(h==null)
    		return 0;
    	int level = 0;
    	Iterator<Integer> it = h.keySet().iterator();
    	while(it.hasNext())
    	{
    		int lev = it.next();
    		if(lev>level)
    			level = lev;
    	}
    	return level;
    }

    public int getReussiteComp(int id,int level)
    {
    	if(id<1 || id>NB_COMP)
    		return 0;
    	Hashtable<Integer,Integer> h = pourcentagesComp.get(id);
    	if(h==null)
    		return 0;
    	Integer i = h.get(level);
    	if(i==null)
    		return 0;
    	return i;
    }
    
    public void augmentComp(int id, int level, int pour)
    {
        if(id<1 || id>NB_COMP)
        	return ;
        Hashtable<Integer,Integer> h = pourcentagesComp.get(id);
    	if(h==null)
    		return ;
    	Integer i = h.get(level);
    	if(i==null)
    		return ;
    	h.put(level, i+pour);
    }
    
    public void augmentSort(int id, int i)
    {
        if(id<1 || id>NB_SORT)
		return ;
	pourcentagesSort[id]+=i;
	if(pourcentagesSort[id]>80)
		pourcentagesSort[id]=80;
    }    
        
    public void setPos(int x,int y, int n)
    {
    	posx=x;posy=y;posn=n;
    }
    
    public int getPosX()
    {
    	return posx;
    }

    public int getPosY()
    {
    	return posy;
    }
    
    public int getPosN()
    {
    	return posn;
    }
        
    public void move(int x, int y, int n)
    {
    	posx+=x;posy+=y;posn+=n;
    }
    
    public String getPos()
    {
    	return posx+" "+posy+" "+posn;
    }
    
    public int getTeam()
    {
    	return team;
    }
    
    public void blesse(int i)
    {
    	pv_actuels-=i;
	//Je suis mort :) peut etre ?
    }
    public int getArmurePhy()
    { return armure_phy; }
    public int getArmureMag()
    { return armure_mag; }
    public int getAttaque()
    { return Math.max(0,des_Attaque-malus_des_attaque); }
    
    public boolean isDead()
    {
    	return (pv_actuels<=0);
    }
    
    public int getEsquive()
    { return Math.max(0,des_Esquive-malus_des_esquive); }  

    public int getEsquiveTotale()
    { return des_Esquive; }  
        
    public int getDegat()
    { return des_Degat; }
    
    public int getRegeneration()
    { return des_Regeneration; }
    
    public void getTouch()
    { malus_des_esquive++; }
    
    public void getParade()
    { malus_des_attaque++; malus_des_esquive++; }
    

    public String getName() {
        return name;
    }

    public int getNiveau() {
        return niveau;
    }

    public int getId() {
        return id;
    }
    
    public int getSocketId()
    {
        return idSocket;
    }

    public int getRace()
    {
        return race;
    }    

    public boolean compare(Troll t)
    {
       return ((t.getId()==id)||(t.getName().equals(name)));
    }
    
    public int getDateJeu()
    {
    	return date_jeu;
    }
    
    public int getNouveauTour()
    {
    	return debut_tour;
    }

    public void setNouveauTour(int i)
    {
    	debut_tour=i;
    }
    
    public int getFatigue()
    {
    	return fatigue;
    }
    
    public void setFatigue(int i)
    {
    	fatigue=i;
    }
        
    public void decale(int i)
    {
    	date_jeu+=i;
	isActive=false;
    }
    
    public int getPA()
    {
    	return pa;
    }
    
    public int getPV()
    {
	return pv_actuels;
    }

    public int getPVTotaux()
    {
        return pv_totaux;
    }
        
    public void setPV(int i)
    {
    	pv_actuels=i;
    }
    
    public void setPA(int i)
    {
    	pa=i;
    }

    public void setSortReserve(boolean b)
    {
    	sortReserve=b;
    }
    
    public boolean getSortReserve()
    {
    	return sortReserve;
    }

    public void setCertif(boolean b)
    {
    	isCertif=b;
    }
    
    public boolean isCertif()
    {
    	return isCertif;
    }

    public void setCompReservee(boolean b)
    {
    	compReservee=b;
    }
    
    public boolean getCompReservee()
    {
    	return compReservee;
    }

    public int getConcentration()
    {
    	return concentration;
    }
    
    public void setConcentration(int i)
    {
    	concentration=i;
    }
    
    public int getVue()
    {
    	return vue;
    }
    
    public void endTurn()
    {
    	date_jeu=debut_tour;
    	isActive=false;
    }
    
    public int getDureeTourTotale()
    {
	return dureeTourTotale;
    }
     
    public String beginTurn(int current)
    {
    	//Il faut régénérer
	//Regarder les bonus/malus encore en cours
	//Virer les poisons
	//Mouches ?
	//remettre l'esquive !
	//gérer la concentration
	//Calculer la nouvelle DLA
	//Remettre les PA.
	String s="";
	boolean modify=false;
	
	if(isDead() && nbResu>0)
	{
		nbResu--;
		s+="\nVous étiez mort mais vous revenez à la vie\nIl ne vous reste plus que "+nbResu+" résurrection(s)\n";
		if(MHAGame.getMode()==MHAGame.MODE_TEAM_DEATHMATCH && MHAGame.isRegroupe())
		{
			MHAGame.placeTrollInHisTeam(this);
		}
		else
			setPos(MHAGame.roll(1,MHAGame.getSizeArena())-1,MHAGame.roll(1,MHAGame.getSizeArena())-1,-MHAGame.roll(1,(MHAGame.getSizeArena()+1)/2));
		pv_actuels=(pv_totaux*50)/100;
		s+="Vous réapparaissez en X="+posx+" Y="+posy+" N="+posn+" avec seulement "+pv_actuels+"PV";
		removeAllBMs();
		pa=0;
		debut_tour=current;
	}
	else
	{
		while(debut_tour<=current)
		{
			if(poids+(250*(pv_totaux-pv_actuels)/pv_totaux)+bmDLA<0)
			{
				debut_tour+=duree_tour;
				dureeTourTotale=duree_tour;
			}
			else
			{
				debut_tour+=duree_tour+poids+(250*(pv_totaux-pv_actuels)/pv_totaux)+bmDLA;
				dureeTourTotale=duree_tour+poids+(250*(pv_totaux-pv_actuels)/pv_totaux)+bmDLA;
			}
		}
		removeBMs();
		s+="\nVotre tour de jeu et votre nouvelle date limite d'action ont été calculés.";
		if(pv_totaux-pv_actuels!=0 && (250*(pv_totaux-pv_actuels)/pv_totaux)+Math.min(0,poids+bmDLA)>0)
			s+="\nSa durée est augmentée de "+convertTime((250*(pv_totaux-pv_actuels)/pv_totaux)+Math.min(0,poids+bmDLA))+" à cause de vos blessures.";
		if(poids+bmDLA>0)
		{
			s+="\nSa durée est augmentée de "+convertTime(poids+bmDLA)+" à cause du poids de votre Equipement.";
		}
	}
	s+="\nVotre nouvelle date limite d'action est le "+hour2string(debut_tour);
	int i=Math.max(MHAGame.roll(des_Regeneration,3)+bmRegeneration+bmmRegeneration,0);
	int j=i-venin;
	if(pv_actuels<pv_totaux)
	{
		s+="\nVous aviez "+pv_actuels+" Points de Vie.";
		s+="\nVous avez régénéré de "+i+" Points de Vie.";
		nbPVRegen+=Math.min(pv_totaux-pv_actuels,i);
		modify=true;
	}
	pv_actuels=Math.min(pv_totaux,pv_actuels+j);
	if(venin>0)
	{
		s+="\nLe venin vous fait perdre "+venin+" Points de Vie.";
		if(pv_actuels<=0)
		{
			s+="\nVous êtes mort !!!!";
			return s.substring(1);
		}
		modify=true;
	}
	if(modify)
		s+="\nVotre total actuel est donc de "+pv_actuels+" Points de Vie.";
	//Le venin !!!!
	if(pa!=0)
	{
		s+="\n"+pa+" PA de votre tour précédent n'ont pas été utilisés et on été automatiquement attribués à une concentration.";
		concentration+=pa*5;
	}
	if(fatigue>0)
	{
		fatigue=(int) Math.floor( ((double) fatigue)/1.25);
		s+="\nVotre fatigue est de "+fatigue;
	}
	isActive=true;
	compReservee=false;
	sortReserve=false;
	frenetique=false;
	malus_des_esquive=0;
	malus_des_attaque=0;
	nbParade=0;
	nbCA=0;
	pa=6;
	return s.substring(1);
    }
    
    public boolean getCamouflage()
    {
    	return camoufle;
    }
    
    public void setCamouflage(boolean b)
    {
    	camoufle=b;
    }   
    
    public boolean getFrenetique()
    {
    	return frenetique;
    }
    
    public void setFrenetique(boolean b)
    {
    	frenetique=b;
    }   
    
    public boolean getInvisible()
    {
    	return invisible;
    }
    
    public void setInvisible(boolean b)
    {
    	invisible=b;
    }

    public int getNbCA()
    {
    	return nbCA;
    }
    
    public void setNbCA(int i)
    {
    	nbCA=i;
    }

    public int getNbParade()
    {
    	return nbParade;
    }
    
    public void setNbParade(int i)
    {
    	nbParade=i;
    }    
    
    public int getTempsRestant()
    {
    	return tempsRestant;
    }
    
    public void setTempsRestant(int i)
    {
    	tempsRestant=i;
    }
    public boolean isVisibleFrom(int x,int y, int n, int v)
    {
    	if(v<0)
    		v=0;
    	if(invisible || isDead())
		return false;
	if(camoufle)
		return x==posx && y==posy && n==posn && v>=0;
	return Math.abs(x-posx)<=v && Math.abs(y-posy)<=v && Math.abs(n-posn)<=(v+1)/2;
    }
    
    public static String convertTime(int t)
    {
    	if(t>=0)
    		return (t/60)+" heures et "+(t%60)+" minutes";
	else
		return "-"+(-t/60)+" heures et -"+((-t)%60)+" minutes";
    }
    
    public static String hour2string(int t)
    {
    	return "jour "+(t/(60*24)+1)+" à "+convertTime(t%(60*24));
    }
    
    public Vector<String> getInbox()
    {
    	return inbox;
    }
    

}
