import java.util.LinkedList;
import java.util.Queue;
//time distributed equally to each process
public class User{
    //user variables
    private char userID;
    private Queue<Process> WaitingProcesses = new LinkedList<>();
    private Queue<Process> ReadyProcesses = new LinkedList<>();

    //constructor
    public User(char userID) {
        this.userID = userID;
    }

    public Queue<Process> getReadyProcesses() {
        return ReadyProcesses;
    }

    public void setReadyProcesses(Queue<Process> readyProcesses) {
        ReadyProcesses = readyProcesses;
    }

    //getters and setters
    public Queue<Process> getWaitingProcesses() { return WaitingProcesses; }

    public char getUserID() {
        return userID;
    }
    public void setUserID(char userID) {
        this.userID = userID;
    }

}
