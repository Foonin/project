import java.util.*;
interface verifyAccount{
    void logIn(String username, String password);
    void logOut(String username);
    void addAccount(String username, String password, String email, double weight, double height, Goal goal, CalculateExcerciseCalories caloriescalculation);
}

class Systems implements verifyAccount{
    int availableId;
    Map<Integer, FitnessUser> accounts; // 
    List<FitnessUser> signedInAccount; //Optimize better than Arraylist
    Systems(){
        this.accounts = new HashMap<>();
        this.signedInAccount= new LinkedList<>();
        this.availableId = 0;
    }
    public void addAccount(String username, String password, String email, double weight, double height, Goal goal, CalculateExcerciseCalories caloriescalculation){
        this.availableId++;
        this.accounts.put(availableId, new FitnessUser(username, password, email, weight, height, goal, caloriescalculation));
        this.accounts.get(availableId).initializeAccount();
    }
    public void logIn(String username, String password){
        for (Map.Entry<Integer,FitnessUser> entry : this.accounts.entrySet()){
            if (entry.getValue().getName().equals(username)){
                boolean sucess = entry.getValue().authorized(password);
                if (sucess){
                    this.signedInAccount.add(entry.getValue());
                    System.out.println("Log In successfully");
                    return;
                }
                else{
                    System.out.println("Log In unsuccessfully");
                }
            }
        }
    }
    public void logIn(int id, String password){
        FitnessUser logInUser = this.accounts.get(id);
        boolean sucess = logInUser.authorized(password);
        if (sucess){
            this.signedInAccount.add(logInUser);
            System.out.println("Log In successfully");
            return;
        }
        else{
            System.out.println("Log In unsuccessfully");
        }
    }
        
    public void logOut(String username){
        for (FitnessUser user : this.signedInAccount){
            if (user.getName().equals(username)){
                this.signedInAccount.remove(user);
                System.out.println("Log Out successfully");
            }
            else{
                System.out.println("Log Out unsuccessfully");
            }
        }
    }
    //testing part -> REMOVE AFTER DONE
    public static void main(String[] args) {
        Systems s = new Systems();
        s.addAccount("tlmthong", "s12hb321", null, 4, 1, null, null);
        s.addAccount("tlmthong2", "s12erw21", null, 4, 1, null, null);
        s.addAccount("tlmth23ng", "s1sdf321", null, 4, 1, null, null);
        s.addAccount("tlmth3ng", "s2343241", null, 4, 1, null, null);
        System.out.println(s.accounts.get(1).printPass());
        System.out.println(s.accounts.get(1).authorized("s12hb321"));
        s.logIn(3,"s1sdf321");
    }
}
