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
public class BrickFragment extends Movable
{
    private static final String id = "BrickFragment";
    
    public BrickFragment(World w, DVector loc, int dir,boolean top)
    {
        super(w,new DVector(Global.BLOCK_R,Global.BLOCK_R),new Vector());
        location = loc;
        direction = dir;
        interactable = false;
        gravity = true;
        onGround = false;
        staysAtTheEnd = false;
        
        assert dir==LEFT||dir==RIGHT;
        if (dir==LEFT)
            imageName="LBrickFragment";
        else imageName = "RBrickFragment";
        
        double vx = 1.5;
        double vy;
        if (!top)
            vy=-vx;
        else vy=-vx*4;
        
        if (dir==LEFT)
            vx*=-1;
        velocity = new DVector(vx,vy);
        futureV = new DVector(velocity);
        depth = FRONT;
    }
    
    @Override
    protected void dealWithCollisions()
    {
        //should also do nothing
    }

    @Override
    public void collideWith(Entity a, int colType)//should never be called since dealWithCollisions does nothing
    {
        System.out.println("Fragment is colliding for some reason");
    }

    @Override
    public void limitVelocity() 
    {
        double maxV = Global.MAX_FALLING_SPEED;
        //if (velocity.getY()>maxV)
          //  velocity.setY(maxV);
    }

    @Override
    public void factorInControl() 
    {
        //nothing
    }

    @Override
    public void finalUpdates() 
    {
        
    }

    @Override
    public void updateImageName() 
    {
        //should do nothing
    }

    @Override
    public boolean readyToBeRemoved() 
    {
        return !inRange();
    }
    
    @Override
    public String id()
    {
        return id;
    }
}
