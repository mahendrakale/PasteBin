package com.clypd.server;

import com.google.common.hash.Hashing;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class ShortenerService {

    public static void main(String[] args) {
        SpringApplication.run(ShortenerService.class, args);
    }

    @Autowired
    private StringRedisTemplate redis;

    private static final String DUMMY_DOMAIN = "https://cly.pd/";

    @RequestMapping(value = "/short", method = RequestMethod.POST)
    public ResponseEntity<String> shorten(HttpServletRequest req) throws Exception {

        Collection<Part> fileParts = req.getParts().stream().filter(part -> "content".equals(part.getName())).collect(Collectors.toList()); // Retrieves <input type="file" name="file" multiple="true">
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        for (Part p : fileParts) {
            is = p.getInputStream();
            int ch = -1;
            while (-1 != (ch = is.read())) {
                sb.append((char) ch);
            }
        }
        if (null != is) {
            is.close();
        }

        String url = sb.toString();
        if (!url.isEmpty() && url.startsWith("http")) {
            final String id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
            redis.opsForValue().set(id, url);
            return new ResponseEntity(DUMMY_DOMAIN + id, HttpStatus.OK) ;
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> unShorten(@PathVariable String id) throws Exception {
        final String urlExpanded = redis.opsForValue().get(id);
        if (urlExpanded != null) {
            return new ResponseEntity(urlExpanded, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping(value = "/health_check", method = RequestMethod.GET)
    public ResponseEntity<Integer> status() throws Exception {
        return new ResponseEntity(HttpStatus.OK);
    }
}
