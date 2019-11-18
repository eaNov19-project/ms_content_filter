package ea.sof.ms_content_filter.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FilterBadWords {
    public static final String BAD_WORD_FILE_PATH = "config/bad_words.txt";
    public boolean filterBadWords(String content)  {
        try {
            Scanner scanner = new Scanner(new File(BAD_WORD_FILE_PATH));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (content.contains(line)) {
                    return false;
                }
            }
        }catch (FileNotFoundException ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }
}
