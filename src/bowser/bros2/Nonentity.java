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
            g2.drawImage(getImage(), (int) (location.getX()-world.getLowerX()), (int) location.getY(),
                    (int) size.getX(),(int) size.getY(),world);
        
            //g2.drawRect((int) location.getX(), (int) location.getY(),
              //      (int) size.getX(), (int) size.getY());
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
        return location.getY();
    }
    
    public double bottomY()
    {
        return location.getY()+size.getY();
    }
    
    public double leftX()
    {
        return location.getX();
    }
    
    public double rightX()
    {
        return location.getX()+size.getX();
    }
    
    protected abstract void updateImageName();
    protected abstract void finalUpdates();
    public abstract boolean readyToBeRemoved();
}
