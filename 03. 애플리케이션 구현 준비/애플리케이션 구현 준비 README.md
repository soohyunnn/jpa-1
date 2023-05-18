# 애플리케이션 구현 준비

## 구현 요구사항

![Untitled](https://github.com/soohyunnn/jpa-1/assets/58289675/e7bb9642-11e7-4b13-a9a7-aa5093f84585)

- 회원 기능
    - 회원 목록
    - 회원 조회
- 상품 기능
    - 상품 등록
    - 상품 수정
    - 상품 조회
- 주문 기능
    - 상품 주문
    - 주문 내역 조회
    - 주문 취소

**예제를 단순화 하기 위해 다음 기능은 구현 X**

- 로그인과 권한 관리 X
- 파라미터 검증과 예외 처리 X
- 상품은 도서만 사용
- 카테고리는 사용X
- 배송 정보는 사용X

## 애플리케이션 아키텍처

![Untitled 1](https://github.com/soohyunnn/jpa-1/assets/58289675/6b935ad2-8b1e-438b-a176-536a51718ff7)

**계층형 구조 사용**

- controller, web: 웹 계층
- service: 비즈니스 로직, 트랜잭션 처리
- repository: JPA를 직접 사용하는 계층, 엔티티 매니저 사용
- domain: 엔티티가 모여 있는 계층, 모든 계층에서 사용

**패키지 구조**

- jpabook.jpashop
    - domain
    - exception
    - repository
    - service
    - web

개발 순서 👉 서비스, 리포지토리 계층을 개발하고, 테스트 케이스를 작성해서 검증, 마지막에 웹 계층 적용