import java.util.ArrayList;  // imports ArrayList
import java.util.Collections;  // imports Collections
import java.util.Comparator;  // imports Comparator
import java.util.LinkedList;  // imports LinkedList
import java.util.List;  // imports List

enum ExerciseType {

    PUSH_UPS("Push-ups",8, 20, 0000),
    SIT_UPS("Sit-ups", 6, 30, 0001),
    STAR_JUMPS("Star Jumps", 8, 40, 0002),
    TRICEP_DIPS("Tricep Dips", 5, 20, 0003),
    SQUATS("Squats", 5, 25, 0004),
    BURPEES("Burpees", 10, 10, 0005),
    PULL_UPS("Pull-ups", 8, 10, 0007),
    CHIN_UPS("Chin-ups", 8, 10, 0010);
    
    String name;
    int METvalue;  // this is a value that represents the energy cost of physical activites
    int amountOfExercisePerMinute;  // the amount of repetitions per minute, this is needed to calulate the calories burnt for repetitions
    int identifierID;  // a way to identify the exercise

    ExerciseType(String name, int METvalue, int amountOfExercisePerMinute, int identifierID) {
        this.name = name;
        this.METvalue = METvalue;
        this.amountOfExercisePerMinute = amountOfExercisePerMinute;
        this.identifierID = identifierID;
    }

}

interface ExerciseTracker {
    double trackExerciseCaloriesBurntByCount();
    void addExercise(Exercise exericse);
    double trackExerciseCaloriesBurntByDuration();
}

interface NutritionTracker {
    void trackCaloriesBurnt(double calories);
    void trackCaloriesConsumed(double calories);
}
interface SleepTracker {
    void trackSleepHours(double hours);
}

abstract class Feature {
    protected FitnessUser user;

    FitnessUser getUser() {
        return this.user;
    }
}

class Exercise extends Feature {
    protected double hours;
    protected int count;
    protected ExerciseType exerciseType;

    Exercise(double hours, int count, ExerciseType exerciseType) {
        this.hours = hours;
        this.count = count;
        this.exerciseType = exerciseType;
    }

    double getHours() {
        return this.hours;
    }
    int getCount() {
        return this.count;
    }

    ExerciseType getExerciseType() {
        return this.exerciseType;
    }
}

class PhysicalMonitor extends Feature implements NutritionTracker, ExerciseTracker {
    List<Exercise> exercises;
    double caloriesBurnt;
    double caloriesConsumed;

    PhysicalMonitor() {
        this.exercises = new ArrayList<>();
        this.caloriesBurnt = 0;
        this.caloriesConsumed = 0;
    }

    @Override
    public void addExercise(Exercise exercise) {
        boolean exists = false;
        for (Exercise e : this.exercises) {
            if (e.exerciseType.identifierID == exercise.exerciseType.identifierID) {
                System.out.println("You already performed this exercise");
                exists = true;
                break;
            }
        }
        if (!exists) {
            exercises.add(exercise);
        }
    }
    public double trackExerciseCaloriesBurntByCount() {
        double totalCaloriesBurnt = 0;

        for (Exercise e : this.exercises) {
            totalCaloriesBurnt += (e.getExerciseType().METvalue*super.user.getWeight())/(60*e.getExerciseType().amountOfExercisePerMinute)
            *e.getCount();
        }
        return totalCaloriesBurnt;
    }
    public double trackExerciseCaloriesBurntByDuration() {
        double totalCaloriesBurnt = 0;
        for (Exercise e : this.exercises) {
            totalCaloriesBurnt += e.exerciseType.METvalue*super.user.getWeight()*e.hours;
        }
        return totalCaloriesBurnt;
    }
    @Override
    public void trackCaloriesBurnt(double calories) {
        this.caloriesBurnt += calories;
    }
    public void trackCaloriesConsumed(double calories) {
        this.caloriesConsumed += calories;
    }
}

class StressMonitor extends Feature implements SleepTracker {
    double hoursOfSleep;

    @Override
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
    double bmi = this.user.getWeight()/this.user.getHeight()*this.user.getHeight();



    DailyLog() {
        this.date = this.days+"/"+this.months+"/"+this.year;
        this.features = new ArrayList<>();
        for (Feature f : this.features) {
            this.user = f.user;
            if (f instanceof PhysicalMonitor) {
                PhysicalMonitor p = (PhysicalMonitor) f;
                this.exercises = p.exercises;
                this.caloriesBurnt = p.caloriesBurnt;
                this.caloriesConsumed = p.caloriesConsumed;
                this.caloriesBurntFromExercisesByCount = p.trackExerciseCaloriesBurntByCount();
                this.caloriesBurntFromExercisesByDuration = p.trackExerciseCaloriesBurntByDuration();
            } else if (f instanceof StressMonitor) {
                StressMonitor s = (StressMonitor) f;
                this.hoursOfSleep = s.hoursOfSleep;
            }
        }
    }
  
    void calculateImprovement() {
        double totalImprovement = 0;
        int comparisons = 0;
    
        for (int i = 0; i < super.history.size()-1; i++) {
            DailyLog currentLog = super.history.get(i);
            DailyLog nextLog = super.history.get(i+1);
    
            double dailyImprovement = ((nextLog.bmi-currentLog.bmi)/currentLog.bmi)*100;
            totalImprovement += dailyImprovement;
            comparisons++;
        }
    
        if (comparisons > 0) {
            double averageImprovement = totalImprovement/comparisons;
    
            if (this.user.getGoal().equals(Goal.LOSE_WEIGHT)) {
                this.improvementPercentage += -averageImprovement; // negative improvement is good for weight loss
            } else if (this.user.getGoal().equals(Goal.GAIN_WEIGHT)) {
                this.improvementPercentage += averageImprovement; // positive improvement is good for weight gain
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

    String getDate() {  // accessor method
        return this.date;
    }
    int getDays() {
        return this.days;
    }
    int getMonths() {
        return this.months;
    }
    int getYear() {
        return this.year;
    }
    double getHoursOfSleep() {
        return this.hoursOfSleep;
    }
    List<Exercise> getExericses() {
        return this.exercises;
    }
    double caloriesBurnt() {
        return this.caloriesBurnt;
    }
    double caloriesConsumed() {
        return this.caloriesConsumed;
    }
    double getCaloriesBurntFromExercisesByCount() {
        return this.caloriesBurntFromExercisesByCount;
    }
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
    void viewDailyLog() {
        for (DailyLog d : dailyLog) {
            System.out.println("--------------------------------");
            System.out.println("Date: "+d.date+"\n"+
                            "Weight: "+d.user.getWeight()+"\n"+
                            "BMI: "+d.bmi+"\n"+
                            "Improvement Percetange: "+d.improvementPercentage+"%"+"\n"+
                            "General Calories Burnt: "+d.caloriesBurnt()+"\n"+
                            "General Calories Consumed: "+d.caloriesConsumed());
                            if (this.user.getCaloriesCalculation().equals(CalculateExcerciseCalories.PER_EXCERCISE)) {
                                System.out.println("Calories Burnt from Exercises: "+d.getCaloriesBurntFromExercisesByCount());
                            } else if (this.user.getCaloriesCalculation().equals(CalculateExcerciseCalories.DURATION_OF_EXCERCISE)) {
                                System.out.println("Calories Burnt from Exercises: "+d.getCaloriesBurntFromExercisesByDuration());
                            }
                            
                            System.out.println("Exercises: ");
                            if (this.user.getCaloriesCalculation().equals(CalculateExcerciseCalories.PER_EXCERCISE)) {
                                for (Exercise e : d.exercises) {
                                    System.out.println("  • "+e.exerciseType.name+": \n     • Repetition: "+e.count);
                                }
                            } else if (this.user.getCaloriesCalculation().equals(CalculateExcerciseCalories.DURATION_OF_EXCERCISE)) {
                                for (Exercise e : d.exercises) {
                                    System.out.println("  • "+e.exerciseType.name+": \n     • Hours: "+e.hours);
                                }
                            }
                            System.out.println("Sleep Hours: "+d.hoursOfSleep);

            System.out.println("--------------------------------");
        }
    }
}

class FitnessHistory extends Feature {
    protected List<DailyLog> history;

    FitnessHistory() {
        this.history = new LinkedList<>();
    }

    void addDailyLog(DailyLog dailyLog) {
        boolean exists = false;
        for (DailyLog d : this.history) {
            if (d.date.equals(dailyLog.date)) {
                System.out.println("Daily-Log already admitted");
                exists = true;
                break;
            }
        }
        if (!exists) {
            this.history.add(dailyLog);
        }
    }

    void replaceDailyLog(DailyLog dailyLog) {
        int indexToRemove = -1;  // declaring a variable and assigning -1 to check if it equals to -1 later in the method
        
        for (int i = 0; i < this.history.size(); i++) {  // iterates through 0 = i - size of history array, adding 1 to i each iteration
            if (this.history.get(i).date.equals(dailyLog.date)) {  // checking if history contains a DailyLog object with date equal to "dailyLog"
                indexToRemove = i;  // assigns the index which is i in this case to "indexToRemove" variable that was declared earlier
                break;
            }
        }
        if (indexToRemove != -1) {  // checks if "indexToRemove" is equal to -1
            this.history.remove(indexToRemove);  // removes the indexToRemove
            this.history.add(dailyLog);  // adds the dailyLog parameter
        }
    }
    void addToExerciseCount(int count, Exercise exercise) {
        exercise.count += count;
    }
    void addToExerciseCountDuration(double hours, Exercise exercise) {
        exercise.hours += hours;
    }
    void removeFromExerciseCount(int count, Exercise exercise) {
        exercise.count -= count;
    }
    void removeFromExerciseDuration(double hours, Exercise exercise) {
        exercise.hours -= hours;
    }

    void removeDailyLog(DailyLog dailyLog) {
        int indexToRemove = -1;
    
        for (int i = 0; i < this.history.size(); i++) {
            if (this.history.get(i).date.equals(dailyLog.date)) {
                indexToRemove = i;
                break;
            }
        }
        if (indexToRemove != -1) {
            this.history.remove(indexToRemove);
        }
    }
    
    void viewFitnessHistory() {
        boolean firstIndex = true;
        final Comparator<DailyLog> comparatorForDates = Comparator.comparing(DailyLog::getYear)
        .thenComparing(DailyLog::getMonths).thenComparing(DailyLog::getDays);

        Collections.sort(this.history, comparatorForDates);

        int index = 1;

        for (DailyLog d : this.history) {
            System.out.println("Fitness History for "+d.user.getName());
            System.out.println(index+".");
            System.out.println("--------------------------------");
            System.out.println("Date: "+d.date+"\n"+
                                "BMI: "+d.bmi+"\n"+
                                "Improvement Percetange: "+d.improvementPercentage+"%");
            System.out.println("--------------------------------");
            index++;
        }
    }   
}