/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowser.bros2;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author MichaelMiller
 */
public class Game 
{
    public static void main(String[] args)
    {
        JFrame f = new JFrame("Bowser Bros");
        f.setSize((int)(Global.LENGTH*Global.BLOCK_LEN),(int)(Global.HEIGHT*Global.BLOCK_LEN)+20);
        World w = new World(f,new Dimension(f.getWidth(),f.getHeight()),0,50);
        f.add(w);
        int lowest = Global.HEIGHT-1;
        for (int i = 0; i < 50; i++)
        {
            w.add(new Block(w,new Vector(i,lowest-1),Block.GROUND,Block.UNBREAKABLE));
            w.add(new Block(w,new Vector(i,lowest),Block.GROUND,Block.UNBREAKABLE));
        }
        
        //Structure.addSteps(w,new Vector(32,Global.HEIGHT-3),10);
        
        w.add(new Block(w,new Vector(10,6),Block.INVISIBLE,Block.COIN));
        w.add(new Block(w,new Vector(11,6),Block.BRICK,Block.BREAKABLE));
        //w.add(new Block(w,new Vector(12,6),Block.BRICK,Block.BREAKABLE));
        //w.add(new Block(w,new Vector(13,6),Block.BRICK,Block.BREAKABLE));
        //w.add(new Block(w,new Vector(15,9),Block.BRICK,Block.COIN));
        //w.add(new Block(w,new Vector(20,9),Block.INVISIBLE,Block.COIN));
        
        w.add(new Goomba(w,new Vector(11,4)));
        w.add(new Goomba(w,new Vector(15,4)));
        w.add(new Koopa(w,new Vector(13,5)));
        
        w.add(new Coin(w,new Vector(9,8),Vector.NONEXISTENT));
        
        w.add(new Mushroom(w,new Vector(16,7),Mushroom.POWER));
        
        w.add(new Flagpole(w,new Vector(30,Global.HEIGHT-13)));
        
        w.add(new Castle(w,new Vector(37,lowest-6),Castle.SMALL));
        
        w.sortEntities();//sorts so that koopas get priority.
        
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        w.run();
    }
}