package com.zerobase.foodlier.common.validator.image;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ImageFileValidator implements ConstraintValidator<ImageFile, Object> {

    private static final Tika tika = new Tika();
    private static final List<String> notValidTypeList = Arrays.asList("image/jpeg", "image/pjpeg", "image/png", "image/gif", "image/bmp", "image/x-windows-bmp");

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(Objects.isNull(value)){
            return true;
        }

        if(value instanceof MultipartFile){
            return isImage((MultipartFile) value);
        }

        if(value instanceof List<?>){
            List<?> list = (List<?>) value;

            for(Object element : list){
                if(element instanceof MultipartFile){
                    if(!isImage((MultipartFile) element)){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean isImage(MultipartFile file) {

        try (InputStream inputStream = file.getInputStream()) {
            String mimeType = tika.detect(inputStream);

            return notValidTypeList.stream().anyMatch(notValidType -> notValidType.equalsIgnoreCase(mimeType));

        } catch (IOException e) {
            return false;
        }
    }
}
