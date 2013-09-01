package com.jinhs.fetch.mirror;

public enum CustomActionConfigEnum {

	FETCH {
		public String getName() {
			return "FETCH";
		}

		public String getIconUrl() {
			return "https://glassfetch.appspot.com/static/images/chipotle-tube-640x360.jpg";
		}

		public String getType() {
			return MenuItemActionEnum.REPLY.getValue();
		}

		public boolean hasPending() {
			return false;
		}

		public boolean hasComplete() {
			return false;
		}

		public String getPendingName() {
			return "Pending";
		}

		public String getCompleteName() {
			return "Complete";
		}

		public String getPendingIconUrl() {
			return "https://glassfetch.appspot.com/static/images/chipotle-tube-640x360.jpg";
		}

		public String getCompleteIconUrl() {
			return "https://glassfetch.appspot.com/static/images/chipotle-tube-640x360.jpg";
		}
	},

	PUSH {
		public String getName() {
			return "PUSH";
		}

		public String getIconUrl() {
			return "https://glassfetch.appspot.com/static/images/chipotle-tube-640x360.jpg";
		}

		public String getType() {
			return MenuItemActionEnum.REPLY.getValue();
		}

		public boolean hasPending() {
			return false;
		}

		public boolean hasComplete() {
			return false;
		}

		public String getPendingName() {
			return "Pending";
		}

		public String getCompleteName() {
			return "Complete";
		}

		public String getPendingIconUrl() {
			return "https://glassfetch.appspot.com/static/images/chipotle-tube-640x360.jpg";
		}

		public String getCompleteIconUrl() {
			return "https://glassfetch.appspot.com/static/images/chipotle-tube-640x360.jpg";
		}
	},

	LIKE {
		public String getName() {
			return "LIKE";
		}

		public String getIconUrl() {
			return "https://glassfetch.appspot.com/static/images/chipotle-tube-640x360.jpg";
		}

		public String getType() {
			return MenuItemActionEnum.CUSTOM.getValue();
		}

		public boolean hasPending() {
			return true;
		}

		public boolean hasComplete() {
			return true;
		}

		public String getPendingName() {
			return "Pending";
		}

		public String getCompleteName() {
			return "Complete";
		}

		public String getPendingIconUrl() {
			return "https://glassfetch.appspot.com/static/images/chipotle-tube-640x360.jpg";
		}

		public String getCompleteIconUrl() {
			return "https://glassfetch.appspot.com/static/images/chipotle-tube-640x360.jpg";
		}
	},

	DISLIKE {
		public String getName() {
			return "DISLIKE";
		}

		public String getIconUrl() {
			return "https://glassfetch.appspot.com/static/images/chipotle-tube-640x360.jpg";
		}

		public String getType() {
			return MenuItemActionEnum.CUSTOM.getValue();
		}

		public boolean hasPending() {
			return true;
		}

		public boolean hasComplete() {
			return true;
		}

		public String getPendingName() {
			return "Pending";
		}

		public String getCompleteName() {
			return "Complete";
		}

		public String getPendingIconUrl() {
			return "https://glassfetch.appspot.com/static/images/chipotle-tube-640x360.jpg";
		}

		public String getCompleteIconUrl() {
			return "https://glassfetch.appspot.com/static/images/chipotle-tube-640x360.jpg";
		}
	};

	public String getName() {
		return "";
	}

	public String getPendingName() {
		return "";
	}

	public String getCompleteName() {
		return "";
	}

	public String getIconUrl() {
		return "";
	}

	public String getPendingIconUrl() {
		return "";
	}

	public String getCompleteIconUrl() {
		return "";
	}

	public String getType() {
		return "";
	}

	public boolean hasPending() {
		return false;
	}

	public boolean hasComplete() {
		return false;
	}
}
