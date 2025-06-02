package soc.ip;

import soc.game.SOCPlayingPiece;
import soc.message.SOCPutPiece;
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * LogHandler primește mesaje de tip SOCPutPiece (piese plasate în JSettlers)
 * și retransmite comanda către JavaWrapperServer (care, la rândul său, forwardează către Unity).
 */
public class LogHandler {

<<<<<<< Updated upstream
    private static final String JAVA_WRAPPER_SERVER_HOST = "127.0.0.1";
    private static final int JAVA_WRAPPER_SERVER_PORT = 6969;
=======
    // Host şi port unde ascultă „JavaWrapperServer” (care trimite mai departe către Unity)
    private static final String JAVA_WRAPPER_SERVER_HOST = "127.0.0.1";
    private static final int JAVA_WRAPPER_SERVER_PORT = 7070;
>>>>>>> Stashed changes

    /**
     * Apelează această metodă de fiecare dată când se primește mesaj SOCPutPiece.
     * Extrage tipul piesei şi coordonatele (x, y, d) și trimite către JavaWrapperServer.
     */
    public static void putpiece(final SOCPutPiece mes) {
        int pieceType = mes.getPieceType();
        int coord      = mes.getCoordinates();
        int playerNum  = mes.getPlayerNumber();

<<<<<<< Updated upstream
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

=======
        // Prefix „CLIENT” sau „BOT” în funcție de playerNumber
        String who = (playerNum == 0) ? "CLIENT0" : "BOT" + playerNum;

        String actionMessage;
        switch (SOCPlayingPiece.getTypeName(pieceType)) {
            case "SETTLEMENT":
                // Obține „x y d” din coord (cod AI) via CoordBridge
                actionMessage = "SETTLEMENT " + CoordBridge.getVertex(coord);
                break;

            case "CITY":
                actionMessage = "CITY " + CoordBridge.getVertex(coord);
                break;

            case "ROAD":
                actionMessage = "ROAD " + CoordBridge.getEdge(coord);
                break;

            default:
                actionMessage = "UNKNOWN";
                break;
        }

        // Construim linia completă de log, ex: „CLIENT0 sent SETTLEMENT 1 2 3”
        String logMessage = who + " sent " + actionMessage;
        System.out.println(logMessage);

        // Transmitere către JavaWrapperServer pe socket
        try (Socket socket = new Socket(JAVA_WRAPPER_SERVER_HOST, JAVA_WRAPPER_SERVER_PORT);
             PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true)) {

            out.println(logMessage);
            System.out.println("Sent to JavaWrapperServer: " + logMessage);

        } catch (Exception e) {
            System.err.println("Error communicating with JavaWrapperServer: " + e.getMessage());
        }
    }

    /**
     * Apelează această metodă atunci când se mută tâlharul (robber).
     * Parametrii x, y provin din codul AI (de exemplu, coordonate pe hartă).
     */
    public static void moveRobber(int playerNumber, int x, int y) {
        String who = (playerNumber == 0) ? "CLIENT0" : "BOT" + playerNumber;
        String logMessage = who + " sent ROBBER " + x + " " + y;
>>>>>>> Stashed changes
        System.out.println(logMessage);

        // Send the log message to the JavaWrapperServer
        try (Socket socket = new Socket(JAVA_WRAPPER_SERVER_HOST, JAVA_WRAPPER_SERVER_PORT);
             PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true)) {

            out.println(logMessage);

        } catch (Exception e) {

        }
    }
}
