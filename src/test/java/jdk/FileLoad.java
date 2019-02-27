package jdk;

import io.netty.util.CharsetUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.*;
import java.util.List;

public class FileLoad {
    @Test
    public void fileLoad(){
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            String htmlFileName = "main.html";
            File htmlFile = new File(classLoader.getResource(htmlFileName).getFile());

            List<String> lines = FileUtils.readLines(htmlFile, CharsetUtil.UTF_8);

            for(String line : lines){
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
