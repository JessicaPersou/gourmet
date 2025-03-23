package com.postech.gourmet.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionsTest {

    private static final String ERROR_MESSAGE = "Mensagem de erro de teste";

    @Test
    @DisplayName("Deve criar ResourceNotFoundException com mensagem correta")
    void deveCriarResourceNotFoundExceptionComMensagemCorreta() {
        ResourceNotFoundException exception = new ResourceNotFoundException(ERROR_MESSAGE);

        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve criar InvalidRequestException com mensagem correta")
    void deveCriarInvalidRequestExceptionComMensagemCorreta() {
        InvalidRequestException exception = new InvalidRequestException(ERROR_MESSAGE);

        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve criar DuplicateResourceException com mensagem correta")
    void deveCriarDuplicateResourceExceptionComMensagemCorreta() {
        DuplicateResourceException exception = new DuplicateResourceException(ERROR_MESSAGE);

        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve capturar exceções em blocos try-catch corretamente")
    void deveCapturarExcecoesEmBlocosTryCatchCorretamente() {
        // ResourceNotFoundException
        try {
            throw new ResourceNotFoundException(ERROR_MESSAGE);
        } catch (ResourceNotFoundException e) {
            assertEquals(ERROR_MESSAGE, e.getMessage());
        }

        // InvalidRequestException
        try {
            throw new InvalidRequestException(ERROR_MESSAGE);
        } catch (InvalidRequestException e) {
            assertEquals(ERROR_MESSAGE, e.getMessage());
        }

        // DuplicateResourceException
        try {
            throw new DuplicateResourceException(ERROR_MESSAGE);
        } catch (DuplicateResourceException e) {
            assertEquals(ERROR_MESSAGE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Deve verificar hierarquia de exceções personalizadas")
    void deveVerificarHierarquiaExcecoesPersonalizadas() {
        ResourceNotFoundException resourceException = new ResourceNotFoundException(ERROR_MESSAGE);
        InvalidRequestException invalidRequestException = new InvalidRequestException(ERROR_MESSAGE);
        DuplicateResourceException duplicateException = new DuplicateResourceException(ERROR_MESSAGE);

        assertTrue(resourceException instanceof RuntimeException);
        assertTrue(invalidRequestException instanceof RuntimeException);
        assertTrue(duplicateException instanceof RuntimeException);
    }
}