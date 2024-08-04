import java.util.ArrayList;
import java.util.List;

class MainMenu {
    Systems system = new Systems();
    boolean running = true;
    Goal g = Goal.GAIN_WEIGHT;
    CalculateExcerciseCalories c = CalculateExcerciseCalories.DURATION_OF_EXCERCISE;

    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
        menu.runningMenu();
    }

    void runningMenu() {
        system.addAccount("1", "1", "1", 100, 1.5, g, c);
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
                    creatAccount();
                } else if (choice == 2) {
                    // Log In
                    logInMenu();

                } else if (choice == 3) {
                    // Log Out
                    logOutMenu();
                } else if (choice == 4) {
                    exit();

                } else {
                    System.out.println("Invalid choice. Please try again.");
                }

                // Exercise menu for logged-in users
                if (system.currentUser != null) {
                    boolean loggedIn = true;

                    while (loggedIn) {
                        System.out.println(system.currentUser);
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
                            addExerciseMenu();

                        } else if (exerciseChoice == 2) {
                            modifyExerciseMenu();

                        } else if (exerciseChoice == 3) {
                            addCaloriesMenu();

                        } else if (exerciseChoice == 4) {
                            logSleepHourMenu();

                        } else if (exerciseChoice == 5) {
                            getDailyLogMenu();
                            // View Daily Log

                        } else if (exerciseChoice == 6) {
                            viewHistoryMenu();

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
                System.out.println("An error occurred: " + e.getMessage());
                In.nextLine();
            }
        }
        System.out.println(system);
    }

    private void creatAccount() {
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
    }

    private void logInMenu() {
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
    }

    private void logOutMenu() {
        if (system.currentUser == null) {
            System.out.println("No user is currently logged in.");
        } else {
            system.logOut(system.currentUser.getName());
        }
    }

    private void exit() {
        running = false;
        System.out.println("Thank you for using the Fitness Tracker. Goodbye!");
    }

    private void addExerciseMenu() {
        System.out.println("Enter the date for these exercises (DD MM YYYY):");
        System.out.print("Day: ");
        int day = In.nextInt();
        System.out.print("Month: ");
        int month = In.nextInt();
        System.out.print("Year: ");
        int year = In.nextInt();
        ArrayList<PhysicalMonitor> physical = new ArrayList<>();
        boolean addingExercises = true;
        while (addingExercises) {
            PhysicalMonitor physicalMonitor = new PhysicalMonitor();
            System.out.println("Choose an exercise:");
            ExerciseType[] exerciseTypes = ExerciseType.values();
            for (int i = 0; i < exerciseTypes.length; i++) {
                System.out.println((i + 1) + ". " + exerciseTypes[i].name());
            }
            int exChoice = In.nextInt();

            if (exChoice > 0 && exChoice <= exerciseTypes.length) {
                ExerciseType chosenExercise = exerciseTypes[exChoice - 1];

                if (system.currentUser.getCaloriesCalculation() == CalculateExcerciseCalories.PER_EXCERCISE) {
                    System.out.print("Enter number of repetitions: ");
                    int count = In.nextInt();
                    Exercise exercise = new Exercise(0, count, chosenExercise);
                    physicalMonitor.addExercise(exercise);
                } else {
                    System.out.print("Enter duration in hours: ");
                    double hours = In.nextDouble();
                    Exercise exercise = new Exercise(hours, 0, chosenExercise);
                    physicalMonitor.addExercise(exercise);
                }

                System.out.println("Exercise added successfully.");
                physical.add(physicalMonitor);
            } else {
                System.out.println("Invalid exercise choice.");
            }

            System.out.println("Do you want to add another exercise? (1 for Yes, 0 for No)");
            int continueChoice = In.nextInt();
            addingExercises = (continueChoice == 1);
        }
        DailyLog dailyLog = system.currentUser.getFitnessHistory().getDailyLog(day, month, year);
        if (dailyLog == null) {
            dailyLog = new DailyLog(day, month, year);
        }
        for (PhysicalMonitor pm : physical) {
            dailyLog.setExercises(pm.getExercises());
        }
        // if (system.currentUser.getCaloriesCalculation() ==
        // CalculateExcerciseCalories.PER_EXCERCISE) {
        // system.currentUser.burnCalories(dailyLog.getCaloriesBurntFromExercisesByCount());
        // } else {
        // system.currentUser.burnCalories(dailyLog.getCaloriesBurntFromExercisesByDuration());
        // }
        system.currentUser.getFitnessHistory().addOrUpdateDailyLog(dailyLog);
        System.out.println("Exercises added to the daily log.");
        system.currentUser.calculateCalories(dailyLog);
        system.currentUser.calculateImprovement(dailyLog, system.currentUser.getGoal());
    }

    private void modifyExerciseMenu() {
        System.out.println("Enter the date of the exercise to modify (DD MM YYYY):");
        System.out.print("Day: ");
        int day = In.nextInt();
        System.out.print("Month: ");
        int month = In.nextInt();
        System.out.print("Year: ");
        int year = In.nextInt();

        DailyLog dailyLog = system.currentUser.getFitnessHistory().getDailyLog(day, month, year);
        if (dailyLog != null) {
            List<Exercise> exercises = dailyLog.getTotalExcercises();
            if (exercises != null && !exercises.isEmpty()) {
                System.out.println("Choose an exercise to modify:");
                for (int i = 0; i < exercises.size(); i++) {
                    System.out.println((i + 1) + ". " + exercises.get(i).getExerciseType().name);
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
                    system.currentUser.calculateCalories(dailyLog);
                    system.currentUser.getFitnessHistory().addOrUpdateDailyLog(dailyLog);
                    System.out.println("Exercise modified successfully.");
                } else {
                    System.out.println("Invalid exercise choice.");
                }
            } else {
                System.out.println("No exercises found for this date.");
            }
            // if (system.currentUser.getCaloriesCalculation() ==
            // CalculateExcerciseCalories.PER_EXCERCISE) {
            // system.currentUser.burnCalories(dailyLog.getCaloriesBurntFromExercisesByCount());
            // } else {
            // system.currentUser.burnCalories(dailyLog.getCaloriesBurntFromExercisesByDuration());
            // }
            system.currentUser.getFitnessHistory().addOrUpdateDailyLog(dailyLog);
            system.currentUser.calculateImprovement(dailyLog, system.currentUser.getGoal());

        } else {
            System.out.println("No daily log found for the specified date.");
        }
    }

    private void addCaloriesMenu() {
        System.out.println("Enter the date to add calories (DD MM YYYY):");
        System.out.print("Day: ");
        int day = In.nextInt();
        System.out.print("Month: ");
        int month = In.nextInt();
        System.out.print("Year: ");
        int year = In.nextInt();

        DailyLog dailyLog = system.currentUser.getFitnessHistory().getDailyLog(day, month, year);
        if (dailyLog == null) {
            System.out.println("No daily Log on this day");
            // dailyLog = new DailyLog(day, month, year);
        } else {

            System.out.println("1. Add Calories Consumed");
            System.out.println("2. Add Calories Burnt");
            System.out.print("Enter your choice: ");
            int calorieChoice = In.nextInt();

            if (calorieChoice == 1) {
                System.out.print("Enter calories consumed: ");
                double calories = In.nextDouble();
                dailyLog.addCalories(calories);
                System.out.println("Calories consumed added successfully.");
            } else if (calorieChoice == 2) {
                System.out.print("Enter calories burnt: ");
                double calories = In.nextDouble();
                dailyLog.burmCalories(calories);
                System.out.println("Calories burnt added successfully.");
            } else {
                System.out.println("Invalid choice.");
            }
            system.currentUser.getFitnessHistory().addOrUpdateDailyLog(dailyLog);
            system.currentUser.calculateCalories(dailyLog);
        }
    }

    private void logSleepHourMenu() {
        System.out.println("Enter the date to log sleep hours (DD MM YYYY):");
        System.out.print("Day: ");
        int day = In.nextInt();
        System.out.print("Month: ");
        int month = In.nextInt();
        System.out.print("Year: ");
        int year = In.nextInt();

        DailyLog dailyLog = system.currentUser.getFitnessHistory().getDailyLog(day, month, year);
        if (dailyLog == null) {
            dailyLog = new DailyLog(day, month, year);
        }

        System.out.print("Enter hours of sleep: ");
        double sleepHours = In.nextDouble();
        dailyLog.addSleepHours(sleepHours);
        System.out.println("Sleep hours added successfully.");

        system.currentUser.getFitnessHistory().addOrUpdateDailyLog(dailyLog);
    }

    private void getDailyLogMenu() {
        System.out.println("Enter the date to view: ");
        System.out.print("Day: ");
        int day = In.nextInt();
        System.out.print("Month: ");
        int month = In.nextInt();
        System.out.print("Year: ");
        int year = In.nextInt();

        DailyLog dailyLog = system.currentUser.getFitnessHistory().getDailyLog(day, month, year);
        if (dailyLog != null) {
            dailyLog.viewDailyLog(system.currentUser.getCaloriesCalculation());
        } else {
            System.out.println("No daily log found for the specified date.");
        }
    }

    private void viewHistoryMenu() {
        system.currentUser.viewHistory();
    }
}
