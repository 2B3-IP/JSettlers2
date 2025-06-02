package soc;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public final class UnityBridge {
    private static final String HOST = "localhost";
    private static final int PORT = 7070;

    private UnityBridge() {
    }

    public static boolean buy = false;

    public static void send(String msg) {
        System.out.println("SENT: " + msg);

        try (Socket s = new Socket(HOST, PORT);
             PrintWriter w = new PrintWriter(s.getOutputStream(), true)) {
            w.println(msg);
            buy = false;
//             Thread.sleep(1000);

        } catch (IOException e) {
            System.err.println("UnityBridge error: " + e);
        } catch (Exception e) {
            System.err.println("UnityBridge error: " + e);
        }
    }

    public static void sendBankTrade(int[] give, int[] recv) {
//        StringBuilder sb = new StringBuilder("BANKTRADE");
//        for (int r : give) sb.append(" ").append(r);
//        for (int r : recv) sb.append(" ").append(r);
//        send(sb.toString());
    }

    public static void sendBuildSettlement(int x, int y, int pos) {
        if (buy)
            send("BUY SETTLEMENT " + x + " " + y + " " + pos);
        else send("BUILD SETTLEMENT " + x + " " + y + " " + pos);

    }

    public static void sendBuildCity(int x, int y, int pos) {
        if (buy)
            send("BUY CITY " + x + " " + y + " " + pos);
        else send("BUILD CITY " + x + " " + y + " " + pos);
    }

    public static void sendBuildRoad(int x, int y, int edge) {
        if (buy)
            send("BUY ROAD " + x + " " + y + " " + edge);
        else send("BUILD ROAD " + x + " " + y + " " + edge);
    }

    public static void sendMoveRobber(int x, int y) {
        send("MOVEROBBER " + x + " " + y);
    }

    public static void sendDiceRoll(int dice) {
//        send("DICE_NUMBER " + dice);
    }

    public static void sendDiscard(int[] res) {
//        StringBuilder sb = new StringBuilder("DISCARD");
//        for (int r : res) sb.append(" ").append(r);
//        send(sb.toString());
    }

    public static void sendEndTurn() {
        send("ENDTURN");
    }

    public static void sendChoosePlayer(int p) {
//        send("CHOOSEPLAYER " + p);
    }

    public static void sendMakeOffer(int[] off) {
//        StringBuilder sb = new StringBuilder("MAKEOFFER");
//        for (int r : off) sb.append(" ").append(r);
//        send(sb.toString());
    }

    public static void sendClearOffer() {
//        send("CLEAROFFER");
    }

    public static void sendRejectOffer() {
//        send("REJECTOFFER");
    }

    public static void sendAcceptOffer() {
//        send("ACCEPTOFFER");
    }

    public static void sendBankTrade(int give, int recv) {
//        send("BANKTRADE " + give + " " + recv);
    }

    public static void sendBuildRequest(int type) {
//        send("BUILDREQUEST " + type);
    }

    public static void sendCancelBuildRequest() {
//        send("CANCELBUILDREQUEST");
    }

    public static void sendBuyDevCard() {
//        send("BUYDEVCARDREQUEST");
    }

    public static void sendPlayDevCard() {
//        send("PLAYDEVCARDREQUEST");
    }

    public static void sendPickResources(int[] p) {
//        StringBuilder sb = new StringBuilder("PICKRESOURCES");
//        for (int r : p) sb.append(" ").append(r);
//        send(sb.toString());
    }

    public static void sendPickResourceType(int t) {
//        send("PICKRESOURCETYPE " + t);
    }

    public static void sendDebugFreePlace(int x, int y) {
//        send("DEBUGFREEPLACE " + x + " " + y);
    }

    public static void sendSimpleRequest(int t) {
//        send("SIMPLEREQUEST " + t);
    }

    public static void sendMove(String message) {
        send(message);  // Refolosește metoda privată `send`
    }

    public static void sendInventoryAction(int id, boolean add) {
//        send("INVENTORYITEMACTION " + id + " " + (add ? "ADD" : "REMOVE"));
    }

    public static void sendMovePiece(int x, int y) {
        send("MOVEPIECE " + x + " " + y);
    }

    public static void sendSpecialItem(int id) {
//        send("SETSPECIALITEM " + id);
    }

    public static void sendGameStats() {
//        send("GAMESTATS");
    }

    public static void sendUndoPutPiece(int x, int y, int loc) {
        send("UNDOPUTPIECE " + x + " " + y + " " + loc);
    }
}
