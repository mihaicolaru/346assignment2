package src;/*
Abhinav Batra; 40086809
Mihai Olaru; 40111734

Programming Assignment 2
Simulating Fair-share Process Scheduling
*/

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Scheduler scheduler = new Scheduler();

        Thread ScheduleThread = new Thread(scheduler);
        ScheduleThread.start(); //starts scheduling thread
        try {
            ScheduleThread.join();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
