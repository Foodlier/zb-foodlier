# zb-foodlier

<hr>

### 1️⃣ 커밋 컨벤션

**기본 적인 커밋 메시지 구조는 `제목`,`본문`,`꼬리말` 세가지 파트로 나누고, 각 파트는 빈줄을 두어 구분합니다.**

```
type: subject

body 

footer

ex) 
git commit -m "Fix: 버그 수정

사용자가 로그인할 때 비밀번호가 올바르게 검사되지 않는 버그입니다. 
이 문제를 수정하기 위해 비밀번호 검사 로직을 업데이트했습니다.

resolves: #1234, #1235
```

### 📌 Type

- 어떤 의도인지 짧게 설명합니다.
- 영어로 쓰되 첫 문자는 대문자입니다.
- 스페이스 바는 콜론 뒤에서만 사용합니다.
    
    ```
    // bad
    type : subject
    
    // good
    type: subject
    ```
    
- 타입의 종류
    
    ![image](https://github.com/Foodlier/zb-foodlier/assets/80212139/df902413-fdc8-47a9-9afe-f6f2cb55c3e6)

    

### 📌 Subject

- 제목은 **50글자** 이내로 제한합니다.
- 제목 끝에는 마침표 포함 특수문자를 사용하지 않습니다.

### 📌 Body

- 본문은 **한 줄 당** 72자 내로 작성합니다.
- 본문 내용은 양에 구애받지 않고 **최대한 상세히 작성**합니다.
- 본문 내용은 **어떻게 변경했는지** 보다 **무엇을 변경했는지** 또는 **왜 변경했는지**를 설명합니다.

### 📌 Footer

- 꼬리말은 optional이고 이슈 트래커 ID를 작성합니다.
- 꼬리말은 "유형: #이슈 번호" 형식으로 사용합니다.
- 여러 개의 이슈 번호를 적을 때는 쉼표로 구분합니다.
- 이슈 트래커 유형은 다음 중 하나를 사용합니다.
    - Fixes: 이슈 수정중 (아직 해결되지 않은 경우)
    - Resolves: 이슈를 해결했을 때 사용
    - Ref: 참고할 이슈가 있을 때 사용
    - Related to: 해당 커밋에 관련된 이슈번호 (아직 해결되지 않은 경우)
 
<hr>

### 2️⃣ FE 폴더 구조

![image](https://github.com/Foodlier/zb-foodlier/assets/80212139/f3f1f4fd-b2c9-40be-812f-83d5b11c4319)

* assets
   * 컴포넌트 내부에서 사용하는 이미지 파일인 경우 이 assets 폴더에 위치시켜야 합니다.
* components
   * 재사용 가능한 컴포넌트들이 위치하는 폴더입니다. 컴포넌트는 매우 많아질 수 있기 때문에 이 폴더 내부에서 하위폴더로 추가로 분류하는 경우가 많습니다.
* constants
   * 공통적으로 사용되는 상수들을 정의한 파일들이 위치하는 폴더입니다.
* mocks
   * MSW에 대해 정의한 파일들이 위치하는 폴더입니다.  
* pages
   * react router등을 이용하여 라우팅을 적용할 때 페이지 컴포넌트를 이 폴더에 위치시킵니다.
* services
   * api관련 로직의 모듈 파일이 위치하며 auth와 같이 인증과 관련된 파일이 포함되기도 합니다.
* store
   * redux 등을 사용할 때 관련 파일들이 위치합니다.
* styles
   * css 파일들이 포함되는 폴더입니다.
* utils
   * 정규표현식 패턴이나 공통함수 등 공통으로 사용하는 유틸 파일들이 위치하는 폴더입니다.
     
<hr>

### 3️⃣ BE 폴더 구조

```text

src
|-- main
|   |-- java
|   |   `-- com
|   |       `-- zerobase
|   |           `-- foodlier
|   |               |-- FoodlierApplication.java
|   |               |-- common
|   |               |   |-- elasticsearch
|   |               |   |   `-- config
|   |               |   |       `-- file
|   |               |   |-- exception
|   |               |   |   |-- dto
|   |               |   |   |-- exception
|   |               |   |   `-- handler
|   |               |   |-- jpa
|   |               |   |   |-- audit
|   |               |   |   `-- config
|   |               |   |-- redis
|   |               |   |   |-- domain
|   |               |   |   |   `-- model
|   |               |   |   |-- dto
|   |               |   |   |-- exception
|   |               |   |   |-- exeption
|   |               |   |   |-- repository
|   |               |   |   `-- service
|   |               |   |-- s3
|   |               |   |   |-- config
|   |               |   |   |-- exception
|   |               |   |   `-- service
|   |               |   |-- security
|   |               |   |   |-- config
|   |               |   |   |-- constants
|   |               |   |   |-- exception
|   |               |   |   |-- filter
|   |               |   |   `-- provider
|   |               |   |       |-- constants
|   |               |   |       `-- dto
|   |               |   `-- swagger
|   |               |       `-- config
|   |               |-- global
|   |               |   |-- auth
|   |               |   |   `-- controller
|   |               |   |-- member
|   |               |   |   `-- mail
|   |               |   |       `-- facade
|   |               |   |-- recipe
|   |               |   |   `-- facade
|   |               |   `-- request
|   |               |       |-- controller
|   |               |       |-- dto
|   |               |       `-- facade
|   |               `-- module
|   |                   |-- comment
|   |                   |   |-- comment
|   |                   |   |   |-- domain
|   |                   |   |   |   `-- model
|   |                   |   |   |-- exception
|   |                   |   |   |-- repository
|   |                   |   |   `-- service
|   |                   |   `-- reply
|   |                   |       |-- domain
|   |                   |       |   `-- model
|   |                   |       |-- exception
|   |                   |       |-- reposiotry
|   |                   |       `-- servcie
|   |                   |-- dm
|   |                   |   |-- dm
|   |                   |   |   |-- domain
|   |                   |   |   |   `-- model
|   |                   |   |   |-- exception
|   |                   |   |   |-- repository
|   |                   |   |   `-- service
|   |                   |   `-- room
|   |                   |       |-- domain
|   |                   |       |   `-- model
|   |                   |       |-- exception
|   |                   |       |-- repository
|   |                   |       `-- service
|   |                   |-- heart
|   |                   |   |-- domain
|   |                   |   |   `-- model
|   |                   |   |-- exception
|   |                   |   |-- reposiotry
|   |                   |   `-- service
|   |                   |-- history
|   |                   |   |-- charge
|   |                   |   |   |-- exception
|   |                   |   |   |-- model
|   |                   |   |   |-- repository
|   |                   |   |   `-- service
|   |                   |   `-- transaction
|   |                   |       |-- exception
|   |                   |       |-- model
|   |                   |       |-- repository
|   |                   |       |-- service
|   |                   |       `-- type
|   |                   |-- member
|   |                   |   |-- chef
|   |                   |   |   |-- domain
|   |                   |   |   |   `-- model
|   |                   |   |   |-- exception
|   |                   |   |   |-- repository
|   |                   |   |   |-- service
|   |                   |   |   `-- type
|   |                   |   `-- member
|   |                   |       |-- domain
|   |                   |       |   |-- model
|   |                   |       |   `-- vo
|   |                   |       |-- exception
|   |                   |       |-- mail
|   |                   |       |   |-- constants
|   |                   |       |   `-- service
|   |                   |       |-- repository
|   |                   |       |-- service
|   |                   |       `-- type
|   |                   |-- notification
|   |                   |   |-- domain
|   |                   |   |   `-- model
|   |                   |   |-- exception
|   |                   |   |-- repository
|   |                   |   `-- service
|   |                   |-- recipe
|   |                   |   |-- controller
|   |                   |   |-- domain
|   |                   |   |   |-- dto
|   |                   |   |   |-- model
|   |                   |   |   |-- type
|   |                   |   |   `-- vo
|   |                   |   |-- exception
|   |                   |   |-- repository
|   |                   |   `-- service
|   |                   |-- request
|   |                   |   |-- domain
|   |                   |   |   |-- model
|   |                   |   |   `-- vo
|   |                   |   |-- exception
|   |                   |   |-- repository
|   |                   |   `-- service
|   |                   `-- review
|   |                       |-- chef
|   |                       |   |-- domain
|   |                       |   |   `-- model
|   |                       |   |-- exception
|   |                       |   |-- repository
|   |                       |   `-- service
|   |                       `-- recipe
|   |                           |-- domain
|   |                           |   `-- model
|   |                           |-- exception
|   |                           |-- repository
|   |                           `-- service

```
도메인을 기준으로 패키지 계층을 분리하였습니다.

1. common
   * 여러 모듈에서 공통적으로 사용되는 코드를 포함합니다. 데이터베이스와 관련된 설정, 예외 처리, 보안 등과 같은 공통 기능들을 위치시켰습니다.
2. global
   * 전역적인 기능을 다루는 모듈들을 포함합니다. 파사드 패턴을 준수하여 사용자 인증, 회원 관리, 레시피 관리 등과 같이 여러 모듈에서 공유되는 전역적인 기능들을 위치시켰습니다.  
4. module
   * 프로젝트의 기능을 도메인 별로 분류하여 구조화하는 역할을 합니다. 각각의 도메인은 특정한 기능 또는 업무를 담당하며, 그 안에는 독립적인 기능을 구현하는데 필요한 다양한 패키지와 클래스들이 포함됩니다.
