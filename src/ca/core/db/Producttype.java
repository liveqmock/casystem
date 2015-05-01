package ca.core.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Producttype entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "producttype", catalog = "cadb")
public class Producttype implements java.io.Serializable {

	// Fields

	private long typeId;
	private String productName;
	private String applyStatus;
	private Integer category;
	private String originCountry;
	private String originRegion;
	private String outDistributors;
	private String netContent;
	private String netContentUnit;
	private String labelProof;
	private String brand;
	private Integer remain;
	private String hsCode;
	private String cnProofSample;
	private String cnProofID;
	private String manageReport;
	
	private Integer categoryId;
	private long vendorId;
	private Integer authId;
	private long productCount;
	private long tagsBought;
	private long tagsApplied;
	private long tagsUsed;
	private Integer tagType;
	private String typeKey;
	private Integer batchCount;
	private String shelfLife;
	private String shelfLifeUnit;
	private String typeInfo;
	private long deviceCount0;
	private long deviceCount1;
	private long deviceCount2;
	private long deviceCount3;
	private long deviceCount4;
	private long deviceCount5;
	private long deviceCount6;
	private long deviceCount7;
	private long deviceCount8;
	private long deviceCount9;
	private long deviceCount10;
	private long deviceCount11;
	private long deviceCount12;
	private long deviceCount13;
	private long deviceCount14;
	private long deviceCount15;
	private long deviceCount16;
	private long deviceCount17;
	private long deviceCount18;
	private long deviceCount19;
	private long deviceCount20;
	private long deviceCount21;
	private long deviceCount22;
	private long deviceCount23;
	private long deviceCount24;
	private long deviceCount25;
	private long deviceCount26;
	private long deviceCount27;
	private long deviceCount28;
	private long deviceCount29;
	private long deviceCount30;
	private long deviceCount31;

    private String importer;
    private String remark;
    private Date createDate;
	// Constructors

	/** default constructor */
	public Producttype() {
	}

	/** minimal constructor */
	public Producttype(long typeId, String productName, Integer category, long productCount, String originCountry,String originRegion,
			String outDistributors, String netContent, String netContentUnit,String labelProof, String brand, Integer remain, 
			long tagsBought, long tagsApplied, long tagsUsed, Integer tagType) {
		this.typeId = typeId;
		this.productName = productName;
		this.productCount = productCount;
		this.tagsBought = tagsBought;
		this.tagsApplied = tagsApplied;
		this.tagsUsed = tagsUsed;
		this.tagType = tagType;
		this.originRegion = originRegion;
		this.deviceCount0 = 0;
		this.deviceCount1 = 0;
		this.deviceCount2 = 0;
		this.deviceCount3 = 0;
		this.deviceCount4 = 0;
		this.deviceCount5 = 0;
		this.deviceCount6 = 0;
		this.deviceCount7 = 0;
		this.deviceCount8 = 0;
		this.deviceCount9 = 0;
		this.deviceCount10 = 0;
		this.deviceCount11 = 0;
		this.deviceCount12 = 0;
		this.deviceCount13 = 0;
		this.deviceCount14 = 0;
		this.deviceCount15 = 0;
		this.deviceCount16 = 0;
		this.deviceCount17 = 0;
		this.deviceCount18 = 0;
		this.deviceCount19 = 0;
		this.deviceCount20 = 0;
		this.deviceCount21 = 0;
		this.deviceCount22 = 0;
		this.deviceCount23 = 0;
		this.deviceCount24 = 0;
		this.deviceCount25 = 0;
		this.deviceCount26 = 0;
		this.deviceCount27 = 0;
		this.deviceCount28 = 0;
		this.deviceCount29 = 0;
		this.deviceCount30 = 0;
		this.deviceCount31 = 0;
	}

	/** full constructor */
	public Producttype(long typeId, String productName, Integer categoryId,
			long vendorId, Integer authId, long productCount,
			long tagsBought, long tagsApplied, long tagsUsed, Integer tagType, String typeKey,
			Integer batchCount, String shelfLife,String shelfLifeUnit) {
		this.typeId = typeId;
		this.productName = productName;
		this.categoryId = categoryId;
		this.vendorId = vendorId;
		this.authId = authId;
		this.productCount = productCount;
		this.tagsBought = tagsBought;
		this.tagsApplied = tagsApplied;
		this.tagsUsed = tagsUsed;
		this.tagType = tagType;
		this.typeKey = typeKey;
		this.batchCount = batchCount;
		this.shelfLife = shelfLife;
		this.shelfLifeUnit = shelfLifeUnit;
		this.deviceCount0 = 0;
		this.deviceCount1 = 0;
		this.deviceCount2 = 0;
		this.deviceCount3 = 0;
		this.deviceCount4 = 0;
		this.deviceCount5 = 0;
		this.deviceCount6 = 0;
		this.deviceCount7 = 0;
		this.deviceCount8 = 0;
		this.deviceCount9 = 0;
		this.deviceCount10 = 0;
		this.deviceCount11 = 0;
		this.deviceCount12 = 0;
		this.deviceCount13 = 0;
		this.deviceCount14 = 0;
		this.deviceCount15 = 0;
		this.deviceCount16 = 0;
		this.deviceCount17 = 0;
		this.deviceCount18 = 0;
		this.deviceCount19 = 0;
		this.deviceCount20 = 0;
		this.deviceCount21 = 0;
		this.deviceCount22 = 0;
		this.deviceCount23 = 0;
		this.deviceCount24 = 0;
		this.deviceCount25 = 0;
		this.deviceCount26 = 0;
		this.deviceCount27 = 0;
		this.deviceCount28 = 0;
		this.deviceCount29 = 0;
		this.deviceCount30 = 0;
		this.deviceCount31 = 0;
	}
    /** full constructor */
    public Producttype(long typeId, String productName, Integer categoryId,
                       long vendorId, Integer authId, long productCount,
                       long tagsBought, long tagsApplied, long tagsUsed, Integer tagType, String typeKey,
                       Integer batchCount, String shelfLife,String shelfLifeUnit,Date createDate) {
        this.typeId = typeId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.vendorId = vendorId;
        this.authId = authId;
        this.productCount = productCount;
        this.tagsBought = tagsBought;
        this.tagsApplied = tagsApplied;
        this.tagsUsed = tagsUsed;
        this.tagType = tagType;
        this.typeKey = typeKey;
        this.batchCount = batchCount;
        this.shelfLife = shelfLife;
        this.shelfLifeUnit = shelfLifeUnit;
        this.deviceCount0 = 0;
        this.deviceCount1 = 0;
        this.deviceCount2 = 0;
        this.deviceCount3 = 0;
        this.deviceCount4 = 0;
        this.deviceCount5 = 0;
        this.deviceCount6 = 0;
        this.deviceCount7 = 0;
        this.deviceCount8 = 0;
        this.deviceCount9 = 0;
        this.deviceCount10 = 0;
        this.deviceCount11 = 0;
        this.deviceCount12 = 0;
        this.deviceCount13 = 0;
        this.deviceCount14 = 0;
        this.deviceCount15 = 0;
        this.deviceCount16 = 0;
        this.deviceCount17 = 0;
        this.deviceCount18 = 0;
        this.deviceCount19 = 0;
        this.deviceCount20 = 0;
        this.deviceCount21 = 0;
        this.deviceCount22 = 0;
        this.deviceCount23 = 0;
        this.deviceCount24 = 0;
        this.deviceCount25 = 0;
        this.deviceCount26 = 0;
        this.deviceCount27 = 0;
        this.deviceCount28 = 0;
        this.deviceCount29 = 0;
        this.deviceCount30 = 0;
        this.deviceCount31 = 0;
        this.createDate=createDate;
    }
	// Property accessors
	@Id
	@Column(name = "TypeID")
	public long getTypeId() {
		return this.typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	@Column(name = "ProductName", nullable = false, length = 50)
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@Column(name = "Category")
	public Integer getCategory() {
		return this.category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}
	
	@Column(name = "OriginCountry", nullable = false, length = 50)
	public String getOriginCountry() {
		return this.originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	
//	@Column(name = "InDistributors", nullable = false, length = 50)
//	public String getInDistributors() {
//		return this.inDistributors;
//	}
//
//	public void setInDistributors(String inDistributors) {
//		this.inDistributors = inDistributors;
//	}
	
	@Column(name = "OutDistributors", nullable = false, length = 50)
	public String getOutDistributors() {
		return this.outDistributors;
	}

	public void setOutDistributors(String outDistributors) {
		this.outDistributors = outDistributors;
	}
	
	@Column(name = "NetContent", nullable = false, length = 50)
	public String getNetContent() {
		return this.netContent;
	}

	public void setNetContent(String netContent) {
		this.netContent = netContent;
	}
	
	@Column(name = "LabelProof", nullable = false, length = 50)
	public String getLabelProof() {
		return this.labelProof;
	}

	public void setLabelProof(String labelProof) {
		this.labelProof = labelProof;
	}
	
	@Column(name = "Brand", nullable = false, length = 50)
	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Column(name = "Remain")
	public Integer getRemain() {
		return this.remain;
	}

	public void setRemain(Integer remain) {
		this.remain = remain;
	}

	@Column(name = "CategoryID")
	public Integer getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name = "VendorID")
	public long getVendorId() {
		return this.vendorId;
	}

	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}

	@Column(name = "AuthID")
	public Integer getAuthId() {
		return this.authId;
	}

	public void setAuthId(Integer authId) {
		this.authId = authId;
	}

	@Column(name = "ProductCount", nullable = false)
	public long getProductCount() {
		return this.productCount;
	}

	public void setProductCount(long productCount) {
		this.productCount = productCount;
	}

	@Column(name = "TagsBought", nullable = false)
	public long getTagsBought() {
		return this.tagsBought;
	}

	public void setTagsBought(long tagsBought) {
		this.tagsBought = tagsBought;
	}

	@Column(name = "TagsApplied", nullable = true)
	public long getTagsApplied() {
		return tagsApplied;
	}

	public void setTagsApplied(long tagsApplied) {
		this.tagsApplied = tagsApplied;
	}

	@Column(name = "TagsUsed", nullable = false)
	public long getTagsUsed() {
		return this.tagsUsed;
	}

	public void setTagsUsed(long tagsUsed) {
		this.tagsUsed = tagsUsed;
	}

	@Column(name = "TagType", nullable = false)
	public Integer getTagType() {
		return this.tagType;
	}

	public void setTagType(Integer tagType) {
		this.tagType = tagType;
	}

	@Column(name = "TypeKey", length = 16)
	public String getTypeKey() {
		return this.typeKey;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}

	@Column(name = "BatchCount", nullable = false)
	public Integer getBatchCount() {
		return this.batchCount;
	}

	public void setBatchCount(int batchCount) {
		this.batchCount = batchCount;
	}

	@Column(name = "ShelfLife", nullable = false)
	public String getShelfLife() {
		return this.shelfLife;
	}

	public void setShelfLife(String shelfLife) {
		this.shelfLife = shelfLife;
	}

	@Column(name = "TypeInfo", nullable = true)
	public String getTypeInfo() {
		return typeInfo;
	}

	public void setTypeInfo(String typeInfo) {
		this.typeInfo = typeInfo;
	}

	@Column(name = "DeviceCount0", nullable = false)
	public long getDeviceCount0() {
		return deviceCount0;
	}

	public void setDeviceCount0(long deviceCount0) {
		this.deviceCount0 = deviceCount0;
	}

	@Column(name = "DeviceCount1", nullable = false)
	public long getDeviceCount1() {
		return deviceCount1;
	}

	public void setDeviceCount1(long deviceCount1) {
		this.deviceCount1 = deviceCount1;
	}

	@Column(name = "DeviceCount2", nullable = false)
	public long getDeviceCount2() {
		return deviceCount2;
	}

	public void setDeviceCount2(long deviceCount2) {
		this.deviceCount2 = deviceCount2;
	}

	@Column(name = "DeviceCount3", nullable = false)
	public long getDeviceCount3() {
		return deviceCount3;
	}

	public void setDeviceCount3(long deviceCount3) {
		this.deviceCount3 = deviceCount3;
	}

	@Column(name = "DeviceCount4", nullable = false)
	public long getDeviceCount4() {
		return deviceCount4;
	}

	public void setDeviceCount4(long deviceCount4) {
		this.deviceCount4 = deviceCount4;
	}

	@Column(name = "DeviceCount5", nullable = false)
	public long getDeviceCount5() {
		return deviceCount5;
	}

	public void setDeviceCount5(long deviceCount5) {
		this.deviceCount5 = deviceCount5;
	}

	@Column(name = "DeviceCount6", nullable = false)
	public long getDeviceCount6() {
		return deviceCount6;
	}

	public void setDeviceCount6(long deviceCount6) {
		this.deviceCount6 = deviceCount6;
	}

	@Column(name = "DeviceCount7", nullable = false)
	public long getDeviceCount7() {
		return deviceCount7;
	}

	public void setDeviceCount7(long deviceCount7) {
		this.deviceCount7 = deviceCount7;
	}

	@Column(name = "DeviceCount8", nullable = false)
	public long getDeviceCount8() {
		return deviceCount8;
	}

	public void setDeviceCount8(long deviceCount8) {
		this.deviceCount8 = deviceCount8;
	}

	@Column(name = "DeviceCount9", nullable = false)
	public long getDeviceCount9() {
		return deviceCount9;
	}

	public void setDeviceCount9(long deviceCount9) {
		this.deviceCount9 = deviceCount9;
	}

	@Column(name = "DeviceCount10", nullable = false)
	public long getDeviceCount10() {
		return deviceCount10;
	}

	public void setDeviceCount10(long deviceCount10) {
		this.deviceCount10 = deviceCount10;
	}

	@Column(name = "DeviceCount11", nullable = false)
	public long getDeviceCount11() {
		return deviceCount11;
	}

	public void setDeviceCount11(long deviceCount11) {
		this.deviceCount11 = deviceCount11;
	}

	@Column(name = "DeviceCount12", nullable = false)
	public long getDeviceCount12() {
		return deviceCount12;
	}

	public void setDeviceCount12(long deviceCount12) {
		this.deviceCount12 = deviceCount12;
	}

	@Column(name = "DeviceCount13", nullable = false)
	public long getDeviceCount13() {
		return deviceCount13;
	}

	public void setDeviceCount13(long deviceCount13) {
		this.deviceCount13 = deviceCount13;
	}

	@Column(name = "DeviceCount14", nullable = false)
	public long getDeviceCount14() {
		return deviceCount14;
	}

	public void setDeviceCount14(long deviceCount14) {
		this.deviceCount14 = deviceCount14;
	}

	@Column(name = "DeviceCount15", nullable = false)
	public long getDeviceCount15() {
		return deviceCount15;
	}

	public void setDeviceCount15(long deviceCount15) {
		this.deviceCount15 = deviceCount15;
	}

	@Column(name = "DeviceCount16", nullable = false)
	public long getDeviceCount16() {
		return deviceCount16;
	}

	public void setDeviceCount16(long deviceCount16) {
		this.deviceCount16 = deviceCount16;
	}

	@Column(name = "DeviceCount17", nullable = false)
	public long getDeviceCount17() {
		return deviceCount17;
	}

	public void setDeviceCount17(long deviceCount17) {
		this.deviceCount17 = deviceCount17;
	}

	@Column(name = "DeviceCount18", nullable = false)
	public long getDeviceCount18() {
		return deviceCount18;
	}

	public void setDeviceCount18(long deviceCount18) {
		this.deviceCount18 = deviceCount18;
	}

	@Column(name = "DeviceCount19", nullable = false)
	public long getDeviceCount19() {
		return deviceCount19;
	}

	public void setDeviceCount19(long deviceCount19) {
		this.deviceCount19 = deviceCount19;
	}

	@Column(name = "DeviceCount20", nullable = false)
	public long getDeviceCount20() {
		return deviceCount20;
	}

	public void setDeviceCount20(long deviceCount20) {
		this.deviceCount20 = deviceCount20;
	}

	@Column(name = "DeviceCount21", nullable = false)
	public long getDeviceCount21() {
		return deviceCount21;
	}

	public void setDeviceCount21(long deviceCount21) {
		this.deviceCount21 = deviceCount21;
	}

	@Column(name = "DeviceCount22", nullable = false)
	public long getDeviceCount22() {
		return deviceCount22;
	}

	public void setDeviceCount22(long deviceCount22) {
		this.deviceCount22 = deviceCount22;
	}

	@Column(name = "DeviceCount23", nullable = false)
	public long getDeviceCount23() {
		return deviceCount23;
	}

	public void setDeviceCount23(long deviceCount23) {
		this.deviceCount23 = deviceCount23;
	}

	@Column(name = "DeviceCount24", nullable = false)
	public long getDeviceCount24() {
		return deviceCount24;
	}

	public void setDeviceCount24(long deviceCount24) {
		this.deviceCount24 = deviceCount24;
	}

	@Column(name = "DeviceCount25", nullable = false)
	public long getDeviceCount25() {
		return deviceCount25;
	}

	public void setDeviceCount25(long deviceCount25) {
		this.deviceCount25 = deviceCount25;
	}

	@Column(name = "DeviceCount26", nullable = false)
	public long getDeviceCount26() {
		return deviceCount26;
	}

	public void setDeviceCount26(long deviceCount26) {
		this.deviceCount26 = deviceCount26;
	}

	@Column(name = "DeviceCount27", nullable = false)
	public long getDeviceCount27() {
		return deviceCount27;
	}

	public void setDeviceCount27(long deviceCount27) {
		this.deviceCount27 = deviceCount27;
	}

	@Column(name = "DeviceCount28", nullable = false)
	public long getDeviceCount28() {
		return deviceCount28;
	}

	public void setDeviceCount28(long deviceCount28) {
		this.deviceCount28 = deviceCount28;
	}

	@Column(name = "DeviceCount29", nullable = false)
	public long getDeviceCount29() {
		return deviceCount29;
	}

	public void setDeviceCount29(long deviceCount29) {
		this.deviceCount29 = deviceCount29;
	}

	@Column(name = "DeviceCount30", nullable = false)
	public long getDeviceCount30() {
		return deviceCount30;
	}

	public void setDeviceCount30(long deviceCount30) {
		this.deviceCount30 = deviceCount30;
	}

	@Column(name = "DeviceCount31", nullable = false)
	public long getDeviceCount31() {
		return deviceCount31;
	}

	public void setDeviceCount31(long deviceCount31) {
		this.deviceCount31 = deviceCount31;
	}

	public long getDeviceCount(int deviceId)
	{
		switch (deviceId)
		{
		case 0:
			return deviceCount0;
		case 1:
			return deviceCount1;
		case 2:
			return deviceCount2;
		case 3:
			return deviceCount3;
		case 4:
			return deviceCount4;
		case 5:
			return deviceCount5;
		case 6:
			return deviceCount6;
		case 7:
			return deviceCount7;
		case 8:
			return deviceCount8;
		case 9:
			return deviceCount9;
		case 10:
			return deviceCount10;
		case 11:
			return deviceCount11;
		case 12:
			return deviceCount12;
		case 13:
			return deviceCount13;
		case 14:
			return deviceCount14;
		case 15:
			return deviceCount15;
		case 16:
			return deviceCount16;
		case 17:
			return deviceCount17;
		case 18:
			return deviceCount18;
		case 19:
			return deviceCount19;
		case 20:
			return deviceCount20;
		case 21:
			return deviceCount21;
		case 22:
			return deviceCount22;
		case 23:
			return deviceCount23;
		case 24:
			return deviceCount24;
		case 25:
			return deviceCount25;
		case 26:
			return deviceCount26;
		case 27:
			return deviceCount27;
		case 28:
			return deviceCount28;
		case 29:
			return deviceCount29;
		case 30:
			return deviceCount30;
		case 31:
			return deviceCount31;
		}
		return 0;
	}

	public void setDeviceCount(int deviceId, long count)
	{
		switch (deviceId)
		{
		case 0:
			deviceCount0 = count;
			break;
		case 1:
			deviceCount1 = count;
			break;
		case 2:
			deviceCount2 = count;
			break;
		case 3:
			deviceCount3 = count;
			break;
		case 4:
			deviceCount4 = count;
			break;
		case 5:
			deviceCount5 = count;
			break;
		case 6:
			deviceCount6 = count;
			break;
		case 7:
			deviceCount7 = count;
			break;
		case 8:
			deviceCount8 = count;
			break;
		case 9:
			deviceCount9 = count;
			break;
		case 10:
			deviceCount10 = count;
			break;
		case 11:
			deviceCount11 = count;
			break;
		case 12:
			deviceCount12 = count;
			break;
		case 13:
			deviceCount13 = count;
			break;
		case 14:
			deviceCount14 = count;
			break;
		case 15:
			deviceCount15 = count;
			break;
		case 16:
			deviceCount16 = count;
			break;
		case 17:
			deviceCount17 = count;
			break;
		case 18:
			deviceCount18 = count;
			break;
		case 19:
			deviceCount19 = count;
			break;
		case 20:
			deviceCount20 = count;
			break;
		case 21:
			deviceCount21 = count;
			break;
		case 22:
			deviceCount22 = count;
			break;
		case 23:
			deviceCount23 = count;
			break;
		case 24:
			deviceCount24 = count;
			break;
		case 25:
			deviceCount25 = count;
			break;
		case 26:
			deviceCount26 = count;
			break;
		case 27:
			deviceCount27 = count;
			break;
		case 28:
			deviceCount28 = count;
			break;
		case 29:
			deviceCount29 = count;
			break;
		case 30:
			deviceCount30 = count;
			break;
		case 31:
			deviceCount31 = count;
			break;
		}
	}
	
	@Column(name="OriginRegion")
	public String getOriginRegion() {
		return originRegion;
	}

	public void setOriginRegion(String originRegion) {
		this.originRegion = originRegion;
	}
	
	@Column(name="NetContentUnit")
	public String getNetContentUnit() {
		return netContentUnit;
	}

	public void setNetContentUnit(String netContentUnit) {
		this.netContentUnit = netContentUnit;
	}
	
	@Column(name="HSCode")
	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}
	
	@Column(name="CNProofSample")
	public String getCnProofSample() {
		return cnProofSample;
	}

	public void setCnProofSample(String cnProofSample) {
		this.cnProofSample = cnProofSample;
	}
	
	@Column(name="CNProofID")
	public String getCnProofID() {
		return cnProofID;
	}

	public void setCnProofID(String cnProofID) {
		this.cnProofID = cnProofID;
	}

	@Column(name="ManageReport")
	public String getManageReport() {
		return manageReport;
	}

	public void setManageReport(String manageReport) {
		this.manageReport = manageReport;
	}

	@Column(name="ShelfLifeUnit")
	public String getShelfLifeUnit() {
		return shelfLifeUnit;
	}

	public void setShelfLifeUnit(String shelfLifeUnit) {
		this.shelfLifeUnit = shelfLifeUnit;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}
    public String getImporter() {
        return importer;
    }

    public void setImporter(String importer) {
        this.importer = importer;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Column(name="createDate")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}