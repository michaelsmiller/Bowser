/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowser.bros2;

import java.util.ArrayList;

/**
 *
 * @author MichaelMiller
 */
public class Castle extends Nonentity
{
    private final int type;
    private final ArrayList<DVector> fireLocations;
    private int fireCount;
    
    public static final int LARGE = 0;
    public static final int SMALL = 1;
    
    protected static final DVector SMALL_SIZE = new Vector(5,5).convert();
    protected static final DVector LARGE_SIZE = new DVector();
    
    public Castle(World w, Vector loc, int t) 
    {
        super(w, getSize(t), loc.convert());
        type = t;
        
        switch (type)
        {
            case LARGE:imageName = "LargeCastle";break;
            case SMALL:imageName = "SmallCastle";break;
        }
        fireLocations = fireLocations();
        fireCount = -1;
    }
    
    private static DVector getSize(int type)
    {
        switch (type)
        {
            case LARGE:return LARGE_SIZE;
            case SMALL:return SMALL_SIZE;
        }
        throw new Error("Why you messing up?! Ain't nobody got time for that");
    }
    
    protected final ArrayList<DVector> fireLocations()
    {
        double x = location.x;
        double y = location.y;
        
        ArrayList<DVector> locs = new ArrayList<>();
        
        double d = Global.BLOCK_LEN;
        locs.add(new DVector(x-d,y-2*d));
        if (type==SMALL)
        {
            locs.add(new DVector(x,y+2*d));
            locs.add(new DVector(x+3*d,y+3*d));
            locs.add(new DVector(x+3*d,y));
            locs.add(new DVector(x,y+4*d));
            locs.add(new DVector(x+d,y));
        }
        else //large. Haven't implemented yet
        {
            //locs.add(new DVector(x,y));
        }
        
        return locs;
    }
    
    private void addFire()
    {
        double d = Global.BLOCK_LEN;
        if (fireLocations.isEmpty())
            return;
        else if (fireLocations.size()==1)
        {
            DVector loc = fireLocations.remove(0);
            world.nonentitiesToAddThisStep().add(new Fire(world,
                                                new DVector(size.x = +2*d,size.y = +2*d),loc));
            return;
        }
        
        DVector loc = fireLocations.remove(fireLocations.size()-1);
        world.nonentitiesToAddThisStep().add(new Fire(world,new Vector(1,1).convert(),loc));
    }

    @Override
    protected void updateImageName() 
    {
        //should do nothing.
    }

    @Override
    protected void finalUpdates() 
    {
        counter++;
        
        //deals with fire
        if (between(world.getBowser().rightX(),leftX(),rightX())&&fireCount==-1)
            fireCount = counter;
        if (fireCount!=-1)
        {
            int count = counter-fireCount;
            if (count%Global.FIRE_DELAY==0)
                addFire();
        }
    }

    @Override
    public boolean readyToBeRemoved() 
    {
        return false;
    }
}
