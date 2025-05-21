package soc.ip;

import soc.util.Pair;

import java.util.*;

public class CoordBridge {

    public static int[] addV = {0x01, 0x12, 0x21, 0x10, -0x01, -0x10}; // vertex offsets for each tile


    public static HashMap<Pair<Integer, Integer>, Integer> backToAiCode = new HashMap<>();
    public static HashMap<Integer, Pair<Integer, Integer>> aiCodeToBack = new HashMap<>();

    static {
        add(0x37, -2, 2);
        add(0x59, -1, 2);
        add(0x7b, 0, 2);
        add(0x35, -2, -1);
        add(0x57, -1, 1);
        add(0x79, 0, 1);
        add(0x9b, 1, 1);
        add(0x33, -2, 0);
        add(0x55, -1, 0);
        add(0x77, 0, 0);
        add(0x99, 1, 0);
        add(0xbb, 2, 0);
        add(0x53, -1, -1);
        add(0x75, 0, -1);
        add(0x97, 1, -1);
        add(0xb9, 2, -1);
        add(0x73, 0, -2);
        add(0x95, 1, -2);
        add(0xb7, 2, -2);

    }

    private static void add(int code, int x, int y) {
        backToAiCode.put(new Pair<>(x, y), code);
        aiCodeToBack.put(code, new Pair<>(x, y));
    }

    /**
     *
     * @param code the code for an edge used internally
     * @return x y nr
     */

    public static String getVertex(int code) {
        for (Map.Entry<Integer,Pair<Integer,Integer>> e : aiCodeToBack.entrySet()) {
            int k = e.getKey();
            Pair<Integer,Integer> v = e.getValue();
            for (int i = 0; i < addV.length; i++) {
                if (k + addV[i] == code) {
                    return v.getA() + " " + v.getB() +" "+ i;
                }
            }
        }
        return "ERROR";
    }

}



