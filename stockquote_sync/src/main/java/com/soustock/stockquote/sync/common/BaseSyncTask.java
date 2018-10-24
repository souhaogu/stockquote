package com.soustock.stockquote.sync.common;

import com.soustock.stockquote.exception.BusinessException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by xuyufei on 2015/9/13.
 * 抓取任务的基础类
 */
public abstract class BaseSyncTask {

    private final static Log logger = LogFactory.getLog(BaseSyncTask.class);

    /**
     * 运行方法
     */
    public void run(){
        try {
            logger.info(String.format("Task:[%s] is beginning...", getTaskName()));
            long startTime = System.currentTimeMillis();
            process();
            long hastTime = System.currentTimeMillis() - startTime;
            logger.info(String.format("Task:[%s] is finished，and hasted %d ms", getTaskName(), hastTime));
        } catch (Exception e) {
            logger.error("A error occured when running, the detail cause is :" + e.getMessage(), e);
        }
    }

    protected abstract void process() throws BusinessException;

    /**
     * 任务名称
     * @return
     */
    public abstract String getTaskName();

    /**
     * 执行顺序
     */
    public abstract int getExecuteOrder();

}
