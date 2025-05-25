package soc.ip;

import soc.util.Pair;

import java.util.*;

public class CoordBridge {

    public static int[] addV = {0x01, 0x12, 0x21, 0x10, -0x01, -0x10};

    public static HashMap<Pair<Integer, Integer>, Integer> backToAiCode = new HashMap<>();
    public static HashMap<Integer, Pair<Integer, Integer>> aiCodeToBack = new HashMap<>();
    private static final Map<Integer, String> edgeToCoords = new HashMap<>();

    private static final int[][] HEXES = {
            {-2, 2}, {-1, 2}, {0, 2},
            {-2, 1}, {-1, 1}, {0, 1}, {1, 1},
            {-2, 0}, {-1, 0}, {0, 0}, {1, 0}, {2, 0},
            {-1, -1}, {0, -1}, {1, -1}, {2, -1},
            {0, -2}, {1, -2}, {2, -2}
    };

    private static final int[] baseCodes = {
            0x37, 0x59, 0x7B,
            0x35, 0x57, 0x79, 0x9B,
            0x33, 0x55, 0x77, 0x99, 0xBB,
            0x53, 0x75, 0x97, 0xB9,
            0x73, 0x95, 0xB7
    };

    static {
        for (int i = 0; i < HEXES.length; i++) {
            int x = HEXES[i][0];
            int y = HEXES[i][1];
            int base = baseCodes[i];

            Pair<Integer, Integer> pos = new Pair<>(x, y);
            backToAiCode.put(pos, base);
            aiCodeToBack.put(base, pos);

            for (int d = 0; d < 6; d++) {
                int edgeCode = base + addV[d];
                String val = x + " " + y + " " + d;

                edgeToCoords.put(edgeCode, val);
                edgeToCoords.put(edgeCode + 1, val);  // fallback offset
                edgeToCoords.put(edgeCode - 1, val);

                // Reverse edge
                int nx = x + directionDx(d);
                int ny = y + directionDy(d);
                int revCode = base - addV[d];
                String revVal = nx + " " + ny + " " + ((d + 3) % 6);

                edgeToCoords.putIfAbsent(revCode, revVal);
                edgeToCoords.putIfAbsent(revCode + 1, revVal);
                edgeToCoords.putIfAbsent(revCode - 1, revVal);
            }
        }
    }

    public static String getVertex(int code) {
        for (Map.Entry<Integer, Pair<Integer, Integer>> e : aiCodeToBack.entrySet()) {
            int base = e.getKey();
            Pair<Integer, Integer> pos = e.getValue();
            for (int i = 0; i < addV.length; i++) {
                if (base + addV[i] == code) {
                    return pos.getA() + " " + pos.getB() + " " + i;
                }
            }
        }
        return "ERROR";
    }

    public static String getEdge(int code) {
        return edgeToCoords.getOrDefault(code, "ERROR");
    }

    private static int directionDx(int d) {
        switch (d) {
            case 0:
            case 3: return 0;
            case 1:
            case 2: return 1;
            case 4:
            case 5: return -1;
            default: return 0;
        }
    }

    private static int directionDy(int d) {
        switch (d) {
            case 0:
            case 1: return -1;
            case 2:
            case 5: return 0;
            case 3:
            case 4: return 1;
            default: return 0;
        }
    }
}
