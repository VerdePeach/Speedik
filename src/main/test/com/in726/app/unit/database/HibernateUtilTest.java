package com.in726.app.unit.database;

import com.in726.app.database.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

public class HibernateUtilTest {

    @Test(expected = NullPointerException.class)
    public void startSessionNegative() {
        HibernateUtil.startSession();
    }

}
