package src;//handles what to switch and when to switch
//handles processes in cyclical manner
//equal quantum for each user
//first scheduler cycle at 1
//no priority in users or processes

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Scheduler implements Runnable{
    private long start;
    private int quantum;
    private Queue<User> UserQueue;
    private Queue<Process> ReadyQueue;

    public Scheduler(){
        this.UserQueue = new LinkedList<>();
        this.ReadyQueue = new LinkedList<>();
        this.start = 0;
        this.quantum = 0;
        InputReader();
    }

    public void addProcess(Process process){
        ReadyQueue.add(process);
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public void InputReader(){
        System.out.println("Reading from file");
        try {
            File file = new File("Input.txt");
            Scanner scan = new Scanner(file);
            setQuantum(scan.nextInt());
            System.out.println("Quantum: " + quantum);
            while(scan.hasNextLine()){
                char temp = (scan.next()).charAt(0);
                if((65 <= temp && temp<= 90) || (97 <= temp && temp<= 122)) //if a letter
                {
                    User NewUser = new User(temp); //new user with letter ID
                    UserQueue.add(NewUser); //add new user to the queue
                    temp = (scan.next()).charAt(0); //number of processes
                   //System.out.println(NewUser.toString()); //print new user to console
                    for (int i = 0; i < temp-48; i++) { //loop and find ready and service time for each process
                       Process NewProcess = new Process(); //create new process
                       NewProcess.setReadyTime(scan.nextInt());
                       NewProcess.setServiceTime(scan.nextInt());
                       NewProcess.setProcessID(i);
                       NewProcess.setUserID(NewUser.getUserID());
                       NewUser.AddProcess(NewProcess);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //System.out.println(UserQueue);
    }

    @Override
    synchronized public void run() {
        System.out.println("Beginning scheduler");
        start = System.currentTimeMillis();
        //temporary set time to 1
        try{
            Thread.sleep(1000);
        } catch(Exception e){
            e.printStackTrace();
        }

        boolean nextCycle = true;

        while(nextCycle){
            int numUsers = 0;//at every cycle the assumed number of users with ready processes is 0
            int numProcesses = 0;//at every cycle the assumed number of ready processes is 0

            //for every user in user queue
            for(User User : UserQueue){
                //for every process in user process queue
                boolean userFound = false;
                for(Process process : User.getProcesses()){
                    //if process has reached ready time, add to ready queue

                    if((process.getReadyTime() <= (System.currentTimeMillis() - start) / 1000) && process.getServiceTime() > 0){
                        addProcess(process);
                        userFound = true;
                        numProcesses++;
                    }
                }
                if(userFound) numUsers++;
                User.setNumReady(numProcesses);
            }
            int UserQuantum = 0;
            if(numUsers != 0){
                UserQuantum = quantum / numUsers;
            }


            int ProcessQuantum = 0;

            //assigns quantum to every process of each user, only those in ready queue will run
            for(User user : UserQueue){
                if(numProcesses != 0){
                    ProcessQuantum = UserQuantum / numProcesses;
                }

                for(Process process : user.getProcesses()){
                    process.setProcessQuantum(ProcessQuantum);
                }
            }


            //execution of processes in the ready queue
            for(Process process : ReadyQueue){
                System.out.println(process.toString());

                Thread running = new Thread(process);
                running.start();
                System.out.println("Time " + (System.currentTimeMillis() - start)/1000 + ", User " + process.getUserID() + ", Process " + process.getProcessID() + ", Resumed");

                if(process.getProcessQuantum() < process.getServiceTime()){
                    try{
                        Thread.sleep(1000);
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                    try{
                        running.join(process.getProcessQuantum());
                        System.out.println("Time " + (System.currentTimeMillis() - start)/1000 + ", User " + process.getUserID() + ", Process " + process.getProcessID() + ", Paused");

                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    try{
                        running.join(process.getServiceTime());
                        System.out.println("Time " + (System.currentTimeMillis() - start)/1000 + ", User " + process.getUserID() + ", Process " + process.getProcessID() + ", Finished");

                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            ReadyQueue.clear();

            nextCycle = false;

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
