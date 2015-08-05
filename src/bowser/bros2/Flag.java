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
public class Flag extends Movable
{
    public Flag(World w, Flagpole f) 
    {
        super(w, new Vector(1,1).convert(), new DVector(f.leftX()-Global.BLOCK_LEN*.8,f.topY()+Global.BLOCK_LEN));
        imageName = "Flag";
        interactable = true;
        gravity = false;
        velocity = new DVector();
        staysAtTheEnd = true;
    }

    @Override
    public void collideWith(Entity a, int colType) 
    {
        if (a instanceof Block)
        {
            collideWithBlock(a,colType);
            interactable = false;
            futureV.y = 0;
            
            world.getBowser().goThroughFlagpole();
        }
    }
    
    public void lower()
    {
        velocity.y = Global.FLAGPOLE_SPEED;
        futureV.y = Global.FLAGPOLE_SPEED;
    }

    @Override
    public void limitVelocity() 
    {
        //no
    }

    @Override
    public void factorInControl() 
    {
        //System.out.println("Flag is being included");
        //no
    }

    @Override
    public void finalUpdates() 
    {
        //no need so far
    }

    @Override
    public void updateImageName() 
    {
        //no
    }

    @Override
    public boolean readyToBeRemoved() 
    {
        return false;
    }
    
}
