/*
 * This class is used to as Model. Generates random sum between 1-12
 * and retrieves the values for the current Tuple
 */
package university.dicegame;

import java.util.Random;

/**
 *
 * @author User
 */
public class DiceTuple {
    private int sum;
    private final int lowest = 2;
    private final int highest = 13;
    
    
    public DiceTuple() {
        this.setSum();
    }
    
    public int getSum() {
        return this.sum;
    }
    
    public void setSum() {
        Random r = new Random();
        this.sum =  r.nextInt(highest-lowest) + lowest;
    }
}
