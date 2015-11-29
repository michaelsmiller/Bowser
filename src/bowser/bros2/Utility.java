/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowser.bros2;

import java.util.HashMap;

/**
 *
 * @author MichaelMiller
 */
public final class Utility 
{
    //public static final HashMap<String,String> = new HashMap<>();
    
    public static final Class[] classes = {Bowser.class,Block.class,Coin.class,Goomba.class,Koopa.class};
    
    public static HashMap<String,Class<? extends Entity>> createMap()
    {
        HashMap<String,Class<? extends Entity>> map = new HashMap<>();
        for (Class c: classes)
        {
            map.put(c.getName(), c);
        }
        return map;
    }
}
