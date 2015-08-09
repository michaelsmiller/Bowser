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
public class Bowser extends Movable
{
    private int powerLevel;
    private boolean jump;
    private boolean invincible;
    private int fireballCount;
    private int hurtCount;
    private Flagpole lastFlagpoleTouched;
    
    public static final int BASIC_LEVEL = 0;
    public static final int FIRE_LEVEL = 1;
    
    public Bowser(World w, Vector blockLoc)
    {
        super(w,new DVector(1.5*Global.BLOCK_LEN,2*Global.BLOCK_LEN),blockLoc);
        imageName = "RBowserWalking1";
        direction = STILL;
        interactable = true;
        powerLevel = BASIC_LEVEL;
        gravity = true;
        jump = false;
        invincible = false;
        hurtCount = -1;
        lastFlagpoleTouched = null;
        staysAtTheEnd = true;
    }
    
    public Flagpole lastFlagpoleTouched()
    {
        return lastFlagpoleTouched;
    }

    public void setJump(boolean b)
    {
        jump = b;
    }
    
    public boolean canShootFireball()
    {
        return counter-fireballCount>=40&&powerLevel!=BASIC_LEVEL;
    }
    
    public void shootFireball()
    {
        assert canShootFireball();
        int d;
        DVector loc;
        if (imageName.substring(0,1).equals("L"))//facing left
        {
            d = LEFT;
            loc = new DVector(leftX()-Fireball.STANDARD_SIZE.x,location.y);
        }
        else //facing right
        {
            d = RIGHT;
            loc = topRight();
        }
        
        world.entitiesToAddThisStep().add(new Fireball(world,loc,this,d));
        fireballCount = counter;
    }
    
    public void takeDamage()
    {
        if (powerLevel == FIRE_LEVEL)
        {
            powerLevel = BASIC_LEVEL;
            hurtCount = counter;
            invincible = true;
        }
        else if (!invincible)
            die();
    }
    
    /*public boolean hittingBottomOfBlock(Block b)
    {
        assert intersects(b);
        
        
    }*/
    
    public void goThroughFlagpole()
    {
        double dx = size.x+Flagpole.SIZE.x;
        location.x = location.x+dx;
        
        String dir = imageName.substring(0,1);
        if (dir.equals("L"))
            dir = "R";
        else dir = "L";
        imageName = dir+imageName.substring(1);
        
        World.pause(Global.TIME_STEP*10);
        world.getFrame().repaint();
        World.pause(Global.TIME_STEP*10);
        
        world.moveFrame();
        
        gravity = true;
    }
    
    @Override
    protected void collideWithBlock(Entity block,int colType)
    {
        if (block instanceof Block&&((Block)block).getType()==Block.INVISIBLE&&colType!=UP)//makes sure no collisions with invisible
            return;
        super.collideWithBlock(block,colType);
    }
    
    @Override
    public void collideWith(Entity a, int colType) 
    {
        boolean realCollision = false;
        boolean reciprocal = false;
        if (a instanceof Flagpole)
        {
            Flagpole f = (Flagpole)a;
            lastFlagpoleTouched = f;
            world.setGameplay(World.FLAGPOLE_ENDING);
            world.getAdapter().setUserControl(false);
            collideWithBlock(a,colType);
            gravity = false;
            futureV.y = Global.FLAGPOLE_SPEED;
            direction = STILL;
            jump = false;
            f.getFlag().lower();
        }
        else if (a instanceof Block)
        {
            Block b = (Block)a;
            realCollision = true;
            if (b.invisible()&&
                    (colType!=UP||velocity.y>0))//it reads some collisions as up when they should be down
                return;
            if (colType==UP)
                b.hit();
            collideWithBlock(a,colType);
        }
        else if (a instanceof Enemy)
        {
            Movable enemy = (Movable)a;
            
            //if you're hitting a stationary shell
            if (enemy instanceof Koopa && ((Koopa)a).inShell() && enemy.velocity.x==0)
            {
                DVector temp = new DVector(futureV);
                collideWithBlock(a,colType);
                futureV = temp;
                //enemy.setDirection(midX()>enemy.midX() ? LEFT : RIGHT);
            }
                
                
            if (colType==DOWN)//kill the enemy!
            {
                futureV.y = -Global.JUMPING_SPEED*.5;
                ((Enemy)a).getStomped();
            }
            else //bowser takes damage
            {
                takeDamage();
                reciprocal = true;
                enemy.collideWith(this,reverseCollision(colType));
            }
        }
        else if (a instanceof Coin)
            ((Coin)a).collect();
        else if (a instanceof Mushroom)
        {
            int t = ((Mushroom)a).getType();
            if (t==Mushroom.POWER)
                if (powerLevel<FIRE_LEVEL)
                    powerLevel++;
            a.die();
        }
        if (realCollision)
            world.addCollision(new Collision(this,a,colType));
        if (reciprocal)
            world.addCollision(new Collision(a,this,colType));
    }

    @Override
    public void limitVelocity()
    {
        double vx1 = velocity.x;
        double vy1 = velocity.y;
        
        //if (onGround)
        //{
            double vx2;
            double fric = Global.FRICTION;
            double neg = vx1/abs(vx1);
            if (abs(vx1)>fric)
                vx2 = neg*(abs(vx1)-fric);
            else vx2 = 0;
            velocity.x = vx2;
        //}
        
        if (vy1>Global.MAX_FALLING_SPEED)
            velocity.y = Global.MAX_FALLING_SPEED;
    }
    
    private double moveToward(double a, double goal)
    {
        double neg = a/abs(a);
        double d = abs(a);
        if (d<goal*5/6)
            return neg*(d+goal*1/6);
        else if (d<goal)
            return neg*goal;
        return a;
    }

    @Override
    public void factorInControl()
    {
        if (lastFlagpoleTouched!=null&&rightX()>lastFlagpoleTouched.rightX())//right of the flagpole
        {
            if (bottomY() < (Global.HEIGHT-2)*Global.BLOCK_LEN&&onGround)
            {
                World.pause(Global.TIME_STEP*10);
                jump = true;
            }
            else jump = false;
            
            direction = RIGHT;
        }
        
        switch (direction)
        {
            case RIGHT:
                if (!hitRightBlockThisStep())
                    velocity.x = Global.WALKING_SPEED;
                break;
            case LEFT:
                if (!hitLeftBlockThisStep())
                    velocity.x = -Global.WALKING_SPEED;
                break;
        }
        if (onGround&&jump)
            velocity.y = -Global.JUMPING_SPEED;
    }
    
    public boolean hurt()
    {
        return hurtCount!=-1;
    }
    
    private int hurtCount()
    {
        return counter-hurtCount;
    }

    @Override
    public void finalUpdates() 
    {
        updateOnGround();
        jump = false;
        if (!inRange())
            die();
        counter++;
        if (hurtCount()>Global.HURT_TIME)
        {
            hurtCount = -1;
            invincible = false;
        }
    }
    
    @Override
    public void updateImageName()
    {
        String num;
        int t = Global.TIME_PER_BOWSER_STEP;
        if (!(onGround&&velocity.x!=0))
            num = "";
        else if (counter%t<t/2)
            num="1";
        else num="2";
        
        String dir;
        if (velocity.x>0)
            dir="R";
        else if (velocity.x<0)
            dir="L";
        else dir = imageName.substring(0,1);
        
        String movement = "Still";
        if (onGround&&velocity.x!=0)
            movement = "Walking";
        else if (velocity.y<0)
            movement = "Jumping";
        
        imageName = dir+"Bowser"+movement+num;
        
        if (hurt()&&hurtCount()%10<5)//accounts for flashing when injured
            imageName = imageName.substring(0,1);
    }
    
    @Override
    public boolean readyToBeRemoved()
    {
        return false;//bowser should never ever be removed before the game itself ends
    }
    
    @Override
    public void die()
    {
        world.gameOver();
    }
}