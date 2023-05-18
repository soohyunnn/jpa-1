# 프로젝트 환경설정

## 프로젝트 생성

```html
Project: Gradle - Groovy Project
Language: Java
Spring Boot: 3.0.x

Group: jpabook 
Artifact: jpashop
Name: jpashop
Package name: jpabook.jpashop
Packing: **Jar(주의!)**
Java: 17

Dependencies: web, thymeleaf, jpa, h2, lombok, validation
```

⚠️ **주의! - 스프링 부트 3.0**

스프링 부트 3.0을 선택하게 되면 다음 부분을 꼭 확인해야한다.

1. **Java 17** 이상을 사용해야 한다.
2. **javax 패키지 이름을 jakarta로 변경**해야 한다.
    - 오라클과 자바 라이센스 문제로 모든 javax 패키지를 jakarta로 변경하기로 했다.
3. **H2 데이터베이스를 2.1.214 버전 이상 사용해주세요.**

**패키지 이름 변경 예)**

- **JPA 어노테이션**
    - javax.persistence.Entity → jakarta.persistence.Entity
- **스프링에서 자주 사용하는 @PostConstuct 어노테이션**
    - javax.annotation.PostConstruct → jakarta.annotation.PostConstruct
- **스프링에서 자주 사용하는 검증 어노테이션**
    - javax.validation → jakarta.validation

스프링 부트 3.0 관련 자세한 내용은 다음 링크를 확인해주세요 : [`https://bit.ly/springboot3`](https://bit.ly/springboot3)

- 동작 확인
    - 기본 테스트 케이스 실행
    - 스프링 부트 메인 실행 후 에러페이지로 간단하게 동작 확인(`[`http://localhost:8080`](http://localhost:8080)')

**롬복 적용**

1. Preferences → plugin → lombok 검색 실행 (재시작)
2. Preferences → Annotation Processors 검색 → Enable annotation processing 체크 (재시작)
3. 임의의 테스트 클래스를 만들고 @Getter, @Setter 확인

**IntelliJ Gradle 대신에 자바 직접 실행**

최근 IntelliJ 버전은 Gradle로 실행을 하는 것이 기본 설정이다. 이렇게 하면 실행속도가 느리다. 다음과 같이 변경하면 자바로 바로 실행해서 실행 속도가 더 빠르다.

- Preferences → Build, Execution, Deployment → Build Tools → Gradle
- Build and run using : Gradle → IntelliJ IDEA
- Run tests using : Gradle → IntelliJ IDEA

## 라이브러리 살펴보기

### gradle 의존관계 보기

`./gradlew dependencies -configuration compileClasspath`

### 스프링 부트 라이브러리 살펴보기

- spring-boot-starter-web
    - spring-boot-starter-tomcat: 톰캣 (웹서버)
    - spring-webmvc : 스프링 웹 MVC
- spring-boot-starter-thymeleaf: 타임리프 템플릿 엔진(View)
- spring-boot-starter-data-jpa
    - spring-boot-starter-aop
    - spring-boot-starter-jdbc
        - HikariCP 커넥션 풀 (부트 2.0 기본)
    - hibernate + JPA: 하이버네이트 + JPA
    - spring-data-jpa: 스프링 데이터 JPA
- spring-boot-starter(공통): 스프링 부트 + 스프링 코어 + 로깅
    - spring-boot
        - spring-core
    - spring-boot-starter-logging
        - logback, slf4j

### 테스트 라이브러리

- spring-boot-starter-test
    - junit: 테스트 프레임워크
    - mockito: 목 라이브러리
    - assertj: 테스트 코드를 좀 더 편하게 작성하게 도와주는 라이브러리
    - spring-test: 스프링 통합 테스트 지원

- 핵심 라이브러리
    - 스프링 MVC
    - 스프링 ORM
    - JPA, 하이버네이트
    - 스프링 데이터 JPA
- 기타 라이브러리
    - H2 데이터베이스 클라이언트
    - 커넥션 풀: 부트 기본은 HikariCP
    - WEB(thymeleaf)
    - 로깅 SLF4J & LogBack
    - 테스트

❣️ 참고: 스프링 데이터 JPA는 스프링과 JPA를 먼저 이해하고 사용해야 하는 응용기술이다.

## View 환경 설정

**thymeleaf 템플릿 엔진**

- thymeleaf 공식 사이트: `[https://www.thymeleaf.org/](https://www.thymeleaf.org/)`
- 스프링 공식 튜토리얼: [`https://spring.io/guides/gs/serving-web-content/`](https://spring.io/guides/gs/serving-web-content/)
- 스프링부트 메뉴얼: [`https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-developing-web-applications.html#boot-features-spring-mvc-template-engines`](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-developing-web-applications.html#boot-features-spring-mvc-template-engines)

- 스프링 부트 thymeleaf viewName 매핑
    - `resources: templates/` +{ViewName} + `.html`

**jpabook.jpashop.HelloController**

```java
package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!");
        return "hello";
    }

}
```

**thymeleaf 템플릿엔진 동작 확인(hello.html)**

`resources/templates/hello.html`

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Hello</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<p th:text="'안녕하세요. ' + ${data}" >안녕하세요. 손님</p>
</html>
```

`static/index.html`

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <title>Hello</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
Hello
<a href="/hello">hello</a>
</body>
</html>
```

> `spring-boot-devtools` 라이브러리를 추가하면, `html` 파일을 컴파일만 해주면 서버 재시작 없이 View 파일 변경이 가능하다.
인텔리J 컴파일 방법: 메뉴 build → Recompile
> 

## H2 데이터베이스 설치

개발이나 테스트 용도로 가볍고 편리한 DB, 웹 화면 제공

> ⚠️ Version 1.4.200를 사용해주세요.
1.4.200 버전 다운로드 링크
> 

윈도우 설치 버전: [https://h2database.com/h2-setup-2019-10-14.exe](https://h2database.com/h2-setup-2019-10-14.exe) 

윈도우, 맥, 리눅스 실행 버전: [https://h2database.com/h2-2019-10-14.zip](https://h2database.com/h2-2019-10-14.zip)

- [`https://www.h2database.com`](https://www.h2database.com/)
- 다운로드 및 설치
- 데이터베이스 파일 생성 방법
    - `jdbc:h2:~/jpashop` (최소 한번)
    - `~/jpashop.mv.db` 파일 생성 확인
    - 이후 부터는 `jdbc:h2:tcp://localhost/~/jpashop` 이렇게 접속

⚠️ H2 데이터베이스의 MVCC 옵션은 H2 1.4.198 버전부터 제거되었다. 1.4.200 버전에서는 MVCC 옵션을 사용하면 오류가 발생한다.

## JPA와 DB 설정, 동작확인

`main/resources/application.properties`

```
spring.datasource.url=jdbc:h2:tcp://localhost/~/jpashop
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create
#spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=debug

# org.hibernate.type: trace #스프링 부트 2.x, hibernate5

#스프링 부트 3.x, hibernate6
org.hibernate.orm.jdbc.bind: trace
```

- `spring.jpa.hibernate.ddl-auto: create`
    - 이 옵션은 애플리케이션 실행 시점에 테이블을 drop 하고, 다시 생성한다.
    

> 참고: 모든 로그 출력은 가급적 로거를 통해 남겨야 한다.
`show_sql`: 옵션은 `System.out`에 하이버네이트 실행 SQL을 남긴다.
`org.hibernate.SQL`: 옵션은 logger를 통해 하이버네이트 실행 SQL을 남긴다.
> 

### **실제 동작하는지 확인하기**

**회원 엔티티**

```java
package jpabook.jpashop;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

}
```

**회원 리포지토리**

```java
package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    @PersistenceContext
    EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

}
```

**테스트**

```java
package jpabook.jpashop;

import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testMember() {
        Member member = new Member();
        member.setUsername("memberA");
        Long saveId = memberRepository.save(member);

        Member findMember = memberRepository.find(saveId);

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);

    }

}
```

> 오류: 테스트를 실행했는데 다음과 같이 테스트를 찾을 수 없는 오류가 발생하는 경우
> 
> 
> ```java
> No tests found for given includes: [jpabook.jpashop.MemberRepositoryTest]
> (filter.includeTestsMatching)
> ```
> 
> 해결: 스프링 부트 2.1.x 버전을 사용하지 않고, 2.2.x 이상 버전을 사용하면 Junit5가 설치된다. 이때는
> `build.gradle` 마지막에 다음 내용을 추가하면 테스트를 인식할 수 있다. Junit5 부터는 `build.gradle`
> 에 다음 내용을 추가해야 테스트가 인식된다.
> 
> `build.gradle` 마지막에 추가
> 
> ```java
> test {
>     useJUnitPlatform()
> }
> ```
> 

### 쿼리 파라미터 로그 남기기

- 로그에 다음을 추가하기: SQL 실행 파라미터를 로그로 남긴다.
- ⚠️ 스프링 부트 3.x를 사용한다면 다음 내용을 참고하자.
    - 스프링 부트 2.x, hibernate5
        - org.hibernate.type: trace
    - 스프링 부트 3.x, hibernate6
        - org.hibernate.orm.jdbc.bind: trace
- 외부 라이브러리 사용
    - https://github.com/gavlyukovskiy/spring-boot-data-source-decorator

스프링 부트를 사용하면 아래의 라이브러리만 추가하면 된다.

```java
implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6'
```

> 쿼리 파라미터를 로그로 남기는 외부 라이브러리는 시스템 자원을 사용하므로, 개발 단계에서는 편하게 사용해도 된다. 하지만 운영시스템에 적용하려면 꼭 성능테스트를 하고 사용하는 것이 좋다.
> 

### 쿼리 파라미터 로그 남기기 - 스프링 부트 3.0

`p6spy-spring-boot-starter` 라이브러리는 현재 스플이 부트 3.0을 정상 지원하지 않는다.

스프링 부트 3.0에서 사용하려면 다음과 같은 추가 설정이 필요하다.

1. **org.springframework.boot.autoconfigure.AutoConfiguration.imports 파일 추가**

    
    `src/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
    
    ```java
    com.github.gavlyukovskiy.boot.jdbc.decorator.DataSourceDecoratorAutoConfigurati
      on
    ```
    
    폴더명: `src/resources/META-INF/spring`
    
    파일명: `org.springframework.boot.autoconfigure.AutoConfiguration.imports`
    

1. **spy.properties 파일 추가**
    
    `src/resources/spy.properties`
    
    ```java
    appender=com.p6spy.engine.spy.appender.Slf4JLogger
    ```
    

👉이렇게 2개의 파일을 추가하면 정상 동작한다.