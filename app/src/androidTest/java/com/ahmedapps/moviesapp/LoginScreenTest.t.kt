import org.junit.Test
import org.junit.Assert.*

fun validateLogin(username: String, password: String): Boolean {
    return username.isNotEmpty() && password.isNotEmpty()
}

class LoginUnitTest {

    @Test
    fun testLoginEmptyFields() {
        val result = validateLogin("", "")
        assert(!result) // Teste de falha quando os campos estão vazios
    }

    @Test
    fun testLoginSuccess() {
        val result = validateLogin("user123", "password")
        assert(result) // Teste de sucesso quando os campos são preenchidos
    }
}
