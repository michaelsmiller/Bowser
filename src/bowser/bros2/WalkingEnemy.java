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
public abstract class WalkingEnemy extends Movable implements Enemy
{
    protected boolean dying;//for when its been killed but in the falling down animation

    public WalkingEnemy(World w, DVector s, Vector loc) 
    {
        super(w, s, loc);
        direction = RIGHT;
        interactable = true;
        gravity = true;
        staysAtTheEnd = false;
        dying = false;
        //don't forget imageName
    }

    @Override
    public void collideWith(Entity a, int colType) 
    {
        if (!interactable)//being in the falling animation after being hit by the block
            return;
        
        if (a instanceof Block||a instanceof WalkingEnemy||a instanceof Bowser)
            collideWithBlock(a,colType);
        
        if (a instanceof WalkingEnemy && !(a instanceof Koopa))
            ((Movable)a).collideWithBlock(this,reverseCollision(colType));
    }
    
    @Override
    protected void collideWithBlock(Entity block, int colType)
    {
        super.collideWithBlock(block, colType);
        switch (colType)
        {
            case LEFT:direction = RIGHT;break;
            case RIGHT:direction = LEFT;break;
            
        }
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
        double vx = Global.GOOMBA_SPEED;
        if (direction==RIGHT)
            velocity.x = vx;
        else if (direction==LEFT)
            velocity.x = -vx;
        else //direction==STILL
            velocity.x = 0;
    }

    @Override
    public void finalUpdates() 
    {
        updateOnGround();
        counter++;
    }
    
    @Override
    public void die()
    {
        super.die();
        if (dead())
            interactable = false;
    }
    
    public void dieFromBlockHit()
    {
        interactable = false;
        onGround = false;
        velocity = new DVector(0,-Global.JUMPING_SPEED/2);
        futureV = new DVector(velocity);
        direction = STILL;
        dying = true;
    }

    @Override
    public boolean readyToBeRemoved()
    {
        if (dead())//dead the normal way, through being jumped on
            return counter-deadCount>=Global.GOOMBA_DEATH_DELAY;
        else return !world.inYRange(this);//removed when it passes the bottom of the screen
    }
    
    @Override
    public boolean dying()
    {
        return dying;
    }
    
    public abstract void getStomped();
}