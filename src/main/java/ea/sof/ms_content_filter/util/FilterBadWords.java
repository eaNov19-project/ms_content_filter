package ea.sof.ms_content_filter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FilterBadWords {
    public static final String BAD_WORD_FILE_PATH = "config/bad_words.txt";
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterBadWords.class);
    public boolean filterBadWords(String content)  {
        try {
            Scanner scanner = new Scanner(new File(getClass().getClassLoader().getResource(BAD_WORD_FILE_PATH).getFile()));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (content.contains(line)) {
                    return true;
                }
            }
        }catch (FileNotFoundException ex){
            System.out.println(ex.getMessage());
            LOGGER.error("FilterBadWords error: " + ex.getMessage());
        }
        return false;
    }
}
