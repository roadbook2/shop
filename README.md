# 1.변경사항
- 스프링부트3(2023-07-17) branch
  - 스프링부트 3.1.1 버전으로 업데이트
  - 스프링부트3 버전부터는 자바 17버전 이상을 사용해야합니다.
  - pom.xml 버전 정보가 많이 바뀌었습니다. 최신 pom.xml 버전을 참고해주세요
  - javax에서 jakarta로 변경됨에 따라서 많은 import 들이 jakarta로 수정되었습니다.
    - javax.validation => jakarta.validation
    - javax.persistence => jakarta.persistence
    - CustomAuthenticationEntryPoint.java (javax.servlet => jakarta.servlet)
    - Security 버전이 수정됨에 따라 기존 메소드가 deprecated 됐습니다. 최신 설정은 추후 올려두도록하겠습니다.
  - layout1.html 파일 내용 수정
    - thymeleaf layout 버전 증가에 따른 코드 수정 
    - th:replace="fragments/header::header">   =>   th:replace="~{fragments/header::header}">
    - th:replace="fragments/footer::footer">   =>   th:replace="~{fragments/footer::footer}">
- 2022-06-26 branch 
   - 스프링부트 2.7.1 버전으로 업데이트
   - querydsl 5.0.0 버전으로 pom.xml 업데이트 및 ItemRepositoryCustomImpl의 fetchResults() deprecated 대응
     (list 조회 및 count 조회 쿼리 분리)
   - WebSecurityConfigurerAdapter deprecated로 인한 SecurityConfig 파일 수정
   - thymeleaf-layout-dialect 3.1.0 버전으로 업데이트
   - modelmapper 3.1.0 버전으로 업데이트
  
# 2.안내사항
 - 스프링부트3로 진행 시 기존 소스코드와 import 등 코드가 많이 달라져서 스프링부트 2.7.1 버전 기준으로 진행을 권장드립니다.
 - 백견불여일타 스프링부트 with JPA 질의응답 게시판 공지사항을 보시면 자주 겪으시는 오류 사항들도 계속 업데이트 중입니다. 참고부탁드립니다. (https://cafe.naver.com/codefirst)
