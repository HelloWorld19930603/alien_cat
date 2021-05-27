package com.aliencat.javabase.api.enums;

import org.junit.Test;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;

public class EnumTest {

    @Test
    public void testForEach(){
        for(ColorEnum1 c : ColorEnum1.values()){
            System.out.println(c.name()+"-->"+c.ordinal());
        }
    }

    @Test
    public void testEquals(){
        System.out.println(ColorEnum1.RED.equals(ColorEnum2.RED));
    }

    @Test
    public void testSwitch(){
        switch (ColorEnum.RED){
            case RED:
                System.out.println("这是红色");
                break;
            case GREEN:
                System.out.println("这是绿色");
                break;
            case BLUE:
                System.out.println("这是蓝色");
                break;

        }
    }

    @Test
    public void testColorInfo(){
        for(ColorInfo ci : ColorEnum2.values()){
            ci.printColor();
        }
    }


    @Test
    public void testEnumMap(){
        EnumMap<ColorEnum3,String> enumMap = new EnumMap(ColorEnum3.class);
        enumMap.put(ColorEnum3.RED,"红色");
        enumMap.put(ColorEnum3.GREEN,"绿色");
        enumMap.put(ColorEnum3.BLUE,"蓝色");
        for(Map.Entry<ColorEnum3,String> entry : enumMap.entrySet()){
            System.out.println(entry.getKey()+"-->"+entry.getValue());
        }
    }

    @Test
    public void testEnumSet() {
        EnumSet<ColorEnum3> enumSet = EnumSet.allOf(ColorEnum3.class);
        Iterator<ColorEnum3> iterator = enumSet.iterator();
        while(iterator.hasNext()){
            iterator.next().printColor();
        }
    }
}
