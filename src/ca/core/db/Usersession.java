package ca.core.db;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * Loginfo entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "usersession", catalog = "cadb")
public class Usersession implements java.io.Serializable {

	// Fields

	private Integer sessionId;
	private String  sessionToken;
	private Integer userId;
	private Date    lastTime;
	private Integer status;

	// Constructors

	/** default constructor */
	public Usersession() {
	}

	/** full constructor */
	public Usersession(Date lastTime, String sessionToken, Integer userId,
			Integer status) {
		this.lastTime = lastTime;
		this.sessionToken = sessionToken;
		this.userId = userId;
		this.status = status;

	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "SessionID", unique = true, nullable = false)
	public Integer getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	@Column(name = "LastTime", length = 19)
	public Date getLastTime() {
		return this.lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	@Column(name = "SessionToken", length = 50)
	public String getSessionToken() {
		return this.sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	@Column(name = "UserID")
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "Status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}