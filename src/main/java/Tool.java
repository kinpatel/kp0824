public class Tool {
    private String toolCode;
    private ToolType toolType;
    private String Brand;

    public Tool(String toolCode, ToolType toolType, String brand) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        Brand = brand;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public ToolType getToolType() {
        return toolType;
    }

    public void setToolType(ToolType toolType) {
        this.toolType = toolType;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }
}
