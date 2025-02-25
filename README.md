
<div align="center">
  <img src="logo.png" alt="Twinstar Logo" width="800px" />
</div>

  <div style="text-align: center;">
  <strong>Team TwinStar</strong>


</div>

| <a href="https://github.com/ki-hyun-Jang"><img src="https://avatars.githubusercontent.com/u/185437523?v=4" width="220px" /></a> | <a href="https://github.com/leejaeseok-98"><img src="https://avatars.githubusercontent.com/u/185016962?v=4" width="220px" /></a> | <a href="https://github.com/LetsSeeTerrapin"><img src="https://avatars.githubusercontent.com/u/188145635?v=4" width="220px" /></a> | <a href="https://github.com/SWEETP0T4T0"><img src="https://avatars.githubusercontent.com/u/185882822?v=4" width="220px" /></a> |
| :---: | :---: | :---: | :---: |
| 팀장 장기현 | 팀원 이재석 | 팀원 이준서 | 팀원 이태희 |



<br>
<br>
<br>


# 프로젝트 개요 

## 1. 프로젝트 주제

**[기본에 집중한 SNS 서비스]** </br></br>
일상을 기록하고, 다른 사람들과 공유 할 수 있는 소셜 네트워크 서비스



<br>

## 2. 프로젝트 소개</br>
</br>
TwinStar는 

>

<br>


<br>

## 3. 프로젝트 배경 및 필요성

### 3-1. 도파민에 중독된 우리, 디톡스가 필요하다.
<div align="center">
  <img src="news.png" width="800px" />
</div>
<br>
출처. 연합뉴스(https://www.yna.co.kr/view/MYH20240216019500641)
</br>
<br>
</br>

- **쏟아져 나오는 숏폼, 무방비하게 노출되는 우리**
<br>
</br>
초기 SNS에서 숏폼의 의도는 바쁜 현대인들이 짧은 시간 내에 정보를 주고받고, 빠르게 소통하며 창의적인 표현을 할 수 있도록 한 데에 있었습니다.
하지만 현재의 숏폼은 의미가 퇴색되어 자극적이고, 상업적인 컨텐츠들이 양산되고 있습니다.
   
### 3-2. 

### 3-3.

### 3-4. 

<br>

## 4. 프로젝트 주요 기능

### 4-1. 

### 4-2. 

### 4-3. 

### 4-4. 

### 4-5. 

### 4-6. 

### 4-7. 

<br>

## 5. 요구사항 명세서
<details>
<summary>요구사항 명세서</summary>
<div markdown="1">

![요구사항 명세서](요구사항정의서.png)

</div>
</details>

<details>
<summary>WBS</summary>
<div markdown="1">

![WBS](assets/images/WBS.png)

</div>
</details>

<br>

## 6. UML, 플로우차트
<details>
<summary>UML</summary>
<div markdown="1">

```mermaid
flowchart TB
    A("시작") --> B{"AWS 서비스\n연결 연동"}
    B --> C("인스턴스 생성")
    B --> D("NLB 연결")
    
    %% 인스턴스 생성 하위
    C --> E("환경변수 세팅")
    C --> F("DB 연결")
    C --> G("GitHub Action")
    C --> H("도메인 연결")
    C --> I("서버 구동")
    C --> X("이벤트 처리 연결")
    
    %% 이벤트 처리 하위 (SNS 알람 시스템)
    X --> Y("Redis Pub/Sub")
    Y --> Z("SSE 알람 전송")
    
    %% NLB 연결 하위
    D --> M("라우팅 연결")
    D --> S("테스트")
    
    M --> N("게시글 기능")
    M --> O("회원 기능")
    M --> P("파일 업로드")
    M --> R("이벤트")
```

</div>
</details>
<details>
<summary>플로우차트</summary>
<div markdown="1">

![플로우차트](플로우.png)

</div>
</details>

<br>

## 7. ERD
<details>
<summary>ERD</summary>
<div markdown="1">

![ERD](erd.png)

</div>
</details>

</div>
</details>

<br>

## 8. 테이블 정의서(DDL 쿼리문 포함)
<details>
<summary>테이블 정의서</summary>
<div markdown="1">

![테이블정의서](assets/images/테이블정의서.png)

</div>
</details>

<br>

## 9. 백업 계획

### 9-1 Replication

DB 서버의 부하 분산과 데이터 백업을 위해 Replication을 적용하였습니다.

<details>
<summary>네트워크 구성도</summary>
<div markdown="1">

![네트워크 구성도](assets/images/네트워크구성도.png)

</div>
</details>

실 서버에서 운영하다 보니 Replication이 충돌하는 일이 수시로 반복되었습니다.

문제는 해결하였으나 충돌하여 내려간 Slave의 상태를 확인하기 어려워 따로 로그를 수집하였습니다.
<details>
<summary>/etc/crontab</summary>
<div markdown="1">

```bash
...
* * * * *       root    logtime=`date "+\%Y-\%m-\%d \%H:\%M:\%S"` && logquery=`mariadb -e "SHOW SLAVE STATUS \G" | grep -E "[^_]Master_Log_File|Read_Master_Log_Pos|Running|Last_Error"` && printf "${logtime}\n${logquery}\n\n" >> /var/log/mariadb-replication-slave-status.log
...
```
</div>
</details>

<details>
<summary>Slave Logging</summary>
<div markdown="1">
tail -f /var/log/mariadb-replication-slave-status.log

![Slave Logging](assets/images/replication_slave_log.png)

</div>
</details>


### 9-2 mysqldump

Replication은 실시간 복제를 담당하므로 거기에 더해서 이력을 남기기 위해서 cron으로 mysqldump를 스케줄링하였습니다.

<details>
<summary>백업 스크립트</summary>
<div markdown="1">
trip_crew_backup.sh

```bash
...
backupDir="${1}/backup/${2}/"
dateTime=$(date +%Y%m%d%H%M%S)

mkdir -p ${backupDir}

mysqldump -u${3} -p${4} ${2} > "${backupDir}${2}_${dateTime}.sql"

find ${backupDir} -type f -name "*.sql" -mtime +7 -delete
...
```

crontab -e
```bash
# 민감한 정보는 중괄호로 처리했습니다.
...
0 * * * * {scriptDir}/trip_crew_backup.sh {backupDir} trip_crew {username} {password}
...
```
</div>
</details>

<details>
<summary>결과</summary>
<div markdown="1">

![결과](assets/images/mysqldump-result.png)

</div>
</details>

<br>

## 10. 테스트 결과서(테스트 쿼리문 포함)

<details>
<summary>테스트 케이스 정의서</summary>
<div markdown="1">

![테스트 케이스 정의서](assets/images/테스트케이스.png)

</div>
</details>

### 사용자

<details>
<summary>사용자</summary>
<div markdown="1">

<details>
<summary>회원관리</summary>
<div markdown="1">
<details>
<summary>회원가입(아이디 중복 확인)</summary>
<div markdown="1">

![회원가입(아이디 중복 확인)](assets/images/회원가입.png)

</div>
</details>

###

<details>
<summary>회원정보조회_아이디</summary>
<div markdown="1">

![회원정보조회_아이디](assets/images/회원정보조회_아이디.png)

</div>
</details>

###


<details>
<summary>회원정보조회_성별</summary>
<div markdown="1">

![회원정보조회_성별](assets/images/회원정보조회_성별.png)

</div>
</details>

###


<details>
<summary>관리자 등록 변경</summary>
<div markdown="1">

![관리자등록변경](assets/images/관리자_등록_변경.png)

</div>
</details>


###



<details>
<summary>회원 정보 변경</summary>
<div markdown="1">

![회원정보변경](assets/images/회원_정보_변경_비번.png)

</div>
</details>

###



<details>
<summary>회원 탈퇴</summary>
<div markdown="1">

![회원탈퇴](assets/images/회원탈퇴.png)

</div>
</details>


###


<details>
<summary>회원 공개 정보 변경</summary>
<div markdown="1">

![회원공개정보변경](assets/images/회원공개정보변경.png)

</div>
</details>

</div>
</details>






<details>
<summary>로그인</summary>
<div markdown="1">


<details>
<summary>아이디 정보 조회(디비에 있는 경우)</summary>
<div markdown="1">

![아이디정보조회_있는경우](assets/images/아이디정보조회_있는경우.png)

</div>
</details>

###



<details>
<summary>아이디 정보 조회(디비에 없는 경우)</summary>
<div markdown="1">

![아이디정보조회_없는경우](assets/images/아이디정보조회_없는경우.png)

</div>
</details>


###


<details>
<summary>비밀번호 교체 주기 확인</summary>
<div markdown="1">

![비밀번호교체주기](assets/images/비밀번호교체주기.png)

</div>
</details>

</div>
</details>

</div>
</details>

### 취향

<details>
<summary>취향</summary>
<div markdown="1">


<details>
<summary>사용자 취향 호출</summary>
<div markdown="1">

![사용자 취향 호출](assets/images/사용자취향호출.PNG)

</div>
</details>

###

<details>
<summary>사용자 취향 저장</summary>
<div markdown="1">

![사용자 취향 저장](assets/images/사용자취향저장.PNG)

</div>
</details>

###

<details>
<summary>사용자 취향 삭제</summary>
<div markdown="1">

![사용자 취향 삭제](assets/images/사용자취향삭제.PNG)

</div>
</details>

###

<details>
<summary>사용자 취향 수정</summary>
<div markdown="1">

![사용자 취향 수정](assets/images/사용자취향수정.PNG)

</div>
</details>

###

<details>
<summary>게시글 취향 호출</summary>
<div markdown="1">

![게시글 취향 호출](assets/images/게시글취향호출.PNG)

</div>
</details>

###

<details>
<summary>게시글 취향 저장</summary>
<div markdown="1">

![게시글 취향 저장](assets/images/게시글취향저장.PNG)

</div>
</details>

###

<details>
<summary>게시글 취향 삭제</summary>
<div markdown="1">

![게시글 취향 삭제](assets/images/게시글취향삭제.PNG)

</div>
</details>

###

<details>
<summary>게시글 취향 수정</summary>
<div markdown="1">

![게시글 취향 수정](assets/images/게시글취향수정.PNG)

</div>
</details>

###

<details>
<summary>여행 코스 취향 호출</summary>
<div markdown="1">

![여행 코스 취향 호출](assets/images/여행코스취향호출.PNG)

</div>
</details>

###

<details>
<summary>여행 코스 취향 저장</summary>
<div markdown="1">

![여행 코스 취향 저장](assets/images/여행코스취향저장.PNG)

</div>
</details>

###

<details>
<summary>여행 코스 취향 삭제</summary>
<div markdown="1">

![여행 코스 취향 삭제](assets/images/여행코스취향삭제.PNG)

</div>
</details>

###

<details>
<summary>여행 코스 취향 수정</summary>
<div markdown="1">

![여행 코스 취향 수정](assets/images/여행코스취향수정.PNG)

</div>
</details>



</div>
</details>



### 게시판

<details>
<summary>게시판</summary>
<div markdown="1">

<details>
<summary>게시글 등록</summary>
<div markdown="1">

![게시글 등록](assets/images/게시글등록.png)

</div>
</details>

###

<details>
<summary>게시글 임시 저장</summary>
<div markdown="1">

![게시글 임시저장](assets/images/게시글임시저장.png)

</div>
</details>

###

<details>
<summary>게시글 수정</summary>
<div markdown="1">

![게시글 수정](assets/images/게시글수정.png)

</div>
</details>

###

<details>
<summary>게시글 삭제</summary>
<div markdown="1">

![게시글 삭제](assets/images/게시글삭제.png)

</div>
</details>

###

<details>
<summary>게시글 조회-작성일자 기준</summary>
<div markdown="1">

![게시글 조회-작성자 기준](assets/images/게시글조회(작성일자기준).png)

</div>
</details>

###

<details>
<summary>게시글 조회-조회수 정렬</summary>
<div markdown="1">

![게시글 조회-조회수 높은순](assets/images/게시글조회(조회수높은순).png)

</div>
</details>

###

<details>
<summary>게시글 상세조회</summary>
<div markdown="1">

![게시글 상세조회](assets/images/게시글상세조회.png)

</div>
</details>

###

<details>
<summary>별점 등록</summary>
<div markdown="1">

![별점 등록](assets/images/별점등록.png)

</div>
</details>

###

<details>
<summary>별점 수정</summary>
<div markdown="1">

![별점 수정](assets/images/별점수정.png)

</div>
</details>

###

<details>
<summary>별점 삭제</summary>
<div markdown="1">

![별점 삭제](assets/images/별점삭제.png)

</div>
</details>

###

<details>
<summary>댓글 등록</summary>
<div markdown="1">

![댓글 등록](assets/images/댓글등록.png)

</div>
</details>

###

<details>
<summary>댓글 수정</summary>
<div markdown="1">

![댓글 수정](assets/images/댓글수정.png)

</div>
</details>

###

<details>
<summary>댓글 삭제</summary>
<div markdown="1">

![댓글 삭제](assets/images/댓글삭제.png)

</div>
</details>

###

<details>
<summary>댓글 조회</summary>
<div markdown="1">

![댓글 조회](assets/images/댓글조회.png)

</div>
</details>

###

<details>
<summary>댓글 좋아요 등록</summary>
<div markdown="1">

![댓글 좋아요 등록](assets/images/댓글좋아요등록.png)

</div>
</details>

###

<details>
<summary>댓글 좋아요 삭제</summary>
<div markdown="1">

![댓글 좋아요 삭제](assets/images/댓글좋아요삭제.png)

</div>
</details>

###

<details>
<summary>게시글 작성자 프로필 조회</summary>
<div markdown="1">

![게시글 작성자 프로필 조회](assets/images/게시글작성자프로필조회.png)

</div>
</details>

###

<details>
<summary>댓글 작성자 프로필 조회</summary>
<div markdown="1">

![댓글 작성자 프로필 조회](assets/images/댓글작성자프로필조회.png)

</div>
</details>

</div>
</details>



### 여행 코스

<details>
<summary>여행 코스</summary>
<div markdown="1">



<details>
<summary>나라 등록</summary>
<div markdown="1">

![나라 등록](assets/images/나라등록.png)

</div>
</details>

###

<details>
<summary>나라 수정</summary>
<div markdown="1">

![나라 수정](assets/images/나라수정.png)

</div>
</details>

###

<details>
<summary>나라 삭제</summary>
<div markdown="1">

![나라 삭제](assets/images/나라삭제.png)

</div>
</details>

###

<details>
<summary>도시 등록</summary>
<div markdown="1">

![도시 등록](assets/images/도시등록.png)

</div>
</details>

###

<details>
<summary>도시 수정</summary>
<div markdown="1">

![도시 수정](assets/images/도시수정.png)

</div>
</details>

###

<details>
<summary>도시 삭제</summary>
<div markdown="1">

![도시 삭제](assets/images/도시삭제.png)

</div>
</details>

###

<details>
<summary>코스 등록</summary>
<div markdown="1">

![코스 등록1](assets/images/코스등록1.png)
![코스 등록2](assets/images/코스등록2.png)


</div>
</details>

###

<details>
<summary>코스 수정</summary>
<div markdown="1">

![코스 수정1](assets/images/코스수정1.png)
![코스 수정2](assets/images/코스수정2.png)


</div>
</details>

###

<details>
<summary>코스 삭제</summary>
<div markdown="1">

![코스 삭제](assets/images/코스삭제1.png)


</div>
</details>

###

<details>
<summary>코스 전체 조회 & 검색</summary>
<div markdown="1">

![코스 전체 조회1](assets/images/코스전체조회1.png)
![코스 전체 조회2](assets/images/코스전체조회2.png)


</div>
</details>

###

<details>
<summary>코스 상세 조회</summary>
<div markdown="1">

![코스 상세 조회](assets/images/코스상세조회.png)


</div>
</details>

###

<details>
<summary>여행 동행 모집 등록</summary>
<div markdown="1">

![여행 동행 모집 등록](assets/images/여행동행모집등록.png)

</div>
</details>

###

<details>
<summary>여행 동행 모집 수정</summary>
<div markdown="1">

![여행 동행 모집 수정](assets/images/여행동행모집수정.png)

</div>
</details>

###

<details>
<summary>여행 동행 모집 삭제</summary>
<div markdown="1">

![여행 동행 모집 삭제](assets/images/여행동행모집삭제.png)

</div>
</details>

###

<details>
<summary>여행 동행 모집 상태 설정</summary>
<div markdown="1">

![여행 동행 모집 상태 설정](assets/images/여행동행모집상태설정.png)

</div>
</details>

###

<details>
<summary>여행 동행 모집 전체 조회 & 검색</summary>
<div markdown="1">

![여행 동행 모집 전체 조회1](assets/images/여행동행모집전체조회1.png)
![여행 동행 모집 전체 조회2](assets/images/여행동행모집전체조회2.png)

</div>
</details>

###

<details>
<summary>여행 동행 모집 상세 조회</summary>
<div markdown="1">

![여행 동행 모집 상세 조회](assets/images/여행동행모집상세조회.png)

</div>
</details>


###

<details>
<summary>여행 동행 모집 참가 신청</summary>
<div markdown="1">

![여행 동행 모집 참가 신청](assets/images/여행동행모집참가신청.png)

</div>
</details>

###

<details>
<summary>여행 동행 모집 참가 신청 관리</summary>
<div markdown="1">

![여행 동행 모집 참가 신청 관리1](assets/images/여행동행모집참가신청관리1.png)
![여행 동행 모집 참가 신청 관리2](assets/images/여행동행모집참가신청관리2.png)

</div>
</details>

###

<details>
<summary>여행 동행 모집 강퇴</summary>
<div markdown="1">

![여행 동행 모집 강퇴](assets/images/여행동행모집강퇴.png)

</div>
</details>

###

<details>
<summary>여행 동행 모집 나가기</summary>
<div markdown="1">

![여행 동행 모집 나가기](assets/images/여행동행모집나가기.png)

</div>
</details>

</div>
</details>


### 신고

<details>
<summary>신고</summary>
<div markdown="1">


<details>
<summary>여행 후기 신고</summary>
<div markdown="1">

![여행 후기 신고](assets/images/여행후기신고.png)

</div>
</details>

###

<details>
<summary>여행 후기 신고 승인</summary>
<div markdown="1">

![여행 후기 신고 승인](assets/images/여행후기신고승인.png)

</div>
</details>


###

<details>
<summary>여행 후기 신고 반려</summary>
<div markdown="1">

![여행 후기 신고 반려](assets/images/여행후기신고반려.png)

</div>
</details>

###

<details>
<summary>여행 후기 댓글 신고 </summary>
<div markdown="1">

![여행 후기 댓글 신고](assets/images/여행후기댓글신고.png)

</div>
</details>


###

<details>
<summary>여행 후기 댓글 신고 승인 </summary>
<div markdown="1">

![여행 후기 댓글 신고 승인](assets/images/여행후기댓글신고승인.png)

</div>
</details>

###

<details>
<summary>여행 후기 댓글 신고 </summary>
<div markdown="1">

![여행 후기 댓글 신고 반려](assets/images/여행후기댓글신고반려.png)

</div>
</details>

###

<details>
<summary>여행 코스 신고 </summary>
<div markdown="1">

![여행 코스 신고](assets/images/여행코스신고.png)

</div>
</details>

###

<details>
<summary>여행 코스 신고 승인 </summary>
<div markdown="1">

![여행 코스 신고 승인](assets/images/코스신고승인.png)

</div>
</details>

###

<details>
<summary>여행 코스 신고 반려 </summary>
<div markdown="1">

![여행 코스 신고 반려](assets/images/코스신고반려.png)

</div>
</details>

###

<details>
<summary>여행 동행 신고 </summary>
<div markdown="1">

![여행 동행 신고](assets/images/여행동행신고.png)

</div>
</details>

###

<details>
<summary>여행 동행 신고 승인 </summary>
<div markdown="1">

![여행 동행 신고 승인](assets/images/동행신고승인.png)

</div>
</details>

###

<details>
<summary>여행 동행 신고 반려 </summary>
<div markdown="1">

![여행 동행 신고 반려](assets/images/동행신고반려.png)

</div>
</details>

###

<details>
<summary>신고 내역 조회 </summary>
<div markdown="1">

![신고 내역 조회](assets/images/신고내역조회.png)

</div>
</details>

###

<details>
<summary>신고 내역 상세 조회-후기</summary>
<div markdown="1">

![신고 내역 상세 조회-후기](assets/images/신고내역상세조회_후기.png)

</div>
</details>


###

<details>
<summary>신고 내역 상세 조회-댓글</summary>
<div markdown="1">

![신고 내역 상세 조회-댓글](assets/images/신고내역상세조회_댓글.png)

</div>
</details>

###

<details>
<summary>신고 내역 상세 조회-코스</summary>
<div markdown="1">

![신고 내역 상세 조회-코스](assets/images/신고내역상세조회_코스.png)

</div>
</details>

###

<details>
<summary>신고 내역 상세 조회-동행</summary>
<div markdown="1">

![신고 내역 상세 조회-동행](assets/images/신고내역상세조회_동행.png)

</div>
</details>

###

<details>
<summary>정지 이력 기록</summary>
<div markdown="1">

![정지 이력 기록](assets/images/정지이력기록.png)

</div>
</details>



</div>
</details>

### 알림


<details>
<summary>알림</summary>
<div markdown="1">

<details>
<summary>알림 템플릿 등록</summary>
<div markdown="1">

![알림 템플릿 등록](assets/images/alarm_insert.png)

</div>
</details>

###

<details>
<summary>알림 템플릿 수정</summary>
<div markdown="1">

![알림 템플릿 수정](assets/images/alarm_update.png)

</div>
</details>

###

<details>
<summary>알림 템플릿 삭제</summary>
<div markdown="1">

![알림 템플릿 삭제](assets/images/alarm_delete.png)

</div>
</details>

###

<details>
<summary>알림 템플릿 조회</summary>
<div markdown="1">

![알림 템플릿 조회](assets/images/alarm_select.png)

</div>
</details>

###

<details>
<summary>알림 템플릿 상세조회</summary>
<div markdown="1">

![알림 템플릿 상세조회](assets/images/alarm_detail_select.png)

</div>
</details>

###

<details>
<summary>댓글 작성 알림 발송</summary>
<div markdown="1">

![댓글 작성 알림 발송](assets/images/리뷰%20댓글%20작성%20시%20리뷰%20작성자에게%20알림%20발송.png)

</div>
</details> 

###

<details>
<summary>별점 알림 발송</summary>
<div markdown="1">

![별점 알림 발송](assets/images/리뷰%20별점%20시%20리뷰%20작성자에게%20알림%20발송.png)

</div>
</details> 

###

<details>
<summary>동행참가 알림 발송</summary>
<div markdown="1">

![동행참가 알림 발송](assets/images/동행%20참가%20요청%20시%20동행%20모집자에게%20알림%20발송.png)

</div>
</details> 

###

<details>
<summary>신고 승인 시 알림발송</summary>
<div markdown="1">

![신고 승인 시 알림발송](assets/images/신고%20상태%20accept%20시%20신고자,%20신고당한%20자%20모두에게%20알림발송.png)

</div>
</details> 

###

<details>
<summary>동행 참가여부에 따른 알림발송</summary>
<div markdown="1">

![동행 참가여부에 따른 알림발송](assets/images/여행%20동행자%20참가%20여부에%20따른%20동행%20신청자에게%20알림%20발송.png)

</div>
</details> 

</div>
</details>

<br>

## 11. 트러블슈팅
||내용|
|-|-|
|1|쿼리문 작성 과정에서 트리거를 통한 로직 구현에 있어 자동 마감를 구현하고자 하였으나 트리거의 Update문과 DB 접근과 참가자를 수락하기 위한 Insert의 DB 접근이 동시에 일어나는 문제를 해결하지 못해 트리거를 삭제하게 되었습니다. 이부분의 해결을 위해서 충돌을 피하기 위해 트리거 문을 수정하는 작업을 진행했어야 됐던 것 같습니다.|
|2|각자의 PC에서 작업을 하다가 효율성을 위해서 같은 DB서버를 사용하기로 하였을 때, 간단하게 생각했는데 Replication 적용을 하면서 많은 문제를 만나게 되었습니다. Replication을 적용하면 단순히 복제가 되는 것으로 생각을 했는데 Master의 변경 사항을 Slave에 그대로 Slave에 전달하는 구조를 이해하고 나서 해결의 실마리가 보였던 것 같습니다. 이후에 기존에 운영중이던 DB 중 불필요한 서비스를 내리고 추가로 나머지 DB는 Master를 Slave에 복제한 후에 Replication을 적용하여 해결하였습니다.|
|3|프로젝트에서 알림 발송 쪽을 구현하면서 템플릿의 내용에 값들을 변경하여 알림을 발송하고 싶었습니다. 하지만 백엔드 측으로 구현가능한 내용을 데이터베이스로 처리하려다 보니 어려움을 겪었고 트리거와 REPLACE 함수를 이용해 만들어보았는데 그렇게 쿼리를 작성 하다보니 테이블 구조 쪽에 문제가 생겨 정규화가 전혀 진행되지 않은 테이블이 아니고선 구현이 되지 않는 상황을 맞닥뜨렸습니다. 이런 테이블 구조로는 쿼리를 복잡하게 짜서 칭찬을 받기보다 테이블 구조가 문제가 있다는 말을 듣게될 것 같아 강사님께도 질문드려보고 하였습니다. 하지만 데이터베이스 챕터를 한다고해서 꼭 서비스 측면을 데이터베이스로 구현할 필요가 없다는 것을 알아차리고 팀원들과 함께 고민한 끝에 뜻을 맞춰 하드코딩으로 알림 발송 트리거를 만들게되었습니다.|

<br>

## 12. 팀 회고
|팀원|회고 내용|
|:---:|-|
|장기현||
|이재석||
|이준서||
|이태희||
