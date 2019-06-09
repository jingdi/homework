package io.homework.topk.model;

import lombok.Data;

import java.util.PriorityQueue;

/**
 * Created by jingdi on 2019/5/29
 */
@Data
public class UrlPool {

    /**
     * 比较函数返回值为int，直接返回差值可能导致越界
     */
    private PriorityQueue<Url> queue = new PriorityQueue<Url>(Constants.MAX_SIZE, (o1, o2) -> {
        Long count1 = o1.getCount();
        Long count2 = o2.getCount();

        if (count1 > count2) {
            return 1;
        }
        if (count1 < count2) {
            return -1;
        }

        return 0;
    });

    public void add(Url url) {
        if (queue.size() < Constants.MAX_SIZE) {
            queue.add(url);
        } else {
            Url u = queue.peek();
            if (u != null && u.getCount() < url.getCount() ) {
                queue.poll();
                queue.add(url);
            }
        }
    }
}
