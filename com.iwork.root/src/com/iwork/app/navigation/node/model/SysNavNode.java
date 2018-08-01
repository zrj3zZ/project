package com.iwork.app.navigation.node.model;


/**
 * SysNavNode entity. @author MyEclipse Persistence Tools
 */

public class SysNavNode implements java.io.Serializable {

	// Fields
	public static String DATABASE_ENTITY = "SysNavNode";
	private Long id;
	private String nodeName; 
	private Long parentNodeId;
	private String nodeIcon;
	private String nodeUrl;
	private String nodeTarget;
	private String nodeDesc;
	private String nodeType;
	private Long orderindex;
	private Long systemId;
	private String nodeUuid;

	// Constructors

	/** default constructor */
	public SysNavNode() {
	}

	/** full constructor */
	public SysNavNode(String nodeName, Long parentNodeId,
			String nodeIcon, String nodeUrl, String nodeTarget,
			String nodeDesc, String nodeType, Long orderindex,
			Long systemId, String nodeUuid) {
		this.nodeName = nodeName;
		this.parentNodeId = parentNodeId;
		this.nodeIcon = nodeIcon;
		this.nodeUrl = nodeUrl;
		this.nodeTarget = nodeTarget;
		this.nodeDesc = nodeDesc;
		this.nodeType = nodeType;
		this.orderindex = orderindex;
		this.systemId = systemId;
		this.nodeUuid = nodeUuid;
	}
 
	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Long getParentNodeId() {
		return this.parentNodeId;
	}

	public void setParentNodeId(Long parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public String getNodeIcon() {
		return this.nodeIcon;
	}

	public void setNodeIcon(String nodeIcon) {
		this.nodeIcon = nodeIcon;
	}

	public String getNodeUrl() {
		return this.nodeUrl;
	}

	public void setNodeUrl(String nodeUrl) {
		this.nodeUrl = nodeUrl;
	}

	public String getNodeTarget() {
		return this.nodeTarget;
	}

	public void setNodeTarget(String nodeTarget) {
		this.nodeTarget = nodeTarget;
	}

	public String getNodeDesc() {
		return this.nodeDesc;
	}

	public void setNodeDesc(String nodeDesc) {
		this.nodeDesc = nodeDesc;
	}

	public String getNodeType() {
		return this.nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public Long getOrderindex() {
		return this.orderindex;
	}

	public void setOrderindex(Long orderindex) {
		this.orderindex = orderindex;
	}

	public Long getSystemId() {
		return this.systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public String getNodeUuid() {
		return this.nodeUuid;
	}

	public void setNodeUuid(String nodeUuid) {
		this.nodeUuid = nodeUuid;
	}

}