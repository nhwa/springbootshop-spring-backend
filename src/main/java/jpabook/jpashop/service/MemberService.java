package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //JPA는 무조건 트랜잭션안에서 실행되어야한다.
@RequiredArgsConstructor // final 있는 필드만 자동으로 생성자를 만들어줌
public class MemberService {

    // 필드 주입
    // 스프링이 스프링빈에 등록되어있는 memberRepository를 인젝션 해줌.
//    @Autowired
    private final MemberRepository memberRepository;

    // 생성자 주입 주로 사용
//    @Autowired // 생성자가 한개가 있으면 생략 가능
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //회원가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //동시에 로직이 실행되는 경우가 있으니 DB에 member.name을 유니크 제약조건을 걸어주는 것이 좋다 
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    // 한건만 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
