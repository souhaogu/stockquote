package com.soustock.stockquote.sync.job;


import com.soustock.stockquote.sync.common.BaseSyncTask;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Created by xuyufei on 2015/9/15.
 * implement job interface
 * to run all crawl tasks
 */
public final class SyncJobDetail implements Job {

    private List<BaseSyncTask> tasks = new ArrayList<>(8);

    public SyncJobDetail(){
        ListableBeanFactory context  = new FileSystemXmlApplicationContext("classpath:spring_root.xml");
        Map<String, BaseSyncTask> taskMap = context.getBeansOfType(BaseSyncTask.class);
        for (BaseSyncTask task: taskMap.values()){
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
        for (BaseSyncTask task : tasks) {
            task.run();
        }
    }
}
