//handles what to switch and when to switch
//handles processes in cyclical manner
//equal quantum for each user
//first scheduler cycle at 1
//no priority in users or processes
import java.util.*;
import java.io.*;
import java.time.*;

public class Scheduler implements Runnable{
    //scheduler variables
    private boolean nextCycle;
    private boolean userFound;
    private int numUsers;
    private int numProcesses;
    private int processQuantum;
    private int userQuantum;
    private int quantum;
    private long start;
    private Queue<User> UserQueue;
    private Queue<Process> readyQueue;
    private Map<String, Integer[]> process_quantum;

    //scheduler constructor calls InputReader to instantiate users and processes
    public Scheduler(){
        this.nextCycle = false;
        this.userFound = false;
        this.numUsers = 0;
        this.numProcesses = 0;
        this.processQuantum = 0;
        this.userQuantum = 0;
        this.quantum = 0;
        this.start = 0;
        this.UserQueue = new LinkedList<>();
        this.readyQueue = new LinkedList<>();
        this.process_quantum = new HashMap<>();
        InputReader();
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    //reads input from Input.txt
    public void InputReader(){
        try {
            File file = new File("Input.txt");
            Scanner scan = new Scanner(file);
            setQuantum(scan.nextInt());
            while(scan.hasNextLine()){
                char temp = (scan.next()).charAt(0);
                if((65 <= temp && temp<= 90) || (97 <= temp && temp<= 122)) //if a letter
                {
                    User NewUser = new User(temp); //new user with letter ID
                    UserQueue.add(NewUser); //add new user to the queue
                    temp = (scan.next()).charAt(0); //number of processes
                    for (int i = 0; i < (temp-48); i++) { //loop and find ready and service time for each process
                       Process NewProcess = new Process(); //create new process
                       NewProcess.setReadyTime(scan.nextInt());
                       NewProcess.setServiceTime(scan.nextInt());
                       NewProcess.setProcessID(i);
                       NewProcess.setUserID(NewUser.getUserID());
                        NewUser.getProcesses().add(NewProcess);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //outputs scheduler activity to Output.txt
    public void OutputToFile(String OutputString, boolean append) //function to write file output given an array to write
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

    @Override
    public void run() {
        //pointer to current system time when scheduler starts executing processes
        start = System.currentTimeMillis();

        //temporary set time to 1
        try{
            Thread.sleep(1000);
        } catch(Exception e){
            e.printStackTrace();
        }

        nextCycle = true;

        //process execution cycle
        while(nextCycle){
            numUsers = 0;//at every cycle the assumed number of users with ready processes is 0
            numProcesses = 0;//at every cycle the assumed number of ready processes is 0

            //for every process of every user in user queue, if process has reached ready time, add to ready queue
            for(User User : UserQueue){
                userFound = false;
                for(Process process : User.getProcesses()){
                    if((process.getReadyTime() <= (System.currentTimeMillis() - start) / 1000) && process.getServiceTime() > 0){
                        readyQueue.add(process);
                        userFound = true;
                        numProcesses++;
                    }
                }
                if(userFound) numUsers++;
                User.setNumReady(numProcesses);

                numProcesses = 0;
            }

            //assign time quantum per user
            userQuantum = 0;
            if(numUsers != 0){
                userQuantum = quantum / numUsers;
            }

            //assign quantum to every process of each user
            for(User user : UserQueue){
                processQuantum = 0;//initialize for every user
                if(user.getNumReady() != 0){//if the user has processes that need executing, the quantum per user is divided between the number of processes
                    processQuantum = userQuantum / user.getNumReady();
                }



                //set quantum to every process (only those in ready queue will run) needs revision
                for(Process process : user.getProcesses()){
                    process.setProcessQuantum(processQuantum);
                }
            }

            //execute processes in the ready queue
            for(Process process : readyQueue){

                Thread running = new Thread(process);

                //if process is executing for the first time, output correct message
                if(!process.getStarted()){
                    OutputToFile("Time " + (System.currentTimeMillis() - start)/1000 + ", User " + process.getUserID() + ", Process " + process.getProcessID() + ", Started", true);
                    process.setStarted(true);
                }

                OutputToFile("Time " + (System.currentTimeMillis() - start)/1000 + ", User " + process.getUserID() + ", Process " + process.getProcessID() + ", Resumed", true);

                //compare ready time to process quantum to see if the process has to execute for its allotted quantum or remaining time
                if(process.getProcessQuantum() < process.getServiceTime()){
                    running.start();

                    try{
                        Thread.sleep(process.getProcessQuantum() * 1000);
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                    try{
                        running.join(process.getProcessQuantum());
                        OutputToFile("Time " + (System.currentTimeMillis() - start)/1000 + ", User " + process.getUserID() + ", Process " + process.getProcessID() + ", Paused", true);

                    } catch(Exception e){
                        e.printStackTrace();
                    }

                }
                else{
                    running.start();

                    try{
                        Thread.sleep(process.getServiceTime() * 1000);
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                    try{
                        running.join(process.getServiceTime());
                        OutputToFile("Time " + (System.currentTimeMillis() - start)/1000 + ", User " + process.getUserID() + ", Process " + process.getProcessID() + ", Paused", true);
                        OutputToFile("Time " + (System.currentTimeMillis() - start)/1000 + ", User " + process.getUserID() + ", Process " + process.getProcessID() + ", Finished", true);

                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

            //clear the ready queue
            readyQueue.clear();

            nextCycle = false;

            //check if scheduler needs next cycle
            for(User user : UserQueue){
                if(nextCycle) break;
                for(Process process : user.getProcesses()){
                    if(process.getServiceTime() > 0){
                        nextCycle = true;
                        break;
                    }
                }
            }
        }
    }
}
