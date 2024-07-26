import java.util.ArrayList;  // imports ArrayList
import java.util.Collections;  // imports Collections
import java.util.Comparator;  // imports Comparator
import java.util.LinkedList;  // imports LinkedList
import java.util.List;  // imports List

enum ExerciseType {  // enum to represent various exercise types

    PUSH_UPS("Push-ups",8, 20, 0000),  // push-ups
    SIT_UPS("Sit-ups", 6, 30, 0001),  // sit_ups
    STAR_JUMPS("Star Jumps", 8, 40, 0002),  // star-jumps
    TRICEP_DIPS("Tricep Dips", 5, 20, 0003),  // tricep-dips
    SQUATS("Squats", 5, 25, 0004),  // squats
    BURPEES("Burpees", 10, 10, 0005),  // burpees
    PULL_UPS("Pull-ups", 8, 10, 0007),  // pull-ups
    CHIN_UPS("Chin-ups", 8, 10, 0010);  // chin-ups
    
    String name;  // string value for each exercise to represent its name
    int METvalue;  // this is a value that represents the energy cost of physical activites
    int amountOfExercisePerMinute;  // the amount of repetitions per minute, this is needed to calulate the calories burnt for repetitions
    int identifierID;  // a way to identify the exercise

    ExerciseType(String name, int METvalue, int amountOfExercisePerMinute, int identifierID) {  // to initialise the attribute values
        this.name = name;
        this.METvalue = METvalue;
        this.amountOfExercisePerMinute = amountOfExercisePerMinute;
        this.identifierID = identifierID;
    }

}
// interface for exercise methods
interface ExerciseTracker {
    double trackExerciseCaloriesBurntByCount();  // track the calories burnt from exercises by repetitions
    void addExercise(Exercise exericse);  // add exercises to exercise arraylist
    double trackExerciseCaloriesBurntByDuration();  // track the calories burnt from exercises by duration
}
// interface for nutrition methods
interface NutritionTracker {  
    void trackCaloriesBurnt(double calories);  // tracks genreal calories burnt eg. from food
    void trackCaloriesConsumed(double calories);  // tracks caloies consumed from foods
}
// interface for sleep methods
interface SleepTracker {
    void trackSleepHours(double hours);  // tracks amount of hours of sleep
}

abstract class Feature {
    protected FitnessUser user;  // user attribute to assign each feature for specific user

    // accessor method for user
    FitnessUser getUser() {
        return this.user;
    }
}

class Exercise extends Feature {
    protected double hours;  // hours of each exercise done
    protected int count;  // count of each exericse done
    protected ExerciseType exerciseType;  // the exercise type

    // intialise attributes
    Exercise(double hours, int count, ExerciseType exerciseType) {
        this.hours = hours;
        this.count = count;
        this.exerciseType = exerciseType;
    }

    // accessor method for hours
    double getHours() {
        return this.hours;
    }
    //accessor method for count
    int getCount() {
        return this.count;
    }
    // accessor method for exercise type
    ExerciseType getExerciseType() {
        return this.exerciseType;
    }
}

class PhysicalMonitor extends Feature implements NutritionTracker, ExerciseTracker {
    List<Exercise> exercises;  // holds all exercises done
    double caloriesBurnt;  // total calories burnt 
    double caloriesConsumed;  // total calries consumed

    // initialises attributes
    PhysicalMonitor() {
        this.exercises = new ArrayList<>();
        this.caloriesBurnt = 0;
        this.caloriesConsumed = 0;
    }

    // overrides the methods from ExerciseTracker
    @Override
    // method to add exercises to the exercises done list
    public void addExercise(Exercise exercise) {
        boolean exists = false;
        // iterates through exercise objects
        for (Exercise e : this.exercises) {
            // checks if each exercise object's identifierID is equal to the exercise parameter's identifierID
            if (e.exerciseType.identifierID == exercise.exerciseType.identifierID) {
                System.out.println("You already performed this exercise");
                exists = true;
                break;
            }
        }
        // checks if the exercise dosen't exist
        if (!exists) {
            exercises.add(exercise);  // adds exercise
        }
    }
    // tracks calories burnt from exercises by each repetition
    public double trackExerciseCaloriesBurntByCount() {
        double totalCaloriesBurnt = 0;
        // iterates through exercise objects
        for (Exercise e : this.exercises) {
            totalCaloriesBurnt += (e.getExerciseType().METvalue*super.user.getWeight())/(60*e.getExerciseType().amountOfExercisePerMinute)
            *e.getCount();  // assigns the calories burnt variable the total calories burnt frome each exercise using a specific formula
        }
        return totalCaloriesBurnt;  // returns the total calories burnt
    }
    // tracks thr calories burnt from exercises by duration
    public double trackExerciseCaloriesBurntByDuration() {
        double totalCaloriesBurnt = 0;
        // iterates through exercise objects
        for (Exercise e : this.exercises) {
            // assigns the calories burnt variable the total calories burnt frome each exercise using a specific formula
            totalCaloriesBurnt += e.exerciseType.METvalue*super.user.getWeight()*e.hours;
        }
        return totalCaloriesBurnt;  // returns the total calories burnt
    }
    // overrides the methods from NutritionTracker
    @Override
    // tracks the general calories burnt 
    public void trackCaloriesBurnt(double calories) {
        this.caloriesBurnt += calories;
    }
    // tracks the general calories consumed
    public void trackCaloriesConsumed(double calories) {
        this.caloriesConsumed += calories;
    }
}

class StressMonitor extends Feature implements SleepTracker {
    double hoursOfSleep;

    // overrides the methods from SleepTracker
    @Override
    // tracks sleep hours
    public void trackSleepHours(double hours) {
        hoursOfSleep += hours;
    }
}

class DailyLog extends FitnessHistory {
    int days;
    int months;
    int year;
    String date = this.days+"/"+this.months+"/"+this.year;
    List<DailyLog> dailyLog;  // a sub-collection of the "history" collection
    List<Feature> features;  //! add features to this array
    List<Exercise> exercises;
    double caloriesBurnt;
    double caloriesConsumed;
    double caloriesBurntFromExercisesByCount;
    double caloriesBurntFromExercisesByDuration;
    double hoursOfSleep;
    double improvementPercentage;
    double bmi = this.user.getWeight()/this.user.getHeight()*this.user.getHeight();  // bmi of each user

    // initialises each attribute using polymorphism
    DailyLog(int days, int months, int year) {
        // usual initialisation of attributes
        this.days = days;
        this.months = months;
        this.year = year;
        this.date = this.days+"/"+this.months+"/"+this.year;
        this.features = new ArrayList<>();
        // polymorphic initialisation of attributes
        // iterating through feature objects
        for (Feature f : this.features) {
            // assigning the user from feature class to the user from this class
            this.user = f.user;
            // conditional statement to check if "f" is of type "PhysicalMonitor"
            if (f instanceof PhysicalMonitor) {
                PhysicalMonitor p = (PhysicalMonitor) f;  // casting f onto "PhysicalMonitor" to convert it
                // initialising each attribute 
                this.exercises = p.exercises;
                this.caloriesBurnt = p.caloriesBurnt;
                this.caloriesConsumed = p.caloriesConsumed;
                this.caloriesBurntFromExercisesByCount = p.trackExerciseCaloriesBurntByCount();
                this.caloriesBurntFromExercisesByDuration = p.trackExerciseCaloriesBurntByDuration();
            // checking if "f" is of type "StressMonitor"
            } else if (f instanceof StressMonitor) {
                StressMonitor s = (StressMonitor) f;  // casting "f" onto "StressMonitor" to convert it
                // initialising each attribute
                this.hoursOfSleep = s.hoursOfSleep;
            }
        }
    }
    // calculates improvement
    void calculateImprovement() {
        double totalImprovement = 0;
        int comparisons = 0;
        
        // iterates through indexs of the history list
        for (int i = 0; i < super.history.size()-1; i++) {
            DailyLog currentLog = super.history.get(i);  // assigning current log with the current index we are looping through
            DailyLog nextLog = super.history.get(i+1);  // assigning next log with the next index we are looping through
    
            // assigning daily improvement as a percentage using specific formula
            double dailyImprovement = ((nextLog.bmi-currentLog.bmi)/currentLog.bmi)*100;
            totalImprovement += dailyImprovement;  // then plussing that daily improvement to the total improvement
            comparisons++; // then incrementing comparisons by 1 each time
        }
        
        // checks if comparisons is greater than 0
        if (comparisons > 0) {
            double averageImprovement = totalImprovement/comparisons;  // getting average improvement
    
            // checking if the users goal is to lose weight
            if (this.user.getGoal().equals(Goal.LOSE_WEIGHT)) {
                this.improvementPercentage += -averageImprovement; // negative improvement is good for weight loss
                // checks if users goal is to gain weight
            } else if (this.user.getGoal().equals(Goal.GAIN_WEIGHT)) {
                this.improvementPercentage += averageImprovement; // positive improvement is good for weight gain
                // checks if users goal is to maintain weight
            } else if (this.user.getGoal().equals(Goal.MAINTAIN_WEIGHT)) {
                // for maintaining weight, we'll return a negative value regardless of direction
                if (averageImprovement > 0) {
                    this.improvementPercentage += -averageImprovement;
                } else {
                    this.improvementPercentage += averageImprovement;
                }
            }
        }
        this.improvementPercentage += 0; // assign 0 if there's not enough history to calculate improvement
    }

    // accessor method
    String getDate() {
        return this.date;
    }
    // accessor method
    int getDays() {
        return this.days;
    }
    // accessor method
    int getMonths() {
        return this.months;
    }
    // accessor method
    int getYear() {
        return this.year;
    }
    // accessor method
    double getHoursOfSleep() {
        return this.hoursOfSleep;
    }
    // accessor method
    List<Exercise> getExericses() {
        return this.exercises;
    }
    // accessor method
    double caloriesBurnt() {
        return this.caloriesBurnt;
    }
    // accessor method
    double caloriesConsumed() {
        return this.caloriesConsumed;
    }
    // accessor method
    double getCaloriesBurntFromExercisesByCount() {
        return this.caloriesBurntFromExercisesByCount;
    }
    // accessor method
    double getCaloriesBurntFromExercisesByDuration() {
        return this.caloriesBurntFromExercisesByDuration;
    }

    // creates a sub-collection of history arraylist
    void addDailyLog(int choice) {
        if (choice > 0 && choice <= super.history.size()) {  // checks if "choice" is between 1 and the size of history
            this.dailyLog = super.history.subList(choice-1, choice);  // will creates a sub-list of history and assigns it to "dailylog"
        } else {
            System.out.println("Please enter a valid amount");
        }
    }

    // this method allows the user to view a specific day from the history array
    void viewDailyLog() {
        // iterating through the day object
        for (DailyLog d : this.dailyLog) {
            System.out.println("--------------------------------");
            System.out.println("Date: "+d.date+"\n"+  // displaying the date first
                            "Weight: "+d.user.getWeight()+"\n"+  // displaying the users weight on that specific day
                            "BMI: "+d.bmi+"\n"+  // displaying the users BMI on that specific day
                            "Improvement Percetange: "+d.improvementPercentage+"%"+"\n"+  // shows their improvement percentage
                            "General Calories Burnt: "+d.caloriesBurnt()+"\n"+  // the general calories burnt that day
                            "General Calories Consumed: "+d.caloriesConsumed());  // general caloires consumed that day
                            // checks if the user's calorie caluclation is per rep 
                            if (this.user.getCaloriesCalculation().equals(CalculateExcerciseCalories.PER_EXCERCISE)) {
                                // outputing calories burnt from exercises using the by count method
                                System.out.println("Calories Burnt from Exercises: "+d.getCaloriesBurntFromExercisesByCount());
                                // checking if the user's calorie calculation is by duration
                            } else if (this.user.getCaloriesCalculation().equals(CalculateExcerciseCalories.DURATION_OF_EXCERCISE)) {
                                // outputing calories burnt from exercises using the by duration method
                                System.out.println("Calories Burnt from Exercises: "+d.getCaloriesBurntFromExercisesByDuration());
                            }
                            // printing exercises details
                            System.out.println("Exercises: ");
                            // checks if the user's calorie caluclation is per rep
                            if (this.user.getCaloriesCalculation().equals(CalculateExcerciseCalories.PER_EXCERCISE)) {
                                // iterating through each exercise object in the exercises array
                                for (Exercise e : d.exercises) {
                                    // outputing each exercise name, as well as the repetition
                                    System.out.println("  • "+e.exerciseType.name+": \n     • Repetition: "+e.count);
                                }
                                // checks if the user's calorie caluclation is by duration
                            } else if (this.user.getCaloriesCalculation().equals(CalculateExcerciseCalories.DURATION_OF_EXCERCISE)) {
                                // iterating through each exercise object in the exercises array
                                for (Exercise e : d.exercises) {
                                    // outputing each exercise name, as well as the duration in hours
                                    System.out.println("  • "+e.exerciseType.name+": \n     • Hours: "+e.hours);
                                }
                            }
                            // then outputing the sleep hours
                            System.out.println("Sleep Hours: "+d.hoursOfSleep);

            System.out.println("--------------------------------");
        }
    }
}

class FitnessHistory extends Feature {
    // holds the history of the user
    protected List<DailyLog> history;

    // initialises the history attribute
    FitnessHistory() {
        // using linked list for variety
        this.history = new LinkedList<>();
    }
    // method to add dailylogs to history
    void addDailyLog(DailyLog dailyLog) {
        boolean exists = false;
        // iteratiing through daily log objects
        for (DailyLog d : this.history) {
            // checks if date equals to the date of the parameter
            if (d.date.equals(dailyLog.date)) {
                System.out.println("Daily-Log already admitted");
                exists = true;
                break;
            }
        }
        // checks if the object doesn't exist
        if (!exists) {
            this.history.add(dailyLog);  // adds that parameter dailylog object
        }
    }
    // method to replace dailylog object
    void replaceDailyLog(DailyLog dailyLog) {
        int indexToRemove = -1;  // declaring a variable and assigning -1 to check if it equals to -1 later in the method
        
        // iterates through 0 = i - size of history array, adding 1 to i each iteration
        for (int i = 0; i < this.history.size(); i++) {
            // checking if history contains a DailyLog object with date equal to "dailyLog"
            if (this.history.get(i).date.equals(dailyLog.date)) {
                indexToRemove = i;  // assigns the index which is i in this case to "indexToRemove" variable that was declared earlier
                break;
            }
        }
        if (indexToRemove != -1) {  // checks if "indexToRemove" is equal to -1
            this.history.remove(indexToRemove);  // removes the indexToRemove
            this.history.add(dailyLog);  // adds the dailyLog parameter
        }
    }
    // method to add to the exercise count
    void addToExerciseCount(int count, Exercise exercise) {
        exercise.count += count;
    }
    // method to add to the exercise duration
    void addToExerciseCountDuration(double hours, Exercise exercise) {
        exercise.hours += hours;
    }
    // method to remove from exercise count
    void removeFromExerciseCount(int count, Exercise exercise) {
        exercise.count -= count;
    }
    // method to remove from the exercise duration
    void removeFromExerciseDuration(double hours, Exercise exercise) {
        exercise.hours -= hours;
    }
    // method to remove dailylogs 
    void removeDailyLog(DailyLog dailyLog) {
        int indexToRemove = -1;  // declaring a variable and assigning -1 to check if it equals to -1 later in the method
        
        // iterates through 0 = i - size of history array, adding 1 to i each iteration
        for (int i = 0; i < this.history.size(); i++) {
            // checking if history contains a DailyLog object with date equal to "dailyLog"
            if (this.history.get(i).date.equals(dailyLog.date)) {
                indexToRemove = i;  // assigns the index which is i in this case to "indexToRemove" variable that was declared earlier
                break;  // breaks out of the loop
            }
        }
        // checks if "indexToRemove" is equal to -1
        if (indexToRemove != -1) {
            this.history.remove(indexToRemove);  // removes the indexToRemove
        }
    }
    // method to allow the user to view all of the history but with less details
    void viewFitnessHistory() {
        boolean firstIndex = true;

        // comparator declared to compare dates between dates
        final Comparator<DailyLog> comparatorForDates = Comparator.comparing(DailyLog::getYear)
        .thenComparing(DailyLog::getMonths).thenComparing(DailyLog::getDays);

        // sorts the history list using the comparator declared above
        Collections.sort(this.history, comparatorForDates);

        int index = 1;

        // iterating through dailylog objects
        for (DailyLog d : this.history) {
            // minor string formatting
            // displays the users name
            System.out.println("Fitness History for "+d.user.getName());
            // displays the number of daily log reprented in a index variable that is incremeted by one each iteration
            System.out.println(index+".");
            System.out.println("--------------------------------");
            // displays the date, BMI and improvement percentage
            System.out.println("Date: "+d.date+"\n"+
                                "BMI: "+d.bmi+"\n"+
                                "Improvement Percetange: "+d.improvementPercentage+"%");
            System.out.println("--------------------------------");
            // increment index by 1
            index++;
        }
    }   
}
