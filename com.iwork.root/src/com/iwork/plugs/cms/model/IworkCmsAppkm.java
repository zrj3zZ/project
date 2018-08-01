package com.iwork.plugs.cms.model;

/**
 * IworkCmsAppkm entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class IworkCmsAppkm implements java.io.Serializable {

	// Fields

	private Long id;
	private String title;
	private String url;
	private Long sequence;

	// Constructors

	/** default constructor */
	public IworkCmsAppkm() {
	}

	/** minimal constructor */
	public IworkCmsAppkm(Long id) {
		this.id = id;
	}

	/** full constructor */
	public IworkCmsAppkm(Long id, String title, String url, Long sequence) {
		this.id = id;
		this.title = title;
		this.url = url;
		this.sequence = sequence;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getSequence() {
		return this.sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

}