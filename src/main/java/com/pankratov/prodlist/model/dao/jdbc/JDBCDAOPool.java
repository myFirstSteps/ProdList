package com.pankratov.prodlist.model.dao.jdbc;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.*;

public class JDBCDAOPool<T extends JDBCDAOObject> {

    private static final Logger log = LogManager.getLogger(JDBCDAOPool.class);
    private final T adam;
    private int maxCount;
    private int minCount;
    private long idleTime;
    private long timeOut;
    private ConcurrentLinkedQueue<T> DAOsPool;
    private final AtomicInteger DAOsCount = new AtomicInteger(1);

    public JDBCDAOPool(T adam) {
        this.adam = adam;
        ServletContext context = adam.getContext();
        String contentName = adam.getDAOName();
        log.debug(String.format("Getting %s pool", contentName));
        if (DAOsPool == null) {
            synchronized (JDBCUserDAO.class) {
                if (DAOsPool == null) {
                    if (context.getInitParameter(String.format("%s_POOL_SIZE", contentName)) != null) {
                        maxCount = Integer.parseInt(context.getInitParameter(String.format("%s_POOL_SIZE", contentName)));
                    } else {
                        maxCount = 5;
                        log.info(String.format("В дескрипторе не задан максимальный размер пула для JDBC_%s_DAO", contentName));
                    }
                    if (context.getInitParameter(String.format("%s_POOL_TMEOUT", contentName)) != null) {
                        timeOut = Long.parseLong(context.getInitParameter(String.format("%s_POOL_TMEOUT", contentName)));
                    } else {
                        timeOut = 100;
                        log.info(String.format("В дескрипторе не задано время ожидания перед расширением JDBC_%s_DAO пула", contentName));
                    }
                    if (context.getInitParameter(String.format("%s_MIN_POOL_SIZE", contentName)) != null) {
                        minCount = Integer.parseInt(context.getInitParameter(String.format("%s_MIN_POOL_SIZE", contentName)));
                    } else {
                        minCount = 2;
                        log.info(String.format("В дескрипторе не задан минимальный размер пула для JDBC_%s_DAO", contentName));
                    }
                    if (context.getInitParameter(String.format("%s_IDLE_TIME", contentName)) != null) {
                        idleTime = Integer.parseInt(context.getInitParameter(String.format("%s_IDLE_TIME", contentName)));
                    } else {
                        idleTime = 6000;
                        log.info(String.format("В дескрипторе не задано время бездействия JDBC_%s_DAO перед удалением из пула", contentName));
                    }
                    if (minCount < 1) {
                        minCount = 1;
                    }
                    DAOsPool = new ConcurrentLinkedQueue<>();
                    DAOsPool.offer(adam);
                }
            }
        }
    }

    T get() throws Exception  {
        T instance = DAOsPool.poll();
        try {
            if (instance == null && (DAOsCount.get() < maxCount)) {
                Thread.sleep(timeOut);
                instance = DAOsPool.poll();
                if (instance == null) {
                    synchronized (DAOsCount) {
                        if (DAOsCount.get() < maxCount) {
                            log.debug(String.format("DAOCount (%s)<maxCount (%s) so new instance creating\n", DAOsCount.get(), maxCount));
                            instance = (T) adam.newInstance();
                            DAOsCount.incrementAndGet();
                        }
                    }
                }
            } else {
               log.debug(String.format("%s is waiting for instance\n", Thread.currentThread()));
                while (instance == null) {
                    /*Здесь хорошо бы усыплять поток до освобождения ресурса. Нужно продумать
                     вариант с очередью (чтобы обеспечить правильную очередность выдачи ресурсов).
                     */
                    instance = DAOsPool.poll();
                }
                log.debug(String.format("Thread %s get instance %s\n",Thread.currentThread(),instance));
            }
            if ((System.currentTimeMillis()
                    - instance.getOfferTime()) > idleTime) {
                instance.setPensioner(true);
                log.debug(String.format("Instance %s is pensioner\n",instance));
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка получения %s из пула :", adam), e); throw e;
        }
        return instance;

    }
    void put(T instance) throws SQLException {
        if (instance!=adam&&instance.isPensioner()) {
            synchronized (DAOsCount) {
                if (DAOsCount.get() > minCount) {
                    DAOsCount.decrementAndGet();
                    instance.getConnection().close();
                    log.debug(String.format("Connection for: %s closed. Pool size is %s\n", instance, DAOsPool.size()));
                    return;
                }
            }
        }
        instance.setOfferTime(System.currentTimeMillis());
        DAOsPool.offer(instance);
        log.debug(String.format("Returning: %s to pool. Pool size is %s, DAOSCount=%s\n", instance, DAOsPool.size(),DAOsCount));
    }
    int size() {
        return DAOsCount.get();
    }

}
