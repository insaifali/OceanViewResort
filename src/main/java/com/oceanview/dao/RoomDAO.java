package com.oceanview.dao;

import com.oceanview.model.RoomInfo;
import java.util.List;

public interface RoomDAO {
    double getRate(String roomType) throws Exception;
    List<RoomInfo> getAllRooms() throws Exception;
}