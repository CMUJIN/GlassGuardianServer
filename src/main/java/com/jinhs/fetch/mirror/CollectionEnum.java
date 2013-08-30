package com.jinhs.fetch.mirror;

public enum CollectionEnum {
	TIMELINE{
		public String getValue(){
			return "timeline";
		}
	},
	
	LOCATION{
		public String getValue(){
			return "location";
		}
	};
	
	public String getValue(){
		return "";
	}
}
