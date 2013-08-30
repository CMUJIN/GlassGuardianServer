package com.jinhs.fetch.mirror;
/*
 * CUSTOM - Custom action set by the service. When the user selects this menuItem, the API triggers a notification to your callbackUrl with the userActions.type set to CUSTOM and the userActions.payload set to the ID of this menu item. This is the default value.
 * Built-in actions:
 * REPLY - Initiate a reply to the timeline item using the voice recording UI. The creator attribute must be set in the timeline item for this menu to be available.
 * REPLY_ALL - Same behavior as REPLY. The original timeline item's recipients will be added to the reply item.
 * DELETE - Delete the timeline item.
 * SHARE - Share the timeline item with the available contacts.
 * READ_ALOUD - Read the timeline item's speakableText aloud; if this field is not set, read the text field; if none of those fields are set, this menu item is ignored.
 * VOICE_CALL - Initiate a phone call using the timeline item's creator.phone_number attribute as recipient.
 * NAVIGATE - Navigate to the timeline item's location.
 * TOGGLE_PINNED - Toggle the isPinned state of the timeline item.
 * VIEW_WEBSITE - Open the payload of the menu item in the browser.
 * */
public enum MenuItemActionEnum {

	CUSTOM{
		public String getValue(){
			return "CUSTOM";
		}
	},
	
	REPLY{
		public String getValue(){
			return "REPLY";
		}
	},
	
	DELETE{
		public String getValue(){
			return "DELETE";
		}
	},
	
	SHARE{
		public String getValue(){
			return "SHARE";
		}
	},
	
	REPLY_ALL{
		public String getValue(){
			return "REPLY_ALL";
		}
	},
	
	READ_ALOUD{
		public String getValue(){
			return "READ_ALOUD";
		}
	},
	
	VOICE_CALL{
		public String getValue(){
			return "VOICE_CALL";
		}
	},
	
	NAVIGATE{
		public String getValue(){
			return "NAVIGATE";
		}
	},
	
	TOGGLE_PINNED{
		public String getValue(){
			return "TOGGLE_PINNED";
		}
	},
	
	VIEW_WEBSITE{
		public String getValue(){
			return "VIEW_WEBSITE";
		}
	};
	
	public String getValue(){
		return "";
	}

}
