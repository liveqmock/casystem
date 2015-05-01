package ca.core.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Productbatch entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "productbatch")
public class Productbatch implements java.io.Serializable {

	// Fields

	private Integer batchID;
	private long typeID;
	private String productName;
	private long vendorID;
	private String vendorBatchID;
	private Date productDate;
	private Date expireDate;
	private Integer batchStatus;
	private Integer productCount;
	private long startID;
	private long endID;
	private String inspectCode;
    private Integer inspectID;
	private String batchKey;
	private Integer userID;
	private String productSourceReport;
	private String thirdReport;
	private String vendorReport;
	private String batchInfo;
	private Integer authID;
	

	// Constructors

	/** default constructor */
	public Productbatch() {
	}

	/** minimal constructor */
	public Productbatch(Integer batchID, String productName, Integer productCount,
			Date productDate, Date shelfLife, String batchKey, Integer tagType,
			Integer userId, Integer batchStatus) {
		this.batchID = batchID;
		this.productName = productName;
		this.productCount = productCount;
		this.productDate = productDate;
		this.batchKey = batchKey;
		this.batchStatus = batchStatus;
	}

	/** full constructor */
	public Productbatch(Integer batchID, String productName, Integer productCount,
			Date productDate, Date shelfLife, String batchKey, Integer tagType,
			Integer categoryId, long vendorId, Integer authId,
			Integer userId, Integer batchStatus, String batchInfo,String productTypeID,Date expireDate) {
		this.batchID = batchID;
		this.productName = productName;
		this.productCount = productCount;
		this.productDate = productDate;
		this.batchKey = batchKey;
		this.batchStatus = batchStatus;
		this.batchInfo = batchInfo;
		this.expireDate = expireDate;
	}

	// Property accessors
	@Id
	@Column(name = "BatchID")
	public Integer getBatchID() {
		return this.batchID;
	}

	public void setBatchID(Integer batchID) {
		this.batchID = batchID;
	}

	@Column(name = "ProductName")
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "ProductCount")
	public Integer getProductCount() {
		return this.productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	@Column(name = "ProductDate")
	public Date getProductDate() {
		return this.productDate;
	}

	public void setProductDate(Date productDate) {
		this.productDate = productDate;
	}

	@Column(name = "BatchKey")
	public String getBatchKey() {
		return this.batchKey;
	}

	public void setBatchKey(String batchKey) {
		this.batchKey = batchKey;
	}

	@Column(name = "VendorID")
	public long getVendorID() {
		return this.vendorID;
	}

	public void setVendorID(long vendorID) {
		this.vendorID = vendorID;
	}

	@Column(name = "AuthID")
	public Integer getAuthID() {
		return this.authID;
	}

	public void setAuthID(Integer authID) {
		this.authID = authID;
	}

	@Column(name = "UserID", nullable = false)
	public Integer getUserID() {
		return this.userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	@Column(name = "BatchStatus", nullable = false)
	public Integer getBatchStatus() {
		return this.batchStatus;
	}

	public void setBatchStatus(Integer batchStatus) {
		this.batchStatus = batchStatus;
	}

	@Column(name = "BatchInfo", length = 1)
	public String getBatchInfo() {
		return this.batchInfo;
	}

	public void setBatchInfo(String batchInfo) {
		this.batchInfo = batchInfo;
	}

	@Column(name = "StartID", length = 1)
	public long getStartID() {
		return startID;
	}

	public void setStartID(long startID) {
		this.startID = startID;
	}

	@Column(name = "EndID", length = 1)
	public long getEndID() {
		return endID;
	}

	public void setEndID(long endID) {
		this.endID = endID;
	}

    @Column(name="InspectCode")
    public String getInspectCode() {
        return inspectCode;
    }

    public void setInspectCode(String inspectCode) {
        this.inspectCode = inspectCode;
    }

    @Column(name="ExpireDate")
	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public long getTypeID() {
		return typeID;
	}

	public void setTypeID(long typeID) {
		this.typeID = typeID;
	}

	public String getVendorBatchID() {
		return vendorBatchID;
	}

	public void setVendorBatchID(String vendorBatchID) {
		this.vendorBatchID = vendorBatchID;
	}

	public String getProductSourceReport() {
		return productSourceReport;
	}

	public void setProductSourceReport(String productSourceReport) {
		this.productSourceReport = productSourceReport;
	}

	public String getThirdReport() {
		return thirdReport;
	}

	public void setThirdReport(String thirdReport) {
		this.thirdReport = thirdReport;
	}

	public String getVendorReport() {
		return vendorReport;
	}

	public void setVendorReport(String vendorReport) {
		this.vendorReport = vendorReport;
	}
    @Column(name="InspectID")
    public Integer getInspectID() {
        return inspectID;
    }

    public void setInspectID(Integer inspectID) {
        this.inspectID = inspectID;
    }
}