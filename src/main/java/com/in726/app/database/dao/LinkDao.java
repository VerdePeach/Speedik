package com.in726.app.database.dao;

import com.in726.app.model.sub_functional_model.Link;

import java.util.List;

public interface LinkDao {
    void addLink(Link link);

    List<Link> findLinksByUserId(int id);

        void updateLink(Link link);
    //    void deleteLink(Link link);

    long amountOfLinksForUserByUserId(int userId);

    Link findLastAddedLinkByUserId(int userId);

    List<Link> findLinksByDashboardId(int dashId);
}
