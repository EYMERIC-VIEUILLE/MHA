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

public class Mouche {

    private String name;
    private int type;
    
    public final static int CROBATE     = 0;
    public final static int VERTIE   = 1;
    public final static int LUNETTES        = 2;
    public final static int MIEL = 3;
    public final static int XIDANT = 4;
    public final static int RIVATANT = 5;
    public final static int HEROS = 6;
    public final static int CARNATION = 7;
    public final static int NABOLISANT = 8;
    
    private int duree;

    public Mouche(String n, int t) {
	name=n;
	type=t;
    }
    
    public int getType()
    {
    	return type;
    }
    
    public String getName()
    {
    	return name;
    }
 
    
    public String toString()
    {
    	switch(type)
	{
		case CROBATE : return name+" (Attaque : +1) Crobate";
		case VERTIE : return name+" (Esquive : +1) Vertie";
		case LUNETTES : return name+" (Vue : +1) Lunettes";
		case MIEL : return name+" (Régénération : +1) Miel";
		case XIDANT : return name+" (Armure : +1) Xidant";
		case RIVATANT : return name+" (Tour : -20 min) Rivatant";
		case HEROS : return name+" () Héros";
		case CARNATION : return name+" () Carnation";
		case NABOLISANT : return name+" (Dégâts : +1) Nabolisant";
		
	}
    	return name;
    }
}
