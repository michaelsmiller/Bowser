/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowser.bros2;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author MichaelMiller
 */
public class World extends JComponent
{
    private final JFrame frame;
    private double lowerX;
    private final int length;
    private final long startTime,endTime;
    private int score;
    
    private final ArrayList<Entity> entities;
    private final ArrayList<Nonentity> nonentities;
    private ArrayList<Nonentity> nonentitiesToAddThisStep;
    private ArrayList<Entity> entitiesToAddThisStep;
    public final HashMap<String, BufferedImage> images;
    private String background;
    private ArrayList<Collision> collisions;
    
    private int gameplay;
    
    public static final int NORMAL = 0;
    public static final int FLAGPOLE_ENDING = 1;
    public static final int GAMEOVER = 2;
    
    /**
     * This does everything!
     * @param f the frame
     * @param X
     * @param stageLen
     */
    public World(JFrame f, double X, int stageLen)
    {
        super();
        frame = f;
        f.add(this);
        f.setSize((int)(Global.LENGTH*Global.BLOCK_LEN),(int)(Global.HEIGHT*Global.BLOCK_LEN)+20);
        setSize(new Dimension(f.getWidth(),f.getHeight()));
        
        lowerX = X;
        length = stageLen;
        
        //remember this
        startTime = System.currentTimeMillis();
        endTime = 0;
        
        collisions = new ArrayList<>();
        
        entities = new ArrayList<>();
        entitiesToAddThisStep = new ArrayList<>();
        entities.add(new Bowser(this,new Vector(5,3)));
        
        //adds invisible barriers on either end of the map to keep bowser from walking off
        entities.add(new Obstacle(this,new Vector(-1,0),new Vector(1,Global.HEIGHT),Obstacle.INVISIBLE_WALL));
        entities.add(new Obstacle(this,new Vector(length,0),new Vector(1,Global.HEIGHT),Obstacle.INVISIBLE_WALL));
        
        nonentities = new ArrayList<>();
        nonentitiesToAddThisStep = new ArrayList<>();
        
        frame.addKeyListener(new Adapter(this));
        
        images = new HashMap<>();
        readImages();
        background = "DefaultBackground";
        score = 0;
        
        gameplay = NORMAL;
    }
    
    /**
     * This awesome method reads all the images into a hashmap for quick use
     */
    private void readImages()
    {
        String name="";
        try 
        {
            Scanner reader = new Scanner(getClass().getResourceAsStream("/Files/imageNames.txt"));
            while (reader.hasNext())
            {
                name = reader.nextLine();
                if (name.length()>0)
                    images.put(name,ImageIO.read(getClass().getResourceAsStream("/Pictures/"+name+".png")));
            }
        }
        catch (Exception e)
        {System.out.println("Making images didn't work\n"+name);}
    }
    
    public void add(Entity e)
    {
        entities.add(e);
    }
    
    public void add(Nonentity e)
    {
        nonentities.add(e);
    }
    
    public JFrame getFrame()
    {
        return frame;
    }
    
    public Adapter getAdapter()
    {
        return (Adapter)frame.getKeyListeners()[0];
    }
    
    public void setControl(boolean b)
    {
        getAdapter().setUserControl(b);
    }
    
    public ArrayList<Entity> entitiesToAddThisStep()
    {
        return entitiesToAddThisStep;
    }
    
    public ArrayList<Nonentity> nonentitiesToAddThisStep()
    {
        return nonentitiesToAddThisStep;
    }
    
    public ArrayList<Nonentity> getNonentities()
    {
        return nonentities;
    }
    
    public void incrementScore()
    {
        score++;
    }
    
    public void addCollision(Collision c)
    {
        collisions.add(c);
    }
    
    public double getLowerX()
    {
        return lowerX;
    }
    
    public Bowser getBowser()
    {
        return (Bowser)entities.get(0);
    }
    
    public ArrayList<Entity> getEntities()
    {
        return entities;
    }
    
    public int counter()
    {
        return getBowser().getCount();
    }
    
    public ArrayList<Entity> getInRangeEntities()
    {
        ArrayList<Entity> result = new ArrayList<>();
        for (Entity e: entities)
            if (inRange(e))
                result.add(e);
        return result;
    }
    
    public ArrayList<Nonentity> getInRangeNonentities()
    {
        ArrayList<Nonentity> result = new ArrayList<>();
        for (Nonentity e: nonentities)
            if (inRange(e))
                result.add(e);
        return result;
    }
    
    public boolean inYRange(Entity e)
    {
        assert entities.contains(e);
        return  e.bottomY()>=0 &&
                e.topY()<=Global.BOARD_SIZE.y;
    }
    
    public boolean inRange(Entity e)
    {
        assert entities.contains(e);
        return e.leftX()<lowerX+Global.BOARD_SIZE.x&&
               e.rightX()>lowerX&&
               inYRange(e);
    }
    
    public boolean inRange(Nonentity e)
    {
        assert nonentities.contains(e);
        return e.leftX()<lowerX+Global.BOARD_SIZE.x&&
               e.rightX()>lowerX&&
               e.bottomY()>=0 &&
               e.topY()<=Global.BOARD_SIZE.y;
    }
    
    public ArrayList<Collision> getCollisions()
    {
        return collisions;
    }
    
    public HashSet<Entity> getCollided()
    {
        HashSet<Entity> a = new HashSet<>();
        for (Collision c: collisions)
        {
            a.add(c.getMovable());
            a.add(c.getEntity());
        }
        return a;
    }
    
    /**
     * This draws everything
     * @param g the graphics drawing everything
     */
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(images.get(background), 0, 0,getWidth(),getHeight(),null);//draws background first
        for (Nonentity n: getInRangeNonentities())
            n.draw(g2);
        for (Entity b: getInRangeEntities())
            b.draw(g2);
        double d = Global.BLOCK_LEN;
        //add(new JLabel("This is a test!!!!!!!!!!!!"));
    }
    
    /**
     * This pauses for a moment
     * @param time the moment
     */
    public static void pause(long time)
    {
        try{Thread.sleep(time);}
        catch(Exception e){System.out.println("Pausing problem...how?");}
    }
    
    private void stepLocations()
    {
        for (Entity e: entities)
            if (!e.dead() && e instanceof Movable)
                ((Movable)e).stepLocation();
    }
    
    private void deathThrow(Entity e)//what happens right before the Entity is removed forever
    {
        if (e instanceof Block)
            ((Block)e).throwFragments();
        //fireball explosion
    }
    
    public void moveFrame()
    {
        double X = Global.BOARD_SIZE.x;
        double center = (2*lowerX+X)/2;
        double dx = getBowser().midX()-center;
        double newX = lowerX+dx;
        double maxLowerX = length*Global.BLOCK_LEN-Global.BOARD_SIZE.x;
        
        if (newX<0)//at the beginning of the stage
            lowerX=0;
        else if (newX>maxLowerX)//at the end of the stage
            lowerX = maxLowerX;
        else //most cases
            lowerX = newX;
    }
    
    private void winLevel()
    {
        
    }
    
    public void setGameplay(int n)
    {
        gameplay = n;
    }
    
    public void step()
    {
        moveFrame();
        stepLocations();//moves all the entities into position, doesnt do collision detection yet
        
        for (Entity e: entities)//does the collision detection and updates everything
            e.step();
        for (Entity e: entitiesToAddThisStep)//adds the entities created this step
            entities.add(e);
        for (Nonentity e: nonentities)
            e.step();
        for (Nonentity e: nonentitiesToAddThisStep)
            nonentities.add(e);
        entitiesToAddThisStep = new ArrayList<>();
        nonentitiesToAddThisStep = new ArrayList<>();
        
        for (int i = 0; i < entities.size();i++)//takes out the trash
        {
            Entity e = entities.get(i);
            if (e.readyToBeRemoved())
            {
                deathThrow(e);
                entities.remove(e);
                i--;
            }
        }
        
        frame.repaint();
        collisions = new ArrayList<>();
    }
    
    public void run()
    {
        while (true)
        {
            step();
            pause(Global.TIME_STEP);
        }
    }
    
    public void gameOver()
    {
        double x = lowerX+Global.BOARD_SIZE.x/2-Global.BLOCK_R;
        double y = Global.BOARD_SIZE.y/2-Global.BLOCK_R;
        MarioHead m = new MarioHead(this,new DVector(x,y));
        entities.add(m);
        for (int i = 0; i < 200; i++)
        {
            m.step();
            frame.repaint();
            pause(10);
        }
        JOptionPane.showMessageDialog(null,"Game Over");
        System.exit(0);
    }
}