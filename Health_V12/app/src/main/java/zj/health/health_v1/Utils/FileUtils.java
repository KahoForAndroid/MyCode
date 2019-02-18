package zj.health.health_v1.Utils;

import java.io.File;

/**
 * Created by Administrator on 2018/4/23.
 */

public class FileUtils {

    public static void  DelFile(String path){
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        file = null;
    }
}
