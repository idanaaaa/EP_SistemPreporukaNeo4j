package ptf.si.ep.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.TreeMap;

// Vraća poruke iz @Valid anotacija po poljima: { "greske": { "naziv": "Naziv proizvoda je obavezan" } }
@RestControllerAdvice(basePackages = "ptf.si.ep.controller")
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validacija(MethodArgumentNotValidException e) {
        Map<String, String> greske = new TreeMap<>();
        for (FieldError greska : e.getBindingResult().getFieldErrors()) {
            greske.putIfAbsent(greska.getField(), greska.getDefaultMessage());
        }
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Neispravni podaci");
        problem.setProperty("greske", greske);
        return problem;
    }
}
