import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Connector {
    ServerSocket listener;
    Socket[] link;
    Map<Integer, String> id_to_host = new HashMap<>();
    Map<Integer, Integer> id_to_port = new HashMap<>();
    public void Connect(String baseName, int myId, int numProc, DataInputStream[] dis, DataOutputStream[] dos) throws IOException, ClassNotFoundException {
//        id_to_port.put(0,2020);
//        id_to_port.put(1,2021);
//        id_to_port.put(2,2022);
        readConfig(id_to_host,id_to_port);

        link = new Socket[numProc];
        int localport = id_to_port.get(myId);
        listener = new ServerSocket(localport);

        for (int i =0;i<myId;i++){
            Socket s = listener.accept();
            DataInputStream single_dis = new DataInputStream(s.getInputStream());
            String getline = single_dis.readUTF();
            StringTokenizer st  = new StringTokenizer(getline);
            int hisId = Integer.parseInt(st.nextToken());
            int destId = Integer.parseInt(st.nextToken());
            String tag = st.nextToken();
            if (tag.equals("hello")){
                link[hisId] = s;
                dis[hisId] = single_dis;
                dos[hisId] = new DataOutputStream(s.getOutputStream());
            }

        }
        if (myId == 0) {
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i=myId+1 ;i<numProc;i++){
            InetAddress ip = InetAddress.getByName(id_to_host.get(i));
            link[i] = new Socket(ip, id_to_port.get(i));
//            System.out.println(link[i]);
//            System.out.println(link[i].getOutputStream());
//            System.out.println(link[i].getInputStream());
//            System.out.println(dos[i]);
//            System.out.println(dis[i]);
            dis[i] = new DataInputStream(link[i].getInputStream());
            dos[i] = new DataOutputStream(link[i].getOutputStream());
//            System.out.println(dos[i]);
            dos[i].writeUTF(myId+" "+i+" "+"hello"+" "+"null");
//            dos[i].flush();
//            System.out.println(new ObjectInputStream(link[i].getInputStream()));
        }
    }

    private void readConfig(Map<Integer, String> id_to_host, Map<Integer, Integer> id_to_port) throws FileNotFoundException {
//        id_to_host.put(0,"dc01");
//        id_to_port.put(0,2020);
//
//        id_to_host.put(1,"dc02");
//        id_to_port.put(1,2021);
//
//        id_to_host.put(2,"dc03");
//        id_to_port.put(2,2022);
        File f = new File("src/configuration.txt");
        System.out.println(f);
        Scanner myReader = new Scanner(f);
        int count = 0;
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            if (data.contains("#")) {
                continue;
            }
            break;
        }
        while (myReader.hasNext()){
            String data = myReader.nextLine();
            String[] dataArr = data.split(" ");
            int id = Integer.parseInt(dataArr[0]);
            String host = dataArr[1];
            int port = Integer.parseInt(dataArr[2]);
            System.out.println(port);
            id_to_host.put(id,host);
            id_to_port.put(id, port);

        }


    }


    public void closeSocket() {
        try {
            listener.close();
            for(int i=0;i<link.length;i++){
                link[i].close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
