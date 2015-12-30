/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowser.bros2;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author MichaelMiller
 */
public class Label extends Entity
{
    private String message;
    private final static String id = "Label";
    private final int type;
    
    public final static int NORMAL_HIT = 0;
    
    
    public Label(World w, int type, DVector loc)
    {
        super(w,new DVector(),loc);
        this.type = type;
        interactable = false;
        staysAtTheEnd = false;
        depth = FRONT;
        
        switch (this.type)
        {
            case NORMAL_HIT: message = "200"; break;
        }
    }

    @Override
    public void die() 
    {
        //nothing
    }
    
    @Override
    public void draw(Graphics2D g2)
    {
        Vector loc = new Vector((int) (location.x-world.getLowerX()), (int) location.y);
        g2.setColor(Color.YELLOW);
        g2.drawString(message, loc.x, loc.y);
    }

    @Override
    public void step() 
    {
        location.y -= ((double) Global.BLOCK_LEN) / 50;
        counter++;
    }

    @Override
    public boolean readyToBeRemoved() 
    {
        return counter >= 150;
    }

    @Override
    public String id() 
    {
        return id;
    }
    
}
