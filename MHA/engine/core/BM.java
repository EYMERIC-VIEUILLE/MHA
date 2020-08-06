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

public class BM {

    private String name;
    private int bmAttaque;
    private int bmmAttaque=0;
    private int bmEsquive;
    private int bmmEsquive=0;
    private int bmDegat;
    private int bmmDegat=0;
    private int bmDLA;
    private int bmRegeneration;
    private int bmmRegeneration=0;
    private int bmVenin;
    private int bmVue;
    private int bmmVue=0;
    private int bmArmurePhysique;
    private int bmArmureMagique;
    private int bmMM;
    private int bmRM;
    private boolean glue;
    
    private int duree;

    public BM(String n, int a,int e,int d,int dla,int r,int vue,int venin,int ap,int am,int mm, int rm, boolean g,int dur) {
	name = n;
	bmAttaque=a;
	bmEsquive=e;
	bmDegat=d;
	bmDLA=dla;
	bmRegeneration=r;
	bmVenin=venin;
	bmVue=vue;
	bmArmurePhysique=ap;
	bmArmureMagique=am;;
	glue=g;
	duree=dur;
	bmMM=mm;
	bmRM=rm;
    }

    public BM(String n, int a,int bam, int e,int d, int dm, int dla,int r,int vue,int venin,int ap,int am,int mm, int rm, boolean g,int dur) {
    	this(n,a,e,d,dla,r,vue,venin,ap,am,mm,rm,g,dur);
	bmmDegat=dm;
	bmmAttaque=bam;
    }
    
    public BM(String n, int a,int bam, int e, int em,int d, int dm, int dla,int r,int regm,int vue, int vuem,int venin,int ap,int am,int mm, int rm, boolean g,int dur) {
    	this(n,a,e,d,dla,r,vue,venin,ap,am,mm,rm,g,dur);
	bmmDegat=dm;
	bmmAttaque=bam;
	bmmEsquive=em;
	bmmRegeneration=regm;
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
    	if(v1==0 && v2==0)
    		return "";
    	String s1=s;
    	if(v1>=0)
    		s1+=" +"+v1;
    	else
    		s1+=" "+v1;
    	if(v2>=0)
    		s1+="/+"+v2;
    	else
    		s1+="/"+v2;
    	return s1;
    }
    
    private String formate(String s,int v,String s1)
    {
	if(v>0)
		return s+" +"+v+s1;
	if(v<0)
		return s+" "+v+s1;
	return "";
    }

    public String toString()
    {
    	return name+" : " + formate("Att",bmAttaque,bmmAttaque) + formate("Esq",bmEsquive,bmmEsquive) + formate("Deg",bmDegat,bmmDegat) + formate("Vue",bmVue,bmmVue) + formate("DLA",bmDLA) + formate("Reg",bmRegeneration,bmmRegeneration) + formate("Venin",bmVenin) + formate("Arm",bmArmurePhysique+bmArmureMagique) + formate("MM",bmMM,"%, ") + formate("RM",bmRM,"%, ") + ", Durée "+duree+" tour(s)";
    }
    
    public String getName()
    {
    	return name;
    }
    
    public int getDuree()
    {
    	return duree;
    }
    
    public boolean newTurn()
    {
    	if(duree<=0)
		return true;
	duree--;
	return false;
    }
    
    public boolean isGlue()
    {
    	return glue;
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
    
    public int getVenin()
    {
    	return bmVenin;
    }
    
    public int getBMVue()
    {
    	return bmVue;
    }
    
    public int getBMMVue()
    {
    	return bmmVue;
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
