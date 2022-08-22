package com.myapp.lms.admin.model;

import lombok.Data;

@Data
public class MemberParam extends CommonParam{

    String userId;

    /*
    limit 0, 10 --> pageIndex : 1
    limit 10, 10 --> pageIndex : 2
    limit 20, 30    --> pageIndex : 3
     */

}
