package com.myapp.lms.admin.dto;

import com.myapp.lms.admin.entity.Banner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BannerDto {
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

    public String getRegDtText(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return regDt != null ? regDt.format(formatter) : "";
    }

    public static BannerDto of(Banner banner){
        return BannerDto.builder()
                .id(banner.getId())
                .bannerName(banner.getBannerName())
                .fileName(banner.getFileName())
                .urlFileName(banner.getUrlFileName())
                .link(banner.getLink())
                .target(banner.getTarget())
                .rank(banner.getRank())
                .usingYn(banner.isUsingYn())
                .regDt(banner.getRegDt())
                .build()
                ;
    }

    public static List<BannerDto> of(List<Banner> banners){
        if(banners == null) return null;
        List<BannerDto> bannerList = new ArrayList<>();
        for(Banner x : banners){
            bannerList.add(BannerDto.of(x));
        }
        return bannerList;
    }
}
