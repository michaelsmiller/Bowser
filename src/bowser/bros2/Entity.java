/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowser.bros2;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author MichaelMiller
 */
public abstract class Entity 
{
    protected DVector size;
    protected DVector location;
    protected World world;
    protected int deadCount;
    protected boolean interactable;
    protected String imageName;
    protected int counter;
    protected boolean staysAtTheEnd;//whether it stays when the game ends
    
    public int depth;//the draw priority. Low number means drawn on top
    
    //the depth possible values.
    public static final int FRONT = 1000;
    public static final int MIDDLE = 100;
    public static final int MIDDLE_BACK = 200;
    public static final int BACK = 10;
    
    //everything is from the reference point of the entity
    public static final int RIGHT = 1;
    public static final int LEFT = -1;
    public static final int UP = -2;
    public static final int DOWN = 2;
    public static final int STILL = 0;
    
    //any constructor extending Entity should instantiate imageName and interactable
    public Entity(World w, DVector s, Vector blockLoc)
    {
        size = s;
        location = blockLoc.convert();
        world =w;
        deadCount = -1;
        counter = 0;
    }
    
    public Entity(World w)
    {
        this(w,new DVector(),new Vector());
    }
    
    protected boolean on(Entity a)
    {
        return bottomY()==a.topY()&&
                (intersectsX(a)||
                (leftX()==a.leftX()&&rightX()==a.rightX()));
    }
    
    public int getCount()
    {
        return counter;
    }
    
    public boolean dead()
    {
        return deadCount>=0;
    }
    
    public DVector getLocation()
    {
        return location;
    }
    
    public DVector getSize()
    {
        return size;
    }
    
    protected boolean collidable()
    {
        return interactable&&!dead();
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
    
    public static double abs(double a){return Math.abs(a);}
    
    protected static boolean basicallyEqual(double a, double b)
    {
        return abs(a-b)<.00000001;
    }
    
    //DEAR GOD THIS IS VERY IMPORTANT MAN!
    protected static boolean between(double x, double a, double b)
    {
        return x>a&&x<b;
        //return x>=a&&x<=b;
    }
    
    public boolean intersects(Entity a)
    {
        return intersectsX(a)&&intersectsY(a);
    }
    
    protected boolean intersectsX(Entity a)
    {
        double lx1 =location.x;
        double lx2 = a.location.x;
        double rx1 = lx1+size.x;
        double rx2 = lx2+a.size.x;
        
        if (lx1==lx2&&rx1==rx2)//very special case
            return true;

        return between(lx1,lx2,rx2)||between(lx2,lx1,rx1)
             ||between(rx1,lx2,rx2)||between(rx2,lx1,rx1);
    }
    
    protected boolean intersectsY(Entity a)
    {
        double uy1 = location.y;
        double uy2 = a.location.y;
        double dy1 = uy1+size.y;
        double dy2 = uy2+a.size.y;
        
        if (uy1==uy2&&dy1==dy2)//very special case
            return true;
        
        return between(uy1,uy2,dy2)||between(uy2,uy1,dy1)
              ||between(dy1,uy2,dy2)||between(dy2,uy1,dy1);
    }
    
    protected boolean contains(DVector point)
    {
        double x1 = leftX();
        double x2 = rightX();
        double y1 = topY();
        double y2 = bottomY();
        
        return between(point.x,x1,x2)&&between(point.y,y1,y2);
    }
    
    protected DVector topLeft()
    {
        return location;
    }
    
    protected DVector topRight()
    {
        return new DVector(rightX(),topY());
    }
    
    protected DVector bottomRight()
    {
        return new DVector(rightX(),bottomY());
    }
    
    protected DVector bottomLeft()
    {
        return new DVector(leftX(),bottomY());
    }
    
    protected DVector[] corners()
    {
        return new DVector[]{topLeft(),bottomLeft(),bottomRight(),topRight()};
    }
    
    protected double midX()
    {
        return location.x+size.x/2;
    }
    
    protected double midY()
    {
        return location.y+size.y/2;
    }
    
    protected DVector center()
    {
        return new DVector(midX(),midY());
    }
    
    private static double absMin(double... d)
    {
        assert d.length>1;
        
        double min = d[0];
        for (int i = 1; i < d.length; i++)
        {
            double d1 = d[i];
            if (Math.abs(d1)<Math.abs(min))
                min = d1;
        }
        return min;
    }
    
    protected double xDistance(Entity a)
    {
        double x1 = leftX();
        double x2 = rightX();
        double x3 = a.leftX();
        double x4 = a.rightX();
        
        return absMin(x1-x3,x1-x4,x2-x3,x2-x4);
    }
    
    protected double yDistance(Entity a)
    {
        double y1 = topY();
        double y2 = bottomY();
        double y3 = a.topY();
        double y4 = a.bottomY();
        
        return absMin(y1-y3,y1-y4,y2-y3,y2-y4);
    }
    
    protected ArrayList<DVector> cornersIn(Entity a)
    {
        ArrayList<DVector> cornersAContains = new ArrayList<>();
        DVector[] vertices = corners();
        for (int i = 0; i<4; i++)
        {
            DVector c1 = vertices[i];
            if (a.contains(c1))
                cornersAContains.add(c1);
        }
        return cornersAContains;
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
    
    protected boolean inRange()
    {
        return world.inRange(this);
    }
    
    public static void printDirection(int d)
    {
        switch (d)
        {
            case UP: System.out.println("Up");break;
            case DOWN: System.out.println("Down");break;
            case LEFT: System.out.println("Left");break;
            case RIGHT: System.out.println("Right");break;
            case STILL: System.out.println("Still");break;
        }
    }
    
    public abstract void die();
    public abstract void step();
    public abstract boolean readyToBeRemoved();
    public abstract String id();
}