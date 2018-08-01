package org.activiti.engine.impl.bpmn.parser;

import java.io.InputStream;
import java.net.URL;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.BoundaryEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.BusinessRuleTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.CallActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ErrorEndEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.InclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.IntermediateCatchEventActivitiBehaviour;
import org.activiti.engine.impl.bpmn.behavior.MailActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ManualTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.ReceiveTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ScriptTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.ServiceTaskDelegateExpressionActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ServiceTaskExpressionActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.TaskActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.WebServiceActivityBehavior;
import org.activiti.engine.impl.bpmn.data.AbstractDataAssociation;
import org.activiti.engine.impl.bpmn.data.Assignment;
import org.activiti.engine.impl.bpmn.data.ClassStructureDefinition;
import org.activiti.engine.impl.bpmn.data.Data;
import org.activiti.engine.impl.bpmn.data.DataRef;
import org.activiti.engine.impl.bpmn.data.IOSpecification;
import org.activiti.engine.impl.bpmn.data.ItemDefinition;
import org.activiti.engine.impl.bpmn.data.ItemKind;
import org.activiti.engine.impl.bpmn.data.SimpleDataInputAssociation;
import org.activiti.engine.impl.bpmn.data.StructureDefinition;
import org.activiti.engine.impl.bpmn.data.TransformationDataOutputAssociation;
import org.activiti.engine.impl.bpmn.helper.ClassDelegate;
import org.activiti.engine.impl.bpmn.listener.DelegateExpressionExecutionListener;
import org.activiti.engine.impl.bpmn.listener.DelegateExpressionTaskListener;
import org.activiti.engine.impl.bpmn.listener.ExpressionExecutionListener;
import org.activiti.engine.impl.bpmn.listener.ExpressionTaskListener;
import org.activiti.engine.impl.bpmn.webservice.BpmnInterface;
import org.activiti.engine.impl.bpmn.webservice.BpmnInterfaceImplementation;
import org.activiti.engine.impl.bpmn.webservice.MessageDefinition;
import org.activiti.engine.impl.bpmn.webservice.MessageImplicitDataInputAssociation;
import org.activiti.engine.impl.bpmn.webservice.MessageImplicitDataOutputAssociation;
import org.activiti.engine.impl.bpmn.webservice.Operation;
import org.activiti.engine.impl.bpmn.webservice.OperationImplementation;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.el.FixedValue;
import org.activiti.engine.impl.el.UelExpressionCondition;
import org.activiti.engine.impl.form.DefaultStartFormHandler;
import org.activiti.engine.impl.form.DefaultTaskFormHandler;
import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.jobexecutor.TimerDeclarationImpl;
import org.activiti.engine.impl.jobexecutor.TimerDeclarationType;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.impl.util.xml.Attribute;
import org.activiti.engine.impl.util.xml.Element;
import org.activiti.engine.impl.util.xml.Parse;
import org.activiti.engine.impl.variable.VariableDeclaration;

public class BpmnParse extends Parse
{
  protected static final Logger LOGGER = Logger.getLogger(BpmnParse.class.getName());
  public static final String PROPERTYNAME_DOCUMENTATION = "documentation";
  public static final String PROPERTYNAME_INITIAL = "initial";
  public static final String PROPERTYNAME_INITIATOR_VARIABLE_NAME = "initiatorVariableName";
  public static final String PROPERTYNAME_CONDITION = "condition";
  public static final String PROPERTYNAME_CONDITION_TEXT = "conditionText";
  public static final String PROPERTYNAME_VARIABLE_DECLARATIONS = "variableDeclarations";
  public static final String PROPERTYNAME_TIMER_DECLARATION = "timerDeclarations";
  public static final String PROPERTYNAME_ISEXPANDED = "isExpanded";
  public static final String PROPERTYNAME_START_TIMER = "timerStart";
  protected DeploymentEntity deployment;
  protected List<ProcessDefinitionEntity> processDefinitions = new ArrayList();

  protected Map<String, Error> errors = new HashMap();
  protected Map<String, TransitionImpl> sequenceFlows;
  protected Map<String, MessageDefinition> messages = new HashMap();
  protected Map<String, StructureDefinition> structures = new HashMap();
  protected Map<String, BpmnInterfaceImplementation> interfaceImplementations = new HashMap();
  protected Map<String, OperationImplementation> operationImplementations = new HashMap();
  protected Map<String, ItemDefinition> itemDefinitions = new HashMap();
  protected Map<String, BpmnInterface> bpmnInterfaces = new HashMap();
  protected Map<String, Operation> operations = new HashMap();
  protected ExpressionManager expressionManager;
  protected List<BpmnParseListener> parseListeners;
  protected Map<String, XMLImporter> importers = new HashMap();
  protected Map<String, String> prefixs = new HashMap();
  protected String targetNamespace;
  protected static final String HUMAN_PERFORMER = "humanPerformer";
  protected static final String POTENTIAL_OWNER = "potentialOwner";
  protected static final String RESOURCE_ASSIGNMENT_EXPR = "resourceAssignmentExpression";
  protected static final String FORMAL_EXPRESSION = "formalExpression";
  protected static final String USER_PREFIX = "user(";
  protected static final String GROUP_PREFIX = "group(";
  protected static final String ASSIGNEE_EXTENSION = "assignee";
  protected static final String CANDIDATE_USERS_EXTENSION = "candidateUsers";
  protected static final String CANDIDATE_GROUPS_EXTENSION = "candidateGroups";
  protected static final String DUE_DATE_EXTENSION = "dueDate";

  BpmnParse(BpmnParser parser)
  {
    super(parser);
    this.expressionManager = parser.getExpressionManager();
    this.parseListeners = parser.getParseListeners();
    setSchemaResource(ReflectUtil.getResource("org/activiti/engine/impl/bpmn/parser/BPMN20.xsd").toString());
    initializeXSDItemDefinitions();
  }

  protected void initializeXSDItemDefinitions() {
    this.itemDefinitions.put("http://www.w3.org/2001/XMLSchema:string", new ItemDefinition("http://www.w3.org/2001/XMLSchema:string", 
      new ClassStructureDefinition(String.class)));
  }

  public BpmnParse deployment(DeploymentEntity deployment) {
    this.deployment = deployment;
    return this;
  }

  public BpmnParse execute()
  {
    super.execute();
    try
    {
      parseRootElement();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Uknown exception", e);
    } finally {
      if (hasWarnings()) {
        logWarnings();
      }
      if (hasErrors()) {
        throwActivitiExceptionForErrors();
      }
    }

    return this;
  }

  protected void parseRootElement()
  {
    parseDefinitionsAttributes();
    parseImports();
    parseItemDefinitions();
    parseMessages();
    parseInterfaces();
    parseErrors();
    parseProcessDefinitions();

    parseDiagramInterchangeElements();

    for (BpmnParseListener parseListener : this.parseListeners)
      parseListener.parseRootElement(this.rootElement, getProcessDefinitions());
  }

  protected void parseDefinitionsAttributes()
  {
    String typeLanguage = this.rootElement.attribute("typeLanguage");
    String expressionLanguage = this.rootElement.attribute("expressionLanguage");
    this.targetNamespace = this.rootElement.attribute("targetNamespace");
/*
    if ((typeLanguage != null) && 
      (typeLanguage.contains("XMLSchema"))) {
     // LOGGER.info("XMLSchema currently not supported as typeLanguage");
    }

    if ((expressionLanguage != null) && 
      (expressionLanguage.contains("XPath"))) {
    //  LOGGER.info("XPath currently not supported as expressionLanguage");
    }*/

    for (String attribute : this.rootElement.attributes())
      if (attribute.startsWith("xmlns:")) {
        String prefixValue = this.rootElement.attribute(attribute);
        String prefixName = attribute.substring(6);
        this.prefixs.put(prefixName, prefixValue);
      }
  }

  protected String resolveName(String name)
  {
    if (name == null) {
      return null;
    }
    int indexOfP = name.indexOf(':');
    if (indexOfP != -1) {
      String prefix = name.substring(0, indexOfP);
      String resolvedPrefix = (String)this.prefixs.get(prefix);
      return resolvedPrefix + ":" + name.substring(indexOfP + 1);
    }
    return name;
  }

  protected void parseImports()
  {
    List<Element> imports = this.rootElement.elements("import");
    for (Element theImport : imports) {
      String importType = theImport.attribute("importType");
      XMLImporter importer = getImporter(importType, theImport);
      if (importer == null)
        addError("Could not import item of type " + importType, theImport);
      else
        importer.importFrom(theImport, this);
    }
  }

  protected XMLImporter getImporter(String importType, Element theImport)
  {
    if (this.importers.containsKey(importType)) {
      return (XMLImporter)this.importers.get(importType);
    }
    if (importType.equals("http://schemas.xmlsoap.org/wsdl/")) {
      try
      {
        Class wsdlImporterClass = Class.forName("org.activiti.engine.impl.webservice.CxfWSDLImporter", true, Thread.currentThread().getContextClassLoader());
        XMLImporter newInstance = (XMLImporter)wsdlImporterClass.newInstance();
        this.importers.put(importType, newInstance);
        return newInstance;
      } catch (Exception e) {
        addError("Could not find importer for type " + importType, theImport);
      }
    }
    return null;
  }

  public void parseItemDefinitions()
  {
    for (Element itemDefinitionElement : this.rootElement.elements("itemDefinition")) {
      String id = itemDefinitionElement.attribute("id");
      String structureRef = resolveName(itemDefinitionElement.attribute("structureRef"));
      String itemKind = itemDefinitionElement.attribute("itemKind");
      StructureDefinition structure = null;
      try
      {
        Class classStructure = ReflectUtil.loadClass(structureRef);
        structure = new ClassStructureDefinition(classStructure);
      }
      catch (ActivitiException e) {
        structure = (StructureDefinition)this.structures.get(structureRef);
      }

      ItemDefinition itemDefinition = new ItemDefinition(this.targetNamespace + ":" + id, structure);
      if (itemKind != null) {
        itemDefinition.setItemKind(ItemKind.valueOf(itemKind));
      }
      this.itemDefinitions.put(itemDefinition.getId(), itemDefinition);
    }
  }

  public void parseMessages()
  {
    for (Element messageElement : this.rootElement.elements("message")) {
      String id = messageElement.attribute("id");
      String itemRef = resolveName(messageElement.attribute("itemRef"));

      if (!this.itemDefinitions.containsKey(itemRef)) {
        addError(itemRef + " does not exist", messageElement);
      } else {
        ItemDefinition itemDefinition = (ItemDefinition)this.itemDefinitions.get(itemRef);
        MessageDefinition message = new MessageDefinition(this.targetNamespace + ":" + id, itemDefinition);
        this.messages.put(message.getId(), message);
      }
    }
  }

  public void parseInterfaces()
  {
    for (Element interfaceElement : this.rootElement.elements("interface"))
    {
      String id = interfaceElement.attribute("id");
      String name = interfaceElement.attribute("name");
      String implementationRef = resolveName(interfaceElement.attribute("implementationRef"));
      BpmnInterface bpmnInterface = new BpmnInterface(this.targetNamespace + ":" + id, name);
      bpmnInterface.setImplementation((BpmnInterfaceImplementation)this.interfaceImplementations.get(implementationRef));

      for (Element operationElement : interfaceElement.elements("operation")) {
        Operation operation = parseOperation(operationElement, bpmnInterface);
        bpmnInterface.addOperation(operation);
      }

      this.bpmnInterfaces.put(bpmnInterface.getId(), bpmnInterface);
    }
  }

  public Operation parseOperation(Element operationElement, BpmnInterface bpmnInterface) {
    Element inMessageRefElement = operationElement.element("inMessageRef");
    String inMessageRef = resolveName(inMessageRefElement.getText());

    if (!this.messages.containsKey(inMessageRef)) {
      addError(inMessageRef + " does not exist", inMessageRefElement);
      return null;
    }
    MessageDefinition inMessage = (MessageDefinition)this.messages.get(inMessageRef);
    String id = operationElement.attribute("id");
    String name = operationElement.attribute("name");
    String implementationRef = resolveName(operationElement.attribute("implementationRef"));
    Operation operation = new Operation(this.targetNamespace + ":" + id, name, bpmnInterface, inMessage);
    operation.setImplementation((OperationImplementation)this.operationImplementations.get(implementationRef));

    Element outMessageRefElement = operationElement.element("outMessageRef");
    if (outMessageRefElement != null) {
      String outMessageRef = resolveName(outMessageRefElement.getText());
      if (this.messages.containsKey(outMessageRef)) {
        MessageDefinition outMessage = (MessageDefinition)this.messages.get(outMessageRef);
        operation.setOutMessage(outMessage);
      }
    }

    this.operations.put(operation.getId(), operation);
    return operation;
  }

  public void parseErrors()
  {
    for (Element errorElement : this.rootElement.elements("error")) {
      Error error = new Error();

      String id = errorElement.attribute("id");
      if (id == null) {
        addError("'id' is mandatory on error definition", errorElement);
      }
      error.setId(id);

      String errorCode = errorElement.attribute("errorCode");
      if (errorCode == null) {
        addError("'errorCode' is mandatory on error definition", errorElement);
      }
      error.setErrorCode(errorCode);

      this.errors.put(id, error);
    }
  }

  public void parseProcessDefinitions()
  {
    for (Element processElement : this.rootElement.elements("process"))
      this.processDefinitions.add(parseProcess(processElement));
  }

  public ProcessDefinitionEntity parseProcess(Element processElement)
  {
    this.sequenceFlows = new HashMap();

    ProcessDefinitionEntity processDefinition = new ProcessDefinitionEntity();

    processDefinition.setKey(processElement.attribute("id"));
    processDefinition.setName(processElement.attribute("name"));
    processDefinition.setCategory(this.rootElement.attribute("targetNamespace"));
    processDefinition.setProperty("documentation", parseDocumentation(processElement));
    processDefinition.setTaskDefinitions(new HashMap());
    processDefinition.setDeploymentId(this.deployment.getId());

   // if (LOGGER.isLoggable(Level.FINE)) {
    //  LOGGER.fine("Parsing process " + processDefinition.getKey());
  //  }
    parseScope(processElement, processDefinition);

    for (BpmnParseListener parseListener : this.parseListeners) {
      parseListener.parseProcess(processElement, processDefinition);
    }

    return processDefinition;
  }

  public void parseScope(Element scopeElement, ScopeImpl parentScope)
  {
    parseStartEvents(scopeElement, parentScope);
    parseActivities(scopeElement, parentScope);
    parseAddSignActivities(scopeElement, parentScope);
    parseEndEvents(scopeElement, parentScope);
    parseBoundaryEvents(scopeElement, parentScope);
    parseSequenceFlow(scopeElement, parentScope);
    parseExecutionListenersOnScope(scopeElement, parentScope);

    IOSpecification ioSpecification = parseIOSpecification(scopeElement.element("ioSpecification"));
    parentScope.setIoSpecification(ioSpecification);
  }

  protected IOSpecification parseIOSpecification(Element ioSpecificationElement)
  {
      if(ioSpecificationElement == null)
          return null;
      IOSpecification ioSpecification = new IOSpecification();
      Data dataInput;
      for(Iterator iterator = ioSpecificationElement.elements("dataInput").iterator(); iterator.hasNext(); ioSpecification.addInput(dataInput))
      {
          Element dataInputElement = (Element)iterator.next();
          String id = dataInputElement.attribute("id");
          String itemSubjectRef = resolveName(dataInputElement.attribute("itemSubjectRef"));
          ItemDefinition itemDefinition = (ItemDefinition)itemDefinitions.get(itemSubjectRef);
          dataInput = new Data((new StringBuilder(String.valueOf(targetNamespace))).append(":").append(id).toString(), id, itemDefinition);
      }

      Data dataOutput;
      for(Iterator iterator1 = ioSpecificationElement.elements("dataOutput").iterator(); iterator1.hasNext(); ioSpecification.addOutput(dataOutput))
      {
          Element dataOutputElement = (Element)iterator1.next();
          String id = dataOutputElement.attribute("id");
          String itemSubjectRef = resolveName(dataOutputElement.attribute("itemSubjectRef"));
          ItemDefinition itemDefinition = (ItemDefinition)itemDefinitions.get(itemSubjectRef);
          dataOutput = new Data((new StringBuilder(String.valueOf(targetNamespace))).append(":").append(id).toString(), id, itemDefinition);
      }

      for(Iterator iterator2 = ioSpecificationElement.elements("inputSet").iterator(); iterator2.hasNext();)
      {
          Element inputSetElement = (Element)iterator2.next();
          DataRef dataRef;
          for(Iterator iterator4 = inputSetElement.elements("dataInputRefs").iterator(); iterator4.hasNext(); ioSpecification.addInputRef(dataRef))
          {
              Element dataInputRef = (Element)iterator4.next();
              dataRef = new DataRef(dataInputRef.getText());
          }

      }

      for(Iterator iterator3 = ioSpecificationElement.elements("outputSet").iterator(); iterator3.hasNext();)
      {
          Element outputSetElement = (Element)iterator3.next();
          DataRef dataRef;
          for(Iterator iterator5 = outputSetElement.elements("dataOutputRefs").iterator(); iterator5.hasNext(); ioSpecification.addOutputRef(dataRef))
          {
              Element dataInputRef = (Element)iterator5.next();
              dataRef = new DataRef(dataInputRef.getText());
          }

      }

      return ioSpecification;
  }

  protected AbstractDataAssociation parseDataInputAssociation(Element dataAssociationElement) {
    String sourceRef = dataAssociationElement.element("sourceRef").getText();
    String targetRef = dataAssociationElement.element("targetRef").getText();

    List assignments = dataAssociationElement.elements("assignment");
    if (assignments.isEmpty()) {
      return new MessageImplicitDataInputAssociation(sourceRef, targetRef);
    }
    SimpleDataInputAssociation dataAssociation = new SimpleDataInputAssociation(sourceRef, targetRef);

    for (Element assigmentElement : dataAssociationElement.elements("assignment")) {
      Expression from = this.expressionManager.createExpression(assigmentElement.element("from").getText());
      Expression to = this.expressionManager.createExpression(assigmentElement.element("to").getText());
      Assignment assignment = new Assignment(from, to);
      dataAssociation.addAssignment(assignment);
    }

    return dataAssociation;
  }

  public void parseStartEvents(Element parentElement, ScopeImpl scope)
  {
      List startEventElements = parentElement.elements("startEvent");
      if(startEventElements.size() > 1)
          addError("Multiple start events are currently unsupported", parentElement);
      else
      if(startEventElements.size() > 0)
      {
          Element startEventElement = (Element)startEventElements.get(0);
          ActivityImpl startEventActivity = createActivityOnScope(startEventElement, scope);
          if(scope instanceof ProcessDefinitionEntity)
          {
              ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)scope;
              if(processDefinition.getInitial() != null)
                  addError("multiple startEvents in a process definition are not yet supported", startEventElement);
              processDefinition.setInitial(startEventActivity);
              String startFormHandlerClassName = startEventElement.attributeNS("http://activiti.org/bpmn", "formHandlerClass");
              StartFormHandler startFormHandler;
              if(startFormHandlerClassName != null)
                  startFormHandler = (StartFormHandler)ReflectUtil.instantiate(startFormHandlerClassName);
              else
                  startFormHandler = new DefaultStartFormHandler();
              startFormHandler.parseConfiguration(startEventElement, deployment, processDefinition, this);
              processDefinition.setStartFormHandler(startFormHandler);
              String initiatorVariableName = startEventElement.attributeNS("http://activiti.org/bpmn", "initiator");
              if(initiatorVariableName != null)
                  processDefinition.setProperty("initiatorVariableName", initiatorVariableName);
              Element timerEventDefinition = startEventElement.element("timerEventDefinition");
              if(timerEventDefinition != null)
                  parseTimerStartEventDefinition(timerEventDefinition, startEventActivity, processDefinition);
          } else
          {
              scope.setProperty("initial", startEventActivity);
          }
          startEventActivity.setActivityBehavior(new NoneStartEventActivityBehavior());
          BpmnParseListener parseListener;
          for(Iterator iterator = parseListeners.iterator(); iterator.hasNext(); parseListener.parseStartEvent(startEventElement, scope, startEventActivity))
              parseListener = (BpmnParseListener)iterator.next();

      }
  }

  public void parseActivities(Element parentElement, ScopeImpl scopeElement)
  {
    for (Element activityElement : parentElement.elements()) {
      ActivityImpl activity = null;
      if (activityElement.getTagName().equals("exclusiveGateway"))
        activity = parseExclusiveGateway(activityElement, scopeElement);
      else if (activityElement.getTagName().equals("inclusiveGateway"))
        activity = parseInclusiveGateway(activityElement, scopeElement);
      else if (activityElement.getTagName().equals("parallelGateway"))
        activity = parseParallelGateway(activityElement, scopeElement);
      else if (activityElement.getTagName().equals("scriptTask"))
        activity = parseScriptTask(activityElement, scopeElement);
      else if (activityElement.getTagName().equals("serviceTask"))
        activity = parseServiceTask(activityElement, scopeElement);
      else if (activityElement.getTagName().equals("businessRuleTask"))
        activity = parseBusinessRuleTask(activityElement, scopeElement);
      else if (activityElement.getTagName().equals("task"))
        activity = parseTask(activityElement, scopeElement);
      else if (activityElement.getTagName().equals("manualTask"))
        activity = parseManualTask(activityElement, scopeElement);
      else if (activityElement.getTagName().equals("userTask"))
        activity = parseUserTask(activityElement, scopeElement);
      else if (activityElement.getTagName().equals("sendTask"))
        activity = parseSendTask(activityElement, scopeElement);
      else if (activityElement.getTagName().equals("receiveTask"))
        activity = parseReceiveTask(activityElement, scopeElement);
      else if (activityElement.getTagName().equals("subProcess"))
        activity = parseSubProcess(activityElement, scopeElement);
      else if (activityElement.getTagName().equals("callActivity"))
        activity = parseCallActivity(activityElement, scopeElement);
      else if (activityElement.getTagName().equals("intermediateCatchEvent"))
        activity = parseIntermediateCatchEvent(activityElement, scopeElement);
      else if ((activityElement.getTagName().equals("adHocSubProcess")) || (activityElement.getTagName().equals("complexGateway")) || 
        (activityElement.getTagName().equals("eventBasedGateway")) || (activityElement.getTagName().equals("transaction"))) {
        addWarning("Ignoring unsupported activity type", activityElement);
      }

      if (activity != null)
        parseMultiInstanceLoopCharacteristics(activityElement, activity);
    }
  }

  private void parseAddSignActivities(Element parentElement, ScopeImpl scopeElement)
  {
    ActivityImpl activity = null;
    Element activityElement = null;
    for (Element item : parentElement.elements())
      if (item.getTagName().equals("userTask"))
      {
        String id = "999999";
        item.setAttribute("id", new Attribute("id", id));
        item.setAttribute("name", new Attribute("name", "加签"));
        item.setAttribute("documentation", new Attribute("documentation", null));
        item.setAttribute("default", new Attribute("default", null));
        activity = parseAddSignTask(item, scopeElement);
        if (activity == null) break;
        parseMultiInstanceLoopCharacteristics(item, activity);

        break;
      }
  }

  private ActivityImpl parseIntermediateCatchEvent(Element intermediateEventElement, ScopeImpl scopeElement)
  {
    ActivityImpl nestedActivity = createActivityOnScope(intermediateEventElement, scopeElement);

    nestedActivity.setActivityBehavior(new IntermediateCatchEventActivitiBehaviour());

    Element timerEventDefinition = intermediateEventElement.element("timerEventDefinition");
    if (timerEventDefinition != null)
      parseIntemediateTimerEventDefinition(timerEventDefinition, nestedActivity);
    else {
      addError("Unsupported intermediate event type", intermediateEventElement);
    }
    return nestedActivity;
  }

  public void parseMultiInstanceLoopCharacteristics(Element activityElement, ActivityImpl activity)
  {
    if (!(activity.getActivityBehavior() instanceof AbstractBpmnActivityBehavior)) {
      return;
    }

    Element miLoopCharacteristics = activityElement.element("multiInstanceLoopCharacteristics");
    if (miLoopCharacteristics != null)
    {
      MultiInstanceActivityBehavior miActivityBehavior = null;
      boolean isSequential = parseBooleanAttribute(miLoopCharacteristics.attribute("isSequential"), false).booleanValue();
      if (isSequential)
        miActivityBehavior = new SequentialMultiInstanceBehavior(activity, (AbstractBpmnActivityBehavior)activity.getActivityBehavior());
      else {
        miActivityBehavior = new ParallelMultiInstanceBehavior(activity, (AbstractBpmnActivityBehavior)activity.getActivityBehavior());
      }
      activity.setScope(true);
      activity.setProperty("multiInstance", isSequential ? "sequential" : "parallel");
      activity.setActivityBehavior(miActivityBehavior);

      Element loopCardinality = miLoopCharacteristics.element("loopCardinality");
      if (loopCardinality != null) {
        String loopCardinalityText = loopCardinality.getText();
        if ((loopCardinalityText == null) || ("".equals(loopCardinalityText))) {
          addError("loopCardinality must be defined for a multiInstanceLoopCharacteristics definition ", miLoopCharacteristics);
        }
        miActivityBehavior.setLoopCardinalityExpression(this.expressionManager.createExpression(loopCardinalityText));
      }

      Element completionCondition = miLoopCharacteristics.element("completionCondition");
      if (completionCondition != null) {
        String completionConditionText = completionCondition.getText();
        miActivityBehavior.setCompletionConditionExpression(this.expressionManager.createExpression(completionConditionText));
      }

      String collection = miLoopCharacteristics.attributeNS("http://activiti.org/bpmn", "collection");
      if (collection != null) {
        if (collection.contains("{"))
          miActivityBehavior.setCollectionExpression(this.expressionManager.createExpression(collection));
        else {
          miActivityBehavior.setCollectionVariable(collection);
        }

      }

      Element loopDataInputRef = miLoopCharacteristics.element("loopDataInputRef");
      if (loopDataInputRef != null) {
        String loopDataInputRefText = loopDataInputRef.getText();
        if (loopDataInputRefText != null) {
          if (loopDataInputRefText.contains("{"))
            miActivityBehavior.setCollectionExpression(this.expressionManager.createExpression(loopDataInputRefText));
          else {
            miActivityBehavior.setCollectionVariable(loopDataInputRefText);
          }
        }

      }

      String elementVariable = miLoopCharacteristics.attributeNS("http://activiti.org/bpmn", "elementVariable");
      if (elementVariable != null) {
        miActivityBehavior.setCollectionElementVariable(elementVariable);
      }

      Element inputDataItem = miLoopCharacteristics.element("inputDataItem");
      if (inputDataItem != null) {
        String inputDataItemName = inputDataItem.attribute("name");
        miActivityBehavior.setCollectionElementVariable(inputDataItemName);
      }

      if ((miActivityBehavior.getLoopCardinalityExpression() == null) && (miActivityBehavior.getCollectionExpression() == null) && 
        (miActivityBehavior.getCollectionVariable() == null)) {
        addError("Either loopCardinality or loopDataInputRef/activiti:collection must been set", miLoopCharacteristics);
      }

      if ((miActivityBehavior.getCollectionExpression() == null) && (miActivityBehavior.getCollectionVariable() == null) && 
        (miActivityBehavior.getCollectionElementVariable() != null)) {
        addError("LoopDataInputRef/activiti:collection must be set when using inputDataItem or activiti:elementVariable", miLoopCharacteristics);
      }

      for (BpmnParseListener parseListener : this.parseListeners)
        parseListener.parseMultiInstanceLoopCharacteristics(activityElement, miLoopCharacteristics, activity);
    }
  }

  public ActivityImpl createActivityOnScope(Element activityElement, ScopeImpl scopeElement)
  {
    String id = activityElement.attribute("id");
  //  if (LOGGER.isLoggable(Level.FINE)) {
    //  LOGGER.fine("Parsing activity " + id);
   // }
    ActivityImpl activity = scopeElement.createActivity(id);

    activity.setProperty("name", activityElement.attribute("name"));
    activity.setProperty("documentation", parseDocumentation(activityElement));
    activity.setProperty("default", activityElement.attribute("default"));
    activity.setProperty("type", activityElement.getTagName());
    activity.setProperty("line", Integer.valueOf(activityElement.getLine()));
    return activity;
  }

  public String parseDocumentation(Element element) {
    Element docElement = element.element("documentation");
    if (docElement != null) {
      return docElement.getText().trim();
    }
    return null;
  }

  public ActivityImpl parseExclusiveGateway(Element exclusiveGwElement, ScopeImpl scope)
  {
    ActivityImpl activity = createActivityOnScope(exclusiveGwElement, scope);
    activity.setActivityBehavior(new ExclusiveGatewayActivityBehavior());

    parseExecutionListenersOnScope(exclusiveGwElement, activity);

    for (BpmnParseListener parseListener : this.parseListeners) {
      parseListener.parseExclusiveGateway(exclusiveGwElement, scope, activity);
    }
    return activity;
  }

  public ActivityImpl parseInclusiveGateway(Element inclusiveGwElement, ScopeImpl scope)
  {
    ActivityImpl activity = createActivityOnScope(inclusiveGwElement, scope);
    activity.setActivityBehavior(new InclusiveGatewayActivityBehavior());

    parseExecutionListenersOnScope(inclusiveGwElement, activity);

    for (BpmnParseListener parseListener : this.parseListeners) {
      parseListener.parseInclusiveGateway(inclusiveGwElement, scope, activity);
    }
    return activity;
  }

  public ActivityImpl parseParallelGateway(Element parallelGwElement, ScopeImpl scope)
  {
    ActivityImpl activity = createActivityOnScope(parallelGwElement, scope);
    activity.setActivityBehavior(new ParallelGatewayActivityBehavior());

    parseExecutionListenersOnScope(parallelGwElement, activity);

    for (BpmnParseListener parseListener : this.parseListeners) {
      parseListener.parseParallelGateway(parallelGwElement, scope, activity);
    }
    return activity;
  }

  public ActivityImpl parseScriptTask(Element scriptTaskElement, ScopeImpl scope)
  {
    ActivityImpl activity = createActivityOnScope(scriptTaskElement, scope);

    String script = null;
    String language = null;
    String resultVariableName = null;

    Element scriptElement = scriptTaskElement.element("script");
    if (scriptElement != null) {
      script = scriptElement.getText();

      if (language == null) {
        language = scriptTaskElement.attribute("scriptFormat");
      }

      if (language == null) {
        language = "juel";
      }

      resultVariableName = scriptTaskElement.attributeNS("http://activiti.org/bpmn", "resultVariable");
      if (resultVariableName == null)
      {
        resultVariableName = scriptTaskElement.attributeNS("http://activiti.org/bpmn", "resultVariableName");
      }
    }

    String async = scriptTaskElement.attributeNS("http://activiti.org/bpmn", "async");
    activity.setAsync("true".equals(async));

    activity.setActivityBehavior(new ScriptTaskActivityBehavior(script, language, resultVariableName));

    parseExecutionListenersOnScope(scriptTaskElement, activity);

    for (BpmnParseListener parseListener : this.parseListeners) {
      parseListener.parseScriptTask(scriptTaskElement, scope, activity);
    }
    return activity;
  }

  public ActivityImpl parseServiceTask(Element serviceTaskElement, ScopeImpl scope)
  {
    ActivityImpl activity = createActivityOnScope(serviceTaskElement, scope);

    String type = serviceTaskElement.attributeNS("http://activiti.org/bpmn", "type");
    String className = serviceTaskElement.attributeNS("http://activiti.org/bpmn", "class");
    String expression = serviceTaskElement.attributeNS("http://activiti.org/bpmn", "expression");
    String delegateExpression = serviceTaskElement.attributeNS("http://activiti.org/bpmn", "delegateExpression");
    String resultVariableName = serviceTaskElement.attributeNS("http://activiti.org/bpmn", "resultVariable");
    String async = serviceTaskElement.attributeNS("http://activiti.org/bpmn", "async");
    if (resultVariableName == null) {
      resultVariableName = serviceTaskElement.attributeNS("http://activiti.org/bpmn", "resultVariableName");
    }
    String implementation = serviceTaskElement.attribute("implementation");
    String operationRef = resolveName(serviceTaskElement.attribute("operationRef"));

    activity.setAsync("true".equals(async));
    WebServiceActivityBehavior webServiceActivityBehavior;
    if (type != null) {
      if (type.equalsIgnoreCase("mail"))
        parseEmailServiceTask(activity, serviceTaskElement, parseFieldDeclarations(serviceTaskElement));
      else if (type.equalsIgnoreCase("mule"))
        parseMuleServiceTask(activity, serviceTaskElement, parseFieldDeclarations(serviceTaskElement));
      else {
        addError("Invalid usage of type attribute: '" + type + "'", serviceTaskElement);
      }
    }
    else if ((className != null) && (className.trim().length() > 0)) {
      if (resultVariableName != null) {
        addError("'resultVariableName' not supported for service tasks using 'class'", serviceTaskElement);
      }
      activity.setActivityBehavior(new ClassDelegate(className, parseFieldDeclarations(serviceTaskElement)));
    }
    else if (delegateExpression != null) {
      if (resultVariableName != null) {
        addError("'resultVariableName' not supported for service tasks using 'delegateExpression'", serviceTaskElement);
      }
      activity.setActivityBehavior(new ServiceTaskDelegateExpressionActivityBehavior(this.expressionManager.createExpression(delegateExpression)));
    }
    else if ((expression != null) && (expression.trim().length() > 0)) {
      activity.setActivityBehavior(new ServiceTaskExpressionActivityBehavior(this.expressionManager.createExpression(expression), resultVariableName));
    }
    else if ((implementation != null) && (operationRef != null) && (implementation.equalsIgnoreCase("##WebService"))) {
      if (!this.operations.containsKey(operationRef)) {
        addError(operationRef + " does not exist", serviceTaskElement);
      } else {
        Operation operation = (Operation)this.operations.get(operationRef);
        webServiceActivityBehavior = new WebServiceActivityBehavior(operation);

        Element ioSpecificationElement = serviceTaskElement.element("ioSpecification");
        if (ioSpecificationElement != null) {
          IOSpecification ioSpecification = parseIOSpecification(ioSpecificationElement);
          webServiceActivityBehavior.setIoSpecification(ioSpecification);
        }

        for (Element dataAssociationElement : serviceTaskElement.elements("dataInputAssociation")) {
          AbstractDataAssociation dataAssociation = parseDataInputAssociation(dataAssociationElement);
          webServiceActivityBehavior.addDataInputAssociation(dataAssociation);
        }

        for (Element dataAssociationElement : serviceTaskElement.elements("dataOutputAssociation")) {
          AbstractDataAssociation dataAssociation = parseDataOutputAssociation(dataAssociationElement);
          webServiceActivityBehavior.addDataOutputAssociation(dataAssociation);
        }

        activity.setActivityBehavior(webServiceActivityBehavior);
      }
    } else {
      addError("One of the attributes 'class', 'delegateExpression', 'type', 'operation', or 'expression' is mandatory on serviceTask.", serviceTaskElement);
    }

    parseExecutionListenersOnScope(serviceTaskElement, activity);

    for (BpmnParseListener parseListener : this.parseListeners) {
      parseListener.parseServiceTask(serviceTaskElement, scope, activity);
    }
    return activity;
  }

  public ActivityImpl parseBusinessRuleTask(Element businessRuleTaskElement, ScopeImpl scope)
  {
      ActivityImpl activity = createActivityOnScope(businessRuleTaskElement, scope);
      BusinessRuleTaskActivityBehavior ruleActivity = new BusinessRuleTaskActivityBehavior();
      String ruleVariableInputString = businessRuleTaskElement.attributeNS("http://activiti.org/bpmn", "ruleVariablesInput");
      String rulesString = businessRuleTaskElement.attributeNS("http://activiti.org/bpmn", "rules");
      String excludeString = businessRuleTaskElement.attributeNS("http://activiti.org/bpmn", "exclude");
      String resultVariableNameString = businessRuleTaskElement.attributeNS("http://activiti.org/bpmn", "resultVariable");
      String async = businessRuleTaskElement.attributeNS("http://activiti.org/bpmn", "async");
      activity.setAsync("true".equals(async));
      if(resultVariableNameString == null)
          resultVariableNameString = businessRuleTaskElement.attributeNS("http://activiti.org/bpmn", "resultVariableName");
      if(ruleVariableInputString != null)
      {
          String ruleVariableInputObjects[] = ruleVariableInputString.split(",");
          String as[];
          int k = (as = ruleVariableInputObjects).length;
          for(int i = 0; i < k; i++)
          {
              String ruleVariableInputObject = as[i];
              ruleActivity.addRuleVariableInputIdExpression(expressionManager.createExpression(ruleVariableInputObject.trim()));
          }

      }
      if(rulesString != null)
      {
          String rules[] = rulesString.split(",");
          String as1[];
          int l = (as1 = rules).length;
          for(int j = 0; j < l; j++)
          {
              String rule = as1[j];
              ruleActivity.addRuleIdExpression(expressionManager.createExpression(rule.trim()));
          }

          if(excludeString != null)
          {
              excludeString = excludeString.trim();
              if(!"true".equalsIgnoreCase(excludeString) && !"false".equalsIgnoreCase(excludeString))
                  addError("'exclude' only supports true or false for business rule tasks", businessRuleTaskElement);
              else
                  ruleActivity.setExclude(Boolean.valueOf(excludeString.toLowerCase()).booleanValue());
          }
      } else
      if(excludeString != null)
          addError("'exclude' not supported for business rule tasks not defining 'rules'", businessRuleTaskElement);
      if(resultVariableNameString != null)
      {
          resultVariableNameString = resultVariableNameString.trim();
          if(resultVariableNameString.length() <= 0)
              addError("'resultVariable' must contain a text value for business rule tasks", businessRuleTaskElement);
          else
              ruleActivity.setResultVariable(resultVariableNameString);
      } else
      {
          ruleActivity.setResultVariable("org.activiti.engine.rules.OUTPUT");
      }
      activity.setActivityBehavior(ruleActivity);
      parseExecutionListenersOnScope(businessRuleTaskElement, activity);
      BpmnParseListener parseListener;
      for(Iterator iterator = parseListeners.iterator(); iterator.hasNext(); parseListener.parseBusinessRuleTask(businessRuleTaskElement, scope, activity))
          parseListener = (BpmnParseListener)iterator.next();

      return activity;
  }

  public ActivityImpl parseSendTask(Element sendTaskElement, ScopeImpl scope)
  {
    ActivityImpl activity = createActivityOnScope(sendTaskElement, scope);

    String async = sendTaskElement.attributeNS("http://activiti.org/bpmn", "async");
    activity.setAsync("true".equals(async));

    String type = sendTaskElement.attributeNS("http://activiti.org/bpmn", "type");

    String implementation = sendTaskElement.attribute("implementation");
    String operationRef = resolveName(sendTaskElement.attribute("operationRef"));
    WebServiceActivityBehavior webServiceActivityBehavior;
    if (type != null) {
      if (type.equalsIgnoreCase("mail"))
        parseEmailServiceTask(activity, sendTaskElement, parseFieldDeclarations(sendTaskElement));
      else if (type.equalsIgnoreCase("mule"))
        parseMuleServiceTask(activity, sendTaskElement, parseFieldDeclarations(sendTaskElement));
      else {
        addError("Invalid usage of type attribute: '" + type + "'", sendTaskElement);
      }

    }
    else if ((implementation != null) && (operationRef != null) && (implementation.equalsIgnoreCase("##WebService"))) {
      if (!this.operations.containsKey(operationRef)) {
        addError(operationRef + " does not exist", sendTaskElement);
      } else {
        Operation operation = (Operation)this.operations.get(operationRef);
        webServiceActivityBehavior = new WebServiceActivityBehavior(operation);

        Element ioSpecificationElement = sendTaskElement.element("ioSpecification");
        if (ioSpecificationElement != null) {
          IOSpecification ioSpecification = parseIOSpecification(ioSpecificationElement);
          webServiceActivityBehavior.setIoSpecification(ioSpecification);
        }

        for (Element dataAssociationElement : sendTaskElement.elements("dataInputAssociation")) {
          AbstractDataAssociation dataAssociation = parseDataInputAssociation(dataAssociationElement);
          webServiceActivityBehavior.addDataInputAssociation(dataAssociation);
        }

        for (Element dataAssociationElement : sendTaskElement.elements("dataOutputAssociation")) {
          AbstractDataAssociation dataAssociation = parseDataOutputAssociation(dataAssociationElement);
          webServiceActivityBehavior.addDataOutputAssociation(dataAssociation);
        }

        activity.setActivityBehavior(webServiceActivityBehavior);
      }
    }
    else addError("One of the attributes 'type' or 'operation' is mandatory on sendTask.", sendTaskElement);

    parseExecutionListenersOnScope(sendTaskElement, activity);

    for (BpmnParseListener parseListener : this.parseListeners) {
      parseListener.parseSendTask(sendTaskElement, scope, activity);
    }
    return activity;
  }

  protected AbstractDataAssociation parseDataOutputAssociation(Element dataAssociationElement) {
    String targetRef = dataAssociationElement.element("targetRef").getText();

    if (dataAssociationElement.element("sourceRef") != null) {
      String sourceRef = dataAssociationElement.element("sourceRef").getText();
      return new MessageImplicitDataOutputAssociation(targetRef, sourceRef);
    }
    Expression transformation = this.expressionManager.createExpression(dataAssociationElement.element("transformation").getText());
    AbstractDataAssociation dataOutputAssociation = new TransformationDataOutputAssociation(null, targetRef, transformation);
    return dataOutputAssociation;
  }

  protected void parseMuleServiceTask(ActivityImpl activity, Element serviceTaskElement, List<FieldDeclaration> fieldDeclarations)
  {
    try {
      Class theClass = Class.forName("org.activiti.mule.MuleSendActivitiBehavior");
      activity.setActivityBehavior((ActivityBehavior)ClassDelegate.instantiateDelegate(theClass, fieldDeclarations));
    } catch (ClassNotFoundException e) {
      addError("Could not find org.activiti.mule.MuleSendActivitiBehavior", serviceTaskElement);
    }
  }

  protected void parseEmailServiceTask(ActivityImpl activity, Element serviceTaskElement, List<FieldDeclaration> fieldDeclarations) {
    validateFieldDeclarationsForEmail(serviceTaskElement, fieldDeclarations);
    activity.setActivityBehavior((MailActivityBehavior)ClassDelegate.instantiateDelegate(MailActivityBehavior.class, fieldDeclarations));
  }

  protected void validateFieldDeclarationsForEmail(Element serviceTaskElement, List<FieldDeclaration> fieldDeclarations) {
    boolean toDefined = false;
    boolean textOrHtmlDefined = false;
    for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
      if (fieldDeclaration.getName().equals("to")) {
        toDefined = true;
      }
      if (fieldDeclaration.getName().equals("html")) {
        textOrHtmlDefined = true;
      }
      if (fieldDeclaration.getName().equals("text")) {
        textOrHtmlDefined = true;
      }
    }

    if (!toDefined) {
      addError("No recipient is defined on the mail activity", serviceTaskElement);
    }
    if (!textOrHtmlDefined)
      addError("Text or html field should be provided", serviceTaskElement);
  }

  public List<FieldDeclaration> parseFieldDeclarations(Element element)
  {
    List fieldDeclarations = new ArrayList();

    Element elementWithFieldInjections = element.element("extensionElements");
    if (elementWithFieldInjections == null)
    {
      elementWithFieldInjections = element;
    }
    List<Element> fieldDeclarationElements = elementWithFieldInjections.elementsNS("http://activiti.org/bpmn", "field");
    if ((fieldDeclarationElements != null) && (!fieldDeclarationElements.isEmpty()))
    {
      for (Element fieldDeclarationElement : fieldDeclarationElements) {
        FieldDeclaration fieldDeclaration = parseFieldDeclaration(element, fieldDeclarationElement);
        if (fieldDeclaration != null) {
          fieldDeclarations.add(fieldDeclaration);
        }
      }
    }

    return fieldDeclarations;
  }

  protected FieldDeclaration parseFieldDeclaration(Element serviceTaskElement, Element fieldDeclarationElement) {
    String fieldName = fieldDeclarationElement.attribute("name");

    FieldDeclaration fieldDeclaration = parseStringFieldDeclaration(fieldDeclarationElement, serviceTaskElement, fieldName);
    if (fieldDeclaration == null) {
      fieldDeclaration = parseExpressionFieldDeclaration(fieldDeclarationElement, serviceTaskElement, fieldName);
    }

    if (fieldDeclaration == null) {
      addError("One of the following is mandatory on a field declaration: one of attributes stringValue|expression or one of child elements string|expression", 
        serviceTaskElement);
    }
    return fieldDeclaration;
  }

  protected FieldDeclaration parseStringFieldDeclaration(Element fieldDeclarationElement, Element serviceTaskElement, String fieldName) {
    try {
      String fieldValue = getStringValueFromAttributeOrElement("stringValue", "string", fieldDeclarationElement);
      if (fieldValue != null)
        return new FieldDeclaration(fieldName, Expression.class.getName(), new FixedValue(fieldValue));
    }
    catch (ActivitiException ae) {
      if (ae.getMessage().contains("multiple elements with tag name"))
        addError("Multiple string field declarations found", serviceTaskElement);
      else {
        addError("Error when paring field declarations: " + ae.getMessage(), serviceTaskElement);
      }
    }
    return null;
  }

  protected FieldDeclaration parseExpressionFieldDeclaration(Element fieldDeclarationElement, Element serviceTaskElement, String fieldName) {
    try {
      String expression = getStringValueFromAttributeOrElement("expression", "expression", fieldDeclarationElement);
      if ((expression != null) && (expression.trim().length() > 0))
        return new FieldDeclaration(fieldName, Expression.class.getName(), this.expressionManager.createExpression(expression));
    }
    catch (ActivitiException ae) {
      if (ae.getMessage().contains("multiple elements with tag name"))
        addError("Multiple expression field declarations found", serviceTaskElement);
      else {
        addError("Error when paring field declarations: " + ae.getMessage(), serviceTaskElement);
      }
    }
    return null;
  }

  protected String getStringValueFromAttributeOrElement(String attributeName, String elementName, Element element) {
    String value = null;

    String attributeValue = element.attribute(attributeName);
    Element childElement = element.elementNS("http://activiti.org/bpmn", elementName);
    String stringElementText = null;

    if ((attributeValue != null) && (childElement != null)) {
      addError("Can't use attribute '" + attributeName + "' and element '" + elementName + "' together, only use one", element);
    } else if (childElement != null) {
      stringElementText = childElement.getText();
      if ((stringElementText == null) || (stringElementText.length() == 0)) {
        addError("No valid value found in attribute '" + attributeName + "' nor element '" + elementName + "'", element);
      }
      else
        value = stringElementText;
    }
    else if ((attributeValue != null) && (attributeValue.length() > 0))
    {
      value = attributeValue;
    }

    return value;
  }

  public ActivityImpl parseTask(Element taskElement, ScopeImpl scope)
  {
    ActivityImpl activity = createActivityOnScope(taskElement, scope);
    activity.setActivityBehavior(new TaskActivityBehavior());

    String async = taskElement.attributeNS("http://activiti.org/bpmn", "async");
    activity.setAsync("true".equals(async));

    parseExecutionListenersOnScope(taskElement, activity);

    for (BpmnParseListener parseListener : this.parseListeners) {
      parseListener.parseTask(taskElement, scope, activity);
    }
    return activity;
  }

  public ActivityImpl parseManualTask(Element manualTaskElement, ScopeImpl scope)
  {
    ActivityImpl activity = createActivityOnScope(manualTaskElement, scope);
    activity.setActivityBehavior(new ManualTaskActivityBehavior());

    parseExecutionListenersOnScope(manualTaskElement, activity);

    for (BpmnParseListener parseListener : this.parseListeners) {
      parseListener.parseManualTask(manualTaskElement, scope, activity);
    }
    return activity;
  }

  public ActivityImpl parseReceiveTask(Element receiveTaskElement, ScopeImpl scope)
  {
    ActivityImpl activity = createActivityOnScope(receiveTaskElement, scope);
    activity.setActivityBehavior(new ReceiveTaskActivityBehavior());

    String async = receiveTaskElement.attributeNS("http://activiti.org/bpmn", "async");
    activity.setAsync("true".equals(async));

    parseExecutionListenersOnScope(receiveTaskElement, activity);

    for (BpmnParseListener parseListener : this.parseListeners) {
      parseListener.parseManualTask(receiveTaskElement, scope, activity);
    }
    return activity;
  }

  public ActivityImpl parseUserTask(Element userTaskElement, ScopeImpl scope)
  {
    ActivityImpl activity = createActivityOnScope(userTaskElement, scope);

    String async = userTaskElement.attributeNS("http://activiti.org/bpmn", "async");
    activity.setAsync("true".equals(async));

    TaskDefinition taskDefinition = parseTaskDefinition(userTaskElement, activity.getId(), (ProcessDefinitionEntity)scope.getProcessDefinition());

    UserTaskActivityBehavior userTaskActivity = new UserTaskActivityBehavior(this.expressionManager, taskDefinition);
    activity.setActivityBehavior(userTaskActivity);

    parseProperties(userTaskElement, activity);
    parseExecutionListenersOnScope(userTaskElement, activity);

    for (BpmnParseListener parseListener : this.parseListeners) {
      parseListener.parseUserTask(userTaskElement, scope, activity);
    }
    return activity;
  }

  public ActivityImpl parseAddSignTask(Element userTaskElement, ScopeImpl scope)
  {
    ActivityImpl activity = createActivityOnScope(userTaskElement, scope);
    activity.setAsync(false);
    TaskDefinition taskDefinition = parseTaskDefinition(userTaskElement, activity.getId(), (ProcessDefinitionEntity)scope.getProcessDefinition());
    UserTaskActivityBehavior userTaskActivity = new UserTaskActivityBehavior(this.expressionManager, taskDefinition);
    activity.setActivityBehavior(userTaskActivity);
    parseProperties(userTaskElement, activity);
    parseExecutionListenersOnScope(userTaskElement, activity);
    for (BpmnParseListener parseListener : this.parseListeners) {
      parseListener.parseUserTask(userTaskElement, scope, activity);
    }
    return activity;
  }

  public TaskDefinition parseTaskDefinition(Element taskElement, String taskDefinitionKey, ProcessDefinitionEntity processDefinition)
  {
      String taskFormHandlerClassName = taskElement.attributeNS("http://activiti.org/bpmn", "formHandlerClass");
      TaskFormHandler taskFormHandler;
      if(taskFormHandlerClassName != null)
          taskFormHandler = (TaskFormHandler)ReflectUtil.instantiate(taskFormHandlerClassName);
      else
          taskFormHandler = new DefaultTaskFormHandler();
      taskFormHandler.parseConfiguration(taskElement, deployment, processDefinition, this);
      TaskDefinition taskDefinition = new TaskDefinition(taskFormHandler);
      taskDefinition.setKey(taskDefinitionKey);
      processDefinition.getTaskDefinitions().put(taskDefinitionKey, taskDefinition);
      String name = taskElement.attribute("name");
      if(name != null)
          taskDefinition.setNameExpression(expressionManager.createExpression(name));
      String descriptionStr = parseDocumentation(taskElement);
      if(descriptionStr != null)
          taskDefinition.setDescriptionExpression(expressionManager.createExpression(descriptionStr));
      parseHumanPerformer(taskElement, taskDefinition);
      parsePotentialOwner(taskElement, taskDefinition);
      parseUserTaskCustomExtensions(taskElement, taskDefinition);
      return taskDefinition;
  }

  protected void parseHumanPerformer(Element taskElement, TaskDefinition taskDefinition) {
    List humanPerformerElements = taskElement.elements("humanPerformer");

    if (humanPerformerElements.size() > 1) {
      addError("Invalid task definition: multiple humanPerformer sub elements defined for " + taskDefinition.getNameExpression(), taskElement);
    } else if (humanPerformerElements.size() == 1) {
      Element humanPerformerElement = (Element)humanPerformerElements.get(0);
      if (humanPerformerElement != null)
        parseHumanPerformerResourceAssignment(humanPerformerElement, taskDefinition);
    }
  }

  protected void parsePotentialOwner(Element taskElement, TaskDefinition taskDefinition)
  {
    List<Element> potentialOwnerElements = taskElement.elements("potentialOwner");
    for (Element potentialOwnerElement : potentialOwnerElements)
      parsePotentialOwnerResourceAssignment(potentialOwnerElement, taskDefinition);
  }

  protected void parseHumanPerformerResourceAssignment(Element performerElement, TaskDefinition taskDefinition)
  {
    Element raeElement = performerElement.element("resourceAssignmentExpression");
    if (raeElement != null) {
      Element feElement = raeElement.element("formalExpression");
      if (feElement != null)
        taskDefinition.setAssigneeExpression(this.expressionManager.createExpression(feElement.getText()));
    }
  }

  protected void parsePotentialOwnerResourceAssignment(Element performerElement, TaskDefinition taskDefinition)
  {
    Element raeElement = performerElement.element("resourceAssignmentExpression");
    if (raeElement != null) {
      Element feElement = raeElement.element("formalExpression");
      if (feElement != null) {
        List<String> assignmentExpressions = parseCommaSeparatedList(feElement.getText());
        for (String assignmentExpression : assignmentExpressions) {
          assignmentExpression = assignmentExpression.trim();
          if (assignmentExpression.startsWith("user(")) {
            String userAssignementId = getAssignmentId(assignmentExpression, "user(");
            taskDefinition.addCandidateUserIdExpression(this.expressionManager.createExpression(userAssignementId));
          } else if (assignmentExpression.startsWith("group(")) {
            String groupAssignementId = getAssignmentId(assignmentExpression, "group(");
            taskDefinition.addCandidateGroupIdExpression(this.expressionManager.createExpression(groupAssignementId));
          } else {
            taskDefinition.addCandidateGroupIdExpression(this.expressionManager.createExpression(assignmentExpression));
          }
        }
      }
    }
  }

  protected String getAssignmentId(String expression, String prefix) {
    return expression.substring(prefix.length(), expression.length() - 1).trim();
  }

  protected void parseUserTaskCustomExtensions(Element taskElement, TaskDefinition taskDefinition)
  {
    String assignee = taskElement.attributeNS("http://activiti.org/bpmn", "assignee");
    if (assignee != null) {
      if (taskDefinition.getAssigneeExpression() == null)
        taskDefinition.setAssigneeExpression(this.expressionManager.createExpression(assignee));
      else {
        addError("Invalid usage: duplicate assignee declaration for task " + taskDefinition.getNameExpression(), taskElement);
      }

    }

    String candidateUsersString = taskElement.attributeNS("http://activiti.org/bpmn", "candidateUsers");
    if (candidateUsersString != null) {
      List<String> candidateUsers = parseCommaSeparatedList(candidateUsersString);
      for (String candidateUser : candidateUsers) {
        taskDefinition.addCandidateUserIdExpression(this.expressionManager.createExpression(candidateUser.trim()));
      }

    }

    String candidateGroupsString = taskElement.attributeNS("http://activiti.org/bpmn", "candidateGroups");
    if (candidateGroupsString != null) {
      List<String> candidateGroups = parseCommaSeparatedList(candidateGroupsString);
      for (String candidateGroup : candidateGroups) {
        taskDefinition.addCandidateGroupIdExpression(this.expressionManager.createExpression(candidateGroup.trim()));
      }

    }

    parseTaskListeners(taskElement, taskDefinition);

    String dueDateExpression = taskElement.attributeNS("http://activiti.org/bpmn", "dueDate");
    if (dueDateExpression != null)
      taskDefinition.setDueDateExpression(this.expressionManager.createExpression(dueDateExpression));
  }

  protected List<String> parseCommaSeparatedList(String s)
  {
    List result = new ArrayList();
    if ((s != null) && (!"".equals(s)))
    {
      StringCharacterIterator iterator = new StringCharacterIterator(s);
      char c = iterator.first();

      StringBuilder strb = new StringBuilder();
      boolean insideExpression = false;

      while (c != 65535) {
        if ((c == '{') || (c == '$')) {
          insideExpression = true;
        } else if (c == '}') {
          insideExpression = false;
        } else if ((c == ',') && (!insideExpression)) {
          result.add(strb.toString().trim());
          strb.delete(0, strb.length());
        }

        if ((c != ',') || (insideExpression)) {
          strb.append(c);
        }

        c = iterator.next();
      }

      if (strb.length() > 0) {
        result.add(strb.toString().trim());
      }
    }

    return result;
  }

  protected void parseTaskListeners(Element userTaskElement, TaskDefinition taskDefinition) {
    Element extentionsElement = userTaskElement.element("extensionElements");
    if (extentionsElement != null) {
      List<Element> taskListenerElements = extentionsElement.elementsNS("http://activiti.org/bpmn", "taskListener");
      for (Element taskListenerElement : taskListenerElements) {
        String eventName = taskListenerElement.attribute("event");
        if (eventName != null) {
          if (("create".equals(eventName)) || ("assignment".equals(eventName)) || 
            ("complete".equals(eventName))) {
            TaskListener taskListener = parseTaskListener(taskListenerElement);
            taskDefinition.addTaskListener(eventName, taskListener);
          } else {
            addError("Invalid eventName for taskListener: choose 'create' |'assignment'", userTaskElement);
          }
        }
        else addError("Event is mandatory on taskListener", userTaskElement);
      }
    }
  }

  protected TaskListener parseTaskListener(Element taskListenerElement)
  {
    TaskListener taskListener = null;

    String className = taskListenerElement.attribute("class");
    String expression = taskListenerElement.attribute("expression");
    String delegateExpression = taskListenerElement.attribute("delegateExpression");

    if (className != null)
      taskListener = new ClassDelegate(className, parseFieldDeclarations(taskListenerElement));
    else if (expression != null)
      taskListener = new ExpressionTaskListener(this.expressionManager.createExpression(expression));
    else if (delegateExpression != null)
      taskListener = new DelegateExpressionTaskListener(this.expressionManager.createExpression(delegateExpression));
    else {
      addError("Element 'class' or 'expression' is mandatory on taskListener", taskListenerElement);
    }
    return taskListener;
  }

  public void parseEndEvents(Element parentElement, ScopeImpl scope)
  {
      for(Iterator iterator = parentElement.elements("endEvent").iterator(); iterator.hasNext();)
      {
          Element endEventElement = (Element)iterator.next();
          ActivityImpl activity = createActivityOnScope(endEventElement, scope);
          Element errorEventDefinition = endEventElement.element("errorEventDefinition");
          if(errorEventDefinition != null)
          {
              String errorRef = errorEventDefinition.attribute("errorRef");
              if(errorRef == null || "".equals(errorRef))
              {
                  addError("'errorRef' attribute is mandatory on error end event", errorEventDefinition);
              } else
              {
                  Error error = (Error)errors.get(errorRef);
                  activity.setProperty("type", "errorEndEvent");
                  activity.setActivityBehavior(new ErrorEndEventActivityBehavior(error == null ? errorRef : error.getErrorCode()));
              }
          } else
          {
              activity.setActivityBehavior(new NoneEndEventActivityBehavior());
          }
          BpmnParseListener parseListener;
          for(Iterator iterator1 = parseListeners.iterator(); iterator1.hasNext(); parseListener.parseEndEvent(endEventElement, scope, activity))
              parseListener = (BpmnParseListener)iterator1.next();

      }

  }


  public void parseBoundaryEvents(Element parentElement, ScopeImpl scopeElement)
  {
    for (Element boundaryEventElement : parentElement.elements("boundaryEvent"))
    {
      String attachedToRef = boundaryEventElement.attribute("attachedToRef");
      if ((attachedToRef == null) || (attachedToRef.equals(""))) {
        addError("AttachedToRef is required when using a timerEventDefinition", boundaryEventElement);
      }

      String id = boundaryEventElement.attribute("id");
    //  if (LOGGER.isLoggable(Level.FINE)) {
      //  LOGGER.fine("Parsing boundary event " + id);
    //  }

      ActivityImpl parentActivity = scopeElement.findActivity(attachedToRef);
      if (parentActivity == null) {
        addError("Invalid reference in boundary event. Make sure that the referenced activity is defined in the same scope as the boundary event", 
          boundaryEventElement);
      }
      ActivityImpl nestedActivity = createActivityOnScope(boundaryEventElement, parentActivity);

      String cancelActivity = boundaryEventElement.attribute("cancelActivity", "true");
      boolean interrupting = cancelActivity.equals("true");

      BoundaryEventActivityBehavior behavior = new BoundaryEventActivityBehavior(interrupting);
      nestedActivity.setActivityBehavior(behavior);

      Element timerEventDefinition = boundaryEventElement.element("timerEventDefinition");
      Element errorEventDefinition = boundaryEventElement.element("errorEventDefinition");
      if (timerEventDefinition != null) {
        parseBoundaryTimerEventDefinition(timerEventDefinition, interrupting, nestedActivity);
      } else if (errorEventDefinition != null) {
        interrupting = true;
        parseBoundaryErrorEventDefinition(errorEventDefinition, interrupting, parentActivity, nestedActivity);
      } else {
        addError("Unsupported boundary event type", boundaryEventElement);
      }
    }
  }

  public void parseBoundaryTimerEventDefinition(Element timerEventDefinition, boolean interrupting, ActivityImpl timerActivity)
  {
    timerActivity.setProperty("type", "boundaryTimer");
    TimerDeclarationImpl timerDeclaration = parseTimer(timerEventDefinition, timerActivity, "timer-transition");
    addTimerDeclaration(timerActivity.getParent(), timerDeclaration);

    if ((timerActivity.getParent() instanceof ActivityImpl)) {
      ((ActivityImpl)timerActivity.getParent()).setScope(true);
    }

    for (BpmnParseListener parseListener : this.parseListeners)
      parseListener.parseBoundaryTimerEventDefinition(timerEventDefinition, interrupting, timerActivity);
  }

  private void parseTimerStartEventDefinition(Element timerEventDefinition, ActivityImpl timerActivity, ProcessDefinitionEntity processDefinition)
  {
    timerActivity.setProperty("type", "startTimerEvent");
    TimerDeclarationImpl timerDeclaration = parseTimer(timerEventDefinition, timerActivity, "timer-start-event");
    timerDeclaration.setJobHandlerConfiguration(processDefinition.getKey());

    List timerDeclarations = (List)processDefinition.getProperty("timerStart");
    if (timerDeclarations == null) {
      timerDeclarations = new ArrayList();
      processDefinition.setProperty("timerStart", timerDeclarations);
    }
    timerDeclarations.add(timerDeclaration);
  }

  private void parseIntemediateTimerEventDefinition(Element timerEventDefinition, ActivityImpl timerActivity)
  {
    timerActivity.setProperty("type", "intermediateTimer");
    TimerDeclarationImpl timerDeclaration = parseTimer(timerEventDefinition, timerActivity, "timer-intermediate-transition");
    addTimerDeclaration(timerActivity, timerDeclaration);
    timerActivity.setScope(true);
    for (BpmnParseListener parseListener : this.parseListeners)
      parseListener.parseIntermediateTimerEventDefinition(timerEventDefinition, timerActivity);
  }

  private TimerDeclarationImpl parseTimer(Element timerEventDefinition, ScopeImpl timerActivity, String jobHandlerType)
  {
    TimerDeclarationType type = TimerDeclarationType.DATE;
    Expression expression = parseExpression(timerEventDefinition, "timeDate");

    if (expression == null) {
      type = TimerDeclarationType.CYCLE;
      expression = parseExpression(timerEventDefinition, "timeCycle");
    }

    if (expression == null) {
      type = TimerDeclarationType.DURATION;
      expression = parseExpression(timerEventDefinition, "timeDuration");
    }

    TimerDeclarationImpl timerDeclaration = new TimerDeclarationImpl(expression, type, jobHandlerType);
    timerDeclaration.setJobHandlerConfiguration(timerActivity.getId());
    return timerDeclaration;
  }

  private Expression parseExpression(Element parent, String name) {
    Element value = parent.element(name);
    if (value != null) {
      String expressionText = value.getText().trim();
      return this.expressionManager.createExpression(expressionText);
    }
    return null;
  }

  public void parseBoundaryErrorEventDefinition(Element errorEventDefinition, boolean interrupting, ActivityImpl activity, ActivityImpl nestedErrorEventActivity)
  {
    nestedErrorEventActivity.setProperty("type", "boundaryError");
    ((ActivityImpl)nestedErrorEventActivity.getParent()).setScope(true);

    String errorRef = errorEventDefinition.attribute("errorRef");
    Error error = null;
    if (errorRef != null) {
      error = (Error)this.errors.get(errorRef);
      nestedErrorEventActivity.setProperty("errorCode", error == null ? errorRef : error.getErrorCode());
    }

    List<ActivityImpl> childErrorEndEvents = getAllChildActivitiesOfType("errorEndEvent", activity);
    for (ActivityImpl errorEndEvent : childErrorEndEvents) {
      ErrorEndEventActivityBehavior behavior = (ErrorEndEventActivityBehavior)errorEndEvent.getActivityBehavior();
      if ((errorRef == null) || (errorRef.equals(behavior.getErrorCode())) || ((error != null) && (error.getErrorCode().equals(behavior.getErrorCode()))))
      {
        ActivityImpl catchingActivity = null;
        if (behavior.getBorderEventActivityId() != null) {
          catchingActivity = activity.getProcessDefinition().findActivity(behavior.getBorderEventActivityId());
        }

        if ((catchingActivity == null) || (isChildActivity(activity, catchingActivity))) {
          behavior.setBorderEventActivityId(nestedErrorEventActivity.getId());
        }
      }
    }

    for (BpmnParseListener parseListener : this.parseListeners)
      parseListener.parseBoundaryErrorEventDefinition(errorEventDefinition, interrupting, activity, nestedErrorEventActivity);
  }

  protected List<ActivityImpl> getAllChildActivitiesOfType(String type, ScopeImpl scope)
  {
    List children = new ArrayList();
    for (ActivityImpl childActivity : scope.getActivities()) {
      if (type.equals(childActivity.getProperty("type"))) {
        children.add(childActivity);
      }
      children.addAll(getAllChildActivitiesOfType(type, childActivity));
    }
    return children;
  }

  protected boolean isChildActivity(ActivityImpl activityToCheck, ActivityImpl possibleParentActivity)
  {
    for (ActivityImpl child : possibleParentActivity.getActivities()) {
      if ((child.getId().equals(activityToCheck.getId())) || (isChildActivity(activityToCheck, child))) {
        return true;
      }
    }
    return false;
  }

  protected void addTimerDeclaration(ScopeImpl scope, TimerDeclarationImpl timerDeclaration)
  {
    List timerDeclarations = (List)scope.getProperty("timerDeclarations");
    if (timerDeclarations == null) {
      timerDeclarations = new ArrayList();
      scope.setProperty("timerDeclarations", timerDeclarations);
    }
    timerDeclarations.add(timerDeclaration);
  }

  protected void addVariableDeclaration(ScopeImpl scope, VariableDeclaration variableDeclaration)
  {
    List variableDeclarations = (List)scope.getProperty("variableDeclarations");
    if (variableDeclarations == null) {
      variableDeclarations = new ArrayList();
      scope.setProperty("variableDeclarations", variableDeclarations);
    }
    variableDeclarations.add(variableDeclaration);
  }

  public ActivityImpl parseSubProcess(Element subProcessElement, ScopeImpl scope)
  {
    ActivityImpl activity = createActivityOnScope(subProcessElement, scope);

    String async = subProcessElement.attributeNS("http://activiti.org/bpmn", "async");
    activity.setAsync("true".equals(async));

    activity.setScope(true);
    activity.setActivityBehavior(new SubProcessActivityBehavior());
    parseScope(subProcessElement, activity);

    for (BpmnParseListener parseListener : this.parseListeners) {
      parseListener.parseSubProcess(subProcessElement, scope, activity);
    }
    return activity;
  }

  public ActivityImpl parseCallActivity(Element callActivityElement, ScopeImpl scope)
  {
    ActivityImpl activity = createActivityOnScope(callActivityElement, scope);

    String async = callActivityElement.attributeNS("http://activiti.org/bpmn", "async");
    activity.setAsync("true".equals(async));

    String calledElement = callActivityElement.attribute("calledElement");
    if (calledElement == null) {
      addError("Missing attribute 'calledElement'", callActivityElement);
    }

    CallActivityBehavior callActivityBehaviour = new CallActivityBehavior(calledElement);

    Element extentionsElement = callActivityElement.element("extensionElements");
    if (extentionsElement != null)
    {
      for (Element listenerElement : extentionsElement.elementsNS("http://activiti.org/bpmn", "in")) {
        String sourceExpression = listenerElement.attribute("sourceExpression");
        String target = listenerElement.attribute("target");
        if (sourceExpression != null) {
          Expression expression = this.expressionManager.createExpression(sourceExpression.trim());
          callActivityBehaviour.addDataInputAssociation(new SimpleDataInputAssociation(expression, target));
        } else {
          String source = listenerElement.attribute("source");
          callActivityBehaviour.addDataInputAssociation(new SimpleDataInputAssociation(source, target));
        }
      }

      for (Element listenerElement : extentionsElement.elementsNS("http://activiti.org/bpmn", "out")) {
        String sourceExpression = listenerElement.attribute("sourceExpression");
        String target = listenerElement.attribute("target");
        if (sourceExpression != null) {
          Expression expression = this.expressionManager.createExpression(sourceExpression.trim());
          callActivityBehaviour.addDataOutputAssociation(new MessageImplicitDataOutputAssociation(target, expression));
        } else {
          String source = listenerElement.attribute("source");
          callActivityBehaviour.addDataOutputAssociation(new MessageImplicitDataOutputAssociation(target, source));
        }

      }

    }

    activity.setScope(true);
    activity.setActivityBehavior(callActivityBehaviour);

    parseExecutionListenersOnScope(callActivityElement, activity);

    for (BpmnParseListener parseListener : this.parseListeners) {
      parseListener.parseCallActivity(callActivityElement, scope, activity);
    }
    return activity;
  }

  public void parseProperties(Element element, ActivityImpl activity)
  {
    List<Element> propertyElements = element.elements("property");
    for (Element propertyElement : propertyElements)
      parseProperty(propertyElement, activity);
  }

  public void parseProperty(Element propertyElement, ActivityImpl activity)
  {
    String id = propertyElement.attribute("id");
    String name = propertyElement.attribute("name");

    if (name == null) {
      if (id == null)
        addError("Invalid property usage on line " + propertyElement.getLine() + ": no id or name specified.", propertyElement);
      else {
        name = id;
      }
    }

    String itemSubjectRef = propertyElement.attribute("itemSubjectRef");
    String type = null;
    if (itemSubjectRef != null) {
      ItemDefinition itemDefinition = (ItemDefinition)this.itemDefinitions.get(itemSubjectRef);
      if (itemDefinition != null) {
        StructureDefinition structure = itemDefinition.getStructureDefinition();
        type = structure.getId();
      } else {
        addError("Invalid itemDefinition reference: " + itemSubjectRef + " not found", propertyElement);
      }
    }

    parsePropertyCustomExtensions(activity, propertyElement, name, type);
  }

  public void parsePropertyCustomExtensions(ActivityImpl activity, Element propertyElement, String propertyName, String propertyType)
  {
    if (propertyType == null) {
      String type = propertyElement.attributeNS("http://activiti.org/bpmn", "type");
      propertyType = type != null ? type : "string";
    }

    VariableDeclaration variableDeclaration = new VariableDeclaration(propertyName, propertyType);
    addVariableDeclaration(activity, variableDeclaration);
    activity.setScope(true);

    String src = propertyElement.attributeNS("http://activiti.org/bpmn", "src");
    if (src != null) {
      variableDeclaration.setSourceVariableName(src);
    }

    String srcExpr = propertyElement.attributeNS("http://activiti.org/bpmn", "srcExpr");
    if (srcExpr != null) {
      Expression sourceExpression = this.expressionManager.createExpression(srcExpr);
      variableDeclaration.setSourceExpression(sourceExpression);
    }

    String dst = propertyElement.attributeNS("http://activiti.org/bpmn", "dst");
    if (dst != null) {
      variableDeclaration.setDestinationVariableName(dst);
    }

    String destExpr = propertyElement.attributeNS("http://activiti.org/bpmn", "dstExpr");
    if (destExpr != null) {
      Expression destinationExpression = this.expressionManager.createExpression(destExpr);
      variableDeclaration.setDestinationExpression(destinationExpression);
    }

    String link = propertyElement.attributeNS("http://activiti.org/bpmn", "link");
    if (link != null) {
      variableDeclaration.setLink(link);
    }

    String linkExpr = propertyElement.attributeNS("http://activiti.org/bpmn", "linkExpr");
    if (linkExpr != null) {
      Expression linkExpression = this.expressionManager.createExpression(linkExpr);
      variableDeclaration.setLinkExpression(linkExpression);
    }

    for (BpmnParseListener parseListener : this.parseListeners)
      parseListener.parseProperty(propertyElement, variableDeclaration, activity);
  }

  public void parseSequenceFlow(Element processElement, ScopeImpl scope)
  {
    for (Element sequenceFlowElement : processElement.elements("sequenceFlow"))
    {
      String id = sequenceFlowElement.attribute("id");
      String sourceRef = sequenceFlowElement.attribute("sourceRef");
      String destinationRef = sequenceFlowElement.attribute("targetRef");

      ActivityImpl sourceActivity = scope.findActivity(sourceRef);
      ActivityImpl destinationActivity = scope.findActivity(destinationRef);

      if ((sourceActivity != null) && (destinationActivity != null))
      {
        TransitionImpl transition = sourceActivity.createOutgoingTransition(id);
        this.sequenceFlows.put(id, transition);
        transition.setProperty("name", sequenceFlowElement.attribute("name"));
        transition.setProperty("documentation", parseDocumentation(sequenceFlowElement));
        transition.setDestination(destinationActivity);
        parseSequenceFlowConditionExpression(sequenceFlowElement, transition);
        parseExecutionListenersOnTransition(sequenceFlowElement, transition);

        for (BpmnParseListener parseListener : this.parseListeners) {
          parseListener.parseSequenceFlow(sequenceFlowElement, scope, transition);
        }
      }
      else if (sourceActivity == null) {
        addError("Invalid source '" + sourceRef + "' of sequence flow '" + id + "'", sequenceFlowElement);
      } else if (destinationActivity == null) {
        addError("Invalid destination '" + destinationRef + "' of sequence flow '" + id + "'", sequenceFlowElement);
      }
    }
  }

  public void parseSequenceFlowConditionExpression(Element seqFlowElement, TransitionImpl seqFlow)
  {
    Element conditionExprElement = seqFlowElement.element("conditionExpression");
    if (conditionExprElement != null) {
      String expression = conditionExprElement.getText().trim();
      String type = conditionExprElement.attributeNS("http://www.w3.org/2001/XMLSchema-instance", "type");
      if ((type != null) && (!type.equals("tFormalExpression"))) {
        addError("Invalid type, only tFormalExpression is currently supported", conditionExprElement);
      }

      Condition expressionCondition = new UelExpressionCondition(this.expressionManager.createExpression(expression));
      seqFlow.setProperty("conditionText", expression);
      seqFlow.setProperty("condition", expressionCondition);
    }
    else
    {
      ActivityImpl activity = seqFlow.getSource();
      ActivityImpl target_activity = seqFlow.getDestination();
      ActivityBehavior actBehavior = activity.getActivityBehavior();
      ActivityBehavior target_actBehavior = target_activity.getActivityBehavior();
    //  System.out.println(target_activity.getProperty("name"));
      if (((actBehavior instanceof NoneStartEventActivityBehavior)) || ((actBehavior instanceof ParallelGatewayActivityBehavior)))
      {
      //  System.out.println(target_activity.getProperty("name"));
      } else if ((target_actBehavior instanceof ParallelGatewayActivityBehavior)) {
        String expression = "${parallelGateway==\"" + seqFlow.getId() + "\"}";
        seqFlow.setProperty("conditionText", expression);
        Condition expressionCondition = new UelExpressionCondition(this.expressionManager.createExpression(expression));
        seqFlow.setProperty("condition", expressionCondition);
      } else if ((target_actBehavior instanceof ExclusiveGatewayActivityBehavior)) {
        String expression = "${exclusivegateway==\"" + seqFlow.getId() + "\"}";
        seqFlow.setProperty("conditionText", expression);
        Condition expressionCondition = new UelExpressionCondition(this.expressionManager.createExpression(expression));
        seqFlow.setProperty("condition", expressionCondition);
      } else if ((actBehavior instanceof ExclusiveGatewayActivityBehavior)) {
        String expression = "${exclusivegateway_select==\"" + seqFlow.getId() + "\"}";
        seqFlow.setProperty("conditionText", expression);
        Condition expressionCondition = new UelExpressionCondition(this.expressionManager.createExpression(expression));
        seqFlow.setProperty("condition", expressionCondition);
      } else if (!(actBehavior instanceof ClassDelegate))
      {
        String expression = "${TransitionImpl==\"" + seqFlow.getId() + "\"}";
        seqFlow.setProperty("conditionText", expression);
        Condition expressionCondition = new UelExpressionCondition(this.expressionManager.createExpression(expression));
        seqFlow.setProperty("condition", expressionCondition);
      }
    }
  }

  public void parseExecutionListenersOnScope(Element scopeElement, ScopeImpl scope)
  {
    Element extentionsElement = scopeElement.element("extensionElements");
    if (extentionsElement != null) {
      List<Element> listenerElements = extentionsElement.elementsNS("http://activiti.org/bpmn", "executionListener");
      for (Element listenerElement : listenerElements) {
        String eventName = listenerElement.attribute("event");
        if (isValidEventNameForScope(eventName, listenerElement)) {
          ExecutionListener listener = parseExecutionListener(listenerElement);
          if (listener != null)
            scope.addExecutionListener(eventName, listener);
        }
      }
    }
  }

  protected boolean isValidEventNameForScope(String eventName, Element listenerElement)
  {
    if ((eventName != null) && (eventName.trim().length() > 0)) {
      if (("start".equals(eventName)) || ("end".equals(eventName))) {
        return true;
      }
      addError("Attribute 'eventName' must be one of {start|end}", listenerElement);
    }
    else {
      addError("Attribute 'eventName' is mandatory on listener", listenerElement);
    }
    return false;
  }

  public void parseExecutionListenersOnTransition(Element activitiElement, TransitionImpl activity) {
    Element extentionsElement = activitiElement.element("extensionElements");
    if (extentionsElement != null) {
      List<Element> listenerElements = extentionsElement.elementsNS("http://activiti.org/bpmn", "executionListener");
      for (Element listenerElement : listenerElements) {
        ExecutionListener listener = parseExecutionListener(listenerElement);
        if (listener != null)
        {
          activity.addExecutionListener(listener);
        }
      }
    }
  }

  public ExecutionListener parseExecutionListener(Element executionListenerElement)
  {
    ExecutionListener executionListener = null;

    String className = executionListenerElement.attribute("class");
    String expression = executionListenerElement.attribute("expression");
    String delegateExpression = executionListenerElement.attribute("delegateExpression");

    if (className != null)
      executionListener = new ClassDelegate(className, parseFieldDeclarations(executionListenerElement));
    else if (expression != null)
      executionListener = new ExpressionExecutionListener(this.expressionManager.createExpression(expression));
    else if (delegateExpression != null)
      executionListener = new DelegateExpressionExecutionListener(this.expressionManager.createExpression(delegateExpression));
    else {
      addError("Element 'class' or 'expression' is mandatory on executionListener", executionListenerElement);
    }
    return executionListener;
  }

  public Operation getOperation(String operationId)
  {
    return (Operation)this.operations.get(operationId);
  }

  public void parseDiagramInterchangeElements()
  {
    List<Element> diagrams = this.rootElement.elementsNS("http://www.omg.org/spec/BPMN/20100524/DI", "BPMNDiagram");
    if (!diagrams.isEmpty())
      for (Element diagramElement : diagrams)
        parseBPMNDiagram(diagramElement);
  }

  public void parseBPMNDiagram(Element bpmndiagramElement)
  {
    Element bpmnPlane = bpmndiagramElement.elementNS("http://www.omg.org/spec/BPMN/20100524/DI", "BPMNPlane");
    if (bpmnPlane != null)
      parseBPMNPlane(bpmnPlane);
  }

  public void parseBPMNPlane(Element bpmnPlaneElement)
  {
    String processId = bpmnPlaneElement.attribute("bpmnElement");
    if ((processId != null) && (!"".equals(processId))) {
      ProcessDefinitionEntity processDefinition = getProcessDefinition(processId);
      if (processDefinition != null) {
        processDefinition.setGraphicalNotationDefined(true);

        List<Element> shapes = bpmnPlaneElement.elementsNS("http://www.omg.org/spec/BPMN/20100524/DI", "BPMNShape");
        for (Element shape : shapes) {
          parseBPMNShape(shape, processDefinition);
        }

        List<Element> edges = bpmnPlaneElement.elementsNS("http://www.omg.org/spec/BPMN/20100524/DI", "BPMNEdge");
        for (Element edge : edges)
          parseBPMNEdge(edge, processDefinition);
      }
      else
      {
        addError("Invalid reference in 'bpmnElement' attribute, process " + processId + " not found", bpmnPlaneElement);
      }
    } else {
      addError("'bpmnElement' attribute is required on BPMNPlane ", bpmnPlaneElement);
    }
  }

  public void parseBPMNShape(Element bpmnShapeElement, ProcessDefinitionEntity processDefinition) {
    String activityId = bpmnShapeElement.attribute("bpmnElement");
    if ((activityId != null) && (!"".equals(activityId))) {
      ActivityImpl activity = processDefinition.findActivity(activityId);

      if (activity != null) {
        Element bounds = bpmnShapeElement.elementNS("http://www.omg.org/spec/DD/20100524/DC", "Bounds");
        if (bounds != null) {
          activity.setX(parseDoubleAttribute(bpmnShapeElement, "x", bounds.attribute("x"), true).intValue());
          activity.setY(parseDoubleAttribute(bpmnShapeElement, "y", bounds.attribute("y"), true).intValue());
          activity.setWidth(parseDoubleAttribute(bpmnShapeElement, "width", bounds.attribute("width"), true).intValue());
          activity.setHeight(parseDoubleAttribute(bpmnShapeElement, "height", bounds.attribute("height"), true).intValue());
        } else {
          addError("'Bounds' element is required", bpmnShapeElement);
        }

        String isExpanded = bpmnShapeElement.attribute("isExpanded");
        if (isExpanded != null)
          activity.setProperty("isExpanded", parseBooleanAttribute(isExpanded));
      }
      else {
        addError("Invalid reference in 'bpmnElement' attribute, activity " + activityId + "not found", bpmnShapeElement);
      }
    } else {
      addError("'bpmnElement' attribute is required on BPMNShape", bpmnShapeElement);
    }
  }

  public void parseBPMNEdge(Element bpmnEdgeElement, ProcessDefinitionEntity processDefinition)
  {
      String sequenceFlowId = bpmnEdgeElement.attribute("bpmnElement");
      if(sequenceFlowId != null && !"".equals(sequenceFlowId))
      {
          TransitionImpl sequenceFlow = (TransitionImpl)sequenceFlows.get(sequenceFlowId);
          if(sequenceFlow != null)
          {
              List waypointElements = bpmnEdgeElement.elementsNS("http://www.omg.org/spec/DD/20100524/DI", "waypoint");
              if(waypointElements.size() >= 2)
              {
                  List waypoints = new ArrayList();
                  Element waypointElement;
                  for(Iterator iterator = waypointElements.iterator(); iterator.hasNext(); waypoints.add(Integer.valueOf(parseDoubleAttribute(waypointElement, "y", waypointElement.attribute("y"), true).intValue())))
                  {
                      waypointElement = (Element)iterator.next();
                      waypoints.add(Integer.valueOf(parseDoubleAttribute(waypointElement, "x", waypointElement.attribute("x"), true).intValue()));
                  }

                  sequenceFlow.setWaypoints(waypoints);
              } else
              {
                  addError("Minimum 2 waypoint elements must be definted for a 'BPMNEdge'", bpmnEdgeElement);
              }
          } else
          {
              addError((new StringBuilder("Invalid reference in 'bpmnElement' attribute, sequenceFlow ")).append(sequenceFlowId).append("not found").toString(), bpmnEdgeElement);
          }
      } else
      {
          addError("'bpmnElement' attribute is required on BPMNEdge", bpmnEdgeElement);
      }
  }
  public List<ProcessDefinitionEntity> getProcessDefinitions()
  {
    return this.processDefinitions;
  }

  public ProcessDefinitionEntity getProcessDefinition(String processDefinitionKey) {
    for (ProcessDefinitionEntity processDefinition : this.processDefinitions) {
      if (processDefinition.getKey().equals(processDefinitionKey)) {
        return processDefinition;
      }
    }
    return null;
  }

  public BpmnParse name(String name)
  {
    super.name(name);
    return this;
  }

  public BpmnParse sourceInputStream(InputStream inputStream)
  {
    super.sourceInputStream(inputStream);
    return this;
  }

  public BpmnParse sourceResource(String resource, ClassLoader classLoader)
  {
    super.sourceResource(resource, classLoader);
    return this;
  }

  public BpmnParse sourceResource(String resource)
  {
    super.sourceResource(resource);
    return this;
  }

  public BpmnParse sourceString(String string)
  {
    super.sourceString(string);
    return this;
  }

  public BpmnParse sourceUrl(String url)
  {
    super.sourceUrl(url);
    return this;
  }

  public BpmnParse sourceUrl(URL url)
  {
    super.sourceUrl(url);
    return this;
  }

  public void addStructure(StructureDefinition structure) {
    this.structures.put(structure.getId(), structure);
  }

  public void addService(BpmnInterfaceImplementation bpmnInterfaceImplementation) {
    this.interfaceImplementations.put(bpmnInterfaceImplementation.getName(), bpmnInterfaceImplementation);
  }

  public void addOperation(OperationImplementation operationImplementation) {
    this.operationImplementations.put(operationImplementation.getId(), operationImplementation);
  }

  public Boolean parseBooleanAttribute(String booleanText, boolean defaultValue) {
    if (booleanText == null) {
      return Boolean.valueOf(defaultValue);
    }
    return parseBooleanAttribute(booleanText);
  }

  public Boolean parseBooleanAttribute(String booleanText)
  {
    if (("true".equals(booleanText)) || ("enabled".equals(booleanText)) || ("on".equals(booleanText)) || ("active".equals(booleanText)) || ("yes".equals(booleanText))) {
      return Boolean.TRUE;
    }
    if (("false".equals(booleanText)) || ("disabled".equals(booleanText)) || ("off".equals(booleanText)) || ("inactive".equals(booleanText)) || 
      ("no".equals(booleanText))) {
      return Boolean.FALSE;
    }
    return null;
  }

  public Double parseDoubleAttribute(Element element, String attributename, String doubleText, boolean required) {
    if ((required) && ((doubleText == null) || ("".equals(doubleText))))
      addError(attributename + " is required", element);
    else {
      try {
        return Double.valueOf(Double.parseDouble(doubleText));
      } catch (NumberFormatException e) {
        addError("Cannot parse " + attributename + ": " + e.getMessage(), element);
      }
    }
    return Double.valueOf(-1.0D);
  }
}