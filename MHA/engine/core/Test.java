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

public class  Test{

    private MHAGame MHAGameTest;
    private String toto="jetestecompare";

    public Test(){
        MHAGameTest=new MHAGame();
    }

    private void addtroll (){
        if(MHAGameTest.addTroll(new Troll("TOTO",12345,1,0,Troll.RACE_KASTAR))){System.out.println("création troll 1 ok");}
	MHAGameTest.getTrollById(12345).setProfil(50,3,9,18,4,80,6,600,50,30);
	for(int i=1;i<=14;i++)
		MHAGameTest.getTrollById(12345).addComp(i,90,1);
	for(int i=1;i<=44;i++)
		MHAGameTest.getTrollById(12345).addSort(i,80);
	if(MHAGameTest.getTrollById(12345).verifNiveau()){System.out.println("Upload du profil ok");}
        if(MHAGameTest.addTroll(new Troll("TUTU",23456,1,0,Troll.RACE_SKRIM))){System.out.println("création troll 2 ok");}
    }

    private void newTurn(){
        MHAGameTest.newTurn();
    }

    private void startgame(){
        MHAGameTest.startGame();
    }

    private void deplaceTrolls(Troll T) {
        if(T==MHAGameTest.getTrollById(12345))
            T.setPos(MHAGameTest.getTrollById(23456).getPosX(),MHAGameTest.getTrollById(23456).getPosY(),MHAGameTest.getTrollById(23456).getPosN());
        else
            T.setPos(MHAGameTest.getTrollById(12345).getPosX(),MHAGameTest.getTrollById(12345).getPosY(),MHAGameTest.getTrollById(12345).getPosN());
    }

    
    private void attaque(boolean aMort){
        Troll T1=MHAGameTest.getTrollById(12345);
        Troll T2=MHAGameTest.getTrollById(23456);
        T1.addComp(Troll.COMP_LDP, 90,1);
        T2.addComp(Troll.COMP_LDP, 90,1);
        T1.setPV(100);
        T2.setPV(100);
        Equipement e1 = new Equipement(123456,"potion zone",Equipement.POTION,1,2,3,4,5,6,7,8,9,123,456,true,false,5);
        Equipement e2 = new Equipement(123456,"potion zone",Equipement.POTION,9,8,7,6,5,4,3,2,1,123,456,false,false,5);
        T1.addEquipement(e1);
        T2.addEquipement(e2);
        
        if (aMort) {
        	System.out.println("A MORT");
            while (!T1.isDead() && !T2.isDead()) {
                if (MHAGameTest.getCurrentTroll() == T1) {
                    T1.beginTurn(MHAGameTest.getTime());
                    System.out.println(MHAGameTest.lancerDePotion(T2,e1) + "\n");
                    //deplaceTrolls(T2);
				    T1.endTurn();
				    MHAGameTest.newTurn();
                } 
                else {
                    T2.beginTurn(MHAGameTest.getTime());
                    System.out.println(MHAGameTest.lancerDePotion(T1,e2) + "\n");
                    //deplaceTrolls(T1);
                    T2.endTurn();
                    MHAGameTest.newTurn();
                }
            }
        }
        else {
        	System.out.println("PAS A MORT");
            if (MHAGameTest.getCurrentTroll() == T1) {
            	System.out.println("Troll 1");
                T1.setPA(6);
                System.out.println(MHAGameTest.lancerDePotion(T2,e1) + "\n");
            } 
            else {
            	System.out.println("Troll 2 ");
                T2.setPA(6);
                System.out.println(MHAGameTest.lancerDePotion(T1,e2) + "\n");
            }
        }
    }

    public static void main(String[] args) {
        Test instTest=new Test();
        instTest.addtroll();
        System.out.println(instTest.MHAGameTest.getTrollById(12345).getPos());
        System.out.println(instTest.MHAGameTest.getTrollById(23456).getPos());
        instTest.startgame();
        instTest.newTurn();
        //instTest.deplaceTrolls(instTest.MHAGameTest.getCurrentTroll());
        System.out.println(instTest.MHAGameTest.getTrollById(12345).getPos());
        System.out.println(instTest.MHAGameTest.getTrollById(23456).getPos());
        instTest.attaque(false);
        System.out.println(instTest.MHAGameTest.getTrollById(12345).getPos());
        System.out.println(instTest.MHAGameTest.getTrollById(23456).getPos());
    }

}
