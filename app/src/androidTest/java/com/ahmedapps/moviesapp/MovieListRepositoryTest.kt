import org.junit.Before
import org.junit.Test

// Simulação de uma classe de repositório
class MovieListRepository {
    fun getMovies(): List<String> {
        return listOf("Movie 1", "Movie 2", "Movie 3")
    }
}

// Teste Unitário para o repositório
class MovieListRepositoryTest {

    private lateinit var repository: MovieListRepository

    @Before
    fun setup() {
        repository = MovieListRepository()
    }

    @Test
    fun testFetchMovies() {
        val movies = repository.getMovies()
        assert(movies.size == 3) // Teste se há 3 filmes retornados
    }
}
