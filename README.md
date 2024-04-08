***
# 돌려드림 캡스톤 디자인 프로젝트

</br>

[![All Collaborators](https://img.shields.io/badge/all_Collaborators-6-orange.svg)](#Collaborators)
</br>

## 목차
- [소개](#소개)
- [기존 사례 분석](#기존-사례-분석)
  - [기존 서비스의 문제점](#기존-서비스의-문제점)
  - [이번 프로젝트에서 개선할 점](#이번-프로젝트에서-개선할-점)
- [컴포넌트](#컴포넌트)
- [주기능](#주기능)
  - [프론트엔드](#프론트엔드)
    - [회원가입 및 로그인](#회원가입-및-로그인)
    - [메인페이지](#메인페이지)
    - [돌려드림 분실물 조회 페이지](#돌려드림-분실물-조회-페이지)
  - [백엔드](#백엔드)
    - [회원가입 및 로그인](#회원가입-및-로그인)
- [Collaborators](#Collaborators)
***

## 소개

> 기존 유실물 찾기 서비스의 불편함을 해소하고 개선하고자, 습득자는 유실물을 간편하게 등록하고 분실자는 간단한 인증을 통해 유실물을 찾아가고자 본 서비스를 고안하게 되었습니다.

</br>

## 기존 사례 분석


### 기존 서비스의 문제점

1. 회원가입, 로그인 유효성 검사 기능의 부재
2. 경찰서 마다 카테고리별 지칭명이 서로 달라 찾는데 어려움
3. 잃어버린 물건을 찾으러 경찰서에 방문 필수
4. 게시물마다 일부 정보의 부재(사진, 물품명, 보관장소 등)
6. 경찰관들이 분실물을 수동으로 정보를 기입해야하는 문제

### 이번 프로젝트에서 개선할 점

1. 회원가입, 로그인 기능 강화 및 보안성 강화
2. 습득물건을 사용자가 자발적으로 신고 후 직접 찾을 수 있도록 기능 개선
3. 분류된 결과를 자동으로 기입하여 편의성 개선

</br></br>

***
# 컴포넌트
- **Front**

- **Back**
- 
- **Device**
  - 


***
# 주기능

## 프론트엔드(FrontEnd)

### 회원가입 및 로그인

</br>

>개인 정보 제공 동의 후 회원가입이 가능하며,  
모든 동의를 받으면 회원가입 페이지로 진입할 수 있습니다.  

</br></br>

</br>

>회원가입 진행시 유효성 검사를 실시간으로 진행하며, 
모든 유효성 검사를 충족시 회원가입을 진행합니다.  

</br></br>
</br>

>회원가입시 가입한 이메일로 이메일 인증 메일이 전송됩니다.  
해당 메일에서 인증을 진행하여야 본 서비스를 이용하실 수 있습니다.

</br></br>

### 메인페이지

<p align="center">
  
</p>
</br>

>메인페이지에서는 

<p align="center">
</p>


</br></br>

### 돌려드림 분실물 조회 페이지
<p align="center">

</p>
</br>
<p align="center">

</p>
</br>

>돌려드림 서비스에 자동 분류되어 업로드된 분실물들의 목록을 볼 수 있습니다. 
해당 게시물을 클릭하면 상세 정보를 볼 수 있으며, 작성자의 경우 수정 및 삭제가 가능합니다. 
사진으로부터 가져온 위치 메타데이터를 기반으로 구글 지도와 연동되어 클릭시 분실품을 찾으러 갈 수 있도록 구글 지도에 맵핀을 표시하여줍니다.

</br>

</br></br>

### 돌려드림 분실물 등록
<p align="center">

</p>
</br>


***
## 백엔드(BackEnd)


### 회원가입 및 로그인
<p align="center">

</br>

</br>



### 사진 메타데이터 자동 크롤링
>사진을 업로드하는 것으로 분실물의 위치와 발견날짜를 자동으로 불러오게 하기 위해서 사진메타데이터 정보를 수집하여 사용하였습니다.
~~~python
from PIL import Image
from PIL.ExifTags import TAGS

def metadata(img_path=IMAGE_PATH):
  img = Image.open(img_path)
  info = img._getexif();
  img.close()
  
  taglabel = {}
  for tag, value in info.items():
    decoded = TAGS.get(tag, tag)
    taglabel[decoded] = value

  ...

  return "https://www.google.com/maps/place/"+str(Lat)+"+"+str(Lon), taglabel['DateTimeOriginal']
~~~
>PIL의 ExifTags 라이브러리를 사용하였습니다. 내부 연산으로 경도와 위도를 뽑아낸 후 구글 맵스 URL과 결합하여 링크를 생성하여 응답하도록 하였습니다.

</br></br>

# Collaborators
<table>
  <tr>
    <td align="center">
  <a href="https://github.com/Heestroy-118">
    <img src="markdown/ljh.jpg" width="100px;" alt=""/>
    문재희<br>
    Front End
  </a>
</td>
    <td align="center">
  <a href="https://github.com/HwanHee927">
    <img src="markdown/ljh.jpg" width="100px;" alt=""/>
    정환희<br>
    Front End
  </a>
</td>
</tr>
  <tr>
    </td>
    <td align="center">
  <a href="https://github.com/chihyeonwon">
    <img src="markdown/ljh.jpg" width="100px;" alt=""/>
    원치현<br>
    Back End
  </a>
</td>
    </td>
    <td align="center">
  <a href="https://github.com/jin0792">
    <img src="markdown/ljh.jpg" width="100px;" alt=""/>
    소진오<br>
    Back End
  </a>
</td>
    </td>
    <td align="center">
  <a href="https://github.com/doncici77">
    <img src="markdown/ljh.jpg" width="100px;" alt=""/>
    박광호<br>
    Back End
  </a>
</td>
    </td>
    <td align="center">
  <a href="https://github.com/youngmumi">
    <img src="markdown/ljh.jpg" width="100px;" alt=""/>
    김유정<br>
    Back End
  </a>  
</td>
  <tr>
  </tr>
<table>

## 3월 28일 팀회의 피드백
![image](https://github.com/chihyeonwon/Capstone-Design/assets/58906858/372bfaf1-415c-4c59-83e2-fb9330eb499f)
```
검색 결과 - 사진으로 블록형식으로 여러 개 보일 수 있게
-> gridView의 형태로 사진으로 여러 개를 보여줄 수 있게끔 수정한다.

물건등록 페이지 - 사진을 찍어서 올리는 기능, 사진 가져오는 기능
-> 사진을 찍어서 올리는 기능, 가져오는 기능을 드랍 메뉴의 형태로 구현, 사진을 여러 개를 추가할 수 있는 기능을 구현,
AI기능을 이용하여 여러 개의 사진을 분석하여 물건이 어떤 물건인지 파악하는 데 입력값으로 이용될 수가 있다.

지도 api 기능은 과할 수도 있다
-> 물건 등록 페이지의 지도 api 기능을 빼고 단순히 위치만을 입력하게 한다.

보관장소 입력 시 굳이 채팅 기능이 필요 하지 않을 수 있다.
보관장소 입력 안 할 시 채팅 기능이 필요 할 수도 있다.

물품명을 카테고리 시 - 등록 갯수 상위 5개 (자주 잃어버리는 물품) 나머지는 그냥 기타 로 구현하는 방법

기능 정의서 작성
```
[원주캠퍼스 지도의 건물](https://www.gwnu.ac.kr/newCampusMap/kr/view.do)
![image](https://github.com/chihyeonwon/Capstone-Design/assets/58906858/b6cf8e7a-a1da-4cf9-8de6-24fcb000fa8e)
```
실제 지도에 있는 건물들의 명칭 데이터를 분실물 데이터베이스 Ref의 값(프로퍼티)들로 지정한다.
W1~W16, E1~E5
```
## 
```
UI 설계 수정 - 분실자가 글을 쓰는 인터페이스 없음-알림기능을 위해서 필요
```
## 4월 3일 회의 
```
<UI 수정사항>

● 처음 먼저 등록한 사진은 대표사진으로 추가됨 (안내 문구 추가)
→ 사진 여러장 등록 가능 
● 지도 보류 (학교 홈페이지 참고) api를 쓸 것인가? 지도를 빼고 드롭다운으로 수정
● 지도 도움말 버튼을 클릭 시 웹뷰 
● 상세 위치보단 추가 입력 사항으로 변경 ?
● 지도 상세위치 → 장소클릭+상세위치(과사, 화장실, 201호 등등) 
= W6 과학기술대학 남자 화장실 
● 사용자명 수정
● 검색 결과 분실물과 습득물 , 둘다 나오게 체크박스 추가
● 검색결과에서 습득물명과 분실물명 가시성 높이도록 수정
● 관리번호 쓸것인가
● 분실일자 시간 설정하는 것 삭제
● 습득물명에서 카테고리 한 번 더 쓰는걸로 하는게 좋을 것같다
● 습득물명 분류를 좀 더 포괄적으로 
ex) 금전 = 지갑+카드+현금+기타, 전자기기 = 핸드폰+컴퓨터+기타
● 키워드 알림 ex) 이어폰을 키워드 알림 설정하면 이어폰에 대한 새로운 글이 올라올 때마다 알림 전송
● 채팅알림과 키워드 알림 → 활성화/비활성화 설정 여부
ex) 버튼이나 아이콘을 눌러 간단히 알림 활성화/비활성화 설정할 수도 있다
● 마이페이지 설정 기능 추가
● 메인 메뉴를 게시판으로 고정해놓고 사이드나 하단바에 메뉴를 고를 수 있게 UI를 구현
```
## 4월 5일 회의
```
분실물과 습득물의 차이?
분실물 - 분실장소
습득물 - 습득장소, 보관장소(다른사람이 어디에보관했는지)

건물위치를 색깔로 나타냄

더보기를 누르면 사진을 추가적으로 확인할 수 있다.

분실신고를 누르면 습득자-습득물 게시판, 분실자-분실물 게시판

QnA - 제목 내용 상태
답변이 완료되면 상태로 변함

기능명세서에 보면 빠져있는 것들

캠퍼스 안내도 - 필수사항은아니다

1:1채팅을 구현한다. 어떻게 사람과 사람과의 연결을 할 것냐- 매칭
삭제를 한경우에 사용자 검색
채팅 수신 거부 (사용자 차단)

-이미지 몇개까지 할건지 제한

분실물을 마냥 가지고만 있을 순 없기 때문에 최대 한달,6개월 정도 보관기간을 정해놓고서
물품을 실제로 보관해놓고있는 장소에서도 처리를 해야하기때문에 몇개월정도 들고 있을 건지를
삭제

분실물도 찾은 경우에는 찾은 분실물이 완료가 되었다는 것을 사용자들이 체크를 하게 해서 텍스트들이 비활성화된다거나
하는 기능을 구현하면 좋겠다.

물건의 색깔 - 정확도를 올리기위해서 특징 -(불필요한 알람들이 올 수 도 있어서) 키워드 매칭을 더 해서
사용자가 입력한 것에 맞는 것들에 대한 정보

편의성에 대한 피드백 

다음에 해야 할 것 - 기능 1가지 정도 완료, 역할 분담

2주에 1번 정도 미팅 (4.18일정도)
팀장 주도하에 개발 진행 - 일주일간 했던 것들을 정리해서 보냄
```
## 24.04.07
![image](https://github.com/chihyeonwon/Capstone-Design/assets/58906858/ae47988a-06f6-4bfa-82c4-e3c0ed94c0a4)
```
게시물 작성해서 어뎁터의 키리스트를 get_board_list의 매개변수로 넘겨준다음 해당하는 스토리지 경로의 이미지를 추가했다.
```
![image](https://github.com/chihyeonwon/Capstone-Design/assets/58906858/582d1933-08fa-46a5-805c-004fcfae4ed5)
![image](https://github.com/chihyeonwon/Capstone-Design/assets/58906858/c1df5236-aa5a-409a-bba7-4a0f709f28f0)
```
습득 위치 옆의 물음표 이미지를 클릭하면 건물 조직도 홈페이지로 이동한다.
```
## 24.04.08
![image](https://github.com/chihyeonwon/Capstone-Design/assets/58906858/2d3dbea3-821c-45c9-83e7-bffdc8ce8341)
```
습득 날짜 선택 UI를 EditText에서 DatePickerDialog로 수정하였습니다.
```
![image](https://github.com/chihyeonwon/Capstone-Design/assets/58906858/26e98a05-08af-4310-a125-0ff420a7bc9c)
![image](https://github.com/chihyeonwon/Capstone-Design/assets/58906858/a63f048b-cc71-4901-bc13-4d7673c823a3)
![image](https://github.com/chihyeonwon/Capstone-Design/assets/58906858/020b90ed-97fc-4e3c-9523-5029203ceaff)
```
습득 위치와 보관 위치 선택 UI를 EditText에서 DropDown으로 수정하였습니다.
습득 위치와 보관 위치 드롭다운 선택한 String과 상세 습득위치, 상세 보관위치를 EditText로 입력받아
두 개의 String을 합쳐서 최종적으로 습득 위치, 상세위치를 산출한 뒤 데이터베이스에 저장하였습니다.

ps 드롭다운 메뉴의 Text 색을 변경할 수는 있으나 전체적으로 변경가능하다.
일부분 즉 W1~W15는 빨간색 E1~E5는 노란색 개별로는 변경불가능하다.
W1~W15, E1~E5 전체를 빨간색이나 노란색으로 변경하는 것은 가능
```
![image](https://github.com/chihyeonwon/Capstone-Design/assets/58906858/7d9d7ba6-e97d-46e0-843c-913e92a45e39)
![image](https://github.com/chihyeonwon/Capstone-Design/assets/58906858/4c0a467a-35b0-425e-91bc-34766a1e9924)
```
하단 네비게이션 바의 UI를 설계 UI대로 수정하였습니다.

하단 네비게이션 바의 현재 창을 나타내는 바의 색상을 노란색(MainColor)에서 검정색으로 수정하였습니다.

PS 4번째 텍스트 Q/N -> Q/A로 수정할 예정
```
## 24.04.09
#### 검색 기능 구현
![image](https://github.com/chihyeonwon/Capstone-Design/assets/58906858/87a014f2-8c2f-49e4-a0c9-ee4338783a5b)
![image](https://github.com/chihyeonwon/Capstone-Design/assets/58906858/50094e5a-62ef-446f-8ab2-b78db8b6aa54)
![image](https://github.com/chihyeonwon/Capstone-Design/assets/58906858/37c42101-8e85-4a81-abfb-02ac2efa36cd)
```
검색버튼을 눌렀을 때 검색하는 창(searchActivity)에서 검색옵션을 주고 그 옵션 값을 intent.putExtra에 담아서
검색된 창(searchActivity)로 넘긴다.(intent.getStringExtra)

검색된 창에서 넘겨받은 옵션 값과 게시글을 불러오는 함수 getFBBoardData의 매개변수로 넘겨준다음
옵션 값과 데이터 모델의 값과 같은 게시물만 출력하도록 한다.

최종적으로 옵션값에 입력한 습득물명과 같은 게시물만 출력하도록 했다.

ps 다음 구현 예정 -> 일자, 위치 검색 필터 옵션을 추가한다.
```
