package edu.pe.utp.marcodesarrolloweb.techfix.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public String handleRecursoNoEncontrado(
            RecursoNoEncontradoException e,
            RedirectAttributes redirectAttrs,
            HttpServletRequest request) {
        redirectAttrs.addFlashAttribute("mensajeError", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/inicio");
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public String handleReglaNegocio(
            ReglaNegocioException e,
            RedirectAttributes redirectAttrs,
            HttpServletRequest request) {
        redirectAttrs.addFlashAttribute("mensajeError", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/inicio");
    }
}