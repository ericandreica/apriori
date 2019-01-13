import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

class AprioriBasicThreads {


    private List<Set<String>> transactions;
    private Map<Integer, Map<Set<String>, Integer>> kitemset;
    private double minsup;
    private double minconf;
    private double nrTr;
    private int k;

    private BaseClass base;

    AprioriBasicThreads(String file, double minsup, double minconf) {

        boolean isReduced = false;
        this.base = new BaseClass(file, minsup, minconf, isReduced);
        this.transactions = base.getTransactions();
        this.kitemset = base.getKitemset();
        this.minsup = base.getMinsup();
        this.minconf = base.getMinconf();
        this.nrTr = base.getNrTr();


        aprioriBasicAlgorithmThreads();

        //System.out.println("Numar total iteratii: "+nrit);

    }

    private void aprioriBasicAlgorithmThreads() {
        k = 1;
        while (!kitemset.get(k).isEmpty()) {
            AtomicInteger nrit = new AtomicInteger();
            //base.showKSet(k);
            Map<Set<String>, Integer> candMap = base.generateCand(k);
            if (candMap.isEmpty()) {
                k++;
                break;
            }


            ExecutorService executorService = Executors.newFixedThreadPool(4);
            List<Callable<Object>> todo = new ArrayList<Callable<Object>>(transactions.size());
            for (Set transaction : transactions) {
                todo.add(Executors.callable(() -> nrit.set(parseTransaction(nrit.get(), candMap, transaction))));
            }
            try {
                List<Future<Object>> answers = executorService.invokeAll(todo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println((k+1)+"---"+nrit);
            Map<Set<String>, Integer> kmap = new HashMap<>();
            for (Set cset : candMap.keySet()) {
                double sup = candMap.get(cset) / nrTr;
                if (sup >= minsup) {
                    kmap.put(cset, candMap.get(cset));
                }
            }
            k++;
            kitemset.put(k, kmap);
        }
        base.setTransactions(transactions);
        base.setKitemset(kitemset);
        base.calculateConfidence(k);
    }

    private int parseTransaction(int nrit, Map<Set<String>, Integer> candMap, Set transaction) {

        for (Set cset : candMap.keySet()) {
            nrit++;
            if (transaction.containsAll(cset))
                candMap.put(cset, candMap.get(cset) + 1);
        }

        return nrit;
    }


}