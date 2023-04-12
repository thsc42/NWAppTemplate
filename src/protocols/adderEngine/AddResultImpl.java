package protocols.adderEngine;

class AddResultImpl implements AddResult {

    private final AddOperation addOperation;
    private final int result;

    AddResultImpl(AddOperation addOperation, int result) {
        this.addOperation = addOperation;
        this.result = result;
    }
    @Override
    public AddOperation getOperation() {
        return this.addOperation;
    }

    @Override
    public int getResult() {
        return this.result;
    }

    public String toString() {
        return "operation: " + this.addOperation
        + "\nresult == " + this.result;
    }

}
