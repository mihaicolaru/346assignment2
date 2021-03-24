import java.util.LinkedList;
import java.util.Queue;
//time distributed equally to each process
public class User{
    //user variables
    private char userID;
    private Queue<Process> WaitingProcesses = new LinkedList<>(); //queue of processes that have not reached ready time
    private Queue<Process> ReadyProcesses = new LinkedList<>(); //queue of processes available to start running

    //constructor
    public User(char userID) {
        this.userID = userID;
    }

    //getters and setters
    public Queue<Process> getReadyProcesses() {
        return ReadyProcesses;
    }

    public Queue<Process> getWaitingProcesses() { return WaitingProcesses; }

    public char getUserID() {
        return userID;
    }

}
