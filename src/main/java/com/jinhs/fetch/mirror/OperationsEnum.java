package com.jinhs.fetch.mirror;

public enum OperationsEnum {
	UPDATE{
		public String getValue(){
			return "UPDATE";
		}
	},
	
	INSERT{
		public String getValue(){
			return "INSERT";
		}
	},
	
	DELETE{
		public String getValue(){
			return "DELETE";
		}
	};
	
	public String getValue(){
		return "";
	}
}
