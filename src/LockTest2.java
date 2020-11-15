import java.io.FileWriter;
import java.time.LocalTime;

public class LockTest2 {
    public static void main(String[] args) {
        Linker comm = null;
        int identifier = Integer.parseInt(args[0]);
        int port = Integer.parseInt(args[1]);
        int Inter_request_delay = Integer.parseInt(args[2]);
        int Cs_execution_time = Integer.parseInt(args[3]);
        int Node_requests = Integer.parseInt(args[4]);
        int Node_num = Integer.parseInt(args[5]);
        try{
            String baseName = "dc00";
            int myId = identifier;
            int numProc = Node_num;
            comm = new Linker(baseName,myId,numProc);
            Lock lock = new RAMutex(comm);
            for(int i=0;i<numProc;i++){
                if(i!=myId)
                    (new ListenerThread(i,(MsgHandler)lock)).start();
            }

            FileWriter myWriter = new FileWriter("src/log"+myId+".txt");
            for(int i=0;i<Node_requests;i++){
                System.out.println(myId+ " is not in CS");
//                Util.mySleep(2000);
                Thread.sleep(2000);

                lock.requestCS();
//                System.out.println("startTime "+LocalTime.now());
                myWriter.write("startTime "+LocalTime.now()+"\t");
//                Util.mySleep(2000);
                Thread.sleep(2000);
                System.out.println(myId+ " is in CS");
                lock.releaseCS();
                System.out.println("endTime "+LocalTime.now());
                myWriter.write("endTime "+LocalTime.now()+"\n");
            }
            myWriter.close();
        }
        catch (InterruptedException e){
            if(comm!=null) {
                System.out.println(e);
                comm.close();
            }
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
