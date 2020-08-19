package mha.engine.dice;

import java.util.Random;

/**
 * Helper to manage dice treatments.
 *
 * @author ahsitasqua
 */
public class DiceHelper {

	private static Random r = new Random();

	/**
	 * Roll dices
	 * 
	 * @param nbr
	 *            the number of dices
	 * @param faces
	 *            the number of faces of dices
	 * @return the result of dice roll
	 */
	public int roll(int nbr,int faces)
	{
		int i=0;
		int n=Math.abs(nbr);
		for(int j=0;j<n;j++)
			i+=r.nextInt(faces)+1;
		if(nbr<0)
			return -i;
		return i;
	}
}
