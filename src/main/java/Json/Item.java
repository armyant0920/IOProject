package Json;

import java.util.List;

public class Item {

    /**
     * caseId : 20140804000001
     * caseName : 毫芒雕刻
     * assetsClassifyCode : 4.2.1
     * assetsClassifyName : 傳統工藝
     * assetsTypes : [{"code":"E1.99","name":"其他","subCode":null,"subName":null,"other":"雕刻"}]
     * computeType : [{"type":"漢民族","name":"福佬","otherRemark":null}]
     * judgeCriteria : ["藝術性:具有藝術價值者","特殊性:構成傳統藝術之特殊藝能表現,其技法優秀者"]
     * registerReason : 1.投入毫芒雕刻三十多年，技法精湛，作品細膩，是相當難得的藝術工作者。
     2.藝術價值甚高，毫芒雕刻具特殊性與稀有性。
     3.毫芒雕刻，精緻細膩之工藝，罕見於雕刻藝界，值得保存。
     * lawsReference : 依據傳統藝術民俗及有關文物登錄指定及廢止審查辦法第2條第1款第1、2、3目。
     * officialDocNo : 府授文資字第10351027981號
     * registerDate : 2014-08-04
     * govInstitutionName : 嘉義市政府
     * belongCity : 嘉義市西區
     * representImage : https://data.boch.gov.tw/old_upload/_upload/Assets_new/traditional_art/278/photo/毫芒雕刻.jpg
     * representImageSource : 蕭武龍先生提供
     * recorder : 蕭武龍
     * historyDevelopment : 毫芒雕刻是我國傳統的雕刻藝術之一，此藝術將文字、圖案、水墨畫山水、刻劃於象牙、鯨魚牙、果核、龜殼、鱗片、珊瑚之類上；觀者無法以肉眼一窺全貌，必須借助十倍以上放大鏡才得見一筆一畫纖毫世界。毫芒雕刻是一門吃力而難以討好的冷門工藝，更是我國特殊稀有的藝術功夫。蕭武龍於1980年獸毫芒雕刻大師謝宗安老師啟蒙。
     */

    private String caseId;
    private String caseName;
    private String assetsClassifyCode;
    private String assetsClassifyName;
    private String registerReason;
    private String lawsReference;
    private String officialDocNo;
    private String registerDate;
    private String govInstitutionName;
    private String belongCity;
    private String representImage;
    private String representImageSource;
    private String recorder;
    private String historyDevelopment;
    /**
     * code : E1.99
     * name : 其他
     * subCode : null
     * subName : null
     * other : 雕刻
     */

    private List<AssetsTypesBean> assetsTypes;
    /**
     * type : 漢民族
     * name : 福佬
     * otherRemark : null
     */

    private List<ComputeTypeBean> computeType;
    private List<String> judgeCriteria;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getAssetsClassifyCode() {
        return assetsClassifyCode;
    }

    public void setAssetsClassifyCode(String assetsClassifyCode) {
        this.assetsClassifyCode = assetsClassifyCode;
    }

    public String getAssetsClassifyName() {
        return assetsClassifyName;
    }

    public void setAssetsClassifyName(String assetsClassifyName) {
        this.assetsClassifyName = assetsClassifyName;
    }

    public String getRegisterReason() {
        return registerReason;
    }

    public void setRegisterReason(String registerReason) {
        this.registerReason = registerReason;
    }

    public String getLawsReference() {
        return lawsReference;
    }

    public void setLawsReference(String lawsReference) {
        this.lawsReference = lawsReference;
    }

    public String getOfficialDocNo() {
        return officialDocNo;
    }

    public void setOfficialDocNo(String officialDocNo) {
        this.officialDocNo = officialDocNo;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getGovInstitutionName() {
        return govInstitutionName;
    }

    public void setGovInstitutionName(String govInstitutionName) {
        this.govInstitutionName = govInstitutionName;
    }

    public String getBelongCity() {
        return belongCity;
    }

    public void setBelongCity(String belongCity) {
        this.belongCity = belongCity;
    }

    public String getRepresentImage() {
        return representImage;
    }

    public void setRepresentImage(String representImage) {
        this.representImage = representImage;
    }

    public String getRepresentImageSource() {
        return representImageSource;
    }

    public void setRepresentImageSource(String representImageSource) {
        this.representImageSource = representImageSource;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public String getHistoryDevelopment() {
        return historyDevelopment;
    }

    public void setHistoryDevelopment(String historyDevelopment) {
        this.historyDevelopment = historyDevelopment;
    }

    public List<AssetsTypesBean> getAssetsTypes() {
        return assetsTypes;
    }

    public void setAssetsTypes(List<AssetsTypesBean> assetsTypes) {
        this.assetsTypes = assetsTypes;
    }

    public List<ComputeTypeBean> getComputeType() {
        return computeType;
    }

    public void setComputeType(List<ComputeTypeBean> computeType) {
        this.computeType = computeType;
    }

    public List<String> getJudgeCriteria() {
        return judgeCriteria;
    }

    public void setJudgeCriteria(List<String> judgeCriteria) {
        this.judgeCriteria = judgeCriteria;
    }

    public static class AssetsTypesBean {
        private String code;
        private String name;
        private Object subCode;
        private Object subName;
        private String other;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getSubCode() {
            return subCode;
        }

        public void setSubCode(Object subCode) {
            this.subCode = subCode;
        }

        public Object getSubName() {
            return subName;
        }

        public void setSubName(Object subName) {
            this.subName = subName;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
            this.other = other;
        }
    }

    public static class ComputeTypeBean {
        private String type;
        private String name;
        private Object otherRemark;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getOtherRemark() {
            return otherRemark;
        }

        public void setOtherRemark(Object otherRemark) {
            this.otherRemark = otherRemark;
        }
    }
}
