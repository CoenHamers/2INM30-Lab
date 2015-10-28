package cloudclient;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Icon;
import javax.swing.JOptionPane;

      
/**
 *
 * @author Coen
 */
public class InputReader {
    
    private List<File>filesInFolder;
    
    public InputReader() {
        filesInFolder = null;
    }
    
    public void read() {
        String s = (String)JOptionPane.showInputDialog(
                    null,
                    "Input the map where the images reside:",
                    "Customized Dialog",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "F:\\Documents\\Github\\2INM30-Lab\\Images");
        try {
        filesInFolder = Files.walk(Paths.get(s))
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
