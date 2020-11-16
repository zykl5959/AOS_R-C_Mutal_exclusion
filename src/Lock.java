import java.io.IOException;

public interface Lock extends MsgHandler {
    public void cs_enter() throws IOException;
    public void cs_leave() throws IOException;
}
