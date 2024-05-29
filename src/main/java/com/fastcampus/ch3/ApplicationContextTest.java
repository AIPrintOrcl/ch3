/*
package com.fastcampus.ch3;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.*;
import org.springframework.stereotype.*;

import javax.inject.*;
import java.util.*;

@Component
@Scope("prototype")
class Door {}
@Component class Engine {}
@Component class TurboEngine extends Engine {}
@Component class SuperEngine extends Engine {}

@Component
class Car {
    @Value("red") String color;
    @Value("100") int oil;
//    @Autowired
    Engine engine;
//    @Autowired
    Door[] doors;

    @Autowired */
/* 생성자가 하나일 경우는 @Autowired 생략해도 주입이 잘되지만 다른 생성자가 있을 경우 어느 생성자를 주입해야 할지 알려줘야하기 때문에 @Autowired 입력해야 주입이된다. *//*

    public Car(@Value("red") String color,@Value("100") int oil, Engine engine, Door[] doors) {
        this.color = color;
        this.oil = oil;
        this.engine = engine;
        this.doors = doors;
    }

    public Car() {}

    @Override
    public String toString() {
        return "Car{" +
                "color='" + color + '\'' +
                ", oil=" + oil +
                ", engine=" + engine +
                ", doors=" + Arrays.toString(doors) +
                '}';
    }
}

@Component
@PropertySource("setting.properties")
class SysInfo {
    @Value("#{systemProperties['user.timezone']}")
    String timeZone;
    @Value("#{systemEnvironment['HOMEPATH']}") // 환경변수 id = 환경변수 값
    String currDir;
    @Value("${autosaveDir}")
    String autosaveDir;
    @Value("${autosaveInterval}")
    int autosaveInterval;
    @Value("${autosave}")
    boolean autosave;

    @Override
    public String toString() {
        return "SysInfo{" +
                "timeZone='" + timeZone + '\'' +
                ", currDir='" + currDir + '\'' +
                ", autosaveDir='" + autosaveDir + '\'' +
                ", autosaveInterval=" + autosaveInterval +
                ", autosave=" + autosave +
                '}';
    }
}

public class ApplicationContextTest {
    public static void main(String[] args) {
        ApplicationContext ac = new GenericXmlApplicationContext("config.xml");
//      Car car = ac.getBean("car", Car.class); // 타입을 지정하면 형변환 안해도됨. 아래의 문장과 동일
        Car car  = (Car) ac.getBean("car"); // 이름으로 빈 검색
        Car car2 = (Car) ac.getBean(Car.class);   // 타입으로 빈 검색
        System.out.println("car = " + car);

        System.out.println("ac.getBean(SysInfo.class) = " + ac.getBean(SysInfo.class));

        //환경변수를 가지고 오기
        Map<String, String> map= System.getenv();
        System.out.println("map = " + map); // HOMEPATH=\Users\Administrator

        //System Properties
        Properties properties = System.getProperties();
        System.out.println("properties = " + properties); // java.vendor=Oracle Corporation
    }//main
}//class*/
