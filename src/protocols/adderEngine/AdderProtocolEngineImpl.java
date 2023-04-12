package protocols.adderEngine;

import java.io.IOException;
import java.io.OutputStream;

public class AdderProtocolEngineImpl implements AdderProtocolEngine {
    private final AdderEngineSerialization serializer;
    private final OutputStream os;

    public AdderProtocolEngineImpl(AdderEngineSerialization serializer, OutputStream os) {
        this.serializer = serializer;
        this.os = os;

    }
    @Override
    public void add(AddOperation addOperation) {
        System.out.println("received operation: " + addOperation);

        int result = addOperation.getSummandA() + addOperation.getSummandB();
        System.out.println("calculate: "
                + addOperation.getSummandA() + " + "
                + addOperation.getSummandB() + " == "
                + result);

        // produce result
        AddResultImpl addResult = new AddResultImpl(addOperation, result);
        System.out.println("send back result: " + addResult);
        try {
            // send result
            this.serializer.serialize(addResult, this.os);
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    @Override
    public void handleResult(AddResult addResult) {
        System.out.println("got result: " + addResult);
    }
}
