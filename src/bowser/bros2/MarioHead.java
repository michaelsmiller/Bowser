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
public class MarioHead extends Entity
{
    private static final double INCREASE_PER_STEP = 2;
    private static final String id = "MarioHead";
    
    public MarioHead(World w, DVector loc) 
    {
        super(w, new Vector(1,1).convert(), new Vector());
        location = loc;
        interactable = false;
        imageName = "MarioHead";
        depth = FRONT;
    }

    @Override
    public void die() 
    {
        //nothing
    }

    @Override
    public void step() 
    {
        double dx = INCREASE_PER_STEP;
        double dy = dx;
        location = DVector.subtractVector(location,new DVector(dx,dy));
        size = DVector.addVector(size,new DVector(2*dx,2*dy));
    }

    @Override
    public boolean readyToBeRemoved() 
    {
        return false;
    }
    
    @Override
    public String id()
    {
        return id;
    }
}
