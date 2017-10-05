/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowser.bros2;

import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author MichaelMiller
 */
public class World extends JComponent
{
    private JFrame frame;
    private double lowerX;
    private final int length;
    private final long startTime,endTime;
    private int score;
    private int counter;
    
    private final ArrayList<Entity> entities;
    private ArrayList<Entity> entitiesToAddThisStep;

    public static final HashMap<String, BufferedImage> images = new HashMap<>();
    private String backgroundName;
    private ArrayList<Collision> collisions;
    
    private int gameplay;
    
    public static final int MENU = -1;
    public static final int LEVEL_DESIGNER = -2;
    public static final int NORMAL = 0;
    public static final int FLAGPOLE_ENDING = 1;
    public static final int GAMEOVER = 2;
    
    /**
     * This does everything!
     * @param f the frame
     * @param size
     * @param X
     * @param stageLen
     */
    public World(JFrame f, Dimension size, double X, int stageLen)
    {
        this(f,size,stageLen);
        //f.setSize((int)(Global.LENGTH*Global.BLOCK_LEN),(int)(Global.HEIGHT*Global.BLOCK_LEN)+20);
        frame = f;
        setSize(size);
        setLayout(null);
        frame.setTitle("Menu");
        
        lowerX = X;
        
        collisions = new ArrayList<>();
        
        entitiesToAddThisStep = new ArrayList<>();
        entities.add(new Bowser(this,new Vector(5,3)));
        
        //adds invisible barriers on either end of the map to keep bowser from walking off
        entities.add(new Obstacle(this,new Vector(-1,0),new Vector(1,Global.HEIGHT),Obstacle.INVISIBLE_WALL));
        entities.add(new Obstacle(this,new Vector(length,0),new Vector(1,Global.HEIGHT),Obstacle.INVISIBLE_WALL));
        
        frame.addKeyListener(new Adapter(this));
        
        readImages();
        backgroundName = "DefaultBackground";
        
        setFont(new Font("Serif", Font.BOLD, 12));
        
        score = 0;
        counter = 0;
        
        gameplay = MENU;
        
    }
    
    public World(JFrame f, Dimension size, int stageLen)
    {
        super();
        frame = f;
        setSize(size);
        setLayout(null);
        frame.setTitle("Designer");
        
        lowerX = 0;
        length = stageLen;
        
        //remember this
        startTime = System.currentTimeMillis();
        endTime = 0;
        
        entities = new ArrayList<>();
        
        readImages();
        backgroundName = "DefaultBackground";
        
        gameplay = LEVEL_DESIGNER;
    }
    
    public World(JFrame f, double X, int stageLen)
    {
        this(f,new Dimension(f.getWidth(),f.getHeight()),X,stageLen);
    }
    
    //this makes sure the list of entities is sorted so that it goes bowser, koopas, goombas, others.
    public void sortEntities()
    {
        for (int i = 0; i < entities.size(); i++)
        {
            Entity e = entities.get(i);
            if (e instanceof Koopa)
            {
                entities.add(1,entities.remove(i));
                i++;
            }
        }
    }
    
    /**
     * This awesome method reads all the images into a hashmap for quick use
     */
    public static void readImages()
    {
        String name="";
        try 
        {
            Scanner reader = new Scanner(World.class.getResourceAsStream("/Files/imageNames.txt"));
            while (reader.hasNext())
            {
                name = reader.nextLine();
                if (name.length()>0)
                    images.put(name,ImageIO.read(World.class.getResourceAsStream("/Pictures/"+name+".png")));
            }
        }
        catch (Exception e)
        {System.out.println("Making images didn't work\n"+name);}
    }
    
    public Block getBlockAt(DVector loc)
    {
        if (loc==null)
            return null;
        
        double r = Global.BLOCK_R;
        
        for (Entity e : entities)
            if (e instanceof Block && e.getLocation().equals(loc))
                return (Block)e;
        
        //System.err.println("Shit shit shit shit shit check getBlockAt in World");
        return null;
    }
    
    public void add(Entity e)
    {
        entities.add(e);
    }
    
    public JFrame getFrame()
    {
        return (JFrame)SwingUtilities.getWindowAncestor(this);
    }
    
    public Adapter getAdapter()
    {
        return (Adapter)getFrame().getKeyListeners()[0];
    }
    
    public void setControl(boolean b)
    {
        getAdapter().setUserControl(b);
    }
    
    public ArrayList<Entity> entitiesToAddThisStep()
    {
        return entitiesToAddThisStep;
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
    
    private BufferedImage getBack()
    {
        return images.get(backgroundName);
    }
    
    /**
     * This draws everything
     * @param g the graphics drawing everything
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        if (gameplay == MENU)
        {
            g2.drawImage(images.get("BowserHead"), 100,100,300,300,frame);
            if (counter == 1)
            {
                g2.drawString("Loading...",50,50);
                //gameplay = NORMAL;
            }
        }
        else
        {
            g2.drawImage(getBack(), 0, 0,getWidth(),getHeight(),frame);//draws background first

            ArrayList<Entity> ents = getInRangeEntities();
            ents.sort(null);
            for (Entity b: ents)
                b.draw(g2);

            double d = Global.BLOCK_LEN;
            
        }
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
        double maxLowerX = length*Global.BLOCK_LEN-X;
        
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
        if (gameplay == MENU)
        {
            frame.repaint();
            if (counter==1)
            {
                pause(2000);
                frame.setTitle("BowserBros");
                gameplay = NORMAL;
                counter = 0;
            }
            System.out.println(""+counter);
        }
        else //gameplay == NORMAL
        {
            moveFrame();
            stepLocations();//moves all the entities into position, doesnt do collision detection yet

            for (Entity e: entities)//does the collision detection and updates everything
                e.step();
            for (Entity e: entitiesToAddThisStep)//adds the entities created this step
                entities.add(e);
            entitiesToAddThisStep = new ArrayList<>();//empties that out

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
        counter++;
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