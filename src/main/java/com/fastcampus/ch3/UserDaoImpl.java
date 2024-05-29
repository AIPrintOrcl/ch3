package com.fastcampus.ch3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

//@Component - @Controller, @Repository, @Service, @ControllerAdvice : 어노테이션 정의에 @Component가 붙어있어 자동 스캔이 가능하다.
@Repository
public class UserDaoImpl implements UserDao { // 수정 필요할 때 UserDao 수정X -> UserDaoImpl 수정 => 인터페이스를 생성해서 변경에 유리하도록 한다.
    @Autowired
    DataSource ds;

    final int FAIL = 0;

    @Override
    public int deleteUser(String id) {
        int rowCnt = FAIL; //  insert, delete, update

        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "delete from user_info where id= ? ";

        try {
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
//        int rowCnt = pstmt.executeUpdate(); //  insert, delete, update
//        return rowCnt;
            return pstmt.executeUpdate(); //  insert, delete, update
        } catch (SQLException e) {
            e.printStackTrace();
            return FAIL;
        } finally {
            // close()를 호출하다가 예외가 발생할 수 있으므로, try-catch로 감싸야함.
//            try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace();}
//            try { if(conn!=null)  conn.close();  } catch (SQLException e) { e.printStackTrace();}
            close(pstmt, conn); //     private void close(AutoCloseable... acs) {
        }
    }

    @Override
    public User selectUser(String id) {
        User user = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "select * from user_info where id= ? ";

        try {
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(sql); // SQL Injection공격, 성능향상
            pstmt.setString(1, id);

            rs = pstmt.executeQuery(); //  select

            if (rs.next()) {
                user = new User();
                user.setId(rs.getString(1));
                user.setPwd(rs.getString(2));
                user.setName(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setBirth(new Date(rs.getDate(5).getTime()));
                user.setSns(rs.getString(6));
                user.setReg_date(new Date(rs.getTimestamp(7).getTime()));
            }
        } catch (SQLException e) {
            return null;
        } finally {
            // ResultSet, PreparedStatement, Connection를 close해야한다(자원반환). 메모리가 쌓여 반환이 되지 않아 메모리가 부족할 수 있다.(메모리 누수)
            // close()를 호출하다가 예외가 발생할 수 있으므로, try-catch로 감싸야함.
            // close()의 호출순서는 생성된 순서의 역순(ResultSet->PreparedStatement->Connection)
//            try { if(rs!=null)    rs.close();    } catch (SQLException e) { e.printStackTrace();}
//            try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace();}
//            try { if(conn!=null)  conn.close();  } catch (SQLException e) { e.printStackTrace();}
            close(rs, pstmt, conn);  //     private void close(AutoCloseable... acs) {
        }

        return user;
    }

    // 사용자 정보를 user_info테이블에 저장하는 메서드
    @Override
    public int insertUser(User user) {
        int rowCnt = FAIL;

        Connection conn = null;
        PreparedStatement pstmt = null;

//        insert into user_info (id, pwd, name, email, birth, sns, reg_date)
//        values ('asdf22', '1234', 'smith', 'aaa@aaa.com', '2022-01-01', 'facebook', now());
        String sql = "insert into user_info values (?, ?, ?, ?,?,?, now()) ";

        try {
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(sql); // SQL Injection공격, 성능향상
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getPwd());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
            pstmt.setDate(5, new java.sql.Date(user.getBirth().getTime()));
            pstmt.setString(6, user.getSns());

            return pstmt.executeUpdate(); //  insert, delete, update;
        } catch (SQLException e) {
            e.printStackTrace();
            return FAIL;
        } finally {
            close(pstmt, conn);  //     private void close(AutoCloseable... acs) {
        }
    }

    // 매개변수로 받은 사용자 정보로 user_info테이블을 update하는 메서드
    @Override
    public int updateUser(User user) {
        int rowCnt = FAIL; //  insert, delete, update

//        Connection conn = null;
//        PreparedStatement pstmt = null;

        String sql = "update user_info " +
                "set pwd = ?, name=?, email=?, birth =?, sns=?, reg_date=? " +
                "where id = ? ";

        // try-with-resources - since jdk7
        try (
                // try-with-resources : AutoCloseable(ResultSet, PreparedStatement, Connection)에 해당하는 객체만 자동으로 close() 됨
                Connection conn = ds.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql); // SQL Injection공격, 성능향상
        ){
            pstmt.setString(1, user.getPwd());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setDate(4, new java.sql.Date(user.getBirth().getTime()));
            pstmt.setString(5, user.getSns());
            pstmt.setTimestamp(6, new java.sql.Timestamp(user.getReg_date().getTime()));
            pstmt.setString(7, user.getId());

            rowCnt = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return FAIL;
        }

        return rowCnt;
    }

    public void deleteAll() {
        try {
            Connection conn = ds.getConnection();

            String sql = "delete from user_info ";

            PreparedStatement pstmt = conn.prepareStatement(sql); // SQL Injection공격, 성능향상
            pstmt.executeUpdate(); //  insert, delete, update
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void close(AutoCloseable... acs) {
        for(AutoCloseable ac :acs)
            try { if(ac!=null) ac.close(); } catch(Exception e) { e.printStackTrace(); }
    }
}
