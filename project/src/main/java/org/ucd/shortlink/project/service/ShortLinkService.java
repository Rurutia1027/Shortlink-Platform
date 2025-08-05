package org.ucd.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;
import org.ucd.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkPageRespDTO;

import java.util.List;

/**
 * Short link service layer
 */
public interface ShortLinkService extends IService<ShortLinkDO> {
    /**
     * Create short link
     *
     * @param requestParam create short link object params
     * @return short link creation response info
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);

    /**
     * Page query short link items
     *
     * @param requestParam request parameters
     * @return return paging short link response
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam);

    /**
     * Query group count each short link group
     *
     * @param requestParam request parameters
     * @return list of group cnt for each query gid
     */
    List<ShortLinkGroupCountQueryRespDTO> listGroupShortLinkCount(@RequestParam List<String> requestParam);

    /**
     * Update Short Link request
     *
     * @param requestParam update short link request
     */
    void updateShortLink(ShortLinkUpdateReqDTO requestParam);

    /**
     * Short link redirect
     * @param shortUri short link suffix
     * @param request http request
     * @param response http response
     */
    void restoreUrl(String shortUri, HttpServletRequest request, HttpServletResponse response);
}
