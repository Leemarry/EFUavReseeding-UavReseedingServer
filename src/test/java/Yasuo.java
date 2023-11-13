import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

public class Yasuo {
    public static void main(String[] args) {
        String name = "DJI_20220329120448_0040_Z"+".JPG";
        String path1 = "C:/efuav/UavSystem/photo/uav/UAV1ZNBJBK00C005X/images/20220817/"+name;
        String thumbnailPath = "C:/efuav/UavSystem/photo/uav/UAV1ZNBJBK00C005X/thumbnail/20220817/";
        String thumbnailPath1 = "C:/efuav/UavSystem/photo/uav/UAV1ZNBJBK00C005X/thumbnail/20220817/"+name;
        File file2 = new File(thumbnailPath);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        try {
            Thumbnails.of(path1)
                    .size(160, 120)
                    .toFile(thumbnailPath1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
