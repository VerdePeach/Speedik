package com.in726.app.database.service;

import com.in726.app.database.HibernateUtil;
import com.in726.app.database.dao.DashboardDao;
import com.in726.app.email.EmailAccount;
import com.in726.app.enums.LinkStatus;
import com.in726.app.model.Agent;
import com.in726.app.model.Dashboard;
import com.in726.app.model.dto.DashboardDto;
import com.in726.app.model.dto.DashboardLinkDto;
import com.in726.app.model.sub_functional_model.Link;
import com.in726.app.security.EncoderSHA256HMAC;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Realisation of DashboardServiceDao interface.
 * Service for saving, updating, getting and deleting dashboards from database.
 */
public class DashboardService implements DashboardDao {

    private LinkService linkService = new LinkService();
    private CheckLinkService checkLinkService = new CheckLinkService();
    private UserService userService = new UserService();

    /**
     * Saves new dashboard to database.
     *
     * @param dashboard dashboard
     * @return url to dashboard.
     */
    @Override
    public String saveDashboard(Dashboard dashboard) {
        dashboard.setUrl(null);

        List<Link> userLinks = new ArrayList<>();
        dashboard.getLinks().forEach(l -> {
            var links = linkService.findLinksByUserId(dashboard.getUser().getId()).stream()
                    .filter(fl -> fl.getId() == l.getId()).collect(Collectors.toList());
            userLinks.addAll(links);
        });

        dashboard.setLinks(userLinks);
        dashboard.getLinks().forEach(l -> l.setDashboard(dashboard));
        var foundDashboard = getByUserIdDash(dashboard.getUser().getId());

        if (foundDashboard != null) {
            removeOldLinksFromDashboard(foundDashboard.getLinks());
            dashboard.setId(foundDashboard.getId());
            dashboard.setUrl(foundDashboard.getUrl());
            try (var session = HibernateUtil.startSession()) {

                var transaction = session.beginTransaction();
                session.update(dashboard);
                transaction.commit();
            }
        } else {
            try (var session = HibernateUtil.startSession()) {
                var transaction = session.beginTransaction();

                try (Stream<String> stream = Files.lines(Paths.get("emailConfirmSignature.txt"))) {
//                    dashboard.setUrl("https:/t6.tss2020.site/dashboard/" + dashboard.getUser().getId() + "/"
//                            + EncoderSHA256HMAC.encodeString(stream.limit(1).collect(Collectors.toList()).get(0),
//                            "" + dashboard.getUser().getId()));
                    dashboard.setUrl("/api/dashboard/" + dashboard.getUser().getId() + "/"
                            + EncoderSHA256HMAC.encodeString(stream.limit(1).collect(Collectors.toList()).get(0),
                            "" + dashboard.getUser().getId()));
                    session.save(dashboard);
                    transaction.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        return getByUserId(dashboard.getUser().getId()).getUrl();
    }

    private void removeOldLinksFromDashboard(List<Link> links) {
        links.forEach(l -> {
            l.setDashboard(null);
            linkService.updateLink(l);
        });
    }


    /**
     * fetches dashboard by id.
     *
     * @param userId user id
     * @return dashboard dto
     */
    @Override
    public DashboardDto getByUserId(int userId) {
        try (var session = HibernateUtil.startSession()) {
            var transaction = session.beginTransaction();
            Query query = session.createQuery("from Dashboard where user_id = ?1");
            query.setParameter(1, userId);
            List<Dashboard> dashboards = query.getResultList();
            transaction.commit();
            if (dashboards.size() > 0) {
                var dash = dashboards.get(0);
                var dashDto = new DashboardDto();
                dashDto.setUrl(dash.getUrl());
                dashDto.setDescription(dash.getDescription());

                var links = linkService.findLinksByDashboardId(dash.getId());
                var linksDto = links.stream().map(l -> {
                    var linkDto = new DashboardLinkDto();
                    l.setChecks(Arrays.asList(checkLinkService.getLastCheckByLinkId(l.getId())));
                    linkDto.setLink(l);

                    var allChecks = checkLinkService.getWeekChecksByLinkId(l.getId());
                    var upChecks = allChecks.stream().filter(chl -> chl.getStatus()
                            .equals(LinkStatus.UP)).collect(Collectors.toList());

                    var mes = "Checks not found by last week";
                    if (allChecks.size() > 0) {
                        mes = (upChecks.size() * 100 / allChecks.size()) + "%";
                    }
                    linkDto.setSuccessPercent(mes);
                    return linkDto;
                }).collect(Collectors.toList());
                dashDto.setLinksDto(linksDto);
                dashDto.setOwnerName(userService.getUserById(userId).getUsername());
                return dashDto;
            }
        }
        return null;
    }

    /**
     * fetches dashboard by user id.
     *
     * @param userId user id
     * @return dashboard dto
     */
    @Override
    public Dashboard getByUserIdDash(int userId) {
        try (var session = HibernateUtil.startSession()) {
            var transaction = session.beginTransaction();
            Query query = session.createQuery("from Dashboard where user_id = ?1");
            query.setParameter(1, userId);
            List<Dashboard> dashboards = query.getResultList();
            transaction.commit();
            return dashboards.size() > 0 ? dashboards.get(0) : null;
        }
    }

}
