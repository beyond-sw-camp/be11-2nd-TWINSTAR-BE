  <div style="text-align: center;">
  <strong>Team TwinStar</strong>
</div>

| <a href="https://github.com/ki-hyun-Jang"><img src="https://avatars.githubusercontent.com/u/185437523?v=4" width="220px" /></a> | <a href="https://github.com/leejaeseok-98"><img src="https://avatars.githubusercontent.com/u/185016962?v=4" width="220px" /></a> | <a href="https://github.com/LetsSeeTerrapin"><img src="https://avatars.githubusercontent.com/u/188145635?v=4" width="220px" /></a> | <a href="https://github.com/SWEETP0T4T0"><img src="https://avatars.githubusercontent.com/u/185882822?v=4" width="220px" /></a> |
| :---: | :---: | :---: | :---: |
| **팀장** 장기현 | **팀원** 이재석 | **팀원** 이준서 | **팀원** 이태희 |

<br>

## 🎮 기술 스택

**BACKEND**
<br>
![SPRING](https://img.shields.io/badge/Spring-green?style=for-the-badge&logo=Spring&logoColor=white)
![SPRING BOOT](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=Spring%20Boot&logoColor=white)
![SPRING SECURITY](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![SPRING DATA JPA](https://img.shields.io/badge/Spring_Data_JPA-13C100?style=for-the-badge&logo=Spring%20Boot&logoColor=white)
![WEBSOCKET](https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=socketdotio&logoColor=white)
![STOMP](https://img.shields.io/badge/STOMP-010101?style=for-the-badge&logo=messenger&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)
![HIBERNATE](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![DOCKER](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
<br>
<br>
<br>
**FRONTEND**
<br>
![Vue.js](https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white)
![JavaScript](https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![HTML5](https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1867C0?style=for-the-badge&logo=css3&logoColor=white)
![AXIOS](https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white)
![VUE ROUTER](https://img.shields.io/badge/Vue_Router-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white)
![VUETIFY](https://img.shields.io/badge/Vuetify-1867C0?style=for-the-badge&logo=vuetify&logoColor=#1867C0)
<br>
<br>
<br>
**DB**
<br>
![mariadb](https://img.shields.io/badge/mariadb-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![amazons3](https://img.shields.io/badge/amazons3-569A31?style=for-the-badge)
![RABBITMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
<br>
<br>
<br>

**협업도구**
<br>
![Git](https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white)
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)
&nbsp;![Discord](https://img.shields.io/badge/Discord-%235865F2.svg?style=for-the-badge&logo=discord&logoColor=white)
![POSTMAN](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
<br>
<br>
<br>

## 📝 요구사항 명세서

| 화면 | 구현 기능 | API 작성자 | 설명 |
|------|-----------|------------|----------------------------------------------------------------------------------------------------|
| 🟩 **로그인** | 회원가입 | 🔵 **장기현** | 아이디(필수), 비밀번호(필수), 성별(필수),<br> 닉네임 (중복검사, 필수) 입력 |
| 🟩 **로그인** | 자동 로그인 | 🔵 **장기현** | 이전에 로그인화면에서 자동로그인 팝업 체크 후 로그인할 경우,<br> 이후 자동 로그인이 가능하다. |
| 🟩 **로그인** | 이메일 중복 체크 | 🟡 **이재석** | 회원가입 시 이메일 중복 체크한다. |
| 🟦 **프로필** | 개인 프로필 입력 | 🟡 **이재석** | 닉네임(필수, 중복X 변경O),<br> 프로필사진(선택), 성별(필수) 입력 |
| 🟦 **프로필** | 프로필 사진 변경 가능 | 🟡 **이재석** | 프로필 사진은 jpg, jpeg, png 확장자만<br> 업로드 가능하며, 비울 수 있다. |
| 🟦 **프로필** | 팔로우/팔로워 | 🟠 **이준서** | 회원이 팔로우한 목록, 팔로워 목록을 볼 수 있으며,<br> 각자 인원수가 나타난다. |
| 🟦 **프로필** | 프로필 텍스트 변경 | 🟡 **이재석** | 프로필 내용을 변경할 수 있다. |
| 🟦 **프로필** | 계정 공개 범위 변경 | 🟡 **이재석** | 계정 공개 범위 변경 가능하다. |
| 🟦 **프로필** | 게시물 노출 | 🟡 **이재석** | 내가 올린 게시물들이 모여져 노출되어 있다. |
| 🟦 **프로필** | 게시물 로딩 | 🟡 **이재석** | 12개씩 보이며, 스크롤링할 때마다 추가적으로 로드된다. |
| 🟧 **게시글** | 게시글 작성 | 🔵 **장기현** | 사진, 동영상, 글을 작성할 수 있다.<br> 해시태그 및 맞팔로워 태그가 가능하다. |
| 🟧 **게시글** | 게시물 게시일 | 🔵 **장기현** | 게시글 작성 시 1일전, 일주일전,<br> 한 달전, 일년전과 같은 방식으로 나타난다. |
| 🟧 **게시글** | 게시물 범위 | 🔵 **장기현** | 게시자의 기준으로 게시물의 게시물 노출,<br> 댓글달기 범위 설정 가능 |
| 🟧 **게시글** | 게시글 수정 | 🔵 **장기현** | 게시글을 수정할 수 있으며,<br> 사진과 동영상은 수정 불가. 텍스트와 태그 수정 가능 |
| 🟧 **게시글** | 게시글 삭제 | 🔵 **장기현** | 게시글을 삭제할 수 있다. |
| 🟧 **게시글** | 게시글 좋아요 | 🔵 **장기현** | 게시글에 좋아요를 할 수 있으며,<br> 좋아요 숫자 및 누가 눌렀는지 확인 가능 |
| 🟧 **게시글** | 게시글 더보기 | 🔵 **장기현** | 게시글 내용이 길어지면<br> 더보기 버튼을 통해 전체 내용 확인 가능 |
| 🟧 **게시글** | 게시글 목록 | 🔵 **장기현** | 팔로우한 사람들의 게시물을<br> 게시일시 기준 오름차순으로 볼 수 있다. |
| 🟥 **댓글** | 댓글 작성 | 🔵 **장기현** | 게시글에는 댓글을 작성할 수 있으며<br> 맞팔로워는 태그가 가능하다. |
| 🟥 **댓글** | 댓글 게시일 | 🔵 **장기현** | 댓글 작성 시 1일전, 일주일전,<br> 한 달전, 일년전과 같은 방식으로 나타난다. |
| 🟥 **댓글** | 댓글 나열 | 🔵 **장기현** | 작성일 기준 오래된 순으로 나열된다. |
| 🟥 **댓글** | 댓글 수정 | 🔵 **장기현** | 댓글을 수정할 수 있다. |
| 🟥 **댓글** | 댓글 삭제 | 🔵 **장기현** | 댓글을 삭제할 수 있으며,<br> 삭제된 댓글입니다.로 표시된다. |
| 🟥 **댓글** | 댓글 좋아요 | 🔵 **장기현** | 댓글에 좋아요를 할 수 있으며,<br> 좋아요 숫자 및 누가 눌렀는지 확인 가능 |
| 🟪 **채팅** | 메시지 보내기 | 🔵 **장기현** | 유저는 다른 유저에게 메시지를 보낼 수 있다. |
| 🟪 **채팅** | 메시지 받기 | 🔵 **장기현** | 유저는 다른 유저에게 메시지를 받을 수 있다. |
| 🟪 **채팅** | 그룹채팅방 | 🔵 **장기현** | 그룹 채팅방을 개설하여<br> 여러 유저와 메시지를 주고받을 수 있다. |
| 🟪 **채팅** | 메시지 시각 | 🔵 **장기현** | 보내거나 받은 메시지의 시간을 알 수 있다. |
| 🟨 **이슈** | 팔로우 알림 | 🔵 **장기현** | 누군가 팔로우를 할 경우 알림이 온다. |
| 🟨 **이슈** | 댓글 작성 알림 | 🔵 **장기현** | 내 게시글에 댓글이 작성될 경우 알림이 온다. |
| 🟨 **이슈** | 대댓글 작성 알림 | 🔵 **장기현** | 내 댓글에 대댓글이 작성될 경우 알림이 온다. |
| 🟨 **이슈** | 좋아요 알림 | 🔵 **장기현** | 내 댓글 또는 게시글에 좋아요를 누르면 알림이 온다. |
| 🟦 **관리자** | 관리자 유저검색 | 🟡 **이재석** | 닉네임으로 유저 검색 가능 |
| 🟦 **관리자** | 유저 상세 조회 | 🟡 **이재석** | 유저의 정지일, 계정 삭제 여부 등의 상세 정보를 조회 |
| 🟦 **관리자** | 관리자 권한 부여 및 회수 | 🟡 **이재석** | 관리자가 유저에게 관리자 권한 부여 및 회수 가능 |
| 🟦 **관리자** | 관리자 계정 정지 | 🟡 **이재석** | 계정 정지가 가능하며 정지 기간을 설정할 수 있음 |
| 🟩 **검색** | 닉네임 검색 | 🟡 **이재석** | 닉네임을 입력하면 해당 유저와 비슷하거나 일치하는 유저를 검색 가능 |
| 🏿 **기타** | 유저 간 차단 | 🟡 **이재석** | 유저 A가 유저 B를 차단하면 해당 게시물 및 댓글, 프로필을 볼 수 없음 |
| 🏿 **기타** | 팔로우 요청 | 🟠 **이준서** | 유저 간 팔로우 요청이 가능하다. |
| 🏿 **기타** | 회원탈퇴 | 🟡 **이재석** | 회원탈퇴 기능 |

---
<br>
<br>

## 🗓️ WBS
![WBS](https://github.com/user-attachments/assets/5508a623-ae54-42e9-b3d2-5313bfaf2a34)

<br>
<br>

## 📌 ERD
![ERD](erd.png)

<br>
<br>

## ⚙️ 시스템 아키텍처


## 🖥️ UX/UX 단위테스트


## 💬 팀 회고 - 기술적 경험 정리
|팀원|회고 내용|
|:---:|-|
|장기현| WebSocket(STOMP)를 사용하여 실시간 1:1 채팅 및 그룹 채팅을 구현했지만, 다중 서버 환경에서는 WebSocket 세션이 서버 간 공유되지 않기 때문에 Redis Pub/Sub을 이용한 확장이 필요했습니다. 이를 적용하는 과정에서 세션 정보 동기화 문제와 메시지 유실 방지 방안을 고민해야 했지만, 결국 Redis를 활용한 메시지 브로드캐스팅을 통해 다중 서버에서도 안정적으로 동작하도록 개선할 수 있었습니다.<br><br> 또한, 좋아요 기능에서는 Redis를 활용하여 캐싱 및 비동기 처리 방식으로 성능을 최적화했습니다. 처음에는 좋아요 요청이 직접 DB에 반영되었으나, 트래픽 증가로 인해 부하가 발생할 가능성이 있었는데요, 이를 해결하기 위해 좋아요 데이터를 Redis에 먼저 저장하고, 일정 주기로 DB에 반영하는 방식을 적용하여 DB 부하를 줄이면서도 실시간 반영이 가능하도록 개선했습니다.<br><br> 알림 서비스의 경우, 처음에는 WebSocket을 사용할 계획이었으나, 단방향 메시지 전송이 주를 이루는 알림 시스템에는 SSE(Server-Sent Events)가 더 적합하다고 판단하여, SSE를 활용하면 WebSocket처럼 불필요한 연결 유지 비용 없이 클라이언트가 서버로부터 실시간 이벤트를 받을 수 있기 때문에 서버 리소스를 절약하면서도 안정적으로 알림을 전송할 수 있도록 구현했습니다.<br><br> 이번 경험을 통해 WebSocket과 SSE의 적절한 활용 방식, Redis를 이용한 성능 최적화 및 확장성 확보, 비동기 이벤트 처리 방식의 중요성을 배울 수 있었습니다. 다만, WebSocket을 다중 서버 환경에서 확장하는 과정이 쉽지 않았고, 다음번에는 WebSocket과 SSE를 혼합하여 더 유연한 실시간 시스템을 구축하고, Redis 사용 시 데이터 정리 전략을 명확히 수립하는 것이 필요하다고 느꼈습니다.<br><br> 팀원분들 다들 고생하셨습니다 :) |
|이재석|  |
|이준서|  |
|이태희|  |
