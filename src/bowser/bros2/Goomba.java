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
public class Goomba extends WalkingEnemy
{
    
    public Goomba(World w, Vector loc)
    {
        super(w,(new Vector(1,1)).convert(),loc);
        imageName = "Goomba1";
    }
    
    
    @Override
    public void updateImageName() 
    {
        int t = Global.TIME_PER_GOOMBA_STEP;
        
        if (dead())//dead by squashing
            imageName = "GoombaDead";
        else if (!interactable)//in the falling off stage death animation
            imageName = "GoombaUpsideDown";
        else if (counter%t<t/2)
            imageName = "Goomba1";
        else imageName = "Goomba2";
    }
    
    @Override
    public boolean readyToBeRemoved()
    {
        if (dead())//dead the normal way, through being jumped on
            return counter-deadCount>=Global.GOOMBA_DEATH_DELAY;
        else return !world.inYRange(this);//removed when it passes the bottom of the screen
    }

    @Override
    public void getStomped()
    {
        die();
    }
}