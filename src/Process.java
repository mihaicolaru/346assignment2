public class Process implements Runnable{
    //instance variables
    //always positive values
    private char userID;
    private String processID;
    private int readyTime; //when the process is ready
    private int serviceTime; //total CPU usage required by each process
    private boolean started;
    //private int processQuantum;

    public Process()
    {
        started = false;
    }

    //getters and setters
    public char getUserID(){ return userID; }
    public void setUserID(char ID){ this.userID = ID; }

    public String getProcessID() {
        return processID;
    }
    public void setProcessID(String processID) {
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

    public boolean getStarted(){ return started;}
    public void setStarted(boolean isStarted){ this.started = isStarted; }

    public void run()
    {
        if(started)
        {
            serviceTime--;
        }

    }
}
