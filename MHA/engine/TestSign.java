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

import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.*;

public class TestSign
{

public static boolean verifSignature(String message,String signature)
{
	try
	{
		BigInteger modulo=new BigInteger(1,Base64.decode("APFbzo28aj0laYgu6jpt7MVHGRYi/URzT+h+L2poQSTIGg/tLZU4/Ib2a3NvPh/Oe4AjsHf7TAdQ7dFtOzX4Uvvbyr2kdi2GD0zSYxtDulhhBgQ9coUWZymXKyvx8zK2DIes+j7oHZM2znzu64fj3DYv2Kut98t6CgtqZMrXhNCb"));
		BigInteger exposant=new BigInteger(1,Base64.decode("AQAB"));

		byte [] hash = MessageDigest.getInstance("SHA").digest(message.getBytes());
		BigInteger hash2=new BigInteger(1,Base64.decode(signature)).modPow(exposant, modulo);
		return hash2.equals(new BigInteger(1,hash));
	}
	catch(NoSuchAlgorithmException e)
	{
		System.err.println("Avertissement : vous ne disposez pas de bibliothèque permettant de vérifier l'authenticité des profils");
		return false;
	}
}

public static void main(String [] args)
{
	try
	{
		if(verifSignature(
			"Ceci est un message à signer ! Si si",
			"AL1yitwZ5IThL1lofnNk+8zTdtmLoHkl3207sPFjCLvrpvaVr4QbqkeyZgqctyo36YzpZSPQC7CvfBaRtFVu1wCVvL91EBRhuKIXNaJEbJQu6q3iz5kVKLIiUUhYObxOjvlz1Bz4J5VH1xX2LkVtvWnU61NDx0nPMREJqM4bmuTL"))
			System.out.println("Signature valide");
		else
			System.out.println("Signature invalide");
		//genereCle();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}

public static void genereCle()
{
	try
	{
		KeyPairGenerator kg=KeyPairGenerator.getInstance("RSA");
		kg.initialize(1024);
		KeyPair kp=kg.genKeyPair();
		System.out.println("Modulo : "+Base64.encodeBytes(((RSAPublicKey) kp.getPublic()).getModulus().toByteArray()));
		System.out.println("Exposant Public : "+Base64.encodeBytes(((RSAPublicKey) kp.getPublic()).getPublicExponent().toByteArray()));
		System.out.println("Exposant Privé : "+Base64.encodeBytes(((RSAPrivateKey) kp.getPrivate()).getPrivateExponent().toByteArray()));
	}
	catch(NoSuchAlgorithmException e)
	{
		System.err.println("Avertissement : vous ne disposez pas de bibliothèque permettant de vérifier l'authenticité des profils");
	}
}

}