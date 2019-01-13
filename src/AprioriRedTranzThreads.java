import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

class AprioriRedTranzThreads {

    private List<Set<String>> transactions;
    private Map<Integer, Map<Set<String>, Integer>> kitemset;
    private double minsup;
    private double minconf;
    private double nrTr;
    private int k;

    private BaseClass base;

    //private int nrit = 0;

    AprioriRedTranzThreads(String file, double minsup, double minconf) {

        boolean isReduced = true;
        this.base = new BaseClass(file, minsup, minconf, isReduced);
        this.transactions = base.getTransactions();
        this.kitemset = base.getKitemset();
        this.minsup = base.getMinsup();
        this.minconf = base.getMinconf();
        this.nrTr = base.getNrTr();


        aprioriRedTranzAlgorithm();

        //System.out.println("Numar total iteratii: " + nrit);

    }


    private void aprioriRedTranzAlgorithm() {
        k = 1;
        while (!kitemset.get(k).isEmpty()) {
            AtomicInteger nrit = new AtomicInteger();
            //showKSet(k);
            Map<Set<String>, Integer> candMap = base.generateCand(k);
            if (candMap.isEmpty()) {
                k++;
                break;
            }
            List<Set<String>> ntransactions = new ArrayList<>();
            ExecutorService executorService = Executors.newFixedThreadPool(4);
            List<Callable<Object>> todo = new ArrayList<Callable<Object>>(transactions.size());
            for (Set transaction : transactions) {
                int parseValue = k;
                todo.add(Executors.callable(() -> nrit.set(parseTransaction(parseValue, nrit.get(), candMap, ntransactions, transaction))));
            }
            try {
                List<Future<Object>> answers = executorService.invokeAll(todo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executorService.shutdown();
            System.out.println((k+1)+"---"+nrit);
            transactions = ntransactions;
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

    private int parseTransaction(int k, int nrit, Map<Set<String>, Integer> candMap, List<Set<String>> ntransactions, Set transaction) {
        int count = 0;
        for (Set cset : candMap.keySet()) {
            nrit++;
            if (transaction != null && transaction.containsAll(cset)) {
                count++;
                candMap.put(cset, candMap.get(cset) + 1);
            }
        }
        if ( (count>0) && (transaction.size() > k + 1)) {
            ntransactions.add(transaction);
        }
        return nrit;
    }
}

