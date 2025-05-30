package soc.ip;

import soc.game.SOCPlayingPiece;
import soc.message.SOCPutPiece;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class LogHandler {
    public static void putpiece(final SOCPutPiece mes) {
        final int pieceType = mes.getPieceType();
        final int coord = mes.getCoordinates();
//trb sa citesc mesage ul cu unity bridge (sa ii trimit lui), ii dai lui parametri
        String logMessage = mes.getPlayerNumber() == 0
                ? "CLIENT" + mes.getPlayerNumber() + " sent "
                : "BOT" + mes.getPlayerNumber() + " sent ";

        switch (SOCPlayingPiece.getTypeName(pieceType)) {
            case "SETTLEMENT":
                logMessage += "SETTLEMET " + CoordBridge.getVertex(coord);
                break;
            case "ROAD":
                logMessage += "ROAD " + CoordBridge.getEdge(coord);
                break;
            default:
                logMessage += "UNKNOWN";
        }

        System.out.println(logMessage);
//unde trimit move ul/socplayingpice in loc de unde il primesc,in put piece am informatile,log handler ul sa fie chemat
    }
}
