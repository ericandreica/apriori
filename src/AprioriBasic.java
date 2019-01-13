import java.util.*;

class AprioriBasic {

    private List<Set<String>> transactions;
    private Map<Integer, Map<Set<String>, Integer>> kitemset;
    private double minsup;
    private double minconf;
    private double nrTr;

    private BaseClass base;

    AprioriBasic(String file, double minsup, double minconf) {

        boolean isReduced = false;
        this.base = new BaseClass(file, minsup, minconf, isReduced);
        this.transactions = base.getTransactions();
        this.kitemset = base.getKitemset();
        this.minsup = base.getMinsup();
        this.minconf = base.getMinconf();
        this.nrTr = base.getNrTr();


        aprioriBasicAlgorithm();

        //System.out.println("Numar total iteratii: "+nrit);

    }

    private void aprioriBasicAlgorithm() {
        int k = 1;
        while (!kitemset.get(k).isEmpty()) {
            int nrit=0;
            //base.showKSet(k);
            Map<Set<String>, Integer> candMap = base.generateCand(k);
            if (candMap.isEmpty()) {
                k++;
                break;
            }
            for (Set transaction : transactions) {
                for (Set cset : candMap.keySet()) {
                    nrit++;
                    if (transaction.containsAll(cset))
                        candMap.put(cset, candMap.get(cset) + 1);
                }
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
}
