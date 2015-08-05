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
public class Flagpole extends Entity
{
    public static final DVector SIZE = new DVector(.5*Global.BLOCK_LEN,10*Global.BLOCK_LEN);
    
    private final Flag flag;
    
    public Flagpole(World w, Vector blockLoc) 
    {
        super(w, SIZE, blockLoc);
        location.x = leftX()+Global.BLOCK_R-size.x/2;
        imageName = "Flagpole";
        interactable = true;
        staysAtTheEnd = true;
        flag = new Flag(world,this);
        world.add(flag);
        world.add(new Block(world,new Vector(blockLoc.x,blockLoc.y+10),Block.INDESTRUCTIBLE,Block.UNBREAKABLE));
    }
    
    public Flag getFlag()
    {
        return flag;
    }
    
    @Override
    public void die()
    {
        //no...
    }

    @Override
    public void step() 
    {
        //I hope not
    }

    @Override
    public boolean readyToBeRemoved() 
    {
        return false;
    }
}
