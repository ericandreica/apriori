import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

class AprioriRedTranzThreads {
    private List<Set<String>> transactions = new ArrayList<>();

    private Map<String, Integer> fkitmes = new HashMap<>();

    private Map<Integer, Map<Set<String>, Integer>> kitemset = new HashMap<>();

    private double minsup;

    private double minconf;

    private double nrTr;

    private int k;

    //private int nrit = 0;

    AprioriRedTranzThreads(String file, double minsup, double minconf) {
        this.minsup = minsup;
        this.minconf = minconf;

        readInput(file);

        initItemSet();

        aprioriRedTranzAlgorithm();

        //System.out.println("Numar total iteratii: " + nrit);

    }

    private void readInput(String file) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                List<String> transactionList = new ArrayList<>(Arrays.asList(line.split(Pattern.quote(" "))));
                Set<String> transaction = new HashSet<String>(transactionList);
                // read next line
                transactions.add(transaction);
                //add new items to 1-items
                for (String item : transactionList)
                    fkitmes.put(item, 0);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nrTr = transactions.size();
    }

    private void initItemSet() {
        List<Set<String>> ntransactions = new ArrayList<>();
        int nrit = 0;
        //for each transaction
        for (Set set : transactions) {
            List<String> list = new ArrayList<String>(set);
            for (String item : list) {
                fkitmes.put(item, fkitmes.get(item) + 1);
                nrit++;
            }
            if (set.size() > 1)
                ntransactions.add(set);
        }
        System.out.println(1+"---"+nrit);
        transactions = ntransactions;
        //add to kitemset
        Map<Set<String>, Integer> fmap = new HashMap<>();
        for (String item : fkitmes.keySet()) {
            double sup = fkitmes.get(item) / nrTr;
            if (sup >= minsup) {
                Set<String> fitems = new HashSet<>();
                fitems.add(item);
                fmap.put(fitems, fkitmes.get(item));
            }
        }
        kitemset.put(1, fmap);
    }

    private void showKSet(Integer k) {
        for (Set set : kitemset.get(k).keySet()) {
            double nr = kitemset.get(k).get(set);
            System.out.println(set + "--" + nr + "--" + nr / nrTr);
        }
    }

    private Map<Set<String>, Integer> generateCand(int k) {
        Map<Set<String>, Integer> candMap = new HashMap<>();
        Map<Set<String>, Integer> imap = kitemset.get(k);
        for (Set<String> iset : imap.keySet()) {
            for (Set<String> jset : imap.keySet()) {
                Set<String> union = new HashSet<>();
                union.addAll(iset);
                union.addAll(jset);
                if (union.size() == k + 1)
                    candMap.put(union, 0);
            }
        }
        return candMap;
    }

    private void aprioriRedTranzAlgorithm() {
        k = 1;
        while (!kitemset.get(k).isEmpty()) {
            AtomicInteger nrit = new AtomicInteger();
            //showKSet(k);
            Map<Set<String>, Integer> candMap = generateCand(k);
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
        //confidence
        for (int i = 2; i < k; i++) {
            for (Set set : kitemset.get(i).keySet()) {
                //System.out.println(set);
                List<Set<String>> subsets = getSubsets(set);
                for (Set<String> subset : subsets) {
                    double nr = kitemset.get(subset.size()).get(subset);
                    double conf = kitemset.get(i).get(set) / nr;
                    if (conf >= minconf) {
                        Set dset = new HashSet(set);
                        dset.removeAll(subset);
                        //System.out.println(subset + "-->" + dset + "---" + conf);
                    }
                }
            }
        }
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

    private List<Set<String>> getSubsets(Set<String> myset) {

        ArrayList<Set<String>> sets = new ArrayList<>();
        String[] set = myset.toArray(new String[myset.size()]);
        int n = set.length;

        // Run a loop from 0 to 2^n
        for (int i = 0; i < (1 << n); i++) {
            Set<String> nset = new HashSet<>();
            int m = 1; // m is used to check set bit in binary representation.
            // Print current subset
            for (String aSet : set) {
                if ((i & m) > 0) {
                    nset.add(aSet);
                }
                m = m << 1;
            }
            sets.add(nset);
        }
        sets.remove(sets.size() - 1);
        sets.remove(0);
        return sets;
    }
}
