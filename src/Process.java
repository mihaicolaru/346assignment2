public class Process implements Runnable{
    //instance variables
    //always positive values
    private char userID;
    private int processID;
    private int readyTime; //when the process is ready
    private int serviceTime; //total CPU usage required by each process
    private boolean started;
    private int processQuantum;//has to be removed

    public Process()
    {
        started = false;
    }

    //getters and setters
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

    public boolean getStarted(){ return started;}
    public void setStarted(boolean isStarted){ this.started = isStarted; }

    public void run() {
        //service time changed according to how long process executes
        if(getProcessQuantum() < getServiceTime()){
            setServiceTime(getServiceTime() - getProcessQuantum());
        }
        else setServiceTime(0);
    }
}
