public class Clock
{
    static private int Clock; //counter for time elapsed


    static public void Start() //function to start initial tick
    {
            Clock ++;
    }

    static public void Tick(int tick) //take in an integer value and sleep the current thread for that many seconds
    {

        try {
            Thread.sleep(tick*1000);
            Clock += tick;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    static public int getTime()
    {
        return Clock;
    } //return time elapsed
}
