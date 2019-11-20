package ea.sof.ms_content_filter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

//import javax.annotation.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FilterBadWords {
    public static final String BAD_WORD_FILE_PATH = "bad_words.txt";
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterBadWords.class);
//    @Value("classpath:bad_words.txt")
//    Resource resourceFile;
    public Resource loadRes() {
        return new ClassPathResource(BAD_WORD_FILE_PATH);
    }
    public boolean filterBadWords(String content)  {
//        System.out.println(this.resourceFile);
        BufferedReader reader = null;
        try {
//            InputStream is = new FileInputStream(resourceFile.name());
            Resource res = loadRes();
            System.out.println(res);
//            FileInputStream is = (FileInputStream) res.getInputStream();
//            reader = new BufferedReader(new InputStreamReader(is));
            reader = new BufferedReader(
                    new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8));

            System.out.println("Reading File line by line using BufferedReader");

            String line = reader.readLine();
            while(line != null){
                System.out.println(line);
                if (content.toLowerCase().contains(line.trim())) {
                    return true;
                }
                line = reader.readLine();
            }

//            Scanner scanner = new Scanner(res.getFile());
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                if (content.contains(line)) {
//                    return true;
//                }
//            }
        }catch (IOException ex){
            System.out.println(ex.getMessage());
            LOGGER.error("FilterBadWords error: " + ex.getMessage());
        }
        return false;
    }
}
