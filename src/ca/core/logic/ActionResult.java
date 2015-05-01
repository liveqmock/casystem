package ca.core.logic;

public class ActionResult {
	private Integer Result;
	private String  FailInfo;
	private Object  Data;
	
	public ActionResult()
	{
	}
	
	public Integer getResult()
	{
		return Result;
	}
	
	public void setResult(Integer result)
	{
		this.Result = result;
	}

	public String getFailInfo()
	{
		return FailInfo;
	}
	
	public void setFailInfo(String failInfo)
	{
		this.FailInfo = failInfo;
	}
	
	public Object getData()
	{
		return Data;
	}
	
	public void setData(Object data)
	{
		this.Data = data;
	}
}
