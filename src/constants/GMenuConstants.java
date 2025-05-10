package constants;

public class GMenuConstants {
	public enum EFileMenuItem {
		eNew("New"),
		eOpen("Open"),
		eClose("Close"),
		eSave("Save"),
		eSaveAs("Save as"),
		ePrint("Print"),
		eImport("Import"),
		eExport("Export"),
		eExit("Exit");
		
		private String menu;
		EFileMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum EEditMenuItem {
		eProperty("Property"),
		eUndo("Undo"),
		eForward("Forward"),
		eBackward("Backward"),
		eFade("Fade"),
		eCut("Cut"),
		eCopy("Copy"),
		ePaste("Paste"),
		eClear("Clear"),
		eFill("Fill"),
		eColorSetting("Color Setting");
		private String text;
		EEditMenuItem(String text) {this.text = text;}
		public String getText() {return this.text;}
	}


	public enum EGraphicMenuItem {
		eLineThickness("Line Thickness"),
		eLineStyle("Line Style"),
		eFontStyle("Font Style"),
		eFontSize("Font Size");

		private String menu;
		EGraphicMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum EHelpMenuItem {
		eSystemInfo("System Info"),
		eAbout("about"),
		eOnline("online");

		private String menu;
		EHelpMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum EImageMenuItem {
		eMode("Mode"),
		eImageSize("Image Size"),
		eCanvasSize("Canvas Size"),
		eRotate("Rotate Image"),
		eCrop("Crop"),
		eTrim("Trim"),
		eDuplicate("Duplicate");
		private String menu;
		EImageMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum ELayerMenuItem {
		eNewLayer("New Layer"),
		eDuplicate("Duplicate"),
		eDeleteLayer("Delete Layer"),
		eRenameLayer("Rename Layer"),
		eLock("Lock"),
		eMerge("Merge"),
		eHide("Hide");
		private String menu;
		ELayerMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum ESelectMenuItem {
		eAll("All"),
		eDeselect("Deselect"),
		eReselect("ReSelect"),
		eAllLayer("All Layer");

		private String menu;
		ESelectMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum EFilterMenuItem {
		eBlur("Blur"),
		eNoise("Noise");

		private String menu;
		EFilterMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum ETypeMenuItem {
		ePanel("Panel"),
		eLanguage("Language"),
		eForward("Forward");

		private String menu;
		ETypeMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum EWindowMenuItem {
		eProperty("Workspace"),
		eExtension("Extension"),
		eOption("Option"),
		eTool("Tool"),
		eHistory("History"),
		ePreFerence("Preference"),;
		private String menu;
		EWindowMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

}
