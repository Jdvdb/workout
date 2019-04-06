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
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import java.awt.event.*;
import java.util.LinkedList;
import java.io.*;
import javax.sound.sampled.*;
/**
 *
 * @author jdvan
 */
public class mainFrame extends JFrame {
    
    //Instantiating all of the different global variables and objects that will need to be accessed by the entire program.
    private static RandomSelectPanel randomSelectPanel;
    private static WorkoutPanel workoutPanel;
    private static WorkoutCountdownPanel workoutCountdownPanel;
    private static WorkoutSummaryPanel workoutSummaryPanel;
    private static JFrame frame;
    private static JPanel back;
    public int timerTotal = 1000;
    public int numberOfExercises = 0;
    public int currentIntensity = 0;
    public boolean workoutStart = true;
    public int totalTimeCounter;
    public LinkedList<String> workoutStrings = new LinkedList<String>();
    public LinkedList<Integer> intensityList = new LinkedList<Integer>();
    public savedWorkout a = new savedWorkout();
    //This is where to change workouts. Order from top to bottom is: No weight, No Equipment, TRX, Medicine Ball, Yoga Ball
    public String[][] workoutData = {
            {"", "Pushups", "Plank", "Lunges", "Mountain climbers", "Jumping jacks", "Squats", "Burpees", "Spider Pushups"},//no weight 8
            {"", "Military press with weight", "Bicep curl with weight", "Deadlifts", "Lunge with weight", "One arm swing with weight", "Cross body hammer curl"},//Weight 6
            {"", "Pull ups with TRX", "One leg squats with TRX", "Row with TRX", "Tricep Extensions with TRX", "Plank with TRX", "Side plank with TRX"},//TRX 6
            {"", "Russian twists with med ball", "Squat with med ball", "Crunches with med ball", "Tricep pushup on Med Ball", "Superman with med ball", "Kneeling wood chops with med ball", "Squat with halo and med ball", "Lunge and twist with med ball", "Single leg deadlift with med ball"},//Med Ball 9                        
            {"", "Ab crunches on yoga ball", "Oblique lifts with yoga ball", "Back lift with yoga ball", "Seated balance on yoga ball", "Push-up with legs on Yoga Ball", "Plank with legs elevated on yoga ball"},//Ball 6
        };//30 different exercises
    //make variables to assign intensity to
    
    //constructor for mainFrame class
    public mainFrame() {
        //Initializing all of the global variables and objects
        frame = new JFrame("Workout");
        back = new JPanel(new CardLayout());
        frame.setLayout(new BorderLayout());
        randomSelectPanel = new RandomSelectPanel();
        workoutPanel = new WorkoutPanel();
        workoutCountdownPanel = new WorkoutCountdownPanel();
        workoutSummaryPanel = new WorkoutSummaryPanel();

        //add JPanel's to 'back' frame
        back.add(randomSelectPanel, "Random Select Panel");
        back.add(workoutPanel, "Workout Panel");
        back.add(workoutCountdownPanel, "Workout Countdown Panel");
        back.add(workoutSummaryPanel, "Workout Summary Panel");
        
        //add back to frame
        frame.add(back);
        
        //setup frame
        frame.setSize(600,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(500, 500));
        frame.setVisible(true);
        
 

        randomSelectPanel.goLabel.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (randomSelectPanel.noWeightBox.isSelected() || randomSelectPanel.weightBox.isSelected() || randomSelectPanel.TRXBox.isSelected() || randomSelectPanel.medicineBallBox.isSelected() || randomSelectPanel.ballBox.isSelected()) {
            CardLayout cardLayout = (CardLayout) back.getLayout();
            cardLayout.show(back, "Workout Countdown Panel");
            preWorkoutCountdownClock();
        }
        else {
            JOptionPane.showMessageDialog(new JFrame(), "Please select a workout");
        }
        }
    });
        
    }
    
    public class savedWorkout {
        /*This class will turn the information from the workout into an object
        that contains an int for how many workouts are there and two linked lists.
        One that stores what the intensity of each exercise is (or how many reps)
        and another that stores the string of each exercise.
        */
        int howManyWorkouts;
        LinkedList<Integer> diffIntensity;
        LinkedList<String> theExercises = new LinkedList<>();
        
        //nothing needs to be done in the constructor.
        public savedWorkout() {
        }
        
        //saves the number of exercises into the object.        
        public void getHowManyWorkouts() {
            howManyWorkouts = numberOfExercises;
        }
        
        //this will fill in the Workout Summary Panel created at the beginning.
        public void fillInSummary() {
            diffIntensity = intensityList;
            theExercises = workoutStrings;
            System.out.println(theExercises);
            System.out.println(diffIntensity);
            
            //This loop will fill the table row by row
            for (int i = howManyWorkouts; i > 0; i--) {
                //This starts at the end of the exercises linked list (i.e the first workout) and works down the first column
                workoutSummaryPanel.workoutSummaryTable.setValueAt(theExercises.get(i - 1), (howManyWorkouts - i) + 1, 0);
                
                //This starts at the end of the intensity linked list and works down the second and third column.
                workoutSummaryPanel.workoutSummaryTable.setValueAt(diffIntensity.get(howManyWorkouts - i), (howManyWorkouts - i) + 1, 1);
                //these if statements determine what wording goes into the third panel based on the intensity.
                if (diffIntensity.get(i-1) == 3) {
                    workoutSummaryPanel.workoutSummaryTable.setValueAt("30 Seconds", i, 2);
                }
                else if (diffIntensity.get(i-1) == 4) {
                    workoutSummaryPanel.workoutSummaryTable.setValueAt("45 Seconds", i, 2);                    
                }
                else {
                    workoutSummaryPanel.workoutSummaryTable.setValueAt("1 minute", i, 2);                    
                }                
            }
        }
        
    }
    
    //These next three methods are used to get the sounds from the source packages and play them
    public void workoutNoise() {
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
    public void breakNoise() {
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
    public void repFinishNoise() {
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
    
    //This method starts the countdown for while a workout is being done.
    public void workoutCountdownClock(int i,int e) {
    
        //first it makes sure there is more than one workout left.
        if (numberOfExercises == 1) {
            workoutPanel.currentExerciseLabel.setText(workoutStrings.get(numberOfExercises-1));
            workoutPanel.nextExerciseLabel.setText("Done!");
        }
        else {
            workoutPanel.currentExerciseLabel.setText(workoutStrings.get(numberOfExercises-1));
            workoutPanel.nextExerciseLabel.setText(workoutStrings.get(numberOfExercises-2));
        }
        
        //create a timer object but do not run it.
        Timer easyCountdownTimer;
        easyCountdownTimer = new Timer(timerTotal, new ActionListener() {
            //first all of the variables this method will need are shown.
            int time = 15 + (i * 15);
            int intensity = i;
            int exercises = e;
            boolean check = true;
            
            //this is run every second to change the time shown on the JLabel and, at the end, start the break timer.
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
        //starts the timer's countdown.
        easyCountdownTimer.start();
    }
    
    //this timer works very similarily to the workout timer.
    public void workoutCountdownBreak(int i,int e) {// e = current round
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
                    /*this is one difference between this and the workout timer.
                    It determines how many more reps are left in the exercise.
                    If there are none left, it runs the method 'workout assemble.'
                    */
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
    
    //a simple countdown for the '5,4,3,2,1' 
    public void preWorkoutCountdownClock() {// e = current round
        Timer easyCountdownBreak;        
        easyCountdownBreak = new Timer(timerTotal, new ActionListener() {//change number to 1000 for final
            int time = 4;
            boolean check = true;
            public void actionPerformed(ActionEvent e) {

                if (time > 0) {
                    workoutCountdownPanel.fiveFourThreeTwoOne.setText("" + time);                    
                    time--;
                }
                
                //At the end of the countdown, it moves to the Workout Panel and calls the 'workout assemble' method.
                else if (check){
                    CardLayout cardLayout = (CardLayout) back.getLayout();
                    cardLayout.show(back, "Workout Panel");
                    //fills in the global variables 'numberOfExercises' and 'currentIntensity'
                    numberOfExercises = randomSelectPanel.exerciseSlider.getValue();
                    currentIntensity = randomSelectPanel.intensitySlider.getValue();
                    //creates a linked list in the global variable 'workoutStrings' with the number of exercises selected.
                    workoutString(numberOfExercises);
                    workoutAssemble(randomSelectPanel.intensitySlider.getValue());
                    check = false;           
                }
                
            }
            
        });
        easyCountdownBreak.start();
    }    
    
    //fills in the workoutTimeLeftClock label and starts the countdown.
    public void workoutTimeLeftClock(int i) {//i = difficulty e = # of exercises 
        Timer workoutTime = new Timer(timerTotal, new ActionListener() {
            int time = i;
            public void actionPerformed(ActionEvent e) {
                if (time > 0) {
                    //this if statement here ensures that it always has two values following the semicolon in the time.
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
    
    //this method determines how long a workout will be.
    public int getTotalTime(int i) {
        //timeCalc will be the value returned at the end of the method.
        int timeCalc = 0;
        int e = numberOfExercises - 3;
        //each switch determines what the overall time will be based on the intensity selected (i) and how many reps per exercise.
        switch (i) {
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
                else {
                    timeCalc = timeCalc + 240;
                    e--;
                }
                }
                     break;
            case 3:
                timeCalc = 615;
                while (e > 0) {
                if (e == 3) {
                    timeCalc = timeCalc + 135;
                    e--;
                }
                else {
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
                case 2:
                    timeCalc = timeCalc + 135;
                    e--;
                    break;
                case 1:
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
                if (e == 3) {
                    timeCalc = timeCalc + 135;
                    e--;
                }
                else {
                    timeCalc = timeCalc + 450;
                    e--;
                }
                }                
                     break;
            case 9:
                timeCalc = 1140;
                while (e > 0) {
                if (e == 3) {
                    timeCalc = timeCalc + 240;
                    e--;
                }
                else {
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
    
    public int randomWorkoutNumber() {
        //gets a random number between 0 and 4
        double i = Math.random()*5.0;
        int answer = (int) i;
        return answer;
    }
    
    public void workoutString(int e) {//[category][workout]
        //This method builds the linked list 'workoutStrings'
        
        //First, check to make which types of workouts are selected.
        boolean noWeights = randomSelectPanel.noWeightBox.isSelected();
        boolean weights = randomSelectPanel.weightBox.isSelected();
        boolean trx = randomSelectPanel.TRXBox.isSelected();
        boolean medBall = randomSelectPanel.medicineBallBox.isSelected();
        boolean ball = randomSelectPanel.ballBox.isSelected();
        
        //make a 2D array that stores two values that relate to the 'workoutData' 2D array.
        //This will be used to make sure the same workout is not done twice in a row.
        int[][] workoutNumbers = new int[e][2];
        int exercises = e;
        while (e > 0) {
            int j = randomWorkoutNumber();
            //this switch statements goes to the category randomly selected by the method above.
            switch (j) {
                case 0:
                    if (noWeights) {
                        //i represents the number of different types of workouts available in the type of exercise
                        double i = Math.random()*8.0;
                        int currentNumber = (int) (i+1);
                        //fail tries to catch whether or not a certain workout has been selected yet.
                        boolean fail = false;
                        //this for loop runs through the workoutNumbers 2D array and tries to check if the current
                        //selected has been done before.
                        for (int test = 0; test < exercises; test++) {
                            System.out.println("j = " + j + " = " + workoutNumbers[test][0] + " currentNumber = " + currentNumber + " = " + workoutNumbers[test][1] );
                            if (j == workoutNumbers[test][0] && currentNumber == workoutNumbers[test][1]) {
                                fail = true;
                            }
                        }
                        //this runs if fail is not true at the end of the loop.
                        if (!fail) {
                            workoutNumbers[e-1][0] = j;
                            workoutNumbers[e-1][1] = currentNumber;
                            System.out.println(workoutData[j][currentNumber]);
                            workoutStrings.add(workoutData[j][currentNumber]);
                            e--;
                        }
                        //otherwise, this runs.
                        else {
                            System.out.println(workoutData[j][currentNumber]);
                            System.out.println("Fail");                            
                        }
                    }
                    break;
                    //all other cases work in the same way.
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
    
    public void workoutAssemble(int i) {
        //i = intensity 
        /* Intensity works in sets of three
        easy = 45 x 3 = 135
        med = 60 x 4 = 240
        hard = 75 x 6 = 450
        1 - 3 easy 1:30 x3 = 4:30
        2 - 2 easy 1 med 1:00 x3 + 0:45 x4 = 6:00
        3 - 1 easy 2 med 1:00 x 3 + 1:00 x 6 = 9:00
        4 - 1 easy 2 med 0:30 x 3 + 1:30 x4 = 9:00
        5 - 3 med 2:15 x 4 = 9:15
        6 - 1 easy 1 med 1 hard 0:30 x 3 + 0:45 x 4 + 1:00 x 6 = 10:30
        7 - 2 med 1 hard 1:30 x4 + 1:00 x6 = 12:30
        8 - 1 easy 2 hard 0:30 x3 + 2:00 x6 = 13:30
        9 - 1 med 2 hard 0:45 x4 + 2:00 x6 = 15:00
        10 - 3 hard 3:00 x6 = 18:00        
        */
        //This will only run the first time workoutAssemble is called to get the total countdown clock
        //going and saves the total number of workouts to the object storing the workout data. (a)
        if (workoutStart) {
            workoutTimeLeftClock(getTotalTime(i));
            workoutStart = false;
            a.getHowManyWorkouts();
        }
        //this loop will run if there are more exercises left to do in the program.
        if (numberOfExercises > 0) {
            //each of these is called depending on the intensity and number of exercises remaining to
            //start the workout countdown clock.
   switch (currentIntensity) {//check medulo of rounds
            case 1:
                intensityList.add(3);
                workoutCountdownClock(1, 3);
                            //System.out.println(i + "lvl 1");
                     break;
            case 2:
                if (numberOfExercises % 3 > 0) {
                    intensityList.add(3);
                    workoutCountdownClock(1,3);
                }
                else {
                    intensityList.add(4);
                    workoutCountdownClock(2,4);
                }
                     break;
            case 3: 
                if (numberOfExercises % 3 > 1) {
                    intensityList.add(3);
                    workoutCountdownClock(1,3);
                }
                else {
                    intensityList.add(4);
                    workoutCountdownClock(2,4);
                }                
                     break;
            case 4:  
                if (numberOfExercises % 3 > 0) {
                    intensityList.add(3);
                    workoutCountdownClock(1,3);
                }
                else {
                    intensityList.add(6);
                    workoutCountdownClock(3,6);
                }
                     break;
            case 5:
                intensityList.add(4);
                workoutCountdownClock(2,4);
                     break;
            case 6:  
                if (numberOfExercises % 3 > 1) {
                    intensityList.add(3);
                    workoutCountdownClock(1,3);
                }
                else if (numberOfExercises % 3 == 0) {
                    intensityList.add(6);
                    workoutCountdownClock(3,6);
                }
                else {
                    intensityList.add(4);
                    workoutCountdownClock(2,4);
                }
                     break;
            case 7:  
                if (numberOfExercises % 3 > 0) {
                    intensityList.add(4);
                    workoutCountdownClock(2,4);
                }
                else {
                    intensityList.add(6);
                    workoutCountdownClock(3,6);
                }
                     break;
            case 8: 
                if (numberOfExercises % 3 > 1) {
                    intensityList.add(3);
                    workoutCountdownClock(1,3); 
                }
                else {
                    intensityList.add(6);
                    workoutCountdownClock(3,6);
                }
                     break;
            case 9: 
                if (numberOfExercises % 3 > 1) {
                    intensityList.add(4);
                    workoutCountdownClock(2,4);
                }
                else {
                    intensityList.add(6);
                    workoutCountdownClock(3,6); 
                }                
                     break;
            case 10:
                intensityList.add(6);
                workoutCountdownClock(3,6);             
                     break;
            default:
                     break;
        }
        }
        //this runs when everything else is done and moves to the summary screen and fills all the data in.
        else {
            CardLayout cardLayout = (CardLayout) back.getLayout();
            cardLayout.show(back, "Workout Summary Panel");
            a.fillInSummary();
        }
        
        
        
    }
    
    public static void main(String[] args) {
        mainFrame app = new mainFrame();
    }
    
}
