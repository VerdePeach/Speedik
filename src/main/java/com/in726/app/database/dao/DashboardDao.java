package com.in726.app.database.dao;

import com.in726.app.model.Dashboard;
import com.in726.app.model.dto.DashboardDto;

public interface DashboardDao {
    String saveDashboard(Dashboard dashboard);

    //    void updateDashboard(Dashboard dashboard);
//    void deleteDashboard(Dashboard dashboard);
//    Dashboard findByUserId(int userId);
    DashboardDto getByUserId(int userId);
    Dashboard getByUserIdDash(int userId);
}
