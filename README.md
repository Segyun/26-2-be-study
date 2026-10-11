# 26-2-be-study
<details> 
<summary>1주차 과제</summary>

<ul>
<li>
<details>
<summary>심화반</summary>

# 메모 CRUD API 구현 과제
## 1. 학습 목표와 완료 기준

- 수업에서 함께 구현한 기본 CRUD 코드
- 공통 필수 API 5개, 기본 오류 처리, 실행 방법을 갖춘 PR을 제출

| 항목 | 기준 |
| --- | --- |
| 구현 스택 | 자유|
| 저장 방식 | DB X, 메모리 (dict, Map 등) |
| 초기 상태 | 서버를 새로 시작하면 메모가 없는 상태 |
| 실행 범위 | 로컬 서버에서 요청을 보내며 확인 |
| 필수 제출물 | 구현 코드, 의존성 설정, README, PR |
| 제출 기한 | 2026.10.11. 23:59까지 |

## 2. 공통 데이터 규칙

메모는 다음 두 필드로 구성합니다.

```json
{
"id": 1,
"content": "첫 메모"
}
```

| 필드 | 타입 | 규칙 |
| --- | --- | --- |
| id | 정수 | 서버가 발급하는 고유한 양의 정수. 같은 서버 실행 중 중복되거나 삭제한 ID가 재사용되지 않음 |
| content | 문자열 | 최소 한 글자 이상 |

- 생성·수정 요청에는 `content`를 보내며 클라이언트가 ID를 지정하거나 수정하는 경우는 없습니다.
- 빈 문자열, 공백만 있는 문자열, content 누락, null, 숫자 등 문자열이 아닌 값은 거부합니다.

## 3. API 명세

- PR 설명란에 기본 URL을 적어주세요!
- 실패 응답 본문의 형식과 메시지는 자유이며, 예시는 참고용입니다.

| 작업 | 메서드 | 경로 | 성공 코드 | 성공 응답 |
| --- | --- | --- | --- | --- |
| 생성 | POST | /memos | 201 | 생성한 메모 객체 |
| 전체 조회 | GET | /memos | 200 | 메모 객체의 배열 |
| 단건 조회 | GET | /memos/{memo_id} | 200 | 해당 메모 객체 |
| 수정 | PUT | /memos/{memo_id} | 200 | 수정한 메모 객체 |
| 삭제 | DELETE | /memos/{memo_id} | 200 | 삭제 결과 객체 |


### 3.1 생성: POST /memos

요청:

```json
{
"content": "첫 메모"
}
```

응답: `201 Created`

```json
{
"id": 1,
"content": "첫 메모"
}
```

### 3.2 전체 조회: GET /memos

- 저장된 메모 '전체'를 반환 
- 저장된 메모가 없으면 `200`과 `[]`를 반환해주세요!

응답: `200 OK`

```json
[
{ "id": 1, "content": "첫 메모" },
{ "id": 2, "content": "두 번째 메모" }
]
```

### 3.3 단건 조회: GET /memos/{memo_id}

- 해당 ID가 없으면 `404`를 반환해주세요!

응답: `200 OK`

```json
{
"id": 1,
"content": "첫 메모"
}
```

### 3.4 수정: PUT /memos/{memo_id}

- 기존 메모의 수정 가능한 필드인 content 전체를 교체합니다. 
- 없는 ID에는 새 메모를 만들지 않고 `404`를 반환합니다.

요청:

```json
{
"content": "수정한 메모"
}
```

응답: `200 OK`

```json
{
"id": 1,
"content": "수정한 메모"
}
```

### 3.5 삭제: DELETE /memos/{memo_id}

- 전체 목록에는 삭제한 메모가 없어야 합니다. 
- 없는 ID 또는 이미 삭제한 ID의 삭제 요청에는 `404`를 반환해주세요!

응답: `200 OK`

```json
{
"message": "삭제 완료",
"id": 1
}
```

## 4. 필수 검증 시나리오

새 서버에서 아래 순서로 요청을 보냅니다. 
- A: 생성한 첫 메모의 실제 ID
- B: 두 번째 메모의 실제 ID
- N: 생성된 적 없는 양의 정수 ID

| 번호 | 확인할 상황 | 기대 결과 |
| --- | --- | --- |
| V01 | 새 서버에서 목록 조회 | 200, 빈 배열 [] |
| V02 | 공백이 포함된 내용으로 메모 A 생성 | 201 |
| V03 | 같은 내용으로 메모 B 생성 | 201, A와 B의 ID가 다름. 목록에 둘 다 있음 |
| V04 | A 상세 조회 | 200, 저장한 ID와 내용 |
| V05 | A 내용 수정 후 상세·목록 조회 | 200, A의 ID 유지·내용 변경. B의 내용 유지, 총 2개 |
| V06 | A 삭제 후 상세·목록 조회 | 삭제 200, 삭제 결과 JSON. 상세 404, 목록에는 B만 남음 |
| V07 | 없는 ID N의 조회·수정·삭제 | 모두 404. 새 메모가 생기거나 기존 메모가 변하지 않음 |
| V08 | 생성·수정에 {}, content:null, content:123 사용 | 각각 400 또는 422. 잘못된 생성·수정이 반영되지 않음 |
| V09 | 조회·수정·삭제의 ID 자리에 abc 사용 | 각각 400 또는 422. 서버가 계속 요청을 처리할 수 있음 |
| V10 | A를 다시 삭제하고 새 메모 C 생성 | 재삭제 404, C 생성 201, C의 ID가 A와 B와 다름 |
| V11 | 서버를 완전히 종료한 뒤 재시작하여 목록 조회 | 200, 빈 배열 [] |


## 5. PR 필수 내용

- 언어·런타임·프레임워크 버전.
- 의존성 설치 명령.
- 서버 실행·종료 방법, 포트와 기본 URL.
- 선택 기능과 미완성 사항이 있다면 그 내용.
- 생성된 빌드 결과물이나 의존성 설치 폴더는 PR에서 제외해주세요!


</details>

<details>

<summary><b>초보반</b></summary>

# 게시글 CRUD REST API 구현 과제

## 1. 학습 목표와 완료 기준

Spring Boot를 이용하여 간단한 **게시글 CRUD REST API**를 구현합니다.

- Spring Boot 프로젝트 생성
- 게시글 CRUD API 구현
- HTTP Method와 Request / Response 흐름 이해
- Controller / Service 역할 고민
- GitHub Repository를 통한 과제 제출

10/6 스터디 전까지 가능한 만큼 먼저 구현하고, 스터디 이후 학습한 내용을 바탕으로 코드를 보완하여 제출합니다.

<table>
  <thead>
    <tr>
      <th>항목</th>
      <th>기준</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>구현 스택</td>
      <td>Spring Boot</td>
    </tr>
    <tr>
      <td>저장 방식</td>
      <td>DB X, 메모리 (<code>List</code>, <code>Map</code> 등)</td>
    </tr>
    <tr>
      <td>초기 상태</td>
      <td>서버 재시작 시 데이터가 없는 상태</td>
    </tr>
    <tr>
      <td>필수 제출물</td>
      <td>구현 코드, README</td>
    </tr>
    <tr>
      <td>제출 기한</td>
      <td>2026.10.11 23:59까지</td>
    </tr>
  </tbody>
</table>

---

## 2. 공통 데이터 규칙

게시글은 다음 세 필드로 구성합니다.

```text
Post
├─ id
├─ title
└─ content
```

<table>
  <thead>
    <tr>
      <th>필드</th>
      <th>규칙</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>id</code></td>
      <td>서버에서 생성하는 고유한 값</td>
    </tr>
    <tr>
      <td><code>title</code></td>
      <td>게시글 제목</td>
    </tr>
    <tr>
      <td><code>content</code></td>
      <td>게시글 내용</td>
    </tr>
  </tbody>
</table>

이번 과제에서는 **DB를 사용하지 않습니다.**

`List`, `Map` 등 원하는 자료구조를 이용하여 메모리에 저장합니다.  
서버를 종료하고 다시 실행하면 기존 데이터가 사라져도 됩니다.

---

## 3. 필수 기능

다음 5개의 API를 구현합니다.

<table>
  <thead>
    <tr>
      <th>기능</th>
      <th>설명</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>게시글 등록</td>
      <td>새로운 게시글 생성</td>
    </tr>
    <tr>
      <td>전체 조회</td>
      <td>모든 게시글 조회</td>
    </tr>
    <tr>
      <td>단건 조회</td>
      <td>특정 게시글 조회</td>
    </tr>
    <tr>
      <td>게시글 수정</td>
      <td>기존 게시글 수정</td>
    </tr>
    <tr>
      <td>게시글 삭제</td>
      <td>기존 게시글 삭제</td>
    </tr>
  </tbody>
</table>

URI, HTTP Method, Request / Response 형식 등 **구체적인 API 설계는 직접 결정합니다.**

### 직접 고민해볼 것

- URI는 어떻게 설계할까?
- 각 기능에 어떤 HTTP Method를 사용할까?
- Controller와 Service의 역할은 어떻게 나눌까?
- Request / Response는 어떤 형태로 만들까?
- DTO가 필요할까?
- 어떤 HTTP Status Code를 반환하는 것이 적절할까?
- 존재하지 않는 게시글은 어떻게 처리할까?

검색, 공식 문서, 강의, 블로그, 예제 등을 자유롭게 참고해도 됩니다.

---

## 4. 선택 Challenge

기본 기능을 완료했다면 원하는 내용을 추가로 구현할 수 있습니다.

- Request / Response DTO 분리
- `ResponseEntity` 사용
- 존재하지 않는 게시글 처리
- 적절한 HTTP Status Code 적용
- 간단한 Validation
- Repository 계층 분리

> Challenge는 필수가 아닙니다. 기본 CRUD 구현을 우선으로 진행해주세요.

---

## 5. 동작 확인

Postman, IntelliJ HTTP Client 등 원하는 도구를 이용하여 다음 흐름이 정상적으로 동작하는지 확인합니다.

```text
게시글 생성
    ↓
전체 조회
    ↓
단건 조회
    ↓
게시글 수정
    ↓
게시글 삭제
```

Challenge를 구현했다면 잘못된 요청이나 존재하지 않는 게시글에 대한 처리도 확인해보세요.

---

## 6. 제출 방법

과제는 **개인 GitHub Repository**를 통해 제출합니다.

이번에 생성한 Spring Boot 프로젝트는 이후 과제에서도 새로 만들지 않고 **계속 발전시키는 방식**으로 사용합니다.

Repository는 **Public을 권장**합니다.

Private Repository로 진행해야 하는 경우 아래 계정을 Collaborator로 추가해주세요.

```text
GitHub: sinyeowon
Email: ioohyou@knu.ac.kr
```

### README 필수 내용

- 사용한 언어 / Java / Spring Boot 버전
- 프로젝트 실행 방법
- 구현한 API와 기능
- 진행한 Challenge (있는 경우)
- 구현하면서 고민했거나 어려웠던 점

---

## 7. 참고

과제를 완성하지 못해도 괜찮습니다.  
구현 중 막힌 부분이 있다면 현재까지 작성한 코드로 제출해도 됩니다.

이번 과제에서는 완성된 결과물보다

**직접 구현 → 스터디에서 학습 → 코드 보완 → 코드 리뷰 → 개선**

과정을 중요하게 생각합니다.

궁금하거나 구현 중 막히는 부분은 자유롭게 질문해주세요!

</details>
</details>

<details>
<summary>2주차 과제</summary>

<ul>
<li>
<details>
<summary><b>초보반</b></summary>

# 과제 2. 기존 프로젝트에 DB/JPA 적용하기

## 1. 학습 목표와 완료 기준

이번 과제에서는 새로운 프로젝트를 생성하지 않고, **1주차 과제에서 만든 게시글 CRUD REST API를 그대로 발전시킵니다.**

1주차에서는 `List`, `Map` 등을 이용해 데이터를 메모리에 저장했다면,  
2주차에서는 **Database와 Spring Data JPA를 적용하여 실제 DB에 게시글 데이터를 저장**합니다.

- 기존 게시글 CRUD API 유지
- DB 연결
- JPA Entity 작성
- Spring Data JPA Repository 적용
- Entity / DTO 역할 고민
- 존재하지 않는 데이터 처리
- 예외 처리 흐름 고민

<table>
  <thead>
    <tr>
      <th>항목</th>
      <th>기준</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>구현 스택</td>
      <td>Spring Boot, Spring Data JPA</td>
    </tr>
    <tr>
      <td>저장 방식</td>
      <td>DB 사용</td>
    </tr>
    <tr>
      <td>진행 방식</td>
      <td>1주차 프로젝트를 새로 만들지 않고 이어서 발전시키기</td>
    </tr>
    <tr>
      <td>필수 제출물</td>
      <td>구현 코드, README</td>
    </tr>
    <tr>
      <td>제출 기한</td>
      <td>2026.10.16 23:59까지</td>
    </tr>
  </tbody>
</table>

---

## 2. 과제 배경

### Before

1주차 과제에서는 게시글을 메모리에 저장했습니다.

```text
Controller
    ↓
Service
    ↓
List / Map
```

이 방식은 간단하게 CRUD 흐름을 이해하기에는 좋지만, 서버를 종료하면 저장된 데이터가 사라집니다.

### After

2주차 과제에서는 저장 방식을 DB로 변경합니다.

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

즉, 기존 CRUD 기능은 유지하면서 **데이터 저장 방식과 프로젝트 구조를 개선하는 것**이 핵심입니다.

---

## 3. 필수 구현

다음 내용을 필수로 구현합니다.

<table>
  <thead>
    <tr>
      <th>구현 항목</th>
      <th>설명</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>DB 연결</td>
      <td>게시글 데이터를 저장할 Database를 연결합니다.</td>
    </tr>
    <tr>
      <td>Post Entity 작성</td>
      <td>JPA에서 관리할 게시글 Entity를 작성합니다.</td>
    </tr>
    <tr>
      <td>Spring Data JPA 적용</td>
      <td>JPA를 이용해 DB에 데이터를 저장하고 조회합니다.</td>
    </tr>
    <tr>
      <td>Repository 작성</td>
      <td>Spring Data JPA Repository를 이용해 데이터 접근 계층을 구성합니다.</td>
    </tr>
    <tr>
      <td>게시글 등록</td>
      <td>새로운 게시글을 DB에 저장합니다.</td>
    </tr>
    <tr>
      <td>전체 조회</td>
      <td>DB에 저장된 모든 게시글을 조회합니다.</td>
    </tr>
    <tr>
      <td>단건 조회</td>
      <td>특정 id에 해당하는 게시글을 조회합니다.</td>
    </tr>
    <tr>
      <td>게시글 수정</td>
      <td>기존 게시글의 제목과 내용을 수정합니다.</td>
    </tr>
    <tr>
      <td>게시글 삭제</td>
      <td>기존 게시글을 삭제합니다.</td>
    </tr>
    <tr>
      <td>존재하지 않는 데이터 처리</td>
      <td>없는 id로 조회, 수정, 삭제 요청이 들어왔을 때 적절히 처리합니다.</td>
    </tr>
  </tbody>
</table>

---

## 4. 직접 고민해볼 것

구현하면서 다음 질문들을 함께 고민해보세요.

- Entity와 DTO는 어떤 차이가 있을까?
- Entity와 DTO를 분리해야 할까?
- Entity를 Controller에서 그대로 반환해도 될까?
- 존재하지 않는 데이터는 어떻게 처리할까?
- Repository에서 조회한 데이터는 Service에서 어떻게 처리할까?
- 수정 기능은 어떻게 구현하는 것이 좋을까?
- 예외 처리는 어느 계층에서 담당하는 것이 좋을까?
- DB를 사용하면 1주차의 메모리 저장 방식과 무엇이 달라질까?

---

## 5. 선택 Challenge

필수 구현을 완료했다면 아래 내용 중 관심 있는 것을 선택해 추가로 구현해볼 수 있습니다.

- `@Valid`를 이용한 Validation
- `@RestControllerAdvice`
- Custom Exception
- API 테스트
- Pagination
- 검색 기능
- Swagger
- Docker를 이용한 DB 실행

> Challenge는 모두 구현할 필요 없습니다.  
> 기본 CRUD 기능을 DB/JPA 기반으로 정상 동작하게 만드는 것을 우선으로 진행해주세요.

---

## 6. 동작 확인

Postman, IntelliJ HTTP Client 등 원하는 도구를 이용하여 다음 흐름이 정상적으로 동작하는지 확인합니다.

```text
게시글 생성
    ↓
전체 조회
    ↓
단건 조회
    ↓
게시글 수정
    ↓
게시글 삭제
```

추가로 아래 상황도 확인해보세요.

- 존재하지 않는 id로 단건 조회
- 존재하지 않는 id로 수정
- 존재하지 않는 id로 삭제
- 서버를 재시작한 뒤에도 DB에 저장된 데이터가 유지되는지 확인

---

## 7. README 필수 내용

제출 Repository의 README에는 다음 내용을 포함해주세요.

- 사용한 언어 / Java / Spring Boot 버전
- 사용한 DB 종류
- 프로젝트 실행 방법
- DB 실행 또는 연결 방법
- 구현한 API와 기능
- 진행한 Challenge (있는 경우)
- 구현하면서 고민했거나 어려웠던 점

---

## 8. 참고

과제를 완성하지 못해도 괜찮습니다.  
구현 중 막힌 부분이 있다면 현재까지 작성한 코드로 제출해도 됩니다.

이번 과제에서는 단순히 JPA 코드를 따라 치는 것보다,

**기존 메모리 저장 방식 → DB 저장 방식으로 바꾸는 과정에서 구조가 어떻게 달라지는지 이해하는 것**

을 중요하게 생각합니다.

궁금하거나 구현 중 막히는 부분은 자유롭게 질문해주세요!

</details>
</li>
</ul>

</details>

</li>
</ul>

</details>
