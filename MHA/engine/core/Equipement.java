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

import java.io.*;

public class Equipement {

    private String name;
    private int id;
    private int type;
    private int bmAttaque;
    private int bmmAttaque=0;
    private int bmEsquive;
    private int bmmEsquive;
    private int bmDegat;
    private int bmmDegat=0;
    private int bmDLA;
    private int bmRegeneration;
    private int bmmRegeneration;
    private int bmPV;
    private int bmVue;
    private int bmmVue;
    private int bmArmurePhysique;
    private int bmArmureMagique;
    private int bmMM;
    private int bmRM;
    private boolean zone;
    private boolean droppable;    
    private int poids;
    
    public final static int ARMURE=0;
    public final static int BOUCLIER=1;
    public final static int CASQUE=2;
    public final static int ARME_1_MAIN=3;
    public final static int TALISMAN=4;
    public final static int BOTTES=5;
    public final static int BIDOUILLE=6;
    public final static int ANNEAU=7;
    public final static int BRIC_A_BRAC=8;
    public final static int ARME_2_MAINS=9;
    public final static int COMPOSANT=10;
    public final static int PARCHEMIN=11;
    public final static int POTION=12;
    public final static int TAROT=13;
    public final static int CHAMPIGNON=14;
    public final static int MINERAI=15;

    public Equipement(int i,String n,int t, int a,int e,int d,int dla,int r,int vue,int pv,int ap,int am,int bmm, int brm, boolean g,boolean dr,int p) {
    	id=i;
	name = n;
	type=t;
	bmAttaque=a;
	bmEsquive=e;
	bmDegat=d;
	bmDLA=dla;
	bmRegeneration=r;
	bmPV=pv;
	bmVue=vue;
	bmArmurePhysique=ap;
	bmArmureMagique=am;;
	zone=g;
	droppable=dr;
	poids=p;
	bmMM=bmm;
	bmRM=brm;
    }

    public Equipement(int i,String n,int t, int a,int bam,int e,int d,int dm,int dla,int r,int vue,int pv,int ap,int am,int bmm, int brm, boolean g,boolean dr,int p) {
    	this(i,n,t,a,e,d,dla,r,vue,pv,ap,am,bmm,brm,g,dr,p);
	bmmAttaque=bam;
	bmmDegat=dm;
    }
    
    public Equipement(int i,String n,int t, int a,int bam,int e,int em,int d,int dm,int dla,int r,int rem,int vue,int vuem,int pv,int ap,int am,int bmm, int brm, boolean g,boolean dr,int p) {
    	this(i,n,t,a,e,d,dla,r,vue,pv,ap,am,bmm,brm,g,dr,p);
	bmmAttaque=bam;
	bmmDegat=dm;
	bmmEsquive=em;
	bmmRegeneration=rem;
	bmmVue=vuem;
    }
    
    private String formate(String s,int v)
    {
	if(v>0)
		return s+" +"+v+", ";
	if(v<0)
		return s+" "+v+", ";
	return "";
    }
    
    private String formate(String s,int v1,int v2)
    {
    	if(v2==0)
    		return formate(s,v1);
    	String result = s;
    	if(v1>0)
    		result+="+";
    	result+=v1+"/";
    	if(v2>0)
    		result+="+";
    	result+=v2+", ";
    	return result;
    }
    
    private String formate(String s,int v1,int v2,String s1)
    {
    	if(v2==0)
    		return formate(s,v1,s1);
    	String result = s;
    	if(v1>0)
    		result+="+";
    	result+=v1+"/";
    	if(v2>0)
    		result+="+";
    	result+=v2+s1;
    	return result;
    }
    
    private String formate(String s,int v,String s1)
    {
	if(v>0)
		return s+"+"+v+s1;
	if(v<0)
		return s+v+s1;
	return "";
    }
    
    private String formate(String s,boolean b,String s1)
    {
    	if(b)
		return s+s1;
	else
		return "";
    }

    
        
    public static String formateType(int i)
    {
    	switch(i)
	{
		case ARMURE: return "Armure";
		case BOUCLIER: return "Bouclier";
		case CASQUE: return "Casque";
		case ARME_1_MAIN: return "Arme (une main)";
		case TALISMAN: return "Talisman";
		case BOTTES: return "Bottes";
		case BIDOUILLE: return "Bidouille";
		case ANNEAU: return "Anneau";
		case BRIC_A_BRAC: return "Bric à brac";
		case ARME_2_MAINS: return "Arme (deux mains)";
		case COMPOSANT: return "Composant";
		case PARCHEMIN: return "Parchemin";
		case POTION: return "Potion";
		case TAROT: return "Tarot";
		case CHAMPIGNON: return "Champignon";
		case MINERAI: return "Minerai";
	}
	return "Inconnu";
    }
    
    public String toString()
    {
    	if(type==POTION || type==PARCHEMIN)
	{
		return name+" ("+formateType(type)+") : " + formate("Att",bmAttaque,"D3, ") + formate("Esq",bmEsquive,"D3, ") + formate("Deg",bmDegat) + formate("DLA",bmDLA," min, ") + formate("Reg",bmRegeneration) + formate("PV",bmPV,"D3, ") + formate("Arm",bmArmurePhysique+bmArmureMagique) + formate("Vue",bmVue) + formate("MM",bmMM,"%, ") + formate("RM",bmRM,"%, ")+ formate("Effet de zone, ",zone,", ")+"Poids: "+poids+" min";
	}
    	return name+" ("+formateType(type)+") : " + formate("Att",bmAttaque,bmmAttaque) + formate("Esq",bmEsquive,bmmEsquive) + formate("Deg",bmDegat,bmmDegat) + formate("DLA",bmDLA) + formate("Reg",bmRegeneration) + formate("PV",bmPV) + formate("Arm",bmArmurePhysique+bmArmureMagique) + formate("Vue",bmVue) + formate("MM",bmMM) + formate("RM",bmRM)+"Poids: "+poids+" min";
    }
    
    public String getDescr()
    {
    	if(type==POTION || type==PARCHEMIN)
    	{
    		return formate("Att ",bmAttaque,"D3 | ") + formate("Esq ",bmEsquive,"D3 | ") + formate("Deg ",bmDegat," | ") + formate("DLA ",bmDLA," min | ") + formate("Reg ",bmRegeneration," | ") + formate("PV ",bmPV,"D3 | ") + formate("Arm ",bmArmurePhysique+bmArmureMagique," | ") + formate("Vue ",bmVue," | ") + formate("MM" ,bmMM,"% | ") + formate("RM ",bmRM,"% | ")+ formate("Effet de zone",zone," | ")+"Poids "+poids+" min";
    	}
    	return formate("Att ",bmAttaque,bmmAttaque," | ") + formate("Esq ",bmEsquive, bmmEsquive," | ") + formate("Deg ",bmDegat,bmmDegat," | ") + formate("DLA ",bmDLA," min | ") + formate("Reg ",bmRegeneration,bmmRegeneration," | ") + formate("PV ",bmPV," | ") + formate("Arm ",bmArmurePhysique+bmArmureMagique," | ") + formate("Vue ",bmVue,bmmVue," | ") + formate("MM ",bmMM,"% | ") + formate("RM ",bmRM,"% | ")+"Poids "+poids+" min";
    }
    
    public String getName()
    {
    	return name;
    }
    
    public int getId()
    {
    	return id;
    }
    
    public int getType()
    {
    	return type;
    }
    
    public int getPoids()
    {
    	return poids;
    }    
    
    public boolean isZone()
    {
    	return zone;
    }

    public boolean isBidouille()
    {
    	return droppable;
    }
    
    public int getBMAttaque()
    {
    	return bmAttaque;
    }

    public int getBMMAttaque()
    {
    	return bmmAttaque;
    }
    
    public boolean isDroppable()
    {
    	return droppable;
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
    
    public int getBMDLA()
    {
    	return bmDLA;
    }
    
    public int getBMRegeneration()
    {
    	return bmRegeneration;
    }
    
    public int getBMMRegeneration()
    {
    	return bmmRegeneration;
    }
    
    public int getPV()
    {
    	return bmPV;
    }
    
    public int getBMVue()
    {
    	return bmVue;
    }
    
    public int getBMMVue()
    {
    	return bmVue;
    }
    
    public int getBMArmurePhysique()
    {
    	return bmArmurePhysique;
    }
    
    public int getBMArmureMagique()
    {
    	return bmArmureMagique;
    }
    
    public int getBMMM()
    {
    	return bmMM;
    }
    
    public int getBMRM()
    {
    	return bmRM;
    }
}
