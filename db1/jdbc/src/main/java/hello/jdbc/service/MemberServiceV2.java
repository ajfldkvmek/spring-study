package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.beans.Transient;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *  트랜잭션 - 파라메터 연고으 풀을 고려한 종료
 *  프레임워크를 사용하면 이렇게 안해도 된다!
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    private void logic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);

        memberRepository.update(toId, toMember.getMoney() + money);
    }

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection con = dataSource.getConnection();

        try{
            con.setAutoCommit(false); //트랜잭션 시작
            //비즈니스 로직 수행
            logic(con, fromId, toId, money);
            // 성공시 커밋
            con.commit();

        } catch (Exception e) {
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            release(con);
        }

    }


    private static void release(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true); // 커넥션 풀 고려하여 auto commit 변경
                con.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }

    private static void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 에러 발생");
        }
    }

}
