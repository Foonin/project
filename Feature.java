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

// interface for sleep methods
interface SleepTracker {
    void trackSleepHours(double hours);  // tracks amount of hours of sleep
}

class Feature {
    protected FitnessUser user;  // user attribute to assign each feature for specific user

    Feature(FitnessUser user) {
        this.user = user;
    }

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
    Exercise(double hours, int count, ExerciseType exerciseType, FitnessUser user) {
        super(user);
        this.hours = hours;
        this.count = count;
        this.exerciseType = exerciseType;
    }

    void addHours(double hours) {
        this.hours += hours;
    }

    void addCount(int count) {
        this.count += count;
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

class PhysicalMonitor extends Feature implements ExerciseTracker {
    List<Exercise> exercises;  // holds all exercises done
    double caloriesBurnt;  // total calories burnt 
    double caloriesConsumed;  // total calries consumed

    // initialises attributes
    PhysicalMonitor(FitnessUser user) {
        super(user);
        this.exercises = new ArrayList<>();
        this.caloriesBurnt = 0;
        this.caloriesConsumed = 0;
    }

    public List<Exercise> getExercises() {
        return this.exercises;
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

}

class StressMonitor extends Feature implements SleepTracker {
    double hoursOfSleep;

    StressMonitor(double hoursOfSleep, FitnessUser user) {
        super(user);
        this.hoursOfSleep = hoursOfSleep;
    }

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
    List<Feature> features;  
    List<Exercise> exercises;
    double caloriesBurnt;
    double caloriesConsumed;
    double caloriesBurntFromExercisesByCount;
    double caloriesBurntFromExercisesByDuration;
    double hoursOfSleep;
    double improvementPercentage;
    double bmi;  // bmi of each user

    void populateFeatures() {
        this.features = new ArrayList<>();
        // Create and add PhysicalMonitor
        PhysicalMonitor physicalMonitor = new PhysicalMonitor(this.user);
        this.features.add(physicalMonitor);
        // Create and add StressMonitor
        StressMonitor stressMonitor = new StressMonitor(0, this.user);
        this.features.add(stressMonitor);
        // adds the FitnessHistory
        this.features.add(this);
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = new ArrayList<>(exercises);  // Create a new list to avoid reference issues
    }

    public void calculateCalories() {
        this.caloriesBurntFromExercisesByCount = 0;
        this.caloriesBurntFromExercisesByDuration = 0;
        if (this.exercises != null) {
            for (Exercise e : this.exercises) {
                if (this.user.getCaloriesCalculation() == CalculateExcerciseCalories.PER_EXCERCISE) {
                    this.caloriesBurntFromExercisesByCount += (e.getExerciseType().METvalue * this.user.getWeight()) / (60 * e.getExerciseType().amountOfExercisePerMinute) * e.getCount();
                } else {
                    this.caloriesBurntFromExercisesByDuration += e.getExerciseType().METvalue * this.user.getWeight() * e.getHours();
                }
            }
        }
    }

    // initialises each attribute using polymorphism
    DailyLog(int days, int months, int year, FitnessUser user) {
        // assigning the user from fittnessHistory class to the user from this class
        super(user);
        this.bmi = this.user.getWeight()/(this.user.getHeight()*this.user.getHeight());
        this.user = user;
        // usual initialisation of attributes
        this.days = days;
        this.months = months;
        this.year = year;
        this.date = this.days+"/"+this.months+"/"+this.year;
        // call the populateFeatures method to add all features into the arraylist
        populateFeatures();
        // polymorphic initialisation of attributes
        // iterating through feature objects
        for (Feature f : this.features) {
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
    public void addSleepHours(double hours) {
        this.hoursOfSleep += hours;
        for (Feature f : this.features) {
            if (f instanceof StressMonitor) {
                ((StressMonitor) f).trackSleepHours(hours);
                break;
            }
        }
    }    
    public void updateBMI(double weight, double height) {
        this.bmi = weight / (height * height);
    }
    void calculateImprovement() {
        DailyLog previousLog = this.user.getFitnessHistory().getPreviousDailyLog(this);
        if (previousLog != null) {
            double improvement = ((this.bmi - previousLog.bmi) / previousLog.bmi) * 100;
            
            if (this.user.getGoal().equals(Goal.LOSE_WEIGHT)) {
                this.improvementPercentage = -improvement; // negative improvement is good for weight loss
            } else if (this.user.getGoal().equals(Goal.GAIN_WEIGHT)) {
                this.improvementPercentage = improvement; // positive improvement is good for weight gain
            } else if (this.user.getGoal().equals(Goal.MAINTAIN_WEIGHT)) {
                this.improvementPercentage = Math.abs(improvement) * -1; // any change is considered negative for maintaining weight
            }
        } else {
            this.improvementPercentage = 0; // No previous log to compare with
        }
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
    void addCaloriesConsumed(double calories) {
        this.caloriesConsumed += calories;
    }

    void addCaloriesBurnt(double calories) {
        this.caloriesBurnt += calories;
    }

    // this method allows the user to view a specific day from the history array
    public void viewDailyLog() {
        System.out.println("--------------------------------");
        System.out.println("Date: " + this.date);
        System.out.println("Weight: " + this.user.getWeight());
        System.out.println("BMI: " + this.bmi);
        System.out.println("Improvement Percentage: " + this.improvementPercentage + "%");
        System.out.println("General Calories Burnt: " + this.caloriesBurnt);
        System.out.println("General Calories Consumed: " + this.caloriesConsumed);
        
        if (this.user.getCaloriesCalculation() == CalculateExcerciseCalories.PER_EXCERCISE) {
            System.out.println("Calories Burnt from Exercises: " + this.caloriesBurntFromExercisesByCount);
        } else if (this.user.getCaloriesCalculation() == CalculateExcerciseCalories.DURATION_OF_EXCERCISE) {
            System.out.println("Calories Burnt from Exercises: " + this.caloriesBurntFromExercisesByDuration);
        }
        
        System.out.println("Exercises: ");
        if (this.exercises != null && !this.exercises.isEmpty()) {
            for (Exercise e : this.exercises) {
                if (this.user.getCaloriesCalculation() == CalculateExcerciseCalories.PER_EXCERCISE) {
                    System.out.println("  • " + e.getExerciseType().name + ": \n     • Repetition: " + e.getCount());
                } else {
                    System.out.println("  • " + e.getExerciseType().name + ": \n     • Hours: " + e.getHours());
                }
            }
        } else {
            System.out.println("No exercises recorded for this day.");
        }
        
        System.out.println("Sleep Hours: " + this.hoursOfSleep);
        System.out.println("--------------------------------");
    }
}

class FitnessHistory extends Feature {
    // holds the history of the user
    protected List<DailyLog> history;

    FitnessHistory(FitnessUser user) {
        super(user);
        this.history = new ArrayList<>();
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
    public DailyLog getPreviousDailyLog(DailyLog currentLog) {
        int currentIndex = this.history.indexOf(currentLog);
        if (currentIndex > 0) {
            return this.history.get(currentIndex - 1);
        }
        return null;
    }
    public DailyLog getMostRecentDailyLog() {
        if (this.history.isEmpty()) {
            return null;
        }
        return this.history.get(this.history.size() - 1);
    }
    void calculateAllImprovements() {
        for (DailyLog log : this.history) {
            log.calculateImprovement();
        }
    }
    void addOrUpdateDailyLog(DailyLog dailyLog) {
        for (int i = 0; i < this.history.size(); i++) {
            if (this.history.get(i).getDate().equals(dailyLog.getDate())) {
                this.history.set(i, dailyLog);
                return;
            }
        }
        this.history.add(dailyLog);
    }

    public DailyLog getDailyLog(int day, int month, int year) {
        String date = day+"/"+month+"/"+year;
        for (DailyLog log : this.history) {
            if (log.getDate().equals(date)) {
                return log;
            }
        }
        return null;
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
        if (this.history.isEmpty()) {
            System.out.println("No fitness history available.");
            return;
        }

        // comparator declared to compare dates between dates
        final Comparator<DailyLog> comparatorForDates = Comparator.comparing(DailyLog::getYear).thenComparing(DailyLog::getMonths).thenComparing(DailyLog::getDays);

        // sorts the history list using the comparator declared above
        Collections.sort(this.history, comparatorForDates);

        int index = 1;
        // iterating through dailylog objects
        for (DailyLog d : this.history) {
            System.out.println(index + ".");
            System.out.println("--------------------------------");
            System.out.println("Date: " + d.date);
            System.out.println("BMI: " + d.bmi);
            System.out.println("Improvement Percentage: " + d.improvementPercentage + "%");
            System.out.println("--------------------------------");
            index++;
        }
    }   
}
