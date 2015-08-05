/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowser.bros2;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author MichaelMiller
 */
public abstract class Nonentity 
{
    protected DVector location;
    protected DVector size;
    protected World world;
    protected int counter;
    protected String imageName;
    
    public Nonentity(World w,DVector s,DVector loc)
    {
        world = w;
        size = s;
        location = loc;
        counter = 0;
    }
    
    public BufferedImage getImage()
    {
        return world.images.get(imageName);
    }
    
    public void draw(Graphics2D g2)
    {
        if (imageName!=null&&!imageName.equals(""))
        {
            g2.drawImage(getImage(), (int) (location.x-world.getLowerX()), (int) location.y,
                    (int) size.x,(int) size.y,world);
        
            //g2.drawRect((int) location.x, (int) location.y,
              //      (int) size.x, (int) size.y);
        }
    }
    
    public void step()
    {
        updateImageName();
        finalUpdates();
    }
    
    protected static boolean between(double x, double a, double b)
    {
        return x>a&&x<b;
        //return x>=a&&x<=b;
    }
    
    public double topY()
    {
        return location.y;
    }
    
    public double bottomY()
    {
        return location.y+size.y;
    }
    
    public double leftX()
    {
        return location.x;
    }
    
    public double rightX()
    {
        return location.x+size.x;
    }
    
    protected abstract void updateImageName();
    protected abstract void finalUpdates();
    public abstract boolean readyToBeRemoved();
}
