package com.myapp.lms.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerInput extends CommonParam{
    Long id;
    String bannerName;
    String fileName;
    String urlFileName;
    String link;
    String target;
    long rank;
    boolean usingYn;
    LocalDateTime regDt;

    long seq;
    long totalCount;
}
