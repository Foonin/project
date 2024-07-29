import java.util.List;

interface SleepTracker {
    void trackSleepHours(double hours);
}

class StressMonitor extends Feature implements SleepTracker {
    double hoursOfSleep;

    // Constructor to initialize the hoursOfSleep attribute
    StressMonitor() {
        this.hoursOfSleep = 0;
    }

    // Override the method from SleepTracker
    @Override
    public void trackSleepHours(double hours) {
        this.hoursOfSleep += hours;
    }

    // Accessor method for hoursOfSleep
    public double getHoursOfSleep() {
        return this.hoursOfSleep;
    }
}

class DailyLog extends FitnessHistory {
    int days;
    int months;
    int year;
    String date;
    List<Feature> features; // Add features to this array
    double caloriesBurnt;
    double caloriesConsumed;
    double caloriesBurntFromExercisesByCount;
    double caloriesBurntFromExercisesByDuration;
    double hoursOfSleep;
    double improvementPercentage;
    double bmi;
    FitnessUser user;

    // Initialises each attribute using polymorphism
    DailyLog(int days, int months, int year, List<Feature> features, FitnessUser user) {
        this.days = days;
        this.months = months;
        this.year = year;
        this.date = this.days + "/" + this.months + "/" + this.year;
        this.features = features;
        this.user = user;
        this.bmi = this.user.getWeight() / (this.user.getHeight() * this.user.getHeight());

        for (Feature f : this.features) {
            if (f instanceof PhysicalMonitor) {
                PhysicalMonitor p = (PhysicalMonitor) f;
                this.caloriesBurnt = p.caloriesBurnt;
                this.caloriesConsumed = p.caloriesConsumed;
                this.caloriesBurntFromExercisesByCount = p.trackExerciseCaloriesBurntByCount();
                this.caloriesBurntFromExercisesByDuration = p.trackExerciseCaloriesBurntByDuration();
            } else if (f instanceof StressMonitor) {
                StressMonitor s = (StressMonitor) f;
                this.hoursOfSleep = s.getHoursOfSleep();
            }
        }
    }

    // Calculates improvement
    void calculateImprovement() {
        double totalImprovement = 0;
        int comparisons = 0;

        for (int i = 0; i < super.history.size() - 1; i++) {
            DailyLog currentLog = super.history.get(i);
            DailyLog nextLog = super.history.get(i + 1);

            double dailyImprovement = ((nextLog.bmi - currentLog.bmi) / currentLog.bmi) * 100;
            totalImprovement += dailyImprovement;
            comparisons++;
        }

        if (comparisons > 0) {
            double averageImprovement = totalImprovement / comparisons;

            if (this.user.getGoal().equals(Goal.LOSE_WEIGHT)) {
                this.improvementPercentage = -averageImprovement;
            } else if (this.user.getGoal().equals(Goal.GAIN_WEIGHT)) {
                this.improvementPercentage = averageImprovement;
            } else if (this.user.getGoal().equals(Goal.MAINTAIN_WEIGHT)) {
                this.improvementPercentage = averageImprovement > 0 ? -averageImprovement : averageImprovement;
            }
        }
    }

    // Accessor methods
    String getDate() {
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

    double getCaloriesBurnt() {
        return this.caloriesBurnt;
    }

    double getCaloriesConsumed() {
        return this.caloriesConsumed;
    }

    double getCaloriesBurntFromExercisesByCount() {
        return this.caloriesBurntFromExercisesByCount;
    }

    double getCaloriesBurntFromExercisesByDuration() {
        return this.caloriesBurntFromExercisesByDuration;
    }
}
