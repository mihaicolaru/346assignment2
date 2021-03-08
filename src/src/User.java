package src;

import java.util.LinkedList;
import java.util.Queue;

//time distributed equally to each process
public class User{
    private char userID;
    private int numReady;
    private Queue<Process> Processes = new LinkedList<>();

    public User(char userID) {
        this.numReady = 0;
        this.userID = userID;
    }

    public void AddProcess(Process NewProcess) //add a new process in the process Queue
    {
        Processes.add(NewProcess);
    }



    public int getNumProcess(){ return numReady; }

    public void setNumReady(int numReady){
        this.numReady = numReady;
    }

    public Queue<Process> getProcesses() {
        return Processes;
    }


    public void setProcesses(Queue<Process> processes) {
        Processes = processes;
    }


    public char getUserID() {
        return userID;
    }

    public void setUserID(char userID) {
        this.userID = userID;
    }




    @Override
    public String toString() {
        return "User" + userID + ": " +
                " Waiting Queue: " + Processes.toString() + "\n";
    }
}
