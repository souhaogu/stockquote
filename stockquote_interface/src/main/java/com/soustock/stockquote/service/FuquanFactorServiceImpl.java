package com.soustock.stockquote.service;


import com.soustock.stockquote.dao.FuquanFactorDao;
import com.soustock.stockquote.exception.BusinessException;
import com.soustock.stockquote.po.DayQuoteCdtVo;
import com.soustock.stockquote.po.FuquanFactorPo;
import com.soustock.stockquote.utils.ListUtity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by xuyufei on 2018/10/24.
 */
@Service("fuquanFactorService")
public class FuquanFactorServiceImpl implements FuquanFactorService {

    @Autowired
    private FuquanFactorDao fuquanFactorDao;

    @Override
    public double getFuquanFactorOfCurrent(String stockCode) throws BusinessException {
        DayQuoteCdtVo dayQuoteCdtVo = new DayQuoteCdtVo();
        dayQuoteCdtVo.setStockCode(stockCode);
        dayQuoteCdtVo.setRecentlyCount(1);
        List<FuquanFactorPo> fuquanFactorPoList = fuquanFactorDao.getFuquanFactorsByStockCodeCount(dayQuoteCdtVo);
        if (ListUtity.isNullOrEmpty(fuquanFactorPoList)){
            return fuquanFactorPoList.get(0).getFactor();
        } else {
            throw new BusinessException(String.format("股票:[%s]查找最新复权因子失败.", stockCode));
        }
    }

    @Override
    public String getMaxDateOfStockFuquan(String stockCode) {
        return fuquanFactorDao.getMaxDateOfStockFuquan(stockCode);
    }

    @Override
    public void insertFuquanFactors(List<FuquanFactorPo> fuquanFactorPos) {
        fuquanFactorDao.insertFuquanFactors(fuquanFactorPos);
    }
}
