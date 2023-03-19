package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //스프링과 테스트 통합
@SpringBootTest // 스프링 부트 띄우고 테스트 (이게 없으면 @Autowired 다 실패)
@Transactional //테스트 케이스에서만 기본적으로 rollback이 true로 설정
public class MemberServiceTest {

    //테스트이니 다른데서 참조할게 없으니 필드주입으로~~
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
//    @Rollback(false) //DB에 넣어진게 보고싶을때
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(saveId));
        
    }
    @Test(expected = IllegalStateException.class) //예외가 감지돼면 return;
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2); // !!예외가 발생

//        try {
//            memberService.join(member2);
//        } catch (IllegalStateException e){
//            return;
//        }

        //then
        fail("예외가 발생해야 한다.");
        /*
        이 라인까지 진행이 되면 fail을 던져줘서 테스트가 실패하게됌
        즉, join(member2)에서 중복이 발생되었으므로 예외가 발생해야하는데
        예외가 발생되지 않으면 테스트가 실패한거!!
         */
        
    }
}