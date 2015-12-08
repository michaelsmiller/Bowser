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
public class Obstacle extends Block
{
    public static final int INVISIBLE_WALL = 1;
    
    private static final String id = "Obstacle";

    public Obstacle(World w, Vector loc,DVector s,int whatItIs) 
    {
        super(w, loc, PERMANENT, UNBREAKABLE);
        size = s;
        imageName = typeToImage(whatItIs);
    }
    
    public Obstacle(World w, Vector loc,Vector s,int whatItIs) 
    {
        this(w,loc,s.convert(),whatItIs);
    }
    
    private static String typeToImage(int t)
    {
        switch(t)
        {
            case INVISIBLE_WALL:return "";
        }
        throw new Error("Did you misspell something?");
    }
}
