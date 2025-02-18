package com.hl.affiliate_project.service;

import com.hl.affiliate_project.model.Banners;
import com.hl.affiliate_project.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Banner service class
 * business logic layer (data processing)
 *
 * */

@Service
public class BannerService {
	@Autowired
	private BannerRepository bannerRepository;

	// get all banners
	public List<Banners> getAllBanners() {
		return bannerRepository.findByStatusOrderByIdDesc(true);
	}

	// get active banners
	public List<Banners> getActiveBanners() {
		return bannerRepository.findByStatusOrderByIdDesc(true);
	}

	// get banner by id
	public Optional<Banners> getBannerById(int id) {
		return bannerRepository.findById(id);
	}

	// add new banner
	public Banners addBanner(Banners banner) {
		return bannerRepository.save(banner);
	}

	// update banner
	public Banners updateBanner(int id, Banners bannerDetails) {
		return bannerRepository.findById(id).map(banner -> {
			banner.setTitle(bannerDetails.getTitle());
			banner.setImageUrl(bannerDetails.getImageUrl());
			banner.setDescription(bannerDetails.getDescription());
			return bannerRepository.save(banner);
		}).orElseThrow(() -> new RuntimeException("Banner not found with id: " + id));
	}

	// delete banner
	public void deleteBanner(int id) {
		bannerRepository.deleteById(id);
	}
}