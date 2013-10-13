package com.jinhs.fetch.mirror.enums;

public enum CustomActionConfigEnum {

	FETCH {
		public String getName() {
			return "FETCH";
		}

		public String getIconUrl() {
			return "https://glassfetch.appspot.com/static/images/action_fetch.png";
		}

		public String getType() {
			return MenuItemActionEnum.CUSTOM.getValue();
		}
	},
	
	FETCH_MORE {
		public String getName() {
			return "FETCH MORE";
		}

		public String getIconUrl() {
			return "https://glassfetch.appspot.com/static/images/action_more.png";
		}

		public String getType() {
			return MenuItemActionEnum.CUSTOM.getValue();
		}
	},
	
	FETCH_FIRST {
		public String getName() {
			return "FETCH FIRST";
		}

		public String getIconUrl() {
			return "https://glassfetch.appspot.com/static/images/action_first.png";
		}

		public String getType() {
			return MenuItemActionEnum.CUSTOM.getValue();
		}
	},

	PUSH {
		public String getName() {
			return "PUSH";
		}

		public String getIconUrl() {
			return "https://glassfetch.appspot.com/static/images/action_push.png";
		}

		public String getType() {
			return MenuItemActionEnum.REPLY.getValue();
		}
	},

	LIKE {
		public String getName() {
			return "LIKE";
		}

		public String getIconUrl() {
			return "https://glassfetch.appspot.com/static/images/action_like.png";
		}

		public String getType() {
			return MenuItemActionEnum.CUSTOM.getValue();
		}
	},

	DISLIKE {
		public String getName() {
			return "DISLIKE";
		}

		public String getIconUrl() {
			return "https://glassfetch.appspot.com/static/images/action_dislike.png";
		}

		public String getType() {
			return MenuItemActionEnum.CUSTOM.getValue();
		}
	};

	public String getName() {
		return "";
	}

	public String getCompleteName() {
		return "";
	}

	public String getIconUrl() {
		return "";
	}

	public String getType() {
		return "";
	}
}
