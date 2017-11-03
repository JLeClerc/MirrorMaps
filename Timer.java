package MirrorMaps;

//Credit goes to StackOverflow user StationaryTraveller (https://stackoverflow.com/users/2204803/stationarytraveller) for this.
public class Timer {
    private static long start_time;
    public static double tic(){
        return start_time = System.nanoTime();
    }
    public static double toc(){
        return (System.nanoTime()-start_time)/1000000000.0;
    }
}
