package uoc.ded.practica;

import org.junit.After;
import org.junit.Before;
import uoc.ded.practica.util.DateUtils;

import java.time.LocalDate;
import java.util.Date;

public class SafetyActivities4Covid19PR {
    protected SafetyActivities4Covid19 safetyActivities4Covid19;

    @Before
    public void setUp() throws Exception {
        this.safetyActivities4Covid19 = FactorySafetyActivities4Covid19.getSafetyActivities4Covid19();
    }

    @After
    public void tearDown() {
        this.safetyActivities4Covid19 = null;
    }

    protected Date createDate(String date) {
        return DateUtils.createDate(date);
    }

    protected LocalDate createLocalDate(String date) {
        return DateUtils.createLocalDate(date);
    }

}
