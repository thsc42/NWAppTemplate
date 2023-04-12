package protocols.adderEngine;

class AddOperationImpl implements AddOperation {
    private final int summandA;
    private final int summandB;

    AddOperationImpl(int summandA, int summandB) {
        this.summandA = summandA;
        this.summandB = summandB;
    }

    @Override
    public int getSummandA() {
        return this.summandA;
    }

    @Override
    public int getSummandB() {
        return this.summandB;
    }

    public String toString() {
        return this.summandA + " + " + this.summandB;
    }
}
