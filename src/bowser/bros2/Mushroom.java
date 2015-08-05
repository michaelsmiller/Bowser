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
public class Mushroom extends Movable
{
    private final int type;
    
    public static final int LIFE=0;
    public static final int POWER=1;

    public Mushroom(World w, Vector loc,int t)
    {
        super(w,(new Vector(1,1)).convert(),loc);
        direction = RIGHT;
        velocity.x = Global.MUSHROOM_SPEED;
        futureV = new DVector(velocity);
        type=t;
        if (type==LIFE)
            imageName = "LifeMushroom";
        else if (type==POWER)
            imageName = "PowerMushroom";
        gravity = true;
        interactable = true;
        staysAtTheEnd = false;
    }
    
    public Mushroom(World w, DVector loc, int t)
    {
        this(w,loc.convert(),t);
    }
    
    public int getType()
    {
        return type;
    }
    
    @Override
    public void collideWith(Entity a, int colType) 
    {
        if (a instanceof Block)
        {
            collideWithBlock(a,colType);
            switch (colType)
            {
                case LEFT:direction = RIGHT;break;
                case RIGHT:direction = LEFT;break;
            }
        }
        //System.out.println(velocity.x);
    }

    @Override
    public void limitVelocity() 
    {
        if (velocity.y>Global.MAX_FALLING_SPEED)
            velocity.y = Global.MAX_FALLING_SPEED;
    }

    @Override
    public void factorInControl() 
    {
        switch(direction)
        {
            case RIGHT:velocity.x = Global.MUSHROOM_SPEED;
            case LEFT:velocity.x = -Global.MUSHROOM_SPEED;
        }
    }

    @Override
    public void finalUpdates() 
    {
        updateOnGround();
        counter++;
    }

    @Override
    public void updateImageName() 
    {
        //no need
    }

    @Override
    public boolean readyToBeRemoved() 
    {
        return dead();
    }
}
