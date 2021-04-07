package com.in726.app.database.dao;

import com.in726.app.model.sub_functional_model.Letter;

public interface LetterDao {
    void save(Letter letter);

    long getLettersCount();
    long getLettersCountByPeriod(String period);
}
