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
public class Fireball extends Movable
{
    private final Entity origin;
    
    public static final DVector STANDARD_SIZE = new DVector(Global.BLOCK_LEN*1.2,Global.BLOCK_LEN*.8);
    
    public Fireball(World w, DVector loc,Entity o,int dir)
    {
        super(w,(new DVector(STANDARD_SIZE)),loc);
        origin = o;
        direction = dir;
        if (direction==RIGHT)
            velocity.setX(Global.FIREBALL_SPEED);
        else if (direction==LEFT)
            velocity.setX(-Global.FIREBALL_SPEED);
        updateImageName();
        futureV = new DVector(velocity);
        interactable = true;
        gravity = false;
        staysAtTheEnd = false;
    }
    
    private void explode(int colType)
    {
        DVector loc = new DVector();
        double b = Global.BLOCK_LEN;
        double dx = size.getX()/4;
        switch (colType)
        {
            case RIGHT:loc = new DVector(rightX()-dx,topY());break;
            case LEFT:loc = new DVector(dx+leftX()-b,topY());break;
            case DOWN:loc = bottomLeft();break;
            case UP:loc = new DVector(leftX(),topY()-b);break;
        }
        world.entitiesToAddThisStep().add(new Explosion(world,new DVector(b,b),loc));
    }
    
    private boolean hittableEntity(Entity a)
    {
        return a instanceof Enemy||
               a instanceof Bowser||
               (a instanceof Block && !((Block)a).invisible());
    }

    @Override
    public void collideWith(Entity a, int colType) 
    { 
        if (!hittableEntity(a)||dead())
            return;
        
        //this solves a weird problem involving equality and rounding of doubles in java.
        if (basicallyEqual(a.topY(),bottomY()) ||
            basicallyEqual(a.bottomY(),topY()))
                return;

        die();//no matter what the fireball hits, it has to die.
        explode(colType);
        
        if (a==origin)//it cant kill its own source, that wouldnt make sense
            return;
        if (a instanceof Enemy)
            ((Enemy)a).dieFromBlockHit();
        else if (a instanceof Bowser)//since its not the source, bowser should take damage
            ((Bowser)a).takeDamage();
        else if (a instanceof Fireball)//fireballs should cancel each other out
            a.die();
    }

    @Override
    public void limitVelocity() 
    {
        //shouldn't do anything
    }

    @Override
    public void factorInControl() 
    {
        //no need
    }

    @Override
    public void finalUpdates() 
    {
        counter++;
    }

    @Override
    public void updateImageName() 
    {
        String dir;
        if (direction==LEFT)
            dir = "L";
        else dir = "R";
        
        int n;
        if (counter%10<5)
            n = 1;
        else n = 2;
        
        imageName = dir+"Fireball"+n;
    }

    @Override
    public boolean readyToBeRemoved() 
    {
        return dead()||!inRange();
    }
    
}
