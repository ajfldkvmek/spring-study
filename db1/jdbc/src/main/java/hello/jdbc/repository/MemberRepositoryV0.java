package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DriverManager 사용
 * <p>
 * 최근에는 이렇게 직접 JDBC 를 연결을 직접 작성할 일은 거의 없다.
 */

/**
 *  prepareStatement 방식을 사용하면 SQLInjection 공격을 예방할 수 있다.
 *  prepareStatement 은 파라메터 바인딩 방식으로 처리
 *  ex)
 *  ' -- ' 와 같은 방식으로 조건문을 주석처리하기위한 인젝션 공격을 하면
 *  '--' 라는 문자열로 인식하여 뒤에 문장이 주석처리 되지 않음
 */

@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member (member_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            // 인덱스로 데이터 바인딩
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            //쿼리 실행
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            // 역 순으로 close
            // close 시에도 예외가 발생하면 처리가 되지 않는다. -> 따로 예외처리 필요
            close(con, pstmt, null);
        }

    }


    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found member id : " + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }
        return null;
    }


    public void update(String memberId, int money) throws SQLException {

        String sql = "update member set money = ? where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            //쿼리 실행
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize : {}",  resultSize);

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            // 역 순으로 close
            // close 시에도 예외가 발생하면 처리가 되지 않는다. -> 따로 예외처리 필요
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId) throws SQLException {

        String sql = "delete from member where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            //쿼리 실행
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize : {}",  resultSize);

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            close(con, pstmt, null);
        }

    }




    private void close(Connection con, Statement stmt, ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("error", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }


    private static Connection getConnection() {
        return DBConnectUtil.getConnection();
    }

}
