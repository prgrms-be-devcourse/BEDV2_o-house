# 🏠 BEDV2_o-house

조셉팀 오늘의 집 클론코딩

# 프로젝트 개요

### 팀 구성

<table>
  <tr>
    <td align="center"><b>Product Owner</b></td>
    <td align="center"><b>Scrum Master</b></td>
    <td align="center"><b>Developer</b></td>
    <td align="center"><b>Developer</b></td>
    <td align="center"><b>Developer</b></td>
  </tr>
  <tr>
    <td>
        <a href="https://github.com/blessing333">
            <img src="https://avatars.githubusercontent.com/u/65841596?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/res-cogitans">
            <img src="https://avatars.githubusercontent.com/u/54278885?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/epicblues">
            <img src="https://avatars.githubusercontent.com/u/19306609?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/kkj0419">
            <img src="https://avatars.githubusercontent.com/u/72663337?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/HunkiKim">
            <img src="https://avatars.githubusercontent.com/u/66348135?v=4" width="100px" />
        </a>
    </td>
  </tr>

  <tr> 
    <td align="center"><a href="https://github.com/blessing333">이민재</a></td>
    <td align="center"><a href="https://github.com/res-cogitans">이한빈</a></td>
    <td align="center"><a href="https://github.com/epicblues">김민성</a></td>
    <td align="center"><a href="https://github.com/kkj0419">김현정</a></td>
    <td align="center"><a href="https://github.com/HunkiKim">김훈기</a></td>
  </tr>

</table>


### 주제

오늘의 집 백엔드 서비스 클론 코딩

1. 회원
2. 커뮤니티
3. 스토어

### 프로젝트 기획 및 설명

육각형 아키텍처를 코드로 구현해보는 것에 초점을 두었습니다.

우리 조셉 팀에서 이번 프로젝트를 바라본 관점은 다음과 같습니다.

- 서비스에 집중하기 보다는 백엔드 개발자로서 갖춰야하는 기본기에 집중
- 협업에 대한 숙련을 중점적으로 다루기로 함

이런 목적을 바탕으로 클론할 서비스를 오늘의 집으로 선택하였습니다.

오늘의집을 선택했던 이유는 다음과 같습니다.

- 개인화 및 커머스와 커뮤니티가 결합되어 있는 서비스입니다.
    - 업무 분담이 용이해집니다: 사용자1, 커머스2, 커뮤니티2
    - 이로 인해 Github 머지 충돌을 감소시키고, 원활하게 협업을 수행하고자 하였습니다.
    - 넓은 주제로 인하여 수업에서 배웠던 내용들을 활용하기 용이해보였기 때문입니다.

---

### ⛏️ 기술 스택

---

사용한 기술 스택들의 이유는 다음과 같습니다.

- `Java Version` : `11`
    - 11 이상 버전의 기술을 사용할 일이 아직 많이 없었기 때문
- `Build Tool` : `gradle`
    - 빌드툴은 `maven`보다 가독성이 좋아 `gradle`을 선택하였습니다.
- `Data Layer`: `Spring Data JPA` + `QueryDSL`
    - 팀원들이 기술에 친숙한 정도를 감안해서 선택
- `DB`: `H2 DB`
    - 빠른 개발에 용이해서 선택
- `API 문서화`: `Spring Rest Docs`
    - 팀원들이 익숙한 기술임

### **협업 툴**

- `Jira`: 티켓 발행, 일정 관리
    - 많은 회사들이 사용하고 있어서, 취업 후 협업에 쉽게 적응하기 위해 선택했습니다.
    - 다른 툴보다 Agile한 협업을 쉽게 실천할 수 있어서 선택했습니다.
- `Mural`: 유저스토리 작성
    - 유저스토리를 자유로운 형식으로 시각화 하기 편해서 선택했습니다.

---

### 

---

### 컨벤션

- `Branch Strategy`: `github flow`
    - git flow가 첫 협업에선 어렵다고 생각되었기 때문에 github flow를 먼저 적용하는 방향으로 잡았습니다.
- `Coding Convention`: `Naver Java Convention`
    - 한국에서 가장 보편적이라고 생각했기 때문에 선택하였습니다.
- `헥사고날 아키텍처`
    - 애플리케이션 계층에서 특정 프레임워크에 지나치게 의존하는 문제를 분석해보고, 해결책을 탐색하는 시간을 가질 수 있었습니다.
    - Jpa를 상속받는 클래스를 어댑터로 판단하여, infra 계층으로 옮겼습니다. 도메인에서는 PORT 역할을 하는 인터페이스만 작성해서 jpa에 대한 의존성을 최소화했습니다.
    - Service 클래스에서 사용자 정보를 받기 위해 Spring Security에 직접 의존하는 것이 헥사고날 아키텍처에 맞지 않는다고 판단해서, Security 에 의존하는 코드를 Controller로 옮기고, 서비스에서는 사용자 식별자만 받도록 리팩터링 했습니다.
        - 리팩터링으로 인해 서비스 코드의 테스트에서 security 관련 설정을 제거하게 되어 테스트 코드가 간결해지는 효과를 얻었습니다.

---

### ERD

![ers](https://user-images.githubusercontent.com/65841596/178052360-6c060adc-14dc-4708-a221-046fafd87d84.png)

### API docs
https://blessing333.github.io/

