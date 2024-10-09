import org.junit.Test

// Função de validação de senha simples
fun validatePassword(password: String): String {
    return if (password.length >= 8) "Senha válida" else "Senha fraca"
}

// Teste Unitário para a função de validação de senha
class RegisterUnitTest {

    @Test
    fun testWeakPassword() {
        val result = validatePassword("1234")
        assert(result == "Senha fraca") // Teste para senha fraca
    }

    @Test
    fun testStrongPassword() {
        val result = validatePassword("Password@123")
        assert(result == "Senha válida") // Teste para senha forte
    }
}
