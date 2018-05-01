package com.soustock.stockquote.crawl.job;

import com.soustock.stockquote.crawl.common.BaseCrawlTask;
import com.soustock.stockquote.utils.DateUtity;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.*;


/**
 * Created by xuyufei on 2015/9/15.
 * implement job interface
 * to run all crawl tasks
 */
public final class CrawlJobDetail implements Job {

    private List<BaseCrawlTask> tasks = new ArrayList<>(8);

    public CrawlJobDetail(){
        ListableBeanFactory context  = new FileSystemXmlApplicationContext("classpath:spring_root.xml");
        Map<String, BaseCrawlTask> taskMap = context.getBeansOfType(BaseCrawlTask.class);
        for (BaseCrawlTask task: taskMap.values()){
            tasks.add(task);
        }
//        Collections.sort(tasks, new Comparator<BaseCrawlTask>() {
//                                        @Override
//                                        public int compare(BaseCrawlTask o1, BaseCrawlTask o2) {
//                                            int ret = o1.getExecuteOrder() - o2.getExecuteOrder();
//                                            if (ret==0){
//                                                return 0;
//                                            }
//                                            else {
//                                                return ret > 0 ? 1: -1;
//                                            }
//                                        }
//                                    });
        Collections.sort(tasks, (o1, o2) -> {
            int ret = o1.getExecuteOrder() - o2.getExecuteOrder();
            if (ret==0) {
                return 0;
            }
            else {
                return ret > 0 ? 1: -1;
            }
        });

    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        for (BaseCrawlTask task : tasks) {
            task.run();
        }
    }
}
