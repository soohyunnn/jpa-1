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