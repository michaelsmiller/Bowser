/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowser.bros2;

import java.util.ArrayList;

/**
 *
 * @author MichaelMiller
 */
public abstract class Movable extends Entity
{
    protected DVector velocity;
    protected DVector futureV;
    protected int direction;
    protected boolean gravity;
    protected boolean onGround;
    
    public static final int NO_COLLISION = 10;
    
    //extending this should take care of gravity
    public Movable(World w, DVector s, Vector loc)
    {
        super(w,s,loc);
        velocity = new DVector();
        futureV = new DVector();
        onGround = false;
    }
    
    public Movable(World w,DVector s, DVector loc)
    {
        this(w,s,new Vector());
        location = loc;
    }
    
    public boolean onGround()
    {
        return onGround;
    }
    
    public void setDirection(int d)
    {
        direction = d;
    }
    
    public void stepLocation()
    {
        location = DVector.addVector(location,velocity);
    }
    
    private boolean intersectedX(Entity a)
    {
        DVector pLoc = DVector.subtractVector(location, velocity);
        double left1 = pLoc.x;
        double right1 = left1+size.x;
        double left2 = a.location.x;
        double right2 = left2+a.size.x;
        
        return between(left1,left2,right2)||between(right1,left2,right2);
    }
    
    private boolean intersectedY(Entity a)
    {
        DVector pLoc = DVector.subtractVector(location, velocity);
        double up1 = pLoc.y;
        double down1 = up1+size.y;
        double up2 = a.location.y;
        double down2 = up2+a.size.y;
        
        return between(up1,up2,down2)||between(down1,up2,down2);
    }
    
    private boolean intersected(Entity a)
    {
        return intersectedX(a)&&intersectedY(a);
    }
    
    protected int getCollisionDirection(Entity a)
    {
        assert intersects(a);
        
        //if (intersected(a))
          //  return NO_COLLISION;
        
        if (velocity.equals(new DVector())&&a instanceof Movable)
        {
            Movable m = (Movable)a;
            //if (!m.velocity.equals(new DVector()))
                return reverseCollision(m.getCollisionDirection(this));
        }
        
        //deals with the easy case of only moving in one axis.
        if (velocity.x==0)//moving up or down
        {
            if (topY()<a.topY())
                return DOWN;
            else return UP;
        }
        else if (velocity.y==0)//moving right or left
        {
            if (rightX()>a.rightX())
                return LEFT;
            else return RIGHT;
        }
        
        ArrayList<DVector> cornersAContains = cornersIn(a);
        ArrayList<DVector> aCornersThisContains = a.cornersIn(this);
        int numCornersInA = cornersAContains.size();
        if (numCornersInA==0&&aCornersThisContains.isEmpty())//if theyre the same size and exactly on edges
        {
            if (topY()<a.topY())
                return DOWN;
            else if (rightX()>a.rightX())
                return LEFT;
            else if (leftX()<a.leftX())
                return RIGHT;
            else if (bottomY()>a.bottomY())
                return UP;
        }
        if (numCornersInA==1)//each has one vertex in the other so working backwards is how to find the collision
        {
            if ((a instanceof Block))
                assert aCornersThisContains.size()==1;
            
            DVector thisCorner = cornersAContains.get(0);
            
            double dx;
            if (velocity.x>0)
                dx=abs(thisCorner.x-a.leftX());
            else dx=abs(thisCorner.x-a.rightX());
            double dt = dx/abs(velocity.x);
            double dy;
            if (velocity.y>0)
                dy = abs(thisCorner.y-a.topY());
            else dy = abs(thisCorner.y-a.bottomY());
            
            if (dt*abs(velocity.y)<dy)//x collision happened first
            {
                /*if (thisCorner.x>otherCorner.x)
                    return RIGHT;
                else return LEFT;*/
                if ((velocity.x<0 && thisCorner.x!=leftX())//seems like up when down
                  ||(velocity.x>0 && thisCorner.x!=rightX()))//seems like down when its up
                {
                    if (velocity.y<0)
                        return UP;
                    else return LEFT;
                }
                /*if (rightX()>a.rightX())
                    return LEFT;
                else return RIGHT;*/
                if (velocity.x>0)
                    return RIGHT;
                else return LEFT;
            }
            else //y collision happened first
            {
                if ((velocity.y<0 && thisCorner.y!=topY())//seems like up when its not
                  ||(velocity.y>0 && thisCorner.y!=bottomY()))//seems like down when its not
                {
                    if (velocity.x>0)
                        return RIGHT;
                    else return LEFT;
                }
                /*if (topY()<a.topY())
                    return DOWN;
                else return UP;*/
                
                if (velocity.y>0)
                    return DOWN;
                else return UP;
            }
        }
        else if (numCornersInA==0)//the other guy has two vertices in this one or 1 possibly
        {
            if (aCornersThisContains.size()==1)//if one of their sides have identical x or y values
                return collisionInWeirdCase(this,a);
            return collisionWhenBHas2InA(this,a);//if 
        }
        else if (numCornersInA==2)//two of this guys corners are in a
        {
            assert aCornersThisContains.isEmpty();
            return reverseCollision(collisionWhenBHas2InA(a,this));
        }
        //that should cover everything
        throw new Error("Somehow this collision net of possibilities isn't overarching...");
    }
    
    //from the perspective of a
    private static int collisionWhenBHas2InA(Entity a, Entity b)
    {
        ArrayList<DVector> bCornersAContains = b.cornersIn(a);
        DVector c1 = bCornersAContains.get(0);
        DVector c2 = bCornersAContains.get(1);
        if (c1.x==c2.x)//its a side collision
        {
            if (a.leftX()<b.leftX())
                return RIGHT;
            else return LEFT;
        }
        else //vertical collision
        {
            assert c1.y==c2.y;

            if (a.topY()<b.topY())
                return DOWN;
            else return UP;
        }
    }
    
    //covers the case when one vertex of b is in a but the corresponding
    //vertex of a is on the edge of b.
    private static int collisionInWeirdCase(Entity a, Entity b)
    {
        ArrayList<DVector> bCornersAContains = b.cornersIn(a);
        DVector c = bCornersAContains.get(0);
        if (c.equals(b.topLeft()))
        {
            if (a.rightX()==b.rightX())
                return DOWN;
            else if (a.bottomY()==b.bottomY())
                return RIGHT;
        }
        else if (c.equals(b.topRight()))
        {
            if (a.leftX()==b.leftX())
                return DOWN;
            else if (a.bottomY()==b.bottomY())
                return LEFT;
        }
        else if (c.equals(b.bottomRight()))
        {
            if (a.topY()==b.topY())
                return LEFT;
            else if (a.leftX()==b.leftX())
                return UP;
        }
        else if (c.equals(b.bottomLeft()))
        {
            if (a.topY()==b.topY())
                return RIGHT;
            else if (a.rightX()==b.rightX())
                return UP;
        }
        throw new Error("You done messed up");
    }
    
    protected int reverseCollision(int c)
    {
        switch (c)
        {
            case LEFT:return RIGHT;
            case RIGHT:return LEFT;
            case UP:return DOWN;
            case DOWN:return UP;
        }
        throw new Error("The collision is weird");
    }
    
    protected ArrayList<Entity> getIntersectingEntities()
    {
        ArrayList<Entity> result = new ArrayList<>();
        for (Entity e: world.getEntities())
            if (e.collidable()&&intersects(e))
                result.add(e);
        result.remove(this);
        return result;
    }
    
    //makes sure the list has a good order to avoid glitching
    protected void sortByPriorityCollision(ArrayList<Entity> intersecting)
    {
        int size = intersecting.size();
        if (size<=1)
            return;
        for (int i = 0; i < size; i++)
        {
            Entity a = intersecting.get(i);
            if (a.contains(new DVector(midX(),topY()-Global.BLOCK_R)))//the block directly above
                intersecting.add(0,intersecting.remove(i));
            else if (a.contains(new DVector(rightX()+Global.BLOCK_R,midY())))//the block directly to the right
                intersecting.add(0,intersecting.remove(i));
            else if (a.contains(new DVector(leftX()-Global.BLOCK_R,midY())))//the block directly to the left
                intersecting.add(0,intersecting.remove(i));
            //else if (a.contains(new DVector(midX(),bottomY()+Global.BLOCK_R)))//the block directly below)
                //intersecting.add(intersecting.remove(i));//want the ground to have the last priority
        }
        assert size==intersecting.size();
    }
    
    protected void collideWithBlock(Entity block, int colType)
    {
        switch (colType)
        {
            case UP:
                futureV.y = -futureV.y*.4;
                double dy = block.bottomY()-topY();
                assert dy>0;
                location.y = location.y+dy;
                break;
            case DOWN:
                futureV.y = 0;
                double y = bottomY()-block.topY();
                location.y = location.y-y;
                break;
            case RIGHT:
                futureV.x = 0;
                double dx = rightX()-block.leftX();
                location.x = location.x-dx;
                break;
            case LEFT:
                futureV.x = 0;
                double x = block.rightX()-leftX();
                location.x = location.x+x;
                break;
        }
    }
    
    protected void dealWithCollisions()
    {
        if (!collidable())
            return;
        ArrayList<Entity> collisions = getIntersectingEntities();
        sortByPriorityCollision(collisions);
        for (Entity a: collisions)
        {
            if (intersects(a))
            {
                int colType = getCollisionDirection(a);
                //if (a instanceof Block&&this instanceof Mushroom)
                  //  printDirection(colType);
                //if (colType==UP)
                  //  System.out.println(counter);
                //System.out.println(colType);
                collideWith(a,colType);
            }
        }
    }
    
    protected boolean hitGroundThisStep()
    {
        for (Collision c: world.getCollisions())
            if (c.getMovable()==this&&
                c.getEntity() instanceof Block&&
                c.getType()==DOWN)
            {
                //System.out.println(counter);
                return true;
            }
        return false;
    }
    
    protected boolean hitLeftBlockThisStep()
    {
        for (Collision c: world.getCollisions())
            if (c.getMovable()==this&&
                c.getEntity() instanceof Block&&
                c.getType()==LEFT)
            {
                //System.out.println(counter);
                return true;
            }
        return false;
    }
    
    protected boolean hitRightBlockThisStep()
    {
        for (Collision c: world.getCollisions())
            if (c.getMovable()==this&&
                c.getEntity() instanceof Block&&
                c.getType()==RIGHT)
            {
                //System.out.println(counter);
                return true;
            }
        return false;
    }
    
    protected void factorInGravity()
    {
        if (gravity && !onGround && !hitGroundThisStep())
            velocity.y = velocity.y+Global.GRAVITY;
    }
    
    protected void updateOnGround()
    {
        boolean grounded = false;
        for (Entity e: world.getEntities())
            if (e instanceof Block && !((Block)e).invisible() && e.topY()==bottomY() && intersectsX(e))
                grounded = true;
        onGround = grounded;
    }
    
    protected void stepVelocity()
    {
        velocity = new DVector(futureV);
        factorInGravity();
        limitVelocity();
        factorInControl();
        futureV = new DVector(velocity);
    }
    
    @Override
    public void step() 
    {
        dealWithCollisions();
        stepVelocity();
        finalUpdates();
        updateImageName();
    }
    
    public void die()
    {
        deadCount = counter;
        interactable = false;
    }
    
    //should add a collision to world and update futureV and location and some booleans and values
    public abstract void collideWith(Entity a,int colType);//self explanatory
    public abstract void limitVelocity();//factors in friction and velocity ceilings
    public abstract void factorInControl();//factors in user or ai pushing on the movable
    public abstract void finalUpdates();//this allows the thing to update non-velocity or position variables
    public abstract void updateImageName();//what do you think this does?
}