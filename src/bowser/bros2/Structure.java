/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowser.bros2;

/**
 *
 * @author MichaelMiller
 */
public final class Structure 
{
    public static void addSteps(World w,Vector firstLoc,int length)
    {
        int x1 = firstLoc.x;
        int y1 = firstLoc.y;
        
        for (int i = 1; i <=length;i++)
            for (int j = y1; j>y1-i; j--)
                w.add(new Block(w,new Vector(x1+i-1,j),Block.INDESTRUCTIBLE,Block.UNBREAKABLE));
    }
}
