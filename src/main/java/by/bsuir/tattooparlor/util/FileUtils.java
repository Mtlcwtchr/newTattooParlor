package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.config.GlobalPaths;
import by.bsuir.tattooparlor.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static String getNewPictureUri(String fileName, String contentType) {
        return Integer.toHexString(fileName.hashCode()) + contentType;
    }

    public static File trySaveNewPictureByPath(MultipartFile multipartFile, String profilePictureUri) throws IOException {
        File file = new File(GlobalPaths.IMAGES_SRC + profilePictureUri);
        multipartFile.transferTo(file);
        return file;
    }

}
