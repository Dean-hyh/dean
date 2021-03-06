package com.dean.demo.service.impl;

import com.dean.demo.dao.RaffleActiveWinnerDao;
import com.dean.demo.enums.DbankExceptionEnum;
import com.dean.demo.exception.DbankException;
import com.dean.demo.pojo.DO.RaffleActiveWinner;
import com.dean.demo.pojo.DTO.RaffleActiveWinnerDTO;
import com.dean.demo.service.RaffleActiveWinnerService;
import com.dean.demo.utils.BeanHelper;
import com.dean.demo.utils.CurrentLineInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Dean
 * @version 1.0
 * @date 2019/12/19 17:12
 */
@Service
public class RaffleActiveWinnerServiceImpl implements RaffleActiveWinnerService {

    @Autowired
    private RaffleActiveWinnerDao raffleActiveWinnersDao;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 添加获奖名单
     * @param winnerList
     */
    @Override
    public void insertWinner(List<RaffleActiveWinnerDTO> winnerList) {
        List<RaffleActiveWinner> raffleActiveWinners = BeanHelper.copyWithCollection(winnerList, RaffleActiveWinner.class);
        for (RaffleActiveWinner raffleActiveWinner : raffleActiveWinners) {
            try {
                int insert = raffleActiveWinnersDao.insert(raffleActiveWinner);
            } catch (Exception e) {
                logger.error("新增获奖名单失败" + CurrentLineInfo.getFileAddress());
                throw new DbankException(DbankExceptionEnum.INSERT_OPERATION_FAIL);
            }
        }
    }

    /**
     * 查询中奖名单
     * @param raffleAcriveId
     * @return
     */
    @Override
    public List<RaffleActiveWinnerDTO> downloadWinnerListByRaffleActiveId(Long raffleAcriveId) {
        List<RaffleActiveWinner> winnersList = raffleActiveWinnersDao.selectByRaffleAcriveId(raffleAcriveId);
        try {
            List<RaffleActiveWinnerDTO> winnerDTOList = BeanHelper.copyWithCollection(winnersList, RaffleActiveWinnerDTO.class);
            return winnerDTOList;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DbankException(DbankExceptionEnum.DATA_TRANSFER_ERROR);
        }
    }

    /**
     * 测试
     * @return
     */
    @Override
    public List<Map<String, Object>> queryTest(Map params) {
        return raffleActiveWinnersDao.queryTest(params);
    }

    @Override
    public Map<String, Object> queryTest1(Map<String, Object> params) {
        return raffleActiveWinnersDao.queryTest1(params);
    }
}
