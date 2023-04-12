package protocols.adderEngine;

public class AdderParseException extends AdderProtocolEngineException {
    public AdderParseException() {super();}
    public AdderParseException(String msg) {super(msg);}
    public AdderParseException(String msg, Exception e) {super(msg, e);}
}

