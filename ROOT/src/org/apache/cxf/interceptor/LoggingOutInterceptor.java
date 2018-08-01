package org.apache.cxf.interceptor;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.io.*;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;

public class LoggingOutInterceptor extends AbstractPhaseInterceptor {
	private static final Logger LOG = LogUtils
			.getL7dLogger(LoggingOutInterceptor.class);

	private int limit = 102400;
	private PrintWriter writer;

	public LoggingOutInterceptor(String phase) {
		super(phase);
		addBefore(StaxOutInterceptor.class.getName());
	}

	public LoggingOutInterceptor() {
		this("pre-stream");
	}

	public LoggingOutInterceptor(int lim) {
		this();
		this.limit = lim;
	}

	public LoggingOutInterceptor(PrintWriter w) {
		this();
		this.writer = w;
	}

	public void setLimit(int lim) {
		this.limit = lim;
	}

	public int getLimit() {
		return this.limit;
	}

	public void handleMessage(Message message) throws Fault {
		OutputStream os = (OutputStream) message.getContent(OutputStream.class);
		if (os == null) {
			return;
		}

		if ((LOG.isLoggable(Level.INFO)) || (this.writer != null)) {
			CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(os);
			message.setContent(OutputStream.class, newOut);
			newOut.registerCallback(new LoggingCallback(message, os));
		}
	}

	protected String transform(String originalLogString) {
		return originalLogString;
	}

	class LoggingCallback implements CachedOutputStreamCallback {
		private final Message message;
		private final OutputStream origStream;

		public LoggingCallback(Message msg, OutputStream os) {
			this.message = msg;
			this.origStream = os;
		}

		public void onFlush(CachedOutputStream cos) {
		}

		public void onClose(CachedOutputStream cos) {
			String id = (String) this.message.getExchange().get(
					LoggingMessage.ID_KEY);
			if (id == null) {
				id = LoggingMessage.nextId();
				this.message.getExchange().put(LoggingMessage.ID_KEY, id);
			}
			LoggingMessage buffer = new LoggingMessage(
					"Outbound Message\n---------------------------", id);

			String encoding = (String) this.message.get(Message.ENCODING);

			if (encoding != null) {
				buffer.getEncoding().append(encoding);
			}

			String address = (String) this.message
					.get(Message.ENDPOINT_ADDRESS);
			if (address != null) {
				buffer.getAddress().append(address);
			}
			String ct = (String) this.message.get("Content-Type");
			if (ct != null) {
				buffer.getContentType().append(ct);
			}
			Object headers = this.message.get(Message.PROTOCOL_HEADERS);
			if (headers != null) {
				buffer.getHeader().append(headers);
			}

			if (cos.getTempFile() == null) {
				if (cos.size() > LoggingOutInterceptor.this.limit)
					buffer.getMessage().append(
							"(message truncated to "
									+ LoggingOutInterceptor.this.limit
									+ " bytes)\n");
			} else {
				buffer.getMessage().append(
						"Outbound Message (saved to tmp file):\n");
				buffer.getMessage().append(
						"Filename: " + cos.getTempFile().getAbsolutePath()
								+ "\n");
				if (cos.size() > LoggingOutInterceptor.this.limit)
					buffer.getMessage().append(
							"(message truncated to "
									+ LoggingOutInterceptor.this.limit
									+ " bytes)\n");
			}
			try {
				cos.writeCacheTo(buffer.getPayload(),
						LoggingOutInterceptor.this.limit);
			} catch (Exception ex) {
			}
			/*if (LoggingOutInterceptor.this.writer != null)
				LoggingOutInterceptor.this.writer
						.println(LoggingOutInterceptor.this.transform(buffer
								.toString()));
			else if (LoggingOutInterceptor.LOG.isLoggable(Level.INFO)) {
				LoggingOutInterceptor.LOG.info(LoggingOutInterceptor.this
						.transform(buffer.toString()));
			}*/
			try {
				cos.lockOutputStream();
				cos.resetOut(null, false);
			} catch (Exception ex) {
			}
			this.message.setContent(OutputStream.class, this.origStream);
		}
	}
}