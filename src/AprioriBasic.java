import javax.sound.midi.SysexMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class AprioriBasic {
    private List<Set<String>> transactions = new ArrayList<>();

    private Map<String, Integer> fkitmes = new HashMap<>();

    private Map<Integer, Map<Set<String>, Integer>> kitemset = new HashMap<>();

    private double minsup;

    private double minconf;

    double nrTr;

    public AprioriBasic(String file, double minsup, double minconf) {
        int k = 1;
        this.minsup = minsup;
        this.minconf = minconf;
        readInput(file);

        initItemSet();

        while (!kitemset.get(k).isEmpty()) {
            showKSet(k);
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
            for (Set set : transactions) {
                for (Set cset : candMap.keySet())
                    if (set.containsAll(cset))
                        candMap.put(cset, candMap.get(cset) + 1);
            }
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
        k--;
        //confidence
        for (Set set : kitemset.get(k).keySet()) {
            List<Set<String>> subsets = printSubsets(set);
            System.out.println(set);
            for (Set<String> subset : subsets) {
                double nr = kitemset.get(subset.size()).get(subset);
                double conf = kitemset.get(k).get(set) / nr;
                if (conf > 0.99) {
                    Set dset = new HashSet(set);
                    dset.removeAll(subset);
                    System.out.println(subset + "-->" + dset + "---" + conf);
                }
            }
        }
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
        //for each transaction
        for (Set set : transactions) {
            List<String> list = new ArrayList<String>(set);
            for (String item : list) {
                fkitmes.put(item, fkitmes.get(item) + 1);
            }
        }
        //add to kitemset
        Map<Set<String>, Integer> fmap = new HashMap<>();
        for (String item : fkitmes.keySet()) {
            double sup = fkitmes.get(item) / nrTr;
            if (sup >= minsup) {
                Set<String> fitems = new HashSet<>();
                fitems.add(item);
                fmap.put(fitems, fkitmes.get(item));
                //System.out.println(item + "-" + fkitmes.get(item) + "-" + sup);
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

    public List<Set<String>> printSubsets(Set<String> myset) {

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
