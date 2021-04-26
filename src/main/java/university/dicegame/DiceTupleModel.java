/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university.dicegame;

/**
 *
 * @author User
 */
public class DiceTupleModel extends Dice {
    
    private Dice d1;
    private Dice d2;
    
    public DiceTupleModel() {
        d1 = new Dice();
        d2 = new Dice();
    }
    
    public int getFirstDiceSum() {
        return d1.getSum();
    }
    
    public int getSecondDiceSum() {
        return d2.getSum();
    }
    
    public int getTupleSum() {
        return d1.getSum() + d2.getSum();
    }
    
}
