import java.util.List;

public class mainMethod {
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
                    running = false;
                    System.out.println("Thank you for using the Fitness Tracker. Goodbye!");
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
                            // View Fitness History
                            system.currentUser.getFitnessHistory().calculateAllImprovements();
                            system.currentUser.getFitnessHistory().viewFitnessHistory();
                        } else if (exerciseChoice == 7) {
                            // Log Out
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
                In.nextLine(); // Clear the input buffer
            }
        }
    }
}