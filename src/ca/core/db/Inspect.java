package ca.core.db;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Productbatch entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "inspect", catalog = "cadb")
public class Inspect implements java.io.Serializable {

	// Fields

	private Integer inspectId;
	private String inspectCode;
	private String productName;
	private long typeId;
	private Date InspectDate;
	private String InDistributors;
	private String InAgentor;
	private Integer category;
	private String hsCode;
	private String inspectInfo;
	private String productCount;
	private String productCountUnit;
	private String productWeight;
	private String productWeightUnit;
	private String productValue;
	private String productValueUnit;
	private String productPackage;
	private String productPackageUnit;
	private Date intoDate;
	private Integer inspectStatus;
	private Date ProductShipDate;
	private String certificateFile;
	private String  comment;
    private String  urgent;
	private Integer InspectRemain;
	private long vendorID;
    private String inExportPort;
    private String inspectInCode;
    private Integer tagApplyCount;
    private String importer;
    private String importGate;

	// Constructors

	/** default constructor */
	public Inspect() {
	}

	/** minimal constructor */
	public Inspect(Integer inspectId, String inspectCode, String productName, Long typeId,
			Integer category,
			String hsCode, String inspectInfo, String productCount, String productWeight,
			String productValue, String productPackage, Date productDate, Date intoDate, 
			Integer inspectStatus, String pictureFile, long vendorId) {
		this.inspectId = inspectId;
		this.inspectCode = inspectCode;
		this.productName = productName;
		this.typeId = typeId;
		this.category = category;
		this.hsCode = hsCode;
		this.productCount = productCount;
		
		this.productWeight = productWeight;
		this.productValue = productValue;
		this.productPackage = productPackage;
		this.intoDate = intoDate;
		this.inspectStatus = inspectStatus;
		

	}

	/** full constructor */
	public Inspect(Integer inspectId, String inspectCode, String productName, Long typeId,
			Integer category,
			String hsCode, String inspectInfo, String productCount, String productWeight,
			String productValue, String productPackage, Date productDate, Date intoDate, 
			Integer inspectStatus, String certificateFile, String  comment) {
		this.inspectId = inspectId;
		this.inspectCode = inspectCode;
		this.productName = productName;
		this.typeId = typeId;
		this.category = category;
		this.hsCode = hsCode;
		this.inspectInfo = inspectInfo;
		this.productCount = productCount;
		
		this.productWeight = productWeight;
		this.productValue = productValue;
		this.productPackage = productPackage;
		this.intoDate = intoDate;
		this.inspectStatus = inspectStatus;
		this.certificateFile = certificateFile;
		this.comment = comment;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "InspectID")
	public Integer getInspectId() {
		return this.inspectId;
	}

	public void setInspectId(Integer inspectId) {
		this.inspectId = inspectId;
	}
	
	@Column(name = "InspectCode", nullable = false)
	public String getInspectCode() {
		return this.inspectCode;
	}

	public void setInspectCode(String inspectCode) {
		this.inspectCode = inspectCode;
	}
	
	@Column(name = "ProductName", nullable = false, length = 50)
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@Column(name = "TypeID")
	public long getTypeId() {
		return this.typeId;
	}

	public void setTypeId(long TypeId) {
		this.typeId = TypeId;
	}
	
	@Column(name = "Category", nullable = false)
	public Integer getCategory() {
		return this.category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}
	
	@Column(name = "HsCode", nullable = false, length = 50)
	public String getHsCode() {
		return this.hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}
	
	@Column(name = "InspectInfo")
	public String getInspectInfo() {
		return this.inspectInfo;
	}

	public void setInspectInfo(String inspectInfo) {
		this.inspectInfo = inspectInfo;
	}

	@Column(name = "ProductCount", nullable = false)
	public String getProductCount() {
		return this.productCount;
	}

	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}
	
	@Column(name = "ProductWeight", nullable = false)
	public String getProductWeight() {
		return this.productWeight;
	}

	public void setProductWeight(String productWeight) {
		this.productWeight = productWeight;
	}
	
	@Column(name = "ProductValue", nullable = false)
	public String getProductValue() {
		return this.productValue;
	}

	public void setProductValue(String productValue) {
		this.productValue = productValue;
	}

	@Column(name = "ProductPackage", nullable = false, length = 20)
	public String getProductPackage() {
		return this.productPackage;
	}

	public void setProductPackage(String productPackage) {
		this.productPackage = productPackage;
	}

	@Column(name = "IntoData", nullable = false, length = 19)
	public Date getIntoData() {
		return this.intoDate;
	}

	public void setIntoData(Date intoDate) {
		this.intoDate = intoDate;	
	}

	@Column(name = "InspectStatus", nullable = false)
	public Integer getInspectStatus() {
		return this.inspectStatus;
	}

	public void setInspectStatus(Integer inspectStatus) {
		this.inspectStatus = inspectStatus;
	}
	
	@Column(name = "CertificateFile")
	public String getCertificateFile() {
		return this.certificateFile;
	}

	public void setCertificateFile(String certificateFile) {
		this.certificateFile = certificateFile;
	}
	
	@Column(name = "Comment")
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Column(name="InspectDate")
	public Date getInspectDate() {
		return InspectDate;
	}

	public void setInspectDate(Date inspectDate) {
		InspectDate = inspectDate;
	}
	
	@Column(name="InDistributors")
	public String getInDistributors() {
		return InDistributors;
	}

	public void setInDistributors(String inDistributors) {
		InDistributors = inDistributors;
	}
	
	@Column(name="InAgentor")
	public String getInAgentor() {
		return InAgentor;
	}

	public void setInAgentor(String inAgentor) {
		InAgentor = inAgentor;
	}
	
	@Column(name="ProductCountUnit")
	public String getProductCountUnit() {
		return productCountUnit;
	}

	public void setProductCountUnit(String productCountUnit) {
		this.productCountUnit = productCountUnit;
	}
	
	@Column(name="ProductWeightUnit")
	public String getProductWeightUnit() {
		return productWeightUnit;
	}

	public void setProductWeightUnit(String productWeightUnit) {
		this.productWeightUnit = productWeightUnit;
	}
	
	@Column(name="ProductValueUnit")
	public String getProductValueUnit() {
		return productValueUnit;
	}

	public void setProductValueUnit(String productValueUnit) {
		this.productValueUnit = productValueUnit;
	}
	
	@Column(name="ProductPackageUnit")
	public String getProductPackageUnit() {
		return productPackageUnit;
	}
	
	public void setProductPackageUnit(String productPackageUnit) {
		this.productPackageUnit = productPackageUnit;
	}
	
	@Column(name="IntoDate")
	public Date getIntoDate() {
		return intoDate;
	}

	public void setIntoDate(Date intoDate) {
		this.intoDate = intoDate;
	}
	
	@Column(name="ProductShipDate")
	public Date getProductShipDate() {
		return ProductShipDate;
	}

	public void setProductShipDate(Date productShipDate) {
		ProductShipDate = productShipDate;
	}
	
	@Column(name="InspectRemain")
	public Integer getInspectRemain() {
		return InspectRemain;
	}

	public void setInspectRemain(Integer inspectRemain) {
		InspectRemain = inspectRemain;
	}
	
	@Column(name="VendorID")
	public long getVendorID() {
		return vendorID;
	}

	public void setVendorID(long vendorID) {
		this.vendorID = vendorID;
	}

    public String getInExportPort() {
        return inExportPort;
    }

    public void setInExportPort(String inExportPort) {
        this.inExportPort = inExportPort;
    }

    public String getInspectInCode() {
        return inspectInCode;
    }

    public void setInspectInCode(String inspectInCode) {
        this.inspectInCode = inspectInCode;
    }

    public Integer getTagApplyCount() {
        return tagApplyCount;
    }

    public void setTagApplyCount(Integer tagApplyCount) {
        this.tagApplyCount = tagApplyCount;
    }
    @Column(name="Importer")
    public String getImporter() {
        return importer;
    }

    public void setImporter(String importer) {
        this.importer = importer;
    }
    @Column(name="ImportGate")
    public String getImportGate() {
        return importGate;
    }

    public void setImportGate(String importGate) {
        this.importGate = importGate;
    }
    @Column(name="Urgent")
    public String getUrgent() {
        return urgent;
    }

    public void setUrgent(String urgent) {
        this.urgent = urgent;
    }
}