package com.hl.affiliate_project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "banners")
public class Banners {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "title")
	private String title;
	private String imageUrl;
	private String description;
	private Boolean status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt = new Date();
}
