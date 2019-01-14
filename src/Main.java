public class Main {

    public static long startTime;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();
        System.out.println("###Apriori Basic###");
        AprioriBasic aprioriBasic = new AprioriBasic("E:\\facultate\\a1s1\\datamining\\apriori\\src\\test2", 0.02,0.4);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time (ms): " + elapsedTime);

        /*
        startTime = System.currentTimeMillis();
        System.out.println("###Apriori Red. Tranz.###");
        AprioriRedTranz aprioriRedTranz = new AprioriRedTranz("E:\\facultate\\a1s1\\datamining\\apriori\\src\\test2", 0.02,0.4);
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time (ms): " + elapsedTime);

        startTime = System.currentTimeMillis();
        System.out.println("###Apriori Red. Tranz. - Threads###");
        AprioriRedTranzThreads aprioriRedTranzThreads = new AprioriRedTranzThreads("E:\\facultate\\a1s1\\datamining\\apriori\\src\\test2", 0.02,0.4);
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time (ms): " + elapsedTime);


        startTime = System.currentTimeMillis();
        System.out.println("###Apriori Basic  - Threads###");
        AprioriBasicThreads aprioriBasicThreads = new AprioriBasicThreads("E:\\facultate\\a1s1\\datamining\\apriori\\src\\test2", 0.02,0.4);
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time (ms): " + elapsedTime);
        */

    }
}
