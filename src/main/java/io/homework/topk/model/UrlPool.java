package io.homework.topk.model;

import lombok.Data;
import java.util.PriorityQueue;

/**
 * Created by jingdi on 2019/5/29
 */
@Data
public class UrlPool {

    private PriorityQueue<Url> queue = new PriorityQueue<Url>(Constants.MAX_SIZE, (o1, o2) -> (int) (o1.getCount() - o2.getCount()));

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
