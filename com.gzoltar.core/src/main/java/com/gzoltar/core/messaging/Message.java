package com.gzoltar.core.messaging;

import java.io.Serializable;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.ProbeGroup;

public interface Message {

  public static class HandshakeMessage implements Message, Serializable {

    private static final long serialVersionUID = 4280869312305959655L;

    public final String id;

    public HandshakeMessage(final String id) {
      this.id = id;
    }

    protected HandshakeMessage() {
      this(null);
    }
  }

  public static class ByeMessage implements Message, Serializable {

    private static final long serialVersionUID = -4155285280206362746L;

  }

  public static class EndTransactionMessage implements Message, Serializable {

    private static final long serialVersionUID = 214052749607422773L;

    public final Transaction transaction;

    public EndTransactionMessage(final Transaction transaction) {
      this.transaction = transaction;
    }

    protected EndTransactionMessage() {
      this(null);
    }
  }

  public static class AddProbeGroupMessage implements Message, Serializable {

    private static final long serialVersionUID = 3116251573538148450L;

    public final ProbeGroup probeGroup;

    public AddProbeGroupMessage(final ProbeGroup probeGroup) {
      this.probeGroup = probeGroup;
    }

    protected AddProbeGroupMessage() {
      this(null);
    }
  }

}
