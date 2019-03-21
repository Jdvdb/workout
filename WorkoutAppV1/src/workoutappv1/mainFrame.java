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
import javax.swing.JOptionPane;
import java.awt.event.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.io.*;
import javax.sound.sampled.*;
import java.net.URL;
//import sun.audio.*; does not recognize and idk why
/**
 *
 * @author jdvan
 */
public class mainFrame extends JFrame {
    
    private static SelectionPanel selectionPanel;
    private static PremadeSelectPanel premadeSelectPanel;
    private static RandomSelectPanel randomSelectPanel;
    private static WorkoutPanel workoutPanel;
    private static WorkoutCountdownPanel workoutCountdownPanel;
    private static JFrame frame;
    private static JPanel back;
    public static int timerTotal = 1000;
    public static int numberOfExercises = 0;
    public static int currentIntensity = 0;
    public static boolean workoutStart = true;
    public static int totalTimeCounter;
    public static LinkedList<String> workoutStrings = new LinkedList<String>();
    
    public static String[][] workoutData = {
            {"", "Pushups", "Plank", "Lunges", "Mountain climbers", "Jumping jacks", "Squats", "Burpees", "Spider Pushups"},//no weight 8
            {"", "Military press with weight", "Bicep curl with weight", "Deadlifts", "Lunge with weight", "One arm swing with weight", "Cross body hammer curl"},//Weight 6
            {"", "Pull ups with TRX", "One leg squats with TRX", "Row with TRX", "Tricep Extensions with TRX", "Plank with TRX", "Side plank with TRX"},//TRX 6
            {"", "Russian twists with med ball", "Squat with med ball", "Crunches with med ball", "Tricep pushup on Med Ball", "Superman with med ball", "Kneeling wood chops with med ball", "Squat with halo and med ball", "Lunge and twist with med ball", "Single leg deadlift with med ball"},//Med Ball 9                        
            {"", "Ab crunches on yoga ball", "Oblique lifts with yoga ball", "Back lift with yoga ball", "Seated balance on yoga ball", "Push-up with legs on Yoga Ball", "Plank with legs elevated on yoga ball"},//Ball 6
        };//30 different exercises
    //make variables to assign intensity to
    
    
    public mainFrame() {

        
    }
    public static void workoutNoise() {
    try
    {
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(new File("src/workoutappv1/Audio/Blooper-sound-effect.wav")));
        clip.start();
    }
    catch (Exception exc)
    {
        exc.printStackTrace(System.out);
    }
        
    }
    public static void breakNoise() {
    try
    {
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(new File("src/workoutappv1/Audio/Short-beep-noise.wav")));
        clip.start();
    }
    catch (Exception exc)
    {
        exc.printStackTrace(System.out);
    }
        
    }
    public static void repFinishNoise() {
    try
    {
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(new File("src/workoutappv1/Audio/Wrong-alert-beep-sound.wav")));
        clip.start();
    }
    catch (Exception exc)
    {
        exc.printStackTrace(System.out);
    }
        
    }
    public static void workoutCountdownClock(int i,int e) {// e = current round
    
        if (numberOfExercises == 1) {
            workoutPanel.currentExerciseLabel.setText(workoutStrings.get(numberOfExercises-1));
            workoutPanel.nextExerciseLabel.setText("Done!");
        }
        else {
            workoutPanel.currentExerciseLabel.setText(workoutStrings.get(numberOfExercises-1));
            workoutPanel.nextExerciseLabel.setText(workoutStrings.get(numberOfExercises-2));
        }
        
        Timer easyCountdownTimer;
        easyCountdownTimer = new Timer(timerTotal, new ActionListener() {//change number to 1000 for final
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
                    breakNoise();
                    System.out.println("---------------------------------" + intensity);
                    check = false;
                }
                
            }
            
        });
        easyCountdownTimer.start();
    }
    public static void workoutCountdownBreak(int i,int e) {// e = current round
        workoutPanel.currentExerciseLabel.setText("Break");
        Timer easyCountdownBreak;
        easyCountdownBreak = new Timer(timerTotal, new ActionListener() {//change number to 1000 for final
            int time = 15;
            int intensity = i;
            int exercises = e-1;
            boolean check = true;
            public void actionPerformed(ActionEvent e) {

                if (time > 1) {
                    workoutPanel.timeExerciseLabel.setText("" + time);
                    //System.out.println("time remaining " + time);
                    workoutPanel.currentExerciseLabel.setText(" Break ");                     
                    time--;
                }  
                else {
                    if (exercises == 0 && check) {
                        numberOfExercises--;
                        workoutPanel.timeExerciseLabel.setText("" + 1);
                        System.out.println("------------Finish Rep--------------" + intensity);
                        workoutAssemble(intensity);
                        repFinishNoise();
                        check = false;                
                    }
                    else if (check){
                        System.out.println("============================" + intensity);
                        check = false;
                        workoutCountdownClock(intensity, exercises);
                        workoutNoise();
                    }
                }
                
            }
            
        });
        easyCountdownBreak.start();
    }
    public static void preWorkoutCountdownClock() {// e = current round
        workoutPanel.currentExerciseLabel.setText("Break");
        Timer easyCountdownBreak;
        easyCountdownBreak = new Timer(timerTotal, new ActionListener() {//change number to 1000 for final
            int time = 5;
            boolean check = true;
            public void actionPerformed(ActionEvent e) {

                if (time > 0) {
                    workoutCountdownPanel.fiveFourThreeTwoOne.setText("" + time);                    
                    time--;
                }
                else if (check){
                    CardLayout cardLayout = (CardLayout) back.getLayout();
                    cardLayout.show(back, "Workout Panel");
                    numberOfExercises = randomSelectPanel.exerciseSlider.getValue();
                    currentIntensity = randomSelectPanel.intensitySlider.getValue();
                    workoutString(numberOfExercises);
                    workoutAssemble(randomSelectPanel.intensitySlider.getValue());
                    check = false;
                }
                
            }
            
        });
        easyCountdownBreak.start();
    }    
    
    public static void workoutTimeLeftClock(int i) {//i = difficulty e = # of exercises 
        Timer workoutTime = new Timer(timerTotal, new ActionListener() {
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
            switch (e) {
                case 1:
                    timeCalc = timeCalc + 135;
                    e--;
                    break;
                case 2:
                    timeCalc = timeCalc + 240;
                    e--;
                    break;
                default:
                    timeCalc = timeCalc + 450;
                    e--;
                    break;
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
    
    public static void workoutString(int e) {//[category][workout] also make case for none selected.
        boolean noWeights = randomSelectPanel.noWeightBox.isSelected();
        boolean weights = randomSelectPanel.weightBox.isSelected();
        boolean trx = randomSelectPanel.TRXBox.isSelected();
        boolean medBall = randomSelectPanel.medicineBallBox.isSelected();
        boolean ball = randomSelectPanel.ballBox.isSelected();
        int[][] workoutNumbers = new int[e][2];
        int exercises = e;
        while (e > 0) {
            int j = randomWorkoutNumber();
            switch (j) {
                case 0:
                    if (noWeights) {
                        double i = Math.random()*8.0;
                        int currentNumber = (int) (i+1);
                        boolean fail = false;
                        for (int test = 0; test < exercises; test++) {
                            System.out.println("j = " + j + " = " + workoutNumbers[test][0] + " currentNumber = " + currentNumber + " = " + workoutNumbers[test][1] );
                            if (j == workoutNumbers[test][0] && currentNumber == workoutNumbers[test][1]) {
                                fail = true;
                            }
                        }
                        if (!fail) {
                            workoutNumbers[e-1][0] = j;
                            workoutNumbers[e-1][1] = currentNumber;
                            System.out.println(workoutData[j][currentNumber]);
                            workoutStrings.add(workoutData[j][currentNumber]);
                            e--;
                        }
                        else {
                            System.out.println(workoutData[j][currentNumber]);
                            System.out.println("Fail");                            
                        }
                    }
                    break;
                case 1:
                    if (weights) {
                        double i = Math.random()*6.0;
                        int currentNumber = (int) (i+1);
                        boolean fail = false;
                        for (int test = 0; test < exercises; test++) {
                            System.out.println("j = " + j + " = " + workoutNumbers[test][0] + " currentNumber = " + currentNumber + " = " + workoutNumbers[test][1] );
                            if (j == workoutNumbers[test][0] && currentNumber == workoutNumbers[test][1]) {
                                fail = true;
                            }
                        }
                        if (!fail) {
                            workoutNumbers[e-1][0] = j;
                            workoutNumbers[e-1][1] = currentNumber;
                            System.out.println(workoutData[j][currentNumber]);
                            workoutStrings.add(workoutData[j][currentNumber]);
                            e--;
                        }
                        else {
                            System.out.println(workoutData[j][currentNumber]);
                            System.out.println("Fail");                            
                        }                        
                    }                    
                    break;
                case 2:
                    if (trx) {
                        double i = Math.random()*6.0;
                        int currentNumber = (int) (i+1);
                        boolean fail = false;
                        for (int test = 0; test < exercises; test++) {
                            System.out.println("j = " + j + " = " + workoutNumbers[test][0] + " currentNumber = " + currentNumber + " = " + workoutNumbers[test][1] );
                            if (j == workoutNumbers[test][0] && currentNumber == workoutNumbers[test][1]) {
                                fail = true;
                            }
                        }
                        if (!fail) {
                            workoutNumbers[e-1][0] = j;
                            workoutNumbers[e-1][1] = currentNumber;
                            System.out.println(workoutData[j][currentNumber]);
                            workoutStrings.add(workoutData[j][currentNumber]);
                            e--;
                        }
                        else {
                            System.out.println(workoutData[j][currentNumber]);
                            System.out.println("Fail");                            
                        }                        
                    }                    
                    break;
                case 3:
                    if (medBall) {
                        double i = Math.random()*9.0;
                        int currentNumber = (int) (i+1);
                        boolean fail = false;
                        for (int test = 0; test < exercises; test++) {
                            System.out.println("j = " + j + " = " + workoutNumbers[test][0] + " currentNumber = " + currentNumber + " = " + workoutNumbers[test][1] );
                            if (j == workoutNumbers[test][0] && currentNumber == workoutNumbers[test][1]) {
                                fail = true;
                            }
                        }
                        if (!fail) {
                            workoutNumbers[e-1][0] = j;
                            workoutNumbers[e-1][1] = currentNumber;
                            System.out.println(workoutData[j][currentNumber]);
                            workoutStrings.add(workoutData[j][currentNumber]);
                            e--;
                        }
                        else {
                            System.out.println(workoutData[j][currentNumber]);
                            System.out.println("Fail");                            
                        }                        
                    }                    
                    break;
                default:
                    if (ball) {
                        double i = Math.random()*6.0;
                        int currentNumber = (int) (i+1);
                        boolean fail = false;
                        for (int test = 0; test < exercises; test++) {
                            System.out.println("j = " + j + " = " + workoutNumbers[test][0] + " currentNumber = " + currentNumber + " = " + workoutNumbers[test][1] );
                            if (j == workoutNumbers[test][0] && currentNumber == workoutNumbers[test][1]) {
                                fail = true;
                            }
                        }
                        if (!fail) {
                            workoutNumbers[e-1][0] = j;
                            workoutNumbers[e-1][1] = currentNumber;
                            System.out.println(workoutData[j][currentNumber]);
                            workoutStrings.add(workoutData[j][currentNumber]);
                            e--;
                        }
                        else {
                            System.out.println(workoutData[j][currentNumber]);
                            System.out.println("Fail");                            
                        }                        
                    }                    
                    break;
            }
        }
        System.out.println(workoutStrings);
      
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
        workoutCountdownPanel = new WorkoutCountdownPanel();

        
        

        
        //add JPanel's to back
        back.add(selectionPanel, "Selection Panel");
        back.add(premadeSelectPanel, "Premade Select Panel");
        back.add(randomSelectPanel, "Random Select Panel");
        back.add(workoutPanel, "Workout Panel");
        back.add(workoutCountdownPanel, "Workout Countdown Panel");
        
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
        if (randomSelectPanel.noWeightBox.isSelected() || randomSelectPanel.weightBox.isSelected() || randomSelectPanel.TRXBox.isSelected() || randomSelectPanel.medicineBallBox.isSelected() || randomSelectPanel.ballBox.isSelected()) {
            CardLayout cardLayout = (CardLayout) back.getLayout();
            cardLayout.show(back, "Workout Countdown Panel");
            preWorkoutCountdownClock();
        }
        }
    });
    } 
    
}
