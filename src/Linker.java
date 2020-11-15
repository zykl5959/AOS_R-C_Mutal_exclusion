import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class Linker {
    DataOutputStream[] dos;
    DataInputStream[] dis;
    int myId, N;
    Socket server_accept;
    Socket client;
    ServerSocket server_socket   = null;
    Connector connector;

    public Linker(String baseName, int id, int numProc) throws IOException, ClassNotFoundException {
        myId = id;
        N = numProc;
        // Size 2
        dis = new DataInputStream[numProc];
        dos = new DataOutputStream[numProc];
        connector = new Connector();
        connector.Connect(baseName,myId,numProc,dis,dos);
    }

    public int getMyId() {
        return myId;
    }

    public int getNumProc() {
        return N;
    }

    public Msg receiveMsg(int fromId) throws IOException {
        String getline = dis[fromId].readUTF();
        System.out.println("receive message "+getline);
        StringTokenizer st = new StringTokenizer(getline);
        int srcId = Integer.parseInt(st.nextToken());
        int destId = Integer.parseInt(st.nextToken());
        String tag = st.nextToken();
        String msg = st.nextToken("#");
        return new Msg(srcId,destId,tag,msg);
    }

    public void close() {
        connector.closeSocket();
    }

    public void sendMsg(int destId,String tag, String msg) throws IOException {
        dos[destId].writeUTF(myId+" "+destId+" "+tag+" "+msg+"#");
        dos[destId].flush();
    }
}
