package com.hl.affiliate_project.controller;

import com.hl.affiliate_project.model.Banners;
import com.hl.affiliate_project.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


/**
 *
 * handle HTTP requests (API entry point)
 *
 * */

@RestController
@RequestMapping("/api/banners")
public class BannerController {
	@Autowired
	private BannerService bannerService;

	// fetch all active banners
	@GetMapping
	public List<Banners> getActiveBanners() {
		return bannerService.getActiveBanners();
	}

	// fetch specific banner by id
	@GetMapping("/{id}")
	public Banners getBannerById(@PathVariable int id) {
		Optional<Banners> banner = bannerService.getBannerById(id);
		// return banner.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
		return banner.orElse(null);
	}

	// add new banner
	@PostMapping
	public Banners addBanner(@RequestBody Banners banner) {
		return bannerService.addBanner(banner);
	}

	// update existing banner
	@PutMapping("/{id}")
	public Banners updateBanner(@PathVariable int id, @RequestBody Banners banner) {
		return bannerService.updateBanner(id, banner);
	}

	// delete existing banner
	@DeleteMapping("/{id}")
	public void deleteBanner(@PathVariable int id) {
		bannerService.deleteBanner(id);
	}
}
