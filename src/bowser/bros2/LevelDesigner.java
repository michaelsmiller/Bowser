/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowser.bros2;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author MichaelMiller
 */
public class LevelDesigner extends JFrame
{
    //FIX THE NONENTITY PROBLEM!!!!!!!!
    private World picture;
    private Square[] squares;
    private Square selectedSquare;
    
    public static void main(String[] args)
    {
        
    }
    
    public LevelDesigner()
    {
        super();
        setSize(500,500);
        picture = new World(this,new Dimension(getWidth()*2/3,getHeight()),0,0/*<- this is the stageLen*/);
        add(picture);
        World.readImages();
        
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(4,4));
        squares = new Square[16];
        selectedSquare = null;
    }
}

class Square extends JComponent
{
    private boolean selected;
    private 
    
    public Square(boolean b,String name)
    {
        super();
        selected = b;
    }
    
    public Square(String name)
    {
        this(false,name);
    }
    
    public boolean isSelected()
    {
        return selected;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        Dimension size = getSize();
        int x = (int)size.getWidth();
        int y = (int)size.getHeight();
        
        
        g.drawRect(0,0,x,y);
    }
}

enum Ent
{
    private String name;
    private 
}