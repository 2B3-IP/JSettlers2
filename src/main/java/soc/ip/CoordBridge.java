package soc.ip;

import java.util.*;

public class CoordBridge {

    public static class HexPosition {
        public final int x;
        public final int y;

        public HexPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof HexPosition)) return false;
            HexPosition other = (HexPosition) obj;
            return this.x == other.x && this.y == other.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    private static final Map<Integer, HexPosition> hexes = new HashMap<>();
    private static final Map<Integer, List<HexPosition>> nodes = new HashMap<>();
    private static final Map<Integer, List<HexPosition>> edges = new HashMap<>();
    private static final List<HexPosition> ports = new ArrayList<>();
    private static final List<HexPosition> tradeLocations = new ArrayList<>();

    static {
        // === HEXES ===
        hexes.put(0x33, new HexPosition(0, -2));
        hexes.put(0x35, new HexPosition(1, -2));
        hexes.put(0x37, new HexPosition(2, -2));
        hexes.put(0x39, new HexPosition(-1, -1));
        hexes.put(0x3B, new HexPosition(0, -1));
        hexes.put(0x3D, new HexPosition(1, -1));
        hexes.put(0x3F, new HexPosition(2, -1));
        hexes.put(0x41, new HexPosition(-2, 0));
        hexes.put(0x43, new HexPosition(-1, 0));
        hexes.put(0x45, new HexPosition(0, 0));
        hexes.put(0x47, new HexPosition(1, 0));
        hexes.put(0x49, new HexPosition(2, 0));
        hexes.put(0x4B, new HexPosition(-2, 1));
        hexes.put(0x4D, new HexPosition(-1, 1));
        hexes.put(0x4F, new HexPosition(0, 1));
        hexes.put(0x51, new HexPosition(1, 1));
        hexes.put(0x53, new HexPosition(-2, 2));
        hexes.put(0x55, new HexPosition(-1, 2));
        hexes.put(0x57, new HexPosition(0, 2));

        // === PORTS ===
        ports.add(new HexPosition(0, -3));
        ports.add(new HexPosition(2, -3));
        ports.add(new HexPosition(3, 0));
        ports.add(new HexPosition(-1, 3));

        // === TRADE LOCATIONS ===
        tradeLocations.add(new HexPosition(-2, -1));
        tradeLocations.add(new HexPosition(3, -2));
        tradeLocations.add(new HexPosition(-3, 1));
        tradeLocations.add(new HexPosition(1, 2));
        tradeLocations.add(new HexPosition(-3, 3));

        edges.put(34, Arrays.asList(new HexPosition(-2, 0), new HexPosition(99, 99)));
        edges.put(36, Arrays.asList(new HexPosition(-1, -1), new HexPosition(99, 99)));
        edges.put(37, Arrays.asList(new HexPosition(-1, -1), new HexPosition(99, 99)));
        edges.put(39, Arrays.asList(new HexPosition(0, -2), new HexPosition(0, -3)));
        edges.put(50, Arrays.asList(new HexPosition(-2, 0), new HexPosition(99, 99)));
        edges.put(54, Arrays.asList(new HexPosition(0, -2), new HexPosition(-1, -1)));
        edges.put(56, Arrays.asList(new HexPosition(0, -2), new HexPosition(99, 99)));
        edges.put(66, Arrays.asList(new HexPosition(-2, 1), new HexPosition(99, 99)));
        edges.put(67, Arrays.asList(new HexPosition(-2, 0), new HexPosition(-2, 1)));
        edges.put(72, Arrays.asList(new HexPosition(0, -2), new HexPosition(1, -2)));
        edges.put(73, Arrays.asList(new HexPosition(1, -2), new HexPosition(99, 99)));
        edges.put(82, Arrays.asList(new HexPosition(-2, 1), new HexPosition(99, 99)));
        edges.put(84, Arrays.asList(new HexPosition(-1, 0), new HexPosition(-2, 1)));
        edges.put(86, Arrays.asList(new HexPosition(-1, 0), new HexPosition(0, -1)));
        edges.put(88, Arrays.asList(new HexPosition(0, -1), new HexPosition(1, -2)));
        edges.put(90, Arrays.asList(new HexPosition(1, -2), new HexPosition(2, -3)));
        edges.put(98, Arrays.asList(new HexPosition(-2, 2), new HexPosition(99, 99)));
        edges.put(99, Arrays.asList(new HexPosition(-2, 2), new HexPosition(-2, 1)));
        edges.put(100, Arrays.asList(new HexPosition(-1, 1), new HexPosition(-2, 1)));
        edges.put(101, Arrays.asList(new HexPosition(-1, 0), new HexPosition(-1, 1)));
        edges.put(102, Arrays.asList(new HexPosition(0, 0), new HexPosition(-1, 0)));
        edges.put(103, Arrays.asList(new HexPosition(0, 0), new HexPosition(0, -1)));
        edges.put(104, Arrays.asList(new HexPosition(0, -1), new HexPosition(1, -1)));
        edges.put(105, Arrays.asList(new HexPosition(1, 0), new HexPosition(2, 0)));
        edges.put(106, Arrays.asList(new HexPosition(1, -2), new HexPosition(2, -2)));
        edges.put(107, Arrays.asList(new HexPosition(2, -2), new HexPosition(2, -3)));
        edges.put(114, Arrays.asList(new HexPosition(-2,2), new HexPosition(-3,3)));
        edges.put(116, Arrays.asList(new HexPosition(-1, 1), new HexPosition(-2, 2)));
        edges.put(118, Arrays.asList(new HexPosition(0,0), new HexPosition(-1,1)));
        edges.put(120, Arrays.asList(new HexPosition(0, 0), new HexPosition(1, -1)));
        edges.put(122, Arrays.asList(new HexPosition(1, -1), new HexPosition(2, -2)));
        edges.put(124, Arrays.asList(new HexPosition(2, -2), new HexPosition(99, 99)));
        edges.put(131, Arrays.asList(new HexPosition(-2, 2), new HexPosition(99, 99)));
        edges.put(132, Arrays.asList(new HexPosition(-2, 2), new HexPosition(-1, 2)));
        edges.put(134, Arrays.asList(new HexPosition(-1, 1), new HexPosition(0, 1)));
        edges.put(135, Arrays.asList(new HexPosition(0,0), new HexPosition(0, 1)));
        edges.put(136, Arrays.asList(new HexPosition(0,0), new HexPosition(1,0)));
        edges.put(139, Arrays.asList(new HexPosition(2, -2), new HexPosition(2, -1)));
        edges.put(148, Arrays.asList(new HexPosition(-2, 0), new HexPosition(-2, 1)));
        edges.put(150, Arrays.asList(new HexPosition(-1, 2), new HexPosition(0, 1)));
        edges.put(152, Arrays.asList(new HexPosition(1, 0), new HexPosition(0, 1)));
        edges.put(154, Arrays.asList(new HexPosition(2, -1), new HexPosition(1, 0)));
        edges.put(156, Arrays.asList(new HexPosition(2, -1), new HexPosition(99, 99)));
        edges.put(165, Arrays.asList(new HexPosition(-1, 2), new HexPosition(99, 99)));
        edges.put(166, Arrays.asList(new HexPosition(-1, 2), new HexPosition(0, 2)));
        edges.put(167, Arrays.asList(new HexPosition(0, 1), new HexPosition(0, 2)));
        edges.put(168, Arrays.asList(new HexPosition(0, 1), new HexPosition(1, 1)));
        edges.put(169, Arrays.asList(new HexPosition(1, 0), new HexPosition(1, -1)));
        edges.put(171, Arrays.asList(new HexPosition(2,-1), new HexPosition(2,0)));
        edges.put(172, Arrays.asList(new HexPosition(2, -1), new HexPosition(99, 99)));
        edges.put(182, Arrays.asList(new HexPosition(0, 2), new HexPosition(99, 99)));
        edges.put(184, Arrays.asList(new HexPosition(1, 1), new HexPosition(0, 2)));
        edges.put(186, Arrays.asList(new HexPosition(2, 0), new HexPosition(1, 1)));
        edges.put(188, Arrays.asList(new HexPosition(2, 0), new HexPosition(99, 99)));
        edges.put(199, Arrays.asList(new HexPosition(0, 2), new HexPosition(99, 99)));
        edges.put(202, Arrays.asList(new HexPosition(1,1), new HexPosition(99,99)));
        edges.put(204, Arrays.asList(new HexPosition(2, 0), new HexPosition(3, 0)));
    }
    public static HexPosition getHexPosition(int encoded) {
        return hexes.get(encoded);
    }

    public static List<HexPosition> getNodeHexes(int nodeId) {
        return nodes.get(nodeId);
    }

    public static List<HexPosition> getEdgeHexes(int edgeId) {
        return edges.get(edgeId);
    }

    public static List<HexPosition> getPorts() {
        return ports;
    }

    public static List<HexPosition> getTradeLocations() {
        return tradeLocations;
    }
}




