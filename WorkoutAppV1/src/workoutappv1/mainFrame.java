/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workoutappv1;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.*;
/**
 *
 * @author jdvan
 */
public class mainFrame extends JFrame {
    
    private static SelectionPanel selectionPanel;
    private static PremadeSelectPanel premadeSelectPanel;
    private static RandomSelectPanel randomSelectPanel;
    private static WorkoutPanel workoutPanel;
    private static JFrame frame;
    private static JPanel back;
    public static int numberOfExercises = 0;
    public static int currentIntensity = 0;
    public static boolean workoutStart = true;
    public static int totalTimeCounter;
    //make variables to assign intensity to
    
    
    public mainFrame() {

        
    }
    public static void workoutCountdownClock(int i,int e) {// e = current round
        Timer easyCountdownTimer;
        easyCountdownTimer = new Timer(50, new ActionListener() {//change number to 1000 for final
            int time = 15 + (i * 15);
            int intensity = i;
            int exercises = e;
            boolean check = true;
            public void actionPerformed(ActionEvent e) {
                if (time > 1) {
                    workoutPanel.timeExerciseLabel.setText("" + time);
                    //System.out.println("time remaining " + time);
                    time--;                    
                }  
                else if (time == 1 && check) {
                    workoutPanel.timeExerciseLabel.setText("" + 1);                    
                    workoutCountdownBreak(intensity, exercises);
                    System.out.println("---------------------------------" + intensity);
                    check = false;
                }
                
            }
            
        });
        easyCountdownTimer.start();
    }
    public static void workoutCountdownBreak(int i,int e) {// e = current round
        Timer easyCountdownBreak;
        easyCountdownBreak = new Timer(50, new ActionListener() {//change number to 1000 for final
            int time = 15;
            int intensity = i;
            int exercises = e-1;
            boolean check = true;
            public void actionPerformed(ActionEvent e) {

                if (time > 1) {
                    workoutPanel.timeExerciseLabel.setText("" + time);
                    //System.out.println("time remaining " + time);
                    time--;
                }  
                else {
                    if (exercises == 0 && check) {
                        numberOfExercises--;
                        workoutPanel.timeExerciseLabel.setText("" + 1);
                        System.out.println("------------Finish Rep--------------" + intensity);
                        workoutAssemble(intensity);
                        check = false;                
                    }
                    else if (check){
                        System.out.println("============================" + intensity);
                        check = false;
                        workoutCountdownClock(intensity, exercises);
                    }
                }
                
            }
            
        });
        easyCountdownBreak.start();
    }
    
    
    public static void workoutTimeLeftClock(int i) {//i = difficulty e = # of exercises 
        Timer workoutTime = new Timer(50, new ActionListener() {
            int time = i;            
            public void actionPerformed(ActionEvent e) {
                if (time > 0) {
                    if (time % 60 >= 10) {
                        workoutPanel.timeWorkoutLabel.setText("" + time / 60 + ":" + time % 60);
                    }
                    else {
                        workoutPanel.timeWorkoutLabel.setText("" + time / 60 + ":0" + time % 60);                        
                    }
                    time--;                    
                }
                else {
                    //tell code to run next workout
                }
            }
            
        });
        workoutTime.start();
    }
    
    public static int getTotalTime(int i) {
        int timeCalc = 0;
        int e = numberOfExercises - 3;
        switch (i) {//check medulo of rounds
            case 1:  
                timeCalc = numberOfExercises * 135;
                     break;
            case 2:
                timeCalc = 510;
                while (e > 0) {
                if (e == 1 || e == 2) {
                    timeCalc = timeCalc + 135;
                    e--;
                }
                else if (e == 3) {
                    timeCalc = timeCalc + 240;
                    e--;
                }
                }
                     break;
            case 3:
                timeCalc = 615;
                while (e > 0) {
                if (e == 1) {
                    timeCalc = timeCalc + 135;
                    e--;
                }
                else if (e == 3 || e == 2) {
                    timeCalc = timeCalc + 240;
                    e--;
                }
                }                
                     break;
            case 4:
                timeCalc = 720;
                while (e > 0) {
                if (e == 1 || e == 2) {
                    timeCalc = timeCalc + 135;
                    e--;
                }
                else if (e == 3) {
                    timeCalc = timeCalc + 450;
                    e--;
                }
                }
                     break;
            case 5:  
                timeCalc = numberOfExercises * 240;                
                     break;
            case 6:
                timeCalc = 825;
                while (e > 0) {
                if (e == 1) {
                    timeCalc = timeCalc + 135;
                    e--;
                }
                else if (e == 2) {
                    timeCalc = timeCalc + 240;
                    e--;
                }
                else {
                    timeCalc = timeCalc + 450;
                    e--;
                }
                }
                     break;
            case 7: 
                timeCalc = 930;
                while (e > 0) {
                if (e == 1 || e == 2) {
                    timeCalc = timeCalc + 240;
                    e--;
                }
                else if (e == 3) {
                    timeCalc = timeCalc + 450;
                    e--;
                }
                }                
                     break;
            case 8:  
                timeCalc = 1035;
                while (e > 0) {
                if (e == 1) {
                    timeCalc = timeCalc + 135;
                    e--;
                }
                else if (e == 3 || e == 2) {
                    timeCalc = timeCalc + 450;
                    e--;
                }
                }                
                     break;
            case 9:
                timeCalc = 1140;
                while (e > 0) {
                if (e == 1) {
                    timeCalc = timeCalc + 240;
                    e--;
                }
                else if (e == 3 || e == 2) {
                    timeCalc = timeCalc + 450;
                    e--;
                }
                }                 
                     break;
            case 10:
                timeCalc = numberOfExercises * 450;
                     break;
            default:
                     break;
        }
        
        
        return timeCalc;
    }
    
    public static int randomWorkoutNumber() {//0-4
        double i = Math.random()*5.0;
        int answer = (int) i;
        return answer;
    }
    public static void workoutAssemble(int i) {//make String
        //System.out.println(i);
        //i = intensity e = # of exercises
        /* Intensity works in sets of three
        easy = 45 x 3 = 135
        med = 60 x 4 = 240
        hard = 75 x 6 = 450
        1 - 3 easy 1:30 x3 = 4:30
        2 - 2 easy 1 med 1:00 x3 + 0:45 x4 = 6:00
        3 - 2 easy 1 hard 1:00 x 3 + 1:00 x 6 = 9:00
        4 - 1 easy 2 med 0:30 x 3 + 1:30 x4 = 9:00
        5 - 3 med 2:15 x 4 = 9:15
        6 - 1 easy 1 med 1 hard 0:30 x 3 + 0:45 x 4 + 1:00 x 6 = 10:30
        7 - 2 med 1 hard 1:30 x4 + 1:00 x6 = 12:30
        8 - 1 easy 2 hard 0:30 x3 + 2:00 x6 = 13:30
        9 - 1 med 2 hard 0:45 x4 + 2:00 x6 = 15:00
        10 - 3 hard 3:00 x6 = 18:00
        
        */
        boolean noWeights = randomSelectPanel.noWeightBox.isSelected();
        boolean weights = randomSelectPanel.weightBox.isSelected();
        boolean trx = randomSelectPanel.TRXBox.isSelected();
        boolean medBall = randomSelectPanel.medicineBallBox.isSelected();
        boolean ball = randomSelectPanel.ballBox.isSelected();
        if (workoutStart) {
            workoutTimeLeftClock(getTotalTime(i));
            workoutStart = false;
        }
        
        if (numberOfExercises > 0) {
            //.println(i);
   switch (currentIntensity) {//check medulo of rounds
            case 1:  
                workoutCountdownClock(1, 3);
                            //System.out.println(i + "lvl 1");
                     break;
            case 2:
                if (numberOfExercises % 3 > 0) {
                workoutCountdownClock(1,3);
                }
                else {
                workoutCountdownClock(2,4);
                }
                     break;
            case 3: 
                if (numberOfExercises % 3 > 1) {
                    workoutCountdownClock(1,3);
                }
                else {
                    workoutCountdownClock(2,4);
                }                
                     break;
            case 4:  
                if (numberOfExercises % 3 > 0) {
                    workoutCountdownClock(1,3);
                }
                else {
                    workoutCountdownClock(3,6);
                }
                     break;
            case 5: 
                workoutCountdownClock(2,4);
                     break;
            case 6:  
                if (numberOfExercises % 3 > 1) {
                    workoutCountdownClock(1,3);
                }
                else if (numberOfExercises % 3 == 0) {
                    workoutCountdownClock(3,6);
                }
                else {
                    workoutCountdownClock(2,4);
                }
                     break;
            case 7:  
                if (numberOfExercises % 3 > 0) {
                    workoutCountdownClock(2,4);
                }
                else {
                    workoutCountdownClock(3,6);
                }
                     break;
            case 8: 
                if (numberOfExercises % 3 > 1) {
                    workoutCountdownClock(1,3); 
                }
                else {
                    workoutCountdownClock(3,6);
                }
                     break;
            case 9: 
                if (numberOfExercises % 3 > 1) {
                    workoutCountdownClock(2,4);
                }
                else {
                    workoutCountdownClock(3,6); 
                }                
                     break;
            case 10:
                workoutCountdownClock(3,6);             
                     break;
            default:
                     break;
        }
        }
        
        
        
    }
    
    public static void main(String[] args) {
        //create all frames
        frame = new JFrame("Workout");
        back = new JPanel(new CardLayout());
        frame.setLayout(new BorderLayout());
        selectionPanel = new SelectionPanel();
        premadeSelectPanel = new PremadeSelectPanel();
        randomSelectPanel = new RandomSelectPanel();
        workoutPanel = new WorkoutPanel();
        
        
        String[][] workoutData = {
            {"Pushups", "Plank", "Lunge", "Mountain climbers", "Jumping jacks", "Squat", "Burpees", "Spider Pushups"},//no weight 8
            {"Pull ups with TRX", "One leg squats with TRX", "Row with TRX", "Tricep Extensions with TRX", "Plank with TRX"},//TRX 5
            {"Military Press", "Bicep Curl", "Deadlifts", "Lunge with Weight"},//Weight 4
            {"Ab crunches on yoga ball", "Oblique lifts", "Back lift", "Seated balance", "Push-up with legs on Yoga Ball"},//Ball 5
            {"Russian twists with med ball", "Squat with med ball", "Crunches with med ball", "Tricep pushup on Med Ball", "Superman with med ball", "Kneeling wood chops with med ball", "Squat with halo and med ball", "Lunge and twist with med ball", "Single leg deadlift with med ball"},//Med Ball 9
        };//30 different exercises
        
        //add JPanel's to back
        back.add(selectionPanel, "Selection Panel");
        back.add(premadeSelectPanel, "Premade Select Panel");
        back.add(randomSelectPanel, "Random Select Panel");
        back.add(workoutPanel, "Workout Panel");
        
        //add back to frame
        frame.add(back);
        
        //initialize frame
        frame.setSize(500,500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(500, 500));
        frame.setVisible(true);
        
        //button from SelectionPanel to Random Select Panel
        selectionPanel.RandomLabel.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            CardLayout cardLayout = (CardLayout) back.getLayout();
            cardLayout.show(back, "Random Select Panel");
            
        }
    });
        //button from SelectionPanel to Premade Select Panel
        selectionPanel.PremadeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            CardLayout cardLayout = (CardLayout) back.getLayout();
            cardLayout.show(back, "Premade Select Panel");            
        }
    });
        //button from Random Select Panel to SelectionPanel
        randomSelectPanel.backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            CardLayout cardLayout = (CardLayout) back.getLayout();
            cardLayout.show(back, "Selection Panel");            
        }
    }); 
        //button from Premade Select Panel to SelectionPanel
        premadeSelectPanel.backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            CardLayout cardLayout = (CardLayout) back.getLayout();
            cardLayout.show(back, "Selection Panel");            
        }
    });
        randomSelectPanel.goLabel.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            CardLayout cardLayout = (CardLayout) back.getLayout();
            cardLayout.show(back, "Workout Panel");
            numberOfExercises = randomSelectPanel.exerciseSlider.getValue();
            currentIntensity = randomSelectPanel.intensitySlider.getValue();
            workoutAssemble(randomSelectPanel.intensitySlider.getValue());
        }
    });
    } 
    
}
