public class Main {


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("###Apriori Basic###");
        AprioriBasic aprioriBasic = new AprioriBasic("//Users//ionutmarisca//Documents//Projects//apriori//src//test2", 0.03,0.4);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time (ms): " + elapsedTime);

        startTime = System.currentTimeMillis();
        System.out.println("###Apriori Red. Tranz.###");
        AprioriRedTranz aprioriRedTranz = new AprioriRedTranz("//Users//ionutmarisca//Documents//Projects//apriori//src//test2", 0.03,0.4);
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time (ms): " + elapsedTime);

        startTime = System.currentTimeMillis();
        System.out.println("###Apriori Red. Tranz. - Threads###");
        AprioriRedTranzThreads aprioriRedTranzThreads = new AprioriRedTranzThreads("//Users//ionutmarisca//Documents//Projects//apriori//src//test2", 0.03,0.4);
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time (ms): " + elapsedTime);
    }
}
