package luxe.chaos.choss.worker.controller;

import luxe.chaos.choss.worker.entity.EchoEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Optional;

@RestController
public class EchoController {



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
