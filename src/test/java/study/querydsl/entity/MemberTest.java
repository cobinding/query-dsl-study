package study.querydsl.entity;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    EntityManager em;

    @Test
    public void testEntity() {
        Team a = new Team("A");
        Team b = new Team("B");
        em.persist(a);
        em.persist(b);

        Member m1 = new Member("m1", 10, a);
        Member m2 = new Member("m2", 10, a);

        Member m3 = new Member("m3", 10, b);
        Member m4 = new Member("m4", 10, b);

        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.persist(m4);

        //초기화
        em.flush(); // 영속성 컨텍스트에 있는 객체 쿼리를 만들어서 DB에
        em.clear(); // 완전 초기화

        // JPQL 쿼리
        // Member.class: 결과로 반환될 타입 지정하는 거
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam() = " + member.getTeam());
        }


    }

}