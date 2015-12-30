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
public class Block extends Entity
{
    private int breakType;
    private int type;
    
    private final static String id = "Block";
    
    //types
    public static final int BRICK = 0;
    public static final int GROUND = 1;
    public static final int INVISIBLE = 2;
    public static final int PERMANENT = 3;
    public static final int QUESTION = 4;
    public static final int INDESTRUCTIBLE = 5;
    
    //breaktypes
    public static final int BREAKABLE = 0;
    public static final int UNBREAKABLE = -1;
    public static final int COIN = 1;
    public static final int MUSHROOM = 2;
    
    public Block(World w, Vector loc, int t, int b)
    {
        super(w,(new Vector(1,1)).convert(),loc);
        interactable = true;
        breakType = b;
        type = t;
        staysAtTheEnd = true;
        depth = MIDDLE;
        updateImageName();
    }
    
    public Block()
    {
        super();
    }
    
    private void updateImageName()
    {
        switch (type)
        {
            case BRICK:imageName = "BrickBlock";break;
            case GROUND:imageName = "GroundBlock";break;
            case PERMANENT:imageName = "PermanentBlock";break;
            case INVISIBLE:imageName = "";break;//
            case QUESTION:imageName = "QuestionBlock";break;
            case INDESTRUCTIBLE:imageName = "IndestructibleBlock";break;
        }
    }
    
    public int getType()
    {
        return type;
    }
    
    public boolean invisible()
    {
        return type==INVISIBLE;
    }
    
    public int getBreakType()
    {
        return breakType;
    }
    
    @Override
    public void step() 
    {
        //I don't think this should ever do anything
    }
    
    private ArrayList<Entity> getAboveEntities()
    {
        ArrayList<Entity> result = new ArrayList<>();
        for (Entity e: world.getInRangeEntities())
            if (e.on(this))
                result.add(e);
        return result;
    }
    
    private void drop()
    {
        switch (breakType)
        {
            case COIN:dropCoin();break;
            case MUSHROOM:dropMushroom(Mushroom.POWER);break;
        }
    }
    
    private void dropMushroom(int mushType)
    {
        double l = Global.BLOCK_LEN;
        DVector loc = new DVector(location.x,location.y-l);
        world.entitiesToAddThisStep().add(new Mushroom(world,loc,mushType));
    }
    
    private void dropCoin()
    {
        double l = Global.BLOCK_LEN;
        DVector loc = new DVector(location.x,location.y-l);
        world.entitiesToAddThisStep().add(new Coin(world,loc,this));
    }
    
    public boolean breakable()
    {
        return breakType==BREAKABLE;
    }
    
    public boolean hittable()
    {
        return breakType!=UNBREAKABLE;
    }
       
    public void hit()
    {
        //kills enemy standing on a hittable block
        if (hittable())
            for (Entity e: getAboveEntities())
                if (e instanceof WalkingEnemy)
                    ((WalkingEnemy)e).dieFromBlockHit();
        
        switch (breakType)
        {
            case BREAKABLE:die();break;
            case COIN:case MUSHROOM:
                type = PERMANENT;
                updateImageName();
                drop();
                breakType = UNBREAKABLE;
                break;
        }
    }
    
    @Override
    public boolean readyToBeRemoved()
    {
        return dead();
    }
    
    @Override
    public void die()
    {
        interactable = false;
        deadCount = counter;
    }
    
    public void throwFragments()
    {
        assert breakable();
        
        double x = location.x;
        double y = location.y;
        double r = Global.BLOCK_R;
        
        world.add(new BrickFragment(world,new DVector(x,y),LEFT,true));//topLeft
        world.add(new BrickFragment(world,new DVector(x+r,y),RIGHT,true));//topRight
        world.add(new BrickFragment(world,new DVector(x,y+r),LEFT,false));//bottomLeft
        world.add(new BrickFragment(world,new DVector(x+r,y+r),RIGHT,false));//bottomRight
    }
    
    public String id()
    {
        return id;
    }
}
