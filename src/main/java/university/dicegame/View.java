/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university.dicegame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;

//imported for chart
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
/**
 *
 * @author User
 */
public class View extends JPanel {
// View uses Swing framework to display UI to user
    private int histogramHeight = 200;
    private int barWidth = 50;
    private int barGap = 10;

    private JPanel barPanel;
    private JPanel labelPanel;
    private JPanel buttonsPanel;
    
    private JButton exitButton;
    private JButton rollButton;
    
    public List<Bar> bars = new ArrayList<Bar>();

    public View()
    {
        setBorder( new EmptyBorder(10, 10, 10, 10) );
        setLayout( new BorderLayout() );
        
        //create bars layout
        barPanel = new JPanel( new GridLayout(1, 0, barGap, 0) );
        Border outer = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Border inner = new EmptyBorder(10, 10, 0, 10);
        Border compound = new CompoundBorder(outer, inner);
        barPanel.setBorder( compound );
        
        //create labels layout
        labelPanel = new JPanel( new GridLayout(1, 0, barGap, 0) );
        labelPanel.setBorder( new EmptyBorder(5, 10, 0, 10) );
        
        // Add button panel on right and set text for buttons
        buttonsPanel = new JPanel(  new GridLayout(1, 2, 0, 0) );
        exitButton = new JButton("Exit!");
        rollButton = new JButton("Roll Again!");
        
        buttonsPanel.add(rollButton);
        buttonsPanel.add(exitButton);
        
        //add all layouts to jframe
        add(barPanel, BorderLayout.CENTER);
        add(labelPanel, BorderLayout.PAGE_END);
        add(buttonsPanel,BorderLayout.PAGE_START);
        
    }

    public void addHistogramColumn(String label, int value, Color color)
    {
        Bar bar = new Bar(label, value, color);
        bars.add( bar );
    }
    
    public void removeAllPanels() {
        barPanel.removeAll();
        labelPanel.removeAll();
    }
    
    public void layoutHistogram()
    {
        removeAllPanels();

        int maxValue = 0;
        
        for (Bar bar: bars) {
            maxValue = Math.max(maxValue, bar.getValue());
        }
        for (Bar bar: bars)
        {
            JLabel label = new JLabel(bar.getValue() + "");
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.TOP);
            label.setVerticalAlignment(JLabel.BOTTOM);
            int barHeight = (bar.getValue() * histogramHeight) / maxValue;
            Icon icon = new ColorIcon(bar.getColor(), barWidth, barHeight);
            label.setIcon( icon );
            barPanel.add( label );

            JLabel barLabel = new JLabel( bar.getLabel() );
            barLabel.setHorizontalAlignment(JLabel.CENTER);
            labelPanel.add( barLabel );
        }
        
    }
    
    public JButton getExit() {
        return this.exitButton;
    }
    
    public JButton getRoll() {
        return this.rollButton;
    }
}
