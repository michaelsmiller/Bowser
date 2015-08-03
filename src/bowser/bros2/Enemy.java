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
public interface Enemy 
{
    public boolean dying();//refers to
    public void getStomped();
    public void dieFromBlockHit();
}
