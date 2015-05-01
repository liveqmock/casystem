package ca.core.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Productauth entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "productauth")
public class Productauth implements java.io.Serializable {

	// Fields

	private long productId;
	private String productKey;
	private Integer counter;
	private String authCode;
	private Integer batchId;
	private String inspectCode;
    private Integer inspectID;
	private String logisticsId;
	private String random;
	private Integer status;
	private Integer linkInfoId;
    private Date SaleDate;
    private Integer SaleFlag;
    private String CoatCode;

	// Constructors

	/** default constructor */
	public Productauth() {
	}

	/** minimal constructor */
	public Productauth(long productId, Integer counter, Integer batchId,
			String random) {
		this.productId = productId;
		this.counter = counter;
		this.batchId = batchId;
		this.random = random;
	}

	/** full constructor */
	public Productauth(long productId, String productKey, String random, String authCode, Integer counter,
			 Integer batchId, String inspectCode, String logisticsId,
			Integer status, Integer linkInfoId) {
		this.productId = productId;
		this.productKey = productKey;
		this.random = random;
		this.authCode = authCode;
		this.counter = counter;
		this.batchId = batchId;
		this.inspectCode = inspectCode;
		this.logisticsId = logisticsId;
		this.status = status;
		this.linkInfoId =linkInfoId;
	}

    @Column(name="SaleDate")
    public Date getSaleDate() {
        return SaleDate;
    }

    public void setSaleDate(Date saleDate) {
        SaleDate = saleDate;
    }

    @Column(name="SaleFlag, nullable = false")
    public Integer getSaleFlag() {
        return SaleFlag;
    }

    public void setSaleFlag(Integer saleFlag) {
        SaleFlag = saleFlag;
    }

    @Column(name="CoatCode, nullable = false")
    public String getCoatCode() {
        return CoatCode;
    }

    public void setCoatCode(String coatCode) {
        CoatCode = coatCode;
    }

    // Property accessors
	@Id
	@Column(name = "ProductID", nullable = false)
	public long getProductId() {
		return this.productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	@Column(name = "ProductKey")
	public String getProductKey() {
		return this.productKey;
	}

	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}

	@Column(name = "Random", nullable = false)
	public String getRandom() {
		return this.random;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	@Column(name = "AuthCode")
	public String getAuthCode() {
		return this.authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	@Column(name = "Counter", nullable = false)
	public Integer getCounter() {
		return this.counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
	}

	@Column(name = "BatchID", nullable = false)
	public Integer getBatchId() {
		return this.batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

    @Column(name="InspectCode")
    public String getInspectCode() {
        return inspectCode;
    }

    public void setInspectCode(String inspectCode) {
        this.inspectCode = inspectCode;
    }

    @Column(name = "LogisticsID", nullable = false)
	public String getLogisticsId() {
		return this.logisticsId;
	}

	public void setLogisticsId(String logisticsId) {
		this.logisticsId = logisticsId;
	}

	@Column(name = "Status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "LinkInfoID")
	public Integer getLinkInfoId() {
		return this.linkInfoId;
	}

	public void setLinkInfoId(Integer linkInfoId) {
		this.linkInfoId = linkInfoId;
	}
    @Column(name="InspectID")
    public Integer getInspectID() {
        return inspectID;
    }

    public void setInspectID(Integer inspectID) {
        this.inspectID = inspectID;
    }
}