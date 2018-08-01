package org.apache.cxf.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;

public class LoggingInInterceptor extends AbstractPhaseInterceptor<Message>
{
  private static final Logger LOG = LogUtils.getL7dLogger(LoggingInInterceptor.class);

  private int limit = 102400;
  private PrintWriter writer;

  public LoggingInInterceptor()
  {
    super("receive");
  }

  public LoggingInInterceptor(String phase) {
    super(phase);
  }

  public LoggingInInterceptor(int lim) {
    this();
    this.limit = lim;
  }

  public LoggingInInterceptor(PrintWriter w) {
    this();
    this.writer = w;
  }

  public void setPrintWriter(PrintWriter w) {
    this.writer = w;
  }

  public PrintWriter getPrintWriter() {
    return this.writer;
  }

  public void setLimit(int lim) {
    this.limit = lim;
  }

  public int getLimit() {
    return this.limit;
  }

  public void handleMessage(Message message) throws Fault {
    if ((this.writer != null) || (LOG.isLoggable(Level.INFO)))
      logging(message);
  }

  protected String transform(String originalLogString)
  {
    return originalLogString;
  }

  private void logging(Message message) throws Fault {
    String id = (String)message.getExchange().get(LoggingMessage.ID_KEY);
    if (id == null) {
      id = LoggingMessage.nextId();
      message.getExchange().put(LoggingMessage.ID_KEY, id);
    }
    LoggingMessage buffer = new LoggingMessage("Inbound Message\n----------------------------", id);

    String encoding = (String)message.get(Message.ENCODING);

    if (encoding != null) {
      buffer.getEncoding().append(encoding);
    }
    String ct = (String)message.get("Content-Type");
    if (ct != null) {
      buffer.getContentType().append(ct);
    }
    Object headers = message.get(Message.PROTOCOL_HEADERS);

    if (headers != null) {
      buffer.getHeader().append(headers);
    }
    String uri = (String)message.get(Message.REQUEST_URI);
    if (uri != null) {
      buffer.getAddress().append(uri);
    }

    InputStream is = (InputStream)message.getContent(InputStream.class);
    if (is != null) {
      CachedOutputStream bos = new CachedOutputStream();
      try {
        IOUtils.copy(is, bos);

        bos.flush();
        is.close();

        message.setContent(InputStream.class, bos.getInputStream());
        if (bos.getTempFile() != null)
        {
          buffer.getMessage().append("\nMessage (saved to tmp file):\n");
          buffer.getMessage().append("Filename: " + bos.getTempFile().getAbsolutePath() + "\n");
        }
        if (bos.size() > this.limit) {
          buffer.getMessage().append("(message truncated to " + this.limit + " bytes)\n");
        }
        bos.writeCacheTo(buffer.getPayload(), this.limit);

        bos.close();
      } catch (Exception e) {
        throw new Fault(e);
      }
    }

    if (this.writer != null)
      this.writer.println(transform(buffer.toString()));
   /* else if (LOG.isLoggable(Level.INFO))
      LOG.info(transform(buffer.toString()));*/
  }
}