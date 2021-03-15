//handles what to switch and when to switch
//handles processes in cyclical manner
//equal quantum for each user
//first scheduler cycle at 1
//no priority in users or processes
import java.util.*;
import java.io.*;

public class Scheduler implements Runnable{
    //scheduler instance variables
    private boolean nextCycle;
    private int readyUserNum;
    private int processQuantum; //quantum provided to the process
    private int userQuantum; //quantum provided to user
    private int quantum;
    private Queue<User> UserQueue;
    private Queue<Process> readyProcessQueue;
    private Queue<Thread> ProcessThreadQueue;
    private Map<String, Integer> process_quantum;
    private boolean isProcessRunning;

    //scheduler constructor calls InputReader to instantiate users and processes
    public Scheduler(){
        this.nextCycle = true;
        this.readyUserNum = 0;
        this.processQuantum = 0;
        this.userQuantum = 0;
        this.quantum = 0;
        this.UserQueue = new LinkedList<>();
        this.readyProcessQueue = new LinkedList<>();
        this.ProcessThreadQueue = new LinkedList<>();
        this.process_quantum = new HashMap<>();
        InputReader();
    }

    //reads input from Input.txt
    public void InputReader(){
        try {
            File file = new File("Input.txt");
            Scanner scan = new Scanner(file);
            quantum = scan.nextInt();
            while(scan.hasNextLine()){
                char temp = scan.next().charAt(0);
                if((65 <= temp && temp<= 90) || (97 <= temp && temp<= 122)) //if a letter
                {
                    User NewUser = new User(temp); //new user with letter ID
                    UserQueue.add(NewUser); //add new user to the queue
                    temp = (scan.next()).charAt(0); //number of processes
                    for (int i = 0; i < (temp-48); i++) { //loop and find ready and service time for each process
                        Process newProcess = new Process(); //create new process
                        newProcess.setReadyTime(scan.nextInt());
                        newProcess.setServiceTime(scan.nextInt());
                        newProcess.setProcessID("User " + NewUser.getUserID() + ", Process " + i);
                        newProcess.setUserID(NewUser.getUserID());
                        NewUser.getWaitingProcesses().add(newProcess);
                        Thread process = new Thread(newProcess);
                        process.setName(newProcess.getProcessID());
                        ProcessThreadQueue.add(process);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //outputs scheduler activity to Output.txt
    public void OutputToFile(String OutputString, boolean append) //function to write to file
    {
        try { //write to output file
            FileWriter WriteToOutput = new FileWriter("Output.txt", append); //create file writer
            WriteToOutput.write(OutputString + '\n');
            WriteToOutput.close(); //close out the file writer
        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    //first method in cycle
    public void ProcessSetup(){
        readyUserNum = 0; //find and add all ready processes to user's ready queue
        for(User user : UserQueue) {

           for (Process process : user.getWaitingProcesses())
           {
                if ((process.getReadyTime() <= Clock.getTime()) && process.getServiceTime() > 0) {
                    user.getReadyProcesses().add(process);
                    user.getWaitingProcesses().remove(process);
                }
            }

            if (!user.getReadyProcesses().isEmpty()) //if user has a ready process
            {
                readyUserNum++; //add to the amount of users ready
            }
        }//all users have a queue of their ready processes now

        userQuantum = quantum / readyUserNum; //sets a quantum per ready user


    } //every ready process is now in the scheduler's ready queue
    //each process in scheduler's ready queue has an associated hashcode to its specific quantum

    public void AssignQuantum() {
        for(User user: UserQueue) { //for every user in user queue
            if(!user.getReadyProcesses().isEmpty()) { //if user has a ready process
                processQuantum = userQuantum / user.getReadyProcesses().size(); //sets current process quantum to be given to each process for this user
                for (Process process : user.getReadyProcesses()) { //for each process that is ready
                    if(process.getReadyTime() <= Clock.getTime())
                        readyProcessQueue.add(process);
                    process_quantum.put(process.getProcessID(), processQuantum); //create hash relation to process and processquantum
                }
            }

        }
    }

    public void StartProcess(Process process) {
        if (!process.getStarted())
        {//if process is executed for the first time, print proper message and start thread
            for (Thread thread : ProcessThreadQueue) {
                if (thread.getName().equals(process.getProcessID())) {
                    thread.start(); //thread has been started
                    OutputToFile("Time " + Clock.getTime() + ", " + process.getProcessID() + ", Started", true);
                    process.setStarted(true);
                    isProcessRunning = false;
                    break;
                }
            }
        }
    }

    public void RunProcess(Process process) {
        if (!isProcessRunning) {
            OutputToFile("Time " + Clock.getTime() + ", " + process.getProcessID() + ", Resumed", true);
            isProcessRunning = true;
        }
        process.run();
        process_quantum.put(process.getProcessID(), process_quantum.get(process.getProcessID()) - 1);//decrement quantum in hash table
    }

    public void TerminateProcess(Process process) {
        if (process.getServiceTime() == 0 || process_quantum.get(process.getProcessID()) == 0) { //check if process is complete or done quantum
            OutputToFile("Time " + Clock.getTime() + ", " + process.getProcessID() + ", Paused", true); //print the process being paused
            isProcessRunning = false;

            if (process.getServiceTime() == 0) {

                for (Thread thread : ProcessThreadQueue) {
                    if (thread.getName().equals(process.getProcessID())) {
                        try {
                            thread.join();
                            ProcessThreadQueue.remove(thread);
                            OutputToFile("Time " + Clock.getTime() + ", " + process.getProcessID() + ", Finished", true); //if process is finished remove it
                            isProcessRunning = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }

                for (User user : UserQueue) {
                    for (Process myprocess :
                            user.getReadyProcesses()) {
                        if (myprocess == process) {
                            user.getReadyProcesses().remove(process);
                        }
                    }

                }
            }
            process_quantum.remove(process.getProcessID());
            readyProcessQueue.remove(process);

        }
    }

    public void ProcessExecution(){
        Process process = readyProcessQueue.peek();//first process in ready queue
        isProcessRunning = false;

            for (int i = 0; i < quantum; i++) {
                if(process == null) break; //if ready queue is empty break from loop
                else {
                    StartProcess(process);
                    RunProcess(process);
                    Clock.Tick(1);
                    TerminateProcess(process);
                    process = readyProcessQueue.peek(); //finished processes removed and next process assigned
                }
            }

            readyProcessQueue.clear();  //clear the ready queue
            process_quantum.clear();  //clear process quantum for this round

    }

    @Override
    public void run() {
        Clock.Start();

        while(nextCycle){//process execution cycle
            ProcessSetup();
            AssignQuantum();
            ProcessExecution();//process execution
            nextCycle = !ProcessThreadQueue.isEmpty();//check if scheduler needs next cycle based on if any threads are left
        }
    }
}
