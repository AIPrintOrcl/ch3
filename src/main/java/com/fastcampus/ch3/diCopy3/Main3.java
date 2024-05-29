package com.fastcampus.ch3.diCopy3;

import com.google.common.reflect.ClassPath;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component class Car {}
@Component class SportsCar extends Car {}
@Component class Truck extends Car {}
@Component class Engine extends Car {}

class AppContext {
    Map map; // 객체 저장소

    AppContext() {
        map = new HashMap();
        doComponentScan();
    }

    private void doComponentScan() { //Maven guava 라이브러리 설치
        try {
            ClassLoader classLoader = AppContext.class.getClassLoader();
            ClassPath classPath = ClassPath.from(classLoader);

            // 1. 패키지내의 클래스 목록을 가져온다.
            Set<ClassPath.ClassInfo> set = classPath.getTopLevelClasses("com.fastcampus.ch3.diCopy3");

            // 2. 반목문으로 클래스를 하나씩 읽어와서 @Component이 붙어 있는지 확인
            for(ClassPath.ClassInfo classInfo : set) {
                Class clazz = classInfo.load(); // class com.fastcampus.ch3.diCopy3.Car
                Component component = (Component) clazz.getAnnotation(Component.class);
                
                // 3. @Component가 붙어있으면 객체를 생성해서 map에 저장
                if(component != null) {
                    String id = StringUtils.uncapitalize(classInfo.getSimpleName()); // Car -> car
                    map.put(id, clazz.newInstance()); // 객체 생성해서 map에 저장
                }//end if
            }//end for
        } catch (Exception e) {
            e.printStackTrace();
        }//end catch
    }//doComponentScan

    Object getBean(String key) { return map.get(key); } // by Name : 이름(id)로 찾기
    Object getBean(Class clazz) { // by type : 타입(value)으로 찾기
        for (Object obj : map.values()) {
            if(clazz.isInstance(obj)) { // obj instanceof clazz
                return obj;
            }//end if
        }//end for
        return null;
    }//end getBean
}

    public class Main3 {
    public static void main(String[] args) throws Exception {
        AppContext ac =new AppContext();
        Car car = (Car) ac.getBean("car"); // byName으로 객체를 검색
        Engine engine = (Engine) ac.getBean(Engine.class); // byType으로 객체를 검색
        System.out.println("car = " + car);
        System.out.println("engine = " + engine);
    }//main
}//class
