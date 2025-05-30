package soc.bridge;

import soc.game.ResourceSet;
import soc.game.SOCTradeOffer;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class UnityTradeBridge {

    private String host;
    private int port;

    public UnityTradeBridge(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void sendMessage(String message) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {
            out.println(message);
        } catch (Exception e) {
            System.err.println("UnityTradeBridge: Failed to send message - " + e.getMessage());
        }
    }

    public void notifyTradeOffer(int fromPlayer, SOCTradeOffer offer) {
        String msg = String.format("TRADE_OFFER from=%d give=%s get=%s",
                fromPlayer,
                offer.getGiveSet().toString(),
                offer.getGetSet().toString());
        sendMessage(msg);
    }

    public void notifyTradeAccepted(int fromPlayer, int toPlayer, ResourceSet give, ResourceSet get) {
        String msg = String.format("TRADE_ACCEPTED from=%d to=%d give=%s get=%s",
                fromPlayer,
                toPlayer,
                give.toString(),
                get.toString());
        sendMessage(msg);
    }
}
