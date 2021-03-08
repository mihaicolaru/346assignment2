import java.util.LinkedList;
import java.util.Queue;
//time distributed equally to each process
public class User{
    //user variables
    private char userID;
    private int numReady;
    private Queue<Process> Processes = new LinkedList<>();

    //constructor
    public User(char userID) {
        this.numReady = 0;
        this.userID = userID;
    }

    //getters and setters
    public int getNumReady(){ return numReady; }
    public Queue<Process> getProcesses() { return Processes; }
    public void setNumReady(int numReady){ this.numReady = numReady;}

    public char getUserID() {
        return userID;
    }
    public void setUserID(char userID) {
        this.userID = userID;
    }

}
