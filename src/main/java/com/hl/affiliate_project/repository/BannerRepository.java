package com.hl.affiliate_project.repository;

import com.hl.affiliate_project.model.Banners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * data access layer (JPA)
 * */
// Integer 代表的是 JPA 实体 Banners 的主键（Primary Key, ID）类型。
@Repository
public interface BannerRepository extends JpaRepository<Banners, Integer>  {
	List<Banners> findByStatusOrderByIdDesc(Boolean status);
}
