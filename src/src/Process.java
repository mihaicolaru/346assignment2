package src;

public class Process implements Runnable{
    //instance variables
    //always positive values
    private char userID;
    private int processID;
    private int readyTime; //when the process is ready
    private int serviceTime; //total CPU usage required by each process

    private int processQuantum;

    public int getProcessQuantum(){ return processQuantum; }

    public void setProcessQuantum(int quantum){ this.processQuantum = quantum; }

    public char getUserID(){ return userID; }

    public void setUserID(char ID){ this.userID = ID; }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public int getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(int readyTime) {
        this.readyTime = readyTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Override
    public String toString() {
        return "Process " + processID +" {" +
                "readyTime = " + readyTime +
                ", serviceTime = " + serviceTime +
                ", current quantum allotted = " + processQuantum +
                "}";
    }
    synchronized public void run() {
        if(getProcessQuantum() < getServiceTime()){
            setServiceTime(getServiceTime() - getProcessQuantum());
        }
        else setServiceTime(0);
    }
}
