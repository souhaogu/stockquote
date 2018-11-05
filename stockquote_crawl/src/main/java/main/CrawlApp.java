package main;

import com.soustock.stockquote.crawl.job.CrawlJobDetail;
import com.soustock.stockquote.exception.BusinessException;
import com.soustock.stockquote.utils.pinyin4j.IllegalPinyinException;
import com.soustock.stockquote.utils.pinyin4j.PinyinConverter;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.TimeZone;

/**
 * Created by xuyufei on 2016/3/6.
 */
public class CrawlApp {

    /**
     * 程序入口方法
     * @param args
     */
    private static Scheduler sched = null;
    public static void main( String[] args ) throws IllegalPinyinException, BusinessException {
//        //String str = "st长沙市长";
//        String str = "*st浦发";
//        System.out.println(str);
//        PinyinConverter converter = new PinyinConverter();
//        System.out.println((converter.getPinyin(str)));
//        System.out.println((converter.getPinyinFirstSpell(str)));

        try {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
            sched = startSchedule();
            Thread.sleep(1000);
            System.out.println("The program is running...");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Scheduler startSchedule() throws SchedulerException {
        // 通过SchedulerFactory获取一个调度器实例
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        //创建任务
        JobDetail dailyJob = JobBuilder.newJob(CrawlJobDetail.class)
                .withIdentity("timing crawl job")
                .build();
        //创建触发器, 每24小时触发一次，首次触发为启动后5秒
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInHours(24)
                .repeatForever();
        SimpleTrigger trigger = TriggerBuilder
                .newTrigger()
                .startAt(DateBuilder.futureDate(5, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(simpleScheduleBuilder)
                .build();

        //register job
        sched.scheduleJob(dailyJob, trigger);
        //start job
        sched.start();

        return sched;
    }
}
