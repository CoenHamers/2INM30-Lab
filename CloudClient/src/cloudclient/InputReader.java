package cloudclient;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import ij.*;
import ij.gui.*;
import java.awt.Color;
import java.awt.Font;
      
/**
 *
 * @author Administrator
 */
public class InputReader {
    
    private List<File>filesInFolder;
    
    public InputReader() {
        filesInFolder = null;
    }
    
    public void read() {
        try {
        filesInFolder = Files.walk(Paths.get("C:\\Users\\Administrator\\Pictures\\cloudImages"))
                                .filter(Files::isRegularFile)
                                .map(Path::toFile)
                                .collect(Collectors.toList());
        } catch (Exception e) {
            
        }
    }
    
    public List<File> getFilesinFolder() {
        return filesInFolder;
    }
}
