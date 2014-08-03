package demo.old;

import java.util.*;

/**
 * Created by ByjuV on 6/21/2014.
 */
interface Formula {
    double calculate(int a);
    double sqrt(int a);
}
 abstract class FormulaDefault implements Formula{
     @Override
     public double sqrt(int a) {
         return Math.sqrt(a);
     }
     static double getPi(){
         return 3.14;
     }
}
public class Examples_Old {
    public static void main(String[] args) {

        // Interfaces
        Formula formula = new FormulaDefault() {
            @Override
            public double calculate(int a) {
                return sqrt(a * 100);
            }

        };

        formula.calculate(100);     // 100.0
        formula.sqrt(16);
        FormulaDefault.getPi();

        // Lambda
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");

        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return b.compareTo(a);
            }
        });

        Calendar day = Calendar.getInstance();
        day.add(Calendar.MONTH,2);
        System.out.println(day);
    }
}
