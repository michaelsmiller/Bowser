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
public class Koopa extends WalkingEnemy
{
    
    public Koopa(World w, Vector loc) 
    {
        super(w, new Vector(1,2).convert(), loc);
        imageName = "LGreenKoopa1";
        direction = LEFT;
    }
    
    public boolean inShell()
    {
        return size.x==size.y;
    }

    /*@Override
    public void factorInControl() 
    {
        //nothing for now I think
    }*/

    @Override
    public void finalUpdates()
    {
        if (direction==LEFT)
            printDirection(direction);
        super.finalUpdates();
    }

    @Override
    public void updateImageName() 
    {
        if (dying())
            imageName = "GreenShell1";
        else if (inShell())
            imageName = "GreenShell1";
        else
        {
            int t = Global.TIME_PER_GOOMBA_STEP;
            String dir = direction==LEFT ? "L" : "R";
            int num = counter%t<t/2 ? 1 : 2;
            imageName = dir+"GreenKoopa"+num;
        }
    }
    
    @Override
    public void factorInControl()
    {
        if (!inShell())
            super.factorInControl();
        else
            velocity.x = Global.GOOMBA_SPEED*Global.SHELL_SPEED_MULTIPLIER*direction;
    }
    
    private void turnIntoShell()
    {
        size = new Vector(1,1).convert();
        location.y = location.y+Global.BLOCK_LEN;
        direction = STILL;
    }
    
    @Override
    public void dieFromBlockHit()
    {
        turnIntoShell();
        super.dieFromBlockHit();
    }

    @Override
    public void getStomped() 
    {
        if (!inShell())
            turnIntoShell();
        else if (direction==STILL)
            direction = (world.getBowser().midX()<midX()) ? RIGHT : LEFT;
        else //moving shell
            direction = STILL;
    }
}
