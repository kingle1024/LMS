# LMS
[설명]
- 관리자가 강의를 오픈하거나 삭제할 수 있는 백오피스 위주의 프로젝트입니다.
고객은 오픈된 강의를 신청할 수 있습니다. 

[구현 기능]
- 회원가입
- 로그인, 로그인 히스토리 저장
- 비밀번호 찾기 시, 초기화 메일 발송
- 회원 정보 변경, 회원 탈퇴
- 강의 추가, 읽기, 수정, 삭제
- 강의 카테고리 추가, 읽기, 수정, 삭제

[구현 상세]
- Spring Security WebSecurityConfigurerAdapter
  - 권한 처리, 로그인, 예외 처리
  - authorizeRequests(), formLogin(), exceptionHandling()
- MimeMessagePreparator
  - 메일 발송
- Builder Pattern
  - 객체 생성 전 데이터 정의를 위해 사용
- ResponseEntity
  - Body, Header, Status를 담아 정보를 전달하기 위해 사용
- Paging
  - 페이지 정보를 계산하기 위해 PagingUtil class 운영
- JPA
  - 간편 쿼리는 ORM 사용
- MyBatis
  - 복합 쿼리를 처리하기 위해 사용

[기술 스택]
- Spring Boot, JPA, MyBatis, Maven
