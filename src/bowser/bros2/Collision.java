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
public class Collision
{
    private Entity entity;
    private Entity movable;
    private int type;
    
    public static final int UP = Entity.UP;
    public static final int DOWN = Entity.DOWN;
    public static final int LEFT = Entity.LEFT;
    public static final int RIGHT = Entity.RIGHT;
    
    public Collision(Entity m, Entity e, int t)
    {
        entity = e;
        movable = m;
        type = t;
    }
    
    public Entity getEntity()
    {
        return entity;
    }
    
    public Entity getMovable()
    {
        return movable;
    }
    
    public int getType()
    {
        return type;
    }
}
