package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    /**
     * 스프링부트를 사용하니깐 entitymanagerfactory 만들고 em등록하는 과정 다 생략해줌!!
     * EntityManager를 빈으로 주입할 때 사용하는 어노테이션 (스프링이 동시성 문제가 발생안되게 보장)
     * 스프링이 entitymaner 만들어서 주입해줌
     *
     * @PersistenceContext 스프링 부트 쓰면 @RequiredArgsConstructor로 생략가능
     */

    private final EntityManager em;
    public void save(Member member){
        member.setCreatedAt(LocalDateTime.now());
        em.persist(member);
    }
    public Member findOne(Long id){
        return em.find(Member.class , id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name",name)
                    .getResultList();
    }
}
