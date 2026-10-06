package com.nhnacademy.agent.util;

import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

/**
 *  지역명 -> 지역 코드 (시/도, 시/군/구) => 교재 참고
 */
@Slf4j
public class RegionCodeMapper {
    // 지역명 -> 지역 코드 (시/도)
    private static final Map<String, String> REGION_MAP = new HashMap<>();

    // 세부지역명 -> 코드 (시/군/구)
    private static final Map<String, String> DETAIL_REGION_MAP = new HashMap<>();
    static {
        // ========== 시/도 단위 (2자리 코드) ==========

        // 서울특별시 (11)
        REGION_MAP.put("서울", "11");
        REGION_MAP.put("서울특별시", "11");
        REGION_MAP.put("seoul", "11");

        // 부산광역시 (21)
        REGION_MAP.put("부산", "21");
        REGION_MAP.put("부산광역시", "21");
        REGION_MAP.put("busan", "21");

        // 대구광역시 (22)
        REGION_MAP.put("대구", "22");
        REGION_MAP.put("대구광역시", "22");
        REGION_MAP.put("daegu", "22");

        // 인천광역시 (23)
        REGION_MAP.put("인천", "23");
        REGION_MAP.put("인천광역시", "23");
        REGION_MAP.put("incheon", "23");

        // 광주광역시 (24)
        REGION_MAP.put("광주", "24");
        REGION_MAP.put("광주광역시", "24");
        REGION_MAP.put("gwangju", "24");

        // 대전광역시 (25)
        REGION_MAP.put("대전", "25");
        REGION_MAP.put("대전광역시", "25");
        REGION_MAP.put("daejeon", "25");

        // 울산광역시 (26)
        REGION_MAP.put("울산", "26");
        REGION_MAP.put("울산광역시", "26");
        REGION_MAP.put("ulsan", "26");

        // 세종특별자치시 (29)
        REGION_MAP.put("세종", "29");
        REGION_MAP.put("세종특별자치시", "29");
        REGION_MAP.put("sejong", "29");

        // 경기도 (31)
        REGION_MAP.put("경기", "31");
        REGION_MAP.put("경기도", "31");
        REGION_MAP.put("gyeonggi", "31");

        // 강원특별자치도 (32)
        REGION_MAP.put("강원", "32");
        REGION_MAP.put("강원도", "32");
        REGION_MAP.put("강원특별자치도", "32");
        REGION_MAP.put("gangwon", "32");

        // 충청북도 (33)
        REGION_MAP.put("충북", "33");
        REGION_MAP.put("충청북도", "33");
        REGION_MAP.put("chungbuk", "33");

        // 충청남도 (34)
        REGION_MAP.put("충남", "34");
        REGION_MAP.put("충청남도", "34");
        REGION_MAP.put("chungnam", "34");

        // 전북특별자치도 (35)
        REGION_MAP.put("전북", "35");
        REGION_MAP.put("전라북도", "35");
        REGION_MAP.put("jeonbuk", "35");

        // 전라남도 (36)
        REGION_MAP.put("전남", "36");
        REGION_MAP.put("전라남도", "36");
        REGION_MAP.put("jeonnam", "36");

        // 경상북도 (37)
        REGION_MAP.put("경북", "37");
        REGION_MAP.put("경상북도", "37");
        REGION_MAP.put("gyeongbuk", "37");

        // 경상남도 (38)
        REGION_MAP.put("경남", "38");
        REGION_MAP.put("경상남도", "38");
        REGION_MAP.put("gyeongnam", "38");

        // 제주특별자치도 (39)
        REGION_MAP.put("제주", "39");
        REGION_MAP.put("제주도", "39");
        REGION_MAP.put("제주특별자치도", "39");
        REGION_MAP.put("jeju", "39");

        // ========== 시/군/구 단위 (5자리 코드) ==========

        // 서울특별시 구 (11xxx)
        DETAIL_REGION_MAP.put("종로구", "11010");
        DETAIL_REGION_MAP.put("서울 중구", "11020");
        DETAIL_REGION_MAP.put("용산구", "11030");
        DETAIL_REGION_MAP.put("성동구", "11040");
        DETAIL_REGION_MAP.put("광진구", "11050");
        DETAIL_REGION_MAP.put("동대문구", "11060");
        DETAIL_REGION_MAP.put("중랑구", "11070");
        DETAIL_REGION_MAP.put("성북구", "11080");
        DETAIL_REGION_MAP.put("강북구", "11090");
        DETAIL_REGION_MAP.put("도봉구", "11100");
        DETAIL_REGION_MAP.put("노원구", "11110");
        DETAIL_REGION_MAP.put("은평구", "11120");
        DETAIL_REGION_MAP.put("서대문구", "11130");
        DETAIL_REGION_MAP.put("마포구", "11140");
        DETAIL_REGION_MAP.put("양천구", "11150");
        DETAIL_REGION_MAP.put("서울 강서구", "11160");
        DETAIL_REGION_MAP.put("구로구", "11170");
        DETAIL_REGION_MAP.put("금천구", "11180");
        DETAIL_REGION_MAP.put("영등포구", "11190");
        DETAIL_REGION_MAP.put("동작구", "11200");
        DETAIL_REGION_MAP.put("관악구", "11210");
        DETAIL_REGION_MAP.put("서초구", "11220");
        DETAIL_REGION_MAP.put("강남구", "11230");
        DETAIL_REGION_MAP.put("송파구", "11240");
        DETAIL_REGION_MAP.put("강동구", "11250");

        // 부산광역시 구 (21xxx)
        DETAIL_REGION_MAP.put("부산 중구", "21010");
        DETAIL_REGION_MAP.put("부산 서구", "21020");
        DETAIL_REGION_MAP.put("부산 동구", "21030");
        DETAIL_REGION_MAP.put("영도구", "21040");
        DETAIL_REGION_MAP.put("부산진구", "21050");
        DETAIL_REGION_MAP.put("동래구", "21060");
        DETAIL_REGION_MAP.put("부산 남구", "21070");
        DETAIL_REGION_MAP.put("부산 북구", "21080");
        DETAIL_REGION_MAP.put("해운대구", "21090");
        DETAIL_REGION_MAP.put("사하구", "21100");
        DETAIL_REGION_MAP.put("금정구", "21110");
        DETAIL_REGION_MAP.put("부산 강서구", "21120");
        DETAIL_REGION_MAP.put("연제구", "21130");
        DETAIL_REGION_MAP.put("수영구", "21140");
        DETAIL_REGION_MAP.put("사상구", "21150");
        DETAIL_REGION_MAP.put("기장군", "21310");

        // 대구광역시 구 (22xxx)
        DETAIL_REGION_MAP.put("대구 중구", "22010");
        DETAIL_REGION_MAP.put("대구 동구", "22020");
        DETAIL_REGION_MAP.put("대구 서구", "22030");
        DETAIL_REGION_MAP.put("대구 남구", "22040");
        DETAIL_REGION_MAP.put("대구 북구", "22050");
        DETAIL_REGION_MAP.put("수성구", "22060");
        DETAIL_REGION_MAP.put("달서구", "22070");
        DETAIL_REGION_MAP.put("달성군", "22310");
        DETAIL_REGION_MAP.put("대구 군위군", "22320");

        // 인천광역시 구 (23xxx)
        DETAIL_REGION_MAP.put("인천 중구", "23010");
        DETAIL_REGION_MAP.put("인천 동구", "23020");
        DETAIL_REGION_MAP.put("인천 남구", "23030");
        DETAIL_REGION_MAP.put("연수구", "23040");
        DETAIL_REGION_MAP.put("남동구", "23050");
        DETAIL_REGION_MAP.put("부평구", "23060");
        DETAIL_REGION_MAP.put("계양구", "23070");
        DETAIL_REGION_MAP.put("인천 서구", "23080");
        DETAIL_REGION_MAP.put("강화군", "23310");
        DETAIL_REGION_MAP.put("옹진군", "23320");

        // 광주광역시 구 (24xxx) - 중요: 남구 포함
        DETAIL_REGION_MAP.put("광주 동구", "24010");
        DETAIL_REGION_MAP.put("광주 서구", "24020");
        DETAIL_REGION_MAP.put("광주 남구", "24030");
        DETAIL_REGION_MAP.put("광주 북구", "24040");
        DETAIL_REGION_MAP.put("광산구", "24050");

        // 대전광역시 구 (25xxx)
        DETAIL_REGION_MAP.put("대전 동구", "25010");
        DETAIL_REGION_MAP.put("대전 중구", "25020");
        DETAIL_REGION_MAP.put("대전 서구", "25040");
        DETAIL_REGION_MAP.put("유성구", "25030");
        DETAIL_REGION_MAP.put("대덕구", "25050");

        // 울산광역시 구 (26xxx)
        DETAIL_REGION_MAP.put("울산 중구", "26010");
        DETAIL_REGION_MAP.put("울산 남구", "26020");
        DETAIL_REGION_MAP.put("울산 동구", "26030");
        DETAIL_REGION_MAP.put("울산 북구", "26040");
        DETAIL_REGION_MAP.put("울주군", "26310");

        // 세종특별자치시 (29xxx)
        DETAIL_REGION_MAP.put("세종시", "29010");

        // 경기도 시/군/구 (31xxx)
        DETAIL_REGION_MAP.put("수원시", "31010");
        DETAIL_REGION_MAP.put("수원시 장안구", "31011");
        DETAIL_REGION_MAP.put("수원시 권선구", "31012");
        DETAIL_REGION_MAP.put("수원시 팔달구", "31013");
        DETAIL_REGION_MAP.put("수원시 영통구", "31014");
        DETAIL_REGION_MAP.put("성남시", "31020");
        DETAIL_REGION_MAP.put("성남시 수정구", "31021");
        DETAIL_REGION_MAP.put("성남시 중원구", "31022");
        DETAIL_REGION_MAP.put("성남시 분당구", "31023");
        DETAIL_REGION_MAP.put("의정부시", "31030");
        DETAIL_REGION_MAP.put("안양시", "31040");
        DETAIL_REGION_MAP.put("안양시 만안구", "31041");
        DETAIL_REGION_MAP.put("안양시 동안구", "31042");
        DETAIL_REGION_MAP.put("부천시", "31050");
        DETAIL_REGION_MAP.put("광명시", "31060");
        DETAIL_REGION_MAP.put("평택시", "31070");
        DETAIL_REGION_MAP.put("동두천시", "31080");
        DETAIL_REGION_MAP.put("안산시", "31090");
        DETAIL_REGION_MAP.put("안산시 상록구", "31091");
        DETAIL_REGION_MAP.put("안산시 단원구", "31092");
        DETAIL_REGION_MAP.put("고양시", "31100");
        DETAIL_REGION_MAP.put("고양시 덕양구", "31101");
        DETAIL_REGION_MAP.put("고양시 일산동구", "31103");
        DETAIL_REGION_MAP.put("고양시 일산서구", "31104");
        DETAIL_REGION_MAP.put("과천시", "31110");
        DETAIL_REGION_MAP.put("구리시", "31120");
        DETAIL_REGION_MAP.put("남양주시", "31130");
        DETAIL_REGION_MAP.put("오산시", "31140");
        DETAIL_REGION_MAP.put("시흥시", "31150");
        DETAIL_REGION_MAP.put("군포시", "31160");
        DETAIL_REGION_MAP.put("의왕시", "31170");
        DETAIL_REGION_MAP.put("하남시", "31180");
        DETAIL_REGION_MAP.put("용인시", "31190");
        DETAIL_REGION_MAP.put("용인시 처인구", "31191");
        DETAIL_REGION_MAP.put("용인시 기흥구", "31192");
        DETAIL_REGION_MAP.put("용인시 수지구", "31193");
        DETAIL_REGION_MAP.put("파주시", "31200");
        DETAIL_REGION_MAP.put("이천시", "31210");
        DETAIL_REGION_MAP.put("안성시", "31220");
        DETAIL_REGION_MAP.put("김포시", "31230");
        DETAIL_REGION_MAP.put("화성시", "31240");
        DETAIL_REGION_MAP.put("광주시", "31250");
        DETAIL_REGION_MAP.put("양주시", "31260");
        DETAIL_REGION_MAP.put("포천시", "31270");
        DETAIL_REGION_MAP.put("여주시", "31280");
        DETAIL_REGION_MAP.put("연천군", "31350");
        DETAIL_REGION_MAP.put("가평군", "31370");
        DETAIL_REGION_MAP.put("양평군", "31380");

        // 강원특별자치도 시/군 (32xxx)
        DETAIL_REGION_MAP.put("춘천시", "32010");
        DETAIL_REGION_MAP.put("원주시", "32020");
        DETAIL_REGION_MAP.put("강릉시", "32030");
        DETAIL_REGION_MAP.put("동해시", "32040");
        DETAIL_REGION_MAP.put("태백시", "32050");
        DETAIL_REGION_MAP.put("속초시", "32060");
        DETAIL_REGION_MAP.put("삼척시", "32070");
        DETAIL_REGION_MAP.put("홍천군", "32310");
        DETAIL_REGION_MAP.put("횡성군", "32320");
        DETAIL_REGION_MAP.put("영월군", "32330");
        DETAIL_REGION_MAP.put("평창군", "32340");
        DETAIL_REGION_MAP.put("정선군", "32350");
        DETAIL_REGION_MAP.put("철원군", "32360");
        DETAIL_REGION_MAP.put("화천군", "32370");
        DETAIL_REGION_MAP.put("양구군", "32380");
        DETAIL_REGION_MAP.put("인제군", "32390");
        DETAIL_REGION_MAP.put("강원 고성군", "32400");
        DETAIL_REGION_MAP.put("양양군", "32410");

        // 충청북도 시/군/구 (33xxx)
        DETAIL_REGION_MAP.put("충주시", "33020");
        DETAIL_REGION_MAP.put("제천시", "33030");
        DETAIL_REGION_MAP.put("청주시", "33040");
        DETAIL_REGION_MAP.put("청주시 상당구", "33041");
        DETAIL_REGION_MAP.put("청주시 서원구", "33042");
        DETAIL_REGION_MAP.put("청주시 흥덕구", "33043");
        DETAIL_REGION_MAP.put("청주시 청원구", "33044");
        DETAIL_REGION_MAP.put("보은군", "33320");
        DETAIL_REGION_MAP.put("옥천군", "33330");
        DETAIL_REGION_MAP.put("영동군", "33340");
        DETAIL_REGION_MAP.put("진천군", "33350");
        DETAIL_REGION_MAP.put("괴산군", "33360");
        DETAIL_REGION_MAP.put("음성군", "33370");
        DETAIL_REGION_MAP.put("단양군", "33380");
        DETAIL_REGION_MAP.put("증평군", "33390");

        // 충청남도 시/군/구 (34xxx)
        DETAIL_REGION_MAP.put("천안시", "34010");
        DETAIL_REGION_MAP.put("천안시 동남구", "34011");
        DETAIL_REGION_MAP.put("천안시 서북구", "34012");
        DETAIL_REGION_MAP.put("공주시", "34020");
        DETAIL_REGION_MAP.put("보령시", "34030");
        DETAIL_REGION_MAP.put("아산시", "34040");
        DETAIL_REGION_MAP.put("서산시", "34050");
        DETAIL_REGION_MAP.put("논산시", "34060");
        DETAIL_REGION_MAP.put("계룡시", "34070");
        DETAIL_REGION_MAP.put("당진시", "34080");
        DETAIL_REGION_MAP.put("금산군", "34310");
        DETAIL_REGION_MAP.put("부여군", "34330");
        DETAIL_REGION_MAP.put("서천시", "34340");
        DETAIL_REGION_MAP.put("청양군", "34350");
        DETAIL_REGION_MAP.put("홍성군", "34360");
        DETAIL_REGION_MAP.put("예산군", "34370");
        DETAIL_REGION_MAP.put("태안군", "34380");

        // 전북특별자치도 시/군 (35xxx)
        DETAIL_REGION_MAP.put("전주시", "35010");
        DETAIL_REGION_MAP.put("전주시 완산구", "35011");
        DETAIL_REGION_MAP.put("전주시 덕진구", "35012");
        DETAIL_REGION_MAP.put("군산시", "35020");
        DETAIL_REGION_MAP.put("익산시", "35030");
        DETAIL_REGION_MAP.put("정읍시", "35040");
        DETAIL_REGION_MAP.put("남원시", "35050");
        DETAIL_REGION_MAP.put("김제시", "35060");
        DETAIL_REGION_MAP.put("완주군", "35310");
        DETAIL_REGION_MAP.put("진안군", "35320");
        DETAIL_REGION_MAP.put("무주군", "35330");
        DETAIL_REGION_MAP.put("장수군", "35340");
        DETAIL_REGION_MAP.put("임실군", "35350");
        DETAIL_REGION_MAP.put("순창군", "35360");
        DETAIL_REGION_MAP.put("고창군", "35370");
        DETAIL_REGION_MAP.put("부안군", "35380");

        // 전라남도 시/군 (36xxx)
        DETAIL_REGION_MAP.put("목포시", "36010");
        DETAIL_REGION_MAP.put("여수시", "36020");
        DETAIL_REGION_MAP.put("순천시", "36030");
        DETAIL_REGION_MAP.put("나주시", "36040");
        DETAIL_REGION_MAP.put("광양시", "36060");
        DETAIL_REGION_MAP.put("담양군", "36310");
        DETAIL_REGION_MAP.put("곡성군", "36320");
        DETAIL_REGION_MAP.put("구례군", "36330");
        DETAIL_REGION_MAP.put("고흽군", "36350");
        DETAIL_REGION_MAP.put("보성군", "36360");
        DETAIL_REGION_MAP.put("화순군", "36370");
        DETAIL_REGION_MAP.put("장흥군", "36380");
        DETAIL_REGION_MAP.put("강진군", "36390");
        DETAIL_REGION_MAP.put("해남군", "36400");
        DETAIL_REGION_MAP.put("영암군", "36410");
        DETAIL_REGION_MAP.put("무안군", "36420");
        DETAIL_REGION_MAP.put("함평군", "36430");
        DETAIL_REGION_MAP.put("영광군", "36440");
        DETAIL_REGION_MAP.put("장성군", "36450");
        DETAIL_REGION_MAP.put("완도군", "36460");
        DETAIL_REGION_MAP.put("진도군", "36470");
        DETAIL_REGION_MAP.put("신안군", "36480");

        // 경상북도 시/군/구 (37xxx)
        DETAIL_REGION_MAP.put("포항시", "37010");
        DETAIL_REGION_MAP.put("포항시 남구", "37011");
        DETAIL_REGION_MAP.put("포항시 북구", "37012");
        DETAIL_REGION_MAP.put("경주시", "37020");
        DETAIL_REGION_MAP.put("김천시", "37030");
        DETAIL_REGION_MAP.put("안동시", "37040");
        DETAIL_REGION_MAP.put("구미시", "37050");
        DETAIL_REGION_MAP.put("영주시", "37060");
        DETAIL_REGION_MAP.put("상주시", "37080");
        DETAIL_REGION_MAP.put("문경시", "37090");
        DETAIL_REGION_MAP.put("경산시", "37100");
        DETAIL_REGION_MAP.put("군위군", "37310");
        DETAIL_REGION_MAP.put("의성군", "37320");
        DETAIL_REGION_MAP.put("청송군", "37330");
        DETAIL_REGION_MAP.put("영양군", "37340");
        DETAIL_REGION_MAP.put("영덕군", "37350");
        DETAIL_REGION_MAP.put("청도군", "37360");
        DETAIL_REGION_MAP.put("고령군", "37370");
        DETAIL_REGION_MAP.put("성주시", "37380");
        DETAIL_REGION_MAP.put("칠곡군", "37390");
        DETAIL_REGION_MAP.put("예천시", "37400");
        DETAIL_REGION_MAP.put("봉화군", "37410");
        DETAIL_REGION_MAP.put("울진군", "37420");
        DETAIL_REGION_MAP.put("울릉군", "37430");

        // 경상남도 시/군/구 (38xxx)
        DETAIL_REGION_MAP.put("진주시", "38030");
        DETAIL_REGION_MAP.put("통영시", "38050");
        DETAIL_REGION_MAP.put("사천시", "38060");
        DETAIL_REGION_MAP.put("김해시", "38070");
        DETAIL_REGION_MAP.put("밀양시", "38080");
        DETAIL_REGION_MAP.put("거제시", "38090");
        DETAIL_REGION_MAP.put("양산시", "38100");
        DETAIL_REGION_MAP.put("창원시", "38110");
        DETAIL_REGION_MAP.put("창원시 의창구", "38111");
        DETAIL_REGION_MAP.put("창원시 성산구", "38112");
        DETAIL_REGION_MAP.put("창원시 마산합포구", "38113");
        DETAIL_REGION_MAP.put("창원시 마산회원구", "38114");
        DETAIL_REGION_MAP.put("창원시 진해구", "38115");
        DETAIL_REGION_MAP.put("의령군", "38310");
        DETAIL_REGION_MAP.put("함안군", "38320");
        DETAIL_REGION_MAP.put("창녕군", "38330");
        DETAIL_REGION_MAP.put("경남 고성군", "38340");
        DETAIL_REGION_MAP.put("남해군", "38350");
        DETAIL_REGION_MAP.put("하동군", "38360");
        DETAIL_REGION_MAP.put("산청군", "38370");
        DETAIL_REGION_MAP.put("함양군", "38380");
        DETAIL_REGION_MAP.put("거창군", "38390");
        DETAIL_REGION_MAP.put("합천군", "38400");

        // 제주특별자치도 (39xxx)
        DETAIL_REGION_MAP.put("제주시", "39010");
    }

    public static String regionCode(String regionName) {
        if (regionName == null) {
            return null;
        }
        return REGION_MAP.get(regionName.trim());
    }

    public static String detailRegionCode(String detailRegion) {
        if (detailRegion == null) {
            return null;
        }
        return DETAIL_REGION_MAP.get(detailRegion.trim());
    }
}
