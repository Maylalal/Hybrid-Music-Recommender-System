package top.wangruns.trackstacking.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import top.wangruns.trackstacking.model.Review;
import top.wangruns.trackstacking.model.TrendingSong;
import top.wangruns.trackstacking.service.SearchService;
import top.wangruns.trackstacking.service.UserService;
import top.wangruns.trackstacking.utils.OneDayOneWord;
import top.wangruns.trackstacking.utils.Static;

@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;
	@Autowired
	private UserService userService;
	
	/**
	 * 
	 * @param request
	 * @param keyword
	 * @param mode
	 * mode =0 :音乐搜索 ;
	 * mode =1 :用户所示 ;
	 * mode =2 :评论搜索 ;
	 * mode =null :用户及以下身份搜索
	 * @return
	 */
	@RequestMapping(value = "searchFrameLoad.do",method = { RequestMethod.GET })
	public ModelAndView searchFrameLoad(HttpServletRequest request,String keyword,String mode) {
		ModelAndView modelAndView=new ModelAndView();
		//管理员搜索
		if(mode!=null && userService.isHasPrivilege(request)) {
			int modeInt=Integer.parseInt(mode);
			if(modeInt==0) {
				//歌曲搜索
				modelAndView.setViewName("songManageSearchFrame");
				List<TrendingSong> songManageSearchList=searchService.getSearchSongWithCollectionFlag(request,keyword);
				modelAndView.addObject("songManageSearchList",songManageSearchList);
				if(songManageSearchList.size()==0) {
					modelAndView.addObject("oneDayOneWord","下落不明");
				}else {
					modelAndView.addObject("oneDayOneWord",OneDayOneWord.getOneDayOneWord(Static.SEARCH_WORD_ARRAY));
				}
			}else if(modeInt==1) {
				//用户搜索
				modelAndView.setViewName("userManageSearchFrame");
				
			}else {
				//评论搜索
				modelAndView.setViewName("reviewManageSearchFrame");
				List<Review> reviewManageSearchList=searchService.getSearchReview(keyword);
				modelAndView.addObject("reviewManageSearchList",reviewManageSearchList);
				if(reviewManageSearchList.size()==0) {
					modelAndView.addObject("oneDayOneWord","下落不明");
				}else {
					modelAndView.addObject("oneDayOneWord",OneDayOneWord.getOneDayOneWord(Static.SEARCH_WORD_ARRAY));
				}
				
			}
		}else {
			//普通用户及以下搜索
			modelAndView.setViewName("searchFrame");
			List<TrendingSong> searchSongList=searchService.getSearchSongWithCollectionFlag(request,keyword);
			modelAndView.addObject("searchSongList",searchSongList);
			if(searchSongList.size()==0) {
				modelAndView.addObject("oneDayOneWord","下落不明");
			}else {
				modelAndView.addObject("oneDayOneWord",OneDayOneWord.getOneDayOneWord(Static.SEARCH_WORD_ARRAY));
			}
			
		}
		
		return modelAndView;
	}
	

}
