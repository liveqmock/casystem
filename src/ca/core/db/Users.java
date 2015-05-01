package ca.core.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Users entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "users", catalog = "cadb")
public class Users implements java.io.Serializable {

	// Fields

	private Integer userId;
	private Integer userType;
	private String userName;
	private String userPassword;
	private Integer companyId;
	private String userTelephone;
	private String userMobilephone;
	private String userEmail;
	private String userPermission;
	private Integer userStatus;
	private Integer fatherId;

	// Constructors

	/** default constructor */
	public Users() {
		this.userEmail = "";
		this.userMobilephone = "";
		this.userTelephone = "";
	}

	/** minimal constructor */
	public Users(Integer userType, String userName, String userPassword,
			Integer companyId, String userPermission) {
		this.userType = userType;
		this.userName = userName;
		this.userPassword = userPassword;
		this.companyId = companyId;
		this.userPermission = userPermission;
	}

	/** full constructor */
	public Users(Integer userType, String userName, String userPassword,
			Integer companyId, String userTelephone, String userMobilephone,
			String userEmail, String userPermission, Integer userStatus,
			Integer fatherId) {
		this.userType = userType;
		this.userName = userName;
		this.userPassword = userPassword;
		this.companyId = companyId;
		this.userTelephone = userTelephone;
		this.userMobilephone = userMobilephone;
		this.userEmail = userEmail;
		this.userPermission = userPermission;
		this.userStatus = userStatus;
		this.fatherId = fatherId;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "UserID", nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "UserType", nullable = false)
	public Integer getUserType() {
		return this.userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	@Column(name = "UserName", nullable = false)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "UserPassword", nullable = false)
	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	@Column(name = "CompanyID", nullable = false)
	public Integer getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	@Column(name = "UserTelephone")
	public String getUserTelephone() {
		return this.userTelephone;
	}

	public void setUserTelephone(String userTelephone) {
		this.userTelephone = userTelephone;
	}

	@Column(name = "UserMobilephone")
	public String getUserMobilephone() {
		return this.userMobilephone;
	}

	public void setUserMobilephone(String userMobilephone) {
		this.userMobilephone = userMobilephone;
	}

	@Column(name = "UserEmail")
	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Column(name = "UserPermission", nullable = false)
	public String getUserPermission() {
		return this.userPermission;
	}

	public void setUserPermission(String userPermission) {
		this.userPermission = userPermission;
	}

	@Column(name = "UserStatus")
	public Integer getUserStatus() {
		return this.userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	@Column(name = "FatherID")
	public Integer getFatherId() {
		return this.fatherId;
	}

	public void setFatherId(Integer fatherId) {
		this.fatherId = fatherId;
	}

}