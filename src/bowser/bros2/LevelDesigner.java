/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowser.bros2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author MichaelMiller
 */
public class LevelDesigner extends JFrame
{
    private World picture;
    private Square[] squares;
    private Square selectedSquare;
    
    public static void main(String[] args)
    {
        LevelDesigner f = new LevelDesigner();
        f.setVisible(true);
    }
    
    public LevelDesigner()
    {
        super();
        //setLocationRelativeTo(null);
        setSize(1000,500);
        setLayout(new BorderLayout());
        double d= Global.BLOCK_LEN;
        picture = new World(this, new Dimension((int)(6*d),getHeight()),6);
        picture.setGameplay(World.LEVEL_DESIGNER);
        add(picture, BorderLayout.CENTER);
        World.readImages();
        
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(4,4));
        add(sidePanel, BorderLayout.EAST);
        
        squares = new Square[16];
        squares[0] = new Square("Bowser");
        squares[1] = new Square("BrickBlock");
        squares[2] = new Square("QuestionBlock");
        squares[3] = new Square("GroundBlock");
        squares[4] = new Square("IndestructibleBlock");
        squares[5] = new Square("PermanentBlock");
        squares[6] = new Square("Goomba");
        squares[7] = new Square("Koopa");
        squares[8] = new Square("Coin");
        squares[9] = new Square("Castle");
        squares[10] = new Square("");
        squares[11] = new Square("");
        squares[12] = new Square("");
        squares[13] = new Square("");
        squares[14] = new Square("");
        squares[15] = new Square("");
        
        for(Square s: squares)
            sidePanel.add(s);
        
        selectedSquare = null;
    }
}

class Square extends JComponent
{
    private boolean selected;
    private String id;
    
    public Square(boolean b,String name)
    {
        super();
        selected = b;
        setPreferredSize(new Dimension((int)Global.BLOCK_LEN,(int)Global.BLOCK_LEN));
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawRect(0,0,getWidth(),getHeight());
    }
    
    public Square(String name)
    {
        this(false,name);
    }
    
    public Entity createEntity(World world, Vector blockLoc)
    {
        if(id.substring(id.length()-5,id.length()).equals("Block"))
        {
            String prefix = id.substring(0,id.length()-5);
            Integer blockType = null;
            if (prefix.equals("Brick"))
                blockType = Block.BRICK;
            else if (prefix.equals("Ground"))
                blockType = Block.GROUND;
            else if (prefix.equals("Invisible"))
                blockType = Block.INVISIBLE;
            else if (prefix.equals("Permanent"))
                blockType = Block.PERMANENT;
            else if (prefix.equals("Question"))
                blockType = Block.QUESTION;
            else if (prefix.equals("Indestructible"))
                blockType = Block.INDESTRUCTIBLE;
            
            return new Block(world, blockLoc, blockType, Block.BREAKABLE);
        }
        else if (id.equals("Bowser"))
            return new Bowser(world, blockLoc);
        else if (id.equals("Castle"))
            return new Castle(world, blockLoc, Castle.SMALL);
        else if (id.equals("Flagpole"))
            return new Flagpole(world, blockLoc);
        else if (id.equals("Goomba"))
            return new Goomba(world, blockLoc);
        else if (id.equals("Koopa"))
            return new Koopa(world, blockLoc);
        
        assert false;
        return null;
    }
    
    public boolean isSelected()
    {
        return selected;
    }
    
}

enum EntityType
{
    Block(new Block());
    
    private final String type;
    private final Entity entity;

    EntityType(Entity e)
    {
        type = e.id();
        entity = e;
    }
}