import java.util.*;

public class Main {


    public static void main(String[] args) {
        Integer l1[] = {1, 2, 3, 5};
        Set<Integer> s1 = new HashSet<>(Arrays.asList(l1));
        Integer l2[] = {1, 2, 3, 4};
        Set<Integer> s2 = new HashSet<>(Arrays.asList(l2));
        Set<Integer> s3 = new HashSet<>();
        s3.addAll(s1);
        s3.addAll(s2);

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        Set<String> set = new HashSet<>();
        set.add("1");
        set.add("2");
        set.add("3");
        AprioriBasic aprioriBasic = new AprioriBasic("E:\\facultate\\a1s1\\datamining\\apriori\\src\\test", 0.95,98);

    }
}
