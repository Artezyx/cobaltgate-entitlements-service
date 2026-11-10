package dev.cobaltgate.web;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import java.util.NoSuchElementException;
@RestControllerAdvice
public class ErrorHandler {
 @ExceptionHandler(NoSuchElementException.class) public ProblemDetail notFound(Exception ex){return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,ex.getMessage());}
 @ExceptionHandler({IllegalStateException.class,DataIntegrityViolationException.class,ObjectOptimisticLockingFailureException.class})
 public ProblemDetail conflict(Exception ex){return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,"Seat capacity or concurrent update conflict");}
 @ExceptionHandler({IllegalArgumentException.class,MethodArgumentNotValidException.class})
 public ProblemDetail invalid(Exception ex){return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,"Invalid request");}
}
