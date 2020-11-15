import java.util.StringTokenizer;

public class Msg {
    int srcId,destId;
    String tag;
    String msgBuf;
    public Msg(int srcId, int destId, String msgType, String buf) {
        this.srcId =srcId;
        this.destId = destId;
        this.tag = msgType;
        this.msgBuf = buf;
    }

    public int getSrcId() {
        return srcId;
    }

    public String getTag() {
        return tag;
    }

    public String getMessage(){
        return msgBuf;
    }

    public int getMessageInt(){
        StringTokenizer st = new StringTokenizer(msgBuf);
        return Integer.parseInt(st.nextToken());
    }
}
