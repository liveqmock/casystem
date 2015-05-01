package ca.core.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "loginfo", catalog = "cadb2")
public class Logistics implements Serializable {

	private long logisticsID;
	private String passGateID;
    private String passInspectID;
	private long typeID;
	private long inspectID;
	private String inspectCode;
	private Integer category;
	private long vendorID;
	private String outDate;
	private long outCount;
	private long productStartID;
	private long productEndID;
	private String receiveCompany;
	private String receivePhone;
	private String receiveAddress;
	private String productCountUnit;

	public String getProductCountUnit() {
		return productCountUnit;
	}

	public void setProductCountUnit(String productCountUnit) {
		this.productCountUnit = productCountUnit;
	}

	/** default constructor */
	public Logistics() {

	}

	/** minimal constructor */
	public Logistics(long typeID, String inspectCode, String outDate,
			long outCount) {
		this.inspectCode = inspectCode;
		this.typeID = typeID;
		this.outCount = outCount;
		this.outDate = outDate;
	}

	/** full constructor */
	public Logistics(String passGateID,String passInspectID, long typeID, String inspectCode,Integer category,long vendorID,
			String outDate, long outCount, long productStartID,
			long productEndID, String receiveCompany, String receivePhone,
			String receiveAddress) {
		this.inspectCode = inspectCode;
		this.category=category;
		this.vendorID=vendorID;
		this.typeID = typeID;
		this.outCount = outCount;
		this.outDate = outDate;
	    this.passGateID = passGateID;
        this.passInspectID=passInspectID;
		this.productStartID = productStartID;
		this.productEndID = productEndID;
		this.receiveCompany = receiveCompany;
		this.receiveAddress = receiveAddress;
		this.receivePhone = receivePhone;
	}

	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "LogisticsID", unique = true, nullable = false)
	public long getLogisticsID() {
		return logisticsID;
	}

	public void setLogisticsID(long logisticsID) {
		this.logisticsID = logisticsID;
	}
	
	@Column(name="PassGateID")
	public String getPassGateID() {
		return passGateID;
	}

	public void setPassGateID(String passGateID) {
		this.passGateID = passGateID;
	}
	
	@Column(name="TypeID")
	public long getTypeID() {
		return typeID;
	}

	public void setTypeID(long typeID) {
		this.typeID = typeID;
	}
	@Column(name="InspectID")
	public long getInspectID() {
		return inspectID;
	}

	public void setInspectID(long inspectID) {
		this.inspectID = inspectID;
	}
	@Column(name="InspectCode")
	public String getInspectCode() {
		return inspectCode;
	}

	public void setInspectCode(String inspectCode) {
		this.inspectCode = inspectCode;
	}
    @Column(name="PassInspectID")
    public String getPassInspectID() {
        return passInspectID;
    }

    public void setPassInspectID(String passInspectID) {
        this.passInspectID = passInspectID;
    }

	@Column(name = "OutDate")
	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}
	
	@Column(name="OutCount")
	public long getOutCount() {
		return outCount;
	}

	public void setOutCount(long outCount) {
		this.outCount = outCount;
	}
	
	@Column(name="ProductStartID")
	public long getProductStartID() {
		return productStartID;
	}

	public void setProductStartID(long productStartID) {
		this.productStartID = productStartID;
	}
	
	@Column(name="ProductEndID")
	public long getProductEndID() {
		return productEndID;
	}

	public void setProductEndID(long productEndID) {
		this.productEndID = productEndID;
	}
	
	@Column(name="ReceiveCompany")
	public String getReceiveCompany() {
		return receiveCompany;
	}

	public void setReceiveCompany(String receiveCompany) {
		this.receiveCompany = receiveCompany;
	}
	
	@Column(name="ReceivePhone")
	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}
	
	@Column(name="ReceiveAddress")
	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	
	@Column(name="Category")
	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}
	@Column(name="VendorID")
	public long getVendorID() {
		return vendorID;
	}

	public void setVendorID(long vendorID) {
		this.vendorID = vendorID;
	}

}
