/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 *
 * @author luminoso
 */
public class ClientCom {

    private Socket commSocket = null; // Socket de comunicação

    private String serverHostName = null;
    private int serverPortNumb;

    private ObjectInputStream in = null; //  Stream de entrada do canal de comunicação
    private ObjectOutputStream out = null;

    public ClientCom(String hostName, int portNumb) {
        serverHostName = hostName;
        serverPortNumb = portNumb;
    }

    public boolean open() {
        boolean success = true;
        SocketAddress serverAddress = new InetSocketAddress(serverHostName, serverPortNumb);

        try {
            commSocket = new Socket();
            commSocket.connect(serverAddress);
        } catch (UnknownHostException e) {
        } catch (NoRouteToHostException e) {
        } catch (ConnectException e) {
        } catch (SocketTimeoutException e) {
        } catch (IOException e) {
        }

        if (!success) {
            return success;
        }

        try {
            out = new ObjectOutputStream(commSocket.getOutputStream());
        } catch (IOException e) {

        }

        try {
            in = new ObjectInputStream(commSocket.getInputStream());
        } catch (IOException e) {

        }

        return success;
    }

    /**
     * Fecho do canal de comunicação. Fecho dos streams de entrada e de saída do
     * socket. Fecho do socket de comunicação.
     */
    public void close() {
        try {
            in.close();
        } catch (IOException e) {
        }

        try {
            out.close();
        } catch (IOException e) {

        }

        try {
            commSocket.close();
        } catch (IOException e) {

        }
    }

    /**
     * Leitura de um objecto do canal de comunicação.
     *
     * @return objecto lido
     */
    public Object readObject() {
        Object fromServer = null;                            // objecto

        try {
            fromServer = in.readObject();
        } catch (InvalidClassException e) {

        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        }

        return fromServer;
    }

    /**
     * Escrita de um objecto no canal de comunicação.
     *
     * @param toServer objecto a ser escrito
     */
    public void writeObject(Object toServer) {
        try {
            out.writeObject(toServer);
        } catch (InvalidClassException e) {

        } catch (NotSerializableException e) {

        } catch (IOException e) {

        }
    }
}
