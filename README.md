
# mind-diary 서버 구조도
![서버 아키텍쳐](https://user-images.githubusercontent.com/29730565/147021123-58b873b7-b901-41d9-b03f-f91993e25123.png)


## 프로젝트 목표
* 스트레스를 관리할 수 있는 앱 서비스를 구현해 내는 것이 목표입니다.
* 대표 기능으로는 기분 일기, 커뮤니티, 자가진단 검사 기능이 있습니다.
* 단순 기능 구현뿐 아니라 대용량 트래픽 처리를 고려한 서비스를 구현하는 것이 목표입니다.
* 객체지향 원리와 Ioc/DI, AOP, 전략패턴 등을 적용함으로써 더 나은 코드를 작성하는 것이 목표입니다.
* REST API형 서버로써 클라이언트는 프로토타입으로만 제작하여 백엔드 공부에 집중했습니다.

## 기술적 issue 해결 과정

* [MindDiary 이슈 1. 회원가입 시 중복 정보일 때 응답 status code](https://velog.io/@meme2367/MindDiary-%EC%9D%B4%EC%8A%88-1-%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85-%EC%8B%9C-%EC%A4%91%EB%B3%B5-%EC%A0%95%EB%B3%B4%EC%9D%BC-%EB%95%8C-%EC%9D%91%EB%8B%B5-status-code)


* [MindDiary 이슈 2. Controller 계층에서 대부분의 비즈니스 로직이 이루어지는 문제](https://velog.io/@meme2367/MindDiary-%EC%9D%B4%EC%8A%88-2-Controller-%EA%B3%84%EC%B8%B5%EC%97%90%EC%84%9C-%EB%8C%80%EB%B6%80%EB%B6%84%EC%9D%98-%EB%B9%84%EC%A6%88%EB%8B%88%EC%8A%A4-%EB%A1%9C%EC%A7%81%EC%9D%B4-%EC%9D%B4%EB%A3%A8%EC%96%B4%EC%A7%80%EB%8A%94-%EB%AC%B8%EC%A0%9C)

* [MindDiary 이슈 3. 예외처리 전략](https://velog.io/@meme2367/MindDiary-%EC%9D%B4%EC%8A%88-3.-%EC%98%88%EC%99%B8%EC%B2%98%EB%A6%AC-%EC%A0%84%EB%9E%B5)

* [MindDiary 이슈 4. 반복되는 사용자 권한 확인을 AOP로 리팩토링하기](https://velog.io/@meme2367/MindDiary-%EC%9D%B4%EC%8A%88-4.-%EB%B0%98%EB%B3%B5%EB%90%98%EB%8A%94-%EC%82%AC%EC%9A%A9%EC%9E%90-%EA%B6%8C%ED%95%9C-%ED%99%95%EC%9D%B8%EC%9D%84-AOP%EB%A1%9C-%EB%A6%AC%ED%8C%A9%ED%86%A0%EB%A7%81%ED%95%98%EA%B8%B0)

* [MindDiary 이슈 5. 회원가입 시 이메일 전송에 실패했을 경우 트랜잭션 롤백 후처리하기](https://velog.io/@meme2367/MindDiary-%EC%9D%B4%EC%8A%88-5.-%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85-%EC%8B%9C-%EC%9D%B4%EB%A9%94%EC%9D%BC-%EC%A0%84%EC%86%A1%EC%97%90-%EC%8B%A4%ED%8C%A8%ED%96%88%EC%9D%84-%EA%B2%BD%EC%9A%B0-%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98-%EB%A1%A4%EB%B0%B1-%ED%9B%84%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0)

* [MindDiary 이슈 6. 게시글 목록 조회 시 일대다 관계의 컬렉션 데이터를 가져오는 문제](https://velog.io/@meme2367/MindDiary-%EC%9D%B4%EC%8A%88-6.-%EA%B2%8C%EC%8B%9C%EA%B8%80-%EB%AA%A9%EB%A1%9D-%EC%A1%B0%ED%9A%8C-%EC%8B%9C-%EC%9D%BC%EB%8C%80%EB%8B%A4-%EA%B4%80%EA%B3%84%EC%9D%98-%EC%BB%AC%EB%A0%89%EC%85%98-%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%A5%BC-%EA%B0%80%EC%A0%B8%EC%98%A4%EB%8A%94-%EB%AC%B8%EC%A0%9C)

* [ MindDiary  이슈 7. Redis에 여러 개의 데이터 추가 시 네트워크 문제 해결하기 : Redis PipeLining](https://velog.io/@meme2367/MindDiary-%EC%9D%B4%EC%8A%88-7.-Redis%EC%97%90-%EC%97%AC%EB%9F%AC-%EA%B0%9C%EC%9D%98-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%B6%94%EA%B0%80-%EC%8B%9C-%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0-Redis-PipeLining)

* [ MindDiary  이슈 8. 자가진단 조회 시 Look aside 캐시 구조 적용](https://velog.io/@meme2367/MindDiary-%EC%9D%B4%EC%8A%88-8.-%EC%9E%90%EA%B0%80%EC%A7%84%EB%8B%A8-%EA%B4%80%EB%A0%A8-Cache-miss-%ED%95%B4%EA%B2%B0%EC%9D%84-%EC%9C%84%ED%95%B4-Look-aside-%EA%B5%AC%EC%A1%B0-%EC%A0%81%EC%9A%A9)

* [ MindDiary  이슈 9. 자가진단 목록 조회 쿼리 최적화 : 인덱스](https://velog.io/@meme2367/MindDiary-%EC%9D%B4%EC%8A%88-9.-%ED%8E%98%EC%9D%B4%EC%A7%80-%EC%B2%98%EB%A6%AC%EC%8B%9C-ORDER-BY-..-LIMIT-%EC%BF%BC%EB%A6%AC-%EC%B5%9C%EC%A0%81%ED%99%94)

* [ MindDiary  이슈 10. 다중 서버에서 인증 정보는 어디에 저장할까 : 토큰 인증 방식](https://velog.io/@meme2367/MindDiary-%EC%9D%B4%EC%8A%88-10.-%EB%8B%A4%EC%A4%91-%EC%84%9C%EB%B2%84%EC%97%90%EC%84%9C-%EC%9D%B8%EC%A6%9D-%EC%A0%95%EB%B3%B4%EB%8A%94-%EC%96%B4%EB%94%94%EC%97%90-%EC%A0%80%EC%9E%A5%ED%95%A0%EA%B9%8C-JWT-%ED%86%A0%ED%81%B0)


## 프로젝트 중점사항
* 서버의 확장성
* 의존적이지 않은 단위테스트 작성
* 다중 서버 환경에서 JWT 토큰과 쿠키를 활용한 인증 구현
* 로그인을 했는지 확인하는 부가기능 반복 문제에 AOP를 적용하여 문제 해결하기
* Redis Cache를 이용해 자가진단 조회 기능 구현
* ControllerAdvice, ExceptionHandler를 사용하여 비즈니스 예외 처리 전략
* 스프링의 @Transactional을 이용하여 작업의 완전성 보장하기
* Redis 트랜잭션에는 롤백이 없기 때문에 직접 트랜잭션 롤백 후처리하기
* Jenkins를 사용하여 CI/CD 환경 구축
* 게시글 목록 조회 시 중복된 일대다 데이터를 가져오는 문제를 메서드 분리와 쿼리 튜닝으로 해결하기
* Redis Pipeline을 이용하여 한번에 많은 데이터 추가 시 네트워크 병목 개선
* Redis에 Look Aside 캐시 구조를 적용하여 가용성 확보하기
* Mysql에서 인덱스 설정과 실행계획 분석 후 쿼리 최적화
* naver Cloud Platform을 이용하여 로드밸런싱

* Mysql Replication - Master/Slave 데이터 이중화 적용(미완성)
* Ngrinder를 이용하여 connection pool 테스트(미완성)



## DB ERD


## API Docs


## Front

