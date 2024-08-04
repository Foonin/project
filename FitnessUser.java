import java.util.*;

public class FitnessUser extends User implements UserAuthentication {
    private double weight;
    private double height;
    private Goal goal;
    private String password;
    private CalculateExcerciseCalories caloriesCalculation;
    private HashMap<Character, Character> encryptionMap = new HashMap<>();
    private FitnessHistory history = new FitnessHistory();
    private double calorieBalance = 0;
    private double bmi;
    ArrayList<Double> bmiHistory = new ArrayList<>();

    FitnessUser(String username, String password, String email, double weight, double height, Goal goal,
            CalculateExcerciseCalories caloriesCalculation) {
        super(username, password, email);
        this.height = height;
        this.weight = weight;
        this.caloriesCalculation = caloriesCalculation;
        this.goal = goal;
        this.password = password;
        this.bmi = this.getWeight() / (this.getHeight() * this.getHeight());
        this.bmiHistory.add(bmi);
        this.encryptionMap.put('a', 'm');
        this.encryptionMap.put('b', 'n');
        this.encryptionMap.put('c', 'o');
        this.encryptionMap.put('d', 'p');
        this.encryptionMap.put('e', 'q');
        this.encryptionMap.put('f', 'r');
        this.encryptionMap.put('g', 's');
        this.encryptionMap.put('h', 't');
        this.encryptionMap.put('i', 'u');
        this.encryptionMap.put('j', 'v');
        this.encryptionMap.put('k', 'w');
        this.encryptionMap.put('l', 'x');
        this.encryptionMap.put('m', 'y');
        this.encryptionMap.put('n', 'z');
        this.encryptionMap.put('o', 'a');
        this.encryptionMap.put('p', 'b');
        this.encryptionMap.put('q', 'c');
        this.encryptionMap.put('r', 'd');
        this.encryptionMap.put('s', 'e');
        this.encryptionMap.put('t', 'f');
        this.encryptionMap.put('u', 'g');
        this.encryptionMap.put('v', 'h');
        this.encryptionMap.put('w', 'i');
        this.encryptionMap.put('x', 'j');
        this.encryptionMap.put('y', 'k');
        this.encryptionMap.put('z', 'l');

        // Numeric characters
        this.encryptionMap.put('0', '5');
        this.encryptionMap.put('1', '6');
        this.encryptionMap.put('2', '7');
        this.encryptionMap.put('3', '8');
        this.encryptionMap.put('4', '9');
        this.encryptionMap.put('5', '0');
        this.encryptionMap.put('6', '1');
        this.encryptionMap.put('7', '2');
        this.encryptionMap.put('8', '3');
        this.encryptionMap.put('9', '4');

        // Special characters
        this.encryptionMap.put('!', '@');
        this.encryptionMap.put('@', '#');
        this.encryptionMap.put('#', '$');
        this.encryptionMap.put('$', '%');
        this.encryptionMap.put('%', '^');
        this.encryptionMap.put('^', '&');
        this.encryptionMap.put('&', '*');
        this.encryptionMap.put('*', '(');
        this.encryptionMap.put('(', ')');
        this.encryptionMap.put(')', '!');

    }

    public double getHeight() {
        return this.height;
    }

    public double getWeight() {
        return this.weight;
    }

    public Goal getGoal() {
        return this.goal;
    }

    public void updateBMI(double weight, double height) {

        this.bmi = weight / (height * height);
        this.bmiHistory.add(bmi);
    }

    public String encrypt(String password) {
        String encryptString = "";
        for (int i = 0; i < password.length(); i++) {
            char currChar = password.charAt(i);
            encryptString += this.encryptionMap.get(currChar);
        }
        return encryptString;
    }

    private boolean decrypt(String password) {
        boolean matchPass = encrypt(password).equals(this.password);
        return matchPass;
    }

    @Override
    public void initializeAccount() {
        this.password = encrypt(this.password);
    }

    @Override
    public boolean authorized(String password) {
        return decrypt(password);
    }

    public String getName() {
        return this.username;
    }

    public CalculateExcerciseCalories getCaloriesCalculation() {
        return this.caloriesCalculation;
    }

    public FitnessHistory getFitnessHistory() {
        return this.history;
    }

    public void viewHistory() {
        this.history.viewFitnessHistory(this.caloriesCalculation);
    }

    public void burnCalories(double calories) {
        this.calorieBalance -= calories;
    }

    public ArrayList<PhysicalMonitor> getPhysicalList(DailyLog daily) {
        ArrayList<PhysicalMonitor> m = new ArrayList<>();
        for (Feature f : daily.getFeatures()) {
            if (f instanceof PhysicalMonitor) {
                m.add((PhysicalMonitor) f);
            }
        }
        return m;
    }

    public void calculateCalories(DailyLog d) {
        d.setCaloriesBurntFromExercisesByCount(0);
        d.setCaloriesBurntFromExercisesByDuration(0);
        if (d.getExericses() != null) {
            for (Exercise e : d.getExericses()) {
                if (getCaloriesCalculation() == CalculateExcerciseCalories.PER_EXCERCISE) {
                    double calPerExercise = d.getCaloriesBurntFromExercisesByCount();
                    d.setCaloriesBurntFromExercisesByCount(
                            calPerExercise += (e.getExerciseType().METvalue * this.getWeight())
                                    / (60 * e.getExerciseType().amountOfExercisePerMinute) * e.getCount());
                    System.out.println(d.getCaloriesBurntFromExercisesByCount());
                } else {
                    double calPerDuration = d.getCaloriesBurntFromExercisesByCount();
                    d.setCaloriesBurntFromExercisesByCount(
                            calPerDuration += e.getExerciseType().METvalue * this.getWeight()
                                    * e.getHours());
                    System.out.println(d.getCaloriesBurntFromExercisesByDuration());
                }
            }
        }
        for (PhysicalMonitor f : getPhysicalList(d)) {
            ((PhysicalMonitor) f).setCaloriesBurnt(d.getCaloriesBurntFromExercisesByCount()
                    + d.getCaloriesBurntFromExercisesByDuration());
            ((PhysicalMonitor) f).setCaloriesConsumed(2000);
        }
    }

    void calculateImprovement(DailyLog d, Goal g) {
        adjustWeight(this.history.getCurrentIndex());
        this.history.setCurrentIndex(this.history.getCurrentIndex() + 1);
        d.setDailyBMI(this.bmi);
        int size = this.history.getHistory().size() - 1;
        if (size > 0) {
            int previous = this.history.getHistory().indexOf(d) - 1;
            double currentBMI = bmiHistory.get(previous + 1);
            double previousBMI = bmiHistory.get(previous);
            double improvement = ((previousBMI - currentBMI) * 100 / previousBMI);

            if (g.equals(Goal.LOSE_WEIGHT)) {
                d.setImprovementPercentage(-improvement);// negative improvement is good for weight loss
            } else if (g.equals(Goal.GAIN_WEIGHT)) {
                d.setImprovementPercentage(improvement); // positive improvement is good for weight gain
            } else if (g.equals(Goal.MAINTAIN_WEIGHT)) {
                d.setImprovementPercentage(Math.abs(improvement) * -1); // any change is considered negative for
                                                                        // maintaining weight
            }
        } else {
            d.setImprovementPercentage(0); // No previous log to compare with
        }
    }

    public void setCalories(int targetInt) {
        PhysicalMonitor target = ((PhysicalMonitor) this.history.getHistory().get(targetInt).getFeatures().get(0));
        this.calorieBalance = target.getCaloriesConsumed() - target.getCaloriesConsumed();
    }

    public void adjustWeight(int target) {
        setCalories(target);
        double weightChangeKg = (calorieBalance / 3500) * 0.45;
        this.weight += weightChangeKg;
        this.calorieBalance %= 3500; // Keep the remainder for future calculations

        // Update BMI in the most recent DailyLog
        this.updateBMI(this.weight, this.height);
    }
}

interface UserAuthentication {
    void initializeAccount();

    boolean authorized(String password);
}

abstract class User {
    String username;
    String password;
    String email;

    User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}

enum Goal {
    LOSE_WEIGHT,
    GAIN_WEIGHT,
    MAINTAIN_WEIGHT
}

enum CalculateExcerciseCalories {
    PER_EXCERCISE,
    DURATION_OF_EXCERCISE
}

class DailyLog {
    private int days;
    private int months;
    private int year;

    private String date = this.days + "/" + this.months + "/" + this.year;
    private List<Feature> features;
    private List<Exercise> exercises;
    private double caloriesBurntFromExercisesByCount;
    private double caloriesBurntFromExercisesByDuration;
    private double hoursOfSleep;
    private double improvementPercentage;
    private double dailyBMI = 0;

    public void setExercises(List<Exercise> exercises) {
        this.exercises = new ArrayList<>(exercises); // Create a new list to avoid reference issues
    }

    // initialises each attribute using polymorphism
    DailyLog(int days, int months, int year) {
        // usual initialisation of attributes
        this.days = days;
        this.months = months;
        this.year = year;
        this.date = this.days + "/" + this.months + "/" + this.year;
        this.features = new ArrayList<>();
        // call the populateFeatures method to add all features into the arraylist
        // polymorphic initialisation of attributes
        // iterating through feature objects
        for (Feature f : this.features) {
            // conditional statement to check if "f" is of type "PhysicalMonitor"
            if (f instanceof PhysicalMonitor) {
                PhysicalMonitor p = (PhysicalMonitor) f; // casting f onto "PhysicalMonitor" to convert it
                // initialising each attribute
                this.exercises = p.getExercises();
                this.caloriesBurntFromExercisesByCount = 0;// p.trackExerciseCaloriesBurntByCount();
                this.caloriesBurntFromExercisesByDuration = 0;// p.trackExerciseCaloriesBurntByDuration();
                // checking if "f" is of type "StressMonitor"
            } else if (f instanceof StressMonitor) {
                StressMonitor s = (StressMonitor) f; // casting "f" onto "StressMonitor" to convert it
                // initialising each attribute
                this.hoursOfSleep = s.hoursOfSleep;
            }
        }
    }

    public void setImprovementPercentage(double improve) {
        this.improvementPercentage = improve;
    }

    public double getImprovementPercentage() {
        return this.improvementPercentage;
    }

    public void setDailyBMI(double bmi) {
        this.dailyBMI = bmi;
    }

    public void addExercise(Exercise e) {
        // check pm exist
        for (Feature f : this.features) {
            if (f instanceof PhysicalMonitor) {
                ((PhysicalMonitor) f).addExercise(e);
                return;
            }
        }
        PhysicalMonitor pm = new PhysicalMonitor();
        pm.addExercise(e);
        this.features.add(pm);
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

    public void addCalories(double cal) {
        this.caloriesBurntFromExercisesByCount += cal;
    }

    public void burmCalories(double cal) {
        this.caloriesBurntFromExercisesByCount -= cal;
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
    List<Exercise> getExericses() {
        return this.exercises;
    }

    List<Feature> getFeatures() {
        return this.features;
    }

    // accessor method
    void setCaloriesBurntFromExercisesByCount(double cal) {
        this.caloriesBurntFromExercisesByCount = cal;
    }

    void setCaloriesBurntFromExercisesByDuration(double cal) {
        this.caloriesBurntFromExercisesByDuration = cal;
    }

    double getCaloriesBurntFromExercisesByCount() {
        return this.caloriesBurntFromExercisesByCount;
    }

    // accessor method
    double getCaloriesBurntFromExercisesByDuration() {
        return this.caloriesBurntFromExercisesByDuration;
    }

    // creates a sub-collection of history arraylist

    // this method allows the user to view a specific day from the history array
    public void viewDailyLog(CalculateExcerciseCalories c) {
        System.out.println("--------------------------------");
        System.out.println("Date: " + this.date);
        System.out.println("BMI: " + this.dailyBMI);
        System.out.println("Improvement Percentage: " + this.improvementPercentage + "%");
        // System.out.println("General Calories Burnt: " + this.caloriesBurnt);
        // System.out.println("General Calories Consumed: " + this.caloriesConsumed);

        if (c == CalculateExcerciseCalories.PER_EXCERCISE) {
            System.out.println("Calories Burnt from Exercises: " + this.caloriesBurntFromExercisesByCount);
        } else if (c == CalculateExcerciseCalories.DURATION_OF_EXCERCISE) {
            System.out.println("Calories Burnt from Exercises: " + this.caloriesBurntFromExercisesByDuration);
        }

        System.out.println("Exercises: ");
        if (this.exercises != null && !this.exercises.isEmpty()) {
            for (Exercise e : this.exercises) {
                if (c == CalculateExcerciseCalories.PER_EXCERCISE) {
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

class FitnessHistory {
    // holds the history of the user
    private List<DailyLog> history;
    private int currentIndex = 0;

    FitnessHistory() {
        this.history = new ArrayList<>();
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public void setCurrentIndex(int current) {
        this.currentIndex = current;
    }

    List<DailyLog> getHistory() {
        return this.history;
    }

    // method to add dailylogs to history
    void addDailyLog(DailyLog dailyLog) {
        boolean exists = false;
        // iteratiing through daily log objects
        for (DailyLog d : this.history) {
            // checks if date equals to the date of the parameter
            if (d.getDate().equals(dailyLog.getDate())) {
                System.out.println("Daily-Log already admitted");
                exists = true;
                break;
            }
        }
        // checks if the object doesn't exist
        if (!exists) {
            this.history.add(dailyLog); // adds that parameter dailylog object
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
        String date = day + "/" + month + "/" + year;
        for (DailyLog log : this.history) {
            if (log.getDate().equals(date)) {
                return log;
            }
        }
        return null;
    }

    // method to allow the user to view all of the history but with less details
    void viewFitnessHistory(CalculateExcerciseCalories c) {
        if (this.history.isEmpty()) {
            System.out.println("No fitness history available.");
            return;
        }

        // comparator declared to compare dates between dates
        final Comparator<DailyLog> comparatorForDates = Comparator.comparing(DailyLog::getYear)
                .thenComparing(DailyLog::getMonths).thenComparing(DailyLog::getDays);

        // sorts the history list using the comparator declared above
        Collections.sort(this.history, comparatorForDates);

        int index = 1;
        // iterating through dailylog objects
        for (DailyLog d : this.history) {
            System.out.println(index + ".");
            System.out.println("--------------------------------");
            System.out.println("Date: " + d.getDate());
            System.out.println("Improvement Percentage: " + d.getImprovementPercentage() + "%");
            if (d.getExericses() != null && !d.getExericses().isEmpty()) {
                for (Exercise e : d.getExericses()) {
                    if (c == CalculateExcerciseCalories.PER_EXCERCISE) {
                        System.out
                                .println("  • " + e.getExerciseType().name + ": \n     • Repetition: " + e.getCount());
                    } else {
                        System.out.println("  • " + e.getExerciseType().name + ": \n     • Hours: " + e.getHours());
                    }
                }
            } else {
                System.out.println("No exercises recorded for this day.");
            }
            System.out.println("--------------------------------");
            index++;
        }
    }
}