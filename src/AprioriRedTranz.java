import java.util.*;

class AprioriRedTranz {

    private BaseClass base;
    //private int nrit = 0;
    private List<Set<String>> transactions;
    private Map<Integer, Map<Set<String>, Integer>> kitemset;
    private double minsup;
    private double minconf;
    private double nrTr;


    AprioriRedTranz(String file, double minsup, double minconf) {

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
        int k = 1;
        while (!kitemset.get(k).isEmpty()) {
            int nrit = 0;
            //base.showKSet(k);
            Map<Set<String>, Integer> candMap = base.generateCand(k);
            if (candMap.isEmpty()) {
                k++;
                break;
            }
            List<Set<String>> ntransactions = new ArrayList<>();
            for (Set transaction : transactions) {
                int count = 0;
                for (Set cset : candMap.keySet()) {
                    nrit++;
                    if (transaction.containsAll(cset)) {
                        count++;
                        candMap.put(cset, candMap.get(cset) + 1);
                    }
                }
                if ( (count>0) && (transaction.size() > k + 1)) {
                    ntransactions.add(transaction);
                }
            }
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

}
