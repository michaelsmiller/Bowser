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
public class Vector 
{
    private int x;
    private int y;
    
    public Vector(int x1, int y1)
    {
        x = x1;
        y = y1;
    }
    
    public Vector()
    {
        x = 0;
        y = 0;
    }
    
    public Vector(Vector a)
    {
        x = a.x;
        y = a.y;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public void setX(int x1)
    {
        x = x1;
    }
    
    public void setY(int y1)
    {
        y = y1;
    }
    
    public DVector convert()
    {
        return new DVector(x*Global.BLOCK_LEN,y*Global.BLOCK_LEN);
    }
}
