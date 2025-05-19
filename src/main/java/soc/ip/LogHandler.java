package soc.ip;

import soc.game.SOCPlayingPiece;
import soc.message.SOCPutPiece;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import soc.ip.CoordBridge;

public class LogHandler {

    private static final String JAVA_WRAPPER_SERVER_HOST = "127.0.0.1"; // JavaWrapperServer address
    private static final int JAVA_WRAPPER_SERVER_PORT = 6969; // JavaWrapperServer port

    public static void putpiece(final SOCPutPiece mes) {
        final int pieceType = mes.getPieceType();
        final int coord = mes.getCoordinates();

        String logMessage = mes.getPlayerNumber() == 0 ? "CLIENT" + mes.getPlayerNumber() + " sent " : "BOT" + mes.getPlayerNumber() + " sent ";
        switch (SOCPlayingPiece.getTypeName(pieceType)){

            case "SETTLEMENT" : {
                logMessage += "SETTLEMET " + CoordBridge.getVertex(coord) ;
                break;
            }

        }

        System.out.println(logMessage);

        // Send the log message to the JavaWrapperServer
        try (Socket socket = new Socket(JAVA_WRAPPER_SERVER_HOST, JAVA_WRAPPER_SERVER_PORT);
             PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true)) {

            out.println(logMessage); // Send the log message
            System.out.println("Sent to JavaWrapperServer: " + logMessage);

        } catch (Exception e) {
            System.err.println("Error communicating with JavaWrapperServer: " + e.getMessage());
        }
    }
}
