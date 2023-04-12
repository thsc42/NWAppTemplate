package protocols.adderEngine;

/**
 * Protocol engine interfaces. It describes offered methods/services/functions (call it as you like).
 *
 * It does <b>not</b> describe serialisation.
 */
public interface AdderProtocolEngine {
    /**
     * Calculates sum of two integer numbers. There is no return value - we describe
     * a protocol engine.
     */
    void add(AddOperation addOperation);

    void handleResult(AddResult addResult);
}
