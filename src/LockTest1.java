import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Date;

public class LockTest1 {
    public static void main(String[] args) {
        Linker comm = null;
        int identifier = Integer.parseInt(args[0]);
        int port = Integer.parseInt(args[1]);
        int Inter_request_delay = Integer.parseInt(args[2]);
        int Cs_execution_time = Integer.parseInt(args[3]);
        int Node_requests = Integer.parseInt(args[4]);
        int Node_num = Integer.parseInt(args[5]);

        try{
            String baseName = "dc01";
            int myId = identifier;
            int numProc = Node_num;

            FileWriter myWriter = new FileWriter("mylog"+myId+".txt");

            comm = new Linker(baseName,myId,numProc);
            Lock lock = new RAMutex(comm);
            for(int i=0;i<numProc;i++){
                if(i!=myId)
                    (new ListenerThread(i,(MsgHandler)lock)).start();
            }
            for(int i=0;i<Node_requests;i++){
                myWriter.write("RequestNum"+i+"#\t");

                System.out.println(myId+ " is not in CS");

//                Util.mySleep(2000);
                Thread.sleep(Inter_request_delay);
                myWriter.write("requestTime "+LocalTime.now()+"#\t");
                lock.cs_enter();
//                System.out.println("startTime "+ LocalTime.now());
                myWriter.write("startTime "+LocalTime.now()+"#\t");
//                Util.mySleep(2000);
                Thread.sleep(Cs_execution_time);
                System.out.println(myId+ " is in CS");
                myWriter.write("endTime "+LocalTime.now()+"\n");
//                System.out.println("endTime "+LocalTime.now());
                lock.cs_leave();
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
