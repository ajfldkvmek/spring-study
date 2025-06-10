package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionConst.*;
import static hello.jdbc.repository.MemberRepositoryV1.dataSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 memberRepository;

    @BeforeEach
    void beforeEach() {
        // 기본 driverManager - 항상 새로운 커넥션 획득
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        
        // ConnectionPooling- Hikari
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        memberRepository = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException {
        Member member = new Member("memberV99", 10000);
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getMemberId());
//        log.info("findMember = {}", findMember);
        assertThat(findMember).isEqualTo(member);

        // update money -> 20000
        memberRepository.update(member.getMemberId(),20000);
        Member updateMember = memberRepository.findById(member.getMemberId());
        assertThat(updateMember.getMoney()).isEqualTo(20000);

        //delete
        memberRepository.delete(member.getMemberId());
        // 삭제후 조회 ( 없는 데이터 이므로 NoSuchElementException 발생 하는지 체크)
        assertThatThrownBy(() -> memberRepository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);


    }

}