import java.util.*;

public class Main {


    public static void main(String[] args) {

        String filepath = "C://Users//Artyomska//IdeaProjects//apriori//src//test2";

        double minsup = 0.03;
        double minconf = 0.4;

        long startTime = System.currentTimeMillis();
        System.out.println("###Apriori Basic###");
        AprioriBasic aprioriBasic = new AprioriBasic(filepath, minsup,minconf);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time (ms): " + elapsedTime);

        startTime = System.currentTimeMillis();
        System.out.println("###Apriori Basic. - Threads###");
        AprioriBasicThreads aprioriBasicThreads = new AprioriBasicThreads(filepath, minsup,minconf);
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time (ms): " + elapsedTime);

        startTime = System.currentTimeMillis();
        System.out.println("###Apriori Red. Tranz.###");
        AprioriRedTranz aprioriRedTranz = new AprioriRedTranz(filepath, minsup,minconf);
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time (ms): " + elapsedTime);

        startTime = System.currentTimeMillis();
        System.out.println("###Apriori Red. Tranz. - Threads###");
        AprioriRedTranzThreads aprioriRedTranzThreads = new AprioriRedTranzThreads(filepath, minsup,minconf);
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time (ms): " + elapsedTime);
    }
}
