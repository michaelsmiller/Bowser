/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowser.bros2;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author MichaelMiller
 */
public class Adapter extends KeyAdapter
{
    private World world;
    private boolean userControl;
    
    /**
     * Makes the keyadapter
     * @param w the world
     */
    public Adapter(World w)
    {
        world = w;
        userControl = true;
    }
    
    public void setUserControl(boolean b)
    {
        userControl = b;
    }
    
    /**
     * Does stuff when the key is typed. This has never worked so I just leave it here
     * @param e the typing
     */
    @Override
    public void keyTyped(KeyEvent e) 
    {
        //System.out.println("Key is typed");
    }

    /**
     * This does stuff when the key is pressed
     * @param e the event
     */
    @Override
    public void keyPressed(KeyEvent e) 
    {
        if (!userControl)
            return;
        
        Bowser bowser = world.getBowser();
        int k = e.getKeyCode();
        switch(k)
        {
            case KeyEvent.VK_UP:
                if (bowser.onGround())
                    bowser.setJump(true);
                break;
            case KeyEvent.VK_DOWN:
                //nothing yet for down
            break;
            case KeyEvent.VK_RIGHT:
                bowser.setDirection(Entity.RIGHT);
                break;
            case KeyEvent.VK_LEFT:
                bowser.setDirection(Entity.LEFT);
                break;
            case KeyEvent.VK_X:
                if (bowser.canShootFireball())
                {
                    //System.out.println("Shooting Fireball hopefully");
                    bowser.shootFireball();
                }
                break;
        }
    }

    /**
     * This undoes things when the keys are released, for movement
     * @param e the event
     */
    @Override
    public void keyReleased(KeyEvent e) 
    {
        if (!userControl)
            return;
        
        Bowser bowser = world.getBowser();
        int k = e.getKeyCode();
        switch (k)
        {
            case KeyEvent.VK_RIGHT:case KeyEvent.VK_LEFT:
                bowser.setDirection(Entity.STILL);
                break;
            case KeyEvent.VK_UP:
                //stop jump
                break;
        }
    }
}
