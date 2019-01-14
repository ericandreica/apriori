public class TestMain {

    public static long startTime;

    public static long sum = 0;

    public static void main(String[] args) {

        for (int i = 1; i <= 10; i++) {
            startTime = System.currentTimeMillis();

//            AprioriBasic aprioriBasic = new AprioriBasic("E:\\facultate\\a1s1\\datamining\\apriori\\src\\test2", 0.03, 0.4);

//            AprioriRedTranz aprioriRedTranz = new AprioriRedTranz("E:\\facultate\\a1s1\\datamining\\apriori\\src\\test2", 0.03,0.4);

//            AprioriRedTranzThreads aprioriRedTranzThreads = new AprioriRedTranzThreads("E:\\facultate\\a1s1\\datamining\\apriori\\src\\test2", 0.05,0.4);

//            AprioriBasicThreads aprioriBasicThreads = new AprioriBasicThreads("E:\\facultate\\a1s1\\datamining\\apriori\\src\\test2", 0.05,0.4);

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println("Elapsed time (ms): " + elapsedTime);
            sum = sum + elapsedTime;
        }
        System.out.println("Medium time:"+sum/10);
    }

}
