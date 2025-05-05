package client.src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * MainClientGUI è l'interfaccia grafica Swing per il client delle strutture ricettive.
 * Permette di selezionare protocollo, IP, porta, inviare comandi e visualizzare risposte.
 * Supporta sia TCP che UDP con I/O asincrono e storico dei comandi.
 */
public class MainClientGUI extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(MainClientGUI.class.getName());

    private Connection connection;                // Connessione attiva (TCP o UDP)

    private JComboBox<String> protocolBox;        // Selettore del protocollo (TCP/UDP)
    private JTextField ipField;                   // Campo per inserire l'IP del server
    private JTextField portField;                 // Campo per inserire la porta del server
    private JButton connectButton;                // Pulsante per avviare la connessione

    private JTextArea outputArea;                 // Area di testo per visualizzare output e log
    private JComboBox<String> commandHistoryBox;  // Combo box per storico comandi
    private DefaultComboBoxModel<String> historyModel; // Modello per gestire lo storico
    private JButton sendButton;                   // Pulsante per inviare il comando corrente

    /**
     * Costruisce e mostra la GUI principale.
     */
    public MainClientGUI() {
        super("Client Strutture Ricettive");
        initLookAndFeel();   // Imposta look and feel di sistema
        initComponents();    // Crea e posiziona tutti i componenti
    }

    /**
     * Tenta di impostare il Look and Feel di sistema, aggiornando la UI sul thread EDT.
     */
    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.invokeLater(() -> SwingUtilities.updateComponentTreeUI(this));
        } catch (Exception e) {
            LOGGER.warning("Unable to set system look and feel: " + e.getMessage());
        }
    }

    /**
     * Inizializza tutti i componenti Swing, layout e listener.
     */
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Centra la finestra

        createMenuBar(); // Menu File->Exit

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // -- Pannello superiore: impostazioni di connessione --
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.add(new JLabel("Protocollo:"));
        protocolBox = new JComboBox<>(new String[]{"TCP", "UDP"});
        topPanel.add(protocolBox);
        topPanel.add(new JLabel("Server IP:"));
        ipField = new JTextField("127.0.0.1", 12);
        topPanel.add(ipField);
        topPanel.add(new JLabel("Porta:"));
        portField = new JTextField("1050", 6);
        topPanel.add(portField);
        connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> onConnect()); // Listener di connessione
        topPanel.add(connectButton);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // -- Pannello centrale: area di output --
        outputArea = new JTextArea();
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        outputArea.setEditable(false);
        mainPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // -- Pannello inferiore: input comando e invio --
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 0));
        historyModel = new DefaultComboBoxModel<>();
        commandHistoryBox = new JComboBox<>(historyModel);
        commandHistoryBox.setEditable(true);
        commandHistoryBox.setEnabled(false); // Disabilitato finché non connesso
        bottomPanel.add(commandHistoryBox, BorderLayout.CENTER);
        sendButton = new JButton("Send");
        sendButton.setEnabled(false); // Disabilitato finché non connesso
        bottomPanel.add(sendButton, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Listener per invio comandi
        sendButton.addActionListener(e -> onSend());
        ((JTextField) commandHistoryBox.getEditor().getEditorComponent())
                .addActionListener(e -> onSend());

        setVisible(true);
    }

    /**
     * Crea la barra dei menu con la voce File->Exit per chiudere l'app.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> {
            // Chiude la connessione se aperta
            if (connection != null && connection.isConnected()) {
                connection.close();
            }
            System.exit(0);
        });
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Callback chiamata quando si clicca su Connect.
     * Avvia un SwingWorker per aprire la connessione in background.
     */
    private void onConnect() {
        String protocol = (String) protocolBox.getSelectedItem();
        String ip = ipField.getText().trim();
        int port;
        try {
            port = Integer.parseInt(portField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Porta non valida", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        connectButton.setEnabled(false); // Disabilita per evitare doppio click
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() {
                try {
                    // Crea la connessione TCP o UDP
                    if ("UDP".equals(protocol)) {
                        connection = new UdpClientConnection(ip, port);
                    } else {
                        connection = new ClientConnection(ip, port);
                    }
                    // Richiede messaggio di help
                    return connection.sendCommand("help");
                } catch (IOException e) {
                    return "[Connection error: " + e.getMessage() + "]";
                }
            }
            @Override
            protected void done() {
                try {
                    String help = get();
                    if (help.startsWith("[Connection error")) {
                        outputArea.append(help + "\n");
                        connectButton.setEnabled(true);
                    } else {
                        // Connessione avvenuta con successo
                        outputArea.append("[Connesso via " + protocol + " a " + ip + ":" + port + "]\n");
                        outputArea.append(help);
                        // Disabilita impostazioni di connessione
                        protocolBox.setEnabled(false);
                        ipField.setEnabled(false);
                        portField.setEnabled(false);
                        commandHistoryBox.setEnabled(true);
                        sendButton.setEnabled(true);
                    }
                } catch (Exception e) {
                    outputArea.append("[Errore durante la connessione]\n");
                    connectButton.setEnabled(true);
                }
            }
        }.execute();
    }

    /**
     * Callback chiamata quando si invia un comando.
     * Aggiorna lo storico, invia il comando in background e stampa la risposta.
     */
    private void onSend() {
        String cmd = ((JTextField) commandHistoryBox.getEditor().getEditorComponent()).getText().trim();
        // Verifica comando non vuoto e connessione attiva
        if (cmd.isEmpty() || connection == null || !connection.isConnected()) {
            return;
        }
        // Aggiunge comando in cima allo storico
        historyModel.removeElement(cmd);
        historyModel.insertElementAt(cmd, 0);
        commandHistoryBox.setSelectedIndex(0);

        sendButton.setEnabled(false);
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() {
                try {
                    return connection.sendCommand(cmd);
                } catch (IOException e) {
                    return "[Error: " + e.getMessage() + "]";
                }
            }
            @Override
            protected void done() {
                try {
                    // Stampa prompt e comando
                    outputArea.append(">>> " + cmd + "\n");
                    // Recupera e stampa risposta
                    String response = get();
                    outputArea.append(response);
                    if (!response.endsWith("\n")) {
                        outputArea.append("\n");
                    }
                    // Disabilita o riabilita i controlli post-exit
                    if ("exit".equalsIgnoreCase(cmd)) {
                        sendButton.setEnabled(false);
                        commandHistoryBox.setEnabled(false);
                    } else {
                        sendButton.setEnabled(true);
                    }
                } catch (Exception e) {
                    outputArea.append("[Error retrieving response]\n");
                    sendButton.setEnabled(true);
                }
            }
        }.execute();
        // Pulisce il campo di input
        ((JTextField) commandHistoryBox.getEditor().getEditorComponent()).setText("");
    }

    /**
     * Avvia la GUI sul Event Dispatch Thread.
     *
     * @param args non utilizzati
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainClientGUI::new);
    }
}
