package ca.core.db;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * Tagdeals entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tagdeals", catalog = "cadb")
public class Tagdeals implements java.io.Serializable {

	// Fields

	private Integer dealsId;
	private Integer productCount;
	private Date dealsTime;
	private Integer dealsType;
	private long typeId;
	private Integer userId;
	private double amount;

	// Constructors

	/** default constructor */
	public Tagdeals() {
	}

	/** minimal constructor */
	public Tagdeals(Integer productCount, Date dealsTime, Integer dealsType) {
		this.productCount = productCount;
		this.dealsTime = dealsTime;
		this.dealsType = dealsType;
	}

	/** full constructor */
	public Tagdeals(Integer productCount, Date dealsTime, Integer dealsType,
			long typeId, Integer userId, double amount) {
		this.productCount = productCount;
		this.dealsTime = dealsTime;
		this.dealsType = dealsType;
		this.typeId = typeId;
		this.userId = userId;
		this.amount = amount;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "DealsID", nullable = false)
	public Integer getDealsId() {
		return this.dealsId;
	}

	public void setDealsId(Integer dealsId) {
		this.dealsId = dealsId;
	}

	@Column(name = "ProductCount", nullable = false)
	public Integer getProductCount() {
		return this.productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	@Column(name = "DealsTime", nullable = false, length = 19)
	public Date getDealsTime() {
		return this.dealsTime;
	}

	public void setDealsTime(Date dealsTime) {
		this.dealsTime = dealsTime;
	}

	@Column(name = "DealsType", nullable = false)
	public Integer getDealsType() {
		return this.dealsType;
	}

	public void setDealsType(Integer dealsType) {
		this.dealsType = dealsType;
	}

	@Column(name = "TypeID")
	public long getTypeId() {
		return this.typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	@Column(name = "UserID")
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "Amount", precision = 22, scale = 0)
	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}