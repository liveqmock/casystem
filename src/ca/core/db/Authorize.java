package ca.core.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * Authorize entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "authorize", catalog = "cadb")
public class Authorize implements java.io.Serializable {

	// Fields

	private Integer authId;
	private String authName;
	private String authAddress;
	private String authUrl;
	private String authPhone;
	private String authEmail;
	private String authKey;
	private Integer authStatus;
	private Integer fatherId;
	private Integer vendorCount;

	// Constructors

	/** default constructor */
	public Authorize() {
	}

	/** minimal constructor */
	public Authorize(String authName, String authAddress, String authUrl,
			String authPhone, String authEmail, String authKey,
			Integer authStatus, Integer vendorCount) {
		this.authName = authName;
		this.authAddress = authAddress;
		this.authUrl = authUrl;
		this.authPhone = authPhone;
		this.authEmail = authEmail;
		this.authKey = authKey;
		this.authStatus = authStatus;
		this.vendorCount = vendorCount;
	}

	/** full constructor */
	public Authorize(String authName, String authAddress, String authUrl,
			String authPhone, String authEmail, String authKey,
			Integer authStatus, Integer fatherId, Integer vendorCount) {
		this.authName = authName;
		this.authAddress = authAddress;
		this.authUrl = authUrl;
		this.authPhone = authPhone;
		this.authEmail = authEmail;
		this.authKey = authKey;
		this.authStatus = authStatus;
		this.fatherId = fatherId;
		this.vendorCount = vendorCount;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "AuthID")
	public Integer getAuthId() {
		return this.authId;
	}

	public void setAuthId(Integer authId) {
		this.authId = authId;
	}

	@Column(name = "AuthName", nullable = false, length = 100)
	public String getAuthName() {
		return this.authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	@Column(name = "AuthAddress", nullable = false, length = 100)
	public String getAuthAddress() {
		return this.authAddress;
	}

	public void setAuthAddress(String authAddress) {
		this.authAddress = authAddress;
	}

	@Column(name = "AuthURL", nullable = false, length = 100)
	public String getAuthUrl() {
		return this.authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	@Column(name = "AuthPhone", nullable = false, length = 20)
	public String getAuthPhone() {
		return this.authPhone;
	}

	public void setAuthPhone(String authPhone) {
		this.authPhone = authPhone;
	}

	@Column(name = "AuthEmail", nullable = false, length = 50)
	public String getAuthEmail() {
		return this.authEmail;
	}

	public void setAuthEmail(String authEmail) {
		this.authEmail = authEmail;
	}

	@Column(name = "AuthKey", nullable = false, length = 16)
	public String getAuthKey() {
		return this.authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	@Column(name = "AuthStatus", nullable = false)
	public Integer getAuthStatus() {
		return this.authStatus;
	}

	public void setAuthStatus(Integer authStatus) {
		this.authStatus = authStatus;
	}

	@Column(name = "FatherID")
	public Integer getFatherId() {
		return this.fatherId;
	}

	public void setFatherId(Integer fatherId) {
		this.fatherId = fatherId;
	}

	@Column(name = "VendorCount")
	public Integer getVendorCount() {
		return this.vendorCount;
	}

	public void setVendorCount(Integer vendorCount) {
		this.vendorCount = vendorCount;
	}

}