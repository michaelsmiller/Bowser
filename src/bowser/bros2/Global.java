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
public final class Global 
{
    public static final double GRAVITY = .3;//acceleration due to gravity
    public static final double MAX_FALLING_SPEED = 6;//terminal velocity
    public static final double JUMPING_SPEED = 8;//initial jumping speed
    public static final double WALKING_SPEED = 2;
    public static final double GOOMBA_SPEED = .5;
    public static final int GOOMBA_DEATH_DELAY = 40;
    public static final double SHELL_SPEED_MULTIPLIER = 2;
    public static final int COIN_SPIN_DELAY = 10;
    public static final double MUSHROOM_SPEED = 1;
    public static final double FIREBALL_SPEED = 5;
    public static final double FRICTION = 3;//acceleration due to friction on the ground
    public static final double BLOCK_LEN = 27;//the length of one block
    public static final double BLOCK_R = BLOCK_LEN/2;//half block length
    public static final int HEIGHT = 15;//height of frame in blocks
    public static final int LENGTH = 25;//length of frame in blocks
    public static final int TIME_STEP = 10;//the time delay between steps in milliseconds
    public static final int TIME_PER_BOWSER_STEP = 20;
    public static final int TIME_PER_GOOMBA_STEP = 30;
    public static final int EXPLOSION_TIME = 20;//duration of explosion animation
    public static final int HURT_TIME = 100;//frames during which bowser is invincible after being hurt
    public static final double FLAGPOLE_SPEED = 2;
    public static final int FIRE_DELAY = 30;
    
    public static final DVector BOARD_SIZE = new DVector(LENGTH*BLOCK_LEN,HEIGHT*BLOCK_LEN);
}