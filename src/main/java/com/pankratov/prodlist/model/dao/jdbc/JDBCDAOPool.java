/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.model.dao.jdbc;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author pankratov
 */
public class JDBCDAOPool<T extends JDBCDAOObject> {

    private static Logger log = LogManager.getLogger(JDBCDAOPool.class);
    private T adam;
    private int maxCount;
    private int minCount;
    private long idleTime;
    private long timeOut;
    private ConcurrentLinkedQueue<T> DAOsPool;
    private final AtomicInteger DAOsCount = new AtomicInteger(0);

    public JDBCDAOPool(T adam) {
        this.adam = adam;
        ServletContext context = adam.getContext();
        String contentName = adam.getDAOName();
        System.out.println(String.format("Getting %s pool", contentName));
        if (DAOsPool == null) {
            synchronized (JDBCUserDAO.class) {
                if (DAOsPool == null) {
                    if (context.getInitParameter(String.format("%s_POOL_SIZE", contentName)) != null) {
                        maxCount = Integer.parseInt(context.getInitParameter(String.format("%s_POOL_SIZE", contentName)));
                    } else {
                        maxCount = 5;
                        log.info(String.format("В дескрипторе не задан максимальный размер пула для JDBC%sDAO", contentName));
                    }
                    if (context.getInitParameter(String.format("%s_POOL_TMEOUT", contentName)) != null) {
                        timeOut = Long.parseLong(context.getInitParameter(String.format("%s_POOL_TMEOUT", contentName)));
                    } else {
                        timeOut = 100;
                        log.info(String.format("В дескрипторе не задано время ожидания перед расширением JDBC%sDAO пула", contentName));
                    }
                    if (context.getInitParameter(String.format("%s_MIN_POOL_SIZE", contentName)) != null) {
                        minCount = Integer.parseInt(context.getInitParameter(String.format("%s_MIN_POOL_SIZE", contentName)));
                    } else {
                        minCount = 2;
                        log.info(String.format("В дескрипторе не задан минимальный размер пула для JDBC%sDAO", contentName));
                    }
                    if (context.getInitParameter(String.format("%s_IDLE_TIME", contentName)) != null) {
                        idleTime = Integer.parseInt(context.getInitParameter(String.format("%s_IDLE_TIME", contentName)));
                    } else {
                        idleTime = 6000;
                        log.info(String.format("В дескрипторе не задано время бездействия JDBC%sDAO перед удалением из пула", contentName));
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

    T get() {
        try {
            T instance = DAOsPool.poll();
            if (instance == null && (DAOsCount.get() < maxCount)) {
                Thread.sleep(timeOut);
                instance = DAOsPool.poll();
                if (instance == null) {
                    synchronized (DAOsCount) {
                        if (DAOsCount.get() < maxCount) {
                            System.out.println("DAOCount is:" + DAOsCount.get() + "<" + maxCount);
                            instance = (T) adam.newInstance();
                            System.out.println("so i creating");
                            DAOsCount.incrementAndGet();
                        }

                    }
                }
                System.out.println("pool count is: " + DAOsCount.get());
                System.out.println("Now in pool: " + DAOsPool.size());

            } else {
                System.out.println("Waiting for jdbc" + Thread.currentThread());
                while (instance == null) {
                    /*Здесь хорошо бы усыплять поток до освобождения ресурса. Нужно продумать
                     вариант с очередью (что бы обеспечить правильную очередность выдачи ресурсов).
                     */
                    instance = DAOsPool.poll();
                }
                System.out.println("Oh, I get it!!" + Thread.currentThread());
            }
            log.debug("i am created" + instance);

            if ((System.currentTimeMillis()
                    - instance.getOfferTime()) > idleTime) {
                instance.setPensioner(true);
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка получения %s из пула :", adam), e);
        }
        return null;

    }

    void put(T instance) throws SQLException {
        if (instance.isPensioner()) {
            synchronized (DAOsCount) {
                if (DAOsCount.get() > minCount) {
                    DAOsCount.decrementAndGet();
                    instance.getConnection().close();
                }
                return;
            }
        }
        instance.setOfferTime(System.currentTimeMillis());
        System.out.println("Pool Size:" + DAOsPool.size());
        DAOsPool.offer(instance);
    }

    int size() {
        return DAOsCount.get();
    }

}
