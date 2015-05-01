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
@Table(name = "loginfo", catalog = "cadb")
public class Loginfo implements java.io.Serializable {

	// Fields

	private Integer logId;
	private Date logTime;
	private Integer logType;
	private Integer userId;
	private String className;
	private long typeId;
	private String level;
	private String logInfo;

	// Constructors

	/** default constructor */
	public Loginfo() {
	}

	/** full constructor */
	public Loginfo(Date logTime, Integer logType, Integer userId,
			String className, long typeId, String logInfo) {
		this.logTime = logTime;
		this.logType = logType;
		this.userId = userId;
		this.className = className;
		this.typeId = typeId;
		this.logInfo = logInfo;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "LogID", unique = true, nullable = false)
	public Integer getLogId() {
		return this.logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	@Column(name = "LogTime", length = 19)
	public Date getLogTime() {
		return this.logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	@Column(name = "LogType")
	public Integer getLogType() {
		return this.logType;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	@Column(name = "UserID")
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "ClassName", length = 256)
	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name = "TypeID")
	public long getTypeId() {
		return this.typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	@Column(name = "LogInfo", length = 2000)
	public String getLogInfo() {
		return this.logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	@Column(name = "Level", length = 20)
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}