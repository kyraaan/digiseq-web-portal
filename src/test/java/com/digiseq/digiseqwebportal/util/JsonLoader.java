package com.digiseq.digiseqwebportal.util;

import static java.nio.file.Files.readString;
import static org.springframework.util.ResourceUtils.getFile;

import java.io.File;
import java.io.FileNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonLoader {

  private JsonLoader() {
    throw new UnsupportedOperationException("static class");
  }

  public static String loadJsonFromFile(String path) {
    try {
      File file = getFileFromClassPath(path);
      return readString(file.toPath());
    } catch (Exception e) {
      log.error("Failed to read json file {}", path);
      throw new RuntimeException("Failed to read file: " + path, e);
    }
  }

  private static File getFileFromClassPath(String path) throws FileNotFoundException {
    return getFile("classpath:" + path);
  }
}
