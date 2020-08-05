package com.toeat.toeat.vo;

public class MetaVo {
	private String total_count;
	private String is_end;
	private String pageable_count;
	private String same_name;
	
	public MetaVo() {
		total_count = null;
		is_end = null;
		pageable_count = null;
		same_name = null;
	}
	
	public MetaVo(Object total_count, Object is_end, 
			Object pageable_count, Object same_name) {
		this.total_count = total_count.toString();
		this.is_end = is_end.toString();
		this.pageable_count = pageable_count.toString();
		this.same_name = same_name.toString();
	}

	public String getTotal() {
		return total_count;
	}
	
	public void setTotal(String total_count) {
		this.total_count = total_count;
	}

	public String getIsEnd() {
		return is_end;
	}
	
	public void setIsEnd(String is_end) {
		this.is_end = is_end;
	}

	public String getPageable() {
		return pageable_count;
	}
	
	public void setPageable(String pageable_count) {
		this.pageable_count = pageable_count;
	}
	
	public String getSameName()	{
		return same_name;
	}
	
	public void setSameName(String same_name)	{
		this.same_name = same_name;
	}
}


