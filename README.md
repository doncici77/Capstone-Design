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
```
검색 결과 - 사진으로 블록형식으로 여러 개 보일 수 있게

물건등록 페이지 - 사진을 찍어서 올리는 기능, 사진 가져오는 기능

지도 api 기능은 과할 수도 있다

보관장소 입력 시 굳이 채팅 기능이 필요 하지 않을 수 있다.
보관장소 입력 안 할 시 채팅 기능이 필요 할 수도 있다.

물품명을 카테고리 시 - 등록 갯수 상위 5개 (자주 잃어버리는 물품) 나머지는 그냥 기타 로 구현하는 방법

기능 정의서 작성
```
