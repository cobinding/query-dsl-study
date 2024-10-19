package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    @BeforeEach // 테스트 실행 전 데이터 세팅
    public void before()
    {
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
    }

    @Test
    public void startJPQL() {
        // JPQL로 먼저 써보기 !!
        String qlString = "select m from Member m where m.username= :username";

        // 1. member1을 찾아라
        Member findMember = em.createQuery(qlString, Member.class)
            .setParameter("username", "m1")
            .getSingleResult();

        Assertions.assertThat(findMember.getUsername()).isEqualTo("m1");
    }

    @Test
    public void startQuerydsl() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember m = new QMember("m");

        Member findMember = queryFactory
            .select(m)
            .from(m)
            .where(m.username.eq("m1"))
            .fetchOne();

        Assertions.assertThat(findMember.getUsername()).isEqualTo("m1");

    }
}
