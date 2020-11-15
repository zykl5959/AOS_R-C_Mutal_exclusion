import java.io.IOException;

public interface MsgHandler {
    public void handleMsg(Msg m,int srcId,String tag) throws IOException;
    public Msg receiveMsg(int fromId) throws IOException;

}
