package com.sip.ams.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sip.ams.entities.Article;
import com.sip.ams.entities.Provider;
import com.sip.ams.repositories.ArticleRepository;
import com.sip.ams.repositories.ProviderRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping({ "/articles" })
public class ArticleController {
	private final ArticleRepository articleRepository;
	private final ProviderRepository providerRepository;
	private final Path root = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/images/article");

	@Autowired
	public ArticleController(ArticleRepository articleRepository, ProviderRepository providerRepository) {
		this.articleRepository = articleRepository;
		this.providerRepository = providerRepository;
	}

	/*
	 * @Autowired private ArticleRepository articleRepository;
	 * 
	 * @Autowired private ProviderRepository providerRepository;
	 */
	@GetMapping("/list")
	public List<Article> getAllArticles() {
		return (List<Article>) articleRepository.findAll();
	}

	@PostMapping("/add")
	public Article createArticle(@RequestParam(name = "provider", required = false) Long p,
			@RequestParam("label") String label, @RequestParam("price") float price,
			@RequestParam("imageFile") MultipartFile[] files, @RequestParam("imageName") String imageName)
			throws IOException {

		Provider provider = providerRepository.findById(p)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + p));

		StringBuilder fileName = new StringBuilder();
		MultipartFile file = files[0];
		Path fileNameAndPath = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/images/article",
				file.getOriginalFilename());
		fileName.append(file.getOriginalFilename());
		try {
			Files.write(fileNameAndPath, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Article article = new Article(label, price, provider, fileName.toString());

		articleRepository.save(article);

		return article;

	}

	@PutMapping("/{articleId}")
	public Article updateArticle(@PathVariable(value = "articleId") Long articleId,
			 @RequestParam("label") String label,
			@RequestParam("price") float price,
			
			 @RequestParam("imageFile") MultipartFile[] files,
				@RequestParam("imageName") String imageName
			) {
		
		return articleRepository.findById(articleId).map(article -> {
			// STEP 1 : delete Old Image from server
			String OldImageName = article.getPicture();

			////////
			try {
				File f = new File(this.root + "/" + OldImageName); // file to be delete
				if (f.delete()) // returns Boolean value
				{
					System.out.println(f.getName() + " deleted"); // getting and printing the file name
				} else {
					System.out.println("failed");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			/////// END STEP 1
			StringBuilder fileName = new StringBuilder();
			MultipartFile file = files[0];
			Path fileNameAndPath = Paths.get(
					System.getProperty("user.dir") + "/src/main/resources/static/images/article",
					file.getOriginalFilename());
			fileName.append(file.getOriginalFilename());
			try {
				Files.write(fileNameAndPath, file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		/*	Provider provider = providerRepository.findById(p)
					.orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + p));
			article.setProvider(provider);*/
			article.setPrice(price);
			article.setLabel(label);
			article.setPicture(fileName.toString());
			
			return articleRepository.save(article);
		}).orElseThrow(() -> new IllegalArgumentException("ArticleId " + articleId + "not found"));
	}

	@DeleteMapping("/{articleId}")
	public ResponseEntity<?> deleteArticle(@PathVariable(value = "articleId") Long articleId) {
		return articleRepository.findById(articleId).map(article -> {
			articleRepository.delete(article);
			//
			try {
				File f = new File(this.root + "/" + article.getPicture()); // file to be delete
				if (f.delete()) // returns Boolean value
				{
					System.out.println(f.getName() + " deleted"); // getting and printing the file name
				} else {
					System.out.println("failed");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new IllegalArgumentException("Article not found with id " + articleId));
	}

	@GetMapping("/{articleId}")
	public Article getArticle(@PathVariable Long articleId) {
		Optional<Article> a = articleRepository.findById(articleId);
		return a.get();
	}
}