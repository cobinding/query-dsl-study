package study.querydsl;

import static study.querydsl.entity.QMember.member;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    @Test // 테스트 실행 전 데이터 세팅
    @Commit
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
        // QMember m = new QMember("m");

        Member findMember = queryFactory
            .select(member)
            .from(member)
            .where(member.username.eq("m1"))
            .fetchOne();

        Assertions.assertThat(findMember.getUsername()).isEqualTo("m1");

    }

    @Test
    public void simpleProjection() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<String> result = queryFactory
            .select(member.username)
            .from(member)
            .fetch(); // 쿼리 실행 및 결과(List로) 가져오기


        // 얘도 프로젝션 대상이 하나인 것
        List<Member> result2 = queryFactory
            .select(member)
            .from(member)
            .fetch(); // 쿼리 실행 및 결과(List로) 가져오기
    }

    @Test
    public void tupleProjection() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        // select 여러 개 일 때,,
        // 실제로 soutv 찍으면 member table에 있는 모든 값(m1,m2,m3,m4) 가져올 수 있음


        /*
        * tuple은 Repository 계층에서 쓰는 건 괜찮은데, 그 이외 service 등에서는 X
        * JPA, QueryDsl같은 거 쓴다는 걸 서비스 로직에 드러내면 좋지 않다.
        * 그래서 Repository나 Dto 내에서만 드러내는 것이 좋은 설계 !
        *
        * */

        List<Tuple> tupleResult = queryFactory
            .select(member.username, member.age)
            .from(member)
            .fetch();

        for (Tuple tuple : tupleResult) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);

        }

    }
}
