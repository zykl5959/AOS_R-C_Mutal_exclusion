
import java.io.IOException;
import java.util.List;

public class Process implements MsgHandler{
    int N,myId;
    Linker comm;
    public Process(Linker initComm){
        comm = initComm;
        myId = comm.getMyId();
        N = comm.getNumProc();
    }
    @Override
    public synchronized void handleMsg(Msg m, int srcId, String tag) throws IOException {

    }

    @Override
    public Msg receiveMsg(int fromId) {
        try{
            return comm.receiveMsg(fromId);
        }catch (IOException e){
            System.out.println(e);
            comm.close();
            return null;
        }
    }

    public void sendMsg(int destId,String tag,String msg) throws IOException {
        System.out.println("Sending msg to "+destId +":"+tag+" "+msg);
        comm.sendMsg(destId, tag, msg);
    }

    public void sendMsg(int destId,String tag,int msg) throws IOException {
        sendMsg(destId,tag,String.valueOf(msg));
    }
    public void boardcastMsg(String tag, int msg) throws IOException {
        for (int i=0;i<N;i++)
            if(i!=myId) sendMsg(i,tag,msg);
    }
    public void multicastMsg(String tag,int msg, List<Integer> competed_process) throws IOException {
        for (int process_id : competed_process){
            sendMsg(process_id,tag,msg);
        }
    }
    public synchronized void myWait(){
        try {
            wait();
        }catch (InterruptedException e){
            System.err.println(e);
        }
    }
}
