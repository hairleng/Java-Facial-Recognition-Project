/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principle;
import Util.ConnectDatabase;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.chart.plot.PlotOrientation; 
import org.opencv.core.Core;
/**
 *
 * @author liko97
 */
public class RReport extends javax.swing.JFrame {
    ConnectDatabase connectdb = new ConnectDatabase();
    static String startDate;
    static String endDate;
    int ct;
    ArrayList reasonD = new ArrayList();
    /**
     * Creates new form RReport
     */
    public RReport(String startDate, String endDate) {
        
        initComponents();
        this.startDate = startDate;
        this.endDate = endDate;
        
         JFreeChart chart = ChartFactory.createBarChart(   
                    "Students Visiting", // The title of the chart
                    "Reasons", // The label of X axis
                    "Frequecny", // The label of Y axis
                    initJframe(startDate, endDate), // Dataset
                    PlotOrientation.HORIZONTAL, // The direction of chart
                    true, // Display legend
                    true, // Generate tool
                    false // Not display URL link
                    );
         ChartPanel cpp = new ChartPanel(chart);
         jPanel1.setBounds(10, 300, 800, 800); // Set panel bound
         cpp.setBounds(10,10 , 750, 500); // Set the chart bound
         jPanel1.add(cpp);
         jPanel1.setVisible(true);
         cpp.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Visit Reason Report");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBounds(new java.awt.Rectangle(10, 300, 330, 250));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 795, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 491, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(879, 591));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new Menu().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * This method is to select records by categories for a given date range
     * @param The start date
     * @param The end date
     * @return The dataset of required records
     */
    private DefaultCategoryDataset initJframe(String sd, String ed){
        try {
            String SQL = "SELECT COUNT(DISTINCT Visit_Reason) FROM VisitRecord where Visit_Time BETWEEN STR_TO_DATE('"+sd+"','%Y-%m-%d') AND STR_TO_DATE('"+ed+"','%Y-%m-%d')";
            connectdb.connect();
            connectdb.executeSQL(SQL);
            connectdb.rs.next();
            String cts=connectdb.rs.getString(1);
            ct=Integer.parseInt(cts);// Get the number of required records.
        } catch (Exception e) {
            System.out.println("count Error: " + e);
        }
        try {
            String SQLA = "SELECT DISTINCT Visit_Reason FROM VisitRecord where Visit_Time BETWEEN STR_TO_DATE('"+sd+"','%Y-%m-%d') AND STR_TO_DATE('"+ed+"','%Y-%m-%d')";
            connectdb.connect();
            connectdb.executeSQL(SQLA);
            connectdb.rs.next();
            reasonD.add(connectdb.rs.getString(1));
            while(connectdb.rs.next()){
                reasonD.add(connectdb.rs.getString(1));// Store the records in an ArrayList.
            }
            System.out.println(reasonD.get(1));
        } catch (Exception e) {
            System.out.println("arraylist Error: " + e);
        }
        connectdb.disconnect();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int i=0;i<ct;i++){
            try {
            String SQL = "SELECT COUNT(*) FROM VisitRecord WHERE Visit_Reason="+"'"+String.valueOf(reasonD.get(i))+"'"+"and Visit_Time BETWEEN STR_TO_DATE('"+sd+"','%Y-%m-%d') AND STR_TO_DATE('"+ed+"','%Y-%m-%d')";
            connectdb.connect();
            connectdb.executeSQL(SQL);
            connectdb.rs.next();
            String ps=connectdb.rs.getString(1);
            int tempp=Integer.parseInt(ps);
            String[] reasonsplit = String.valueOf(reasonD.get(i)).split(" ");
            String reasonlines = "";
            for(String reason : reasonsplit) {
            	reasonlines += reason+" " ;
            }
            dataset.setValue(tempp,"Frequency",reasonlines);// Add records into dataset.

        } catch (Exception e) {
            System.out.println("get Error: " + e);// Error handling
        }
        }
    return dataset;}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
