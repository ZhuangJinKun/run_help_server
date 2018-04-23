package com.zjk.service.impl;

import com.zjk.common.DefList;
import com.zjk.dao.SportsDao;
import com.zjk.dao.UserDao;
import com.zjk.entity.*;
import com.zjk.service.SportsService;
import com.zjk.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

@Service
public class SportsServiceImpl implements SportsService {

	@Autowired
	private SportsDao sportsDao;

	@Autowired
	private UserDao userDao;

	public int insert(SportsData sportsData) {
		if (sportsData.getrGDList() == null || sportsData.getrGDList().size() == 0) {
			return 2;
		}
		// 插入总的数据
		boolean bool = sportsDao.insert(sportsData);
		if (!bool) {
			return 0;
		}
		// 插入粒度数据
		int sDId = sportsDao.selectMaxSDId();
		for (SportsGranularityData sportsGranularityData : sportsData.getrGDList()) {
			sportsGranularityData.setsDId(sDId);
			bool = sportsDao.insertGranularity(sportsGranularityData);
			if (!bool) {
				return 0;
			}
		}
		// 更新排行榜单的数据
		RankingVersion rankingVersion = sportsDao.queryRVByUId(sportsData.getuId());
		if (rankingVersion != null) {
			if (DateUtil.isYesterday(rankingVersion.getTime())) {
				rankingVersion.setDistance(sportsData.getDistance());
			} else {
				rankingVersion.setDistance(rankingVersion.getDistance() + sportsData.getDistance());
			}
			rankingVersion.setTime(DateUtil.stringToDate(DateUtil.dateToString(new Date())));
			sportsDao.updateRV(rankingVersion);
		} else {
			rankingVersion = new RankingVersion();
			rankingVersion.setuId(sportsData.getuId());
			UserInfo userInfo = userDao.queryByUId(sportsData.getuId());
			if (userInfo != null) {
				rankingVersion.setUserName(userInfo.getUserName());
				rankingVersion.setHeadUrl(userInfo.getHeadUrl());
			} else {
				rankingVersion.setUserName(DefList.EMPTY);
				rankingVersion.setHeadUrl(DefList.EMPTY);
			}
			rankingVersion.setRanking(0);
			rankingVersion.setTime(DateUtil.stringToDate(DateUtil.dateToString(new Date())));
			rankingVersion.setDistance(sportsData.getDistance());
			sportsDao.insertRV(rankingVersion);
		}
		return 1;
	}

	public ArrayList<FallThreshold> selectFallThreshold() {
		return sportsDao.selectFallThreshold();
	}

	public ArrayList<SportsData> querySportsData(int uId) {
		ArrayList<SportsData> sportsDatas = sportsDao.querySportsData(uId);
		if (sportsDatas == null) {
			return null;
		}
		for (SportsData data : sportsDatas) {
			data.setrGDList(sportsDao.querySportsGranularityData(data.getsDId()));
		}
		return sportsDatas;
	}

	public ArrayList<RankingVersion> queryRankingVersion() {
		ArrayList<RankingVersion> rankingVersions = sportsDao.queryRankingVersion();
		if (rankingVersions == null) {
			return null;
		}
		// 过滤一天前的数据
		for (RankingVersion rankingVersion : rankingVersions) {
			if (DateUtil.isYesterday(rankingVersion.getTime())) {
				rankingVersion.setDistance(0);
			}
		}
		Comparator<RankingVersion> comparator = (r1, r2) -> {
			if (r1.getDistance() < r2.getDistance()) {
				return 1;
			}
			return -1;
		};
		rankingVersions.sort(comparator);
		for (int index = 0; index < rankingVersions.size(); index++) {
			rankingVersions.get(index).setRanking(index + 1);
		}

		return sportsDao.queryRankingVersion();
	}

	public ArrayList<TrainingSuggestData> queryUserSportsSuggesttedData(int uId) {
		return sportsDao.queryUserSportsSuggestedData(uId);
	}

	public ArrayList<SportsSuggestion> querySportsSuggestion(int uId) {
		return sportsDao.querySportsSuggestion(uId);
	}

	public boolean updateFallThreshold(double fallThreshold) {
		return sportsDao.updateFallThreshold(fallThreshold);
	}

	public TrainingSuggestData queryTrainingSuggestData(int uId) {
		return sportsDao.queryTrainingSuggestData(uId);
	}
}
