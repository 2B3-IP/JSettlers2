package soc.ip;

import soc.game.SOCPlayingPiece;
import soc.message.SOCPutPiece;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

public class LogHandler {

    private static final String JAVA_WRAPPER_SERVER_HOST = "127.0.0.1";
    private static final int JAVA_WRAPPER_SERVER_PORT = 6969;

    private static String lastMessageSent = null;
    private static boolean errorShown = false;

    public static void putpiece(final SOCPutPiece mes) {
        final int pieceType = mes.getPieceType();
        final int coord = mes.getCoordinates();

        String posStr = "(unknown)";

        List<CoordBridge.HexPosition> edgeHexes = CoordBridge.getEdgeHexes(coord);
        List<CoordBridge.HexPosition> nodeHexes = CoordBridge.getNodeHexes(coord);
        CoordBridge.HexPosition hex = CoordBridge.getHexPosition(coord);

        if (hex != null) {
            posStr = hex.toString();
        } else if (edgeHexes != null && !edgeHexes.isEmpty()) {
            posStr = "edge between " + edgeHexes;
        } else if (nodeHexes != null && !nodeHexes.isEmpty()) {
            posStr = "node adjacent to " + nodeHexes;
        }

        String logMessage = (mes.getPlayerNumber() == 0)
                ? "CLIENT" + mes.getPlayerNumber() + " received " +
                SOCPlayingPiece.getTypeName(pieceType) + " at " + posStr + " [" + coord + "]"
                : "BOT" + mes.getPlayerNumber() + " sent " +
                SOCPlayingPiece.getTypeName(pieceType) + " at " + posStr + " [" + coord + "]";

        if (Objects.equals(logMessage, lastMessageSent)) {
            return;
        }
        lastMessageSent = logMessage;

        System.out.println(logMessage);

        try (Socket socket = new Socket(JAVA_WRAPPER_SERVER_HOST, JAVA_WRAPPER_SERVER_PORT);
             PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true)) {

            out.println(logMessage);
            System.out.println("Sent to JavaWrapperServer: " + logMessage);
            errorShown = false;

        } catch (Exception e) {
            if (!errorShown) {
                System.err.println("Error communicating with JavaWrapperServer: " + e.getMessage());
                errorShown = true;
            }
        }
    }
}
