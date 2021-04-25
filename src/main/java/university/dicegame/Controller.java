/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university.dicegame;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.Random;
import java.util.stream.IntStream;


/**
 *
 * @author User
 */
public class Controller {
    private DiceTuple model;
    private View view;
    private DataBase db;
    private final int rolls = 300;
    int arr[] = new int[rolls];
    Map<Integer, Integer> mp = new HashMap<>();
    private JFrame frame;
    private boolean isViewPainted = false;
    
    public Controller(DiceTuple m, View v) {
        model = m;
        view = v;
        initView();
    }
    
    public void initView() {
        
        frame = new JFrame("Histogram Dice Rolling 300 times");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.repaint();
//            frame.add( view );
            frame.setLocationByPlatform( true );
        frame.pack();
        frame.setVisible( true );
    }
    
    //different events handler
    public void initController() {
        view.getExit().addActionListener(l -> sayBye());
        view.getRoll().addActionListener(l -> repaint());
    }
    
    //exit button event
    private void sayBye() {
        System.exit(0);
    }
    
    private void repaint() 
    {
        if(isViewPainted) {
            view.revalidate();
            view.repaint();
            view.bars = new ArrayList<Bar>();
        } 
        
        //prepare DB
        db = new DataBase();
        
        for(int i = 0; i < rolls ; i++ ) {
            model = new DiceTuple();
            arr[i] = model.getSum();
            if (mp.containsKey(arr[i])) {
                mp.put(arr[i], mp.get(arr[i]) + 1);
            } else {
                mp.put(arr[i], 1);
            }
        }
        
        //prepare insert query
        db.prepareStatementQuery();
        
        for (Map.Entry<Integer, Integer> entry : mp.entrySet())
        {
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            //prepare insert values
            db.prepareStatemntQueryValue(entry.getKey()-1,entry.getValue());
            view.addHistogramColumn(String.valueOf(entry.getKey()), entry.getValue(), new Color(r, g, b));
        }
        
        //insert the values
        db.InsertQuery();
        view.layoutHistogram();
        
        this.isViewPainted = true;
        frame.add(view);
    }
}
