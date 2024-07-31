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
    void addExercise(Exercise exericse);  // add exercises to exercise arraylist
}

// interface for sleep methods
interface SleepTracker {
    void trackSleepHours(double hours);  // tracks amount of hours of sleep
}

class Feature {
    protected int id;  // user attribute to assign each feature for specific user
    static int nextID = 0;
    Feature() {
        this.id = nextID;
        this.nextID+=1;
    }

    // accessor method for user
    public int getUser() {
        return this.id;
    }
    public int getNextId(){
        return this.nextID;
    }
}

class Exercise {
    protected double hours;  // hours of each exercise done
    protected int count;  // count of each exericse done
    protected ExerciseType exerciseType;  // the exercise type

    // intialise attributes
    Exercise(double hours, int count, ExerciseType exerciseType) {
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
    PhysicalMonitor() {
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
    public void setCaloriesBurnt(double cal){
        this.caloriesBurnt = cal;
    }
    public void setCaloriesConsumed(double cal){
        this.caloriesConsumed = cal;
    }

}

class StressMonitor extends Feature implements SleepTracker {
    double hoursOfSleep;

    StressMonitor(double hoursOfSleep) {
        super();
        this.hoursOfSleep = hoursOfSleep;
    }

    // overrides the methods from SleepTracker
    @Override
    // tracks sleep hours
    public void trackSleepHours(double hours) {
        hoursOfSleep += hours;
    }
}


