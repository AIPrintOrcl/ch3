package com.fastcampus.ch3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit4ClassRunner : ac를 자동으로 생성
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/**/root-context.xml") // @ContextConfiguration : xml 설정 파일 위치를 지정
public class DBConnectionTest2Test {

    //자동주입을 통해 빈을 가져와서 JDBC하기
    @Autowired
    DataSource ds;

    @Test
    public void insertUpdateTest() throws Exception {

        User user = new User("o2oo", "1444", "smith", "ooo@ooo.com", new Date(), "facebook", new Date());
        deleteAll(); // User 임포트 테이블에 있는 데이터를 모두 지우는 메서드.
        int rowCnt = insertUser(user);

        assertTrue(rowCnt==1);
    }

    @Test
    public void selectUserTest() throws Exception {
        deleteAll(); // User 임포트 테이블에 있는 데이터를 모두 지우는 메서드.
        User user1 = new User("o2oo", "1444", "smith", "ooo@ooo.com", new Date(), "facebook", new Date());
        int rowCnt = insertUser(user1);

        User user2 = selectUser("o2oo");

        assertTrue(user2.getId().equals("o2oo"));
    }

    @Test
    public void deleteUserTest() throws Exception {
        deleteAll(); // User 임포트 테이블에 있는 데이터를 모두 지우는 메서드.
        int rowCnt = deleteUser("o2oo");

        assertTrue(rowCnt==0);

        User user = new User("o2oo", "1444", "smith", "ooo@ooo.com", new Date(), "facebook", new Date());
        rowCnt = insertUser(user);
        assertTrue(rowCnt==1);

        rowCnt = deleteUser(user.getId());

        assertTrue(rowCnt==1);

        assertTrue(selectUser(user.getId())==null);
    }

    @Test
    public void updateUserTest() throws Exception {
        deleteAll(); // User 임포트 테이블에 있는 데이터를 모두 지우는 메서드.
        int rowCnt = deleteUser("o2oo");

        assertTrue(rowCnt==0);

        User user = new User("o2oo", "1444", "smith", "ooo@ooo.com", new Date(), "facebook", new Date());
        rowCnt = insertUser(user);
        assertTrue(rowCnt==1);

        user.setPwd("1999");
        user.setName("sam");
        user.setReg_date(new Date());

        rowCnt = updateUser(user);

        assertTrue(rowCnt==1);

        assertTrue(selectUser(user.getId())!=null);
    }

    // 사용자 정보를 user_info 테이블에 저장하는 메서드
    public int insertUser(User user) throws Exception {
        Connection conn = ds.getConnection();

//        insert into user_info (id, pwd, name, email, birth, sns, reg_date)
//        values ('sdss', '1444', 'smith', 'sss@sss.com', '2024-05-29', 'facebook', now());

        String sql = "insert into user_info values (?, ?, ?, ?, ?, ?, now())";

        PreparedStatement pstmt = conn.prepareStatement(sql); // SQL Injection공격, 성능향상
        //SQL Injection공격 : 다른 sql 문장들을 복잡하게 넣어서 sql문을 자기 마음대로 바꿀 수 있는 해커들의 공격 방법.
        //성능향상 : PreparedStatement 사용 시 sql문을 여러번 재사용 가능 => 일반 Statement보다 실행속도가 빠르다.
        pstmt.setString(1, user.getId());
        pstmt.setString(2, user.getPwd());
        pstmt.setString(3, user.getName());
        pstmt.setString(4, user.getEmail());
        pstmt.setDate(5, new java.sql.Date(user.getBirth().getTime()));
        pstmt.setString(6, user.getSns());

        int rowCnt = pstmt.executeUpdate(); // insert, delete, update 쿼리 실행한 행 수를 출력. insert는 성공 : 1 실패 : 0
        return rowCnt;
    }

    private void deleteAll() throws Exception {
        Connection conn = ds.getConnection();

        String sql = "delete from user_info";

        PreparedStatement pstmt = conn.prepareStatement(sql); // SQL Injection공격, 성능향상

        pstmt.executeUpdate(); // insert, delete, update 쿼리 실행한 행 수를 출력. insert는 성공 : 1 실패 : 0
    }

    public User selectUser(String id) throws Exception {
        Connection conn = ds.getConnection();

        String sql = "select * from user_info where id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql); // SQL Injection공격, 성능향상
        pstmt.setString(1, id);

        ResultSet rs = pstmt.executeQuery(); // select

        User user = new User();
        if(rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setBirth(new Date(rs.getDate("birth").getTime()));
            user.setSns(rs.getString("sns"));
            user.setReg_date(new Date(rs.getDate("reg_date").getTime()));
        }

        return user;
    }

    public int deleteUser(String id) throws Exception {
        Connection conn = ds.getConnection();

        String sql = "delete from user_info where id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql); // SQL Injection공격, 성능향상
        pstmt.setString(1, id);

        return pstmt.executeUpdate(); // delete
    }

    // 매개변수로 받은 사용자 정보로 user_info테이블을 update하는 메서드
    public int updateUser(User user) throws Exception {
        Connection conn = ds.getConnection();

        String sql = "update user_info set pwd = ?, name = ?, email = ?, reg_date = ? where id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, user.getPwd());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getEmail());
        pstmt.setDate(4, new java.sql.Date(user.getReg_date().getTime()));
        pstmt.setString(5, user.getId());

        return pstmt.executeUpdate(); // update
    }

    @Test
    public void springJdbcConnectionTest() throws Exception {
        //수동주입
//        ApplicationContext ac = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/**/root-context.xml");
//        DataSource ds = ac.getBean(DataSource.class);

        Connection conn = ds.getConnection(); // 데이터베이스의 연결을 얻는다.

        System.out.println("conn = " + conn);

        // Assert문 => 테스트 성공여부 확인
        assertTrue(conn!=null); // 괄호 안의 조건식이 true면, 테스트 성공, 아니면 실패
    }
}