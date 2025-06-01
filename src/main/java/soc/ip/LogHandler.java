package soc.ip;

import soc.UnityBridge;
import soc.game.SOCPlayingPiece;
import soc.message.SOCPutPiece;

public class LogHandler {
    private static String lastMessage = "";

    public static void putpiece(final SOCPutPiece mes) {
        final int pieceType = mes.getPieceType();
        final int coord = mes.getCoordinates();

        String logMessage = (mes.getPlayerNumber() == 0 ? "CLIENT" : "BOT" + mes.getPlayerNumber()) + " sent ";

        String actionMessage;

        switch (SOCPlayingPiece.getTypeName(pieceType)) {
            case "SETTLEMENT":
                actionMessage = "SETTLEMET " + CoordBridge.getVertex(coord);
                break;
            case "ROAD":
                actionMessage = "ROAD " + CoordBridge.getEdge(coord);
                break;
            case "CITY":
                actionMessage = "CITY " + CoordBridge.getVertex(coord);
                break;
            default:
                actionMessage = "UNKNOWN";
        }

        logMessage += actionMessage;
        System.out.println(logMessage);


        try {
            if(mes.getPlayerNumber() != 0) // is bot
                 UnityBridge.sendMove(actionMessage);
        } catch (Exception e) {
            System.err.println("? Unity connection failed: " + e.getMessage());
        }
    }
}
