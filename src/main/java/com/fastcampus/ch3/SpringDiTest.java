/*
package com.fastcampus.ch3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

@Component class Car{
    @Value("red") String color;
    @Value("100") int oil;
//    @Autowired
//    @Qualifier("superEngine") // 만약 @Autowired에서 타입과 이름으로 찾지 못할 경우 @Qualifier 직접 지정해준다.
    @Resource(name="superEngine") // byName;
    Engine engine; // byType - 타입으로 먼저 검색, 타입이 여러개면 이름으로 검색. - engine, superEngine, turboEngine
    @Autowired
    Door[] doors;

    public Car() { // 기본 생성자를 꼭 만들어주자.
    }

    public Car(String color, int oil, Engine engine, Door[] doors) {
        this.color = color;
        this.oil = oil;
        this.engine = engine;
        this.doors = doors;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setOil(int oil) {
        this.oil = oil;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setDoors(Door[] doors) {
        this.doors = doors;
    }

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
//@Component
class Engine{} // @Component => <bean id="engine" class="com.fastcampus.ch3.Engine"/>
@Component class SuperEngine extends Engine{}
@Component class TurboEngine extends Engine{}
@Component class Door{}

public class SpringDiTest {
    public static void main(String[] args) {
        ApplicationContext ac = new GenericXmlApplicationContext("config.xml");
//        Car car = (Car) ac.getBean("car"); // byName. 아래와 같은 문장
        Car car = ac.getBean("car", Car.class); // byName. com.fastcampus.ch3.Car@3c407114
//        Car car2 = (Car) ac.getBean(Car.class); // byType. com.fastcampus.ch3.Car@3c407114 => car과 같은 객체(싱글톤)
//        Engine engine = (Engine) ac.getBean("superEngine");
////        Engine engine = (Engine) ac.getBean(Engine.class); //byType => expected single matching bean but found 3 : 같은 타입이 여러개 일 경우 에러..
//        Door door = (Door) ac.getBean("door");

        //setter를 호출X => xml에서 property 태그로 처리 가능.
//        car.setColor("red");
//        car.setOil(100);
//        car.setEngine(engine);
//        car.setDoors(new Door[]{ (Door)ac.getBean("door"), (Door)ac.getBean("door")});

        System.out.println("car = " + car);

    }//main
}//class
*/
