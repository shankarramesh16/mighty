package com.team.mighty.controller;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team.mighty.constant.MightyAppConstants;
import com.team.mighty.domain.AdminUser;
import com.team.mighty.domain.MightyFeaturedPlaylist;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.AdminInstrumentService;

@Controller
public class MightyFeaturedPlaylistController {

private static final MightyLogger logger = MightyLogger.getLogger(MightyFeaturedPlaylistController.class);
	
	@Autowired
	private AdminInstrumentService adminInstrumentServiceImpl;
	
	@RequestMapping(value = "/addDevicePlaylist")
	public String addDevicePlaylistHandler(HttpServletRequest request,Map<String,Object> map) throws Exception {
		logger.debug("Adding Mighty Device Featured Playlist");
				 return "addMightyFeaturedPlaylist";
	}
	
	
	@RequestMapping(value = "/devicePlaylist", method = RequestMethod.GET)
	public String devicePlaylistInfoHandler(Map<String,Object> map) throws Exception {
		logger.debug("Getting devicePlaylist inform");
		List<MightyFeaturedPlaylist> mightyFeaturedPlayList=adminInstrumentServiceImpl.getMightyFeaturedPlaylist();
		map.put("mightyFeaturedPlayList", mightyFeaturedPlayList);
		return "mightyFeaturedPlaylistInfo";
	}
	
	@RequestMapping(value = "/mightyFeaturedPlaylist",method=RequestMethod.POST)
	public String mightyFeaturedPlaylistHandler(HttpServletRequest request,HttpSession session,Map<String,Object> map,RedirectAttributes redirectAttributes) throws Exception {
		logger.debug("In submitting mighty Featured Playlist details");
		
		AdminUser admin=(AdminUser) session.getAttribute("adminUser");
		String playlistId=request.getParameter("playlist");
		String playlistName=request.getParameter("playlistName");
		String playlistUrl=request.getParameter("playlistUrl");
		String genre=request.getParameter("genre");
		
		MightyFeaturedPlaylist mightyFeaturedPlaylist=new MightyFeaturedPlaylist();
		mightyFeaturedPlaylist.setPlaylist_ID(playlistId);
		mightyFeaturedPlaylist.setPlaylist_Name(playlistName);
		mightyFeaturedPlaylist.setPlaylist_URL(playlistUrl);
		mightyFeaturedPlaylist.setGenre(genre);
		mightyFeaturedPlaylist.setCreated_Date(new Date(System.currentTimeMillis()));
		mightyFeaturedPlaylist.setUpdated_Date(new Date(System.currentTimeMillis()));
		mightyFeaturedPlaylist.setCreated_By(admin.getLoginId());
		mightyFeaturedPlaylist.setUpdated_By(admin.getLoginId());
		mightyFeaturedPlaylist.setStatus(MightyAppConstants.IND_A);
		
		try{
			adminInstrumentServiceImpl.insertMightyFeaturedPlaylistDetails(mightyFeaturedPlaylist);
			redirectAttributes.addFlashAttribute("status", "Mighty Featured Playlist added successfully..");
			}catch(Exception ex){
				redirectAttributes.addFlashAttribute("status", "Mighty Featured Playlist addition Failed..");
				logger.error(ex);
			}
		
			return "redirect:/devicePlaylist";
	}
}
