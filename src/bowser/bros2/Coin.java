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
public final class Coin extends Movable
{
    private final Block origin;
    
    private static final String id = "Coin";
    
    public Coin(World w, Vector loc, Vector originLoc)
    {
        this(w,loc,w.getBlockAt(originLoc.convert()));
    }
    
    public Coin(World w,DVector loc,Block or)
    {
        super(w,new DVector(),new Vector());
        location = loc;
        interactable=true;
        onGround=false;
        origin = or;
        staysAtTheEnd = true;//do stuff with this!!!!!!!!!!
        depth = FRONT;
        
        double len = Global.BLOCK_LEN;
        if (hasOrigin())
        {
            size = new DVector(len,len);
            gravity=true;
            velocity=new DVector(0,-Global.JUMPING_SPEED*.8);
            futureV = new DVector(velocity);
        }
        else//a fixed coin
        {
            gravity=false;
            size = new DVector(len,len);
        }
        imageName = "Coin1";
    }
    
    public Coin(World w,Vector loc,Block or)
    {
        this(w,loc.convert(),or);
    }
    
    public boolean hasOrigin()
    {
        return origin!=null;
    }
    
    public boolean collectable()
    {
        return origin==null;
    }
    
    public void collect()
    {
        interactable = false;
        gravity = false;
        die();
        world.incrementScore();
    }

    @Override
    public void collideWith(Entity a, int colType) 
    {
        if (a==origin&&colType==DOWN)
        {
            collect();
        }
    }

    @Override
    public void limitVelocity() 
    {
        //nothing
    }

    @Override
    public void factorInControl() 
    {
        //nothing
    }

    @Override
    public void finalUpdates() 
    {
        counter++;
    }

    @Override
    public void updateImageName() 
    {
        if (collectable())
        {
            int n = (counter%144/12)+1;
            imageName = "Coin"+n;
        }
        else
        {
            int n = (counter%20/5)+1;
            imageName = "FlyingCoin"+n;
        }
    }

    @Override
    public boolean readyToBeRemoved() 
    {
        return dead();
    }
    
    @Override
    public String id()
    {
        return id;
    }
}
