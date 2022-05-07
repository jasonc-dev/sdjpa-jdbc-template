package jason.springframework.jdbc;

import jason.springframework.jdbc.dao.AuthorDao;
import jason.springframework.jdbc.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = {"jason.springframework.jdbc.dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DaoIntegrationTest {

    @Autowired
    AuthorDao authorDao;

    @Test
    void testDeleteAuthor() {
        Author author = new Author();
        author.setFirstName("Jay");
        author.setLastName("C");

        Author saved = authorDao.saveNewAuthor(author);

        authorDao.deleteAuthorById(saved.getId());

        assertThrows(EmptyResultDataAccessException.class, () -> {
            authorDao.getById(saved.getId());
        });
    }

    @Test
    void testUpdateAuthor() {
        Author author = new Author();
        author.setFirstName("Jay");
        author.setLastName("Cohen");

        Author saved = authorDao.saveNewAuthor(author);

        saved.setLastName("Lowther");
        Author updated = authorDao.updateAuthor(saved);

        assertThat(updated.getLastName()).isEqualTo("Lowther");
    }

    @Test
    void testSaveAuthor() {
        Author author = new Author();
        author.setFirstName("Jay");
        author.setLastName("Cohen");
        Author saved = authorDao.saveNewAuthor(author);

        assertThat(saved).isNotNull();
    }

    @Test
    void testGetAuthorByName() {

        Author author = authorDao.findAuthorByName("Craig", "Walls");

        assertThat(author).isNotNull();
    }

    @Test
    void testGetAuthor() {

        Author author = authorDao.getById(1L);

        assertThat(author).isNotNull();
    }
}
