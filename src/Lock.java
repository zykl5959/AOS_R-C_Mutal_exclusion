import java.io.IOException;

public interface Lock extends MsgHandler {
    public void requestCS() throws IOException;
    public void releaseCS() throws IOException;
}
