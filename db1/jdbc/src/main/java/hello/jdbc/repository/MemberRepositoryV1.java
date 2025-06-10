package hello.jdbc.repository;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DataSource 사용, JDBCUtils 사용
 */
@Slf4j
public class MemberRepositoryV1 {

    public static DataSource dataSource;

    public MemberRepositoryV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }

    private static Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        log.info("getConnection : {}, class = {}", connection, connection.getClass());
        return connection;
    }

}
