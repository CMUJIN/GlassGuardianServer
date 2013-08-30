package com.jinhs.fetch.mirror;
public enum CustomMenuItemActionEnum {

	FETCH{
		public String getValue(){
			return "FETCH";
		}
	},
	
	PUSH{
		public String getValue(){
			return "PUSH";
		}
	},
	
	LIKE{
		public String getValue(){
			return "LIKE";
		}
	},
	
	DISLIKE{
		public String getValue(){
			return "DISLIKE";
		}
	};
	
	public String getValue(){
		return "";
	}

}
