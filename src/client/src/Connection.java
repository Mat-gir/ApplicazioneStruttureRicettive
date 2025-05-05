package client.src;

import java.io.IOException;

/**
 * Common interface for client-server connections.
 * Defines basic operations for sending textual commands and receiving responses.
 */
public interface Connection {

    /**
     * Sends a command to the server and returns its response.
     *
     * @param cmd the command string to send
     * @return the server response as a string (may contain line breaks)
     * @throws IOException if an I/O error occurs during the operation
     */
    String sendCommand(String cmd) throws IOException;

    /**
     * Checks whether the underlying connection is still open.
     *
     * @return true if connected and ready for I/O, false otherwise
     */
    boolean isConnected();

    /**
     * Closes the connection and releases any associated resources.
     */
    void close();
}
