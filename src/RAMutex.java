import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RAMutex extends Process implements Lock{
    public boolean inCritical;
    int myts;
    LamportClock c = new LamportClock();
    LinkedList<Integer> pendingQ = new LinkedList<>();
    int numOkay = 0;

    boolean[] keys = new boolean[N];

    public RAMutex(Linker initComm){
        super(initComm);
        myts = Integer.MAX_VALUE;
        for (int i=myId;i<keys.length;i++){
            keys[i] = true;
        }
        inCritical = false;
//        if (myId == 0) inCritical = true;
//        else inCritical = false;

    }
    @Override
    public synchronized void requestCS() throws IOException {
        List<Integer> competed_process_id = count_num_of_competed(keys);
        int numOfcompeted = competed_process_id.size();
        c.tick();
        myts = c.getValue();
//        this.boardcastMsg("request",myts);
        this.multicastMsg("request",myts,competed_process_id);
        numOkay = 0;
        while (numOkay<numOfcompeted)
            this.myWait();
        inCritical = true;
    }

    private List<Integer> count_num_of_competed(boolean[] keys) {
        List<Integer> competed_process_id = new ArrayList<>();
        for (int i=0;i< keys.length;i++){
            if (!keys[i] && i!=myId){
                competed_process_id.add(i);
            }
        }
        return competed_process_id;
    }

    @Override
    public synchronized void releaseCS() throws IOException {
        myts = Integer.MAX_VALUE;
        while (!pendingQ.isEmpty()){
            int pid = pendingQ.removeFirst();
            sendMsg(pid,"okay",c.getValue());
//            sendMsg(pid,"request",c.getValue());
            boolean temp = keys[pid];
            keys[pid] = false;
            numOkay--;
            System.out.println("change from key["+pid+"]"+temp + " to "+keys[pid]);
        }
        inCritical = false;
    }
    public synchronized void handleMsg(Msg m,int srcId,String tag) throws IOException {
        int timeStamp = m.getMessageInt();
        c.receiveAction(srcId,timeStamp);
        if(tag.equals("request")){
            if(myts==Integer.MAX_VALUE || timeStamp<myts||(timeStamp==myts&& srcId<myId)){
                sendMsg(srcId,"okay",c.getValue());
//                sendMsg(srcId,"request",c.getValue());
                boolean temp = keys[srcId];

                keys[srcId] = false;
                numOkay--;
                System.out.println("change from key["+srcId+"]"+temp + " to "+keys[srcId]);
            }
            else if (inCritical){
                pendingQ.add(srcId);
            }
            else
                pendingQ.add(srcId);
        }else if(tag.equals("okay")){
            boolean temp = keys[srcId];
            keys[srcId] = true;
            numOkay++;
            System.out.println("change from key["+srcId+"]"+temp + " to "+keys[srcId]);

            List<Integer> competed_process_id = count_num_of_competed(keys);
            System.out.println("competedId "+competed_process_id);
            if (competed_process_id.size()==0)
                notify();
//            numOkay++;
//            if(numOkay==N-1)
//                notify();
        }

    }
}
