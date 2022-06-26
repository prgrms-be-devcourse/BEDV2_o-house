package com.prgrms.ohouse.domain.community.model.post.hwpost;

import static com.google.common.base.Preconditions.*;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 데이터베이스에서 보관하는 것은 code
// 약속된 mapper를 통해 치환
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class District {

	private static final List<String> RLGList = List.of(
		"서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "강원도", "경기도", "경상남도"
		, "경상북도", "전라남도", "전라북도", "충청남도", "충청북도"
	);

	private static final List<List<String>> BLGList = List.of(
		List.of("강남구", "강동구", "강북구",
			"강서구", "관악구", "광진구", "구로구", "금천구",
			"노원구", "도봉구", "동대문구", "동작구", "마포구",
			"서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구")
		, List.of("강서구", "금정구", "남구", "동구", "동래구", "부산진구", "북구", "사상구", "사하구", "서구", "수영구", "연제구", "영도구", "중구", "해운대구",
			"기장군")
		, List.of("남구", "달서구", "동구", "북구", "서구", "수성구", "중구", "달성군")
		, List.of("계양구", "미추홀구", "남동구", "동구", "부평구", "서구", "연수구", "중구", "강화군", "옹진군")
		, List.of("광산구", "남구", "동구", "북구", "서구")
		, List.of("대덕구", "동구", "서구", "유성구", "중구")
		, List.of("남구", "동구", "북구", "중구", "울주군")
		,
		List.of("강릉시", "고성군", "동해시", "삼척시", "속초시", "양구군", "양양군", "영월군", "원주시", "인제군", "정선군", "철원군", "춘천시", "태백시", "평창군",
			"홍천군", "화천군", "횡성군")
		, List.of("가평군", "고양시", "과천시", "광명시", "광주시", "구리시", "군포시", "김포시", "남양주시", "동두천시", "부천시", "성남시", "수원시", "시흥시",
			"안산시", "안양시", "양주시", "양평군", "여주시", "연천군", "오산시", "용인시", "의왕시", "의정부시", "이천시", "파주시", "평택시", "포천시", "하남시",
			"화성시", "안성시")
		,
		List.of("거제시", "거창군", "고성군", "김해시", "남해군", "마산시", "밀양시", "사천시", "산청군", "양산시", "의령군", "진주시", "창녕군", "창원시", "통영시",
			"하동군", "함안군", "함양군", "합천군")
		,
		List.of("경산시", "경주시", "구미시", "김천시", "문경시", "상주시", "안동시", "영주시", "영천시", "포항시", "고령군", "군위군", "봉화군", "성주군", "영덕군",
			"영양군", "예천군", "울릉군", "울진군", "의성군", "청도군", "청송군", "칠곡군")
		,
		List.of("광양시", "나주시", "목포시", "순천시", "여수시", "강진군", "고흥군", "곡성군", "구례군", "담양군", "무안군", "보성군", "신안군", "영광군", "영암군",
			"완도군", "장성군", "장흥군", "진도군", "함평군", "해남군", "화순군")
		, List.of("군산시", "김제시", "남원시", "익산시", "전주시", "정읍시", "고창군", "무주군", "부안군", "순창군", "완주군", "임실군", "장수군", "진안군")
		,
		List.of("계룡시", "공주시", "논산시", "보령시", "서산시", "아산시", "천안시", "금산군", "당진시", "부여군", "서천군", "연기군", "예산군", "청양군", "태안군",
			"홍성군")
		, List.of("제천시", "청주시", "충주시", "괴산군", "단양군", "보은군", "영동군", "옥천군", "음성군", "증평군", "진천군", "청원군")
		, List.of()
		, List.of("제주시", "서귀포시")
	);

	// Regional Local Government (검색 대상)
	private Integer RLG;
	// Basic Local Government (검색 대상이 아닐 가능성이 있다. nullable)
	private Integer BLG;

	@Column(name = "district_desc")
	private String districtDescription;

	public District(String code, String districtDescription) {
		if (code != null) {
			checkArgument(code.matches("^\\d{1,2}(_\\d{1,2})?$"));
			var codes = code.split("_");
			if (codes.length == 1) {
				this.RLG = Integer.parseInt(codes[0]);
			} else if (codes.length == 2) {
				this.RLG = Integer.parseInt(codes[0]);
				this.BLG = Integer.parseInt(codes[1]);
			}
		}
		this.districtDescription = districtDescription;
	}

	@Override
	public String toString() {
		var rlgStr = RLGList.get(RLG);
		var blgStr = BLG == null ? "" : " " + BLGList.get(RLG).get(BLG);
		var description = districtDescription == null ? "" : " " + districtDescription;
		return rlgStr + blgStr + description;
	}
}
