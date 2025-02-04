package at.htl.leoquest.control;

import at.htl.leoquest.entities.Questionnaire;
import at.htl.leoquest.entities.Survey;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import java.time.LocalDate;

import static org.assertj.db.api.Assertions.assertThat;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SurveyRepositoryTest {

    @Inject
    SurveyRepository surveyRepository;

    Table t = new Table(DataSource.getDataSource(), "survey");

    @Test
    @Order(10)
    void createSurveyTest(){
        Questionnaire q = new Questionnaire(1L, "Test", "Test of the Questionnaire");
        LocalDate d = LocalDate.now();
        Survey s = new Survey(d, q);

        surveyRepository.save(s);
        assertThat(t).row(0)
                .value().isEqualTo(1)
                .value().isEqualTo(d)
                .value().isEqualTo(1);
    }
}
