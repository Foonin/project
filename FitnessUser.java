import java.util.HashMap;

public class FitnessUser extends User implements UserAuthentication {
    double weight;
    double height;
    Goal goal;
    String password;
    CalculateExcerciseCalories caloriesCalculation;
    HashMap <Character,Character> encryptionMap = new HashMap<>();
    FitnessUser(String username, String password, String email, double weight, double height, Goal goal, CalculateExcerciseCalories caloriescalculation){
        super(username, password, email);
        this.height = height;
        this.weight = weight;
        this.caloriesCalculation = caloriescalculation;
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
enum ImprovementMetric{

}
enum CalculateExcerciseCalories{
    PER_EXCERCISE,
    DURATION_OF_EXCERCISE
}