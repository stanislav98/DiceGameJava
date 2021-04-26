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
public class Main {
    public static void main(String[] args){
      DiceTupleModel m = new DiceTupleModel();
      View v = new View();
      Controller c = new Controller(m, v);
      c.initController();
    }
}
