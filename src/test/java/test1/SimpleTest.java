package test1;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class SimpleTest {

    @Test
    public void SystemProperties_확인(){
        Properties props = System.getProperties();
        Set<String> sets = props.stringPropertyNames();

        for(String key : sets){
            System.out.printf("%s : %s\n", key, props.getProperty(key));
        }
    }

    @Test
    public void String_확인(){
        String input = "[id: 0x52aeeae7, L:/0:0:0:0:0:0:0:1:8080 - R:/0:0:0:0:0:0:0:1:3428]";
        System.out.println(input.lastIndexOf(':'));
        System.out.println(input.substring(input.lastIndexOf(':')+1, input.length()-1));

    }

    @Test
    public void ArrayList_확인(){
        List<String> list = new ArrayList<String>();
        list.add("AA");
        list.add("BB");
        list.add("CC");

        System.out.println(list.contains("AA"));
        System.out.println(list.contains("AAA"));

        list.remove("AA");
        System.out.println(list.contains("AA"));
    }


}
