package protocols.adderEngine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface AdderEngineSerialization {
    void serialize(AddOperation addOperation, OutputStream os) throws IOException;

    AddOperation parseAddOperation(InputStream is) throws IOException;

    void serialize(AddResult addResult, OutputStream os) throws IOException;

    AddResult parseAddResult(InputStream is) throws IOException;

    byte parseOperation(InputStream is) throws AdderParseException, IOException;
}
