package com.cs321.team1.util;

import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.objects.crates.Crate;
import com.cs321.team1.framework.objects.crates.DivideCrate;
import com.cs321.team1.framework.objects.crates.IntegerCrate;
import com.cs321.team1.framework.objects.crates.ModuloCrate;
import com.cs321.team1.framework.objects.crates.MultiplyCrate;
import com.cs321.team1.framework.objects.crates.NegateCrate;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeMap;

public class CrateChecker {
    private static boolean bottomed;
    private static int reached;
    
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var crates = new ArrayList<Crate>();
        var scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Current List:");
            crates.forEach(it -> System.out.println(it.getClass().getSimpleName() + ":" + it.getValue()));
            System.out.println(
                    "\nSelect Crate:\n1 -> IntegerCrate\n2 -> ModuloCrate\n3 -> NegateCrate\n4 -> ScaleUpCrate\n5 -> ScaleDownCrate\n6 -> Calculate Possibilities\n7 -> Calculate Complexity\n");
            int input = scanner.nextInt();
            if (input > 0 && input < 6 || input == 7) {
                if (input == 3) crates.add(new NegateCrate(null, null));
                else {
                    System.out.print("Select Value: ");
                    int input2 = scanner.nextInt();
                    switch (input) {
                        case 1 -> crates.add(new IntegerCrate(null, null, input2));
                        case 2 -> crates.add(new ModuloCrate(null, null, input2));
                        case 5 -> crates.add(new DivideCrate(null, null, input2));
                        case 4 -> crates.add(new MultiplyCrate(null, null, input2));
                        case 7 -> {
                            var total = new BigDecimal(1);
                            for (int i = 3; i <= input2; i++) {
                                var total2 = new BigDecimal(0);
                                for (int j = 1; j < i; j++) {
                                    total2 = total2.add(new BigDecimal(j));
                                }
                                total = total.multiply(total2);
                            }
                            var format = new DecimalFormat("0E0");
                            format.setParseBigDecimal(true);
                            System.out.println("Complexity: " + format.format(total));
                            return;
                        }
                    }
                }
            } else if (input == 6) {
                break;
            }
            for (int i = 0; i < 100; i++) System.out.println();
        }
        var list = scanCrates(crates);
        var set = new HashSet<>(list);
        var map = new TreeMap<Integer, Integer>();
        set.forEach(i -> map.put(i, (int) list.stream().filter(it -> Objects.equals(it, i)).count()));
        System.out.println();
        map.forEach((i, count) -> {
            if (i == Integer.MIN_VALUE) System.out.println(". \t:" + count);
            else System.out.println(i + "\t:" + count);
        });
    }
    
    private static List<Integer> scanCrates(List<Crate> list) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (list.size() == 1) {
            if (!bottomed) {
                bottomed = true;
                reached = list.size();
                System.out.print("..");
            }
            return Collections.singletonList(list.get(0).getValue());
        } else if (list.size() == 2 && !list.get(0).canInteractWith(list.get(1))) {
            if (!bottomed) {
                bottomed = true;
                reached = list.size();
                System.out.print("..");
            }
            return Collections.singletonList(Integer.MIN_VALUE);
        } else {
            if (bottomed && list.size() == reached + 1) {
                reached = list.size();
                System.out.print(".");
            }
            var results = new ArrayList<Integer>();
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    var crate1 = list.get(i);
                    var crate2 = list.get(j);
                    if (crate1.canInteractWith(crate2)) {
                        var newList = new ArrayList<>(list);
                        newList.remove(crate1);
                        newList.remove(crate2);
                        newList.add(crate2.getClass()
                                .getDeclaredConstructor(Level.class, Location.class, int.class)
                                .newInstance(null, null, crate1.getMergedValue(crate2)));
                        results.addAll(scanCrates(newList));
                    } else if (crate2.canInteractWith(crate1)) {
                        var newList = new ArrayList<>(list);
                        newList.remove(crate1);
                        newList.remove(crate2);
                        newList.add(crate1.getClass()
                                .getDeclaredConstructor(Level.class, Location.class, int.class)
                                .newInstance(null, null, crate2.getMergedValue(crate1)));
                        results.addAll(scanCrates(newList));
                    }
                }
            }
            return results;
        }
    }
}
