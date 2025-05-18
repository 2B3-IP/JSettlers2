package soc.ip;

import soc.game.SOCPlayingPiece;
import soc.message.SOCPutPiece;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class LogHandler {

    private static final String JAVA_WRAPPER_SERVER_HOST = "127.0.0.1"; // JavaWrapperServer address
    private static final int JAVA_WRAPPER_SERVER_PORT = 6969; // JavaWrapperServer port

    public static void putpiece(final SOCPutPiece mes) {
        final int pieceType = mes.getPieceType();
        final int coord = mes.getCoordinates();

        String logMessage;
        if (mes.getPlayerNumber() == 0) { // only for the player
            logMessage = "CLIENT" + mes.getPlayerNumber() + " received " + SOCPlayingPiece.getTypeName(pieceType) + " " + coord;
        } else { // bot
            logMessage = "BOT" + mes.getPlayerNumber() + " sent " + SOCPlayingPiece.getTypeName(pieceType) + " " + coord;
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
