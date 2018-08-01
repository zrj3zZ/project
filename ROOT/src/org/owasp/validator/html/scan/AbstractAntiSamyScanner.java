package org.owasp.validator.html.scan;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.xml.serialize.HTMLSerializer;
import org.apache.xml.serialize.OutputFormat;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.owasp.validator.html.util.ErrorMessageUtil;

public abstract class AbstractAntiSamyScanner
{
  protected Policy policy;
  protected ArrayList errorMessages = new ArrayList();
  protected ResourceBundle messages;
  protected Locale locale = Locale.getDefault();

  protected boolean isNofollowAnchors = false;
  protected boolean isValidateParamAsEmbed = false;

  public abstract CleanResults scan(String paramString1, String paramString2, String paramString3) throws ScanException;

  public abstract CleanResults getResults();

  public AbstractAntiSamyScanner(Policy policy) {
    this.policy = policy;
    initializeErrors();
  }

  public AbstractAntiSamyScanner() throws PolicyException {
    this.policy = Policy.getInstance();
    initializeErrors();
  }

  protected void initializeErrors() {
    try {
      this.messages = ResourceBundle.getBundle("AntiSamy", this.locale);
    } catch (MissingResourceException mre) {
      this.messages = ResourceBundle.getBundle("AntiSamy", new Locale("en", "US"));
    }
  }

  protected void addError(String errorKey, Object[] objs) {
    this.errorMessages.add(ErrorMessageUtil.getMessage(this.messages, errorKey, objs));
  }

  protected OutputFormat getOutputFormat(String encoding)
  {
    boolean formatOutput = "true".equals(this.policy.getDirective("formatOutput"));
    boolean preserveSpace = "true".equals(this.policy.getDirective("preserveSpace"));

    OutputFormat format = new OutputFormat();
    format.setEncoding(encoding);
    format.setOmitXMLDeclaration("true".equals(this.policy.getDirective("omitXmlDeclaration")));
    format.setOmitDocumentType("true".equals(this.policy.getDirective("omitDoctypeDeclaration")));
    format.setPreserveEmptyAttributes(true);
    format.setPreserveSpace(preserveSpace);
    format.setLineSeparator(this.policy.getDirective("lineSeparator"));

    if (formatOutput) {
      format.setLineWidth(80);
      format.setIndenting(true);
      format.setIndent(2);
    }

    return format;
  }

  protected HTMLSerializer getHTMLSerializer(Writer w, OutputFormat format) {
    boolean useXhtml = "true".equals(this.policy.getDirective("useXHTML"));

    if (useXhtml) {
      return new ASXHTMLSerializer(w, format, this.policy);
    }

    return new ASHTMLSerializer(w, format, this.policy);
  }

  protected String trim(String original, String cleaned) {
    if ((cleaned.endsWith("\n")) && 
      (!original.endsWith("\n"))) {
      if (cleaned.endsWith("\r\n"))
        cleaned = cleaned.substring(0, cleaned.length() - 2);
      else if (cleaned.endsWith("\n")) {
        cleaned = cleaned.substring(0, cleaned.length() - 1);
      }

    }

    return cleaned;
  }
}