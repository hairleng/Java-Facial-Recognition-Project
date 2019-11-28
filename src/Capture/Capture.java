/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capture;

import Principle.VisitReason;
import Util.ConnectDatabase;
import Util.ControlPerson;
import Util.ModelPerson;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Vector;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import static org.opencv.core.CvType.CV_32SC1;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.COLOR_BGRA2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author Ann
 */

 //Class Capture: to conduct face detection
public class Capture extends javax.swing.JFrame {

    private Capture.DaemonThread myThread = null;
    int count = 0;  //current number of captured photos
    VideoCapture webSource = null;  //to use video camera
    Mat frame = new Mat();
    MatOfByte mem = new MatOfByte();
    CascadeClassifier cascade = new CascadeClassifier(
            Capture.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));//detect frontal face
    MatOfRect faceDetections = new MatOfRect();

    String root, firstNamePerson, lastNamePerson, dobPerson, programPerson, gender;
    int numSamples = 5, sample = 1, idPerson;
    ConnectDatabase connectdb = new ConnectDatabase();

    class DaemonThread implements Runnable {
        protected volatile boolean runnable = false;

        @Override
        public void run() {
            synchronized (this) {
                while (runnable) {
                    if (webSource.grab()) {//open the camera
                        try {
                            webSource.retrieve(frame);// retrieve the camera live videos
                            Graphics g = label_photo.getGraphics(); //label_photo is used to show the videos

                            Mat imageColor = new Mat();
                            imageColor = frame;

                            Mat imageGray = new Mat();
                            cvtColor(imageColor, imageGray, COLOR_BGRA2GRAY);

                            cascade.load("haarcascade_frontalface_alt.xml");
                            // detect multi faces
                            cascade.detectMultiScale(imageColor, faceDetections);
                            

                            // mark detected faces with rectangle
                            for (Rect rect : faceDetections.toArray()) {
                                Imgproc.rectangle(imageColor, rect, new Scalar(255, 255, 255, 5));

                                Mat face = new Mat(imageGray, rect);// gray color photo used to recognize students
                                Imgproc.resize(face, face, new Size(160, 160));
                                Mat photo = new Mat(imageColor, rect);// colored photo used to show the picture of students
                                Imgproc.resize(photo, photo, new Size(160, 185));

                                //click the button to grab a capture and save the photo to the photos folder
                                if (saveButton.getModel().isPressed()) {
                                    System.out.println("capture is pressed");
                                    if (sample <= numSamples) {
                                        String cropped = "photos/person." + idPerson + "." + sample + ".jpg";//gray picture
                                        Imgcodecs.imwrite(cropped, face);
                                        String c = "photos/person." + idPerson + ".0.jpg"; //colored picture
                                        Imgcodecs.imwrite(c, photo);
                                        counterLabel.setText(String.valueOf(sample));
                                        sample++;
                                    } else {
                                        System.out.println("capture finished and start generate...");
                                        insertDatabase(); // save student info into the database
                                        System.out.println("File Generated");
                                    }
                                }
                            }

                            Imgcodecs.imencode(".bmp", frame, mem);
                            Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
                            BufferedImage buff = (BufferedImage) im;
                            if (g.drawImage(buff, 0, 0, getWidth(), getHeight() - 150, 0, 0, buff.getWidth(),
                                    buff.getHeight(), null)) {
                                if (runnable == false) {
                                    this.wait();
                                }
                            }

                        } catch (IOException | InterruptedException e) {
                            System.out.println("Error: " + e);
                        }
                    }
                }
            }
        }

        /**
         * insert student information into database
         */
        public void insertDatabase() {
            ControlPerson cod = new ControlPerson();
            ModelPerson mod = new ModelPerson();
            mod.setId(idPerson);
            mod.setFirst_name(firstNamePerson);
            mod.setLast_name(lastNamePerson);
            mod.setDob(dobPerson);
            mod.setProgram(programPerson);
            mod.setGender(gender);
            cod.insert(mod);

        }

        /**
         * turn off the video camera after face detection and recognition
         */
        public void stopCamera() {
            myThread.runnable = false;// stop thread
            webSource.release();// stop capturing from camera
            dispose();
        }
    }

    /**
     * Creates new form Capture
     */
    public Capture() {
        initComponents();
    }

    /**
     * create a capture object with person information
     * @param id
     * @param fName
     * @param lName
     * @param dob
     * @param program
     * @param gender
     */
    public Capture(int id, String fName, String lName, String dob, String program, String gender) {
        idPerson = id;
        firstNamePerson = fName;
        lastNamePerson = lName;
        dobPerson = dob;
        programPerson = program;
        this.gender = gender;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        label_photo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        counterLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        Jbutton2 = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Capture Photos");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(218, 218, 216));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Capture 5 snapshots");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 620, -1));

        label_photo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_photo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel1.add(label_photo, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 36, 590, 330));

        jPanel2.setBackground(new java.awt.Color(218, 218, 216));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        counterLabel.setBackground(new java.awt.Color(218, 218, 216));
        counterLabel.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        counterLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        counterLabel.setText("00");
        counterLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        counterLabel.setOpaque(true);
        jPanel2.add(counterLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 60, 40));

        jButton1.setText("Start Camera");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 110, -1));

        Jbutton2.setText("Next");
        Jbutton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Jbutton2ActionPerformed(evt);
            }
        });
        jPanel2.add(Jbutton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, -1, -1));

        saveButton.setText("Capture");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jPanel2.add(saveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 380, 590, 100));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 490));

        setSize(new java.awt.Dimension(619, 516));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        webSource = new VideoCapture(0);// video capture from default cam
        myThread = new DaemonThread();// create object of thread class
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        myThread.runnable = true;
        t.start();
        jButton1.setEnabled(false);// deactivate start button
    }// GEN-LAST:event_jButton1ActionPerformed

    private void Jbutton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_Jbutton2ActionPerformed
        System.out.println(idPerson + firstNamePerson);
        myThread.runnable = false;// stop thread
        webSource.release();// stop capturing from cam
        dispose();
        new VisitReason(idPerson, firstNamePerson, lastNamePerson, programPerson, gender).setVisible(true);
        this.setVisible(false);
    }// GEN-LAST:event_Jbutton2ActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_saveButtonActionPerformed


    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Jbutton2;
    private javax.swing.JLabel counterLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel label_photo;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}
