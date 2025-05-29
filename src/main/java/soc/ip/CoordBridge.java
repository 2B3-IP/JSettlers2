package soc.ip;

import java.util.*;

class Point<A, B> {
    private A a;
    private B b;

    public Point(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() { return a; }
    public B getB() { return b; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point<?, ?> point = (Point<?, ?>) o;
        return Objects.equals(a, point.a) && Objects.equals(b, point.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}

public class CoordBridge {

    public static int[] addV = {0x01, 0x12, 0x21, 0x10, -0x01, -0x10};

    public static HashMap<Point<Integer, Integer>, Integer> backToAiCode = new HashMap<>();
    public static HashMap<Integer, Point<Integer, Integer>> aiCodeToBack = new HashMap<>();
    private static final Map<Integer, String> edgeToCoords = new HashMap<>();
    private static final Map<String, Integer> coordsToEdge = new HashMap<>();
    private static final Map<Integer, String> vertexToCoords = new HashMap<>();
    private static final Map<String, Integer> coordsToVertex = new HashMap<>();

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

    private static final List<String> allVertexCoords = new ArrayList<>();
    private static final int[] unityToJavaDir = {1, 2, 3, 4, 5, 0};

    static {
        for (int i = 0; i < HEXES.length; i++) {
            int x = HEXES[i][0];
            int y = HEXES[i][1];
            int base = baseCodes[i];

            Point<Integer, Integer> pos = new Point<>(x, y);
            backToAiCode.put(pos, base);
            aiCodeToBack.put(base, pos);
        }

        addEdge(0x38, -2, 2, 0);
        addEdge(0x48, -2, 2, 1);
        addEdge(0x47, -2, 2, 2);
        addEdge(0x36, -2, 2, 3);
        addEdge(0x26, -2, 2, 4);
        addEdge(0x27, -2, 2, 5);

        addEdge(0x5A, -1, 2, 0);
        addEdge(0x6A, -1, 2, 1);
        addEdge(0x69, -1, 2, 2);
        addEdge(0x58, -1, 2, 3);
        addEdge(0x48, -1, 2, 4);
        addEdge(0x49, -1, 2, 5);

        addEdge(0x7C, 0, 2, 0);
        addEdge(0x8C, 0, 2, 1);
        addEdge(0x8B, 0, 2, 2);
        addEdge(0x7A, 0, 2, 3);
        addEdge(0x6A, 0, 2, 4);
        addEdge(0x6B, 0, 2, 5);

        addEdge(0x36, -2, 1, 0);
        addEdge(0x46, -2, 1, 1);
        addEdge(0x45, -2, 1, 2);
        addEdge(0x43, -2, 1, 3);
        addEdge(0x24, -2, 1, 4);
        addEdge(0x25, -2, 1, 5);

        addEdge(0x58, -1, 1, 0);
        addEdge(0x68, -1, 1, 1);
        addEdge(0x67, -1, 1, 2);
        addEdge(0x56, -1, 1, 3);
        addEdge(0x46, -1, 1, 4);
        addEdge(0x47, -1, 1, 5);

        addEdge(0x7A, 0, 1, 0);
        addEdge(0x8A, 0, 1, 1);
        addEdge(0x89, 0, 1, 2);
        addEdge(0x78, 0, 1, 3);
        addEdge(0x68, 0, 1, 4);
        addEdge(0x69, 0, 1, 5);

        addEdge(0x9C, 1, 1, 0);
        addEdge(0xAC, 1, 1, 1);
        addEdge(0xAB, 1, 1, 2);
        addEdge(0x9A, 1, 1, 3);
        addEdge(0x8A, 1, 1, 4);
        addEdge(0x8B, 1, 1, 5);

        addEdge(0x34, -2, 0, 0);
        addEdge(0x44, -2, 0, 1);
        addEdge(0x43, -2, 0, 2);
        addEdge(0x32, -2, 0, 3);
        addEdge(0x22, -2, 0, 4);
        addEdge(0x23, -2, 0, 5);

        addEdge(0x56, -1, 0, 0);
        addEdge(0x66, -1, 0, 1);
        addEdge(0x65, -1, 0, 2);
        addEdge(0x54, -1, 0, 3);
        addEdge(0x44, -1, 0, 4);
        addEdge(0x45, -1, 0, 5);

        addEdge(0x77, 0, 0, 0);
        addEdge(0x87, 0, 0, 1);
        addEdge(0x86, 0, 0, 2);
        addEdge(0x75, 0, 0, 3);
        addEdge(0x65, 0, 0, 4);
        addEdge(0x66, 0, 0, 5);

        addEdge(0x9A, 1, 0, 0);
        addEdge(0xAA, 1, 0, 1);
        addEdge(0xA9, 1, 0, 2);
        addEdge(0x97, 1, 0, 3);
        addEdge(0x88, 1, 0, 4);
        addEdge(0x89, 1, 0, 5);

        addEdge(0xBD, 2, 0, 0);
        addEdge(0xCC, 2, 0, 1);
        addEdge(0xCB, 2, 0, 2);
        addEdge(0xBA, 2, 0, 3);
        addEdge(0xAA, 2, 0, 4);
        addEdge(0xAB, 2, 0, 5);

        addEdge(0x54, -1, -1, 0);
        addEdge(0x64, -1, -1, 1);
        addEdge(0x63, -1, -1, 2);
        addEdge(0x52, -1, -1, 3);
        addEdge(0x42, -1, -1, 4);
        addEdge(0x43, -1, -1, 5);

        addEdge(0x76, 0, -1, 0);
        addEdge(0x86, 0, -1, 1);
        addEdge(0x85, 0, -1, 2);
        addEdge(0x74, 0, -1, 3);
        addEdge(0x64, 0, -1, 4);
        addEdge(0x65, 0, -1, 5);

        addEdge(0x98, 1, -1, 0);
        addEdge(0xA8, 1, -1, 1);
        addEdge(0xA7, 1, -1, 2);
        addEdge(0x96, 1, -1, 3);
        addEdge(0x86, 1, -1, 4);
        addEdge(0x87, 1, -1, 5);

        addEdge(0xBA, 2, -1, 0);
        addEdge(0xCA, 2, -1, 1);
        addEdge(0xC9, 2, -1, 2);
        addEdge(0xB8, 2, -1, 3);
        addEdge(0xA8, 2, -1, 4);
        addEdge(0xA9, 2, -1, 5);

        addEdge(0x74, 0, -2, 0);
        addEdge(0x84, 0, -2, 1);
        addEdge(0x83, 0, -2, 2);
        addEdge(0x72, 0, -2, 3);
        addEdge(0x62, 0, -2, 4);
        addEdge(0x63, 0, -2, 5);

        addEdge(0x96, 1, -2, 0);
        addEdge(0xA6, 1, -2, 1);
        addEdge(0xA5, 1, -2, 2);
        addEdge(0x94, 1, -2, 3);
        addEdge(0x84, 1, -2, 4);
        addEdge(0x85, 1, -2, 5);

        addEdge(0xB8, 2, -2, 0);
        addEdge(0xC8, 2, -2, 1);
        addEdge(0xC7, 2, -2, 2);
        addEdge(0xB6, 2, -2, 3);
        addEdge(0xA6, 2, -2, 4);
        addEdge(0xA7, 2, -2, 5);

        // Adăugare toate nodurile (vertexuri) generate automat
        for (int[] hex : HEXES) {
            int x = hex[0];
            int y = hex[1];

            for (int pos = 0; pos < 6; pos++) {
                int javaDir = unityToJavaDir[pos];
                int code = getVertex(x, y, javaDir);  // înregistrează codul
                String coord = x + " " + y + " " + javaDir;
                allVertexCoords.add(coord);
            }
        }
    }

    private static void addEdge(int code, int x, int y, int dir) {
        String key = x + " " + y + " " + dir;
        edgeToCoords.put(code, key);
        coordsToEdge.put(key, code);
    }

    private static void addVertex(int code, int x, int y, int dir) {
        String key = x + " " + y + " " + dir;
        vertexToCoords.put(code, key);
        coordsToVertex.put(key, code);
    }

    public static Integer getEdgeCodeFromCoords(int x, int y, int dir) {
        String key = x + " " + y + " " + dir;
        return coordsToEdge.getOrDefault(key, -1);
    }

    public static Integer getVertexCodeFromCoords(int x, int y, int dir) {
        String key = x + " " + y + " " + dir;
        return coordsToVertex.getOrDefault(key, -1);
    }

    public static int getVertex(int x, int y, int d) {
        Integer base = backToAiCode.get(new Point<>(x, y));
        if (base == null) return -1;
        int code = base + addV[d];
        addVertex(code, x, y, d);
        return code;
    }

    public static String getVertex(int code) {
        return vertexToCoords.getOrDefault(code, "ERROR");
    }

    public static String getEdge(int code) {
        String value = edgeToCoords.get(code);
        if (value == null) {
            System.err.println("getEdge(" + code + ") not found");
            return "ERROR";
        }
        System.out.println("getEdge(" + code + ") => " + value);
        return value;
    }


    public static Map<Integer, String> getEdgeToCoords() {
        return edgeToCoords;
    }

    public static Map<String, Integer> getCoordsToEdge() {
        return coordsToEdge;
    }

    public static Map<Integer, String> getVertexToCoords() {
        return vertexToCoords;
    }

    public static Map<String, Integer> getCoordsToVertex() {
        return coordsToVertex;
    }

    public static List<String> getAllVertexCoords() {
        return allVertexCoords;
    }
}
