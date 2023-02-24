package jp.co.axa.apidemo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class TestUtils {
  public static String getJson(String fileName) throws IOException {
    ResourceLoader resourceLoader = new DefaultResourceLoader();
    Resource resource = resourceLoader.getResource("classpath:" +fileName);
    if (resource.getInputStream() != null) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
      return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    } else {
      throw new RuntimeException("resource not found");
    }
  }
}
