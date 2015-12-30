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
public class Explosion extends Entity
{
    private int stage;
    
    private static final String id = "Explosion";
    
    public Explosion(World w, DVector s, DVector loc) 
    {
        super(w, s, new Vector());
        location = loc;
        interactable = false;
        stage = 1;
        staysAtTheEnd = false;
        depth = FRONT;
    }
    
    private void updateImageName()
    {
        imageName = "Explosion"+stage;
    }
    
    private boolean between(int x, int a, int b)
    {
        return x>a&&x<=b;
    }
    
    private void updateStage()
    {
       int x = Global.EXPLOSION_TIME;
       int x1 = x/3;
       int x2 = 2*x1;
       int c = counter;
       
       if (c <= x1)
           stage = 1;
       else if (between(c,x1,x2))
           stage = 2;
       else if (between(c,x2,x))
           stage = 3;
       else //fireball has overstayed welcome
           die();
       
    }

    @Override
    public void die() 
    {
        deadCount = counter;
    }

    @Override
    public void step() 
    {
        counter++;
        updateStage();
        updateImageName();
    }

    @Override
    public boolean readyToBeRemoved() 
    {
        return dead();
    }
    
    @Override
    public String id()
    {
        return id;
    }
}
