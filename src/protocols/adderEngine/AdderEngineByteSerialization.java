package protocols.adderEngine;

import java.io.*;

public class AdderEngineByteSerialization implements AdderEngineSerialization {
    private static final byte OPERATION_ADD = 0;
    private static final byte OPERATION_ADD_RESULT = 1;
    @Override
    public void serialize(AddOperation addOperation, OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);

        dos.write(OPERATION_ADD);
        dos.writeInt(addOperation.getSummandA());
        dos.writeInt(addOperation.getSummandB());
    }
    @Override
    public AddOperation parseAddOperation(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        int summandA = dis.readInt();
        int summandB = dis.readInt();

        return new AddOperationImpl(summandA, summandB);
    }

    @Override
    public void serialize(AddResult addResult, OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);

        dos.write(OPERATION_ADD);
        // serialize original operation
        serialize(addResult.getOperation(), os);
        // serialize result
        dos.writeInt(addResult.getResult());
    }
    @Override
    public AddResult parseAddResult(InputStream is) throws IOException {
        AddOperation addOperation = parseAddOperation(is);

        DataInputStream dis = new DataInputStream(is);
        int result = dis.readInt();
        return new AddResultImpl(addOperation, result);
    }

    @Override
    public byte parseOperation(InputStream is) throws AdderParseException, IOException {
        byte operation = (byte) is.read();
        if(operation != OPERATION_ADD && operation != OPERATION_ADD_RESULT)
            throw new AdderParseException("unknown operation: " + operation);

        return operation;
    }

}
