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
public class DVector 
{
    public double x;
    public double y;
    
    public DVector(double x1, double y1)
    {
        x = x1;
        y = y1;
    }
    
    public DVector()
    {
        x = 0;
        y = 0;
    }
    
    public DVector(DVector a)
    {
        x = a.x;
        y = a.y;
    }
    
    public static DVector abs(DVector a)
    {
        return new DVector(Math.abs(a.x),Math.abs(a.y));
    }
    
    //I dunno, this could be really problematic in the future...Don't forget
    public Vector convert()
    {
        double r = Global.BLOCK_LEN;
        return new Vector((int)(Math.round(x/r)),(int)(Math.round(y/r)));
    }
    
    public Vector approximate()
    {
        return new Vector((int)Math.round(x),(int)Math.round(y));
    }
    
    public static DVector addVector(DVector a, DVector b)
    {
        return new DVector(b.x+a.x,b.y+a.y);
    }
    
    public static DVector subtractVector(DVector a, DVector b)
    {
        return new DVector(a.x-b.x,a.y-b.y);
    }
    
    public int compareX(DVector a)
    {
        Double x1 = x;
        return x1.compareTo(a.x);
    }
    
    public int compareY(DVector a)
    {
        Double y1 = y;
        return y1.compareTo(a.y);
    }
    
    @Override
    public boolean equals(Object o)
    {
        DVector a = (DVector)o;
        return x==a.x&&y==a.y;
    }
    
    private static double mean(double a, double b)
    {
        return (a+b)/2;
    }
    
    public static DVector midpoint(DVector a, DVector b)
    {
        return new DVector(mean(a.x,b.x),mean(a.y,b.y));
    }
}
