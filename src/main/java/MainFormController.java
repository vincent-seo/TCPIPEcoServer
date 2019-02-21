import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainFormController {

    ServerSocket serverSocket;
    List<Client> clientSocket;
    Thread threadServer;

    @FXML Button btnStart, btnStop;
    @FXML TextField txfPort;
    @FXML TextArea txaDisplay;

    public void initialize() {
        btnStart.setOnAction(event -> {
            actionStartServer();
        });
        btnStop.setOnAction(event -> {
            actionStopServer();
        });

        btnStart.setDisable(false);
        btnStop.setDisable(true);
    }

    private void actionStopServer() {

    }
    private void actionStartServer() {
        int port = getPortNumber();
        if(port == 0) { return; }

        try {
            serverSocket = new ServerSocket(port);

            btnStart.setDisable(true);
            btnStop.setDisable(false);

            threadServer = new Thread(() -> {
                while (!serverSocket.isClosed()) {
                    try {
                        Socket socket = serverSocket.accept();
                        Random r = new Random();
                        int n = r.nextInt(1000);
                        new Thread(new Client(socket, String.valueOf(n))).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadServer.start();
        } catch (IOException e) {

        }
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR,message , ButtonType.OK);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            return;
        }
    }
    private int getPortNumber() {
        int port = 0;
        String portNumber = txfPort.getText();

        try {
            port = Integer.parseInt(portNumber);
            if(port < 1000)
            {
                showErrorDialog("The number can't samller then 10000.");
                return 0;
            }

            if(port > 99999)
            {
                showErrorDialog("The number can't more then 99999.");
                return 0;
            }

        }catch (Exception e) {
            showErrorDialog("The number format is wrong.");
            return 0;
        }

        return port;
    }

    public class Client implements Runnable {
        Socket socket;
        String id;
        Scanner in;
        PrintWriter out;

        public Client(Socket socket, String id) {
            this.socket = socket;
            this.id = id;
            updateDisplay("Client ID: " + id + " connected.");
        }

        @Override
        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream());

                while (in.hasNextLine()) {
                    String received = in.nextLine().toUpperCase();
                    updateDisplay("Client ID: " + id + " received [" + received + "]");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void updateDisplay(String message) {
            Platform.runLater(() -> {
                txaDisplay.appendText(message + "\n");
            });
        }
    }
}
