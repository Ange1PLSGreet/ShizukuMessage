package org.cookiebyte.dev.thread;

import org.cookiebyte.dev.announce.log.UnionLogInterface;
import org.cookiebyte.dev.announce.thread.EventThreadInterface;

import java.util.ArrayList;
import java.util.List;

public class EventThreadInterfaceImpl extends Thread implements EventThreadInterface, UnionLogInterface {

    public boolean status = true;

    public final List<Object> evList = new ArrayList<>();

    public Object event;
    @Override
    public void run() {
        super.run();
        AddEvent(event);
    }

    @Override
    public void AddEvent(Object event) {
        if (status) {
            log.info("Event Execute: " + event.getClass() + "\nThread: " + Thread.currentThread().getName());
            try{
                Object ev = event.getClass().getDeclaredConstructor().newInstance();
                synchronized (evList){
                    evList.add(ev);
                }
            } catch (Exception e){
                log.info(e.getMessage());
            }
        } else {
            log.info("Thread interrupt!");
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 获取事件列表的方法
     * @return List
     */
    public List<Object> GetEvList(){
         if (evList != null){
             return evList;
         } else {
             log.info("Get evList Failed! May Be Multi-Thread Happened Question!");
             return null;
         }
    }
}
