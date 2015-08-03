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
public class Fire extends Nonentity
{

    public Fire(World w, DVector s, DVector loc) 
    {
        super(w, s, loc);
        imageName = "Fire1";
    }

    @Override
    protected void updateImageName() 
    {
        if (counter%20<10)
            imageName = "Fire1";
        else imageName = "Fire2";
    }

    @Override
    protected void finalUpdates() 
    {
        counter++;
    }

    @Override
    public boolean readyToBeRemoved() 
    {
        return false;
    }
    
}
