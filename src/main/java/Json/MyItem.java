package Json;

public class MyItem {

    private String caseId;
    private String caseName;
    private String registerReason;

    private String belongCity;
    private String representImage;
    private String historyDevelopment;

    @Override
    public String toString() {
        return "MyItem{" +
                "caseId='" + caseId + '\'' +
                ", caseName='" + caseName + '\'' +
                ", registerReason='" + registerReason + '\'' +
                ", belongCity='" + belongCity + '\'' +
                ", representImage='" + representImage + '\'' +
                ", historyDevelopment='" + historyDevelopment + '\'' +
                '}';
    }

    public MyItem(String caseId, String caseName, String registerReason, String belongCity, String representImage, String historyDevelopment) {
        this.caseId = caseId;
        this.caseName = caseName;
        this.registerReason = registerReason;
        this.belongCity = belongCity;
        this.representImage = representImage;
        this.historyDevelopment = historyDevelopment;
    }

    public String getCaseId() {
        return caseId;
    }

    public String getCaseName() {
        return caseName;
    }

    public String getRegisterReason() {
        return registerReason;
    }

    public String getBelongCity() {
        return belongCity;
    }

    public String getRepresentImage() {
        return representImage;
    }

    public String getHistoryDevelopment() {
        return historyDevelopment;
    }
}
