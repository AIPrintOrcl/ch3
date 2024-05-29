package com.fastcampus.ch3;

import junit.framework.TestCase;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit4ClassRunner : ac를 자동으로 생성
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/**/root-context.xml") // @ContextConfiguration : xml 설정 파일 위치를 지정
public class UserDaoImplTest {

    @Autowired
    UserDao userDao;

    @Test
    public void deleteUser() {
        Calendar cal = Calendar.getInstance(); // assertTrue(user.equals(user2)); user와 user2의 birth 날짜가 다르다. birth는 util date는 시간도 들어가는데 user2 입력될 때 시간이 짤려서 입력되어 user 에러가 발생
        cal.clear();
        cal.set(2021, 1, 1); // 그래서 시간 정보를 지운다.

        userDao.deleteAll();

        User user = new User("aaa", "1111", "sam", "aaa@aaa.com", new Date(cal.getTimeInMillis()), "in", new Date());

        assertTrue(userDao.insertUser(user)==1);

        assertTrue(userDao.deleteUser(user.getId())!=0);
    }

    @Test
    public void selectUser() {
        Calendar cal = Calendar.getInstance(); // assertTrue(user.equals(user2)); user와 user2의 birth 날짜가 다르다. birth는 util date는 시간도 들어가는데 user2 입력될 때 시간이 짤려서 입력되어 user 에러가 발생
        cal.clear();
        cal.set(2021, 1, 1); // 그래서 시간 정보를 지운다.

        userDao.deleteAll();

        User user = new User("aaa", "1111", "sam", "aaa@aaa.com", new Date(cal.getTimeInMillis()), "in", new Date());

        assertTrue(userDao.insertUser(user)==1);

        User userSelect = userDao.selectUser(user.getId());

        System.out.println("userSelect = " + userSelect);

        assertTrue(user.equals(userSelect));
    }

    @Test
    public void insertUser() {

        userDao.deleteAll();

        User user = new User("aaa", "1111", "sam", "aaa@aaa.com", new Date(), "in", new Date());

        assertTrue(userDao.insertUser(user)==1);
    }

    @Test
    public void updateUser() throws Exception {
        Calendar cal = Calendar.getInstance(); // assertTrue(user.equals(user2)); user와 user2의 birth 날짜가 다르다. birth는 util date는 시간도 들어가는데 user2 입력될 때 시간이 짤려서 입력되어 user 에러가 발생
        cal.clear();
        cal.set(2021, 1, 1); // 그래서 시간 정보를 지운다.

        userDao.deleteAll();

        User user = new User("asdf", "1234", "abc", "aaa@aaa.com", new Date(cal.getTimeInMillis()), "fb", new Date());
        int rowCnt = userDao.insertUser(user);
        assertTrue(rowCnt==1);

        user.setPwd("4321");
        user.setEmail("bbb@bbb.com");
        rowCnt = userDao.updateUser(user);
        assertTrue(rowCnt == 1);

        User user2 = userDao.selectUser(user.getId());
        assertTrue(user.equals(user2));

        System.out.println("user = " + user);
        System.out.println("user2 = " + user2);
    }
}