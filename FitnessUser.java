public class FitnessUser extends User{
    double weight;
    double height;
    Goal goal;
    CalculateExcerciseCalories caloriesCalculation;
    private int passcode;
    FitnessUser(String username, String password, String email, double weight, double height, Goal goal, CalculateExcerciseCalories caloriescalculation, int passcode){
        super(username, password, email);
        this.height = height;
        this.weight = weight;
        this.caloriesCalculation = caloriescalculation;
        this.goal = goal;
        this.passcode = passcode;
    }
    private String encrypt(String password){
        String encryptString = password + this.passcode;
        return encryptString;
    }
    private boolean decrypt(String password){
        boolean matchPass = encrypt(password).equals(this.password);
        return matchPass;
    }
    public void initializeAccount(){
        this.password = encrypt(this.password);
    }
    public boolean authorized(String password){
        return decrypt(password);
    }
    public String printPass(){
        return this.password;
    }
}

interface  UserAuthentication{
    String encrypt();
    boolean decrypt();
    void initializeAccount();
    boolean authorized();
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
enum ImprovementMetric{

}
enum CalculateExcerciseCalories{
    PER_EXCERCISE,
    DURATION_OF_EXCERCISE
}