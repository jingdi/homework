package io.homework.topk.model;

import lombok.Data;

/**
 * Created by jingdi on 2019/5/29
 */
@Data
public class Url {

    private String uri;

    private Long count;

    public Url(String uri, Long count) {
        this.uri = uri;
        this.count = count;
    }

    public void increase() {
        this.count += 1;
    }
}
