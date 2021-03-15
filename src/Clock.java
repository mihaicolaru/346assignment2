public class Clock {
    static private int Clock;

    static public void Tick(int tick)
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
    }
}
