package com.test.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.test.web.dto.input.GetPbocDataListReqDto;
import com.test.web.dto.output.GetPbocDataListResDto;

public interface PbocDataService {

    IPage getPbocDataListPage(GetPbocDataListReqDto reqDto);
}
