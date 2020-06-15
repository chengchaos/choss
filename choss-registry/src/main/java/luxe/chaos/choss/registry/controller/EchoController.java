package luxe.chaos.choss.registry.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class EchoController {

    public static class EchoEntity {

        private final Map<String, String> header;
        private final Map<String, String> params;

        public EchoEntity() {
            this.header = new HashMap<>();
            this.params = new HashMap<>();
        }

        public Map<String, String> getHeader() {
            return header;
        }

        public Map<String, String> getParams() {
            return params;
        }

        public EchoEntity addHeader(String key, String value) {
            this.header.put(key, value);
            return this;
        }

        public EchoEntity addParams(String key, String value) {
            this.params.put(key, value);
            return this;
        }
    }

    @RequestMapping("/v1/echo")
    public ResponseEntity<EchoEntity> echo(HttpServletRequest request,
                                           HttpServletResponse response) {

        EchoEntity entity = new EchoEntity();

        Enumeration<String> headerNames = request.getHeaderNames();


        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            Optional<String> op = Optional.ofNullable(request.getHeader(name));
            entity.addHeader(name, op.orElse(""));
        }

        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            Optional<String> op = Optional.ofNullable(request.getParameter(name));
            entity.addParams(name, op.orElse(""));
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(entity);
    }
}
