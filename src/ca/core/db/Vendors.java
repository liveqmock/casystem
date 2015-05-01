package ca.core.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Vendors entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "vendors", catalog = "cadb")
public class Vendors implements java.io.Serializable {

	// Fields

	private long vendorId;
	private String vendorName;
	private String vendorAddress;
	private String vendorPerson;
	private String vendorURL;
	private String vendorPhone;
	private String vendorFox;
	private String vendorEmail;
	private String vendorKey;
	private Integer vendorStatus;
	private Integer authId;
	private Integer fatherId;
	private String vendorRecord;
	private Integer typeCount;
	private String certification;
	private String comment;

	// Constructors

	/** default constructor */
	public Vendors() {
	}

	/** minimal constructor */
	public Vendors(long vendorId, String vendorName, String vendorAddress,
			String vendorUrl, String vendorPhone, String vendorEmail,
			Integer vendorStatus, Integer typeCount) {
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.vendorAddress = vendorAddress;
		this.vendorURL = vendorUrl;
		this.vendorPhone = vendorPhone;
		this.vendorEmail = vendorEmail;
		this.vendorStatus = vendorStatus;
		this.typeCount = typeCount;
	}

	/** full constructor */
	public Vendors(long vendorId, String vendorName, String vendorAddress,String vendorPerson,
			String vendorUrl, String vendorPhone, String vendorFox,String vendorEmail,
			String vendorKey, Integer vendorStatus, Integer authId,
			Integer fatherId, String vendorRecord,Integer typeCount,String certification) {
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.vendorAddress = vendorAddress;
		this.vendorPerson = vendorPerson;
		this.vendorURL = vendorUrl;
		this.vendorPhone = vendorPhone;
		this.vendorFox = vendorFox;
		this.vendorEmail = vendorEmail;
		this.vendorKey = vendorKey;
		this.vendorStatus = vendorStatus;
		this.authId = authId;
		this.fatherId = fatherId;
		this.vendorRecord = vendorRecord;
		this.typeCount = typeCount;
		this.certification = certification;
	}

	// Property accessors
	@Id
	@Column(name = "VendorID")
	public long getVendorId() {
		return this.vendorId;
	}

	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}

	@Column(name = "VendorName", nullable = false, length = 100)
	public String getVendorName() {
		return this.vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	@Column(name = "VendorAddress", nullable = false, length = 100)
	public String getVendorAddress() {
		return this.vendorAddress;
	}

	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	@Column(name = "VendorURL", nullable = false, length = 100)
	public String getVendorURL() {
		return this.vendorURL;
	}

	public void setVendorURL(String vendorUrl) {
		this.vendorURL = vendorUrl;
	}

	@Column(name = "VendorPhone", nullable = false, length = 20)
	public String getVendorPhone() {
		return this.vendorPhone;
	}

	public void setVendorPhone(String vendorPhone) {
		this.vendorPhone = vendorPhone;
	}

	@Column(name = "VendorEmail", nullable = false, length = 50)
	public String getVendorEmail() {
		return this.vendorEmail;
	}

	public void setVendorEmail(String vendorEmail) {
		this.vendorEmail = vendorEmail;
	}

	@Column(name = "VendorKey", length = 16)
	public String getVendorKey() {
		return this.vendorKey;
	}

	public void setVendorKey(String vendorKey) {
		this.vendorKey = vendorKey;
	}

	@Column(name = "VendorStatus", nullable = false)
	public Integer getVendorStatus() {
		return this.vendorStatus;
	}

	public void setVendorStatus(Integer vendorStatus) {
		this.vendorStatus = vendorStatus;
	}

	@Column(name = "AuthID")
	public Integer getAuthId() {
		return this.authId;
	}

	public void setAuthId(Integer authId) {
		this.authId = authId;
	}

	@Column(name = "FatherID")
	public Integer getFatherId() {
		return this.fatherId;
	}

	public void setFatherId(Integer fatherId) {
		this.fatherId = fatherId;
	}

	@Column(name = "TypeCount")
	public Integer getTypeCount() {
		return this.typeCount;
	}

	public void setTypeCount(Integer typeCount) {
		this.typeCount = typeCount;
	}
	
	@Column(name="VendorPerson")
	public String getVendorPerson() {
		return vendorPerson;
	}

	public void setVendorPerson(String vendorPerson) {
		this.vendorPerson = vendorPerson;
	}
	
	@Column(name="VendorFox")
	public String getVendorFox() {
		return vendorFox;
	}

	public void setVendorFox(String vendorFox) {
		this.vendorFox = vendorFox;
	}
	
	@Column(name="VendorRecord")
	public String getVendorRecord() {
		return vendorRecord;
	}

	public void setVendorRecord(String vendorRecord) {
		this.vendorRecord = vendorRecord;
	}
	
	@Column(name="Certification")
	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}
	
	@Column(name="Comment")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}