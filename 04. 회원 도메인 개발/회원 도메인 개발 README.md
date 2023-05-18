# 회원 도메인 개발

## 회원 리포지토리 개발

**회원 리포지토리 코드**

```java
package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class).setParameter("name", name).getResultList();
    }

}
```

- `@Repository` : 스프링 빈으로 등록, JPA 예외를 스프링 기반 예외로 예외 변환
- `@PersistenceContext` : 엔티티 매니저(`EntityManager`) 주입
- `@PersistenceUnit` : 엔티티 매니저 팩토리(`EntityManagerFactory`) 주입

## 회원 서비스 개발

**회원 서비스 코드**

```java
package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplidateMember(member);  // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplidateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
```

- `@Service`
- `@Transactional` : 트랜잭션, 영속성 컨텍스트
    - `readOnly=true` : 데이터의 변경이 없는 읽기 전용 메서드에 사용, 영속성 컨텍스트를 플러시 하지 않으므로 약간의 성능 향상(읽기 전용에는 다 적용)
    - 데이터베이스 드라이버가 지원하면 DB에서 성능 향상
- `@Autowired`
    - 생성자 Injection 많이 사용, 생성자가 하나면 생략 가능

> 참고: 실무에서는 검증 로직이 있어도 멀티 쓰레드 상황을 고려해서 회원 테이블의 회원명 컬럼에 유니크 제약 조건을 추가하는 것이 안전하다.
> 

❣️ **스프링 필드 주입 대신 생성자 주입을 사용하자.**

필드 주입

```java
public class MemberService {
      @Autowired
      MemberRepository memberRepository;
      ...
}
```

**생성자 주입**

```java
public class MemberService {
     
	 private final MemberRepository memberRepository;
      
		public MemberService(MemberRepository memberRepository) {
          this.memberRepository = memberRepository;
		}
	... 
}
```

- 생성자 주입 방식을 권장
- 변경 불가능한 안전한 객체 생성 가능
- 생성자가 하나면, `@Autowired`를 생략할 수 있다.
- `final` 키워드를 추가하면 컴파일 시점에 `memberRepository`를 설정하지 않는 오류를 체크할 수 있다.(보통 기본 생성자를 추가할 때 발견)

**lombok**

```java
@RequiredArgsConstructor
public class MemberService {

		private final MemberRepository memberRepository;
		... 

}
```

> 참고: 스프링 데이터 JPA를 사용하면 `EntityManager`도 주입 가능
> 

```java
@Repository
@RequiredArgsConstructor
public class MemberRepository {
    
		private final EntityManager em;
		...
 
}
```

## 회원 기능 테스트

**테스트 요구사항**

- 회원가입을 성공해야 한다.
- 회원가입 할 때 같은 이름이 있으면 예외가 발생해야 한다.

**회원가입 테스트 코드**

```java
package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
//    @Rollback(value = false)  //-> 이 코드를 사용하면 log에 insert문이 보인다.
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("soo");

        //when
        Long saveId = memberService.join(member);

        //then
//        em.flush();  //영속성 컨텍스트에 있는 변경 내용을 DB에 등록 반영하는 것(Rollback 어노테이션이 아닌 이 코드를 적용해도 log에 INSERT 문이 보인다.)
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test()
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("soo1");
        Member member2 = new Member();
        member2.setName("soo1");

        //when
        memberService.join(member1);
//        try {
//            memberService.join(member2);  //예외가 발생해야 한다!!
//        } catch (IllegalStateException e) {
//            return;
//        }

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);  //예외가 발생해야 한다!!
        });
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());

        //then
//        fail("예외가 발생해야 한다.");

    }

}
```

- `@Runwith(SpringRunnber.class)` : 스프링과 테스트 통합
    
    → `@Junit4`에서만 필요! `@Junit5`는 필요 X
    
- `@SpringBootTest` : 스프링 부트 띄우고 테스트(이게 없으면 `@Autowired` 전부 실패)
- `@Transactional` : 반복 가능한 테스트 지원, 각각의 테스트를 실행할 때마다 트랜잭션을 시작하고 **테스트가 끝나면 트랜잭션을 강제로 롤백**(이 어노테이션이 테스트 케이스에서 사용될 때만 롤백)

> 참고 : 테스트 케이스 작성 고수 되는 마법 : Given, When, Then
([`http://martinfowler.com/bliki/GivenWhenThen.html`](http://martinfowler.com/bliki/GivenWhenThen.html))
이 방법이 필수는 아니지만 이 방법을 기본으로 해서 다양하게 응용하는 것을 권장한다.
> 

**테스트 케이스를 위한 설정**

테스트는 케이스 격리된 환경에서 실행하고, 끝나면 데이터를 초기화하는 것이 좋다. 그런 면에서 메모리 DB를 사용하는 것이 가장 이상적이다.

추가로 테스트 케이스를 위한 스프링 환경과, 일반적으로 애플리케이션을 실행하는 환경은 보통 다르므로 설정 파일을 다르게 사용하자.

다음과 같이 간단하게 테스트용 설정 파일을 추가하면 된다.

`test/resources/application/properties`

```java
#아래 설정을 주석처리해도 SpringBoot 자체에서 내장 메모리로 셋팅을 해줘서 정상적으로 메모리 DB 사용 가능
#spring.datasource.url=jdbc:h2:tcp://localhost/~/jpashop
#spring.datasource.username=sa
#spring.datasource.password=
#spring.datasource.driver-class-name=org.h2.Driver
#spring.jpa.hibernate.ddl-auto=create
#spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=debug

# org.hibernate.type: trace #??? ?? 2.x, hibernate5

#??? ?? 3.x, hibernate6
org.hibernate.orm.jdbc.bind: trace
```

이렇게 설정해주면 테스트에서 스프링을 실행하면 이 위치에 있는 설정 파일을 읽는다.

(만약 이 위치에 없으면 `src/resources/application.properties`를 읽는다.

스프링 부트는 datasource 설정이 없으면, 기본적으로 메모리 DB를 사용하고, driver-class도 현재 등록된 라이브러리를 보고 찾아준다. 추가로 `ddl-auto`도 `create-drop`모드로 동작한다. 따라서 데이터소스나, JPA 관련된 별도의 추가 설정을 하지 않아도 된다.