package org.cookiebyte.dev.announce.thread;

/**
 * 事件线程
 * @Dev Ange1PLSGreet
 */
public interface EventThreadInterface {

    /**
     * 添加一个事件，放到线程中去处理
     * @param event Object
     */
    public void AddEvent(Object event);

}
