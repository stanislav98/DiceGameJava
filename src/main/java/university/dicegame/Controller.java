/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university.dicegame;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import java.util.Random;


/**
 *
 * @author User
 */
public class Controller {
    private DiceTupleModel model;
    private View view;
    //this class is used as service class to manipulate database
    private ServiceClass sc;
    private boolean withPrepare = true;

    Map<Integer, Integer> mp = new HashMap<>();
    private JFrame frame;
    //flag to check if view is pained
    private boolean isViewPainted = false;
    
    public Controller(DiceTupleModel m, View v, ServiceClass sc) {
        model = m;
        view = v;
        this.sc = sc;
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
        view.getComboBox().addActionListener(l -> dateListener());
    }
    
    //exit button event
    private void sayBye() {
        System.exit(0);
    }
    
    public Color generateNewColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r,g,b);
    }
    
    //This function is overloaded because sometimes we need to prepare 
    // the database and do the job
    public void addBars(Map<Integer, Integer> mp, boolean withPrepare) {
        for (Map.Entry<Integer, Integer> entry : mp.entrySet())
        {
            if(withPrepare) {
                sc.servicePreparedStatementKV(entry.getKey()-1,entry.getValue());
            }
            view.addHistogramColumn(String.valueOf(entry.getKey()), entry.getValue(), this.generateNewColor());
        }
    }
     public void addBars(Map<Integer, Integer> mp) {
         addBars(mp,withPrepare);
    }
    
    /** 
     * Used for event listener of JCalendar change
     * It will call repaint for specific date
     */
    private void dateListener() {
        //create sql date used for select prepare statement
           java.util.Date utilDate = new Date();
           java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
           
           //reinit components
           mp.clear();
            
           try  {
                //Retrieve all rolls by date and show graph for the day
                ResultSet rs = sc.getRollsByDate(view.getDateValue());
                
                view.revalidate();
                view.repaint();
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
                
               this.addBars(mp);
               view.layoutHistogram();
                
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
        
            //Set Dates inside combobox
            ResultSet rsDates = sc.getDates();
            try {
                if(rsDates != null) {
                    while(rsDates.next()) {
                         view.setComboBox(rsDates.getDate(1));
                    }
                } else {
                    //set default date if there is no rolls
                    java.util.Date utilDate = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                    view.setComboBox(sqlDate);
                }
            } catch (Exception e) {
                System.err.println("Some error occured while getting dates !");
                System.err.println(e.getMessage());
            }
        
        //clear the results because of dates results
        mp.clear();
        
        sc.rollDices(mp);
        //add bars with prepare statement
        this.addBars(mp,true);
        //insert the values
        sc.Insert();
        view.layoutHistogram();
        
        this.isViewPainted = true;
        frame.add(view);
    }
}
