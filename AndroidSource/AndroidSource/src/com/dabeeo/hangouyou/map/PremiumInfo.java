package com.dabeeo.hangouyou.map;

public class PremiumInfo {
	
	public String m_nID;					// 장소 ID
	public int m_nCategoryID;			// 카테고리 ID

	// 장소 타입 
	// 1은 도시, 2는 Attraction, 3은 Accommodation, 4는 Restaurant, 5는 장소와 장소간 이동할 때 사용하는 경유지(즉, 교통정보에서의 노드의 정보). 
	// 6은 관광안내소(6은 앱에 전송할 때만 사용, 관리자모드에서만 입력,수정,삭제하고 사용자 모드에서는 보여지는 화면 없음)
	public int m_nType;
	
	public String m_strName; 			// 장소 이름 
	public float m_fLatitude; 		// 위도
	public float m_fLongitude;		// 경도
	public String m_strIntro;			// 장소 소개 Carriage Return 처리는 <br />로 처리했습니다. replace해서 사용하시면 됩니다.
	public String m_strSimilarSearchWord; 	// 장소 유사 검색어
	public String m_strAddress; 		// 주소
	public String m_strAvailableTime; 	// 이용 가능 시간 
	public String m_strCharges;			// 이용 요금 
	public String m_strOperatingPeriod; 	// 운영 기간
	public String m_strTrafficInfo; 	// 교통 안내 
	public String m_strHomepageURL; 	// 홈페이지 URL
	public String m_strPhoneNumber;		// 전화번호 
	public String m_strTimeStamp; 		//
	public int m_nExpectedTime;		// 예상 시간 (분?)
	public int m_nMinAcceptablePrice; // 최저 수용 금액
	public int m_nMaxAcceptablePrice; // 최고 수용 금액 
	public String m_strtag; 	// 태그
	public int m_nAverageScore;		// 평균 점수
	public int m_nRanking;				// 순위
	public int m_nReviewCount;
	public int m_nLikeCount;
	public String m_strImageFilePath;	// App의 Place List를 보여줄 때 

}
