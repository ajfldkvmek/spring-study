package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
 import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepository = new MemberRepositoryV0();

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