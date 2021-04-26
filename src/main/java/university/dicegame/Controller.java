/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university.dicegame;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private DiceTupleModel model;
    private View view;
    private DataBase db;
    private final int rolls = 300;
    int arr[] = new int[rolls];
    Map<Integer, Integer> mp;
    private JFrame frame;
    private boolean isViewPainted = false;
    
    public Controller(DiceTupleModel m, View v) {
        model = m;
        view = v;
        initView();
    }
    
    public void initView() {
        
        frame = new JFrame("Histogram Dice Rolling 300 times");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.repaint();
        
        frame.setLocationByPlatform( true );
        frame.pack();
        frame.setVisible( true );
    }
    
    //different events handler
    public void initController() {
        view.getExit().addActionListener(l -> sayBye());
        view.getRoll().addActionListener(l -> repaint());
        view.getCalendar().addPropertyChangeListener(listener -> dateListener());
    }
    
    //exit button event
    private void sayBye() {
        System.exit(0);
    }
    
    /** 
     * Used for event listener of JCalendar change
     * It will call repaint for specific date
     */
    private void dateListener() {
        
            //create sql date used for select prepare statement
           java.util.Date utilDate = view.getCalendar().getDate();
           java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
           
           //reinit components
           mp = new HashMap<>();
            
           try  {
                //Retrieve all rolls by date and show graph for the day
                ResultSet rs = db.getRollsByDate(sqlDate);
                
                view.revalidate();
                view.repaint();
                view.validate();
                view.bars = new ArrayList<Bar>();

                if(rs.next()) {
                    while(rs.next()) {
                        for(int i = 2 ; i <= 12 ; i ++) {
                            if (mp.containsKey(i)) {
                                mp.put(i, mp.get(i) + Integer.parseInt(rs.getString(i)));
                            } else {
                                mp.put(i, Integer.parseInt(rs.getString(i)));
                            }
                        }
                    }
                } else {
                    // Empty the view when there are no results found
                    view.removeAllPanels();
                }
                
                for (Map.Entry<Integer, Integer> entry : mp.entrySet())
                {
                    Random rand = new Random();
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();
                    view.addHistogramColumn(String.valueOf(entry.getKey()), entry.getValue(), new Color(r, g, b));
                }
                
                view.layoutHistogram();
                frame.setVisible( true );
           } catch (SQLException e) {
               System.out.println("Exception : " + e.getMessage());
           }
    }
    
    /** 
     * This function is used to roll dices, paint histogram
     * We check if the view is firstly created or not 
     * Then we init the db and the hashmap which is holding k,v pair with sum as key and value as frequency
     * After that we roll the dices and insert the values
     */
    private void repaint() 
    {
        if(isViewPainted) {
            view.revalidate();
            view.repaint();
            view.bars = new ArrayList<Bar>();
        } 
        
        db = new DataBase();
        mp = new HashMap<>();
        
        for(int i = 0; i < rolls ; i++ ) {
            model = new DiceTupleModel();
            arr[i] = model.getTupleSum();
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
