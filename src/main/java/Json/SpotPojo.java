package Json;

import java.sql.Date;

public class SpotPojo {

    /**
     * caseId : 20110216000001
     * caseName : 北門驛與阿里山森林鐵道
     * assetsTypeCode : D6
     * assetsTypeName : 農林漁牧景觀
     * judgeCriteria : ["表現人類與自然互動具有文化意義","具紀念性、代表性或特殊性之歷史、文化、藝術或科學價值","具罕見性"]
     * registerReason : (一)嘉義車庫園區：園區內有蒸汽機車展示、轉車台及陳列專
     供先總統蔣公搭乘用之貴賓車2台。
     (二)北門驛：北門驛為嘉義市市定古蹟。
     (三)窄軌鐵道。
     * lawsReference : 文化景觀登錄及廢止審查辦法第3條
     * officialDocNo : 府授文資字第1005100471
     * registerDate : 2011-02-16
     * govInstitutionName : 嘉義市政府
     * belongAddress : 嘉義市段鐵道由臺灣鐵路管理局嘉義站阿里山窄軌鐵道起至崎頂平交道止，計4公里460公尺。
     * belongCity : 嘉義市東區
     * longitude : 120.454359
     * latitude : 23.487179
     * representImage : https://data.boch.gov.tw/old_upload/_upload/Assets_new/cultural_tourism/212/photo/北門驛與阿里山森林鐵道.jpg
     * representImageSource :
     * content : 由臺灣鐵路管理局嘉義站起，經嘉義車庫園區、北門車站至崎頂平交道。
     * landscapeRelated : 嘉義製材所的產業歷史
     臨近的營林俱樂部與營林所宿舍群
     北門修理工廠的保存歷程與文物
     貯木池景觀與變貌
     林森路木材街的經濟活動
     * briefDescribe : 阿里山森林鐵道～於明治39年（1906）由日籍營造商藤田組取得森林經營權，進入山區展開先期作業，明治41年（1908）嘉義至竹崎的平地段率先完工，明治42年（1909），由官方的「阿里山作業所」接手。大正1年(1912)，嘉義至萬平路段通車，大正3年（1914），嘉義至沼平路段通車，阿里山森林鐵路的主線宣告完成。
     北門驛～興建於明治41（1908）年。北門驛是阿里山森林鐵路的運材終點站，也是貨運起點站。大正7（1918）年阿里山森林鐵路增加了客運服務，運材列車後方加掛客車車廂，供沿線居民以及登山者搭乘，北門驛也成為通勤列車的停靠站之一。
     營林設施群～北門修理工廠：於大正元年（1912）啟用，負責維修各式火車頭及車廂，以及台車、集材機、起重機、架空索道、製材機、模具機等運輸及製材設備，同時自行生產蒸汽火車頭的各式零件。嘉義製材所：大正3年(1914)完工啟用，產能及製材品質冠稱全東亞地區。營林俱樂部：興建於大正年間，提供外地貴賓及在地員工休閒娛樂的空間。貯木池：大正14年（1925）開鑿竣工。
     * landUsage : 林業文化產業專用區
     * cityLand : 都市地區 保存區
     * notes :
     * specialValue :
     * actualState : 嘉義至竹崎段已於99年12月25日起逢周
     六日恢復行駛 。
     * feature : (一)嘉義車庫園區：園區內有蒸汽機車展示、轉車台及陳列專
     供先總統蔣公搭乘用之貴賓車2台。
     (二)北門驛：北門驛為嘉義市市定古蹟。
     (三)窄軌鐵道。
     */

    private String caseId;
    private String caseName;
    private String registerDate;
    private String belongCity;
    private double longitude;
    private double latitude;
    private String representImage;
    private String briefDescribe;
    public SpotPojo(){

    }
    public SpotPojo(String caseId, String caseName, String registerDate, String belongCity, double longitude, double latitude, String representImage, String briefDescribe) {
        this.caseId = caseId;
        this.caseName = caseName;
        this.registerDate = registerDate;
        this.belongCity = belongCity;
        this.longitude = longitude;
        this.latitude = latitude;
        this.representImage = representImage;
        this.briefDescribe = briefDescribe;
    }

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

    public String getRegisterDate() {
        return registerDate;
    }
    public Date getSqlDate(){
        String[]date=this.registerDate.split("-");
        for(int i=0;i<date.length;i++){
            System.out.print(date[i]+",");
        }
        System.out.println();
        return new Date(Integer.parseInt(date[0])-1900,Integer.parseInt(date[1]),Integer.parseInt(date[2]));
    }


    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getBelongCity() {
        return belongCity;
    }

    public void setBelongCity(String belongCity) {
        this.belongCity = belongCity;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getRepresentImage() {
        return representImage;
    }

    public void setRepresentImage(String representImage) {
        this.representImage = representImage;
    }

    public String getBriefDescribe() {
        return briefDescribe;
    }

    public void setBriefDescribe(String briefDescribe) {
        this.briefDescribe = briefDescribe;
    }

    @Override
    public String toString() {
        return "SpotPojo{" +
                "caseId='" + caseId + '\'' +
                ", caseName='" + caseName + '\'' +
                ", registerDate='" + registerDate + '\'' +
                ", belongCity='" + belongCity + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", representImage='" + representImage + '\'' +
                ", briefDescribe='" + briefDescribe + '\'' +
                '}';
    }
}
