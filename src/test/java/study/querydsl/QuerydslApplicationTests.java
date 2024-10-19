package study.querydsl;

import static org.assertj.core.api.Assertions.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import study.querydsl.entity.Hello;
import study.querydsl.entity.QHello;

@SpringBootTest
@Transactional
@EnableWebMvc
@Commit // 테스트 롤백하지 않도록 설정
public class QuerydslApplicationTests {

    @Autowired
    EntityManager em;

    @Test
    void contextLoads() {
        Hello hello = new Hello();
        em.persist(hello);

        JPAQueryFactory query = new JPAQueryFactory(em);

        // 쿼리와 관련된 건 모두 Q 타입으로 정의하고, DB에 넣기
        QHello qHello = QHello.hello;
        Hello result = query
            .selectFrom(qHello)
            .fetchOne();

        assertThat(result).isEqualTo(hello);
        assertThat(result.getId()).isEqualTo(hello.getId());
    }
}
