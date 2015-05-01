package ca.core.db;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_BUSI_APPFORMAT", catalog = "cadb")
public class APPFormat implements java.io.Serializable {

	// Fields

	private Long id;
    private Long authid;
    private Long vendorid;
    private Long typeid;
    private Integer flag;
    //private Timestamp savetime;
    private String savetime;
    private String formatinfo;

	// Constructors

	/** default constructor */
	public APPFormat() {
	}

	/** full constructor */
	public APPFormat(Long id,Long authid,Long vendorid,Long typeid,Integer flag,String savetime,String formatinfo) {
        this.id=id;
        this.authid=authid;
        this.vendorid=vendorid;
        this.typeid=typeid;
        this.flag=flag;
        this.savetime=savetime;
        this.formatinfo=formatinfo;
	}

	// Property accessors
    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Column(name="AUTHID")
    public Long getAuthid() {
        return authid;
    }

    public void setAuthid(Long authid) {
        this.authid = authid;
    }
    @Column(name="TYPEID")
    public Long getTypeid() {
        return typeid;
    }

    public void setTypeid(Long typeid) {
        this.typeid = typeid;
    }
    @Column(name="VENDORID")
    public Long getVendorid() {
        return vendorid;
    }

    public void setVendorid(Long vendorid) {
        this.vendorid = vendorid;
    }
    @Column(name="FLAG")
    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
    @Column(name="SAVETIME")
    public String getSavetime() {
        return savetime;
    }

    public void setSavetime(String savetime) {
        this.savetime = savetime;
    }
    @Column(name="FORMATINFO")
    public String getFormatinfo() {
        return formatinfo;
    }

    public void setFormatinfo(String formatinfo) {
        this.formatinfo = formatinfo;
    }
}