package pt.up.fe.aes.base.instrumentation;

import javassist.CtClass;

public interface Pass {
    public static enum Outcome {CONTINUE, CANCEL, FINISH};

    Outcome transform (CtClass c) throws Exception;
}