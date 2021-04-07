package com.in726.app.database.dao;

import com.in726.app.model.sub_functional_model.CheckLink;

import java.util.List;

public interface CheckLinkDao {
    void save(CheckLink checkLink);

    //    void deleteByLinkId(int linkId);
    long getLinkChecksCount();
    long getLinkChecksCountByPeriod(String period);

    List<CheckLink> getChecksByLinkId(long linkId);
    CheckLink getLastCheckByLinkId(long linkId);
    List<CheckLink> getWeekChecksByLinkId(long linkId);

}
