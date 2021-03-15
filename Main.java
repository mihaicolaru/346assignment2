/*
Abhinav Batra; 40086809
Mihai Olaru; 40111734

Programming Assignment 2
Simulating Fair-shar Process Scheduling
*/

public class Main {
    public static void main(String[] args) {

        Scheduler scheduler = new Scheduler();
        scheduler.OutputToFile("", false);

        Thread SchedulerThread = new Thread(scheduler);
        SchedulerThread.start(); //starts scheduling thread

        try {
            SchedulerThread.join();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
