import java.util.*;

public class FitnessTracker {
            public static void main(String[] args) {
            Systems system = new Systems();
            boolean running = true;
    
            while (running) {
                System.out.println("\n--- Fitness Tracker Menu ---");
                System.out.println("1. Create Account");
                System.out.println("2. Log In");
                System.out.println("3. Log Out");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                
                try {
                    int choice = In.nextInt();
    
                    if (choice == 1) {
                        // Create Account
                        System.out.print("Enter username: ");
                        String username = In.nextLine();
                        System.out.print("Enter password: ");
                        String password = In.nextLine();
                        System.out.print("Enter email: ");
                        String email = In.nextLine();
                        System.out.print("Enter weight (kg): ");
                        double weight = In.nextDouble();
                        System.out.print("Enter height (m): ");
                        double height = In.nextDouble();
                        
                        System.out.println("Choose your goal:");
                        System.out.println("1. Lose Weight");
                        System.out.println("2. Gain Weight");
                        System.out.println("3. Maintain Weight");
                        int goalChoice = In.nextInt();
                        Goal goal;
                        if (goalChoice == 1) {
                            goal = Goal.LOSE_WEIGHT;
                        } else if (goalChoice == 2) {
                            goal = Goal.GAIN_WEIGHT;
                        } else {
                            goal = Goal.MAINTAIN_WEIGHT;
                        }
    
                        System.out.println("Choose calorie calculation method:");
                        System.out.println("1. Per Exercise");
                        System.out.println("2. Duration of Exercise");
                        int calcChoice = In.nextInt();
                        CalculateExcerciseCalories caloriesCalculation;
                        if (calcChoice == 1) {
                            caloriesCalculation = CalculateExcerciseCalories.PER_EXCERCISE;
                        } else {
                            caloriesCalculation = CalculateExcerciseCalories.DURATION_OF_EXCERCISE;
                        }
    
                        system.addAccount(username, password, email, weight, height, goal, caloriesCalculation);
                    } else if (choice == 2) {
                        // Log In
                        System.out.println("Login method:");
                        System.out.println("1. Login with Username");
                        System.out.println("2. Login with ID");
                        int loginMethod = In.nextInt();
    
    
                        if (loginMethod == 1) {
                            System.out.print("Enter username: ");
                            String username = In.nextLine();
                            System.out.print("Enter password: ");
                            String password = In.nextLine();
                            system.logIn(username, password);
                        } else if (loginMethod == 2) {
                            System.out.print("Enter ID: ");
                            int id = In.nextInt();
                            System.out.print("Enter password: ");
                            String password = In.nextLine();
                            system.logIn(id, password);
                        } else {
                            System.out.println("Invalid login method chosen.");
                        }
                    } else if (choice == 3) {
                        // Log Out
                        if (system.currentUser == null) {
                            System.out.println("No user is currently logged in.");
                        } else {
                            system.logOut(system.currentUser.getName());
                        }
                    } else if (choice == 4) {
                        System.out.println("Thank you for using the Fitness Tracker. Goodbye!");
                        return;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
    
                    // Exercise menu for logged-in users
                    if (system.currentUser != null) {
                        boolean loggedIn = true;
                        PhysicalMonitor physicalMonitor = new PhysicalMonitor(system.currentUser);
                        while (loggedIn) {
                            System.out.println("\n--- Exercise Menu ---");
                            System.out.println("1. Add Exercises");
                            System.out.println("2. Modify Existing Exercise");
                            System.out.println("3. Add General Calories");
                            System.out.println("4. Log sleep hours");
                            System.out.println("5. View Daily Log");
                            System.out.println("6. View Fitness History");
                            System.out.println("7. Log Out");
                            System.out.print("Enter your choice: ");
        
                            int exerciseChoice = In.nextInt();
        
                            if (exerciseChoice == 1) {
                                // Add Exercises
                                System.out.println("Enter the date for these exercises (DD MM YYYY):");
                                System.out.print("Day: ");
                                int day = In.nextInt();
                                System.out.print("Month: ");
                                int month = In.nextInt();
                                System.out.print("Year: ");
                                int year = In.nextInt();
    
                                boolean addingExercises = true;
                                while (addingExercises) {
                                    System.out.println("Choose an exercise:");
                                    ExerciseType[] exerciseTypes = ExerciseType.values();
                                    for (int i = 0; i < exerciseTypes.length; i++) {
                                        System.out.println((i+1)+". "+exerciseTypes[i].name());
                                    }
                                    int exChoice = In.nextInt();
        
                                    if (exChoice > 0 && exChoice <= exerciseTypes.length) {
                                        ExerciseType chosenExercise = exerciseTypes[exChoice-1];
        
                                        if (system.currentUser.getCaloriesCalculation() == CalculateExcerciseCalories.PER_EXCERCISE) {
                                            System.out.print("Enter number of repetitions: ");
                                            int count = In.nextInt();
                                            Exercise exercise = new Exercise(0, count, chosenExercise, system.currentUser);
                                            physicalMonitor.addExercise(exercise);
                                        } else {
                                            System.out.print("Enter duration in hours: ");
                                            double hours = In.nextDouble();
                                            Exercise exercise = new Exercise(hours, 0, chosenExercise, system.currentUser);
                                            physicalMonitor.addExercise(exercise);
                                        }
        
                                        System.out.println("Exercise added successfully.");
                                    } else {
                                        System.out.println("Invalid exercise choice.");
                                    }
        
                                    System.out.println("Do you want to add another exercise? (1 for Yes, 0 for No)");
                                    int continueChoice = In.nextInt();
                                    addingExercises = (continueChoice == 1);
                                }
                                DailyLog dailyLog = system.currentUser.getFitnessHistory().getDailyLog(day, month, year);
                                if (dailyLog == null) {
                                    dailyLog = new DailyLog(day, month, year, system.currentUser);
                                }
                                dailyLog.setExercises(physicalMonitor.getExercises());
                                dailyLog.calculateCalories();
                                if (system.currentUser.getCaloriesCalculation() == CalculateExcerciseCalories.PER_EXCERCISE) {
                                    system.currentUser.burnCalories(dailyLog.getCaloriesBurntFromExercisesByCount());
                                } else {
                                    system.currentUser.burnCalories(dailyLog.getCaloriesBurntFromExercisesByDuration());
                                }
                                system.currentUser.getFitnessHistory().addOrUpdateDailyLog(dailyLog);
                                System.out.println("Exercises added to the daily log.");
                                DailyLog updatedLog = system.currentUser.getFitnessHistory().getDailyLog(day, month, year);
                                dailyLog.updateBMI(system.currentUser.getWeight(), system.currentUser.getHeight());
                                updatedLog.calculateImprovement();
                                system.currentUser.getFitnessHistory().addOrUpdateDailyLog(updatedLog);
                            } else if (exerciseChoice == 2){
                                System.out.println("Enter the date of the exercise to modify (DD MM YYYY):");
                                System.out.print("Day: ");
                                int day = In.nextInt();
                                System.out.print("Month: ");
                                int month = In.nextInt();
                                System.out.print("Year: ");
                                int year = In.nextInt();
    
                                DailyLog dailyLog = system.currentUser.getFitnessHistory().getDailyLog(day, month, year);
                                if (dailyLog != null) {
                                    List<Exercise> exercises = dailyLog.getExericses();
                                    if (exercises != null && !exercises.isEmpty()) {
                                        System.out.println("Choose an exercise to modify:");
                                        for (int i = 0; i < exercises.size(); i++) {
                                            System.out.println((i+1) + ". " + exercises.get(i).getExerciseType().name);
                                        }
                                        int exChoice = In.nextInt() - 1;
                                        if (exChoice >= 0 && exChoice < exercises.size()) {
                                            Exercise exercise = exercises.get(exChoice);
                                            if (system.currentUser.getCaloriesCalculation() == CalculateExcerciseCalories.PER_EXCERCISE) {
                                                System.out.print("Enter additional repetitions: ");
                                                int addCount = In.nextInt();
                                                exercise.addCount(addCount);
                                            } else {
                                                System.out.print("Enter additional duration in hours: ");
                                                double addHours = In.nextDouble();
                                                exercise.addHours(addHours);
                                            }
                                            dailyLog.calculateCalories();
                                            system.currentUser.getFitnessHistory().addOrUpdateDailyLog(dailyLog);
                                            System.out.println("Exercise modified successfully.");
                                        } else {
                                            System.out.println("Invalid exercise choice.");
                                        }
                                    } else {
                                        System.out.println("No exercises found for this date.");
                                    }
                                    if (system.currentUser.getCaloriesCalculation() == CalculateExcerciseCalories.PER_EXCERCISE) {
                                        system.currentUser.burnCalories(dailyLog.getCaloriesBurntFromExercisesByCount());
                                    } else {
                                        system.currentUser.burnCalories(dailyLog.getCaloriesBurntFromExercisesByDuration());
                                    }
                                    dailyLog.updateBMI(system.currentUser.getWeight(), system.currentUser.getHeight());
                                    dailyLog.calculateImprovement();
                                    system.currentUser.getFitnessHistory().addOrUpdateDailyLog(dailyLog);
                                } else {
                                    System.out.println("No daily log found for the specified date.");
                                }
    
                            } else if (exerciseChoice == 3) {
                                System.out.println("Enter the date to add calories (DD MM YYYY):");
                                System.out.print("Day: ");
                                int day = In.nextInt();
                                System.out.print("Month: ");
                                int month = In.nextInt();
                                System.out.print("Year: ");
                                int year = In.nextInt();
    
                                DailyLog dailyLog = system.currentUser.getFitnessHistory().getDailyLog(day, month, year);
                                if (dailyLog == null) {
                                    dailyLog = new DailyLog(day, month, year, system.currentUser);
                                }
    
                                System.out.println("1. Add Calories Consumed");
                                System.out.println("2. Add Calories Burnt");
                                System.out.print("Enter your choice: ");
                                int calorieChoice = In.nextInt();
    
                                if (calorieChoice == 1) {
                                    System.out.print("Enter calories consumed: ");
                                    double calories = In.nextDouble();
                                    dailyLog.addCaloriesConsumed(calories);
                                    system.currentUser.addCalories(calories);
                                    System.out.println("Calories consumed added successfully.");
                                } else if (calorieChoice == 2) {
                                    System.out.print("Enter calories burnt: ");
                                    double calories = In.nextDouble();
                                    dailyLog.addCaloriesBurnt(calories);
                                    system.currentUser.burnCalories(calories);
                                    System.out.println("Calories burnt added successfully.");
                                } else {
                                    System.out.println("Invalid choice.");
                                }
                                dailyLog.updateBMI(system.currentUser.getWeight(), system.currentUser.getHeight());
                                dailyLog.calculateImprovement();
                                system.currentUser.getFitnessHistory().addOrUpdateDailyLog(dailyLog);
    
                            } else if (exerciseChoice == 4) {
                                System.out.println("Enter the date to log sleep hours (DD MM YYYY):");
                                System.out.print("Day: ");
                                int day = In.nextInt();
                                System.out.print("Month: ");
                                int month = In.nextInt();
                                System.out.print("Year: ");
                                int year = In.nextInt();
    
                                DailyLog dailyLog = system.currentUser.getFitnessHistory().getDailyLog(day, month, year);
                                if (dailyLog == null) {
                                    dailyLog = new DailyLog(day, month, year, system.currentUser);
                                }
    
                                System.out.print("Enter hours of sleep: ");
                                double sleepHours = In.nextDouble();
                                dailyLog.addSleepHours(sleepHours);
                                System.out.println("Sleep hours added successfully.");
    
                            system.currentUser.getFitnessHistory().addOrUpdateDailyLog(dailyLog);
                            } else if (exerciseChoice == 5) {
                                // View Daily Log
                                System.out.println("Enter the date to view: ");
                                System.out.print("Day: ");
                                int day = In.nextInt();
                                System.out.print("Month: ");
                                int month = In.nextInt();
                                System.out.print("Year: ");
                                int year = In.nextInt();
    
                                DailyLog dailyLog = system.currentUser.getFitnessHistory().getDailyLog(day, month, year);
                                if (dailyLog != null) {
                                    dailyLog.calculateImprovement();
                                    system.currentUser.getFitnessHistory().addOrUpdateDailyLog(dailyLog);
                                    dailyLog.viewDailyLog();
                                } else {
                                    System.out.println("No daily log found for the specified date.");
                                }
                            } else if (exerciseChoice == 6) {
                                system.currentUser.getFitnessHistory().calculateAllImprovements();
                                system.currentUser.getFitnessHistory().viewFitnessHistory();
                            } else if (exerciseChoice == 7) {
                                system.logOut(system.currentUser.getName());
                                loggedIn = false;
                                System.out.println("Logged out successfully.");
                            } else {
                                System.out.println("Invalid choice. Please try again.");
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("An error occurred: "+e.getMessage());
                    In.nextLine();
                }
            }
        }
    }

interface verifyAccount{
    void logIn(String username, String password);
    void logOut(String username);
    void addAccount(String username, String password, String email, double weight, double height, Goal goal, CalculateExcerciseCalories caloriesCalculation);
}

class Systems implements verifyAccount{
    private int availableId;
    private Map<Integer, FitnessUser> accounts; // 
    private List<FitnessUser> signedInAccount; //Optimize better than Arraylist
    public FitnessUser currentUser;
    Systems(){
        this.accounts = new HashMap<>();
        this.signedInAccount= new LinkedList<>();
        this.availableId = 0;
    }
    public void addAccount(String username, String password, String email, double weight, double height, Goal goal, CalculateExcerciseCalories caloriesCalculation){
        Random ran = new Random();
        int increment = ran.nextInt(10)+1;
        this.availableId += increment;
        this.accounts.put(availableId, new FitnessUser(username, password, email, weight, height, goal, caloriesCalculation));
        this.accounts.get(availableId).initializeAccount();
        System.out.println("Account created successfully. ID: "+ this.availableId + " . Username: "+ username +". Email: "+ email);
    }
    public void logIn(String username, String password){
        for (Map.Entry<Integer,FitnessUser> entry : this.accounts.entrySet()){
            if (entry.getValue().getName().equals(username)){
                boolean sucess = entry.getValue().authorized(password);
                if (sucess){
                    if (this.signedInAccount.contains(entry.getValue())){
                        System.out.println("Log In unsuccessfully, account logged in");
                        return; // Check account is logged in -> exit
                    }
                    this.signedInAccount.add(entry.getValue());
                    System.out.println("Log In successfully");
                    this.currentUser = entry.getValue();
                    return;
                }
                else{
                    System.out.println("Log In unsuccessfully");
                    return;
                }
            }
        }
    }
    public void logIn(int id, String password){
        try { // Try to see if the id existed
            FitnessUser logInUser = this.accounts.get(id);
            boolean sucess = logInUser.authorized(password);
            if (sucess){
                if (this.signedInAccount.contains(logInUser)){
                    System.out.println("Log In successfully, Account already logged in");
                    return;// Check account is logged in -> exit
                }
                this.signedInAccount.add(logInUser);
                System.out.println("Log In successfully");
                this.currentUser = logInUser;
            return;
            }
            else{ // Incorrect Password
                System.out.println("Log In unsuccessfully" + logInUser.getName());
        }
        } catch (Exception e) { // Incorrect ID
            System.out.println("Log In unsuccessfully");
        }
        
    }
        
    public void logOut(String username){
        for (FitnessUser user : this.signedInAccount){
            if (user.getName().equals(username)){
                this.signedInAccount.remove(user);
                this.currentUser = null;
                System.out.println("Log Out successfully");
            }
            else{
                System.out.println("Log Out unsuccessfully");
            }
        }
    }
}
class FitnessUser extends User implements UserAuthentication {
    private double weight;
    private double height;
    private Goal goal;
    private String password;
    private CalculateExcerciseCalories caloriesCalculation;
    private HashMap <Character,Character> encryptionMap = new HashMap<>();
    FitnessHistory fitnessHistory;
    double calorieBalance;
    FitnessUser(String username, String password, String email, double weight, double height, Goal goal, CalculateExcerciseCalories caloriesCalculation){
        super(username, password, email);
        this.fitnessHistory = new FitnessHistory(this);
        this.height = height;
        this.weight = weight;
        this.caloriesCalculation = caloriesCalculation;
        this.goal = goal;
        this.password = password;
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
    public void adjustWeight() {
        double weightChangeKg = (calorieBalance / 3500) * 0.45;
        this.weight += weightChangeKg;
        this.calorieBalance %= 3500;  // Keep the remainder for future calculations
        
        // Update BMI in the most recent DailyLog
        DailyLog mostRecentLog = this.fitnessHistory.getMostRecentDailyLog();
        if (mostRecentLog != null) {
            mostRecentLog.updateBMI(this.weight, this.height);
            mostRecentLog.calculateImprovement();
        }
    }

    public void addCalories(double calories) {
        this.calorieBalance += calories;
        adjustWeight();
    }

    public void burnCalories(double calories) {
        this.calorieBalance -= calories;
        adjustWeight();
    }

    public FitnessHistory getFitnessHistory() {
        return this.fitnessHistory;
    }

    public double getHeight(){
        return this.height;
    }
    public double getWeight(){
        return this.weight;
    }
    public Goal getGoal(){
        return this.goal;
    }
    public CalculateExcerciseCalories getCaloriesCalculation(){
        return this.caloriesCalculation;
    }
    public String encrypt(String password){
        String encryptString = "";
        for (int i = 0; i < password.length(); i++) {
            char currChar = password.charAt(i);
            encryptString += this.encryptionMap.get(currChar);
        }
        return encryptString;
    }
    private boolean decrypt(String password){
        boolean matchPass = encrypt(password).equals(this.password);
        return matchPass;
    }
    @Override
    public void initializeAccount(){
        this.password = encrypt(this.password);
    }
    @Override
    public boolean authorized(String password){
        return decrypt(password);
    }
    public String printPass(){
        return this.password;
    }
    public String getName(){
        return this.username;
    }
}
   

interface  UserAuthentication{
    void initializeAccount();
    boolean authorized(String password);
}  
abstract class User {
    String username;
    String password;
    String email;
    User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
enum Goal{
    LOSE_WEIGHT,
    GAIN_WEIGHT,
    MAINTAIN_WEIGHT
}

enum CalculateExcerciseCalories{
    PER_EXCERCISE,
    DURATION_OF_EXCERCISE
}

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